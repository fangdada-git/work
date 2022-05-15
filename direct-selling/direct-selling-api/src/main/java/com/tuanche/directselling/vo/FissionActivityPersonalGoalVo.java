package com.tuanche.directselling.vo;

import java.io.Serializable;

/**
 * 活动详情页 个人目标完成情况
 *
 * @author ZhangYiXin
 * @version 1.0
 * @date 2020/9/25 16:48
 **/
public class FissionActivityPersonalGoalVo implements Serializable {

    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 完成的任务量
     */
    private Integer finishTaskTotal;

    /**
     * 基础目标 需要销售完成的最低邀请人数
     */
    private Integer awardRule;

    /**
     * 是否完成任务目标 0：否 1：是
     */
    private Integer whetherCompleteTask;

    /**
     * 还差n个完成任务
     */
    private Integer gapGoal;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getFinishTaskTotal() {
        return finishTaskTotal;
    }

    public void setFinishTaskTotal(Integer finishTaskTotal) {
        this.finishTaskTotal = finishTaskTotal;
    }

    public Integer getAwardRule() {
        return awardRule;
    }

    public void setAwardRule(Integer awardRule) {
        this.awardRule = awardRule;
    }

    public Integer getWhetherCompleteTask() {
        return whetherCompleteTask;
    }

    public void setWhetherCompleteTask(Integer whetherCompleteTask) {
        this.whetherCompleteTask = whetherCompleteTask;
    }

    public Integer getGapGoal() {
        return gapGoal;
    }

    public void setGapGoal(Integer gapGoal) {
        this.gapGoal = gapGoal;
    }
}
