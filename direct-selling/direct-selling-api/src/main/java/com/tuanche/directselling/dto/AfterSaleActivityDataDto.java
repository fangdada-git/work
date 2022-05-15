package com.tuanche.directselling.dto;

import java.math.BigDecimal;

/**
 * 活动数据dto
 *
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/8/18 10:59
 **/
public class AfterSaleActivityDataDto extends AfterSaleActivityDataBaseDto {


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
}
