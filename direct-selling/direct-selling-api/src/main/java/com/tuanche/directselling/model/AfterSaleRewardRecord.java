package com.tuanche.directselling.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AfterSaleRewardRecord implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     * 活动id
     */
    private Integer activityId;
    /**
     * 经销商id
     */
    private Integer dealerId;

    /**
     * 用户类型 1：代理人 2：购买用户 3：分享人
     */
    private Integer userType;

    /**
     * 微信unionid
     */
    private String userWxUnionId;

    /**
     * 奖励类型 1：分享红包 2：购买红包 3：礼品券 4：邀请额外奖励
     */
    private Integer rewardType;

    /**
     * 奖励的单位数量：元或者个等
     */
    private BigDecimal quantity;

    /**
     * 奖励来自哪个订单
     */
    private String orderCode;
    /**
     * 商户订单号(随机生成，不用order_code是因为要多次付款)
     */
    private String partnerTradeNo;

    /**
     * 支付状态 0：未支付/未发放 1：支付成功/发放成功 2：支付失败/发放失败 3：已退款/奖励已收回
     */
    private Integer payStatus;

    /**
     * 微信交易流水号
     */
    private String wxTradeNo;

    /**
     * 微信交易时间
     */
    private Date wxTradeTime;

    /**
     * 微信支付成功时间
     */
    private Date wxPayTime;

    /**
     * 微信交易唯一标识
     */
    private String wxNonceStr;
    /**
     * 支付/发放失败原因
     */
    private String failureReason;
    /**
     * 错误码，SUCCESS：成功，TC_开头的都是团车系统错误码，其他的是微信错误码，如V2_ACCOUNT_SIMPLE_BAN：微信未实名认证
     */
    private String errCode;

    /**
     * 创建时间
     */
    private Date createDt;

    /**
     * after_sale_reward_record
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

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getUserWxUnionId() {
        return userWxUnionId;
    }

    public void setUserWxUnionId(String userWxUnionId) {
        this.userWxUnionId = userWxUnionId;
    }

    public Integer getRewardType() {
        return rewardType;
    }

    public void setRewardType(Integer rewardType) {
        this.rewardType = rewardType;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public String getWxTradeNo() {
        return wxTradeNo;
    }

    public void setWxTradeNo(String wxTradeNo) {
        this.wxTradeNo = wxTradeNo;
    }

    public Date getWxTradeTime() {
        return wxTradeTime;
    }

    public void setWxTradeTime(Date wxTradeTime) {
        this.wxTradeTime = wxTradeTime;
    }

    public Date getWxPayTime() {
        return wxPayTime;
    }

    public void setWxPayTime(Date wxPayTime) {
        this.wxPayTime = wxPayTime;
    }

    public String getWxNonceStr() {
        return wxNonceStr;
    }

    public void setWxNonceStr(String wxNonceStr) {
        this.wxNonceStr = wxNonceStr;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public String getPartnerTradeNo() {
        return partnerTradeNo;
    }

    public void setPartnerTradeNo(String partnerTradeNo) {
        this.partnerTradeNo = partnerTradeNo;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }
}