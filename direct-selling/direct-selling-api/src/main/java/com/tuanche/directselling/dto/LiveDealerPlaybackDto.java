package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.LiveDealerPlayback;

import java.io.Serializable;

/**
 * @ClassName: LiveSceneDto
 * @Description: 团车直卖-经销商回放Dto
 * @Author: GongBo
 * @Date: 2020/3/5 9:41
 * @Version 1.0
 **/
public class LiveDealerPlaybackDto extends LiveDealerPlayback implements Serializable {

    /**
     * 活动名称
     **/
    private String activityName;

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
