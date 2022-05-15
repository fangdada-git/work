package com.tuanche.directselling.enums;

/**
 * 任务是否完成状态
 *
 * @author ZhangYixin
 */
public enum CompleteTaskStatus {

    INCOMPLETE(0, "未完成"),
    COMPLETE(1, "完成");

    private String name;
    private int status;

    CompleteTaskStatus(int status, String name) {
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
