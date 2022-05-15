package com.tuanche.directselling.dto;

import java.io.Serializable;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/7/20 20:58
 **/
public class AfterSaleUserShareStatPeriodDto extends AfterSaleUserShareStatDto implements Serializable {

    private String dateTime;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
