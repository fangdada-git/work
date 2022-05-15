package com.tuanche.directselling.enums;

/**
 * 经销商 备案用户 流失用户
 */
public enum AfterSaleUserType {
    /**
     * 0:备案用户 1:流失用户
     */
    KEEP_ON_USER(0, "备案用户"),
    LOST_USER(1, "流失用户");

    private String name;
    private int type;

    AfterSaleUserType(int type, String name) {
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
