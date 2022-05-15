package com.tuanche.directselling.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 活动数据对账dto
 *
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/8/18 10:59
 **/
public class AfterSaleActivityDataVerifyDto extends AfterSaleActivityDataBaseDto {

    /**
     * 打开页面用户数/点击人数/uv
     */
    private Integer uv;

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
     * 推广卡退款总数(主动+被动)
     */
    private Integer promotionCardRefundsTotal;

    /**
     * 推广卡退款金额(主动+被动)
     */
    private BigDecimal promotionCardRefundsAmount;

    /**
     * 核销礼品券总数
     */
    private Integer writeOffGiftVoucherTotal;

    /**
     * 推广卡净营收
     */
    private BigDecimal promotionCardNetRevenue;

    /**
     * 线下兑换开始时间
     */
    private Date offlineConvertStartTime;

    /**
     * 线下兑换结束时间
     */
    private Date offlineConvertEndTime;

    /**
     * 代理人1级裂变数
     */
    private Integer agentFissionTotalOne;

    /**
     * 代理人2级裂变数
     */
    private Integer agentFissionTotalTwo;

    /**
     * 自主裂变数（运营渠道数）
     */
    private Integer autonomousFissionTotal;

    /**
     * 备案人数
     */
    private Integer keepOnRecordUserTotal;

    /**
     * 发放的礼品券数
     */
    private Integer giftCertificatesTotal;


    /**
     * 推广卡退款总数-被动
     */
    private Integer promotionCardRefundsTotalPassive;
    /**
     * 推广卡退款金额-被动
     */
    private BigDecimal promotionCardRefundsAmountPassive;

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

    public Integer getWriteOffGiftVoucherTotal() {
        return writeOffGiftVoucherTotal;
    }

    public void setWriteOffGiftVoucherTotal(Integer writeOffGiftVoucherTotal) {
        this.writeOffGiftVoucherTotal = writeOffGiftVoucherTotal;
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

    public BigDecimal getPromotionCardRefundsAmountPassive() {
        return promotionCardRefundsAmountPassive;
    }

    public void setPromotionCardRefundsAmountPassive(BigDecimal promotionCardRefundsAmountPassive) {
        this.promotionCardRefundsAmountPassive = promotionCardRefundsAmountPassive;
    }

    public Integer getPromotionCardRefundsTotalPassive() {
        return promotionCardRefundsTotalPassive;
    }

    public void setPromotionCardRefundsTotalPassive(Integer promotionCardRefundsTotalPassive) {
        this.promotionCardRefundsTotalPassive = promotionCardRefundsTotalPassive;
    }
}
