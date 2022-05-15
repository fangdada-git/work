package com.tuanche.directselling.model;

import java.io.Serializable;
import java.util.Date;

/**
 * live_dealer_playback
 * @author 
 */
public class LiveDealerPlayback implements Serializable {
    private Integer id;

    /**
     * 直播活动id
     */
    private Integer activityId;

    private Integer dealerId;

    /**
     * 直播回放id--淘宝链接
     */
    private String feedId;

    private Date createTime;

    private Date updateTime;

    /**
     * 操作人id
     */
    private Integer userId;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}