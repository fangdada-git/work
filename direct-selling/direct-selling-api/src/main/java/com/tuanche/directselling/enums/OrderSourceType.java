package com.tuanche.directselling.enums;

/**
 * 下单来源页面类型
 *
 * @author ZhangYixin
 */
public enum OrderSourceType {

    LIVE_ORDER(1, "直播间下单"),
    ACTIVITY_PAGE_ORDER(2, "活动页下单");

    private String name;
    private int type;

    OrderSourceType(int type, String name) {
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
