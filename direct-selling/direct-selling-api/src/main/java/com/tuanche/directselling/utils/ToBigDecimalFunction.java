package com.tuanche.directselling.utils;

import java.math.BigDecimal;

/**
 * @class: ToBigDecimalFunction
 * @description:
 * @author: dudg
 * @create: 2020-09-29 15:43
 */
@FunctionalInterface
public interface ToBigDecimalFunction<T>{
    BigDecimal applyAsBigDecimal(T value);
}
