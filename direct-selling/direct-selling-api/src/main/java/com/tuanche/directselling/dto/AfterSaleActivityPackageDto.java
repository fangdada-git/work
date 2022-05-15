package com.tuanche.directselling.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tuanche.directselling.model.AfterSaleActivityPackage;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AfterSaleActivityPackageDto extends AfterSaleActivityPackage {

    //已售数量
    private Integer soldNumber;
    private Integer dealerId;
    private String dealerName;
    private String shortName;
    private String dealerAddress;
    private String dealerTel;
    /**
     * 经销商品牌
     */
    private Integer brandId;
    private Integer masterBrandId;


    //头图
    private List<FissionGoodsImageDto> headImages;
    //详情图
    private List<FissionGoodsImageDto> detailImages;

    //卡券数量
    private Integer couponCount;

    //描述
    private String description;
    //套餐标签
    private List<AfterSaleActivityPackageLabelDto> labels;
    /**
     * 一级标签id
     */
    private Integer primaryLabelId;

    /**
     * 二级标签id
     */
    private Integer secondaryLabelId;

    public Integer getSoldNumber() {
        return soldNumber;
    }

    public void setSoldNumber(Integer soldNumber) {
        this.soldNumber = soldNumber;
    }

    public List<FissionGoodsImageDto> getHeadImages() {
        return headImages;
    }

    public void setHeadImages(List<FissionGoodsImageDto> headImages) {
        this.headImages = headImages;
    }

    public Integer getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(Integer couponCount) {
        this.couponCount = couponCount;
    }

    public List<FissionGoodsImageDto> getDetailImages() {
        return detailImages;
    }

    public void setDetailImages(List<FissionGoodsImageDto> detailImages) {
        this.detailImages = detailImages;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDealerAddress() {
        return dealerAddress;
    }

    public void setDealerAddress(String dealerAddress) {
        this.dealerAddress = dealerAddress;
    }

    public String getDealerTel() {
        return dealerTel;
    }

    public void setDealerTel(String dealerTel) {
        this.dealerTel = dealerTel;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getMasterBrandId() {
        return masterBrandId;
    }

    public void setMasterBrandId(Integer masterBrandId) {
        this.masterBrandId = masterBrandId;
    }

    public List<AfterSaleActivityPackageLabelDto> getLabels() {
        return labels;
    }

    public void setLabels(List<AfterSaleActivityPackageLabelDto> labels) {
        this.labels = labels;
    }

    public Integer getPrimaryLabelId() {
        return primaryLabelId;
    }

    public void setPrimaryLabelId(Integer primaryLabelId) {
        this.primaryLabelId = primaryLabelId;
    }

    public Integer getSecondaryLabelId() {
        return secondaryLabelId;
    }

    public void setSecondaryLabelId(Integer secondaryLabelId) {
        this.secondaryLabelId = secondaryLabelId;
    }
}
