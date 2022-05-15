package com.tuanche.directselling.model;

import java.io.Serializable;
import java.util.Date;

public class FissionSaleTaskStatistics implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     * 裂变活动ID
     */
    private Integer fissionId;

    /**
     * 经销商ID
     */
    private Integer dealerId;

    /**
     * 销售id
     */
    private Integer saleId;

    /**
     * 销售微信UnionId
     */
    private String saleWxUnionId;

    /**
     * 任务ID
     */
    private Integer taskId;

    /**
     * 完成的任务量
     */
    private Integer finishTaskTotal;

    /**
     * 完成任务积分
     */
    private Integer taskIntegral;

    /**
     * 是否完成任务目标 0：否 1：是
     */
    private Boolean whetherCompleteTask;

    /**
     * 创建时间
     */
    private Date createDt;

    /**
     * 更新时间
     */
    private Date updateDt;

    /**
     * 是否删除 0未删除 1 删除
     */
    private Boolean deleteFlag;

    /**
     * fission_sale_task_statistics
     */
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFissionId() {
        return fissionId;
    }

    public void setFissionId(Integer fissionId) {
        this.fissionId = fissionId;
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public Integer getSaleId() {
        return saleId;
    }

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }

    public String getSaleWxUnionId() {
        return saleWxUnionId;
    }

    public void setSaleWxUnionId(String saleWxUnionId) {
        this.saleWxUnionId = saleWxUnionId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
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

    public Boolean getWhetherCompleteTask() {
        return whetherCompleteTask;
    }

    public void setWhetherCompleteTask(Boolean whetherCompleteTask) {
        this.whetherCompleteTask = whetherCompleteTask;
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

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}