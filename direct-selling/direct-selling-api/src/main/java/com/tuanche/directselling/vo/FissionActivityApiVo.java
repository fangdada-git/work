package com.tuanche.directselling.vo;

import java.io.Serializable;

/**
 * @class: FissionActivityApiVo
 * @description: 裂变活动接口参数实体
 * @author: dudg
 * @create: 2020-10-12 16:35
 */
public class FissionActivityApiVo implements Serializable {

    /**
     * 城市id
     */
    private Integer cityId;
    /**
     * 销售openid
     */
    private String saleWxOpenId;
    /**
     * 销售UnionId;
     */
    private String saleWxUnionId;
    /**
     * 分享者openid
     */
    private String shareWxOpenId;

    /**
     * 分享者UnionId
     */
    private String shareWxUnionId;

    /**
     * 分享者昵称
     */
    private String shareWxNick;
    /**
     * 访问用户openid
     */
    private String userWxOpenId;
    /**
     * 用户微信unionId
     */
    private String userWxUnionId;

    private String userWxNick;
    /**
     * 裂变活动id
     */
    private Integer fissionId;
    /**
     * 留资手机号
     */
    private String userPhone;

    /**
     * 微信订阅消息场景值
     */
    private Integer wxSubScribeScene;

    private Integer liveId;

    private Integer userId;
    /**
     * 推广渠道 1：裸连接 2：销售渠道 3：用户运营渠道
     */
    private Integer channel;

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getSaleWxOpenId() {
        return saleWxOpenId;
    }

    public void setSaleWxOpenId(String saleWxOpenId) {
        this.saleWxOpenId = saleWxOpenId;
    }

    public String getShareWxOpenId() {
        return shareWxOpenId;
    }

    public String getSaleWxUnionId() {
        return saleWxUnionId;
    }

    public void setSaleWxUnionId(String saleWxUnionId) {
        this.saleWxUnionId = saleWxUnionId;
    }

    public String getShareWxUnionId() {
        return shareWxUnionId;
    }

    public void setShareWxUnionId(String shareWxUnionId) {
        this.shareWxUnionId = shareWxUnionId;
    }

    public void setShareWxOpenId(String shareWxOpenId) {
        this.shareWxOpenId = shareWxOpenId;
    }

    public String getShareWxNick() {
        return shareWxNick;
    }

    public void setShareWxNick(String shareWxNick) {
        this.shareWxNick = shareWxNick;
    }

    public String getUserWxOpenId() {
        return userWxOpenId;
    }

    public void setUserWxOpenId(String userWxOpenId) {
        this.userWxOpenId = userWxOpenId;
    }

    public String getUserWxUnionId() {
        return userWxUnionId;
    }

    public void setUserWxUnionId(String userWxUnionId) {
        this.userWxUnionId = userWxUnionId;
    }

    public String getUserWxNick() {
        return userWxNick;
    }

    public void setUserWxNick(String userWxNick) {
        this.userWxNick = userWxNick;
    }

    public Integer getFissionId() {
        return fissionId;
    }

    public void setFissionId(Integer fissionId) {
        this.fissionId = fissionId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Integer getWxSubScribeScene() {
        return wxSubScribeScene;
    }

    public void setWxSubScribeScene(Integer wxSubScribeScene) {
        this.wxSubScribeScene = wxSubScribeScene;
    }

    public Integer getLiveId() {
        return liveId;
    }

    public void setLiveId(Integer liveId) {
        this.liveId = liveId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }
}
