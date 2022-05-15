package com.tuanche.directselling.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author：HuangHao
 * @CreatTime 2021-08-09 17:34
 */
public class AfterSaleRewardRecordAmountAlarmDto {
    /**
     * 活动id
     */
    private Integer activityId;
    /**
     * 活动名称
     */
    private String activityName;
    /**
     *  预算
     */
    private BigDecimal budget;
    /**
     *  报警金额
     */
    private BigDecimal amountAlarm;
    /**
     *  已用金额
     */
    private BigDecimal amountUsed;

    /**
     *  报警手机号
     */
    private String alarmPhone;
    /**
     *  报警邮箱
     */
    private String alarmEmail;

    /**
     * 售卖结束时间
     */
    private Date saleEndTime;

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public BigDecimal getAmountAlarm() {
        return amountAlarm;
    }

    public void setAmountAlarm(BigDecimal amountAlarm) {
        this.amountAlarm = amountAlarm;
    }

    public BigDecimal getAmountUsed() {
        return amountUsed;
    }

    public void setAmountUsed(BigDecimal amountUsed) {
        this.amountUsed = amountUsed;
    }

    public String getAlarmPhone() {
        return alarmPhone;
    }

    public void setAlarmPhone(String alarmPhone) {
        this.alarmPhone = alarmPhone;
    }

    public String getAlarmEmail() {
        return alarmEmail;
    }

    public void setAlarmEmail(String alarmEmail) {
        this.alarmEmail = alarmEmail;
    }

    public Date getSaleEndTime() {
        return saleEndTime;
    }

    public void setSaleEndTime(Date saleEndTime) {
        this.saleEndTime = saleEndTime;
    }
}
