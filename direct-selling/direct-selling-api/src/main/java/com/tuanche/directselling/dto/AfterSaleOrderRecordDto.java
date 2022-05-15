package com.tuanche.directselling.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tuanche.arch.util.DateUtils;
import com.tuanche.directselling.model.AfterSaleOrderRecord;
import com.tuanche.directselling.utils.AfterSaleConstants;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AfterSaleOrderRecordDto extends AfterSaleOrderRecord {

    private List<String> orderCodeList;

    private String activityName;
    private String dealerName;
    private String checkedDealerName;
    private String goodsName;
    private String agentName;
    private String salesName;
    //分享人车牌
    private String shareLicencePlate;
    //分享人昵称
    private String shareNickName;
    //购买人昵称
    private String userNickName;

    private String cbName;
    private String csName;
    private Integer carId;
    private String carNmae;
    private String orderStatusName;
    //微信交易流水号
    private String tradeNo;
    //退款微信交易流水号
    private String refundTradeNo;
    //退款时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date refundTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTimeBegin;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTimeEnd;
    /**
     * 核销开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * 核销结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    //渠道名称
    private String channelName;
    //核销人名称
    private String checkedSalesName;
    //订单类型-格式化后的
    private String orderTypeFormat;
    //用户身份-格式化后的
    private String userTypeFormat;

    //手机号是否脱敏-默认：是
    private Boolean desensitizationPhone=true;
    //活动IDS
    private List<Integer> activityIds;
    //分账开始时间
    private String subAccountStartTime;
    //分账结束时间
    private String subAccountEndTime;
    //分账状态-格式化后的
    private String subAccountStatusFormat;


    //企微官方企微号id
    private String wwUserId;
    //企微号名称
    private String wwUserName;
    //企微添加时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date wwCreateDt;
    private String wwUserNameCreateDt;

    public String getUserTypeFormat() {
        if(getUserType() != null){
            return AfterSaleConstants.UserType.getKey(getUserType());
        }
        return userTypeFormat;
    }
    public String getSubAccountStatusFormat() {
        if(getSubAccountStatus()!=null){
            return AfterSaleConstants.OrderSubAccountStatus.getKey(getSubAccountStatus());
        }
        return subAccountStatusFormat;
    }
    public void setUserTypeFormat(String userTypeFormat) {
        this.userTypeFormat = userTypeFormat;
    }

    public String getOrderTypeFormat() {
        if(getOrderType() != null){
            return AfterSaleConstants.OrderType.getKey(getOrderType());
        }
        return orderTypeFormat;
    }

    public String getChannelName() {
        if (this.getChannel()!=null) {
            return AfterSaleConstants.ChannelType.getKey(this.getChannel());
        }
        return channelName;
    }
    public void setOrderTypeFormat(String orderTypeFormat) {
        this.orderTypeFormat = orderTypeFormat;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getCarNmae() {
        return carNmae;
    }

    public void setCarNmae(String carNmae) {
        this.carNmae = carNmae;
    }

    public String getOrderStatusName() {
        if (getOrderStatus()!=null) {
            if (AfterSaleConstants.OrderStatus.UNPAID.getCode().equals(getOrderStatus()))
                return AfterSaleConstants.OrderStatus.UNPAID.getKey();
            if (AfterSaleConstants.OrderStatus.PAY_ING.getCode().equals(getOrderStatus()))
                return AfterSaleConstants.OrderStatus.PAY_ING.getKey();
            if (AfterSaleConstants.OrderStatus.PAID.getCode().equals(getOrderStatus())){
                //如果订单状态=3 且 是备案用户 且 未完成裂变要求 则状态是未生效
                if(getKeepOnRecordUser() != null && getFinishFissionTask() != null && getKeepOnRecordUser() && !getFinishFissionTask()){
                    return AfterSaleConstants.OrderStatus.NOT_EFFECTIVE.getKey();
                }
                return AfterSaleConstants.OrderStatus.PAID.getKey();
            }
            if (AfterSaleConstants.OrderStatus.CHECKOUT.getCode().equals(getOrderStatus()))
                return AfterSaleConstants.OrderStatus.CHECKOUT.getKey();
            if (AfterSaleConstants.OrderStatus.APPLY_REFUND.getCode().equals(getOrderStatus()))
                return AfterSaleConstants.OrderStatus.APPLY_REFUND.getKey();
            if (AfterSaleConstants.OrderStatus.REFUND_ING.getCode().equals(getOrderStatus()))
                return AfterSaleConstants.OrderStatus.REFUND_ING.getKey();
            if (AfterSaleConstants.OrderStatus.REFUND_SUCCESS_HAND.getCode().equals(getOrderStatus()))
                return AfterSaleConstants.OrderStatus.REFUND_SUCCESS_HAND.getKey();
            if (AfterSaleConstants.OrderStatus.REFUND_SUCCESS_TIMING.getCode().equals(getOrderStatus()))
                return AfterSaleConstants.OrderStatus.REFUND_SUCCESS_TIMING.getKey();
            if (AfterSaleConstants.OrderStatus.WAIT_BUYER_CONFIRM_GOODS.getCode().equals(getOrderStatus()))
                return AfterSaleConstants.OrderStatus.WAIT_BUYER_CONFIRM_GOODS.getKey();
            if (AfterSaleConstants.OrderStatus.WAIT_SELLER_SEND_GOODS.getCode().equals(getOrderStatus()))
                return AfterSaleConstants.OrderStatus.WAIT_SELLER_SEND_GOODS.getKey();
            if (AfterSaleConstants.OrderStatus.TRADE_CLOSED.getCode().equals(getOrderStatus()))
                return AfterSaleConstants.OrderStatus.TRADE_CLOSED.getKey();
            if (AfterSaleConstants.OrderStatus.GRANT_COUPON_NON.getCode().equals(getOrderStatus()))
                return AfterSaleConstants.OrderStatus.GRANT_COUPON_NON.getKey();
            if (AfterSaleConstants.OrderStatus.ARRIVE_SHOP.getCode().equals(getOrderStatus()))
                return AfterSaleConstants.OrderStatus.ARRIVE_SHOP.getKey();

        }
        return orderStatusName;
    }



    public void setSubAccountStatusFormat(String subAccountStatusFormat) {
        this.subAccountStatusFormat = subAccountStatusFormat;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public String getCbName() {
        return cbName;
    }

    public void setCbName(String cbName) {
        this.cbName = cbName;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public String getCsName() {
        return csName;
    }

    public void setCsName(String csName) {
        this.csName = csName;
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

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getCheckedDealerName() {
        return checkedDealerName;
    }

    public void setCheckedDealerName(String checkedDealerName) {
        this.checkedDealerName = checkedDealerName;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getRefundTradeNo() {
        return refundTradeNo;
    }

    public void setRefundTradeNo(String refundTradeNo) {
        this.refundTradeNo = refundTradeNo;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }



    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Date getPayTimeBegin() {
        return payTimeBegin;
    }

    public void setPayTimeBegin(Date payTimeBegin) {
        this.payTimeBegin = payTimeBegin;
    }

    public Date getPayTimeEnd() {
        return payTimeEnd;
    }

    public void setPayTimeEnd(Date payTimeEnd) {
        this.payTimeEnd = payTimeEnd;
    }

    public List<String> getOrderCodeList() {
        return orderCodeList;
    }

    public void setOrderCodeList(List<String> orderCodeList) {
        this.orderCodeList = orderCodeList;
    }

    public Boolean getDesensitizationPhone() {
        return desensitizationPhone;
    }

    public void setDesensitizationPhone(Boolean desensitizationPhone) {
        this.desensitizationPhone = desensitizationPhone;
    }

    public String getShareLicencePlate() {
        return shareLicencePlate;
    }

    public void setShareLicencePlate(String shareLicencePlate) {
        this.shareLicencePlate = shareLicencePlate;
    }

    public String getCheckedSalesName() {
        return checkedSalesName;
    }

    public void setCheckedSalesName(String checkedSalesName) {
        this.checkedSalesName = checkedSalesName;
    }

    public String getShareNickName() {
        return shareNickName;
    }

    public void setShareNickName(String shareNickName) {
        this.shareNickName = shareNickName;
    }

    public List<Integer> getActivityIds() {
        return activityIds;
    }

    public void setActivityIds(List<Integer> activityIds) {
        this.activityIds = activityIds;
    }

    public String getSubAccountStartTime() {
        return subAccountStartTime;
    }

    public void setSubAccountStartTime(String subAccountStartTime) {
        this.subAccountStartTime = subAccountStartTime;
    }

    public String getSubAccountEndTime() {
        return subAccountEndTime;
    }

    public void setSubAccountEndTime(String subAccountEndTime) {
        this.subAccountEndTime = subAccountEndTime;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getWwUserName() {
        return wwUserName;
    }

    public void setWwUserName(String wwUserName) {
        this.wwUserName = wwUserName;
    }

    public Date getWwCreateDt() {
        return wwCreateDt;
    }

    public void setWwCreateDt(Date wwCreateDt) {
        this.wwCreateDt = wwCreateDt;
    }

    public String getWwUserId() {
        return wwUserId;
    }

    public void setWwUserId(String wwUserId) {
        this.wwUserId = wwUserId;
    }

    public String getWwUserNameCreateDt() {
        if (wwCreateDt!=null) {
            return wwUserName+"<br/>"+ DateUtils.dateToString(wwCreateDt, DateUtils.YYYY_MM_DD_HH_MM_SS);
        }
        return wwUserNameCreateDt==null?"":wwUserNameCreateDt;
    }

    public void setWwUserNameCreateDt(String wwUserNameCreateDt) {
        this.wwUserNameCreateDt = wwUserNameCreateDt;
    }
}
