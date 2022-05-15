package com.tuanche.directselling.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/4/20 17:20
 **/
public class FissionDealerOrderAmountDto implements Serializable {
    private Integer dealerId;
    private BigDecimal orderAmount;

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }
}
