package com.tuanche.directselling.dto;

import java.io.Serializable;

/**
 * 用户身份
 * @author：HuangHao
 * @CreatTime 2021-08-18 15:00
 */
public class AfterSaleOrderRecordUserTypeDto implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 活动ID
     */
    private Integer activityId;

    //分享人微信unionid
    private String userWxUnionId;
    //用户类型 1：代理人 2：CC代理人 3：普通用户 4：备案用户
    public Integer userType;

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
        this.userWxUnionId = userWxUnionId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }
}
