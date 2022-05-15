package com.tuanche.directselling.enums;

/**
 * 任务类型枚举
 */
public enum FissionDictType {
    LB((short) 1, "裂变任务"),
    CHANNEL((short) 2, "裂变渠道");

    private String name;
    private short type;

    FissionDictType(short type, String name) {
        this.name = name;
        this.type = type;
    }

    public short getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }
}
