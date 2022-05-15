package com.tuanche.directselling.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * C端数据概览
 */
public class FissionUserDataView extends FissionDataView implements Serializable {
    /**
     * 奖金上限
     */
    private BigDecimal prizePool;

    /**
     * 当前发放
     */
    private BigDecimal issued;

    /**
     * 获得奖金用户数量
     */
    private Integer customerCount;

    /**
     * 人均奖金
     */
    private BigDecimal averagePrize;


    public BigDecimal getPrizePool() {
        return prizePool;
    }

    public void setPrizePool(BigDecimal prizePool) {
        this.prizePool = prizePool;
    }

    public BigDecimal getIssued() {
        return issued;
    }

    public void setIssued(BigDecimal issued) {
        this.issued = issued;
    }

    public Integer getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(Integer customerCount) {
        this.customerCount = customerCount;
    }


    public BigDecimal getAveragePrize() {
        return averagePrize;
    }

    public void setAveragePrize(BigDecimal averagePrize) {
        this.averagePrize = averagePrize;
    }
}