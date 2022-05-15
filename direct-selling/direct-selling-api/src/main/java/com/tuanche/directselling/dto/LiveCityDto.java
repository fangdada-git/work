package com.tuanche.directselling.dto;

import java.io.Serializable;

/**
 * @program: direct-selling
 * @description: ${description}
 * @author: fxq
 * @create: 2020-04-07 17:18
 **/
public class LiveCityDto implements Serializable {

    private static final long serialVersionUID = -3548859510912234398L;

    private Integer cityId;

    private String cityName;

    private String first;

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }
}
