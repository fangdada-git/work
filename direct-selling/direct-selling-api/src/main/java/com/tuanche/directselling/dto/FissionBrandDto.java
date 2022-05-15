package com.tuanche.directselling.dto;

import java.io.Serializable;
import java.util.List;

public class FissionBrandDto implements Serializable {

    private Integer brandId;
    private String brandName;
    //车型列表
    private List<FissionStyleDto> styleList;

    public FissionBrandDto(Integer brandId, String brandName, List<FissionStyleDto> styleList) {
        this.brandId = brandId;
        this.brandName = brandName;
        this.styleList = styleList;
    }

    public FissionBrandDto() {}

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

    public List<FissionStyleDto> getStyleList() {
        return styleList;
    }

    public void setStyleList(List<FissionStyleDto> styleList) {
        this.styleList = styleList;
    }
}
