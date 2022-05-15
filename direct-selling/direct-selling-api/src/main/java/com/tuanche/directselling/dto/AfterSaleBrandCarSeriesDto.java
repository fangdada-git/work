package com.tuanche.directselling.dto;

import java.io.Serializable;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/10/19 20:13
 **/
public class AfterSaleBrandCarSeriesDto implements Serializable {
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
}
