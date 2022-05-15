package com.tuanche.directselling.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.tuanche.arch.commonmodel.paging.PageableDto;
import com.tuanche.arch.commonmodel.resp.TcRpcResponse;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.api.AfterSaleOrderWechatworkConcactService;
import com.tuanche.directselling.api.WechatWorkService;
import com.tuanche.directselling.dto.AfterSaleOrderRecordDto;
import com.tuanche.directselling.model.AfterSaleOrderWechatworkConcact;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.wechatwork.api.WechatworkContactSearchService;
import com.tuanche.wechatwork.dto.TcWechatworkWwContactRelationOuterDto;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class WechatWorkServiceImpl implements WechatWorkService {

    //企微客服名称关键字
    @Value("${after_sale_wechatwork_name_keyword}")
    private String after_sale_wechatwork_name_keyword;
    @Reference
    private WechatworkContactSearchService wechatworkContactSearchService;
    @Reference
    private AfterSaleOrderWechatworkConcactService afterSaleOrderWechatworkConcactService;
    @Autowired
    ExecutorService executorService;

    /**
     * @description : 获取用户对应企微客服关系，分页默认10条
     * @author : fxq
     * @param :
     * @return :
     * @date : 2021/12/10 14:12
     */
    public List<AfterSaleOrderWechatworkConcact> searchContact (TcWechatworkWwContactRelationOuterDto wechatworkConcactDto) {
        List<AfterSaleOrderWechatworkConcact> list = new ArrayList<>();
        TcRpcResponse<PageableDto<TcWechatworkWwContactRelationOuterDto>> pageableDtoTcRpcResponse = wechatworkContactSearchService.searchWwContact(wechatworkConcactDto);
        CommonLogUtil.addInfo(null, "售后特卖购买商品 电商渠道匹配企微客服 searchWwContact", JSON.toJSONString(pageableDtoTcRpcResponse));
        if (pageableDtoTcRpcResponse!=null && pageableDtoTcRpcResponse.getData()!=null && pageableDtoTcRpcResponse.getData().getList()!=null) {
            pageableDtoTcRpcResponse.getData().getList().forEach(v->{
                if (v.getWwUserName() !=null && v.getWwUserName().indexOf(after_sale_wechatwork_name_keyword)>-1) {
                    AfterSaleOrderWechatworkConcact dto = JSON.parseObject(JSON.toJSONString(v), AfterSaleOrderWechatworkConcact.class);
                    dto.setId(null);
                    list.add(dto);
                }
            });
        }
        return list;
    }

    @Override
    public void setAfterSaleOrderWechatworkConcact(AfterSaleOrderRecordDto afterSaleOrderRecord) {
        executorService.execute(() -> {
            try {
                CommonLogUtil.addInfo(null, "售后特卖购买商品 电商渠道匹配企微客服 ing", JSON.toJSONString(afterSaleOrderRecord));
                if (StringUtil.isEmpty(afterSaleOrderRecord.getOrderCode()) || StringUtil.isEmpty(afterSaleOrderRecord.getUserWxUnionId()) || afterSaleOrderRecord.getActivityId()==null) {
                    CommonLogUtil.addInfo(null, "售后特卖购买商品 电商渠道匹配企微客服 return", "参数为空");
                    return;
                }
                AfterSaleOrderWechatworkConcact concact = new AfterSaleOrderWechatworkConcact();
                concact.setOrderCode(afterSaleOrderRecord.getOrderCode());
                List<AfterSaleOrderWechatworkConcact> wechatworkConcactList = afterSaleOrderWechatworkConcactService.getList(concact);
                if (CollectionUtils.isEmpty(wechatworkConcactList)) {
                    TcWechatworkWwContactRelationOuterDto relationOuterDto = new TcWechatworkWwContactRelationOuterDto();
                    relationOuterDto.setUnionid(afterSaleOrderRecord.getUserWxUnionId());
                    relationOuterDto.setPageNo(1);
                    relationOuterDto.setPageSize(30);
                    List<AfterSaleOrderWechatworkConcact> wechatworkConcacts = searchContact(relationOuterDto);
                    if (CollectionUtils.isNotEmpty(wechatworkConcacts)) {
                        wechatworkConcacts.forEach(v->{
                            v.setActivityId(afterSaleOrderRecord.getActivityId());
                            v.setOrderCode(afterSaleOrderRecord.getOrderCode());
                            v.setUserWxUnionId(afterSaleOrderRecord.getUserWxUnionId());
                        });
                        afterSaleOrderWechatworkConcactService.addList(wechatworkConcacts);
                    }
                }
            } catch (Exception e) {
                CommonLogUtil.addError(null, "售后特卖购买商品 电商渠道匹配企微客服 error", e);
            }
        });
    }


}
