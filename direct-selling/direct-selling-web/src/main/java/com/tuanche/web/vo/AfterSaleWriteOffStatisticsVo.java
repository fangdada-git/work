package com.tuanche.web.vo;

import java.math.BigDecimal;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/8/9 15:11
 **/
public class AfterSaleWriteOffStatisticsVo {
    /**
     * 用户微信unionid
     */
    private String userWxUnionId;
    /**
     * 用户姓名
     */
    private String name;
    /**
     * 用户手机
     */
    private String phone;
    /**
     * 核销推广卡数量
     */
    private int promotionCardTotal;
    /**
     * 核销套餐卡数量
     */
    private int packageCardTotal;
    /**
     * 套餐核销总金额
     */
    private BigDecimal packageCardAmount;

    public String getUserWxUnionId() {
        return userWxUnionId;
    }

    public void setUserWxUnionId(String userWxUnionId) {
        this.userWxUnionId = userWxUnionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPromotionCardTotal() {
        return promotionCardTotal;
    }

    public void setPromotionCardTotal(int promotionCardTotal) {
        this.promotionCardTotal = promotionCardTotal;
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
}
