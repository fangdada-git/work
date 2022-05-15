package com.tuanche.web.vo;

import com.tuanche.web.config.BigDecimalFormat;
import com.tuanche.web.config.BigDecimalFormat.Style;

import java.math.BigDecimal;

/**
 * 活动数据对账dto
 *
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/8/18 10:59
 **/
public class AfterSaleActivityDataSumVo {

    /**
     * 打开页面用户数/点击人数/uv
     */
    private Integer uv;

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
     * 推广卡净营收
     */
    @BigDecimalFormat(pattern = "0.00")
    private BigDecimal promotionCardNetRevenue;
    //套餐卡售出总金额
    @BigDecimalFormat(pattern = "0.00")
    private BigDecimal packageCardAmount;

    public Integer getUv() {
        return uv;
    }

    public void setUv(Integer uv) {
        this.uv = uv;
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

    public BigDecimal getPromotionCardNetRevenue() {
        return promotionCardNetRevenue;
    }

    public void setPromotionCardNetRevenue(BigDecimal promotionCardNetRevenue) {
        this.promotionCardNetRevenue = promotionCardNetRevenue;
    }

    public BigDecimal getPackageCardAmount() {
        return packageCardAmount;
    }

    public void setPackageCardAmount(BigDecimal packageCardAmount) {
        this.packageCardAmount = packageCardAmount;
    }
}
