package com.tuanche.directselling.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.tuanche.directselling.utils.BigDecimalFormat.Style;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Objects;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/8/25 10:50
 **/
public class BigDecimalSerialize extends JsonSerializer<BigDecimal> implements
        ContextualSerializer {

    private String pattern;
    private Style style;

    public BigDecimalSerialize() {
    }

    public BigDecimalSerialize(final String pattern, final Style style) {
        this.pattern = pattern;
        this.style = style;
    }

    @Override
    public JsonSerializer<?> createContextual(final SerializerProvider serializerProvider,
                                              final BeanProperty beanProperty) throws JsonMappingException {
        // 为空直接跳过
        if (beanProperty != null) {
            // 非 String 类直接跳过
            if (Objects.equals(beanProperty.getType().getRawClass(), BigDecimal.class)) {
                BigDecimalFormat bigDecimalFormat = beanProperty.getAnnotation(BigDecimalFormat.class);
                if (bigDecimalFormat == null) {
                    bigDecimalFormat = beanProperty.getContextAnnotation(BigDecimalFormat.class);
                }
                // 如果能得到注解，就将注解的 value 传入 SensitiveInfoSerialize
                if (bigDecimalFormat != null) {
                    return new BigDecimalSerialize(bigDecimalFormat.pattern(), bigDecimalFormat.style());
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(beanProperty);
    }

    @Override
    public void serialize(BigDecimal bigDecimal, final JsonGenerator jsonGenerator,
                          final SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if (style == Style.PERCENT) {
            bigDecimal = bigDecimal.multiply(new BigDecimal("100"));
        }
        DecimalFormat df = new DecimalFormat(pattern);
        jsonGenerator.writeString(df.format(bigDecimal));
    }
}
