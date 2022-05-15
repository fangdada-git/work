package com.tuanche.web.vo;

import java.io.Serializable;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/8/13 15:10
 **/
public class AfterSaleWriteOffStatisticsSearchVo implements Serializable {
    /**
     * 代理人微信unionId
     */
    private String agentWxUnionId;
    /**
     * 活动ID
     */
    private Integer activityId;

    /**
     * 经销商id
     */
    private Integer dealerId;
    /**
     * 开始时间 例:20210101
     */
    private Integer startTime;
    /**
     * 结束时间 例:20210101
     */
    private Integer endTime;

    public String getAgentWxUnionId() {
        return agentWxUnionId;
    }

    public void setAgentWxUnionId(String agentWxUnionId) {
        this.agentWxUnionId = agentWxUnionId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }
}
