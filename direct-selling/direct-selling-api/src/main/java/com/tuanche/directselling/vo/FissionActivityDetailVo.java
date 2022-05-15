package com.tuanche.directselling.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuanche.directselling.model.FissionActivity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 裂变活动详细信息
 *
 * @author ZhangYiXin
 * @version 1.0
 * @date 2020/9/23 18:03
 **/
public class FissionActivityDetailVo implements Serializable {
    private Integer fissionId;

    /**
     * 场次ID
     */
    private Integer periodsId;
    /**
     * 销售是否参与活动
     */
    private boolean isJoin;

    /**
     * 活动是否进行中
     */
    private boolean isInProgress;

    /**
     * 活动名称
     */
    private String activityName;
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    /**
     * 头图
     */
    private String headPicUrl;

    /**
     * 对赌金额
     */
    private BigDecimal personMoney;

    /**
     * 销售支付微信openid
     */
    private String saleWxOpenId;
    /**
     * 销售支付微信UnionId
     */
    private String saleWxUnionId;

    /**
     * 0：未开始 1：进行中 2：已结束
     */
    private Integer activityStatus;

    private FissionActivityInfoVo inActivity;

    public FissionActivityInfoVo getInActivity() {
        return inActivity;
    }

    public void setInActivity(FissionActivityInfoVo inActivity) {
        this.inActivity = inActivity;
    }

    public boolean getJoin() {
        return isJoin;
    }

    public void setJoin(boolean join) {
        isJoin = join;
    }

    public boolean getInProgress() {
        return isInProgress;
    }

    public void setInProgress(boolean inProgress) {
        isInProgress = inProgress;
    }

    public Integer getFissionId() {
        return fissionId;
    }

    public void setFissionId(Integer fissionId) {
        this.fissionId = fissionId;
    }

    public boolean isJoin() {
        return isJoin;
    }

    public boolean isInProgress() {
        return isInProgress;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getHeadPicUrl() {
        return headPicUrl;
    }

    public void setHeadPicUrl(String headPicUrl) {
        this.headPicUrl = headPicUrl;
    }

    public BigDecimal getPersonMoney() {
        return personMoney;
    }

    public void setPersonMoney(BigDecimal personMoney) {
        this.personMoney = personMoney;
    }

    public Integer getPeriodsId() {
        return periodsId;
    }

    public void setPeriodsId(Integer periodsId) {
        this.periodsId = periodsId;
    }

    public String getSaleWxOpenId() {
        return saleWxOpenId;
    }

    public void setSaleWxOpenId(String saleWxOpenId) {
        this.saleWxOpenId = saleWxOpenId;
    }

    public String getSaleWxUnionId() {
        return saleWxUnionId;
    }

    public void setSaleWxUnionId(String saleWxUnionId) {
        this.saleWxUnionId = saleWxUnionId;
    }

    public Integer getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(Integer activityStatus) {
        this.activityStatus = activityStatus;
    }
}
