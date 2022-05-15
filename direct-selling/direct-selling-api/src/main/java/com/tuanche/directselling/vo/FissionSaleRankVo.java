package com.tuanche.directselling.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2020/9/23 18:03
 **/
public class FissionSaleRankVo implements Serializable {
    /**
     * 销售ID
     */
    private Integer id;

    /**
     * 销售姓名
     */
    private String saleName;
    /**
     * 实际收入金额
     */
    private BigDecimal realIncome;

    /**
     * 预计收入
     */
    private BigDecimal estimatedIncome;

    /**
     * 销售获得的总积分
     */
    private Integer taskIntegral;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
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

    public Integer getTaskIntegral() {
        return taskIntegral;
    }

    public void setTaskIntegral(Integer taskIntegral) {
        this.taskIntegral = taskIntegral;
    }
}
