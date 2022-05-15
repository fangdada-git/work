package com.tuanche.directselling.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 裂变活动-C端用户奖励金对账
 */
public class FissionUserRewardReconciliationDto implements Serializable {
    /**
     * 用户微信UnionId
     */
    private String userWxUnionId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 流水号
     */
    private String tradeNo;
    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 城市
     */
    private String cityName;

    /**
     * 总奖励金额
     */
    private BigDecimal totalRewardAmount;

    /**
     * 打款时间
     */
    private Date payTime;

    /**
     * 打款成功金额
     */
    private BigDecimal successRewardAmount;

    /**
     * 打款失败金额
     */
    private BigDecimal failRewardAmount;

    /**
     * 浏览页面获得的奖励金额
     */
    private BigDecimal pageView;

    /**
     * 预约直播获得的奖励金额
     */
    private BigDecimal subscribeLive;

    /**
     * 活动页面商品购买获得的奖励金额
     */
    private BigDecimal activityPageProduct;

    /**
     * 观看直播获得的奖励金额
     */
    private BigDecimal liveView;

    /**
     * 直播间商品购买商品获得的奖励金额
     */
    private BigDecimal livePageProduct;

    public String getUserWxUnionId() {
        return userWxUnionId;
    }

    public void setUserWxUnionId(String userWxUnionId) {
        this.userWxUnionId = userWxUnionId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public BigDecimal getTotalRewardAmount() {
        return totalRewardAmount;
    }

    public void setTotalRewardAmount(BigDecimal totalRewardAmount) {
        this.totalRewardAmount = totalRewardAmount;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public BigDecimal getSuccessRewardAmount() {
        return successRewardAmount;
    }

    public void setSuccessRewardAmount(BigDecimal successRewardAmount) {
        this.successRewardAmount = successRewardAmount;
    }

    public BigDecimal getFailRewardAmount() {
        return failRewardAmount;
    }

    public void setFailRewardAmount(BigDecimal failRewardAmount) {
        this.failRewardAmount = failRewardAmount;
    }

    public BigDecimal getPageView() {
        return pageView;
    }

    public void setPageView(BigDecimal pageView) {
        this.pageView = pageView;
    }

    public BigDecimal getSubscribeLive() {
        return subscribeLive;
    }

    public void setSubscribeLive(BigDecimal subscribeLive) {
        this.subscribeLive = subscribeLive;
    }

    public BigDecimal getActivityPageProduct() {
        return activityPageProduct;
    }

    public void setActivityPageProduct(BigDecimal activityPageProduct) {
        this.activityPageProduct = activityPageProduct;
    }

    public BigDecimal getLiveView() {
        return liveView;
    }

    public void setLiveView(BigDecimal liveView) {
        this.liveView = liveView;
    }

    public BigDecimal getLivePageProduct() {
        return livePageProduct;
    }

    public void setLivePageProduct(BigDecimal livePageProduct) {
        this.livePageProduct = livePageProduct;
    }
}