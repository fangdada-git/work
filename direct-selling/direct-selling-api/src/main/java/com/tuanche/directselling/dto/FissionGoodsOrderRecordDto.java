package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.FissionGoodsOrderRecord;

import java.io.Serializable;

public class FissionGoodsOrderRecordDto extends FissionGoodsOrderRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    //订单总数
    private Integer orderSum=0;
    //订单核销数
    private Integer orderCheckoutSum=0;
    //订单退款数
    private Integer orderRefundSum=0;

    public Integer getOrderSum() {
        return orderSum;
    }

    public void setOrderSum(Integer orderSum) {
        this.orderSum = orderSum;
    }

    public Integer getOrderCheckoutSum() {
        return orderCheckoutSum;
    }

    public void setOrderCheckoutSum(Integer orderCheckoutSum) {
        this.orderCheckoutSum = orderCheckoutSum;
    }

    public Integer getOrderRefundSum() {
        return orderRefundSum;
    }

    public void setOrderRefundSum(Integer orderRefundSum) {
        this.orderRefundSum = orderRefundSum;
    }
}
