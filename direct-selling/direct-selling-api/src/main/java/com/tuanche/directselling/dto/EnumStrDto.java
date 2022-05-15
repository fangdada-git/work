package com.tuanche.directselling.dto;

import java.io.Serializable;

/**
 * String类型的枚举Dto
 * @author：HuangHao
 * @CreatTime 2020-05-27 17:29
 */
public class EnumStrDto implements Serializable {
    private static final long serialVersionUID = 1L;

    //字典枚举的值
    private String value;
    //字典枚举文本
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
