package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.FissionSale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 裂变活动-销售表
 */
public class FissionSaleDto extends FissionSale implements Serializable {
    /**
     * 活动名称
     */
    private String activityName;
    /**
     * 完成任务目标的积分
     */
    private Integer finishTaskIntegral;

    /**
     * 销售数量
     */
    private Integer saleCount;

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Integer getFinishTaskIntegral() {
        return finishTaskIntegral;
    }

    public void setFinishTaskIntegral(Integer finishTaskIntegral) {
        this.finishTaskIntegral = finishTaskIntegral;
    }

    public Integer getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Integer saleCount) {
        this.saleCount = saleCount;
    }
}