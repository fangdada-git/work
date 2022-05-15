package com.tuanche.directselling.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * fission_live_remind
 * @author
 */
public class FissionLiveRemind implements Serializable {
    private Long id;

    /**
     * 裂变活动id
     */
    private Integer fissionId;

    /**
     * 直播间ID
     */
    private Integer liveId;

    /**
     * 微信订阅消息-场景值
     */
    private Integer wxScene;

    /**
     * 销售微信UnionId
     */
    private String saleWxUnionId;

    /**
     * 分享人微信UnionId
     */
    private String shareWxUnionId;

    /**
     * 用户微信openid
     */
    private String userWxOpenId;
    /**
     * 用户微信UnionId
     */
    private String userWxUnionId;

    /**
     * 用户手机号
     */
    private String userPhone;

    /**
     * 用户中心id
     */
    private Integer userId;
    /**
     * 推广渠道 1：裸连接 2：销售渠道 3：用户运营渠道
     */
    private Integer channel;

    /**
     * 提醒状态：0 未提醒  1 已提醒
     */
    private Boolean remindState;

    /**
     * 创建时间
     */
    private Date createDt;

    /**
     * 更新时间
     */
    private Date updateDt;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFissionId() {
        return fissionId;
    }

    public void setFissionId(Integer fissionId) {
        this.fissionId = fissionId;
    }

    public Integer getLiveId() {
        return liveId;
    }

    public void setLiveId(Integer liveId) {
        this.liveId = liveId;
    }

    public Integer getWxScene() {
        return wxScene;
    }

    public void setWxScene(Integer wxScene) {
        this.wxScene = wxScene;
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

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
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

    public Boolean getRemindState() {
        return remindState;
    }

    public void setRemindState(Boolean remindState) {
        this.remindState = remindState;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FissionLiveRemind that = (FissionLiveRemind) o;
        return id.equals(that.id) &&
                fissionId.equals(that.fissionId) &&
                liveId.equals(that.liveId) &&
                wxScene.equals(that.wxScene) &&
                saleWxUnionId.equals(that.saleWxUnionId) &&
                shareWxUnionId.equals(that.shareWxUnionId) &&
                userWxOpenId.equals(that.userWxOpenId) &&
                userWxUnionId.equals(that.userWxUnionId) &&
                userPhone.equals(that.userPhone) &&
                userId.equals(that.userId) &&
                channel.equals(that.channel) &&
                remindState.equals(that.remindState) &&
                createDt.equals(that.createDt) &&
                updateDt.equals(that.updateDt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fissionId, liveId, wxScene, saleWxUnionId, shareWxUnionId, userWxOpenId, userWxUnionId, userPhone, userId, channel, remindState, createDt, updateDt);
    }

    @Override
    public String toString() {
        return "FissionLiveRemind{" +
                "id=" + id +
                ", fissionId=" + fissionId +
                ", liveId=" + liveId +
                ", wxScene=" + wxScene +
                ", saleWxUnionId='" + saleWxUnionId + '\'' +
                ", shareWxUnionId='" + shareWxUnionId + '\'' +
                ", userWxOpenId='" + userWxOpenId + '\'' +
                ", userWxUnionId='" + userWxUnionId + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userId=" + userId +
                ", channel=" + channel +
                ", remindState=" + remindState +
                ", createDt=" + createDt +
                ", updateDt=" + updateDt +
                '}';
    }
}