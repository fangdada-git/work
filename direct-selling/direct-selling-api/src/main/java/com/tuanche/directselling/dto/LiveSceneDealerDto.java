package com.tuanche.directselling.dto;

import java.io.Serializable;

/**
 * @ClassName: LiveSceneDealerDto
 * @Description: 场次活动关联经销商dto
 * @Author: GongBo
 * @Date: 2020/3/18 17:08
 * @Version 1.0
 **/
public class LiveSceneDealerDto implements Serializable {

    private Integer id;
    /**
     * 场次活动ID
     **/
    private Integer sceneId;

    /**
     * 城市ID
     **/
    private Integer cityId;

    /**
     * 参展城市ID
     **/
    private Integer joinCityId;

    /**
     * 经销商ID
     **/
    private Integer dealerId;

    /**
     * 场次活动名称
     **/
    private String dealerName;

    /**
     * 经销商门店地址
     **/
    private String address;

    /**
     * 经销商品牌ids
     **/
    private String brandIds;

    /**
     * 经销商车型ids，品牌车型以#分隔
     **/
    private String groupBrandStylesIds;

    /**
     * 场次活动状态
     **/
    private Integer sceneState;
    /**
     * 经销商保量值
     **/
    private Integer ensureSize;

    public Integer getSceneId() {
        return sceneId;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
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

    public String getBrandIds() {
        return brandIds;
    }

    public void setBrandIds(String brandIds) {
        this.brandIds = brandIds;
    }

    public Integer getSceneState() {
        return sceneState;
    }

    public void setSceneState(Integer sceneState) {
        this.sceneState = sceneState;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getEnsureSize() {
        return ensureSize;
    }

    public void setEnsureSize(Integer ensureSize) {
        this.ensureSize = ensureSize;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getJoinCityId() {
        return joinCityId;
    }

    public void setJoinCityId(Integer joinCityId) {
        this.joinCityId = joinCityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGroupBrandStylesIds() {
        return groupBrandStylesIds;
    }

    public void setGroupBrandStylesIds(String groupBrandStylesIds) {
        this.groupBrandStylesIds = groupBrandStylesIds;
    }
}
