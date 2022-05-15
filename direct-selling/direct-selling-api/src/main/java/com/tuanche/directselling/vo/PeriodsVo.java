package com.tuanche.directselling.vo;

import java.io.Serializable;

/**
 * @Author gongbo
 * @Description 团车直卖-场次vo
 * @Date 2020年3月4日17:11:37
 **/
public class PeriodsVo implements Serializable {

    /**
     * 场次ID
     **/
    private Integer id;

    /**
     * 场次标题
     **/
    private String periodsName;

    /**
     * 开始时间
     **/
    private String beginTime;

    /**
     * 结束时间
     **/
    private String endTime;

    /**
     * 活动类型
     */
    private Integer activityType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPeriodsName() {
        return periodsName;
    }

    public void setPeriodsName(String periodsName) {
        this.periodsName = periodsName;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getActivityType() {
        return activityType;
    }

    public void setActivityType(Integer activityType) {
        this.activityType = activityType;
    }
}
