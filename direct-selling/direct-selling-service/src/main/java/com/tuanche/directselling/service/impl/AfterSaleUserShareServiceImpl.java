package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.api.AfterSaleUserShareService;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleUserShareWriteMapper;
import com.tuanche.directselling.model.AfterSaleUserShare;
import com.tuanche.directselling.utils.StatusCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * @author：HuangHao
 * @CreatTime 2021-07-21 10:46
 */
@Service(retries = 0,timeout = 20000)
public class AfterSaleUserShareServiceImpl implements AfterSaleUserShareService {

    @Autowired
    AfterSaleUserShareWriteMapper afterSaleUserShareWriteMapper;

    /**
     * 新增分享记录
     * @author HuangHao
     * @CreatTime 2021-07-21 10:52
     * @param record
     * @return com.tuanche.arch.web.model.TcResponse
     */
    public TcResponse insert(AfterSaleUserShare record){
        if(record == null || record.getActivityId() == null){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"活动ID不能为空");
        }
        if(StringUtils.isEmpty(record.getUserWxUnionId())){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"用户微信UnionId不能为空");
        }
        String pageUrl = record.getPageUrl();
        if (!StringUtil.isEmpty(pageUrl) && pageUrl.length() > 256) {
            record.setPageUrl(pageUrl.substring(0, 255));
        }
        afterSaleUserShareWriteMapper.insert(record);
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(),StatusCodeEnum.SUCCESS.getText());
    }
}
