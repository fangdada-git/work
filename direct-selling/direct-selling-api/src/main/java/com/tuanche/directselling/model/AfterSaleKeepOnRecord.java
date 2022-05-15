package com.tuanche.directselling.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 售后特卖-经销商备案表
 */
public class AfterSaleKeepOnRecord implements Serializable {
    /**
     * after_sale_keep_on_record
     */
    private static final long serialVersionUID = 1L;
    private Integer id;

    /**
     * 活动ID
     */
    private Integer activityId;

    /**
     * 经销商id
     */
    private Integer dealerId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 车牌号
     */
    private String licencePlate;

    /**
     * 手机号
     */
    private String userPhone;

    /**
     * 城市
     */
    private String cityName;

    /**
     * 导入的品牌名
     */
    private String originalBrandName;

    /**
     * 品牌id
     */
    private Integer brandId;

    /**
     * 匹配后的品牌名
     */
    private String brandName;

    /**
     * 导入的车系
     */
    private String originalCarSeriesName;

    /**
     * 车系id
     */
    private Integer carSeriesId;

    /**
     * 匹配后的车系名称
     */
    private String carSeriesName;

    /**
     * 购车时间
     */
    private Date buyCarTime;

    /**
     * 最后进店时间
     */
    private Date latestEnterTime;

    /**
     * 0:备案用户 1:流失用户
     */
    private Byte userType;

    /**
     * 同步状态 0未同步 1已同步
     */
    private Byte syncStatus;

    /**
     * 流失客户数据源 0:本店 1他店
     */
    private Byte dataSource;

    /**
     * 同步时间
     */
    private Date syncTime;

    /**
     * 品牌 车系匹配时间
     */
    private Date matchingTime;

    /**
     * 是否删除 0未删除 1 删除
     */
    private Boolean deleteFlag;

    /**
     * 创建时间
     */
    private Date createDt;

    /**
     * 创建人
     */
    private String createName;

    /**
     * 更新时间
     */
    private Date updateDt;

    /**
     * 更新人
     */
    private String updateName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getOriginalBrandName() {
        return originalBrandName;
    }

    public void setOriginalBrandName(String originalBrandName) {
        this.originalBrandName = originalBrandName;
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

    public String getOriginalCarSeriesName() {
        return originalCarSeriesName;
    }

    public void setOriginalCarSeriesName(String originalCarSeriesName) {
        this.originalCarSeriesName = originalCarSeriesName;
    }

    public Integer getCarSeriesId() {
        return carSeriesId;
    }

    public void setCarSeriesId(Integer carSeriesId) {
        this.carSeriesId = carSeriesId;
    }

    public String getCarSeriesName() {
        return carSeriesName;
    }

    public void setCarSeriesName(String carSeriesName) {
        this.carSeriesName = carSeriesName;
    }

    public Date getBuyCarTime() {
        return buyCarTime;
    }

    public void setBuyCarTime(Date buyCarTime) {
        this.buyCarTime = buyCarTime;
    }

    public Date getLatestEnterTime() {
        return latestEnterTime;
    }

    public void setLatestEnterTime(Date latestEnterTime) {
        this.latestEnterTime = latestEnterTime;
    }

    public Byte getUserType() {
        return userType;
    }

    public void setUserType(Byte userType) {
        this.userType = userType;
    }

    public Byte getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(Byte syncStatus) {
        this.syncStatus = syncStatus;
    }

    public Date getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(Date syncTime) {
        this.syncTime = syncTime;
    }

    public Date getMatchingTime() {
        return matchingTime;
    }

    public void setMatchingTime(Date matchingTime) {
        this.matchingTime = matchingTime;
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public Byte getDataSource() {
        return dataSource;
    }

    public void setDataSource(Byte dataSource) {
        this.dataSource = dataSource;
    }
}