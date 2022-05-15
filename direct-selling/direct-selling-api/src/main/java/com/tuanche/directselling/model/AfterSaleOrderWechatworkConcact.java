package com.tuanche.directselling.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class AfterSaleOrderWechatworkConcact implements Serializable {

    private Integer id;
    //活动ID
    private Integer activityId;
    //订单编号
    private String orderCode;
    //用户微信unionid
    private String userWxUnionId;
    //企微官方企微号id
    private String wwUserId;
    //企微号名称
    private String wwUserName;
    //企微添加时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date wwCreateDt;
    //好友状态 3 正常 4 删除
    private Byte status;

    private Date createDt;

    private Date updateDt;

    private Boolean deleteFlag;

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

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode == null ? null : orderCode.trim();
    }

    public String getUserWxUnionId() {
        return userWxUnionId;
    }

    public void setUserWxUnionId(String userWxUnionId) {
        this.userWxUnionId = userWxUnionId == null ? null : userWxUnionId.trim();
    }

    public String getWwUserId() {
        return wwUserId;
    }

    public void setWwUserId(String wwUserId) {
        this.wwUserId = wwUserId;
    }

    public String getWwUserName() {
        return wwUserName;
    }

    public void setWwUserName(String wwUserName) {
        this.wwUserName = wwUserName == null ? null : wwUserName.trim();
    }

    public Date getWwCreateDt() {
        return wwCreateDt;
    }

    public void setWwCreateDt(Date wwCreateDt) {
        this.wwCreateDt = wwCreateDt;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}