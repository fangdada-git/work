package com.tuanche.directselling.enums;

/**
 * 页面来源类型
 *
 * @author ZhangYixin
 */
public enum PageSourceType {

    ACTIVITY_PAGE(1, "活动页"),
    POSTER(2, "海报");

    private String name;
    private int type;

    PageSourceType(int type, String name) {
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
