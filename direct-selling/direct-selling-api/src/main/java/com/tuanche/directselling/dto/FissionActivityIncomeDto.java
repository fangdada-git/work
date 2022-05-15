package com.tuanche.directselling.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class FissionActivityIncomeDto implements Serializable {

    /**
     * 活动ID
     */
    private Integer fissionId;

    /**
     * 最高预计收入
     */
    private BigDecimal maxEstimatedIncome;
    /**
     * 最高实际收入
     */
    private BigDecimal maxRealIncome;
    /**
     * 最低预计收入
     */
    private BigDecimal minEstimatedIncome;
    /**
     * 最低实际收入
     */
    private BigDecimal minRealIncome;

    /**
     * 活动是否结束
     */
    private Integer isFinished;

    public Integer getFissionId() {
        return fissionId;
    }

    public void setFissionId(Integer fissionId) {
        this.fissionId = fissionId;
    }

    public BigDecimal getMaxEstimatedIncome() {
        return maxEstimatedIncome;
    }

    public void setMaxEstimatedIncome(BigDecimal maxEstimatedIncome) {
        this.maxEstimatedIncome = maxEstimatedIncome;
    }

    public BigDecimal getMaxRealIncome() {
        return maxRealIncome;
    }

    public void setMaxRealIncome(BigDecimal maxRealIncome) {
        this.maxRealIncome = maxRealIncome;
    }

    public BigDecimal getMinEstimatedIncome() {
        return minEstimatedIncome;
    }

    public void setMinEstimatedIncome(BigDecimal minEstimatedIncome) {
        this.minEstimatedIncome = minEstimatedIncome;
    }

    public BigDecimal getMinRealIncome() {
        return minRealIncome;
    }

    public void setMinRealIncome(BigDecimal minRealIncome) {
        this.minRealIncome = minRealIncome;
    }

    public Integer isFinished() {
        return isFinished;
    }

    public void setFinished(Integer finished) {
        isFinished = finished;
    }
}
