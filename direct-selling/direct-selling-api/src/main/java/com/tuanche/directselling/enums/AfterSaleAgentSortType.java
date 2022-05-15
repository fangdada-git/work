package com.tuanche.directselling.enums;

public enum AfterSaleAgentSortType {
    SHARE("share", "分享字数"),
    PAGE_VIEW("pageView", "浏览次数"),
    UNIQUE_VISITOR("uniqueVisitor", "浏览人数"),
    PACKAGE_CARD_COUNT("packageCard", "套餐卡售出数");

    private String name;
    private String type;

    AfterSaleAgentSortType(String type, String name) {
        this.name = name;
        this.type = type;
    }

    public static AfterSaleAgentSortType getEnumType(String type) {
        if (type == null) {
            return AfterSaleAgentSortType.SHARE;
        }
        AfterSaleAgentSortType[] sortType = AfterSaleAgentSortType.values();
        for (int i = 0; i < sortType.length; i++) {
            if (sortType[i].getType().equals(type)) {
                return sortType[i];
            }
        }
        return AfterSaleAgentSortType.SHARE;
    }

    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }
}
