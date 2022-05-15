package com.tuanche.directselling.model;

import java.util.Date;

/**
    * 售后特卖-原品牌-团车品牌对应关系
    */
public class AfterSaleBrand {
    private Integer id;

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