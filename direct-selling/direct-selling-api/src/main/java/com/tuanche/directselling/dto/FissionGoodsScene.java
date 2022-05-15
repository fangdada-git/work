package com.tuanche.directselling.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FissionGoodsScene implements Serializable {

    private Integer fissionId;
    private Integer brandId;
    private String brandLogo;
    private String brandFissionName;

    private String sceneStartDateStr;
    private String sceneEndDateStr;

    private List<FissionGoodsApp> goodsList;

    public Integer getFissionId() {
        return fissionId;
    }

    public void setFissionId(Integer fissionId) {
        this.fissionId = fissionId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandFissionName() {
        return brandFissionName;
    }

    public void setBrandFissionName(String brandFissionName) {
        this.brandFissionName = brandFissionName;
    }

    public String getSceneStartDateStr() {
        return sceneStartDateStr;
    }

    public void setSceneStartDateStr(String sceneStartDateStr) {
        this.sceneStartDateStr = sceneStartDateStr;
    }

    public String getSceneEndDateStr() {
        return sceneEndDateStr;
    }

    public void setSceneEndDateStr(String sceneEndDateStr) {
        this.sceneEndDateStr = sceneEndDateStr;
    }

    public List<FissionGoodsApp> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<FissionGoodsApp> goodsList) {
        this.goodsList = goodsList;
    }


    public String getBrandLogo() {
        return brandLogo;
    }

    public void setBrandLogo(String brandLogo) {
        this.brandLogo = brandLogo;
    }
}
