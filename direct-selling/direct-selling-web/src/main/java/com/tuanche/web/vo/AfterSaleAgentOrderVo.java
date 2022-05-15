package com.tuanche.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/7/21 21:12
 **/
public class AfterSaleAgentOrderVo implements Serializable {
    private Integer id;

    /**
     * 订单状态(业务) “待销”“待定”“进店”“套餐”“备案”“退款”
     */
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
    private Integer keepOnRecordUser;

    /**
     * 优惠卷数量
     */
    private Integer coupon;

    //创建时间
    @JsonFormat(pattern = "MM/dd", timezone = "GMT+8")
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

    public String getUserWxUnionId() {
        return userWxUnionId;
    }

    public void setUserWxUnionId(String userWxUnionId) {
        this.userWxUnionId = userWxUnionId;
    }

    public Integer getKeepOnRecordUser() {
        return keepOnRecordUser;
    }

    public void setKeepOnRecordUser(Integer keepOnRecordUser) {
        this.keepOnRecordUser = keepOnRecordUser;
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
}
