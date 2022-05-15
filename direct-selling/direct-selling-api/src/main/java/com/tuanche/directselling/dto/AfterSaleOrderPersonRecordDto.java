package com.tuanche.directselling.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 个人业绩明细
 *
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/7/22 17:02
 **/
public class AfterSaleOrderPersonRecordDto implements Serializable {
    private Integer id;
    //售后特卖活动id
    private Integer activityId;

    //订单状态:  1:待支付 2:支付中 3:已支付 4:核销 5:申请退款 6:退款中 7:退款完成 8:等待买家确认收货
    private Integer orderStatus;
    //订单类型 1：推广卡 2：套餐卡
    private Integer orderType;

    //用户手机号
    private String userPhone;

    //车牌
    private String licencePlate;

    //代理人微信unionid
    private String agentWxUnionId;

    //用户微信unionid
    private String userWxUnionId;

    //是否为备案用户 0：不是 1：是
    private Boolean keepOnRecordUser;

    /**
     * 优惠卷数量
     */
    private Integer coupon;

    //创建时间
    private Date createDt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
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

    public Integer getCoupon() {
        return coupon;
    }

    public void setCoupon(Integer coupon) {
        this.coupon = coupon;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public String getUserWxUnionId() {
        return userWxUnionId;
    }

    public void setUserWxUnionId(String userWxUnionId) {
        this.userWxUnionId = userWxUnionId;
    }

    public Boolean getKeepOnRecordUser() {
        return keepOnRecordUser;
    }

    public void setKeepOnRecordUser(Boolean keepOnRecordUser) {
        this.keepOnRecordUser = keepOnRecordUser;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }
}
