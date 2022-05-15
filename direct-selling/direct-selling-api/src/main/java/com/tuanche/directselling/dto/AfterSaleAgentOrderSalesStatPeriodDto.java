package com.tuanche.directselling.dto;

import java.io.Serializable;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/7/21 14:20
 **/
public class AfterSaleAgentOrderSalesStatPeriodDto extends AfterSaleAgentOrderSalesStatDto implements Serializable {
    private String dateTime;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
