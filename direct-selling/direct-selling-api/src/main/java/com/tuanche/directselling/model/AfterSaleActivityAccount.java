package com.tuanche.directselling.model;

import java.beans.BeanInfo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * 售后特卖分账信息
 */
public class AfterSaleActivityAccount implements Serializable {
    private Integer id;

    private Integer activityId;

    private Integer dealerId;
    /**
     * 车商商户号
     */
    private String mchId;
    /**
     * 商户主体
     */
    private String mchName;
    /**
     * 分账比例团车
     */
    private BigDecimal subAccountRatio;
    /**
     * 认证资料
     */
    private String approveMaterials;
    /**
     * 认证状态（从左到右 支付状态，分账状态，退款状态 每位0未执行，2失败，1成功  111代表验证通过）
     */
    private String verifyState;
    /**
     * 失败原因
     */
    private String failReason;
    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单金额
     */
    private BigDecimal orderMoney;

    /**
     * 支付流水号
     */
    private String wxTransactionId;
    /**
     * 分账流水号
     */
    private String subAccountTransactionDetailId;
    /**
     * 退款流水号
     */
    private String refundId;
    /**
     * 删除标识（0未删除 1已删除）
     */
    private Boolean deleteFlag;

    private Integer ctreateBy;

    private Date ctreateDt;

    private Integer updateBy;

    private Date updateDt;
    /**
     * 经销商分账比例
     */
    private BigDecimal dealerSubAccountRatio;

    private String approveMaterials1;

    private String approveMaterials2;

    private String approveMaterials3;

    private String approveMaterials4;

    private String approveMaterials5;

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

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getMchName() {
        return mchName;
    }

    public void setMchName(String mchName) {
        this.mchName = mchName;
    }

    public BigDecimal getSubAccountRatio() {
        return subAccountRatio;
    }

    public void setSubAccountRatio(BigDecimal subAccountRatio) {
        this.subAccountRatio = subAccountRatio;
    }

    public String getApproveMaterials() {
        return approveMaterials;
    }

    public void setApproveMaterials(String approveMaterials) {
        this.approveMaterials = approveMaterials;
    }

    public String getVerifyState() {
        return verifyState;
    }

    public void setVerifyState(String verifyState) {
        this.verifyState = verifyState;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getCtreateBy() {
        return ctreateBy;
    }

    public void setCtreateBy(Integer ctreateBy) {
        this.ctreateBy = ctreateBy;
    }

    public Date getCtreateDt() {
        return ctreateDt;
    }

    public void setCtreateDt(Date ctreateDt) {
        this.ctreateDt = ctreateDt;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public String getApproveMaterials1() {
        return approveMaterials1;
    }

    public void setApproveMaterials1(String approveMaterials1) {
        this.approveMaterials1 = approveMaterials1;
    }

    public String getApproveMaterials2() {
        return approveMaterials2;
    }

    public void setApproveMaterials2(String approveMaterials2) {
        this.approveMaterials2 = approveMaterials2;
    }

    public String getApproveMaterials3() {
        return approveMaterials3;
    }

    public void setApproveMaterials3(String approveMaterials3) {
        this.approveMaterials3 = approveMaterials3;
    }

    public String getApproveMaterials4() {
        return approveMaterials4;
    }

    public void setApproveMaterials4(String approveMaterials4) {
        this.approveMaterials4 = approveMaterials4;
    }

    public String getApproveMaterials5() {
        return approveMaterials5;
    }

    public void setApproveMaterials5(String approveMaterials5) {
        this.approveMaterials5 = approveMaterials5;
    }

    public BigDecimal getDealerSubAccountRatio() {
        if (!Objects.isNull(subAccountRatio)) {
            return BigDecimal.valueOf(100L).subtract(subAccountRatio);
        }
        return dealerSubAccountRatio;
    }

    public void setDealerSubAccountRatio(BigDecimal dealerSubAccountRatio) {
        this.dealerSubAccountRatio = dealerSubAccountRatio;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getWxTransactionId() {
        return wxTransactionId;
    }

    public void setWxTransactionId(String wxTransactionId) {
        this.wxTransactionId = wxTransactionId;
    }

    public String getSubAccountTransactionDetailId() {
        return subAccountTransactionDetailId;
    }

    public void setSubAccountTransactionDetailId(String subAccountTransactionDetailId) {
        this.subAccountTransactionDetailId = subAccountTransactionDetailId;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public BigDecimal getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(BigDecimal orderMoney) {
        this.orderMoney = orderMoney;
    }
}