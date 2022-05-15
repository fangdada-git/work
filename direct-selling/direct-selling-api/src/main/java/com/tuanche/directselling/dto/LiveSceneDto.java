package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.LiveScene;
import com.tuanche.directselling.utils.GlobalConstants;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: LiveSceneDto
 * @Description: 团车直卖-场次活动Dto
 * @Author: GongBo
 * @Date: 2020/3/5 9:41
 * @Version 1.0
 **/
public class LiveSceneDto extends LiveScene implements Serializable {

    /**
     * 城市名称
     **/
    private String cityName;
    /**
     * 场次名称
     **/
    private String periodsName;

    /**
     * 场次活动状态
     **/
    private Integer sceneState;
    /**
     * 城市ids
     **/
    private List<Integer> cityIds;

    /**
     * 城市名称
     **/
    private List<String> cityNames;

    /**
     * 品牌ids
     **/
    private List<Integer> brandIds;

    /**
     * 品牌名称ids
     **/
    private List<String> brandNames;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getSceneState() {
        return sceneState;
    }

    public void setSceneState(Integer sceneState) {
        this.sceneState = sceneState;
    }

    public String getPeriodsName() {
        return periodsName;
    }

    public void setPeriodsName(String periodsName) {
        this.periodsName = periodsName;
    }

    public List<Integer> getCityIds() {
        return cityIds;
    }

    public void setCityIds(List<Integer> cityIds) {
        this.cityIds = cityIds;
    }

    public List<String> getCityNames() {
        return cityNames;
    }

    public void setCityNames(List<String> cityNames) {
        this.cityNames = cityNames;
    }

    public List<Integer> getBrandIds() {
        return brandIds;
    }

    public void setBrandIds(List<Integer> brandIds) {
        this.brandIds = brandIds;
    }

    public List<String> getBrandNames() {
        return brandNames;
    }

    public void setBrandNames(List<String> brandNames) {
        this.brandNames = brandNames;
    }
}
