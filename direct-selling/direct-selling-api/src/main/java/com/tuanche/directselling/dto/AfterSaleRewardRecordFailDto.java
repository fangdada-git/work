package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.AfterSaleRewardRecord;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author：HuangHao
 * @CreatTime 2021-08-09 17:34
 */
public class AfterSaleRewardRecordFailDto extends AfterSaleRewardRecord {
    /**
     * 用户微信openid
     */
    private String userWxOpenId;
    //车牌
    private String licencePlate;
    //活动名称
    private String activityName;

    public String getUserWxOpenId() {
        return userWxOpenId;
    }

    public void setUserWxOpenId(String userWxOpenId) {
        this.userWxOpenId = userWxOpenId;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
