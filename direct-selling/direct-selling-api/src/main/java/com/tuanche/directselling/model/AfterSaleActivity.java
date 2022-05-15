package com.tuanche.directselling.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 售后服务活动
 */
public class AfterSaleActivity implements Serializable {

    private Integer id;

    /**
     *  业务类型 1 经销商  2汽修厂
     */
    private Integer businessType;
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
     *  开户状态 0 未开户  1已开启
     */
    private Integer onState;
    /**
     *  预算
     */
    private BigDecimal budget;
    /**
     *  报警金额
     */
    private BigDecimal amountAlarm;
    /**
     *  报警手机号
     */
    private String alarmPhone;
    /**
     * 报警邮箱
     */
    private String alarmEmail;
    /**
     * 分账比率
     */
    private BigDecimal subAccountRatio;

    /**
     * 实际打款金额
     */
    private BigDecimal actualAmount;

    /**
     * 打款时间
     */
    private Date paymentTime;

    /**
     * 打款备注
     */
    private String paymentRemark;

    /**
     * 打款人
     */
    private Integer paymentBy;
    /**
     * 经销商活动在结束前几天可以查看手机明码 0：不可查看手机明码
     */
    private Integer lookPhoneBeforeDays;
    /**
     *  是否开启分账
     */
    private Integer onSubAccount;

    /**
     * 流程编号
     */
    private String paymentFlowNum;

    /**
     * 经销商公众号二维码
     */
    private String publicQrCode;
    /**
     * 删除状态 0 未删，1已删
     */
    private Boolean deleteFlag;

    private Integer createBy;

    private Date createDt;

    private Integer updateBy;

    private Date updateDt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
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

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public BigDecimal getAmountAlarm() {
        return amountAlarm;
    }

    public void setAmountAlarm(BigDecimal amountAlarm) {
        this.amountAlarm = amountAlarm;
    }

    public String getAlarmPhone() {
        return alarmPhone;
    }

    public void setAlarmPhone(String alarmPhone) {
        this.alarmPhone = alarmPhone;
    }

    public String getAlarmEmail() {
        return alarmEmail;
    }

    public void setAlarmEmail(String alarmEmail) {
        this.alarmEmail = alarmEmail;
    }

    public BigDecimal getSubAccountRatio() {
        return subAccountRatio;
    }

    public void setSubAccountRatio(BigDecimal subAccountRatio) {
        this.subAccountRatio = subAccountRatio;
    }

    public String getPublicQrCode() {
        return publicQrCode;
    }

    public void setPublicQrCode(String publicQrCode) {
        this.publicQrCode = publicQrCode;
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getPaymentRemark() {
        return paymentRemark;
    }

    public void setPaymentRemark(String paymentRemark) {
        this.paymentRemark = paymentRemark;
    }

    public Integer getPaymentBy() {
        return paymentBy;
    }

    public void setPaymentBy(Integer paymentBy) {
        this.paymentBy = paymentBy;
    }

    public String getPaymentFlowNum() {
        return paymentFlowNum;
    }

    public void setPaymentFlowNum(String paymentFlowNum) {
        this.paymentFlowNum = paymentFlowNum;
    }

    public Integer getLookPhoneBeforeDays() {
        return lookPhoneBeforeDays;
    }

    public void setLookPhoneBeforeDays(Integer lookPhoneBeforeDays) {
        this.lookPhoneBeforeDays = lookPhoneBeforeDays;
    }

    public Integer getOnSubAccount() {
        return onSubAccount;
    }

    public void setOnSubAccount(Integer onSubAccount) {
        this.onSubAccount = onSubAccount;
    }
}