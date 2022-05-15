package com.tuanche.directselling.dto;

import java.io.Serializable;

public class FissionGoodsApp extends FissionGoods implements Serializable {

    private String goodsDetailsUrl;

    public String getGoodsDetailsUrl() {
        return goodsDetailsUrl;
    }

    public void setGoodsDetailsUrl(String goodsDetailsUrl) {
        this.goodsDetailsUrl = goodsDetailsUrl;
    }
}

