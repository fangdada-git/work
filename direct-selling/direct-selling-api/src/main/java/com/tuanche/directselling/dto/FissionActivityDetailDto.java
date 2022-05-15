package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.FissionActivity;

import java.io.Serializable;

/**
 * 裂变活动详细信息
 * @author ZhangYiXin
 * @date 2020/9/23 18:03
 * @version 1.0
 **/
public class FissionActivityDetailDto extends FissionActivity implements Serializable {

    /**
     * 销售ID
     */
    private Integer saleId;

    /**
     * 支付状态 订单状态 1：待支付  2：已支付
     */
    private Integer orderStatus;

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
}
