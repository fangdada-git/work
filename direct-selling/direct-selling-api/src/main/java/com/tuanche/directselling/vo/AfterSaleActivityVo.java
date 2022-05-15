package com.tuanche.directselling.vo;


import java.io.Serializable;

/**
 * @Author lvsen
 * @Description
 * @Date 2021/7/20
 **/
public class AfterSaleActivityVo implements Serializable {

    /**
     * 奖励id
     */
    private Integer id;

    /**
     * 活动id
     */
    private Integer activityId;
    /**
     * 微信UserWxUnionId
     */
    private String userWxUnionId;

    /**
     * 用户openId
     */
    private String userWxOpenId;
    /**
     * 推广渠道 1：代理人 2：电销客服 3：裸连接（公众号)
     */
    private Integer chanel;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户头像
     */
    private String userWxHead;
    /**
     * 代理人微信unionid
     */
    private String agentWxUnionId;
    /**
     * 分享人微信unionid
     */
    private String shareWxUnionId;

    /**
     * 经销商名称
     */
    private Integer dealerId;

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

    public String getUserWxUnionId() {
        return userWxUnionId;
    }

    public void setUserWxUnionId(String userWxUnionId) {
        this.userWxUnionId = userWxUnionId;
    }

    public String getUserWxOpenId() {
        return userWxOpenId;
    }

    public void setUserWxOpenId(String userWxOpenId) {
        this.userWxOpenId = userWxOpenId;
    }

    public Integer getChanel() {
        return chanel;
    }

    public void setChanel(Integer chanel) {
        this.chanel = chanel;
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

    public String getAgentWxUnionId() {
        return agentWxUnionId;
    }

    public void setAgentWxUnionId(String agentWxUnionId) {
        this.agentWxUnionId = agentWxUnionId;
    }

    public String getShareWxUnionId() {
        return shareWxUnionId;
    }

    public void setShareWxUnionId(String shareWxUnionId) {
        this.shareWxUnionId = shareWxUnionId;
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

}
