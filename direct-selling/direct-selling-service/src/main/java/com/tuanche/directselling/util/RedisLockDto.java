package com.tuanche.directselling.util;

/**
 * @author：HuangHao
 * @CreatTime 2020-11-13 11:31
 */
public class RedisLockDto {
    private boolean isLock;
    private String value;

    RedisLockDto(){

    }
    public RedisLockDto(boolean isLock, String value){
        this.isLock = isLock;
        this.value = value;
    }
    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
