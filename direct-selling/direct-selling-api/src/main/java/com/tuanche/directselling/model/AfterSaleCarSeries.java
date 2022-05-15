package com.tuanche.directselling.model;

import java.util.Date;

/**
    * 售后特卖-原车系-团车车系对应关系
    */
public class AfterSaleCarSeries {
    private Integer id;

    /**
    * 品牌id
    */
    private Integer brandId;

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
    * 是否删除 0未删除 1 删除
    */
    private Boolean deleteFlag;

    /**
    * 创建时间
    */
    private Date createDt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
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
}