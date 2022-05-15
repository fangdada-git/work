package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.tuanche.directselling.api.*;
import com.tuanche.directselling.constant.Constants;
import com.tuanche.directselling.dto.ResultMapDto;
import com.tuanche.directselling.job.AfterSaleActivityStatisticsJob;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleActivityChannelStatisticsReadMapper;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleActivityStatisticsReadMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleActivityChannelStatisticsWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleActivityStatisticsWriteMapper;
import com.tuanche.directselling.model.AfterSaleActivityChannelStatistics;
import com.tuanche.directselling.model.AfterSaleActivityStatistics;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.utils.GlobalConstants;
import org.apache.ibatis.annotations.MapKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author：HuangHao
 * @CreatTime 2021-08-20 11:04
 */
@Service
public class AfterSaleActivityStatisticsServiceImpl implements AfterSaleActivityStatisticsService {

    @Autowired
    AfterSaleOrderRecordService afterSaleOrderRecordService;
    @Autowired
    AfterSaleActivityStatisticsReadMapper afterSaleActivityStatisticsReadMapper;
    @Autowired
    AfterSaleActivityChannelStatisticsReadMapper afterSaleActivityChannelStatisticsReadMapper;
    @Autowired
    AfterSaleActivityChannelStatisticsWriteMapper afterSaleActivityChannelStatisticsWriteMapper;
    @Autowired
    AfterSaleActivityStatisticsWriteMapper afterSaleActivityStatisticsWriteMapper;
    @Reference
    AfterSaleCouponService afterSaleCouponService;
    @Reference
    AfterSaleRewardRecordService afterSaleRewardRecordService;
    @Reference
    AfterSaleUserService afterSaleUserService;
    @Autowired
    AfterSaleUserBrowseService afterSaleUserBrowseService;


    /**
     * 活动数据统计
     * @author HuangHao
     * @CreatTime 2021-08-20 11:00
     * @param activityIds
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivityStatistics>
     */
    public void activityStatistics(List<Integer> activityIds){
        String logKeyWord = AfterSaleActivityStatisticsJob.LOG_KEY_WORD+"，活动统计";
        if(activityIds==null || activityIds.size()==0){
            CommonLogUtil.addInfo(null, logKeyWord+",活动ID列表（activityIds）为空，不统计");
            return;
        }
        List<AfterSaleActivityStatistics> list = afterSaleOrderRecordService.activityStatistics(activityIds);
        CommonLogUtil.addInfo(null, logKeyWord+",开始，活动："+ JSON.toJSONString(list));
        if(CollectionUtils.isEmpty(list)) {
            for (Integer activityId : activityIds) {
                AfterSaleActivityStatistics activityStatistics = new AfterSaleActivityStatistics();
                activityStatistics.setActivityId(activityId);
                setAfterSaleActivityStatistics(activityStatistics);
                list.add(activityStatistics);
            }
        }else if(list.size()<activityIds.size()){
            for (AfterSaleActivityStatistics statistics : list) {
                Integer activityId = statistics.getActivityId();
                if(!activityIds.contains(activityId)){
                    AfterSaleActivityStatistics activityStatistics = new AfterSaleActivityStatistics();
                    activityStatistics.setActivityId(activityId);
                    setAfterSaleActivityStatistics(activityStatistics);
                    list.add(activityStatistics);
                }
            }
        }
        int size = list.size()*2;
        Map<String, ResultMapDto>  giftCertificatesIssuedMap = afterSaleRewardRecordService.giftCertificatesIssuedMap(activityIds);
        Map<String, ResultMapDto>  idsMap = afterSaleActivityStatisticsReadMapper.getActivityStatisticIdMap(activityIds);
        Map<String, ResultMapDto>  uvMap = afterSaleUserService.getUvMap(activityIds);
        Map<String, ResultMapDto>  activityBrowseTotalMap = afterSaleUserBrowseService.activityBrowseTotalMap(activityIds);
        Map<String, ResultMapDto>  writeOffGiftVoucherMap = afterSaleCouponService.getWriteOffGiftVoucherMap(activityIds);
        Map<String, ResultMapDto>  rewardTotalMap = afterSaleRewardRecordService.getRewardTotalMap(activityIds);
        boolean havgift = CollectionUtils.isEmpty(giftCertificatesIssuedMap)?false:true;
        boolean idsVal = CollectionUtils.isEmpty(idsMap)?false:true;
        boolean activityBrowseTotalVal = CollectionUtils.isEmpty(activityBrowseTotalMap)?false:true;
        boolean uvVal = CollectionUtils.isEmpty(uvMap)?false:true;
        boolean writeOffGiftVoucherVal = CollectionUtils.isEmpty(writeOffGiftVoucherMap)?false:true;
        boolean rewardTotalVal = CollectionUtils.isEmpty(rewardTotalMap)?false:true;
        List<AfterSaleActivityStatistics> updateList = new ArrayList<>(size);
        List<AfterSaleActivityStatistics> addList = new ArrayList<>(size);
        for (AfterSaleActivityStatistics statistics : list) {
            String activityIdStr = statistics.getActivityId().toString();
            //奖励总额
            ResultMapDto rewardTotalResult = null;
            if(rewardTotalVal && (rewardTotalResult=rewardTotalMap.get(activityIdStr))!=null){
                BigDecimal rewardTotal = new BigDecimal(rewardTotalResult.getMapValue());
                statistics.setRewardTotal(rewardTotal);
            }else{
                statistics.setRewardTotal(BigDecimal.ZERO);
            }
            //净营收=推广卡售出金额-奖励总额-推广卡主动退款金额-推广卡被动退款金额
            BigDecimal promotionCardNetRevenue = statistics.getPromotionCardAmount()
                    .subtract(statistics.getRewardTotal())
                    .subtract(statistics.getPromotionCardRefundsAmountActive())
                    .subtract(statistics.getPromotionCardRefundsAmountPassive());
            statistics.setPromotionCardNetRevenue(promotionCardNetRevenue.setScale(2, BigDecimal.ROUND_HALF_UP));
            //发放的礼品券数
            ResultMapDto gift = null;
            if(havgift && (gift=giftCertificatesIssuedMap.get(activityIdStr))!=null){
                Integer giftCertificatesTotal = Integer.valueOf(gift.getMapValue());
                statistics.setGiftCertificatesTotal(giftCertificatesTotal);
            }else{
                statistics.setGiftCertificatesTotal(Constants.zero);
            }
            //UV
            ResultMapDto uvResult = null;
            if(uvVal && (uvResult=uvMap.get(activityIdStr))!=null){
                Integer uv = Integer.valueOf(uvResult.getMapValue());
                statistics.setUv(uv);
            }else{
                statistics.setUv(Constants.zero);
            }
            //浏览量
            ResultMapDto activityBrowseTotalResult = null;
            if(activityBrowseTotalVal && (activityBrowseTotalResult=activityBrowseTotalMap.get(activityIdStr))!=null){
                Integer browseTotal = Integer.valueOf(activityBrowseTotalResult.getMapValue());
                statistics.setBrowseTotal(browseTotal);
            }else{
                statistics.setBrowseTotal(Constants.zero);
            }

            //礼品券核销数量
            ResultMapDto writeOffGiftVoucherResult = null;
            if(uvVal && (writeOffGiftVoucherResult=writeOffGiftVoucherMap.get(activityIdStr))!=null){
                Integer writeOffGiftVoucherTotal = Integer.valueOf(writeOffGiftVoucherResult.getMapValue());
                statistics.setWriteOffGiftVoucherTotal(writeOffGiftVoucherTotal);
            }else{
                statistics.setWriteOffGiftVoucherTotal(Constants.zero);
            }
            ResultMapDto idsResult = null;
            if(idsVal && (idsResult=idsMap.get(activityIdStr))!=null){
                Integer id = Integer.valueOf(idsResult.getMapValue());
                statistics.setId(id);
               //更新
                updateList.add(statistics);
            }else{
                //新增
                addList.add(statistics);
            }
        }
        CommonLogUtil.addInfo(null, logKeyWord+",新增数量："+ addList.size());
        if(addList.size()>0){
            afterSaleActivityStatisticsWriteMapper.insertBatch(addList);
        }
        CommonLogUtil.addInfo(null, logKeyWord+",更新数量："+ updateList.size());
        if(updateList.size()>0){
            afterSaleActivityStatisticsWriteMapper.updateBatch(updateList);
        }
        /*}else{
            CommonLogUtil.addInfo(null, logKeyWord+",未获取到活动，不统计");
        }*/
    }

    private void setAfterSaleActivityStatistics(AfterSaleActivityStatistics statistics){
        statistics.setPromotionCardTotal(0);
        statistics.setPromotionCardAmount(BigDecimal.ZERO);
        statistics.setPackageCardTotal(0);
        statistics.setPackageCardAmount(BigDecimal.ZERO);
        statistics.setAutonomousFissionTotal(0);
        statistics.setPromotionCardRefundsTotalActive(0);
        statistics.setPromotionCardRefundsAmountActive(BigDecimal.ZERO);
        statistics.setPromotionCardRefundsTotalPassive(0);
        statistics.setPromotionCardRefundsAmountPassive(BigDecimal.ZERO);
        statistics.setPromotionCardsWaitingReleaseTotal(0);
        statistics.setPromotionCardsWrittenOffTotal(0);
        statistics.setPromotionCardsWrittenOffAmount(BigDecimal.ZERO);
        statistics.setVisitorsTotal(0);
        statistics.setLostUserTotal(0);
        statistics.setKeepOnRecordUserTotal(0);
        statistics.setKeepOnRecordFinishUserTotal(0);
        statistics.setPrimaryFissionTotal(0);
        statistics.setBeyondPrimaryFissionTotal(0);
    }

    /**
     * 活动渠道数据统计
     * @author HuangHao
     * @CreatTime 2021-9-13 11:33:27
     * @param activityIds
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivityStatistics>
     */
    public void activityChannelStatistics(List<Integer> activityIds) {
        String logKeyWord = AfterSaleActivityStatisticsJob.LOG_KEY_WORD + "，活动渠道统计";
        List<AfterSaleActivityChannelStatistics> list = afterSaleOrderRecordService.activityChannelStatistics(activityIds);
        CommonLogUtil.addInfo(null, logKeyWord+",开始，活动："+ JSON.toJSONString(list));
        if(CollectionUtils.isEmpty(list)){
            CommonLogUtil.addInfo(null, logKeyWord+",无统计信息");
            return;
        }
        int size = list.size()*2;
        Map<String, ResultMapDto> idsMap = afterSaleActivityChannelStatisticsReadMapper.getActivityChanelStatisticIdMap(list);
        boolean idsVal = CollectionUtils.isEmpty(idsMap)?false:true;
        List<AfterSaleActivityChannelStatistics> updateList = new ArrayList<>(size);
        List<AfterSaleActivityChannelStatistics> addList = new ArrayList<>(size);
        if(idsVal){
            for (AfterSaleActivityChannelStatistics channelStatistics : list) {
                String key = GlobalConstants.underlineStitching(channelStatistics.getActivityId(),channelStatistics.getChannel().toString());
                if(idsMap.get(key) != null){
                    channelStatistics.setId(Integer.valueOf(idsMap.get(key).getMapValue()));
                    updateList.add(channelStatistics);
                }else{
                    addList.add(channelStatistics);
                }
            }
            CommonLogUtil.addInfo(null, logKeyWord+",新增数量："+ addList.size());
            if(addList.size()>0){
                afterSaleActivityChannelStatisticsWriteMapper.insertBatch(addList);
            }
            CommonLogUtil.addInfo(null, logKeyWord+",更新数量："+ updateList.size());
            if(updateList.size()>0){
                afterSaleActivityChannelStatisticsWriteMapper.updateBatch(updateList);
            }
        }else{
            afterSaleActivityChannelStatisticsWriteMapper.insertBatch(list);
        }

    }

    /**
     * 根据活动ID获取渠道统计信息
     * @author HuangHao
     * @CreatTime 2021-09-13 16:33
     * @param activityId
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivityChannelStatistics>
     */
    public List<AfterSaleActivityChannelStatistics> getListByActivityId( Integer activityId){
        if(activityId==null){
            return null;
        }
        return afterSaleActivityChannelStatisticsReadMapper.getListByActivityId(activityId);
    }

}
