package com.tuanche.directselling.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AfterSaleActivityStatistics implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     * 活动ID
     */
    private Integer activityId;

    /**
     * 打开页面用户数/点击人数/uv
     */
    private Integer uv;
    /**
     * 流失客户数
     */
    private Integer browseTotal;

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
     * 发放的礼品券数
     */
    private Integer giftCertificatesTotal;

    /**
     * 自主裂变数（运营渠道数）
     */
    private Integer autonomousFissionTotal;

    /**
     * 推广卡退款总数-主动
     */
    private Integer promotionCardRefundsTotalActive;

    /**
     * 推广卡退款金额-主动
     */
    private BigDecimal promotionCardRefundsAmountActive;

    /**
     * 推广卡退款总数-被动
     */
    private Integer promotionCardRefundsTotalPassive;

    /**
     * 推广卡退款金额-被动
     */
    private BigDecimal promotionCardRefundsAmountPassive;

    /**
     * 核销礼品券总数
     */
    private Integer writeOffGiftVoucherTotal;

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
    private int primaryFissionTotal;
    /**
     * 超过1级的裂变数（转介绍人数）
     */
    private int beyondPrimaryFissionTotal;

    /**
     * 流失客户数
     */
    private Integer lostUserTotal;
    /**
     * 备案人数
     */
    private Integer keepOnRecordUserTotal;
    /**
     * 备案客户完成裂变任务数
     */
    private Integer keepOnRecordFinishUserTotal;

    /**
     * 待发券推广卡数
     */
    private Integer promotionCardsWaitingReleaseTotal;

    /**
     * 核销推广卡数
     */
    private Integer promotionCardsWrittenOffTotal;

    /**
     * 核销推广卡金额
     */
    private BigDecimal promotionCardsWrittenOffAmount;
    /**
     * 到店人数
     */
    private Integer visitorsTotal;

    /**
     * 创建时间
     */
    private Date createDt;

    /**
     * 更新时间
     */
    private Date updateDt;

    /**
     * after_sale_activity_statistics
     */
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getUv() {
        return uv;
    }

    public void setUv(Integer uv) {
        this.uv = uv;
    }

    public Integer getBrowseTotal() {
        return browseTotal;
    }

    public void setBrowseTotal(Integer browseTotal) {
        this.browseTotal = browseTotal;
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

    public BigDecimal getRewardTotal() {
        return rewardTotal;
    }

    public void setRewardTotal(BigDecimal rewardTotal) {
        this.rewardTotal = rewardTotal;
    }

    public Integer getGiftCertificatesTotal() {
        return giftCertificatesTotal;
    }

    public void setGiftCertificatesTotal(Integer giftCertificatesTotal) {
        this.giftCertificatesTotal = giftCertificatesTotal;
    }

    public Integer getAutonomousFissionTotal() {
        return autonomousFissionTotal;
    }

    public void setAutonomousFissionTotal(Integer autonomousFissionTotal) {
        this.autonomousFissionTotal = autonomousFissionTotal;
    }

    public Integer getPromotionCardRefundsTotalActive() {
        return promotionCardRefundsTotalActive;
    }

    public void setPromotionCardRefundsTotalActive(Integer promotionCardRefundsTotalActive) {
        this.promotionCardRefundsTotalActive = promotionCardRefundsTotalActive;
    }

    public BigDecimal getPromotionCardRefundsAmountActive() {
        return promotionCardRefundsAmountActive;
    }

    public void setPromotionCardRefundsAmountActive(BigDecimal promotionCardRefundsAmountActive) {
        this.promotionCardRefundsAmountActive = promotionCardRefundsAmountActive;
    }

    public Integer getPromotionCardRefundsTotalPassive() {
        return promotionCardRefundsTotalPassive;
    }

    public void setPromotionCardRefundsTotalPassive(Integer promotionCardRefundsTotalPassive) {
        this.promotionCardRefundsTotalPassive = promotionCardRefundsTotalPassive;
    }

    public BigDecimal getPromotionCardRefundsAmountPassive() {
        return promotionCardRefundsAmountPassive;
    }

    public void setPromotionCardRefundsAmountPassive(BigDecimal promotionCardRefundsAmountPassive) {
        this.promotionCardRefundsAmountPassive = promotionCardRefundsAmountPassive;
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

    public Integer getLostUserTotal() {
        return lostUserTotal;
    }

    public void setLostUserTotal(Integer lostUserTotal) {
        this.lostUserTotal = lostUserTotal;
    }

    public Integer getKeepOnRecordUserTotal() {
        return keepOnRecordUserTotal;
    }

    public void setKeepOnRecordUserTotal(Integer keepOnRecordUserTotal) {
        this.keepOnRecordUserTotal = keepOnRecordUserTotal;
    }

    public Integer getPromotionCardsWrittenOffTotal() {
        return promotionCardsWrittenOffTotal;
    }

    public void setPromotionCardsWrittenOffTotal(Integer promotionCardsWrittenOffTotal) {
        this.promotionCardsWrittenOffTotal = promotionCardsWrittenOffTotal;
    }

    public BigDecimal getPromotionCardsWrittenOffAmount() {
        return promotionCardsWrittenOffAmount;
    }

    public void setPromotionCardsWrittenOffAmount(BigDecimal promotionCardsWrittenOffAmount) {
        this.promotionCardsWrittenOffAmount = promotionCardsWrittenOffAmount;
    }

    public Integer getVisitorsTotal() {
        return visitorsTotal;
    }

    public void setVisitorsTotal(Integer visitorsTotal) {
        this.visitorsTotal = visitorsTotal;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public Integer getKeepOnRecordFinishUserTotal() {
        return keepOnRecordFinishUserTotal;
    }

    public void setKeepOnRecordFinishUserTotal(Integer keepOnRecordFinishUserTotal) {
        this.keepOnRecordFinishUserTotal = keepOnRecordFinishUserTotal;
    }

    public Integer getPromotionCardsWaitingReleaseTotal() {
        return promotionCardsWaitingReleaseTotal;
    }

    public void setPromotionCardsWaitingReleaseTotal(Integer promotionCardsWaitingReleaseTotal) {
        this.promotionCardsWaitingReleaseTotal = promotionCardsWaitingReleaseTotal;
    }

    public int getPrimaryFissionTotal() {
        return primaryFissionTotal;
    }

    public void setPrimaryFissionTotal(int primaryFissionTotal) {
        this.primaryFissionTotal = primaryFissionTotal;
    }

    public int getBeyondPrimaryFissionTotal() {
        return beyondPrimaryFissionTotal;
    }

    public void setBeyondPrimaryFissionTotal(int beyondPrimaryFissionTotal) {
        this.beyondPrimaryFissionTotal = beyondPrimaryFissionTotal;
    }
}