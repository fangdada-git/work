package com.tuanche.directselling.vo;

import java.io.Serializable;

/**
 * 活动列表
 *
 * @author ZhangYiXin
 * @version 1.0
 * @date 2020/9/25 9:49
 **/
public class FissionActivityNameVo implements Serializable {

    /**
     * 活动ID
     */
    private Integer id;

    /**
     * 活动名称
     */
    private String activityName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
