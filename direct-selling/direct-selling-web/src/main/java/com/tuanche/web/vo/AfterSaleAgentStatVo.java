package com.tuanche.web.vo;

import java.io.Serializable;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/7/21 21:12
 **/
public class AfterSaleAgentStatVo implements Serializable {
    /**
     * 代理人ID
     */
    private Integer id;
    /**
     * 微信头像
     */
    private String wxHead;

    /**
     * 姓名
     */
    private String name;

    /**
     * 电话
     */
    private String phone;

    /**
     * 职位
     */
    private String position;

    /**
     * 分享数
     */
    private Integer shareCount;

    /**
     * 浏览次数
     */
    private Integer pageViewCount;

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

    /**
     * 转介绍
     */
    private Integer forwardCount;


    public String getWxHead() {
        return wxHead;
    }

    public void setWxHead(String wxHead) {
        this.wxHead = wxHead;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public Integer getPageViewCount() {
        return pageViewCount;
    }

    public void setPageViewCount(Integer pageViewCount) {
        this.pageViewCount = pageViewCount;
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

    public Integer getForwardCount() {
        return forwardCount;
    }

    public void setForwardCount(Integer forwardCount) {
        this.forwardCount = forwardCount;
    }
}
