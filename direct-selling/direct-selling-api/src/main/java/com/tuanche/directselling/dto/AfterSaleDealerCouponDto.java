package com.tuanche.directselling.dto;

import java.io.Serializable;
import java.util.List;

public class AfterSaleDealerCouponDto {

    private String dealerName;

    private List<AfterSaleCouponDto> couponList;

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public List<AfterSaleCouponDto> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<AfterSaleCouponDto> couponList) {
        this.couponList = couponList;
    }
}
