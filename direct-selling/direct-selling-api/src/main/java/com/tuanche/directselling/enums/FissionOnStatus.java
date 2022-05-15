package com.tuanche.directselling.enums;

public enum FissionOnStatus {

    CLOSE(0, "未开启"),
    OPEN(1, "开启");

    private String name;
    private int status;

    FissionOnStatus(int status, String name) {
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
