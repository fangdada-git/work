package com.tuanche.directselling.exception;

public class WinningNumberException extends Exception {
    private String code;

    public WinningNumberException(String code, String message) {
        super(message);
        setCode(code);
    }

    public WinningNumberException(int code, String message) {
        super(message);
        setCode(String.valueOf(code));
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
