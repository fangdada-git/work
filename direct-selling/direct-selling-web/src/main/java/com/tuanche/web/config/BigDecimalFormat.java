package com.tuanche.web.config;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author zhangyixin
 */
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = BigDecimalSerialize.class)
public @interface BigDecimalFormat {
    String pattern() default "#.00";

    Style style() default Style.DEFAULT;

    enum Style {
        /**
         * 默认
         */
        DEFAULT,
        /**
         * 数字
         */
        NUMBER,
        /**
         * 百分数
         */
        PERCENT,
        /**
         * 货币
         */
        CURRENCY
    }
}
