package com.tuanche.directselling.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 销售成绩
 *
 * @author ZhangYiXin
 * @version 1.0
 * @date 2020/9/25 9:49
 **/
public class FissionSaleScoreVo implements Serializable {
    private Integer id;
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
     * 活动是否进行中
     */
    private boolean isInProgress;
    /**
     * 实际收入
     */
    private BigDecimal realIncome;

    /**
     * 预计收入
     */
    private BigDecimal estimatedIncome;

    /**
     * 销售获得的总积分
     */
    private Integer integral;

    /**
     * 个人成绩
     */
    private List<FissionActivityPersonalIntegralVo> personalIntegral;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public BigDecimal getRealIncome() {
        return realIncome;
    }

    public void setRealIncome(BigDecimal realIncome) {
        this.realIncome = realIncome;
    }

    public BigDecimal getEstimatedIncome() {
        return estimatedIncome;
    }

    public void setEstimatedIncome(BigDecimal estimatedIncome) {
        this.estimatedIncome = estimatedIncome;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public List<FissionActivityPersonalIntegralVo> getPersonalIntegral() {
        return personalIntegral;
    }

    public void setPersonalIntegral(List<FissionActivityPersonalIntegralVo> personalIntegral) {
        this.personalIntegral = personalIntegral;
    }

    public boolean isInProgress() {
        return isInProgress;
    }

    public void setInProgress(boolean inProgress) {
        isInProgress = inProgress;
    }
}
