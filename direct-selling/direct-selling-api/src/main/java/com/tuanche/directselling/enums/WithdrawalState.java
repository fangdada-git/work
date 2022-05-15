package com.tuanche.directselling.enums;

/**
 * 提现状态
 *
 * @author ZhangYixin
 */
public enum WithdrawalState {

    WAITING_WITHDRAWAL(0, "未提现"),
    WITHDRAWALED(1, "已提现");

    private String name;
    private int status;

    WithdrawalState(int status, String name) {
        this.name = name;
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public String getName() {
        return this.name;
    }

}
