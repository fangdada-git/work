package com.tuanche.directselling.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * fission_sale
 * @author
 */
public class FissionSale implements Serializable {
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
     * 销售姓名
     */
    private String saleName;

    /**
     * 销售支付微信openid
     */
    private String saleWxOpenId;
    /**
     * 销售支付微信UnionId
     */
    private String saleWxUnionId;

    /**
     * 支付金额（对赌金额）
     */
    private BigDecimal payAmount;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 实际收入金额
     */
    private BigDecimal realIncome;

    /**
     * 是否计算了实际收入 0：否 1：是
     */
    private Boolean calculationRealIncome;
    /**
     * 预计收入金额
     */
    private BigDecimal estimatedIncome;

    /**
     * 是否完成任务目标 0：否 1：是
     */
    private Boolean whetherCompleteTask;

    /**
     * 财务审核状态 0：未审核 1：财务已审核 2:  财务已打款
     */
    private Integer financialAuditStatus;

    /**
     * 提现状态 0：未提现 1：已提现
     */
    private Integer withdrawalState;

    /**
     * 提现时间
     */
    private Date withdrawalTime;

    /**
     * 交易记录id
     */
    private Long tradeRecordId;

    /**
     * 任务总积分
     */
    private Integer taskIntegral;

    /**
     * 是否删除 0未删除 1 删除
     */
    private Boolean deleteFlag;

    /**
     * 创建时间
     */
    private Date createDt;

    /**
     * 创建人
     */
    private String createName;

    /**
     * 更新时间
     */
    private Date updateDt;

    /**
     * 更新人
     */
    private String updateName;

    /**
     * 审核人
     */
    private String auditName;

    /**
     * 打款人
     */
    private String payName;

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

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }

    public String getSaleWxOpenId() {
        return saleWxOpenId;
    }

    public void setSaleWxOpenId(String saleWxOpenId) {
        this.saleWxOpenId = saleWxOpenId;
    }

    public String getSaleWxUnionId() {
        return saleWxUnionId;
    }

    public void setSaleWxUnionId(String saleWxUnionId) {
        this.saleWxUnionId = saleWxUnionId;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getRealIncome() {
        return realIncome;
    }

    public void setRealIncome(BigDecimal realIncome) {
        this.realIncome = realIncome;
    }

    public Boolean getCalculationRealIncome() {
        return calculationRealIncome;
    }

    public void setCalculationRealIncome(Boolean calculationRealIncome) {
        this.calculationRealIncome = calculationRealIncome;
    }

    public BigDecimal getEstimatedIncome() {
        return estimatedIncome;
    }

    public void setEstimatedIncome(BigDecimal estimatedIncome) {
        this.estimatedIncome = estimatedIncome;
    }


    public Boolean getWhetherCompleteTask() {
        return whetherCompleteTask;
    }

    public void setWhetherCompleteTask(Boolean whetherCompleteTask) {
        this.whetherCompleteTask = whetherCompleteTask;
    }

    public Integer getFinancialAuditStatus() {
        return financialAuditStatus;
    }

    public void setFinancialAuditStatus(Integer financialAuditStatus) {
        this.financialAuditStatus = financialAuditStatus;
    }

    public Integer getWithdrawalState() {
        return withdrawalState;
    }

    public void setWithdrawalState(Integer withdrawalState) {
        this.withdrawalState = withdrawalState;
    }

    public Date getWithdrawalTime() {
        return withdrawalTime;
    }

    public void setWithdrawalTime(Date withdrawalTime) {
        this.withdrawalTime = withdrawalTime;
    }

    public Long getTradeRecordId() {
        return tradeRecordId;
    }

    public void setTradeRecordId(Long tradeRecordId) {
        this.tradeRecordId = tradeRecordId;
    }

    public Integer getTaskIntegral() {
        return taskIntegral;
    }

    public void setTaskIntegral(Integer taskIntegral) {
        this.taskIntegral = taskIntegral;
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

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
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
        FissionSale other = (FissionSale) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getFissionId() == null ? other.getFissionId() == null : this.getFissionId().equals(other.getFissionId()))
                && (this.getDealerId() == null ? other.getDealerId() == null : this.getDealerId().equals(other.getDealerId()))
                && (this.getSaleId() == null ? other.getSaleId() == null : this.getSaleId().equals(other.getSaleId()))
                && (this.getSaleName() == null ? other.getSaleName() == null : this.getSaleName().equals(other.getSaleName()))
                && (this.getSaleWxOpenId() == null ? other.getSaleWxOpenId() == null : this.getSaleWxOpenId().equals(other.getSaleWxOpenId()))
                && (this.getPayAmount() == null ? other.getPayAmount() == null : this.getPayAmount().equals(other.getPayAmount()))
                && (this.getOrderNo() == null ? other.getOrderNo() == null : this.getOrderNo().equals(other.getOrderNo()))
                && (this.getEstimatedIncome() == null ? other.getEstimatedIncome() == null : this.getEstimatedIncome().equals(other.getEstimatedIncome()))
                && (this.getRealIncome() == null ? other.getRealIncome() == null : this.getRealIncome().equals(other.getRealIncome()))
                && (this.getWhetherCompleteTask() == null ? other.getWhetherCompleteTask() == null : this.getWhetherCompleteTask().equals(other.getWhetherCompleteTask()))
                && (this.getFinancialAuditStatus() == null ? other.getFinancialAuditStatus() == null : this.getFinancialAuditStatus().equals(other.getFinancialAuditStatus()))
                && (this.getWithdrawalState() == null ? other.getWithdrawalState() == null : this.getWithdrawalState().equals(other.getWithdrawalState()))
                && (this.getWithdrawalTime() == null ? other.getWithdrawalTime() == null : this.getWithdrawalTime().equals(other.getWithdrawalTime()))
                && (this.getTradeRecordId() == null ? other.getTradeRecordId() == null : this.getTradeRecordId().equals(other.getTradeRecordId()))
                && (this.getTaskIntegral() == null ? other.getTaskIntegral() == null : this.getTaskIntegral().equals(other.getTaskIntegral()))
                && (this.getDeleteFlag() == null ? other.getDeleteFlag() == null : this.getDeleteFlag().equals(other.getDeleteFlag()))
                && (this.getCreateDt() == null ? other.getCreateDt() == null : this.getCreateDt().equals(other.getCreateDt()))
                && (this.getCreateName() == null ? other.getCreateName() == null : this.getCreateName().equals(other.getCreateName()))
                && (this.getUpdateDt() == null ? other.getUpdateDt() == null : this.getUpdateDt().equals(other.getUpdateDt()))
                && (this.getUpdateName() == null ? other.getUpdateName() == null : this.getUpdateName().equals(other.getUpdateName()))
                && (this.getAuditName() == null ? other.getAuditName() == null : this.getAuditName().equals(other.getAuditName()))
                && (this.getPayName() == null ? other.getPayName() == null : this.getPayName().equals(other.getPayName()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getFissionId() == null) ? 0 : getFissionId().hashCode());
        result = prime * result + ((getDealerId() == null) ? 0 : getDealerId().hashCode());
        result = prime * result + ((getSaleId() == null) ? 0 : getSaleId().hashCode());
        result = prime * result + ((getSaleName() == null) ? 0 : getSaleName().hashCode());
        result = prime * result + ((getSaleWxOpenId() == null) ? 0 : getSaleWxOpenId().hashCode());
        result = prime * result + ((getPayAmount() == null) ? 0 : getPayAmount().hashCode());
        result = prime * result + ((getOrderNo() == null) ? 0 : getOrderNo().hashCode());
        result = prime * result + ((getEstimatedIncome() == null) ? 0 : getEstimatedIncome().hashCode());
        result = prime * result + ((getRealIncome() == null) ? 0 : getRealIncome().hashCode());
        result = prime * result + ((getWhetherCompleteTask() == null) ? 0 : getWhetherCompleteTask().hashCode());
        result = prime * result + ((getFinancialAuditStatus() == null) ? 0 : getFinancialAuditStatus().hashCode());
        result = prime * result + ((getWithdrawalState() == null) ? 0 : getWithdrawalState().hashCode());
        result = prime * result + ((getWithdrawalTime() == null) ? 0 : getWithdrawalTime().hashCode());
        result = prime * result + ((getTradeRecordId() == null) ? 0 : getTradeRecordId().hashCode());
        result = prime * result + ((getTaskIntegral() == null) ? 0 : getTaskIntegral().hashCode());
        result = prime * result + ((getDeleteFlag() == null) ? 0 : getDeleteFlag().hashCode());
        result = prime * result + ((getCreateDt() == null) ? 0 : getCreateDt().hashCode());
        result = prime * result + ((getCreateName() == null) ? 0 : getCreateName().hashCode());
        result = prime * result + ((getUpdateDt() == null) ? 0 : getUpdateDt().hashCode());
        result = prime * result + ((getUpdateName() == null) ? 0 : getUpdateName().hashCode());
        result = prime * result + ((getAuditName() == null) ? 0 : getAuditName().hashCode());
        result = prime * result + ((getPayName() == null) ? 0 : getPayName().hashCode());
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
        sb.append(", dealerId=").append(dealerId);
        sb.append(", saleId=").append(saleId);
        sb.append(", saleName=").append(saleName);
        sb.append(", saleWxOpenId=").append(saleWxOpenId);
        sb.append(", payAmount=").append(payAmount);
        sb.append(", orderNo=").append(orderNo);
        sb.append(", estimatedIncome=").append(estimatedIncome);
        sb.append(", realIncome=").append(realIncome);
        sb.append(", whetherCompleteTask=").append(whetherCompleteTask);
        sb.append(", financialAuditStatus=").append(financialAuditStatus);
        sb.append(", withdrawalState=").append(withdrawalState);
        sb.append(", withdrawalTime=").append(withdrawalTime);
        sb.append(", tradeRecordId=").append(tradeRecordId);
        sb.append(", taskIntegral=").append(taskIntegral);
        sb.append(", deleteFlag=").append(deleteFlag);
        sb.append(", createDt=").append(createDt);
        sb.append(", createName=").append(createName);
        sb.append(", updateDt=").append(updateDt);
        sb.append(", updateName=").append(updateName);
        sb.append(", auditName=").append(auditName);
        sb.append(", payName=").append(payName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}