package com.tuanche.directselling.model;

import java.io.Serializable;
import java.util.Date;

/**
 * C端数据概览
 */
public class FissionDataView implements Serializable {
    private Long id;

    /**
     * 活动ID
     */
    private Integer fissionId;

    /**
     * 浏览总人数
     */
    private Integer pageView;

    /**
     * 浏览总人次
     */
    private Integer uniqueVisitor;

    /**
     * 分享次数
     */
    private Integer shareCount;

    /**
     * 分享海报次数
     */
    private Integer sharePosterCount;

    /**
     * 分享页面次数
     */
    private Integer sharePageCount;

    /**
     * 预约直播间总人数
     */
    private Integer subscribeUserCount;

    /**
     * 预约直播间进入直播间总人数
     */
    private Integer subscribeLiveUserCount;

    /**
     * 直播间总人数
     */
    private Integer liveUserCount;

    /**
     * 直播间商品购买人数
     */
    private Integer liveProductUserCount;

    /**
     * 直播间商品购买数
     */
    private Integer liveProductCount;

    /**
     * 活动页面商品购买人数
     */
    private Integer activityProductUserCount;

    /**
     * 活动页面商品购买数
     */
    private Integer activityOrderCount;

    /**
     * 是否删除 0未删除 1 删除
     */
    private Boolean deleteFlag;

    /**
     * 创建时间
     */
    private Date createDt;

    /**
     * 修改时间
     */
    private Date updateDt;

    /**
     * 0:B端数据 1:C端数据 2:不带销售标识 3:用户运营渠道 4:销售渠道
     */
    private Integer dataType;

    /**
     * 是否是分享者的数据0:否 1:是
     */
    private boolean isShare;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFissionId() {
        return fissionId;
    }

    public void setFissionId(Integer fissionId) {
        this.fissionId = fissionId;
    }

    public Integer getPageView() {
        return pageView;
    }

    public void setPageView(Integer pageView) {
        this.pageView = pageView;
    }

    public Integer getUniqueVisitor() {
        return uniqueVisitor;
    }

    public void setUniqueVisitor(Integer uniqueVisitor) {
        this.uniqueVisitor = uniqueVisitor;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public Integer getSharePosterCount() {
        return sharePosterCount;
    }

    public void setSharePosterCount(Integer sharePosterCount) {
        this.sharePosterCount = sharePosterCount;
    }

    public Integer getSharePageCount() {
        return sharePageCount;
    }

    public void setSharePageCount(Integer sharePageCount) {
        this.sharePageCount = sharePageCount;
    }

    public Integer getSubscribeUserCount() {
        return subscribeUserCount;
    }

    public void setSubscribeUserCount(Integer subscribeUserCount) {
        this.subscribeUserCount = subscribeUserCount;
    }

    public Integer getSubscribeLiveUserCount() {
        return subscribeLiveUserCount;
    }

    public void setSubscribeLiveUserCount(Integer subscribeLiveUserCount) {
        this.subscribeLiveUserCount = subscribeLiveUserCount;
    }

    public Integer getLiveUserCount() {
        return liveUserCount;
    }

    public void setLiveUserCount(Integer liveUserCount) {
        this.liveUserCount = liveUserCount;
    }

    public Integer getLiveProductUserCount() {
        return liveProductUserCount;
    }

    public void setLiveProductUserCount(Integer liveProductUserCount) {
        this.liveProductUserCount = liveProductUserCount;
    }

    public Integer getLiveProductCount() {
        return liveProductCount;
    }

    public void setLiveProductCount(Integer liveProductCount) {
        this.liveProductCount = liveProductCount;
    }

    public Integer getActivityProductUserCount() {
        return activityProductUserCount;
    }

    public void setActivityProductUserCount(Integer activityProductUserCount) {
        this.activityProductUserCount = activityProductUserCount;
    }

    public Integer getActivityOrderCount() {
        return activityOrderCount;
    }

    public void setActivityOrderCount(Integer activityOrderCount) {
        this.activityOrderCount = activityOrderCount;
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public boolean isShare() {
        return isShare;
    }

    public void setShare(boolean share) {
        isShare = share;
    }
}