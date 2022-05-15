package com.tuanche.web.vo;

import java.io.Serializable;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/7/21 21:12
 **/
public class AfterSaleAgentStatSearchVo implements Serializable {

    /**
     * 活动ID
     */
    private Integer activityId;

    /**
     *  经销商id
     */
    private Integer dealerId;
    /**
     * 开始时间 例:20210101
     */
    private Integer startTime;
    /**
     * 结束时间 例:20210101
     */
    private Integer endTime;

    /**
     * 开始时间
     */
    private String startDateTime;
    /**
     * 结束时间
     */
    private String endDateTime;

    /**
     * 排序字段 share pageView uniqueVisitor
     */
    private String orderBy;

    //0:“待销” 1:“待定” 2:“进店” 3:“套餐”  4:“备案”  5:“退款
    private Integer orderStatus;
    //订单类型 1：推广卡 2：套餐卡
    private Integer orderType;
    //代理人微信unionid
    private String agentWxUnionId;
    //车牌
    private String licencePlate;
    /**
     * 老师WxUnionId
     */
    private String teacherWxUnionId;

    private String phone;

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public String getAgentWxUnionId() {
        return agentWxUnionId;
    }

    public void setAgentWxUnionId(String agentWxUnionId) {
        this.agentWxUnionId = agentWxUnionId;
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public String getTeacherWxUnionId() {
        return teacherWxUnionId;
    }

    public void setTeacherWxUnionId(String teacherWxUnionId) {
        this.teacherWxUnionId = teacherWxUnionId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }
}
