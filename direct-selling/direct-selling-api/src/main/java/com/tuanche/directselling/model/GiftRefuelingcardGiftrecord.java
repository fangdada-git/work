package com.tuanche.directselling.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class GiftRefuelingcardGiftrecord implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     * 经销商ID
     */
    private Integer dealerId;

    /**
     * 经销商名称
     */
    private String dealerName;

    /**
     * 大场次ID
     */
    private Integer periodsId;

    /**
     * 大场次名称
     */
    private String periodsName;

    /**
     * 订单渠道 tmall：淘宝 youzan：有赞
     */
    protected String orderChannel;

    /**
     * 淘宝订单ID
     */
    private String platformOrderId;

    /**
     * 淘宝商品id
     */
    private String goodsTbId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 姓名
     */
    private String name;
    /**
     * 订单金额
     */
    private BigDecimal orderMoney;
    /**
     * 商品类型
     */
    private String commodityType;
    /**
     * 商品类型名称
     */
    private String commodityTypeName;

    /**
     * 赠品-油卡表ID
     */
    private Integer refuelingcarId;

    /**
     * 油卡发送时间（多次发送以最后发送时间为准）
     */
    private Date sendTime;

    /**
     * 品牌ID
     */
    private Integer cbId;

    /**
     * 品牌名称
     */
    private String cbName;

    /**
     * 车型ID
     */
    private Integer csId;

    /**
     * 车型名称
     */
    private String csName;

    /**
     * 是否删除 0未删除 1 删除
     */
    private Boolean deleteFlag;

    /**
     * 创建时间
     */
    private Date createDt;

    /**
     * 创建人
     */
    private String createName;

    /**
     * 更新时间
     */
    private Date updateDt;

    /**
     * 更新人
     */
    private String updateName;

    /**
     * gift_refuelingcard_giftrecord
     */
    private static final long serialVersionUID = 1L;

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
        this.dealerName = dealerName;
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

    public String getOrderChannel() {
        return orderChannel;
    }

    public void setOrderChannel(String orderChannel) {
        this.orderChannel = orderChannel;
    }

    public String getPlatformOrderId() {
        return platformOrderId;
    }

    public void setPlatformOrderId(String platformOrderId) {
        this.platformOrderId = platformOrderId;
    }

    public String getGoodsTbId() {
        return goodsTbId;
    }

    public void setGoodsTbId(String goodsTbId) {
        this.goodsTbId = goodsTbId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(BigDecimal orderMoney) {
        this.orderMoney = orderMoney;
    }

    public String getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(String commodityType) {
        this.commodityType = commodityType;
    }

    public String getCommodityTypeName() {
        return commodityTypeName;
    }

    public void setCommodityTypeName(String commodityTypeName) {
        this.commodityTypeName = commodityTypeName;
    }

    public Integer getRefuelingcarId() {
        return refuelingcarId;
    }

    public void setRefuelingcarId(Integer refuelingcarId) {
        this.refuelingcarId = refuelingcarId;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getCbId() {
        return cbId;
    }

    public void setCbId(Integer cbId) {
        this.cbId = cbId;
    }

    public String getCbName() {
        return cbName;
    }

    public void setCbName(String cbName) {
        this.cbName = cbName;
    }

    public Integer getCsId() {
        return csId;
    }

    public void setCsId(Integer csId) {
        this.csId = csId;
    }

    public String getCsName() {
        return csName;
    }

    public void setCsName(String csName) {
        this.csName = csName;
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

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }
}