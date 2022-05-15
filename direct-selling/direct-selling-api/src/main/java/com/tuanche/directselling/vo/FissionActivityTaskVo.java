package com.tuanche.directselling.vo;

import java.io.Serializable;

/**
 * 裂变活动任务
 */
public class FissionActivityTaskVo implements Serializable {

    /**
     * 任务id
     */
    private Integer taskId;
    /**
     * 任务名称
     */
    private String taskName;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
