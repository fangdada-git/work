package com.tuanche.directselling.enums;

public enum CityBrandDataType {
    CITY(0, "城市"),
    BRAND(1, "品牌");

    private String name;
    private int type;

    CityBrandDataType(int type, String name) {
        this.name = name;
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }
}
