package com.tuanche.directselling.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 裂变活动榜单
 *
 * @author ZhangYiXin
 * @version 1.0
 * @date 2020/9/23 18:03
 **/
public class FissionActivityRankVo implements Serializable {
    /**
     * 排名
     */
    private int rank;
    /**
     * 销售名称 or 经销商名称
     */
    private String name;

    /**
     * 预计收入
     */
    private BigDecimal estimatedIncome;

    /**
     * 实际收入
     */
    private BigDecimal realIncome;

    /**
     * 活动中，活动结束 实际收入&预计收入
     */
    private BigDecimal income;

    /**
     * 积分总数
     */
    private Integer integral;


    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getEstimatedIncome() {
        return estimatedIncome;
    }

    public void setEstimatedIncome(BigDecimal estimatedIncome) {
        this.estimatedIncome = estimatedIncome;
    }

    public BigDecimal getRealIncome() {
        return realIncome;
    }

    public void setRealIncome(BigDecimal realIncome) {
        this.realIncome = realIncome;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }
}
