package com.tuanche.directselling.enums;

public enum FissionGoodsOrderStatus {

    UNPAID(1, "待支付"),
    //瞬间状态 暂时未用到
    PAY_ING(2, "支付中"),
    PAID(3, "已支付"),
    CHECKOUT(4, "核销"),
    APPLY_REFUND(5, "申请退款"),
    //瞬间状态 暂时未用到
    REFUND_ING(6, "退款中"),
    REFUND_SUCCESS(7, "退款完成"),
    WAIT_BUYER_CONFIRM_GOODS(8, "等待买家确认收货"),
    WAIT_SELLER_SEND_GOODS(9, "等待买家确认收货"),
    TRADE_CLOSED(10, "已取消");

    private String name;
    private int type;

    FissionGoodsOrderStatus(int type, String name) {
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
