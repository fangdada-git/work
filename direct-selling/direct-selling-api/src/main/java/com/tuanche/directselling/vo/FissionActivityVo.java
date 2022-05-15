package com.tuanche.directselling.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lvsen
 * @description 裂变活动实体
 * @date 2020/9/22 15:46
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FissionActivityVo implements Serializable {

    private static final long serialVersionUID = 4235592145414431490L;

    private Integer id;
    /**
     * 场次id
     */
    private Integer periodsId;

    /**
     * 小场次id
     */
    private Integer sceneId;
    /**
     * 场次名称
     */
    private String periodsName;
    /**
     * 活动名称
     */
    private String activityName;
    /**
     * 开始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    /**
     * 结束时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    /**
     * 开启状态
     */
    private Short onState;
    /**
     * 直播间id
     */
    private Integer broadcastRoomId;
    /**
     * 头图
     */
    private String headPicUrl;
    /**
     * 详情图
     */
    private String detailPicUrls;
    /**
     * 海报图
     */
    private String posterUrl;
    /**
     * 商品标题图
     */
    private String productTitleUrl;
    /**
     * 是否保留手机号
     */
    private Short reservePhone;

    /**
     * C端是否现金奖励（1是 0否）
     */
    private Integer cAwardFlag;
    /**
     * 微信分享标题
     */
    private String wechatTitle;
    /**
     * 微信分享描述
     */
    private String wechatDescription;
    /**
     * 微信分享图片
     */
    private String wechatPic;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPeriodsId() {
        return periodsId;
    }

    public void setPeriodsId(Integer periodsId) {
        this.periodsId = periodsId;
    }

    public String getPeriodsName() {
        return periodsName;
    }

    public void setPeriodsName(String periodsName) {
        this.periodsName = periodsName;
    }

    public Integer getSceneId() {
        return sceneId;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
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

    public Short getOnState() {
        return onState;
    }

    public void setOnState(Short onState) {
        this.onState = onState;
    }

    public Integer getBroadcastRoomId() {
        return broadcastRoomId;
    }

    public void setBroadcastRoomId(Integer broadcastRoomId) {
        this.broadcastRoomId = broadcastRoomId;
    }

    public String getHeadPicUrl() {
        return headPicUrl;
    }

    public void setHeadPicUrl(String headPicUrl) {
        this.headPicUrl = headPicUrl;
    }

    public String getDetailPicUrls() {
        return detailPicUrls;
    }

    public void setDetailPicUrls(String detailPicUrls) {
        this.detailPicUrls = detailPicUrls;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getProductTitleUrl() {
        return productTitleUrl;
    }

    public void setProductTitleUrl(String productTitleUrl) {
        this.productTitleUrl = productTitleUrl;
    }

    public Integer getcAwardFlag() {
        return cAwardFlag;
    }

    public void setcAwardFlag(Integer cAwardFlag) {
        this.cAwardFlag = cAwardFlag;
    }

    public Short getReservePhone() {
        return reservePhone;
    }

    public void setReservePhone(Short reservePhone) {
        this.reservePhone = reservePhone;
    }

    public String getWechatTitle() {
        return wechatTitle;
    }

    public void setWechatTitle(String wechatTitle) {
        this.wechatTitle = wechatTitle;
    }

    public String getWechatDescription() {
        return wechatDescription;
    }

    public void setWechatDescription(String wechatDescription) {
        this.wechatDescription = wechatDescription;
    }

    public String getWechatPic() {
        return wechatPic;
    }

    public void setWechatPic(String wechatPic) {
        this.wechatPic = wechatPic;
    }
}