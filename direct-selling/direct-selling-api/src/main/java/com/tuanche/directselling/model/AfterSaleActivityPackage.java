package com.tuanche.directselling.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 活动套餐卡
 */
public class AfterSaleActivityPackage implements Serializable {
    private Integer id;
    /**
     *  活动id
     */
    private Integer activityId;
    /**
     * 商品id（套餐）
     */
    private Integer goodsId;
    /**
     * 套餐名称
     */
    private String commodityName;
    /**
     * 套餐价格
     */
    private BigDecimal commodityPrice;
    /**
     * 数量
     */
    private Integer commodityCount;
    /**
     * 原价
     */
    private BigDecimal originalPrice;
    /**
     * 上架时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date upShelfTime;
    /**
     * 下架时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date downShelfTime;
    /**
     * 关联状态（1已关联 2取消关联）
     */
    private Integer relStatus;
    /**
     * 关联时间
     */
    private Date relTime;

    private Integer operateUser;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
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

    public Integer getCommodityCount() {
        return commodityCount;
    }

    public void setCommodityCount(Integer commodityCount) {
        this.commodityCount = commodityCount;
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

    public Integer getRelStatus() {
        return relStatus;
    }

    public void setRelStatus(Integer relStatus) {
        this.relStatus = relStatus;
    }

    public Date getRelTime() {
        return relTime;
    }

    public void setRelTime(Date relTime) {
        this.relTime = relTime;
    }

    public Integer getOperateUser() {
        return operateUser;
    }

    public void setOperateUser(Integer operateUser) {
        this.operateUser = operateUser;
    }
}