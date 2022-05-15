package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.AfterSaleUserBrowse;

import java.io.Serializable;

public class AfterSaleUserBrowseStatDto extends AfterSaleUserBrowse implements Serializable {
    private Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}