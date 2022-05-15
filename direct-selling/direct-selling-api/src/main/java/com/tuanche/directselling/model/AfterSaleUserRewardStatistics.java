package com.tuanche.directselling.model;

import com.tuanche.commons.util.StringUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 售后特卖-用户奖励统计表
 */
public class AfterSaleUserRewardStatistics implements Serializable {
    /**
     * after_sale_user_reward_statistics
     */
    private static final long serialVersionUID = 1L;
    private Integer id;

    /**
     * 活动ID
     */
    private Integer activityId;
    private Integer dealerId;

    /**
     * 用户微信unionid
     */
    private String userWxUnionId;

    /**
     * 用户类型 0：备案用户 1：流失用户 2：普通用户 3：车商代理人
     */
    private Integer userType;

    /**
     * 代理人微信unionid
     */
    private String agentWxUnionId;

    /**
     * 代理人微信昵称
     */
    private String agentNickName;

    /**
     * 分享人微信unionid
     */
    private String shareWxUnionId;

    /**
     * 分享人微信昵称
     */
    private String shareNickName;

    /**
     * 购买奖励
     */
    private BigDecimal purchaseReward;

    /**
     * 分享奖励
     */
    private BigDecimal shareReward;

    /**
     * 额外奖励
     */
    private BigDecimal extraReward;

    /**
     * 礼品券状态 0：未获得 1：待核销 2：已核销
     */
    private Integer writeOffGiftCertificatesTotal;

    /**
     * 邀请人数
     */
    private Integer inviteesNum;

    /**
     * 支付成功金额
     */
    private BigDecimal paymentSuccessfulAmount;

    /**
     * 支付失败金额
     */
    private BigDecimal paymentFailureAmount;

    /**
     * 创建时间
     */
    private Date createDt;

    /**
     * 更新时间
     */
    private Date updateDt;

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

    public String getUserWxUnionId() {
        return userWxUnionId;
    }

    public void setUserWxUnionId(String userWxUnionId) {
        this.userWxUnionId = StringUtil.isEmpty(userWxUnionId) ? null : userWxUnionId.trim();
    }


    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getAgentWxUnionId() {
        return agentWxUnionId;
    }

    public void setAgentWxUnionId(String agentWxUnionId) {
        this.agentWxUnionId = StringUtil.isEmpty(agentWxUnionId) ? null : agentWxUnionId.trim();
    }

    public String getAgentNickName() {
        return agentNickName;
    }

    public void setAgentNickName(String agentNickName) {
        this.agentNickName = agentNickName;
    }

    public String getShareWxUnionId() {
        return shareWxUnionId;
    }

    public void setShareWxUnionId(String shareWxUnionId) {
        this.shareWxUnionId = StringUtil.isEmpty(shareWxUnionId) ? null : shareWxUnionId.trim();
    }

    public String getShareNickName() {
        return shareNickName;
    }

    public void setShareNickName(String shareNickName) {
        this.shareNickName = shareNickName;
    }

    public BigDecimal getPurchaseReward() {
        if (purchaseReward==null) return new BigDecimal(0);
        return purchaseReward;
    }

    public void setPurchaseReward(BigDecimal purchaseReward) {
        this.purchaseReward = purchaseReward;
    }

    public BigDecimal getShareReward() {
        if (shareReward==null) return new BigDecimal(0);
        return shareReward;
    }

    public void setShareReward(BigDecimal shareReward) {
        this.shareReward = shareReward;
    }

    public BigDecimal getExtraReward() {
        if (extraReward==null) return new BigDecimal(0);
        return extraReward;
    }

    public void setExtraReward(BigDecimal extraReward) {
        this.extraReward = extraReward;
    }

    public Integer getWriteOffGiftCertificatesTotal() {
        return writeOffGiftCertificatesTotal;
    }

    public void setWriteOffGiftCertificatesTotal(Integer writeOffGiftCertificatesTotal) {
        this.writeOffGiftCertificatesTotal = writeOffGiftCertificatesTotal;
    }

    public Integer getInviteesNum() {
        return inviteesNum;
    }

    public void setInviteesNum(Integer inviteesNum) {
        this.inviteesNum = inviteesNum;
    }

    public BigDecimal getPaymentSuccessfulAmount() {
        return paymentSuccessfulAmount;
    }

    public void setPaymentSuccessfulAmount(BigDecimal paymentSuccessfulAmount) {
        this.paymentSuccessfulAmount = paymentSuccessfulAmount;
    }

    public BigDecimal getPaymentFailureAmount() {
        return paymentFailureAmount;
    }

    public void setPaymentFailureAmount(BigDecimal paymentFailureAmount) {
        this.paymentFailureAmount = paymentFailureAmount;
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

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }
}