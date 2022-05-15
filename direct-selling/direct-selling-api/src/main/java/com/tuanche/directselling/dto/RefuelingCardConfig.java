package com.tuanche.directselling.dto;

import java.io.Serializable;
import java.util.Date;

public class RefuelingCardConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    //活动有效期，单位-天（大场次结束后的X天内）
    private Integer termOfValidity;

    public Integer getTermOfValidity() {
        return termOfValidity;
    }

    public void setTermOfValidity(Integer termOfValidity) {
        this.termOfValidity = termOfValidity;
    }
}