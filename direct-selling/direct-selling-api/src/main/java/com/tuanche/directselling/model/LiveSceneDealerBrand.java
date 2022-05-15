package com.tuanche.directselling.model;

import java.io.Serializable;
import java.util.Date;

/**
 * live_scene_dealer_brand
 * @author 
 */
public class LiveSceneDealerBrand implements Serializable {
    private Integer id;

    /**
     * 场次活动ID
     */
    private Integer sceneId;

    /**
     * 经销商ID
     */
    private Integer dealerId;

    /**
     * 经销商名称
     */
    private String dealerName;

    /**
     * 经销商城市ID
     */
    private Integer cityId;

    /**
     * 参展城市ID
     */
    private Integer joinCityId;

    /**
     * 品牌ID
     */
    private Integer brandId;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 车型id集合（0为全部车型 ）
     */
    private String styleIds;

    /**
     * 添加人ID
     */
    private Integer createUserId;

    /**
     * 添加人姓名
     */
    private String createUserName;

    /**
     * 添加时间
     */
    private Date createDt;

    /**
     * 编辑人ID
     */
    private Integer updateUserId;

    /**
     * 编辑人姓名
     */
    private String updateUserName;

    /**
     * 编辑时间
     */
    private Date updateDt;

    /**
     * 删除状态（1:是  0:否）
     */
    private Boolean deleteState;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getStyleIds() {
        return styleIds;
    }

    public void setStyleIds(String styleIds) {
        this.styleIds = styleIds;
    }


    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public Integer getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Boolean getDeleteState() {
        return deleteState;
    }

    public void setDeleteState(Boolean deleteState) {
        this.deleteState = deleteState;
    }

    public Integer getJoinCityId() {
        return joinCityId;
    }

    public void setJoinCityId(Integer joinCityId) {
        this.joinCityId = joinCityId;
    }
}