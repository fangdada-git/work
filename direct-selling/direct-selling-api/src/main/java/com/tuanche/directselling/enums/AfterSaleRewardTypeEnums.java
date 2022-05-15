package com.tuanche.directselling.enums;

/**
 * 售后特卖奖励类型
 */
public enum AfterSaleRewardTypeEnums {

    REWARD_TYPE1(1, "分享红包"),
    REWARD_TYPE2(2, "购买红包"),
    REWARD_TYPE3(3, "礼品券"),
    REWARD_TYPE4(4, "邀请额外奖励");

    private String name;
    private int type;

    AfterSaleRewardTypeEnums(int type, String name) {
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
