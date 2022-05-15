package com.tuanche.directselling.dto;

import java.io.Serializable;

public class FashionCarMarkeOrderDto implements Serializable {

    //大场次id
    private Integer periodsId;
    //潮车集id
    private Integer carFashionId;
    private Integer goodsId;
    private Integer cityId;
    private Integer cbId;
    private Integer csId;
    private Integer carId;
    private Integer channel;
    //支付来源  0:pc 1:wap 2:app  3：微信小程序
    private Integer paySource;
    private Integer useOriginalPrice;
    //经销商id串  ,分割
    private String dealerIdStr;
    private String userName;
    //支付成功回调地址
    private String returnUrl;
    private String userWxOpenId;

    public Integer getPeriodsId() {
        return periodsId;
    }

    public void setPeriodsId(Integer periodsId) {
        this.periodsId = periodsId;
    }

    public Integer getCarFashionId() {
        return carFashionId;
    }

    public void setCarFashionId(Integer carFashionId) {
        this.carFashionId = carFashionId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getDealerIdStr() {
        return dealerIdStr;
    }

    public void setDealerIdStr(String dealerIdStr) {
        this.dealerIdStr = dealerIdStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getUserWxOpenId() {
        return userWxOpenId;
    }

    public void setUserWxOpenId(String userWxOpenId) {
        this.userWxOpenId = userWxOpenId;
    }

    public Integer getCbId() {
        return cbId;
    }

    public void setCbId(Integer cbId) {
        this.cbId = cbId;
    }

    public Integer getCsId() {
        return csId;
    }

    public void setCsId(Integer csId) {
        this.csId = csId;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public Integer getPaySource() {
        return paySource;
    }

    public void setPaySource(Integer paySource) {
        this.paySource = paySource;
    }

    public Integer getUseOriginalPrice() {
        return useOriginalPrice;
    }

    public void setUseOriginalPrice(Integer useOriginalPrice) {
        this.useOriginalPrice = useOriginalPrice;
    }
}
