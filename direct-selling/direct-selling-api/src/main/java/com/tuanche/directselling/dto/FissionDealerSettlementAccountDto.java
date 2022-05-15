package com.tuanche.directselling.dto;

import com.tuanche.directselling.enums.BrankAccountType;
import com.tuanche.directselling.model.FissionDealerSettlementAccount;
import com.tuanche.directselling.utils.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author：HuangHao
 * @CreatTime 2021-03-09 11:08
 */
public class FissionDealerSettlementAccountDto extends FissionDealerSettlementAccount {
    //活动名称
    private String activityName;
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
     * 应打款金额
     */
    private BigDecimal realIncome;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    private int page=1;
    private int limit=10;

    public String getPaymentStatusName() {
        if (getPaymentStatus()!=null && getPaymentStatus()) {
            return "已打款";
        }
        return "未打款";
    }
    public String getAccountTypeStr() {
        if(getAccountType() != null){
            return BrankAccountType.getText(getAccountType());
        }
        return accountTypeStr;
    }

    public void setAccountTypeStr(String accountTypeStr) {
        this.accountTypeStr = accountTypeStr;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        if(startTime != null){
            this.startTimeStr=DateUtil.formart(startTime,DateUtil.FORMART_YMD);
        }
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
        if(endTime != null){
            this.endTimeStr=DateUtil.formart(endTime,DateUtil.FORMART_YMD);
            if(new Date().after(endTime)){
                this.activityIsOver = true;
            }
        }
        this.endTime = endTime;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
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

    public Boolean getAgreement() {
        return agreement;
    }

    public void setAgreement(Boolean agreement) {
        this.agreement = agreement;
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

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }
}
