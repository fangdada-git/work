package com.tuanche.directselling.pojo;

import java.io.Serializable;

/**
 * @class: SceneSyncDto
 * @description: 场次活动同步实体
 * @author: gongbo
 * @create: 2020年3月4日17:33:49
 */
public class SceneSyncDto implements Serializable {

    /**
     * id
     */
    private Integer id;
    /**
     * 场次活动名称
     */
    private String title;
    /**
     * 城市
     */
    private Integer cityId;
    /**
     * 开始时间  new date()转换
     */
    private Long beginTime;
    /**
     * 结束时间
     */
    private Long endTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
