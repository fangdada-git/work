package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.AfterSaleAgent;

import java.io.Serializable;

/**
 * 代理人
 */
public class AfterSaleAgentDto extends AfterSaleAgent implements Serializable {
    private String activityName;
    private String dealerName;

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }
}