package com.tuanche.directselling.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 红包榜实体
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AfterSaleRedPacketListDto implements Serializable {

    /**
     * 活动id
     */
    private Integer activityId;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 微信头像
     */
    private String userWxHead;
    /**
     * 微信unionId
     */
    private String userWxUnionId;
    /**
     * 分享数量
     */
    private Integer shareCount;
    /**
     * 代理类型
     */
    private Integer agentType;
    /**
     * 获取邀请金（元）
     */
    private BigDecimal getInviteMoney;

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserWxHead() {
        return userWxHead;
    }

    public void setUserWxHead(String userWxHead) {
        this.userWxHead = userWxHead;
    }

    public String getUserWxUnionId() {
        return userWxUnionId;
    }

    public void setUserWxUnionId(String userWxUnionId) {
        this.userWxUnionId = userWxUnionId;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public BigDecimal getGetInviteMoney() {
        return getInviteMoney;
    }

    public void setGetInviteMoney(BigDecimal getInviteMoney) {
        this.getInviteMoney = getInviteMoney;
    }

    public Integer getAgentType() {
        return agentType;
    }

    public void setAgentType(Integer agentType) {
        this.agentType = agentType;
    }
}
