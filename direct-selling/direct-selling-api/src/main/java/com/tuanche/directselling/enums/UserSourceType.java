package com.tuanche.directselling.enums;

/**
 * 分享链接类型
 *
 * @author ZhangYixin
 */
public enum UserSourceType {

    SALE_SHARE(1, "销售分享链接"),
    USER_SHARE(2, "用户分享链接");

    private String name;
    private int type;

    UserSourceType(int type, String name) {
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
