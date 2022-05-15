package com.tuanche.directselling.enums;

/**
 * @author ZhangYixin
 */
public enum StatDataType {
    /**
     * 0:B端数据 1:C端数据 2:裸链接 3:用户运营渠道 4:销售渠道
     */
    STAT_B(0),
    STAT_C(1),
    STAT_NO_SIGN_CHANNEL(2),
    STAT_USER_CHANNEL(3),
    STAT_SALE_CHANNEL(4);

    private int type;

    StatDataType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

}
