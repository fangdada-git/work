package com.tuanche.directselling.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tuanche.directselling.model.AfterSaleActivity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lvsen
 * @description
 * @date 2021/7/20 16:10
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AfterSaleActivityDto extends AfterSaleActivity implements Serializable {

    /**
     * 经销商名称
     */
    private String dealerName;
    /**
     * 经销商地址
     */
    private String dealerAddress;
    /**
     * 经销商电话
     */
    private String dealerTel;
    /**
     * 经销商门店图
     */
    private String dealerImg;

    /**
     * 简称
     */
    private String shortName;

    /**
     * 公户账号
     */
    private String publicCardNumber;
    /**
     * 公户开户行
     */
    private String publicCardBank;

    /**
     * 是否配置基础账号 (0未设置 1 已设置)
     */
    private Integer isSetBaseCarNumber;

    /**
     * 私户账号
     */
    private String privateCardNumber;
    /**
     * 私户开户行
     */
    private String privateCardBank;

    /**
     * 负责人姓名
     */
    private String managerName;

    /**
     * 负责人手机号
     */
    private String managerPhone;

    private String saleStartTimeStr;
    /**
     * 售卖结束时间
     */
    private String saleEndTimeStr;
    /**
     * 线下兑换开始时间
     */
    private String offlineConvertStartTimeStr;
    /**
     * 线下兑换结束时间
     */
    private String offlineConvertEndTimeStr;

    private List<Map<String, String>> brandList;

    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 礼品券领取开始时间
     */
    private String giftCouponGetStartTimeStr;

    private Date giftCouponGetStartTime;
    /**
     * 礼品券领取结束时间
     */
    private String giftCouponGetEndTimeStr;

    private Date giftCouponGetEndTime;
    /**
     * 礼品券有效开始时间
     */
    private String giftCouponValidStartTimeStr;

    private Date giftCouponValidStartTime;
    /**
     * 礼品券有效结束时间
     */
    private String giftCouponValidEndTimeStr;

    private Date giftCouponValidEndTime;

    /**
     * 经纬度
     */
    private String position;

    /**
     * 代理人微信unionid
     */
    private String agentWxUnionId;

    /**
     * 活动id
     */
    private Integer activityId;
    /**
     * 经销商主品牌
     */
    private Integer masterBrandId;

    /**
     * 商品名称
     */
    private String commodityName;
    /**
     * 商品价格
     */
    private BigDecimal commodityPrice;

    /**
     * 商品标价
     */
    private BigDecimal originalPrice;

    /**
     * 上架时间
     */
    private Date upShelfTime;

    /**
     * 下架时间
     */
    private Date downShelfTime;

    /**
     * 商品描述信息
     */
    private String description;

    /**
     * 头图图片详情
     */
    private List<FissionGoodsImageDto> headImages;

    /**
     * 详情图图片列表
     */
    private List<FissionGoodsImageDto> detailImages;

    /**
     * 是否已购买（0未购买1已购买）
     */
    private Integer isBuyed;

    /**
     * 是否是代理人
     */
    private Boolean agent;

    private String alarmInfoListStr;

    private List<AlarmInfo> alarmInfoList;

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getDealerAddress() {
        return dealerAddress;
    }

    public void setDealerAddress(String dealerAddress) {
        this.dealerAddress = dealerAddress;
    }

    public String getDealerTel() {
        return dealerTel;
    }

    public void setDealerTel(String dealerTel) {
        this.dealerTel = dealerTel;
    }

    public String getPublicCardNumber() {
        return publicCardNumber;
    }

    public void setPublicCardNumber(String publicCardNumber) {
        this.publicCardNumber = publicCardNumber;
    }

    public String getPublicCardBank() {
        return publicCardBank;
    }

    public void setPublicCardBank(String publicCardBank) {
        this.publicCardBank = publicCardBank;
    }

    public Integer getIsSetBaseCarNumber() {
        return isSetBaseCarNumber;
    }

    public void setIsSetBaseCarNumber(Integer isSetBaseCarNumber) {
        this.isSetBaseCarNumber = isSetBaseCarNumber;
    }

    public String getPrivateCardNumber() {
        return privateCardNumber;
    }

    public void setPrivateCardNumber(String privateCardNumber) {
        this.privateCardNumber = privateCardNumber;
    }

    public String getPrivateCardBank() {
        return privateCardBank;
    }

    public void setPrivateCardBank(String privateCardBank) {
        this.privateCardBank = privateCardBank;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }

    public String getSaleStartTimeStr() {
        return saleStartTimeStr;
    }

    public void setSaleStartTimeStr(String saleStartTimeStr) {
        this.saleStartTimeStr = saleStartTimeStr;
    }

    public String getSaleEndTimeStr() {
        return saleEndTimeStr;
    }

    public void setSaleEndTimeStr(String saleEndTimeStr) {
        this.saleEndTimeStr = saleEndTimeStr;
    }

    public String getOfflineConvertStartTimeStr() {
        return offlineConvertStartTimeStr;
    }

    public void setOfflineConvertStartTimeStr(String offlineConvertStartTimeStr) {
        this.offlineConvertStartTimeStr = offlineConvertStartTimeStr;
    }

    public String getOfflineConvertEndTimeStr() {
        return offlineConvertEndTimeStr;
    }

    public void setOfflineConvertEndTimeStr(String offlineConvertEndTimeStr) {
        this.offlineConvertEndTimeStr = offlineConvertEndTimeStr;
    }

    public List<Map<String, String>> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Map<String, String>> brandList) {
        this.brandList = brandList;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGiftCouponGetStartTimeStr() {
        return giftCouponGetStartTimeStr;
    }

    public void setGiftCouponGetStartTimeStr(String giftCouponGetStartTimeStr) {
        this.giftCouponGetStartTimeStr = giftCouponGetStartTimeStr;
    }

    public String getGiftCouponGetEndTimeStr() {
        return giftCouponGetEndTimeStr;
    }

    public void setGiftCouponGetEndTimeStr(String giftCouponGetEndTimeStr) {
        this.giftCouponGetEndTimeStr = giftCouponGetEndTimeStr;
    }

    public String getGiftCouponValidStartTimeStr() {
        return giftCouponValidStartTimeStr;
    }

    public void setGiftCouponValidStartTimeStr(String giftCouponValidStartTimeStr) {
        this.giftCouponValidStartTimeStr = giftCouponValidStartTimeStr;
    }

    public String getGiftCouponValidEndTimeStr() {
        return giftCouponValidEndTimeStr;
    }

    public void setGiftCouponValidEndTimeStr(String giftCouponValidEndTimeStr) {
        this.giftCouponValidEndTimeStr = giftCouponValidEndTimeStr;
    }

    public Date getGiftCouponGetStartTime() {
        return giftCouponGetStartTime;
    }

    public void setGiftCouponGetStartTime(Date giftCouponGetStartTime) {
        this.giftCouponGetStartTime = giftCouponGetStartTime;
    }

    public Date getGiftCouponGetEndTime() {
        return giftCouponGetEndTime;
    }

    public void setGiftCouponGetEndTime(Date giftCouponGetEndTime) {
        this.giftCouponGetEndTime = giftCouponGetEndTime;
    }

    public Date getGiftCouponValidStartTime() {
        return giftCouponValidStartTime;
    }

    public void setGiftCouponValidStartTime(Date giftCouponValidStartTime) {
        this.giftCouponValidStartTime = giftCouponValidStartTime;
    }

    public Date getGiftCouponValidEndTime() {
        return giftCouponValidEndTime;
    }

    public void setGiftCouponValidEndTime(Date giftCouponValidEndTime) {
        this.giftCouponValidEndTime = giftCouponValidEndTime;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAgentWxUnionId() {
        return agentWxUnionId;
    }

    public void setAgentWxUnionId(String agentWxUnionId) {
        this.agentWxUnionId = agentWxUnionId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getMasterBrandId() {
        return masterBrandId;
    }

    public void setMasterBrandId(Integer masterBrandId) {
        this.masterBrandId = masterBrandId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public BigDecimal getCommodityPrice() {
        return commodityPrice;
    }

    public void setCommodityPrice(BigDecimal commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Date getUpShelfTime() {
        return upShelfTime;
    }

    public void setUpShelfTime(Date upShelfTime) {
        this.upShelfTime = upShelfTime;
    }

    public Date getDownShelfTime() {
        return downShelfTime;
    }

    public void setDownShelfTime(Date downShelfTime) {
        this.downShelfTime = downShelfTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<FissionGoodsImageDto> getHeadImages() {
        return headImages;
    }

    public void setHeadImages(List<FissionGoodsImageDto> headImages) {
        this.headImages = headImages;
    }

    public List<FissionGoodsImageDto> getDetailImages() {
        return detailImages;
    }

    public void setDetailImages(List<FissionGoodsImageDto> detailImages) {
        this.detailImages = detailImages;
    }

    public Integer getIsBuyed() {
        return isBuyed;
    }

    public void setIsBuyed(Integer isBuyed) {
        this.isBuyed = isBuyed;
    }

    public Boolean getAgent() {
        return agent;
    }

    public void setAgent(Boolean agent) {
        this.agent = agent;
    }

    public String getAlarmInfoListStr() {
        return alarmInfoListStr;
    }

    public void setAlarmInfoListStr(String alarmInfoListStr) {
        this.alarmInfoListStr = alarmInfoListStr;
    }

    public List<AlarmInfo> getAlarmInfoList() {
        return alarmInfoList;
    }

    public void setAlarmInfoList(List<AlarmInfo> alarmInfoList) {
        this.alarmInfoList = alarmInfoList;
    }

    public String getDealerImg() {
        return dealerImg;
    }

    public void setDealerImg(String dealerImg) {
        this.dealerImg = dealerImg;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}

