package com.tuanche.directselling.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author lvsen
 * @Description 半价车品牌vo
 * @Date 2021/9/17
 **/
public class FashionHalfPriceBrandVo implements Serializable {
    /**
     * 品牌id
     */
    private Integer brandId;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 品牌logo
     */
    private String imageUrl;
    /**
     * 参加日期
     */
    private String joinDateStr;

    private long joinDate;

    /**
     * 活动开始时间
     */
    private Date activityStart;
    /**
     * 活动结束时间
     */
    private Date activityEnd;
    /**
     * 是否为当前日期品牌
     */
    private boolean currentDayFlag;

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isCurrentDayFlag() {
        return currentDayFlag;
    }

    public void setCurrentDayFlag(boolean currentDayFlag) {
        this.currentDayFlag = currentDayFlag;
    }

    public String getJoinDateStr() {
        return joinDateStr;
    }

    public void setJoinDateStr(String joinDateStr) {
        this.joinDateStr = joinDateStr;
    }

    public long getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(long joinDate) {
        this.joinDate = joinDate;
    }

    public Date getActivityStart() {
        return activityStart;
    }

    public void setActivityStart(Date activityStart) {
        this.activityStart = activityStart;
    }

    public Date getActivityEnd() {
        return activityEnd;
    }

    public void setActivityEnd(Date activityEnd) {
        this.activityEnd = activityEnd;
    }
}
