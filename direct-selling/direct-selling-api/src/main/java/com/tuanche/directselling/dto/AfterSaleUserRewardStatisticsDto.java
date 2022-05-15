package com.tuanche.directselling.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.model.AfterSaleUserRewardStatistics;
import com.tuanche.directselling.utils.AfterSaleConstants.UserType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class AfterSaleUserRewardStatisticsDto extends AfterSaleUserRewardStatistics implements Serializable {

    private String dealerName;
    private String activityName;

    private String userTypeName;
    private String userName;
    private String writeOffGiftCertificatesTotalName;

    //用户手机号
    private String userPhone;

    //车牌
    private String licencePlate;

    //累计奖励
    private BigDecimal rewardSun;

    private String orderCode;

    /**
     * 交易流水号
     */
    private String tradeNo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;
    //支付开始时间
    private String payTimeStart;
    //支付结束时间
    private String payTimeEnd;
    private String userNickName;

    private List<AfterSaleUserRewardStatistics> searchList;

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = StringUtil.isEmpty(userPhone) ? null : userPhone.trim();
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = StringUtil.isEmpty(licencePlate) ? null : licencePlate.trim();
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public BigDecimal getRewardSun() {
        BigDecimal rewardSun = this.getShareReward().add(this.getExtraReward()).add(this.getPurchaseReward());
        return rewardSun;
    }

    public void setRewardSun(BigDecimal rewardSun) {
        this.rewardSun = rewardSun;
    }

    public String getUserTypeName() {
        if (this.getUserType()!=null) {
            return UserType.getKey(this.getUserType());
        }
        return userTypeName;
    }

    public void setUserTypeName(String userTypeName) {
        this.userTypeName = userTypeName;
    }

    public String getWriteOffGiftCertificatesTotalName() {
        if (this.getWriteOffGiftCertificatesTotal()!=null) {
            switch (this.getWriteOffGiftCertificatesTotal()) {
                case 0:
                    return "未获得";
                case 1:
                    return "待核销";
                default:
                    return "已核销";
            }
        }
        return writeOffGiftCertificatesTotalName;
    }

    public void setWriteOffGiftCertificatesTotalName(String writeOffGiftCertificatesTotalName) {
        this.writeOffGiftCertificatesTotalName = writeOffGiftCertificatesTotalName;
    }

    public List<AfterSaleUserRewardStatistics> getSearchList() {
        return searchList;
    }

    public void setSearchList(List<AfterSaleUserRewardStatistics> searchList) {
        this.searchList = searchList;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public String getPayTimeStart() {
        return payTimeStart;
    }

    public void setPayTimeStart(String payTimeStart) {
        this.payTimeStart = payTimeStart;
    }

    public String getPayTimeEnd() {
        return payTimeEnd;
    }

    public void setPayTimeEnd(String payTimeEnd) {
        this.payTimeEnd = payTimeEnd;
    }

    public String getUserNickName() {


        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }
}

