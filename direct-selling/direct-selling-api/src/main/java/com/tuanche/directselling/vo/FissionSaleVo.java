package com.tuanche.directselling.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2020/9/23 18:03
 **/
public class FissionSaleVo implements Serializable {
    /**
     * 裂变活动ID
     */
    private Integer id;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 提现时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date withdrawalTime;
    /**
     * 实际收入金额
     */
    private BigDecimal realIncome;

    /**
     * 提现状态 0：未提现 1：已提现
     */
    private Integer withdrawalState;

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

    public Date getWithdrawalTime() {
        return withdrawalTime;
    }

    public void setWithdrawalTime(Date withdrawalTime) {
        this.withdrawalTime = withdrawalTime;
    }

    public BigDecimal getRealIncome() {
        return realIncome;
    }

    public void setRealIncome(BigDecimal realIncome) {
        this.realIncome = realIncome;
    }

    public Integer getWithdrawalState() {
        return withdrawalState;
    }

    public void setWithdrawalState(Integer withdrawalState) {
        this.withdrawalState = withdrawalState;
    }
}
