package com.tuanche.directselling.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/10/12 16:01
 **/
public class AfterSaleKeepOnRecordBrandCarSeriesDto implements Serializable {
    private Integer id;
    /**
     * 活动ID
     */
    private Integer activityId;
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
     * 0:全部 1: 品牌未匹配 2: 车系未匹配
     */
    private Integer status;

    /**
     * 匹配时间
     */
    private Date matchingTime;

    /**
     * 同步状态 0未同步 1已同步
     */
    private Byte syncStatus;

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getMatchingTime() {
        return matchingTime;
    }

    public void setMatchingTime(Date matchingTime) {
        this.matchingTime = matchingTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(Byte syncStatus) {
        this.syncStatus = syncStatus;
    }
}
