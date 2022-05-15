package com.tuanche.directselling.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class FissionStatisticIntDto extends FissionStatisticDto implements Serializable {


    private Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
