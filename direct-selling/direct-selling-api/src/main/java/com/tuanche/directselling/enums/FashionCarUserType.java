package com.tuanche.directselling.enums;

/**
 * 半价车中奖码用户类型 0:被助力人 1:助力人 2:转盘中奖人 3:答题中奖人 4:半价车
 */
public enum FashionCarUserType {
    /**
     * 被助力人
     */
    HELPED(0, "被助力人"),
    /**
     * 助力人
     */
    HELPER(1, "助力人"),
    /**
     * 转盘中奖人
     */
    WINNING_USER(2, "转盘中奖人"),
    /**
     * 答题中奖者
     */
    ANSWER_USER(3, "答题中奖人"),

    HALF_PRICE_USER(4, "半价车");

    private String name;
    private Integer type;

    FashionCarUserType(Integer type, String name) {
        this.name = name;
        this.type = type;
    }

    public Integer getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }
}
