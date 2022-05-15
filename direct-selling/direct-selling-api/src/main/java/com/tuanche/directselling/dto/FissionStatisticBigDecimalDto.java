package com.tuanche.directselling.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class FissionStatisticBigDecimalDto extends FissionStatisticDto implements Serializable {

    private BigDecimal value;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
