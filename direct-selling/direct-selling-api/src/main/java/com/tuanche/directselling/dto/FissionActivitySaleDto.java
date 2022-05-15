package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.FissionActivity;

import java.io.Serializable;

/**
 * 裂变活动和销售信息
 * @author ZhangYiXin
 * @date 2020/9/23 18:03
 * @version 1.0
 **/
public class FissionActivitySaleDto extends FissionActivity implements Serializable {

    /**
     * 销售ID
     */
    private Integer saleId;

    public Integer getSaleId() {
        return saleId;
    }

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }
}
