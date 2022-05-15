package com.tuanche.directselling.model;

import java.io.Serializable;

/**
 * @Author: czy
 * @Date: 2020/4/15 17:14
 * 海报返回实体
 * @Version 1.0
 */
public class PosterModel implements Serializable {
    private Integer cityId;
    private Integer brandId;
    //经销商id
    private Integer merchantId;
    //图片类型  6 海报预热 7 海报正式
    private Integer type;
    //海报url
    private String haibaoUrl;
    //海报预热url
    private String haibaoYureUrl;

    private String dealerName;

    private String cityName;

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getHaibaoUrl() {
        return haibaoUrl;
    }

    public void setHaibaoUrl(String haibaoUrl) {
        this.haibaoUrl = haibaoUrl;
    }

    public String getHaibaoYureUrl() {
        return haibaoYureUrl;
    }

    public void setHaibaoYureUrl(String haibaoYureUrl) {
        this.haibaoYureUrl = haibaoYureUrl;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
