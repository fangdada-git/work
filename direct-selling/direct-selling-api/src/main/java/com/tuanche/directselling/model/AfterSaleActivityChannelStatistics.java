package com.tuanche.directselling.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AfterSaleActivityChannelStatistics implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 活动ID
     */
    private Integer activityId;

    /**
     * 渠道
     */
    private Integer channel;

    /**
     * 售出推广卡总数
     */
    private int promotionCardTotal;

    /**
     * 售出推广卡金额
     */
    private BigDecimal promotionCardAmount;

    /**
     * 售出套餐卡总数
     */
    private int packageCardTotal;

    /**
     * 售出套餐卡金额
     */
    private BigDecimal packageCardAmount;

    /**
     * 推广卡退款总数-主动
     */
    private int promotionCardRefundsTotalActive;

    /**
     * 推广卡退款金额-主动
     */
    private BigDecimal promotionCardRefundsAmountActive;

    /**
     * 推广卡退款总数-被动
     */
    private int promotionCardRefundsTotalPassive;

    /**
     * 推广卡退款金额-被动
     */
    private BigDecimal promotionCardRefundsAmountPassive;

    /**
     * 1级裂变数
     */
    private int primaryFissionTotal;

    /**
     * 超过一级的裂变数
     */
    private int beyondPrimaryFissionTotal;

    /**
     * 核销推广卡数
     */
    private int promotionCardsWrittenOffTotal;

    /**
     * 核销推广卡金额
     */
    private BigDecimal promotionCardsWrittenOffAmount;

    /**
     * 到店人数
     */
    private int visitorsTotal;

    /**
     * 创建时间
     */
    private Date createDt;

    /**
     * 更新时间
     */
    private Date updateDt;

    /**
     * after_sale_activity_channel_statistics
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

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public int getPromotionCardTotal() {
        return promotionCardTotal;
    }

    public void setPromotionCardTotal(int promotionCardTotal) {
        this.promotionCardTotal = promotionCardTotal;
    }

    public BigDecimal getPromotionCardAmount() {
        return promotionCardAmount;
    }

    public void setPromotionCardAmount(BigDecimal promotionCardAmount) {
        this.promotionCardAmount = promotionCardAmount;
    }

    public int getPackageCardTotal() {
        return packageCardTotal;
    }

    public void setPackageCardTotal(int packageCardTotal) {
        this.packageCardTotal = packageCardTotal;
    }

    public BigDecimal getPackageCardAmount() {
        return packageCardAmount;
    }

    public void setPackageCardAmount(BigDecimal packageCardAmount) {
        this.packageCardAmount = packageCardAmount;
    }

    public int getPromotionCardRefundsTotalActive() {
        return promotionCardRefundsTotalActive;
    }

    public void setPromotionCardRefundsTotalActive(int promotionCardRefundsTotalActive) {
        this.promotionCardRefundsTotalActive = promotionCardRefundsTotalActive;
    }

    public BigDecimal getPromotionCardRefundsAmountActive() {
        return promotionCardRefundsAmountActive;
    }

    public void setPromotionCardRefundsAmountActive(BigDecimal promotionCardRefundsAmountActive) {
        this.promotionCardRefundsAmountActive = promotionCardRefundsAmountActive;
    }

    public int getPromotionCardRefundsTotalPassive() {
        return promotionCardRefundsTotalPassive;
    }

    public void setPromotionCardRefundsTotalPassive(int promotionCardRefundsTotalPassive) {
        this.promotionCardRefundsTotalPassive = promotionCardRefundsTotalPassive;
    }

    public BigDecimal getPromotionCardRefundsAmountPassive() {
        return promotionCardRefundsAmountPassive;
    }

    public void setPromotionCardRefundsAmountPassive(BigDecimal promotionCardRefundsAmountPassive) {
        this.promotionCardRefundsAmountPassive = promotionCardRefundsAmountPassive;
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

    public int getPromotionCardsWrittenOffTotal() {
        return promotionCardsWrittenOffTotal;
    }

    public void setPromotionCardsWrittenOffTotal(int promotionCardsWrittenOffTotal) {
        this.promotionCardsWrittenOffTotal = promotionCardsWrittenOffTotal;
    }

    public BigDecimal getPromotionCardsWrittenOffAmount() {
        return promotionCardsWrittenOffAmount;
    }

    public void setPromotionCardsWrittenOffAmount(BigDecimal promotionCardsWrittenOffAmount) {
        this.promotionCardsWrittenOffAmount = promotionCardsWrittenOffAmount;
    }

    public int getVisitorsTotal() {
        return visitorsTotal;
    }

    public void setVisitorsTotal(int visitorsTotal) {
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
}