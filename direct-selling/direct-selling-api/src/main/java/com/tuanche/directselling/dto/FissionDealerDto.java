package com.tuanche.directselling.dto;

import java.io.Serializable;

public class FissionDealerDto implements Serializable {

    private Integer dealerId;
    private String dealerName;
    private String address;

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }
}
