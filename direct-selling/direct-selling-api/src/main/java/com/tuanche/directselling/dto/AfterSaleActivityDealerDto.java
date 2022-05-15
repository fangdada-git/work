package com.tuanche.directselling.dto;

import java.io.Serializable;


public class AfterSaleActivityDealerDto implements Serializable {

    /**
     * 经销商ID
     */
    private Integer id;
    /**
     * 经销商名称
     */
    private String dealerName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }
}

