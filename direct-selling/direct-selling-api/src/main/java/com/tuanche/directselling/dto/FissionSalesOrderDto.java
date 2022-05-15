package com.tuanche.directselling.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tuanche.directselling.model.FissionSalesOrder;
import com.tuanche.directselling.utils.GlobalConstants;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: direct-selling
 * @description: ${description}
 * @author: fxq
 * @create: 2020-09-25 17:46
 **/

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FissionSalesOrderDto extends FissionSalesOrder implements Serializable {

    //状态名称
    public String orderStatusName;
    //场次名称
    public String periodsName;
    //裂变活动名称
    public String activityName;

    public Date activityBeginDate;

    public Date activityEndDate;
    
    //微信支付流水号
    public String tradeNo; 
    public String dealerName; 
    public String cityName; 
    public String saleName; 
    public String salePhone;
    /**
     * 销售支付微信openid
     */
    private String saleWxOpenId;
    /**
     * 销售支付微信UnionId
     */
    private String saleWxUnionId;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 开启状态
     */
    private Integer onState;



    public String getPeriodsName() {
        return periodsName;
    }

    public void setPeriodsName(String periodsName) {
        this.periodsName = periodsName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Date getActivityBeginDate() {
        return activityBeginDate;
    }

    public void setActivityBeginDate(Date activityBeginDate) {
        this.activityBeginDate = activityBeginDate;
    }

    public Date getActivityEndDate() {
        return activityEndDate;
    }

    public void setActivityEndDate(Date activityEndDate) {
        this.activityEndDate = activityEndDate;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }

    public String getSalePhone() {
        return salePhone;
    }

    public void setSalePhone(String salePhone) {
        this.salePhone = salePhone;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getOrderStatusName() {
        if (this.getOrderStatus()!=null) {
            if (this.getOrderStatus().equals(GlobalConstants.fission_sales_order.STATUS_NON_PAYMENT)) {
                orderStatusName = "待支付";
            } else if (this.getOrderStatus().equals(GlobalConstants.fission_sales_order.STATUS_PAID)) {
                orderStatusName = "已支付";
            }
        }
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public Integer getOnState() {
        return onState;
    }

    public void setOnState(Integer onState) {
        this.onState = onState;
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

}
