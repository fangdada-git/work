package com.tuanche.directselling.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AfterSaleActivityCoupon implements Serializable {
    private Integer id;
    /**
     * 活动id
     */
    private Integer activityId;
    /**
     * 类型 1 卡券 2套餐
     */
    private Integer type;
    /**
     * 套餐id
     */
    private Integer packageId;
    /**
     * 卡券id
     */
    private Integer couponId;
    /**
     * 卡券名称
     */
    private String couponName;
    /**
     * 服务规格
     */
    private String serviceSpecification;
    /**
     * 金额
     */
    private BigDecimal money;
    /**
     * 数量
     */
    private Integer couponCount;
    /**
     * 领取开始时间
     */
    private Date getStartTime;
    /**
     * 获取结束时间
     */
    private Date getEndTime;
    /**
     * 有效年
     */
    private Integer validity;

    private Integer relStatus;

    private Date relTime;

    private Integer operateUser;

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

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(Integer couponCount) {
        this.couponCount = couponCount;
    }

    public Date getGetStartTime() {
        return getStartTime;
    }

    public void setGetStartTime(Date getStartTime) {
        this.getStartTime = getStartTime;
    }

    public Date getGetEndTime() {
        return getEndTime;
    }

    public void setGetEndTime(Date getEndTime) {
        this.getEndTime = getEndTime;
    }

    public Integer getValidity() {
        return validity;
    }

    public void setValidity(Integer validity) {
        this.validity = validity;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public Integer getRelStatus() {
        return relStatus;
    }

    public void setRelStatus(Integer relStatus) {
        this.relStatus = relStatus;
    }

    public Date getRelTime() {
        return relTime;
    }

    public void setRelTime(Date relTime) {
        this.relTime = relTime;
    }

    public Integer getOperateUser() {
        return operateUser;
    }

    public void setOperateUser(Integer operateUser) {
        this.operateUser = operateUser;
    }



    public String getServiceSpecification() {
        return serviceSpecification;
    }

    public void setServiceSpecification(String serviceSpecification) {
        this.serviceSpecification = serviceSpecification;
    }
}