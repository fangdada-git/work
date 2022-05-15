package com.tuanche.directselling.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * fission_user_reward
 * @author 
 */
public class FissionUserReward implements Serializable {
    private Integer id;

    /**
     * 裂变活动ID
     */
    private Integer fissionId;

    /**
     * 用户微信UnionId
     */
    private String userWxUnionId;

    /**
     * 任务ID
     */
    private Integer taskId;

    /**
     * 奖励金额
     */
    private BigDecimal rewardAmount;

    /**
     * 是否已打款 0未打款 1 已打款
     */
    private Boolean paymentOrNot;

    /**
     * 任务记录表（fission_user_task_record）id
     */
    private Long taskRecordId;

    /**
     * 打款记录ID
     */
    private Long tradeRecordId;

    /**
     * 是否删除 0未删除 1 删除
     */
    private Boolean deleteFlag;

    /**
     * 创建时间
     */
    private Date createDt;

    /**
     * 更新时间
     */
    private Date updateDt;

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

    public String getUserWxUnionId() {
        return userWxUnionId;
    }

    public void setUserWxUnionId(String userWxUnionId) {
        this.userWxUnionId = userWxUnionId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public BigDecimal getRewardAmount() {
        return rewardAmount;
    }

    public void setRewardAmount(BigDecimal rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    public Boolean getPaymentOrNot() {
        return paymentOrNot;
    }

    public void setPaymentOrNot(Boolean paymentOrNot) {
        this.paymentOrNot = paymentOrNot;
    }

    public Long getTaskRecordId() {
        return taskRecordId;
    }

    public void setTaskRecordId(Long taskRecordId) {
        this.taskRecordId = taskRecordId;
    }

    public Long getTradeRecordId() {
        return tradeRecordId;
    }

    public void setTradeRecordId(Long tradeRecordId) {
        this.tradeRecordId = tradeRecordId;
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

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        FissionUserReward other = (FissionUserReward) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getFissionId() == null ? other.getFissionId() == null : this.getFissionId().equals(other.getFissionId()))
            && (this.getUserWxUnionId() == null ? other.getUserWxUnionId() == null : this.getUserWxUnionId().equals(other.getUserWxUnionId()))
            && (this.getTaskId() == null ? other.getTaskId() == null : this.getTaskId().equals(other.getTaskId()))
            && (this.getRewardAmount() == null ? other.getRewardAmount() == null : this.getRewardAmount().equals(other.getRewardAmount()))
            && (this.getPaymentOrNot() == null ? other.getPaymentOrNot() == null : this.getPaymentOrNot().equals(other.getPaymentOrNot()))
            && (this.getTaskRecordId() == null ? other.getTaskRecordId() == null : this.getTaskRecordId().equals(other.getTaskRecordId()))
            && (this.getTradeRecordId() == null ? other.getTradeRecordId() == null : this.getTradeRecordId().equals(other.getTradeRecordId()))
            && (this.getDeleteFlag() == null ? other.getDeleteFlag() == null : this.getDeleteFlag().equals(other.getDeleteFlag()))
            && (this.getCreateDt() == null ? other.getCreateDt() == null : this.getCreateDt().equals(other.getCreateDt()))
            && (this.getUpdateDt() == null ? other.getUpdateDt() == null : this.getUpdateDt().equals(other.getUpdateDt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getFissionId() == null) ? 0 : getFissionId().hashCode());
        result = prime * result + ((getUserWxUnionId() == null) ? 0 : getUserWxUnionId().hashCode());
        result = prime * result + ((getTaskId() == null) ? 0 : getTaskId().hashCode());
        result = prime * result + ((getRewardAmount() == null) ? 0 : getRewardAmount().hashCode());
        result = prime * result + ((getPaymentOrNot() == null) ? 0 : getPaymentOrNot().hashCode());
        result = prime * result + ((getTaskRecordId() == null) ? 0 : getTaskRecordId().hashCode());
        result = prime * result + ((getTradeRecordId() == null) ? 0 : getTradeRecordId().hashCode());
        result = prime * result + ((getDeleteFlag() == null) ? 0 : getDeleteFlag().hashCode());
        result = prime * result + ((getCreateDt() == null) ? 0 : getCreateDt().hashCode());
        result = prime * result + ((getUpdateDt() == null) ? 0 : getUpdateDt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", fissionId=").append(fissionId);
        sb.append(", userWxUnionId=").append(userWxUnionId);
        sb.append(", taskId=").append(taskId);
        sb.append(", rewardAmount=").append(rewardAmount);
        sb.append(", paymentOrNot=").append(paymentOrNot);
        sb.append(", taskRecordId=").append(taskRecordId);
        sb.append(", tradeRecordId=").append(tradeRecordId);
        sb.append(", deleteFlag=").append(deleteFlag);
        sb.append(", createDt=").append(createDt);
        sb.append(", updateDt=").append(updateDt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}