package com.tuanche.directselling.dto;

import java.io.Serializable;

/**
 * 预约直播人数、预约直播并观看直播人数、观看直播人数
 */
public class FissionSubscribeOrLiveCountDto implements Serializable {
    /**
     * 预约直播人数
     */
    private Integer subscribeCount;
    /**
     * 预约直播并观看直播人数
     */
    private Integer subscribeLiveCount;
    /**
     * 观看直播人数
     */
    private Integer liveCount;

    public Integer getSubscribeCount() {
        return subscribeCount;
    }

    public void setSubscribeCount(Integer subscribeCount) {
        this.subscribeCount = subscribeCount;
    }

    public Integer getSubscribeLiveCount() {
        return subscribeLiveCount;
    }

    public void setSubscribeLiveCount(Integer subscribeLiveCount) {
        this.subscribeLiveCount = subscribeLiveCount;
    }

    public Integer getLiveCount() {
        return liveCount;
    }

    public void setLiveCount(Integer liveCount) {
        this.liveCount = liveCount;
    }
}
