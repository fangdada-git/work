package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.LiveScene;

import java.io.Serializable;
import java.util.List;

/**
 * 场次活动 城市 品牌
 */
public class LiveSceneCityBrandDto extends LiveScene implements Serializable {

    private List<LiveSceneCityDto> cities;
    private List<LiveSceneBrandDto> brands;

    public List<LiveSceneCityDto> getCities() {
        return cities;
    }

    public void setCities(List<LiveSceneCityDto> cities) {
        this.cities = cities;
    }

    public List<LiveSceneBrandDto> getBrands() {
        return brands;
    }

    public void setBrands(List<LiveSceneBrandDto> brands) {
        this.brands = brands;
    }
}
