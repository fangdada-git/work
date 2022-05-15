package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.AfterSaleKeepOnRecord;

import java.util.Date;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/9/28 16:56
 **/
public class AfterSaleKeepOnRecordDto extends AfterSaleKeepOnRecord {
    /**
     * 活动名称
     */
    private String activityName;
    /**
     * 线下兑换结束时间
     */
    private Date offlineConvertEndTime;

    /**
     * 录入总数量
     */
    private Integer inputCount;
    /**
     * 手机号数量
     */
    private Integer userPhoneCount;

    /**
     * 车牌号数量
     */
    private Integer licencePlateCount;

    /**
     * 开户状态 0 未开户  1已开启
     */
    private Integer onState;

    /**
     * 经销商名称
     */
    private String dealerName;

    /**
     * 业务ID 27
     */
    private Integer businessId;

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Date getOfflineConvertEndTime() {
        return offlineConvertEndTime;
    }

    public void setOfflineConvertEndTime(Date offlineConvertEndTime) {
        this.offlineConvertEndTime = offlineConvertEndTime;
    }

    public Integer getInputCount() {
        return inputCount;
    }

    public void setInputCount(Integer inputCount) {
        this.inputCount = inputCount;
    }

    public Integer getUserPhoneCount() {
        return userPhoneCount;
    }

    public void setUserPhoneCount(Integer userPhoneCount) {
        this.userPhoneCount = userPhoneCount;
    }

    public Integer getLicencePlateCount() {
        return licencePlateCount;
    }

    public void setLicencePlateCount(Integer licencePlateCount) {
        this.licencePlateCount = licencePlateCount;
    }

    public Integer getOnState() {
        return onState;
    }

    public void setOnState(Integer onState) {
        this.onState = onState;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }
}
