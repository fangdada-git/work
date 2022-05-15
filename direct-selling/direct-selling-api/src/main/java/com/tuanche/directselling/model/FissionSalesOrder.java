package com.tuanche.directselling.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FissionSalesOrder implements Serializable {

    private static final long serialVersionUID = 8667873512145882654L;

    private Integer id;
    //订单编号
    private String orderNo;
    //直卖场次id
    private Integer periodsId;
    //裂变活动id
    private Integer fissionId;
    //经销商id
    private Integer dealerId;
    //销售id
    private Integer saleId;
    private List<Integer> saleIdList;
    //订单状态 1：待支付  2：已支付 3:支付中
    private Integer orderStatus;
    //订单类型  1：销售对赌金
    private Integer orderType;
    //订单金额 （元）
    private BigDecimal orderAmount;
    //支付类型
    // 1:(ios  weixin alipay，ios微信里选择支付宝支付)
    // 2：(android  weixin alipay，android微信里选择支付宝支付)
    // 3:(other alipay浏览器选择支付宝支付)
    // 4:(other weixinpay浏览器选择微信支付)
    // -1:(微信里面选择微信支付)
    private Byte payType;
    //支付时间
    private Date payTime;
    //创建时间
    private Date createDt;
    //修改时间
    private Date updateDt;
    //操作人id
    private Integer updateUserId;
    //删除状态 0：未删除 1：已删除
    private Integer deleteFlag;
    //销售微信openid
    private String wxUnionId;
    private String wxOpenId;

    /**
     * 支付开始时间
     */
    private Date payBeginTime;

    /**
     * 支付结束时间
     */
    private Date payEndTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public Integer getPeriodsId() {
        return periodsId;
    }

    public void setPeriodsId(Integer periodsId) {
        this.periodsId = periodsId;
    }

    public Integer getFissionId() {
        return fissionId;
    }

    public void setFissionId(Integer fissionId) {
        this.fissionId = fissionId;
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

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Byte getPayType() {
        return payType;
    }

    public void setPayType(Byte payType) {
        this.payType = payType;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
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

    public Integer getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public List<Integer> getSaleIdList() {
        return saleIdList;
    }

    public void setSaleIdList(List<Integer> saleIdList) {
        this.saleIdList = saleIdList;
    }

    public String getWxUnionId() {
        return wxUnionId;
    }

    public void setWxUnionId(String wxUnionId) {
        this.wxUnionId = wxUnionId;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public Date getPayBeginTime() {
        return payBeginTime;
    }

    public void setPayBeginTime(Date payBeginTime) {
        this.payBeginTime = payBeginTime;
    }

    public Date getPayEndTime() {
        return payEndTime;
    }

    public void setPayEndTime(Date payEndTime) {
        this.payEndTime = payEndTime;
    }
}