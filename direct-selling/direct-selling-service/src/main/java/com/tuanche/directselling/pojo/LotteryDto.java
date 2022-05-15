package com.tuanche.directselling.pojo;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/10/15 16:07
 **/
public class LotteryDto {

    private String errorCode;
    private LotteryResultsDto value;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public LotteryResultsDto getValue() {
        return value;
    }

    public void setValue(LotteryResultsDto value) {
        this.value = value;
    }
}
