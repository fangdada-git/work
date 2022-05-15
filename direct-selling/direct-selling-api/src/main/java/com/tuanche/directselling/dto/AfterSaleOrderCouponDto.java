package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.AfterSaleOrderRecord;
import com.tuanche.directselling.utils.AfterSaleConstants;

import java.util.List;

public class AfterSaleOrderCouponDto extends AfterSaleOrderRecord {

    private String goodsName;
    //订单类型-格式化后的
    private String orderTypeName;
    private String orderStatusName;
    //应发卡券数量
    private Integer couponCount=0;
    //卡券发放总数量   coupon_status=0、1、2
    private Integer couponCountSum=0;
    //卡券发放数量 coupon_status=1、2
    private Integer couponGiveOutCount=0;
    //卡券使用数量 coupon_status=2
    private Integer couponUseCount=0;
    //卡券列表
    private List<AfterSaleCouponDto> couponList;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getOrderTypeName() {
        if(getOrderType() != null){
            return AfterSaleConstants.OrderType.getKey(getOrderType());
        }
        return orderTypeName;
    }

    public void setOrderTypeName(String orderTypeName) {
        this.orderTypeName = orderTypeName;
    }

    public String getOrderStatusName() {
        if (getOrderStatus()!=null) {
            return AfterSaleConstants.OrderStatus.getKey(getOrderStatus());
        }
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public Integer getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(Integer couponCount) {
        this.couponCount = couponCount;
    }

    public List<AfterSaleCouponDto> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<AfterSaleCouponDto> couponList) {
        this.couponList = couponList;
    }

    public Integer getCouponGiveOutCount() {
        return couponGiveOutCount;
    }

    public void setCouponGiveOutCount(Integer couponGiveOutCount) {
        this.couponGiveOutCount = couponGiveOutCount;
    }

    public Integer getCouponUseCount() {
        return couponUseCount;
    }

    public void setCouponUseCount(Integer couponUseCount) {
        this.couponUseCount = couponUseCount;
    }

    public Integer getCouponCountSum() {
        return couponCountSum;
    }

    public void setCouponCountSum(Integer couponCountSum) {
        this.couponCountSum = couponCountSum;
    }
}
