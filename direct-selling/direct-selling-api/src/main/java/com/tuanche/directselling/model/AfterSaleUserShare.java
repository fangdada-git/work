package com.tuanche.directselling.model;

import java.io.Serializable;
import java.util.Date;

/**
    * 售后特卖-用户分享表
    */
public class AfterSaleUserShare implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
    * 活动ID
    */
    private Integer activityId;

    /**
    * 代理人微信unionid
    */
    private String agentWxUnionId;

    /**
    * 分享人微信unionid
    */
    private String shareWxUnionId;

    /**
    * 用户微信unionid
    */
    private String userWxUnionId;

    /**
    * 当前页面的URL（取自Referer）
    */
    private String pageUrl;

    /**
    * ip地址
    */
    private String ip;

    /**
    * 推广渠道 1：代理人 2：电销客服 3：裸连接（公众号）
    */
    private Integer chanel;

    /**
    * 创建时间
    */
    private Date createDt;

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

    public String getUserWxUnionId() {
        return userWxUnionId;
    }

    public void setUserWxUnionId(String userWxUnionId) {
        this.userWxUnionId = userWxUnionId;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getChanel() {
        return chanel;
    }

    public void setChanel(Integer chanel) {
        this.chanel = chanel;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }
}