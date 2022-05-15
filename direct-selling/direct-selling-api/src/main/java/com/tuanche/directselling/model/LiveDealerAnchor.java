package com.tuanche.directselling.model;

import java.io.Serializable;
import java.util.Date;

public class LiveDealerAnchor implements Serializable {

    private static final long serialVersionUID = -2934085856173284767L;

    private Integer id;

    private Integer dealerId;

    private String dealerName;

    // 品牌ID
    private Integer brandId;

    // 品牌名称
    private String brandName;

    //主播id
    private Long anchorId;

    //主播达人地址
    private String anchorUrl;

    //主播真实姓名
    private String anchorName;
    //主播昵称
    private String anchorNick;

    //主播手机号
    private String anchorPhone;

    //头像地址
    private String headImg;

    //淘宝会员名
    private String tbNick;

    //介绍信息
    private String desc;

    //城市id
    private Integer cityId;

    //城市名称
    private String cityName;

    //直播标题
    private String liveTitle;

    //直播封面
    private String liveCover;

    //直播间地址
    private String liveUrl;

    //粉丝数
    private Integer fans;

    //内容数
    private Integer contentCount;

    //点赞数
    private Integer praiseCount;

    //状态：0,待挂靠 1.挂靠成功 2.审核失败 3.无法操作 4.已发短信
    private Integer status;

    //删除标识：1 删除 0 未删除
    private Boolean deleteFlag;

    //创建时间
    private Date createDt;

    //更新时间
    private Date updateDt;

    //主播热度
    private Integer anchorHeat=0;

    //主播类型 1. 经销商主播  2. 团车主播
    private Integer anchorType;

    //企业支付宝
    private String enterpriseAlipay;

    // 省id
    private Integer provinceId;

    // 省名称
    private String provinceName;

    // 详细地址
    private String address;

    // 门店座机号
    private String dealerLandline;

    // 负责人姓名
    private String chargeName;

    // 负责人手机号
    private String chargePhone;

    // 企业支付宝名称
    private String enterpriseAlipayName;

    // 淘宝店铺名称
    private String storeName;

    private Integer activityId;
    private String activityName;
    private Integer periodsId;
    private String periodsName;
    private String feedId;
    //live_dealer_playback 主键,用于修改
    private Integer feedIdKeyId;
    //用于直播片段 更新活动id
    private Integer activityId_oldId;
    private Integer operatorUserId;

    public Integer getAnchorHeat() {
        return anchorHeat;
    }

    public void setAnchorHeat(Integer anchorHeat) {
        this.anchorHeat = anchorHeat;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        this.dealerName = dealerName == null ? null : dealerName.trim();
    }

    public Long getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(Long anchorId) {
        this.anchorId = anchorId;
    }

    public String getAnchorUrl() {
        return anchorUrl;
    }

    public void setAnchorUrl(String anchorUrl) {
        this.anchorUrl = anchorUrl == null ? null : anchorUrl.trim();
    }

    public String getAnchorName() {
        return anchorName;
    }

    public void setAnchorName(String anchorName) {
        this.anchorName = anchorName == null ? null : anchorName.trim();
    }

    public String getAnchorPhone() {
        return anchorPhone;
    }

    public void setAnchorPhone(String anchorPhone) {
        this.anchorPhone = anchorPhone == null ? null : anchorPhone.trim();
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg == null ? null : headImg.trim();
    }

    public String getTbNick() {
        return tbNick;
    }

    public void setTbNick(String tbNick) {
        this.tbNick = tbNick == null ? null : tbNick.trim();
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    public String getLiveTitle() {
        return liveTitle;
    }

    public void setLiveTitle(String liveTitle) {
        this.liveTitle = liveTitle;
    }

    public String getLiveCover() {
        return liveCover;
    }

    public void setLiveCover(String liveCover) {
        this.liveCover = liveCover;
    }

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }

    public Integer getFans() {
        return fans;
    }

    public void setFans(Integer fans) {
        this.fans = fans;
    }

    public Integer getContentCount() {
        return contentCount;
    }

    public void setContentCount(Integer contentCount) {
        this.contentCount = contentCount;
    }

    public Integer getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(Integer praiseCount) {
        this.praiseCount = praiseCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
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

    public String getAnchorNick() {
        return anchorNick;
    }

    public void setAnchorNick(String anchorNick) {
        this.anchorNick = anchorNick;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getAnchorType() {
        return anchorType;
    }

    public void setAnchorType(Integer anchorType) {
        this.anchorType = anchorType;
    }

    public String getEnterpriseAlipay() {
        return enterpriseAlipay;
    }

    public void setEnterpriseAlipay(String enterpriseAlipay) {
        this.enterpriseAlipay = enterpriseAlipay;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public Integer getFeedIdKeyId() {
        return feedIdKeyId;
    }

    public void setFeedIdKeyId(Integer feedIdKeyId) {
        this.feedIdKeyId = feedIdKeyId;
    }

    public Integer getActivityId_oldId() {
        return activityId_oldId;
    }

    public void setActivityId_oldId(Integer activityId_oldId) {
        this.activityId_oldId = activityId_oldId;
    }

    public Integer getOperatorUserId() {
        return operatorUserId;
    }

    public void setOperatorUserId(Integer operatorUserId) {
        this.operatorUserId = operatorUserId;
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

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDealerLandline() {
        return dealerLandline;
    }

    public void setDealerLandline(String dealerLandline) {
        this.dealerLandline = dealerLandline;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public String getChargePhone() {
        return chargePhone;
    }

    public void setChargePhone(String chargePhone) {
        this.chargePhone = chargePhone;
    }

    public String getEnterpriseAlipayName() {
        return enterpriseAlipayName;
    }

    public void setEnterpriseAlipayName(String enterpriseAlipayName) {
        this.enterpriseAlipayName = enterpriseAlipayName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}