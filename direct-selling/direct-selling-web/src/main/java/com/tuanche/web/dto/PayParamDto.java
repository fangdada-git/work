package com.tuanche.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class PayParamDto implements Serializable {
    
    //订单编号
    private String orderNo;
    //订单内容
    private String content;
    
    private String productCode;
    
    private BigDecimal amount;
    //支付成功 回调的url （回调修改订单相关信息）
    private String notifyUrl;
    //支付成功  返回的url（页面返回）
    private String returnUrl;
    
    private String openId;
    
    private String payType;
    //订单流水号
    private String tradeBusiness;
    
    private Integer broswerType;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getTradeBusiness() {
        return tradeBusiness;
    }

    public void setTradeBusiness(String tradeBusiness) {
        this.tradeBusiness = tradeBusiness;
    }

    public Integer getBroswerType() {
        return broswerType;
    }

    public void setBroswerType(Integer broswerType) {
        this.broswerType = broswerType;
    }
}
