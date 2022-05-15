package com.tuanche.directselling.vo;

import java.io.Serializable;

/**
 * @Author lvsen
 * @Description
 * @Date 2021/7/23
 **/
public class AfterSaleActivitySimpleVo implements Serializable {

    /**
     * 活动id
     */
    private Integer activityId;

    /**
     * 活动名称
     */
    private String activityName;
    /**
     * 经销商id
     */
    private Integer dealerId;
    /**
     * 经销商名称
     */
    private String dealerName;

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

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }
}
