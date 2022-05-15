package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.FissionActivity;

import java.io.Serializable;
import java.util.List;

/**
 * @class: FissionActivityApiDto
 * @description: 裂变活动前端输出实体
 * @author: dudg
 * @create: 2020-10-12 15:56
 */
public class FissionActivityApiDto extends FissionActivity implements Serializable {

    /**
     * 浏览数
     */
    private Integer browseNum;
    /**
     * 预约数
     */
    private Integer appointNum;
    /**
     * 分享数
     */
    private Integer shareNum;
    /**
     * 分享人昵称
     */
    private String shareNick;
    /**
     * 预约状态
     */
    private Integer appointState;

    /**
     * 直播状态
     */
    private Integer liveState;

    /**
     * 直播倒计时
     */
    private Long liveCountDown;

    /**
     * 活动详情图urls
     */
    private List<String> detailPics;

    /**
     * 分享标题
     */
    private String shareTitle;
    /**
     * 分享文案
     */
    private String shareText;
    /**
     * 分享url
     */
    private String shareUrl;

    /**
     * 分享微信图url
     */
    private String wechatPic;
    /**
     * 微信订阅消息url
     */
    private String wxSubScribeMsgUrl;

    /**
     * 直播间url
     */
    private String liveUrl;

    /**
     * 直播开播时间
     */
    private String liveStartTime;

    /**
     * 活动状态 1 进行中 0 未开始 2 已结束
     */
    private Integer activityStatus;

    /**
     * 是否配置商品
     */
    private Integer hasGoods;

    public Integer getBrowseNum() {
        return browseNum;
    }

    public void setBrowseNum(Integer browseNum) {
        this.browseNum = browseNum;
    }

    public Integer getAppointNum() {
        return appointNum;
    }

    public void setAppointNum(Integer appointNum) {
        this.appointNum = appointNum;
    }

    public Integer getShareNum() {
        return shareNum;
    }

    public void setShareNum(Integer shareNum) {
        this.shareNum = shareNum;
    }

    public String getShareNick() {
        return shareNick;
    }

    public void setShareNick(String shareNick) {
        this.shareNick = shareNick;
    }

    public Integer getLiveState() {
        return liveState;
    }

    public void setLiveState(Integer liveState) {
        this.liveState = liveState;
    }

    public Long getLiveCountDown() {
        return liveCountDown;
    }

    public void setLiveCountDown(Long liveCountDown) {
        this.liveCountDown = liveCountDown;
    }

    public Integer getAppointState() {
        return appointState;
    }

    public void setAppointState(Integer appointState) {
        this.appointState = appointState;
    }

    public List<String> getDetailPics() {
        return detailPics;
    }

    public void setDetailPics(List<String> detailPics) {
        this.detailPics = detailPics;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareText() {
        return shareText;
    }

    public void setShareText(String shareText) {
        this.shareText = shareText;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    @Override
    public String getWechatPic() {
        return wechatPic;
    }

    @Override
    public void setWechatPic(String wechatPic) {
        this.wechatPic = wechatPic;
    }

    public String getWxSubScribeMsgUrl() {
        return wxSubScribeMsgUrl;
    }

    public void setWxSubScribeMsgUrl(String wxSubScribeMsgUrl) {
        this.wxSubScribeMsgUrl = wxSubScribeMsgUrl;
    }

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }

    public String getLiveStartTime() {
        return liveStartTime;
    }

    public void setLiveStartTime(String liveStartTime) {
        this.liveStartTime = liveStartTime;
    }

    public Integer getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(Integer activityStatus) {
        this.activityStatus = activityStatus;
    }

    public Integer getHasGoods() {
        return hasGoods;
    }

    public void setHasGoods(Integer hasGoods) {
        this.hasGoods = hasGoods;
    }
}
