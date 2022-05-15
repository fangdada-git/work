package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.AfterSaleActivityChannelStatistics;
import com.tuanche.directselling.utils.GlobalConstants;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;


/**
 * 活动数据明细dto
 *
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/8/18 10:59
 **/
public class AfterSaleActivityDataDetailDto extends AfterSaleActivityDataBaseDto {


    /**
     * 打开页面用户数/点击人数/uv
     */
    private Integer uv;
    /**
     * 浏览数
     */
    private Integer browseTotal;

    /**
     * 到店人数
     */
    private Integer visitorsTotal;

    /**
     * 售出推广卡总数
     */
    private Integer promotionCardTotal;

    /**
     * 售出推广卡金额
     */
    private BigDecimal promotionCardAmount;

    /**
     * 售出套餐卡总数
     */
    private Integer packageCardTotal;

    /**
     * 售出套餐卡金额
     */
    private BigDecimal packageCardAmount;

    /**
     * 奖励总金额
     */
    private BigDecimal rewardTotal;
    /**
     * 推广卡退款总数-被动(自动退款备案用户数)
     */
    private Integer promotionCardRefundsTotalPassive;
    /**
     * 推广卡退款总数(主动+被动)
     */
    private Integer promotionCardRefundsTotal;

    /**
     * 推广卡退款金额(主动+被动)
     */
    private BigDecimal promotionCardRefundsAmount;

    /**
     * 推广卡净营收
     */
    private BigDecimal promotionCardNetRevenue;

    /**
     * 代理人1级裂变数
     */
    private Integer agentFissionTotalOne;

    /**
     * 代理人2级裂变数
     */
    private Integer agentFissionTotalTwo;
    /**
     * 1级裂变数（直接裂变数）
     */
    private Integer primaryFissionTotal;
    /**
     * 超过1级的裂变数（转介绍人数）
     */
    private Integer beyondPrimaryFissionTotal;

    /**
     * 自主裂变数（运营渠道数）
     */
    private Integer autonomousFissionTotal;

    /**
     * 流失客户数
     */
    private Integer lostUserTotal;
    /**
     * 备案人数
     */
    private Integer keepOnRecordUserTotal;

    /**
     * 发放的礼品券数
     */
    private Integer giftCertificatesTotal;

    /**
     * 核销礼品券总数
     */
    private Integer writeOffGiftVoucherTotal;

    /**
     * 线下兑换开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date offlineConvertStartTime;
    /**
     * 线下兑换结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date offlineConvertEndTime;
    /**
     * 待发券推广卡数
     */
    private Integer promotionCardsWaitingReleaseTotal;
    /**
     * 核销推广卡数
     */
    private Integer promotionCardsWrittenOffTotal;
    /**
     * 备案客户完成裂变任务数
     */
    private Integer keepOnRecordFinishUserTotal;
    /**
     * 推广转化率
     */
    private String promotionPercent;

    /**
     * 到店率
     */
    private String visitorsPercent;

    /**
     * 套餐转化率
     */
    private String packagePercent;

    /**
     * 退款率
     */
    private String refundPercent;
    /**
     * 流失客户数占比
     */
    private String lostUserTotalPercent;
    /**
     * 备案人数占比
     */
    private String keepOnRecordUserTotalPercent;
    /**
     * 备案客户完成裂变任务数占比
     */
    private String keepOnRecordFinishUserTotalPercent;

    /**
     * 1级裂变数（直接裂变数）占比
     */
    private String primaryFissionTotalPercent;
    /**
     * 超过1级的裂变数（转介绍人数）占比
     */
    private String beyondPrimaryFissionTotalPercent;
    /**
     * 核销推广卡率
     */
    private String promotionCardsWrittenOffTotalPercent;

    //活动渠道统计信息列表
    private List<AfterSaleActivityChannelStatisticsDto> channelList;

    //活动状态
    private String activityStatus;

    public String getActivityStatus() {
        String as = "";
        if(getSaleStartTime() != null && getOfflineConvertEndTime() != null){
            Date now = new Date();
            if(now.before(getSaleStartTime())){
                as ="未开始";
            }else if(now.after(getSaleStartTime()) && now.before(getOfflineConvertEndTime())){
                as = "进行中";
            }else{
                as = "已结束";
            }
        }
        this.activityStatus = as;
        return this.activityStatus;
    }

    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus;
    }

    public String getPromotionPercent() {
        if (getUv() == 0) {
            return BigDecimal.ZERO.toString()+"%";
        } else {
            //推广转化率  推广卡售卖数（包括退款） / 点击人数
            return GlobalConstants.percentConversion(getPromotionCardTotal(), getUv()).toString()+"%";
        }
    }

    public String getVisitorsPercent() {
        if (getPromotionCardTotal() > 0) {
            return GlobalConstants.percentConversion(getVisitorsTotal(), getPromotionCardTotal()).toString()+"%";
        }
        return BigDecimal.ZERO.toString()+"%";
    }
    public String getPackagePercent() {
        if(getVisitorsTotal() > 0){
            return GlobalConstants.percentConversion(getPackageCardTotal(), getVisitorsTotal()).toString()+"%";
        }
        return BigDecimal.ZERO.toString()+"%";
    }
    public String getRefundPercent() {
        if (getPromotionCardTotal() > 0) {
            return GlobalConstants.percentConversion(getPromotionCardRefundsTotal(), getPromotionCardTotal()).toString()+"%";
        }
        return BigDecimal.ZERO.toString()+"%";
    }

    public String getLostUserTotalPercent() {
        if (getPromotionCardTotal() > 0) {
            return GlobalConstants.percentConversion(getLostUserTotal(), getPromotionCardTotal()).toString()+"%";
        }
        return BigDecimal.ZERO.toString()+"%";
    }
    public String getKeepOnRecordUserTotalPercent() {
        if (getPromotionCardTotal() > 0) {
            return GlobalConstants.percentConversion(getKeepOnRecordUserTotal(), getPromotionCardTotal()).toString()+"%";
        }
        return BigDecimal.ZERO.toString()+"%";
    }

    public String getKeepOnRecordFinishUserTotalPercent() {
        if(getKeepOnRecordUserTotal() > 0){
            return GlobalConstants.percentConversion(getKeepOnRecordFinishUserTotal(), getKeepOnRecordUserTotal()).toString()+"%";
        }
        return BigDecimal.ZERO.toString()+"%";
    }

    public String getPrimaryFissionTotalPercent() {
        if(getPromotionCardTotal() > 0){
            return GlobalConstants.percentConversion(getPrimaryFissionTotal(), getPromotionCardTotal()).toString()+"%";
        }
        return BigDecimal.ZERO.toString()+"%";
    }

    public String getBeyondPrimaryFissionTotalPercent() {
        if(getPromotionCardTotal() > 0){
            return GlobalConstants.percentConversion(getBeyondPrimaryFissionTotal(), getPromotionCardTotal()).toString()+"%";
        }
        return BigDecimal.ZERO.toString()+"%";
    }

    public String getPromotionCardsWrittenOffTotalPercent() {
        if(getPromotionCardTotal() > 0){
            return GlobalConstants.percentConversion(getPromotionCardsWrittenOffTotal(), getPromotionCardTotal()).toString()+"%";
        }
        return BigDecimal.ZERO.toString()+"%";
    }

    public void setLostUserTotalPercent(String lostUserTotalPercent) {
        this.lostUserTotalPercent = lostUserTotalPercent;
    }

    public void setPromotionPercent(String promotionPercent) {
        this.promotionPercent = promotionPercent;
    }

    public void setVisitorsPercent(String visitorsPercent) {
        this.visitorsPercent = visitorsPercent;
    }

    public void setPackagePercent(String packagePercent) {
        this.packagePercent = packagePercent;
    }

    public void setRefundPercent(String refundPercent) {
        this.refundPercent = refundPercent;
    }

    public void setKeepOnRecordUserTotalPercent(String keepOnRecordUserTotalPercent) {
        this.keepOnRecordUserTotalPercent = keepOnRecordUserTotalPercent;
    }

    public void setKeepOnRecordFinishUserTotalPercent(String keepOnRecordFinishUserTotalPercent) {
        this.keepOnRecordFinishUserTotalPercent = keepOnRecordFinishUserTotalPercent;
    }

    public void setPrimaryFissionTotalPercent(String primaryFissionTotalPercent) {
        this.primaryFissionTotalPercent = primaryFissionTotalPercent;
    }

    public void setBeyondPrimaryFissionTotalPercent(String beyondPrimaryFissionTotalPercent) {
        this.beyondPrimaryFissionTotalPercent = beyondPrimaryFissionTotalPercent;
    }

    public void setPromotionCardsWrittenOffTotalPercent(String promotionCardsWrittenOffTotalPercent) {
        this.promotionCardsWrittenOffTotalPercent = promotionCardsWrittenOffTotalPercent;
    }

    public Integer getUv() {
        return uv;
    }

    public void setUv(Integer uv) {
        this.uv = uv;
    }

    public Integer getVisitorsTotal() {
        return visitorsTotal;
    }

    public void setVisitorsTotal(Integer visitorsTotal) {
        this.visitorsTotal = visitorsTotal;
    }

    public Integer getPromotionCardTotal() {
        return promotionCardTotal;
    }

    public void setPromotionCardTotal(Integer promotionCardTotal) {
        this.promotionCardTotal = promotionCardTotal;
    }

    public BigDecimal getPromotionCardAmount() {
        return promotionCardAmount;
    }

    public void setPromotionCardAmount(BigDecimal promotionCardAmount) {
        this.promotionCardAmount = promotionCardAmount;
    }

    public Integer getPackageCardTotal() {
        return packageCardTotal;
    }

    public void setPackageCardTotal(Integer packageCardTotal) {
        this.packageCardTotal = packageCardTotal;
    }

    public BigDecimal getPackageCardAmount() {
        return packageCardAmount;
    }

    public void setPackageCardAmount(BigDecimal packageCardAmount) {
        this.packageCardAmount = packageCardAmount;
    }

    public Integer getPromotionCardRefundsTotal() {
        return promotionCardRefundsTotal;
    }

    public void setPromotionCardRefundsTotal(Integer promotionCardRefundsTotal) {
        this.promotionCardRefundsTotal = promotionCardRefundsTotal;
    }

    public BigDecimal getPromotionCardRefundsAmount() {
        return promotionCardRefundsAmount;
    }

    public void setPromotionCardRefundsAmount(BigDecimal promotionCardRefundsAmount) {
        this.promotionCardRefundsAmount = promotionCardRefundsAmount;
    }

    public BigDecimal getPromotionCardNetRevenue() {
        return promotionCardNetRevenue;
    }

    public void setPromotionCardNetRevenue(BigDecimal promotionCardNetRevenue) {
        this.promotionCardNetRevenue = promotionCardNetRevenue;
    }

    public BigDecimal getRewardTotal() {
        return rewardTotal;
    }

    public void setRewardTotal(BigDecimal rewardTotal) {
        this.rewardTotal = rewardTotal;
    }

    public Integer getAgentFissionTotalOne() {
        return agentFissionTotalOne;
    }

    public void setAgentFissionTotalOne(Integer agentFissionTotalOne) {
        this.agentFissionTotalOne = agentFissionTotalOne;
    }

    public Integer getAgentFissionTotalTwo() {
        return agentFissionTotalTwo;
    }

    public void setAgentFissionTotalTwo(Integer agentFissionTotalTwo) {
        this.agentFissionTotalTwo = agentFissionTotalTwo;
    }

    public Integer getAutonomousFissionTotal() {
        return autonomousFissionTotal;
    }

    public void setAutonomousFissionTotal(Integer autonomousFissionTotal) {
        this.autonomousFissionTotal = autonomousFissionTotal;
    }

    public Integer getKeepOnRecordUserTotal() {
        return keepOnRecordUserTotal;
    }

    public void setKeepOnRecordUserTotal(Integer keepOnRecordUserTotal) {
        this.keepOnRecordUserTotal = keepOnRecordUserTotal;
    }

    public Integer getGiftCertificatesTotal() {
        return giftCertificatesTotal;
    }

    public void setGiftCertificatesTotal(Integer giftCertificatesTotal) {
        this.giftCertificatesTotal = giftCertificatesTotal;
    }

    public Integer getWriteOffGiftVoucherTotal() {
        return writeOffGiftVoucherTotal;
    }

    public void setWriteOffGiftVoucherTotal(Integer writeOffGiftVoucherTotal) {
        this.writeOffGiftVoucherTotal = writeOffGiftVoucherTotal;
    }

    public Date getOfflineConvertStartTime() {
        return offlineConvertStartTime;
    }

    public void setOfflineConvertStartTime(Date offlineConvertStartTime) {
        this.offlineConvertStartTime = offlineConvertStartTime;
    }

    public Date getOfflineConvertEndTime() {
        return offlineConvertEndTime;
    }

    public void setOfflineConvertEndTime(Date offlineConvertEndTime) {
        this.offlineConvertEndTime = offlineConvertEndTime;
    }

    public List<AfterSaleActivityChannelStatisticsDto> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<AfterSaleActivityChannelStatisticsDto> channelList) {
        this.channelList = channelList;
    }

    public Integer getPromotionCardsWaitingReleaseTotal() {
        return promotionCardsWaitingReleaseTotal;
    }

    public void setPromotionCardsWaitingReleaseTotal(Integer promotionCardsWaitingReleaseTotal) {
        this.promotionCardsWaitingReleaseTotal = promotionCardsWaitingReleaseTotal;
    }

    public Integer getKeepOnRecordFinishUserTotal() {
        return keepOnRecordFinishUserTotal;
    }

    public void setKeepOnRecordFinishUserTotal(Integer keepOnRecordFinishUserTotal) {
        this.keepOnRecordFinishUserTotal = keepOnRecordFinishUserTotal;
    }

    public Integer getPromotionCardRefundsTotalPassive() {
        return promotionCardRefundsTotalPassive;
    }

    public void setPromotionCardRefundsTotalPassive(Integer promotionCardRefundsTotalPassive) {
        this.promotionCardRefundsTotalPassive = promotionCardRefundsTotalPassive;
    }

    public Integer getPrimaryFissionTotal() {
        return primaryFissionTotal;
    }

    public void setPrimaryFissionTotal(Integer primaryFissionTotal) {
        this.primaryFissionTotal = primaryFissionTotal;
    }

    public Integer getBeyondPrimaryFissionTotal() {
        return beyondPrimaryFissionTotal;
    }

    public void setBeyondPrimaryFissionTotal(Integer beyondPrimaryFissionTotal) {
        this.beyondPrimaryFissionTotal = beyondPrimaryFissionTotal;
    }

    public Integer getPromotionCardsWrittenOffTotal() {
        return promotionCardsWrittenOffTotal;
    }

    public void setPromotionCardsWrittenOffTotal(Integer promotionCardsWrittenOffTotal) {
        this.promotionCardsWrittenOffTotal = promotionCardsWrittenOffTotal;
    }

    public Integer getBrowseTotal() {
        return browseTotal;
    }

    public void setBrowseTotal(Integer browseTotal) {
        this.browseTotal = browseTotal;
    }

    public Integer getLostUserTotal() {
        return lostUserTotal;
    }

    public void setLostUserTotal(Integer lostUserTotal) {
        this.lostUserTotal = lostUserTotal;
    }
}
