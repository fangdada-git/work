package com.tuanche.directselling.model;

public class AfterSaleAgentStat {
    private Integer id;

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
     * 转介绍
     */
    private Integer forwardCount;

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

    public Integer getForwardCount() {
        return forwardCount;
    }

    public void setForwardCount(Integer forwardCount) {
        this.forwardCount = forwardCount;
    }
}