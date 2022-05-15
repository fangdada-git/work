package com.tuanche.directselling.enums;

/**
 * 奖励规则类型
 *
 * @author ZhangYixin
 */
public enum AwardRuleType {

    B(1, "B端"),
    C(2, "C端");

    private String name;
    private int type;

    AwardRuleType(int type, String name) {
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
