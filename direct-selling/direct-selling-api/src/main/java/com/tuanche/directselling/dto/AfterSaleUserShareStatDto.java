package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.AfterSaleUserShare;

import java.io.Serializable;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/7/20 20:58
 **/
public class AfterSaleUserShareStatDto extends AfterSaleUserShare implements Serializable {
    private Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
