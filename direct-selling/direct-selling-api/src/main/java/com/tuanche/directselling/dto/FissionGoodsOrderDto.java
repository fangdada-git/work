package com.tuanche.directselling.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tuanche.directselling.enums.FissionGoodsOrderStatus;
import com.tuanche.directselling.utils.GlobalConstants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FissionGoodsOrderDto implements Serializable {
    private static final long serialVersionUID = 4634990148266422467L;
    //订单id
    private Integer orderId;
    //订单编号
    private String orderNo;
    //核销码
    private String checkoutCode;
    //核销链接
    private String checkoutUrl;
    //订单创建时间
    private Date orderCreateDt;
    private String orderCreateDtStr;
    //订单支付时间
    private Date payTime;
    private String payTimeStr;
    //订单金额
    private BigDecimal orderMoney;
    //订单状态
    private FissionGoodsOrderStatus orderStatus;
    private Integer orderStatusType;
    private String orderStatusName;
    //订单形式   1：邮寄 0：核销
    private Integer orderFormal;
    //商品id  
    private Integer goodsId;
    //商品名称
    private String goodsName;
    private String goodsHeadImage;
    private BigDecimal goodsPrice;
    /**
     * 上架形式 0:下架  1:上架
     */
    private Integer putAwayType=0;
    /**
     * 已售商品数量
     */
    private Integer soldNumber=0;

    /**
     * 已售虚拟商品数量
     */
    private Integer virtualSoldNumber=0;
    /**
     * 商品库存数量
     */
    private Integer commodityCount=0;
    /**
     * 是否秒杀类商品 0：否   1：是
     */
    private Integer seckill;
    //商品数量
    private Integer num;
    //下单人id/名称
    private Integer userId;
    private String userName;
    private String userPhone;
    //收件人姓名
    private String receiverName;
    //收件人手机号
    private String receiverPhone;
    //城市id/名称
    private Integer orderCityId;
    private String orderCityName;
    //裂变场次id/名称
    private Integer peroidsId;
    private String peroidsName;
    //裂变活动id/名称
    private Integer fissionActivityId;
    private String fissionActivityName;
    private Date fissionActivityStartDate;
    private Date fissionActivityEndDate;
    private String fissionActivityStartDateStr;
    private String fissionActivityEndDateStr;
    //直播间id
    private Integer liveId;
    private String liveName;
    //下单所属经销商id/名称
    private Integer dealerId;
    private String dealerName;
    //下单所属销售id/名称
    private Integer saleId;
    private String saleName;
    //核销经销商id/名称
    private Integer cancelDealerId;
    private String cancelDealerName;
    //核销销售id/名称
    private Integer cancelSaleId;
    private String cancelSaleName;
    //核销时间
    private Date cancelDate;
    //微信交易流水号
    private String tradeNo;
    //退款微信交易流水号
    private String refundTradeNo;
    //退款时间
    private Date refundDate;
    /**
     * 订单来源   1直播间 2活动页
     */
    private Integer orderSource;
    private String orderSourceName;
    //是否观看过直播   0为未观看过  非0为观看过（为用户任务记录表id）
    private Integer watchLive;
    private String watchLiveName;
    //裂变渠道
    private Integer orderChannel;
    private String orderChannelName;
    //订单过期时间
    private Date expiredTime;
    private Long expiredTimestamp;

    private String userWxUnionId;

    private Date beginPayDate;
    private Date endPayDate;
    private Date beginRefundDate;
    private Date endRefundDate;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCheckoutCode() {
        return checkoutCode;
    }

    public void setCheckoutCode(String checkoutCode) {
        this.checkoutCode = checkoutCode;
    }

    public Date getOrderCreateDt() {
        return orderCreateDt;
    }

    public void setOrderCreateDt(Date orderCreateDt) {
        this.orderCreateDt = orderCreateDt;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public BigDecimal getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(BigDecimal orderMoney) {
        this.orderMoney = orderMoney;
    }

    public FissionGoodsOrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(FissionGoodsOrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    public void setCheckoutUrl(String checkoutUrl) {
        this.checkoutUrl = checkoutUrl;
    }

    public Integer getOrderFormal() {
        return orderFormal;
    }

    public void setOrderFormal(Integer orderFormal) {
        this.orderFormal = orderFormal;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Integer getOrderCityId() {
        return orderCityId;
    }

    public void setOrderCityId(Integer orderCityId) {
        this.orderCityId = orderCityId;
    }

    public String getOrderCityName() {
        return orderCityName;
    }

    public void setOrderCityName(String orderCityName) {
        this.orderCityName = orderCityName;
    }

    public Integer getFissionActivityId() {
        return fissionActivityId;
    }

    public void setFissionActivityId(Integer fissionActivityId) {
        this.fissionActivityId = fissionActivityId;
    }

    public String getFissionActivityName() {
        return fissionActivityName;
    }

    public void setFissionActivityName(String fissionActivityName) {
        this.fissionActivityName = fissionActivityName;
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
        this.dealerName = dealerName;
    }

    public Integer getSaleId() {
        return saleId;
    }

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }

    public Integer getCancelDealerId() {
        return cancelDealerId;
    }

    public void setCancelDealerId(Integer cancelDealerId) {
        this.cancelDealerId = cancelDealerId;
    }

    public String getCancelDealerName() {
        return cancelDealerName;
    }

    public void setCancelDealerName(String cancelDealerName) {
        this.cancelDealerName = cancelDealerName;
    }

    public Integer getCancelSaleId() {
        return cancelSaleId;
    }

    public void setCancelSaleId(Integer cancelSaleId) {
        this.cancelSaleId = cancelSaleId;
    }

    public String getCancelSaleName() {
        return cancelSaleName;
    }

    public void setCancelSaleName(String cancelSaleName) {
        this.cancelSaleName = cancelSaleName;
    }

    public Date getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(Date cancelDate) {
        this.cancelDate = cancelDate;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getRefundTradeNo() {
        return refundTradeNo;
    }

    public void setRefundTradeNo(String refundTradeNo) {
        this.refundTradeNo = refundTradeNo;
    }

    public Date getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(Date refundDate) {
        this.refundDate = refundDate;
    }

    public Integer getPeroidsId() {
        return peroidsId;
    }

    public void setPeroidsId(Integer peroidsId) {
        this.peroidsId = peroidsId;
    }

    public String getPeroidsName() {
        return peroidsName;
    }

    public void setPeroidsName(String peroidsName) {
        this.peroidsName = peroidsName;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Date getFissionActivityStartDate() {
        return fissionActivityStartDate;
    }

    public void setFissionActivityStartDate(Date fissionActivityStartDate) {
        this.fissionActivityStartDate = fissionActivityStartDate;
    }

    public Date getFissionActivityEndDate() {
        return fissionActivityEndDate;
    }

    public void setFissionActivityEndDate(Date fissionActivityEndDate) {
        this.fissionActivityEndDate = fissionActivityEndDate;
    }

    public String getOrderStatusName() {
        if (orderStatus!=null) return orderStatus.getName();
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public Integer getOrderStatusType() {
        if (orderStatus!=null) return orderStatus.getType();
        return orderStatusType;
    }

    public void setOrderStatusType(Integer orderStatusType) {
        this.orderStatusType = orderStatusType;
    }

    public Integer getSeckill() {
        return seckill;
    }

    public void setSeckill(Integer seckill) {
        this.seckill = seckill;
    }

    public String getPayTimeStr() {
        if (payTime!=null) {
            return GlobalConstants.dateToString("yyyy-MM-dd HH:mm:ss",payTime);
        }
        return payTimeStr;
    }

    public void setPayTimeStr(String payTimeStr) {
        this.payTimeStr = payTimeStr;
    }

    public String getGoodsHeadImage() {
        return goodsHeadImage;
    }

    public void setGoodsHeadImage(String goodsHeadImage) {
        this.goodsHeadImage = goodsHeadImage;
    }

    public String getFissionActivityStartDateStr() {
        if (fissionActivityStartDate!=null) {
            return GlobalConstants.dateToString("yyyy-MM-dd HH:mm:ss",fissionActivityStartDate);
        }
        return fissionActivityStartDateStr;
    }

    public void setFissionActivityStartDateStr(String fissionActivityStartDateStr) {
        this.fissionActivityStartDateStr = fissionActivityStartDateStr;
    }

    public String getFissionActivityEndDateStr() {
        if (fissionActivityEndDate!=null) {
            return GlobalConstants.dateToString("yyyy-MM-dd HH:mm:ss",fissionActivityEndDate);
        }
        return fissionActivityEndDateStr;
    }

    public void setFissionActivityEndDateStr(String fissionActivityEndDateStr) {
        this.fissionActivityEndDateStr = fissionActivityEndDateStr;
    }

    public Integer getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(Integer orderSource) {
        if (orderSource!=null) {
            this.orderSourceName = GlobalConstants.FissionOrderSource.FISSION_ORDER_SOURCE_MAP.get(orderSource+"");
        }
        this.orderSource = orderSource;
    }

    public String getOrderCreateDtStr() {
        if (orderCreateDt!=null) {
            return GlobalConstants.dateToString("yyyy-MM-dd HH:mm:ss",orderCreateDt);
        }
        return orderCreateDtStr;
    }

    public void setOrderCreateDtStr(String orderCreateDtStr) {
        this.orderCreateDtStr = orderCreateDtStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public Integer getPutAwayType() {
        return putAwayType;
    }

    public void setPutAwayType(Integer putAwayType) {
        this.putAwayType = putAwayType;
    }

    public Integer getSoldNumber() {
        return soldNumber;
    }

    public void setSoldNumber(Integer soldNumber) {
        this.soldNumber = soldNumber;
    }

    public Integer getVirtualSoldNumber() {
        return virtualSoldNumber;
    }

    public void setVirtualSoldNumber(Integer virtualSoldNumber) {
        this.virtualSoldNumber = virtualSoldNumber;
    }

    public Integer getCommodityCount() {
        return commodityCount;
    }

    public void setCommodityCount(Integer commodityCount) {
        this.commodityCount = commodityCount;
    }

    public Integer getLiveId() {
        return liveId;
    }

    public void setLiveId(Integer liveId) {
        this.liveId = liveId;
    }

    public String getLiveName() {
        return liveName;
    }

    public void setLiveName(String liveName) {
        this.liveName = liveName;
    }

    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }

    public Long getExpiredTimestamp() {
        if (this.expiredTime !=null) {
            return this.expiredTime.getTime();
        }
        return expiredTimestamp;
    }

    public void setExpiredTimestamp(Long expiredTimestamp) {
        this.expiredTimestamp = expiredTimestamp;
    }

    public Integer getWatchLive() {
        return watchLive;
    }

    public void setWatchLive(Integer watchLive) {
        this.watchLive = watchLive;
    }

    public Integer getOrderChannel() {
        return orderChannel;
    }

    public void setOrderChannel(Integer orderChannel) {
        this.orderChannel = orderChannel;
    }

    public String getWatchLiveName() {
        if (watchLive!=null) {
            if (watchLive==0) return "否";
            else return "是";
        }
        return watchLiveName;
    }

    public void setWatchLiveName(String watchLiveName) {
        this.watchLiveName = watchLiveName;
    }

    public String getOrderChannelName() {
        if (orderChannel!=null) {
            return GlobalConstants.FissionChannel.FISSION_CHANNEL_MAP.get(orderChannel);
        }
        return orderChannelName;
    }

    public void setOrderChannelName(String orderChannelName) {

        this.orderChannelName = orderChannelName;
    }

    public String getUserWxUnionId() {
        return userWxUnionId;
    }

    public void setUserWxUnionId(String userWxUnionId) {
        this.userWxUnionId = userWxUnionId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Date getBeginPayDate() {
        return beginPayDate;
    }

    public void setBeginPayDate(Date beginPayDate) {
        this.beginPayDate = beginPayDate;
    }

    public Date getEndPayDate() {
        return endPayDate;
    }

    public void setEndPayDate(Date endPayDate) {
        this.endPayDate = endPayDate;
    }

    public Date getBeginRefundDate() {
        return beginRefundDate;
    }

    public void setBeginRefundDate(Date beginRefundDate) {
        this.beginRefundDate = beginRefundDate;
    }

    public Date getEndRefundDate() {
        return endRefundDate;
    }

    public void setEndRefundDate(Date endRefundDate) {
        this.endRefundDate = endRefundDate;
    }

    public String getOrderSourceName() {
        return orderSourceName;
    }

}
