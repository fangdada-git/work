package com.tuanche.directselling.enums;

public enum FissionTradeStatus {

    TRADING(0, "交易中"),
    SUCCESS(1, "成功"),
    FAIL(2, "失败");

    private String name;
    private int type;

    FissionTradeStatus(int type, String name) {
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
