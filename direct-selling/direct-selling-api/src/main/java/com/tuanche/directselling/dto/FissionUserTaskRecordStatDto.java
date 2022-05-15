package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.FissionUserTaskRecord;

import java.io.Serializable;
import java.util.Date;

public class FissionUserTaskRecordStatDto extends FissionUserTaskRecord implements Serializable {
    private Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}