package com.tuanche.directselling.dto;

import java.io.Serializable;

/**
 * 报警信息
 */
public class AlarmInfo implements Serializable {

    private String alarmInfoPhone;

    private String alarmInfoEmail;

    public String getAlarmInfoPhone() {
        return alarmInfoPhone;
    }

    public void setAlarmInfoPhone(String alarmInfoPhone) {
        this.alarmInfoPhone = alarmInfoPhone;
    }

    public String getAlarmInfoEmail() {
        return alarmInfoEmail;
    }

    public void setAlarmInfoEmail(String alarmInfoEmail) {
        this.alarmInfoEmail = alarmInfoEmail;
    }
}