package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.AfterSaleAgentStat;

import java.io.Serializable;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/7/21 21:12
 **/
public class AfterSaleAgentStatDetailDto extends AfterSaleAgentStat implements Serializable {

    /**
     * 微信头像
     */
    private String wxHead;

    /**
     * 姓名
     */
    private String name;

    /**
     * 电话
     */
    private String phone;

    /**
     * 职位
     */
    private String position;

    /**
     * 时间
     */
    private String dateTime;

    /**
     * 转化率
     */
    private String conversionRate;


    public String getWxHead() {
        return wxHead;
    }

    public void setWxHead(String wxHead) {
        this.wxHead = wxHead;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(String conversionRate) {
        this.conversionRate = conversionRate;
    }
}
