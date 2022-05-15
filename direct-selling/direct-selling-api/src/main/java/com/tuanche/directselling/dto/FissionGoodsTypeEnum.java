package com.tuanche.directselling.dto;

public enum FissionGoodsTypeEnum {

    VOUCHER("voucher", "代金券"),
    STORE_GIFT("store_gift", "到店礼"),
    FIXED_PRICE("fixed_price", "一口价"),
    PREFERENTIAL("preferential", "购车优惠");


    private String value;

    private String tag;

    FissionGoodsTypeEnum(String value, String tag) {
        this.value = value;
        this.tag = tag;
    }

    public String value() {
        return this.value;
    }

    public String tag() {
        return this.tag;
    }

    public static FissionGoodsTypeEnum get(String value) {
        for(FissionGoodsTypeEnum item : FissionGoodsTypeEnum.values()) {
            if(item.value().equals(value)) {
                return item;
            }
        }
        return null;
    }
}
