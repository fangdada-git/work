package com.tuanche.directselling.dto;


import java.io.Serializable;

public class AfterSaleUserBrowseStatPeriodDto extends AfterSaleUserBrowseStatDto implements Serializable {
    private String dateTime;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}