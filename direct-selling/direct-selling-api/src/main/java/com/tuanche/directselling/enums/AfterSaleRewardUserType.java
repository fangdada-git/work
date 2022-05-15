package com.tuanche.directselling.enums;

/**
 * @author：HuangHao
 * @CreatTime 2021-07-22 14:38
 */
public enum AfterSaleRewardUserType {

    USER_TYPE1(1, "代理人"),
    USER_TYPE2(2, "购买用户"),
    USER_TYPE3(3, "分享人");

    private String name;
    private int type;

    AfterSaleRewardUserType(int type, String name) {
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
