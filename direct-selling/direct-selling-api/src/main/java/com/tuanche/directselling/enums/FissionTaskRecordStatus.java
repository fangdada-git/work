package com.tuanche.directselling.enums;

public enum FissionTaskRecordStatus {

    STATUS_0(0, "未开始或未开启数据"),
    STATUS_1(1, "已开启无积分数据"),
    STATUS_2(2, "已开启有积分数据");

    private String name;
    private Integer status;

    FissionTaskRecordStatus(Integer status, String name) {
        this.name = name;
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getName() {
        return this.name;
    }

}
