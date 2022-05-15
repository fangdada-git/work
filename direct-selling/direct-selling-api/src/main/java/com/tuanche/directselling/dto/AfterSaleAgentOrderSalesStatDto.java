package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.AfterSaleOrderRecord;

import java.io.Serializable;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/7/21 14:20
 **/
public class AfterSaleAgentOrderSalesStatDto extends AfterSaleOrderRecord implements Serializable {
    private Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
