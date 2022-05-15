package com.tuanche.directselling.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 活动详情页 个人成绩
 *
 * @author ZhangYiXin
 * @version 1.0
 * @date 2020/9/25 16:48
 **/
public class FissionActivityPersonalIntegralVo implements Serializable {

    private Integer id;

    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 完成的任务量
     */
    private Integer finishTaskTotal;

    /**
     * 完成任务积分
     */
    private Integer taskIntegral;

    /**
     * 单次完成积分奖励
     */
    private Integer singleIntegral;

    /**
     * 基础目标 需要销售完成的最低邀请人数
     */
    private Integer awardRule;

    /**
     * 是否完成任务目标 0：否 1：是
     */
    private Integer whetherCompleteTask;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getTaskIntegral() {
        return taskIntegral;
    }

    public void setTaskIntegral(Integer taskIntegral) {
        this.taskIntegral = taskIntegral;
    }

    public Integer getAwardRule() {
        return awardRule;
    }

    public void setAwardRule(Integer awardRule) {
        this.awardRule = awardRule;
    }

    public Integer getSingleIntegral() {
        return singleIntegral;
    }

    public void setSingleIntegral(Integer singleIntegral) {
        this.singleIntegral = singleIntegral;
    }

    public Integer getWhetherCompleteTask() {
        return whetherCompleteTask;
    }

    public void setWhetherCompleteTask(Integer whetherCompleteTask) {
        this.whetherCompleteTask = whetherCompleteTask;
    }
}
