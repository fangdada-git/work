package com.tuanche.directselling.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * B端数据概览
 */
public class FissionSaleDataView extends FissionDataView implements Serializable {
    private Long id;

    /**
     * 活动ID
     */
    private Integer fissionId;

    /**
     * 奖金上限
     */
    private BigDecimal prizePool;

    /**
     * 实发金额
     */
    private BigDecimal prizeActual;

    /**
     * 积分总数
     */
    private Integer totalIntegral;

    /**
     * 销售数量
     */
    private Integer salesmanCount;

    /**
     * 完成任务的销售数量
     */
    private Integer finishSalesmanCount;

    /**
     * 最高奖金
     */
    private BigDecimal prizeMax;

    /**
     * 最低奖金
     */
    private BigDecimal prizeMin;

    /**
     * 是否删除 0未删除 1 删除
     */
    private Boolean deleteFlag;

    /**
     * 创建时间
     */
    private Date createDt;

    /**
     * 修改时间
     */
    private Date updateDt;

    public BigDecimal getPrizePool() {
        return prizePool;
    }

    public void setPrizePool(BigDecimal prizePool) {
        this.prizePool = prizePool;
    }

    public Integer getTotalIntegral() {
        return totalIntegral;
    }

    public void setTotalIntegral(Integer totalIntegral) {
        this.totalIntegral = totalIntegral;
    }

    public Integer getSalesmanCount() {
        return salesmanCount;
    }

    public void setSalesmanCount(Integer salesmanCount) {
        this.salesmanCount = salesmanCount;
    }

    public Integer getFinishSalesmanCount() {
        return finishSalesmanCount;
    }

    public void setFinishSalesmanCount(Integer finishSalesmanCount) {
        this.finishSalesmanCount = finishSalesmanCount;
    }

    public BigDecimal getPrizeMax() {
        return prizeMax;
    }

    public void setPrizeMax(BigDecimal prizeMax) {
        this.prizeMax = prizeMax;
    }

    public BigDecimal getPrizeMin() {
        return prizeMin;
    }

    public void setPrizeMin(BigDecimal prizeMin) {
        this.prizeMin = prizeMin;
    }

    public BigDecimal getPrizeActual() {
        return prizeActual;
    }

    public void setPrizeActual(BigDecimal prizeActual) {
        this.prizeActual = prizeActual;
    }
}