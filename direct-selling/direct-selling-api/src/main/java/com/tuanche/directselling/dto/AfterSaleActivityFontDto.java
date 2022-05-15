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
public class AfterSaleActivityFontDto implements Serializable {

    /**
     *  经销商id
     */
    private Integer dealerId;

    /**
     * 品牌id
     */
    private Integer brandId;
    /**
     * 公众号
     */
    private String publicAccount;
    /**
     * 商户号
     */
    private String merchantNo;
    /**
     * 活动名称
     */
    private String activityName;
    /**
     * 商品id
     */
    private Integer goodsId;
    /**
     * 备案用户需裂变用户数
     */
    private Integer goodsFissionCount;
    /**
     * 售卖开始时间
     */
    private Date saleStartTime;
    /**
     * 售卖结束时间
     */
    private Date saleEndTime;
    /**
     * 礼品券id
     */
    private Integer giftCouponId;
    /**
     * 礼品券名称
     */
    private String giftCouponName;
    /**
     * 礼品券裂变数
     */
    private Integer giftFissionCount;
    /**
     * 线下兑换开始时间
     */
    private Date offlineConvertStartTime;
    /**
     * 线下兑换结束时间
     */
    private Date offlineConvertEndTime;
    /**
     * 代理人基础奖励
     */
    private BigDecimal agentAwardBase;
    /**
     * 代理人额外奖励，邀请人数
     */
    private Integer agentAwardInviterCount;
    /**
     * 代理人额外奖励金额
     */
    private BigDecimal agentAwardExtra;
    /**
     * 分享人基础奖励
     */
    private BigDecimal shareAwardBase;
    /**
     * 分享人额外奖励限制数
     */
    private Integer shareAwardInviterCount;
    /**
     * 分享人额外奖励
     */
    private BigDecimal shareAwardExtra;
    /**
     * 购买人基础奖励
     */
    private BigDecimal buyerAwardBase;
    /**
     * 微信分享标题
     */
    private String weChatTitle;
    /**
     * 微信分享描述
     */
    private String weChatDescription;
    /**
     * 微信分享图
     */
    private String weChatPic;
    /**
     * 海报图
     */
    private String posterUrl;
    /**
     *  开户状态 0 未开户  1已开启 2已结束
     */
    private Integer onState;
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

    /**
    * 代理人类型 0 销售 1电销
     */
    private Integer agentType;

    /**
     *  经销商公众号二维码
     */
    private String publicQrCode;

    /**
     * 经销商门店图
     */
    private String dealerImg;

    /**
     * 倒计时
     */
    private long countDown;

    /**
     * 简称
     */
    private String shortName;
    /**
     * 分享人昵称
     */
    private String shareWxNickName;
    /**
     * 分享人头像
     */
    private String shareWxHead;

    /**
     * 线下兑换开始时间
     */
    private Long offlineConvertStartTimeMils;
    /**
     * 线下兑换结束时间
     */
    private Long offlineConvertEndTimeMils;

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

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getPublicAccount() {
        return publicAccount;
    }

    public void setPublicAccount(String publicAccount) {
        this.publicAccount = publicAccount;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsFissionCount() {
        return goodsFissionCount;
    }

    public void setGoodsFissionCount(Integer goodsFissionCount) {
        this.goodsFissionCount = goodsFissionCount;
    }

    public Date getSaleStartTime() {
        return saleStartTime;
    }

    public void setSaleStartTime(Date saleStartTime) {
        this.saleStartTime = saleStartTime;
    }

    public Date getSaleEndTime() {
        return saleEndTime;
    }

    public void setSaleEndTime(Date saleEndTime) {
        this.saleEndTime = saleEndTime;
    }

    public Integer getGiftCouponId() {
        return giftCouponId;
    }

    public void setGiftCouponId(Integer giftCouponId) {
        this.giftCouponId = giftCouponId;
    }

    public String getGiftCouponName() {
        return giftCouponName;
    }

    public void setGiftCouponName(String giftCouponName) {
        this.giftCouponName = giftCouponName;
    }

    public Integer getGiftFissionCount() {
        return giftFissionCount;
    }

    public void setGiftFissionCount(Integer giftFissionCount) {
        this.giftFissionCount = giftFissionCount;
    }

    public Date getOfflineConvertStartTime() {
        return offlineConvertStartTime;
    }

    public void setOfflineConvertStartTime(Date offlineConvertStartTime) {
        this.offlineConvertStartTime = offlineConvertStartTime;
    }

    public Date getOfflineConvertEndTime() {
        return offlineConvertEndTime;
    }

    public void setOfflineConvertEndTime(Date offlineConvertEndTime) {
        this.offlineConvertEndTime = offlineConvertEndTime;
    }

    public BigDecimal getAgentAwardBase() {
        return agentAwardBase;
    }

    public void setAgentAwardBase(BigDecimal agentAwardBase) {
        this.agentAwardBase = agentAwardBase;
    }

    public Integer getAgentAwardInviterCount() {
        return agentAwardInviterCount;
    }

    public void setAgentAwardInviterCount(Integer agentAwardInviterCount) {
        this.agentAwardInviterCount = agentAwardInviterCount;
    }

    public BigDecimal getAgentAwardExtra() {
        return agentAwardExtra;
    }

    public void setAgentAwardExtra(BigDecimal agentAwardExtra) {
        this.agentAwardExtra = agentAwardExtra;
    }

    public BigDecimal getShareAwardBase() {
        return shareAwardBase;
    }

    public void setShareAwardBase(BigDecimal shareAwardBase) {
        this.shareAwardBase = shareAwardBase;
    }

    public Integer getShareAwardInviterCount() {
        return shareAwardInviterCount;
    }

    public void setShareAwardInviterCount(Integer shareAwardInviterCount) {
        this.shareAwardInviterCount = shareAwardInviterCount;
    }

    public BigDecimal getShareAwardExtra() {
        return shareAwardExtra;
    }

    public void setShareAwardExtra(BigDecimal shareAwardExtra) {
        this.shareAwardExtra = shareAwardExtra;
    }

    public BigDecimal getBuyerAwardBase() {
        return buyerAwardBase;
    }

    public void setBuyerAwardBase(BigDecimal buyerAwardBase) {
        this.buyerAwardBase = buyerAwardBase;
    }

    public String getWeChatTitle() {
        return weChatTitle;
    }

    public void setWeChatTitle(String weChatTitle) {
        this.weChatTitle = weChatTitle;
    }

    public String getWeChatDescription() {
        return weChatDescription;
    }

    public void setWeChatDescription(String weChatDescription) {
        this.weChatDescription = weChatDescription;
    }

    public String getWeChatPic() {
        return weChatPic;
    }

    public void setWeChatPic(String weChatPic) {
        this.weChatPic = weChatPic;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public Integer getOnState() {
        return onState;
    }

    public void setOnState(Integer onState) {
        this.onState = onState;
    }

    public String getPublicQrCode() {
        return publicQrCode;
    }

    public void setPublicQrCode(String publicQrCode) {
        this.publicQrCode = publicQrCode;
    }

    public String getDealerImg() {
        return dealerImg;
    }

    public void setDealerImg(String dealerImg) {
        this.dealerImg = dealerImg;
    }

    public long getCountDown() {
        return countDown;
    }

    public void setCountDown(long countDown) {
        this.countDown = countDown;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getShareWxNickName() {
        return shareWxNickName;
    }

    public void setShareWxNickName(String shareWxNickName) {
        this.shareWxNickName = shareWxNickName;
    }

    public String getShareWxHead() {
        return shareWxHead;
    }

    public void setShareWxHead(String shareWxHead) {
        this.shareWxHead = shareWxHead;
    }

    public Integer getAgentType() {
        return agentType;
    }

    public void setAgentType(Integer agentType) {
        this.agentType = agentType;
    }

    public Long getOfflineConvertStartTimeMils() {
        return offlineConvertStartTimeMils;
    }

    public void setOfflineConvertStartTimeMils(Long offlineConvertStartTimeMils) {
        this.offlineConvertStartTimeMils = offlineConvertStartTimeMils;
    }

    public Long getOfflineConvertEndTimeMils() {
        return offlineConvertEndTimeMils;
    }

    public void setOfflineConvertEndTimeMils(Long offlineConvertEndTimeMils) {
        this.offlineConvertEndTimeMils = offlineConvertEndTimeMils;
    }
}