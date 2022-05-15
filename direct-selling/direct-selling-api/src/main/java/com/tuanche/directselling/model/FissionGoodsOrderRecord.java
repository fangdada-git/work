package com.tuanche.directselling.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class FissionGoodsOrderRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    //直卖场次id
    private Integer peroidsId;
    //裂变活动id
    private Integer fissionId;
    //直播间id
    private Integer liveId;
    //商品id
    private Integer goodsId;
    private Integer goodsNum;
    //订单编号
    private String orderNo;
    //订单状态:  1:待支付 2:支付中 3:已支付 4:核销 5:申请退款 6:退款中 7:退款完成 8:等待买家确认收货 
    private Integer orderStatus;
    //订单金额
    private BigDecimal orderMoney;
    //订单形式  1：邮寄  2：核销
    private Integer orderFormal=2;
    //用户来源 1:销售分享链接  2:用户分享链接
    private Integer userSource;
    //页面来源  0：自然流量  1：直播间下单  2：活动页下单
    private Integer pageSource;
    //支付来源  0:pc 1:wap 2:app  3：微信小程序
    private Integer paySource;
    //用户微信openid
    private String userWxOpenId;
    private String userWxUnionId;
    //用户id
    private Integer userId;
    private String userName;
    private String userPhone;
    //删除状态 0：未删除 1：已删除
    private Integer orderCityId;
    private Integer deleteFlag;

    private Integer dealerId;
    private List<Integer> dealerIdList;

    private Integer saleId;
    private String saleWxUnionId;

    private Integer cancelDealerId;
    private List<Integer> cancelDealerIdList;

    private Integer cancelSale;
    private Integer optUserId;

    //品牌id
    private Integer brandId;
    //车型id
    private Integer styleId;

    /**
     * 渠道
     */
    private Integer channel;

    /**
      * 终端设置类型   http://wiki.tuanche.cn/pages/viewpage.action?pageId=122095391#id-数据字典服务-2、终端设置类型（sid）定义及枚举值
      */
    private Integer sid;

    //支付页面当前地址
    private String pageUrl;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPeroidsId() {
        return peroidsId;
    }

    public void setPeroidsId(Integer peroidsId) {
        this.peroidsId = peroidsId;
    }

    public Integer getFissionId() {
        return fissionId;
    }

    public void setFissionId(Integer fissionId) {
        this.fissionId = fissionId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getPageSource() {
        return pageSource;
    }

    public void setPageSource(Integer pageSource) {
        this.pageSource = pageSource;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getUserSource() {
        return userSource;
    }

    public void setUserSource(Integer userSource) {
        this.userSource = userSource;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public BigDecimal getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(BigDecimal orderMoney) {
        this.orderMoney = orderMoney;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Integer getOrderFormal() {
        return orderFormal;
    }

    public void setOrderFormal(Integer orderFormal) {
        this.orderFormal = orderFormal;
    }

    public Integer getOrderCityId() {
        return orderCityId;
    }

    public void setOrderCityId(Integer orderCityId) {
        this.orderCityId = orderCityId;
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public Integer getSaleId() {
        return saleId;
    }

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }

    public Integer getCancelDealerId() {
        return cancelDealerId;
    }

    public void setCancelDealerId(Integer cancelDealerId) {
        this.cancelDealerId = cancelDealerId;
    }

    public Integer getCancelSale() {
        return cancelSale;
    }

    public void setCancelSale(Integer cancelSale) {
        this.cancelSale = cancelSale;
    }

    public Integer getOptUserId() {
        return optUserId;
    }

    public void setOptUserId(Integer optUserId) {
        this.optUserId = optUserId;
    }

    public Integer getLiveId() {
        return liveId;
    }

    public void setLiveId(Integer liveId) {
        this.liveId = liveId;
    }

    public List<Integer> getDealerIdList() {
        return dealerIdList;
    }

    public void setDealerIdList(List<Integer> dealerIdList) {
        this.dealerIdList = dealerIdList;
    }

    public List<Integer> getCancelDealerIdList() {
        return cancelDealerIdList;
    }

    public void setCancelDealerIdList(List<Integer> cancelDealerIdList) {
        this.cancelDealerIdList = cancelDealerIdList;
    }

    public Integer getPaySource() {
        return paySource;
    }

    public void setPaySource(Integer paySource) {
        this.paySource = paySource;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getStyleId() {
        return styleId;
    }

    public void setStyleId(Integer styleId) {
        this.styleId = styleId;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
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

    public String getSaleWxUnionId() {
        return saleWxUnionId;
    }

    public void setSaleWxUnionId(String saleWxUnionId) {
        this.saleWxUnionId = saleWxUnionId;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }
}