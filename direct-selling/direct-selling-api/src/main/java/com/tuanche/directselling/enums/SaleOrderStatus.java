package com.tuanche.directselling.enums;

/**
 * 销售对赌金支付状态
 *
 * @author ZhangYixin
 */
public enum SaleOrderStatus {

    UN_PAY(1, "待支付"),
    PAID(2, "已支付");

    private String name;
    private int type;

    SaleOrderStatus(int type, String name) {
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
