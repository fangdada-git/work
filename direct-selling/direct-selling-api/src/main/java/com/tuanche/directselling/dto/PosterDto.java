package com.tuanche.directselling.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: czy
 * @Date: 2020/4/15 15:29
 * @Version 1.0
 */
public class PosterDto implements Serializable {
    private Integer cityId;
    private String cityName;
    private Integer brandId;
    private String brandName;
    //经销商id
    private Integer merchantId;
    //经销商名称
    private String store;
    //礼品 类型为海报预热则要传此字段
    private String present;
    //节目时间  3月21日13：30-15：00
    private String activeTime;
    //活动链接，以https开头url
    private String activeUrl;
    //图片模板类型 6 海报预热 7 海报正式
    private Integer type;
    private Date beginTime;
    private Date endTime;
    private Integer sceneId;

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

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public String getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(String activeTime) {
        this.activeTime = activeTime;
    }

    public String getActiveUrl() {
        return activeUrl;
    }

    public void setActiveUrl(String activeUrl) {
        this.activeUrl = activeUrl;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getSceneId() {
        return sceneId;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }

}