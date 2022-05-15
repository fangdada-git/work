package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.api.AfterSaleCouponService;
import com.tuanche.directselling.api.AfterSaleOrderRecordService;
import com.tuanche.directselling.api.AfterSaleRewardRecordService;
import com.tuanche.directselling.api.AfterSaleUserRewardStatisticsService;
import com.tuanche.directselling.constant.Constants;
import com.tuanche.directselling.dto.AfterSaleOrderRecordDto;
import com.tuanche.directselling.dto.AfterSaleOrderRecordUserTypeDto;
import com.tuanche.directselling.dto.AfterSaleUserRewardStatisticsDto;
import com.tuanche.directselling.dto.ResultMapDto;
import com.tuanche.directselling.job.AfterSaleActivityStatisticsJob;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleUserRewardStatisticsReadMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleUserRewardStatisticsWriteMapper;
import com.tuanche.directselling.model.AfterSaleAgent;
import com.tuanche.directselling.model.AfterSaleOrderRecord;
import com.tuanche.directselling.model.AfterSaleUserRewardStatistics;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.utils.AfterSaleConstants;
import com.tuanche.directselling.utils.GlobalConstants;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.model.CsCompany;
import com.tuanche.merchant.api.payment.IPaymentService;
import com.tuanche.merchant.business.dto.request.PageableRequestDto;
import com.tuanche.merchant.business.dto.response.BaseResponseDto;
import com.tuanche.merchant.pojo.dto.commodity.PageableDto;
import com.tuanche.merchant.pojo.dto.enums.ResultEnum;
import com.tuanche.merchant.pojo.dto.request.payment.OrderPaymentQueryRequestDto;
import com.tuanche.merchant.pojo.dto.response.payment.PaymentResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author：HuangHao
 * @CreatTime 2021-08-18 11:34
 */
@Service
public class AfterSaleUserRewardStatisticsServiceImpl implements AfterSaleUserRewardStatisticsService {

    @Reference
    AfterSaleRewardRecordService afterSaleRewardRecordService;
    @Reference
    AfterSaleOrderRecordService afterSaleOrderRecordService;
    @Reference
    AfterSaleCouponService afterSaleCouponService;
    @Autowired
    AfterSaleUserRewardStatisticsReadMapper afterSaleUserRewardStatisticsReadMapper;
    @Autowired
    AfterSaleUserRewardStatisticsWriteMapper afterSaleUserRewardStatisticsWriteMapper;
    @Autowired
    AfterSaleAgentServiceImpl afterSaleAgentServiceImpl;
    @Reference
    CompanyBaseService companyBaseService;
    @Reference
    private IPaymentService iPaymentService;

    /**
     * 用户奖励统计
     *
     * @param activityIds
     * @return void
     * @author HuangHao
     * @CreatTime 2021-08-19 16:11
     */
    @Override
    public void userRewardStatistics(List<Integer> activityIds) {
        String logKeyWord = AfterSaleActivityStatisticsJob.LOG_KEY_WORD + "，用户奖励统计";
        CommonLogUtil.addInfo(null, logKeyWord + ",开始，activityIds：" + JSON.toJSONString(activityIds));
        List<AfterSaleUserRewardStatistics> list = afterSaleRewardRecordService.userRewardStatistics(activityIds);
        CommonLogUtil.addInfo(null, logKeyWord + ",统计信息：" + JSON.toJSONString(list));
        if (!CollectionUtils.isEmpty(list)) {
            int size = list.size() * 2;
            List<AfterSaleUserRewardStatistics> addList = new ArrayList<>(size);
            List<AfterSaleUserRewardStatistics> updateList = new ArrayList<>(size);
            Map<String, ResultMapDto> shareStatMap = afterSaleRewardRecordService.getShareTotalMap(list);

            Map<String, ResultMapDto> writeOffGgiftCertificatesUserMap = afterSaleCouponService.getWriteOffGgiftCertificatesUserMap(list);
            Map<String, Integer> idsMap = getStatisticsIdMap(list);
            boolean havShareStatVal = CollectionUtils.isEmpty(shareStatMap) ? false : true;
            boolean havIdsVal = CollectionUtils.isEmpty(idsMap) ? false : true;
            boolean havWriteOffGgiftCertificatesUserVal = CollectionUtils.isEmpty(writeOffGgiftCertificatesUserMap) ? false : true;
            for (AfterSaleUserRewardStatistics statistics : list) {
                String key = GlobalConstants.underlineStitching(statistics.getActivityId(), statistics.getUserWxUnionId());
                //邀请人数
                ResultMapDto rewardStatistic = null;
                if (havShareStatVal && (rewardStatistic = shareStatMap.get(key)) != null) {
                    statistics.setInviteesNum(Integer.valueOf(rewardStatistic.getMapValue()));
                } else {
                    statistics.setInviteesNum(Constants.zero);
                }

                //是否核销礼品券
                ResultMapDto writeOffGgiftResult = null;
                if(havWriteOffGgiftCertificatesUserVal && (writeOffGgiftResult=writeOffGgiftCertificatesUserMap.get(key)) != null){
                    statistics.setWriteOffGiftCertificatesTotal(AfterSaleConstants.GiftCertificatesStstus.TYPE_2.getCode());
                }else if(statistics.getWriteOffGiftCertificatesTotal()==1){
                    statistics.setWriteOffGiftCertificatesTotal(AfterSaleConstants.GiftCertificatesStstus.TYPE_1.getCode());
                }else {
                    statistics.setWriteOffGiftCertificatesTotal(AfterSaleConstants.GiftCertificatesStstus.TYPE_0.getCode());
                }
                Integer id = null;
                //更新
                if(havIdsVal && (id=idsMap.get(key)) != null){
                    statistics.setId(id);
                    updateList.add(statistics);
                //新增
                }else{
                    CommonLogUtil.addInfo(null, logKeyWord+",新增数量："+ addList.size());
                    addList.add(statistics);
                }
            }
            CommonLogUtil.addInfo(null, logKeyWord+",新增数量："+ addList.size());
            if(addList.size()>0){
                afterSaleUserRewardStatisticsWriteMapper.batchInsert(addList);
            }
            CommonLogUtil.addInfo(null, logKeyWord+",更新数量："+ updateList.size());
            if(updateList.size()>0){
                afterSaleUserRewardStatisticsWriteMapper.batchupdate(updateList);
            }

        }
    }

    /**
     * 获取用户的id Map
     * @author HuangHao
     * @CreatTime 2021-08-19 16:11
     * @param list
     * @return java.util.Map<java.lang.String,java.lang.Integer>
     */
    @Override
    public Map<String,Integer> getStatisticsIdMap(List<AfterSaleUserRewardStatistics> list){
        List<AfterSaleUserRewardStatistics> statisticsIds = afterSaleUserRewardStatisticsReadMapper.getStatisticsIds(list);
        Map<String,Integer> resultMap = null;
        if (!CollectionUtils.isEmpty(statisticsIds)){
            resultMap = new HashMap<>(statisticsIds.size()*2);
            for (AfterSaleUserRewardStatistics rewardStatistics : statisticsIds) {
                String key = GlobalConstants.underlineStitching(rewardStatistics.getActivityId(),rewardStatistics.getUserWxUnionId());
                resultMap.put(key, rewardStatistics.getId());
            }
        }
        return resultMap;
    }

    @Override
    public PageResult<AfterSaleUserRewardStatisticsDto> getStatisticsListByPage(PageResult<AfterSaleUserRewardStatisticsDto> pageResult, AfterSaleUserRewardStatisticsDto rewardStatistics) {
        PageHelper.startPage(pageResult.getPage(), pageResult.getLimit());
//        rewardStatistics = getSearchList(rewardStatistics);
        List<AfterSaleUserRewardStatisticsDto> list = afterSaleUserRewardStatisticsReadMapper.getStatisticsListByPage(rewardStatistics);
        Set<Integer> dealerIds = new HashSet<>();
        List<String> orderNos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(v -> {
                dealerIds.add(v.getDealerId());
                orderNos.add(v.getOrderCode());
                if (!StringUtil.isEmpty(v.getUserPhone()) && v.getUserPhone().length() == 11) {
                    v.setUserPhone(v.getUserPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
                }
            });
        }

        Map<Integer, CsCompany> csCompanyMap = null;
        if (!dealerIds.isEmpty()) {
            csCompanyMap = companyBaseService.getCompanyMap(new ArrayList<>(dealerIds));
        }
        Map<String, PaymentResponseDto> payMap = new HashMap<>(16);
        if (!orderNos.isEmpty()) {
            //订单流水信息
            PageableRequestDto requestDto = new PageableRequestDto();
            OrderPaymentQueryRequestDto paymentQueryRequestDto = new OrderPaymentQueryRequestDto();
            paymentQueryRequestDto.setOrderCodeList(orderNos);
            requestDto.setQuery(paymentQueryRequestDto);
            requestDto.setPageIndex(1);
            requestDto.setPageSize(orderNos.size());

            BaseResponseDto<PageableDto<PaymentResponseDto>> pageableDtoBaseResponseDto = iPaymentService.listOrderPayment(requestDto);
            if (pageableDtoBaseResponseDto.getCode().equals(ResultEnum.SUCCESS.getCode()) && org.apache.commons.collections4.CollectionUtils.isNotEmpty(pageableDtoBaseResponseDto.getData().getList())) {
                pageableDtoBaseResponseDto.getData().getList().forEach(v -> {
                    payMap.put(v.getOrderCode(), v);
                });
            }
        }
        for (AfterSaleUserRewardStatisticsDto dto : list) {
            if (csCompanyMap != null) {
                CsCompany company = csCompanyMap.get(dto.getDealerId());
                if (company != null) {
                    dto.setDealerName(company.getCompanyName());
                }
            }
            PaymentResponseDto paymentResponseDto = payMap.get(dto.getOrderCode());
            if (paymentResponseDto != null) {
                dto.setTradeNo(paymentResponseDto.getPayment3SerialNo());
            }
        }
        PageInfo<AfterSaleUserRewardStatisticsDto> page = new PageInfo<>(list);
        pageResult.setCount(page.getTotal());
        pageResult.setCode("0");
        pageResult.setMsg("");
        pageResult.setData(list);
        return pageResult;
    }
    /**
      * @description : C端用户奖励对账统计
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/8/23 20:34
      */
    @Override
    public Map<String, Object> getStatisticsCountMap(AfterSaleUserRewardStatisticsDto rewardStatistics) {
//        rewardStatistics = getSearchList(rewardStatistics);
        Map<String, Object> map = afterSaleUserRewardStatisticsReadMapper.getStatisticsCountMap(rewardStatistics);
        return map;
    }

    private AfterSaleUserRewardStatisticsDto getSearchList (AfterSaleUserRewardStatisticsDto rewardStatistics) {
        if (!StringUtil.isEmpty(rewardStatistics.getUserPhone())) {
            List<AfterSaleUserRewardStatistics> searchList = new ArrayList<>();
            //手机号查订单
            AfterSaleOrderRecordDto orderRecordDto = new AfterSaleOrderRecordDto();
            orderRecordDto.setUserPhone(rewardStatistics.getUserPhone());
            List<AfterSaleOrderRecord> orderRecordList = afterSaleOrderRecordService.getAfterSaleOrderRecordList(orderRecordDto);
            if (!CollectionUtils.isEmpty(orderRecordList)) {
                orderRecordList.forEach(v->{
                    AfterSaleUserRewardStatistics statistics = new AfterSaleUserRewardStatistics();
                    statistics.setActivityId(v.getActivityId());
                    statistics.setDealerId(v.getDealerId());
                    statistics.setUserWxUnionId(v.getUserWxUnionId());
                    searchList.add(statistics);
                });
            }
            //手机号查代理人
            AfterSaleAgent agent = new AfterSaleAgent();
            agent.setPhone(rewardStatistics.getUserPhone());
            List<AfterSaleAgent> agentList = afterSaleAgentServiceImpl.getAfterSaleAgentList(agent);
            if (!CollectionUtils.isEmpty(agentList)) {
                agentList.forEach(v->{
                    AfterSaleUserRewardStatistics statistics = new AfterSaleUserRewardStatistics();
                    statistics.setActivityId(v.getActivityId());
                    statistics.setDealerId(v.getDealerId());
                    statistics.setUserWxUnionId(v.getAgentWxUnionId());
                    searchList.add(statistics);
                });
            }
            rewardStatistics.setSearchList(searchList);
        }
        return rewardStatistics;
    }

}
