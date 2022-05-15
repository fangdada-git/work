package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.FissionSale;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 裂变活动-销售奖励金对账
 */
public class FissionSaleRewardReconciliationDto extends FissionSale implements Serializable {
    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 所属经销商名称
     */
    private String dealerName;

    /**
     * 城市
     */
    private String cityName;

    /**
     * 提现总金额
     */
    private BigDecimal tradeAmount;


    /**
     * 流水号
     */
    private String tradeNo;

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }
}