package com.tuanche.directselling.dto;

import java.io.Serializable;

/**
 * mybatis返回map时的通用DTO
 * @CreatTime 2021-08-23 10:48
 */
public class ResultMapDto implements Serializable {

    String mapKey;
    String mapValue;


    public String getMapKey() {
        return mapKey;
    }

    public void setMapKey(String mapKey) {
        this.mapKey = mapKey;
    }

    public String getMapValue() {
        return mapValue;
    }

    public void setMapValue(String mapValue) {
        this.mapValue = mapValue;
    }
}
