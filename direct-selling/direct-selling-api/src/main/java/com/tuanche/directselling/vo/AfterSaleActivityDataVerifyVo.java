package com.tuanche.directselling.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuanche.directselling.utils.BigDecimalFormat;
import com.tuanche.directselling.utils.BigDecimalFormat.Style;
import com.tuanche.directselling.utils.GlobalConstants;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 活动数据对账dto
 *
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/8/18 10:59
 **/
public class AfterSaleActivityDataVerifyVo extends AfterSaleActivityDataBaseVo {

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
    @BigDecimalFormat(pattern = "0.00")
    private BigDecimal promotionCardAmount;

    /**
     * 售出套餐卡总数
     */
    private Integer packageCardTotal;

    /**
     * 售出套餐卡金额
     */
    @BigDecimalFormat(pattern = "0.00")
    private BigDecimal packageCardAmount;

    /**
     * 奖励总金额
     */
    @BigDecimalFormat(pattern = "0.00")
    private BigDecimal rewardTotal;

    /**
     * 推广卡退款总数(主动+被动)
     */
    private Integer promotionCardRefundsTotal;

    /**
     * 推广卡退款金额(主动+被动)
     */
    @BigDecimalFormat(pattern = "0.00")
    private BigDecimal promotionCardRefundsAmount;

    /**
     * 核销礼品券总数
     */
    private Integer writeOffGiftVoucherTotal;

    /**
     * 推广卡净营收
     */
    @BigDecimalFormat(pattern = "0.00")
    private BigDecimal promotionCardNetRevenue;


    /**
     * 推广转化率
     */
    @BigDecimalFormat(pattern = "0.00", style = Style.PERCENT)
    private BigDecimal promotionPercent;

    /**
     * 到店率
     */
    @BigDecimalFormat(pattern = "0.00", style = Style.PERCENT)
    private BigDecimal visitorsPercent;

    /**
     * 套餐转化率
     */
    @BigDecimalFormat(pattern = "0.00", style = Style.PERCENT)
    private BigDecimal packagePercent;

    /**
     * 退款率
     */
    @BigDecimalFormat(pattern = "0.00", style = Style.PERCENT)
    private BigDecimal refundPercent;




    /**
     * 代理人1、2级裂变数
     */
    private Integer agentFissionTotal;
    /**
     * 代理人1级裂变数
     */
    private Integer agentFissionTotalOne;

    /**
     * 代理人2级裂变数
     */
    private Integer agentFissionTotalTwo;

    @BigDecimalFormat(pattern = "0.00", style = Style.PERCENT)
    private BigDecimal agentFissionTotalPercent;

    /**
     * 自主裂变数（运营渠道数）
     */
    private Integer autonomousFissionTotal;

    /**
     * 自主裂变占比
     */
    @BigDecimalFormat(pattern = "0.00", style = Style.PERCENT)
    private BigDecimal autonomousFissionTotalPercent;

    /**
     * 备案人数
     */
    private Integer keepOnRecordUserTotal;

    /**
     * 备案人数占比
     */
    @BigDecimalFormat(pattern = "0.00", style = Style.PERCENT)
    private BigDecimal keepOnRecordUserTotalPercent;

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
    @BigDecimalFormat(pattern = "0.00")
    private BigDecimal promotionCardRefundsAmountPassive;

    //活动状态
    private String activityStatus;

    private String promotionPercentStr;
    private String visitorsPercentStr;
    private String packagePercentStr;

    public String getPromotionPercentStr() {
        if (getUv() == 0) {
            return BigDecimal.ZERO.toString()+"%";
        } else {
            //推广转化率  推广卡售卖数（包括退款） / 点击人数
            return GlobalConstants.percentConversion(getPromotionCardTotal(), getUv()).toString()+"%";
        }
    }

    public String getVisitorsPercentStr() {
        if (getPromotionCardTotal() > 0) {
            return GlobalConstants.percentConversion(getVisitorsTotal(), getPromotionCardTotal()).toString()+"%";
        }
        return BigDecimal.ZERO.toString()+"%";
    }
    public String getPackagePercentStr() {
        if(getVisitorsTotal() > 0){
            return GlobalConstants.percentConversion(getPackageCardTotal(), getVisitorsTotal()).toString()+"%";
        }
        return BigDecimal.ZERO.toString()+"%";
    }

    public void setPromotionPercentStr(String promotionPercentStr) {
        this.promotionPercentStr = promotionPercentStr;
    }

    public void setVisitorsPercentStr(String visitorsPercentStr) {
        this.visitorsPercentStr = visitorsPercentStr;
    }

    public void setPackagePercentStr(String packagePercentStr) {
        this.packagePercentStr = packagePercentStr;
    }

    public String getActivityStatus() {
        return activityStatus;
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

    public BigDecimal getPromotionPercent() {
        return promotionPercent;
    }

    public void setPromotionPercent(BigDecimal promotionPercent) {
        this.promotionPercent = promotionPercent;
    }

    public BigDecimal getVisitorsPercent() {
        return visitorsPercent;
    }

    public void setVisitorsPercent(BigDecimal visitorsPercent) {
        this.visitorsPercent = visitorsPercent;
    }

    public BigDecimal getPackagePercent() {
        return packagePercent;
    }

    public void setPackagePercent(BigDecimal packagePercent) {
        this.packagePercent = packagePercent;
    }

    public BigDecimal getRefundPercent() {
        return refundPercent;
    }

    public void setRefundPercent(BigDecimal refundPercent) {
        this.refundPercent = refundPercent;
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

    public BigDecimal getAgentFissionTotalPercent() {
        return agentFissionTotalPercent;
    }

    public void setAgentFissionTotalPercent(BigDecimal agentFissionTotalPercent) {
        this.agentFissionTotalPercent = agentFissionTotalPercent;
    }

    public BigDecimal getAutonomousFissionTotalPercent() {
        return autonomousFissionTotalPercent;
    }

    public void setAutonomousFissionTotalPercent(BigDecimal autonomousFissionTotalPercent) {
        this.autonomousFissionTotalPercent = autonomousFissionTotalPercent;
    }

    public BigDecimal getKeepOnRecordUserTotalPercent() {
        return keepOnRecordUserTotalPercent;
    }

    public void setKeepOnRecordUserTotalPercent(BigDecimal keepOnRecordUserTotalPercent) {
        this.keepOnRecordUserTotalPercent = keepOnRecordUserTotalPercent;
    }

    public Integer getAgentFissionTotal() {
        return agentFissionTotal;
    }

    public void setAgentFissionTotal(Integer agentFissionTotal) {
        this.agentFissionTotal = agentFissionTotal;
    }

    public Integer getPromotionCardRefundsTotalPassive() {
        return promotionCardRefundsTotalPassive;
    }

    public void setPromotionCardRefundsTotalPassive(Integer promotionCardRefundsTotalPassive) {
        this.promotionCardRefundsTotalPassive = promotionCardRefundsTotalPassive;
    }
}
