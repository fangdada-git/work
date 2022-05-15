package com.tuanche.directselling.enums;

public enum FissionActivityStatus {


    NOT_BEGIN(0, "未开始"),
    IN_PROGRESS(1, "进行中"),
    IS_OVER(2, "已结束");

    private String name;
    private int status;

    FissionActivityStatus(int status, String name) {
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
