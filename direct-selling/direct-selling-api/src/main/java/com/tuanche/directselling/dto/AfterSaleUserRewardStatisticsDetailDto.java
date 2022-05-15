package com.tuanche.directselling.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuanche.directselling.utils.AfterSaleConstants;
import com.tuanche.directselling.utils.GlobalConstants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author：HuangHao
 * @CreatTime 2021-08-23 14:41
 */
public class AfterSaleUserRewardStatisticsDetailDto  implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userWxUnionId;
    private String activityName;
    //车牌
    private String licencePlate;
    //手机号
    private String userPhone;
    //代理人手机
    private String agentPhone;
    //分享人手机
    private String sharePhone;
    //购买时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;
    //邀请奖励
    private BigDecimal invitationReward;
    //额外奖励
    private BigDecimal extraReward;
    //额外奖励
    private int keepOnRecordUser;

    private Integer dealerId;
    private String dealerName;
    private String orderCode;

    /**
     * 交易流水号
     */
    private String tradeNo;
    //用户身份
    private Integer userType;
    //用户身份
    private String userTypeStr;
    //用户昵称
    private String nickName;

    //手机号脱敏
    public String getSharePhone() {
        return GlobalConstants.desensitizationPhone(userPhone);
    }

    //手机号脱敏
    public String getUserPhone() {
        return GlobalConstants.desensitizationPhone(userPhone);
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }
    public String getUserTypeStr() {
        if(getUserType()!=null){
            return AfterSaleConstants.UserType.getKey(getUserType());
        }
        return userTypeStr;
    }

    public void setUserTypeStr(String userTypeStr) {
        this.userTypeStr = userTypeStr;
    }
    public String getUserWxUnionId() {
        return userWxUnionId;
    }

    public void setUserWxUnionId(String userWxUnionId) {
        this.userWxUnionId = userWxUnionId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }



    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getAgentPhone() {
        return agentPhone;
    }

    public void setAgentPhone(String agentPhone) {
        this.agentPhone = agentPhone;
    }



    public void setSharePhone(String sharePhone) {
        this.sharePhone = sharePhone;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public BigDecimal getInvitationReward() {
        return invitationReward;
    }

    public void setInvitationReward(BigDecimal invitationReward) {
        this.invitationReward = invitationReward;
    }

    public BigDecimal getExtraReward() {
        return extraReward;
    }

    public void setExtraReward(BigDecimal extraReward) {
        this.extraReward = extraReward;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getKeepOnRecordUser() {
        return keepOnRecordUser;
    }

    public void setKeepOnRecordUser(int keepOnRecordUser) {
        this.keepOnRecordUser = keepOnRecordUser;
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }


}
