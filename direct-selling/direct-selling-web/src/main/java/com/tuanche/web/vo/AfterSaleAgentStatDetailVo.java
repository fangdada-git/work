package com.tuanche.web.vo;

import java.io.Serializable;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/7/21 21:12
 **/
public class AfterSaleAgentStatDetailVo implements Serializable {

    /**
     * 分享数
     */
    private Integer shareCount;


    /**
     * 浏览人数
     */
    private Integer uniqueVisitorCount;

    /**
     * 套餐卡售出数
     */
    private Integer packageCardCount;

    /**
     * 时间
     */
    private String dateTime;

    /**
     * 转化率
     */
    private String conversionRate;

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public Integer getUniqueVisitorCount() {
        return uniqueVisitorCount;
    }

    public void setUniqueVisitorCount(Integer uniqueVisitorCount) {
        this.uniqueVisitorCount = uniqueVisitorCount;
    }

    public Integer getPackageCardCount() {
        return packageCardCount;
    }

    public void setPackageCardCount(Integer packageCardCount) {
        this.packageCardCount = packageCardCount;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(String conversionRate) {
        this.conversionRate = conversionRate;
    }
}
