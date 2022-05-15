package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.FissionActivityExtend;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author：HuangHao
 * @CreatTime 2021-04-14 11:54
 */
public class FissionActivityExtendDto extends FissionActivityExtend {

    //裂变名称
    private String fissionName;
    //经销商名称
    private String dealerName;
    private String address;
    //活动开始时间
    private Date startTime;
    //活动开始时间-格式化后
    private String startTimeStr;
    //活动结束时间
    private Date endTime;
    //活动结束时间-格式化后
    private String endTimeStr;
    //是否同意协议
    private Boolean agreement;
    //账号类型-格式化后
    private String accountTypeStr;

    //订单总数
    private Integer orderSum=0;
    //订单核销数
    private Integer orderCheckoutSum=0;
    //订单退款数
    private Integer orderRefundSum=0;


    //订单状态
    private String paymentStatusName;
    //活动是否结束
    private Boolean activityIsOver;

    /**
     * 是否已打款 0：否 1：是
     */
    private Boolean paymentStatus;
    //结算账户表ID
    private Integer settlementAccountId;

    /**
     * 账号类型 1：对公账户 2：个人账户
     */
    private Integer accountType;
    /**
     * 应打款金额
     */
    private BigDecimal realIncome;
    //订单金额
    private BigDecimal orderAmount;
    private int page=1;
    private int limit=10;

    public String getPaymentStatusName() {
        if (getPaymentStatus()!=null && getPaymentStatus()) {
            return "已打款";
        }
        return "未打款";
    }

    public String getFissionName() {
        return fissionName;
    }

    public void setFissionName(String fissionName) {
        this.fissionName = fissionName;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public Boolean getAgreement() {
        return agreement;
    }

    public void setAgreement(Boolean agreement) {
        this.agreement = agreement;
    }

    public String getAccountTypeStr() {
        return accountTypeStr;
    }

    public void setAccountTypeStr(String accountTypeStr) {
        this.accountTypeStr = accountTypeStr;
    }

    public Integer getOrderSum() {
        return orderSum;
    }

    public void setOrderSum(Integer orderSum) {
        this.orderSum = orderSum;
    }

    public Integer getOrderCheckoutSum() {
        return orderCheckoutSum;
    }

    public void setOrderCheckoutSum(Integer orderCheckoutSum) {
        this.orderCheckoutSum = orderCheckoutSum;
    }

    public Integer getOrderRefundSum() {
        return orderRefundSum;
    }

    public void setOrderRefundSum(Integer orderRefundSum) {
        this.orderRefundSum = orderRefundSum;
    }

    public void setPaymentStatusName(String paymentStatusName) {
        this.paymentStatusName = paymentStatusName;
    }

    public Boolean getActivityIsOver() {
        return activityIsOver;
    }

    public void setActivityIsOver(Boolean activityIsOver) {
        this.activityIsOver = activityIsOver;
    }

    public BigDecimal getRealIncome() {
        return realIncome;
    }

    public void setRealIncome(BigDecimal realIncome) {
        this.realIncome = realIncome;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Integer getSettlementAccountId() {
        return settlementAccountId;
    }

    public void setSettlementAccountId(Integer settlementAccountId) {
        this.settlementAccountId = settlementAccountId;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }
}
