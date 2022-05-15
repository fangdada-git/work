package com.tuanche.directselling.dto;/**
 * @program: direct-selling
 * @description: ${description}
 * @author: lvsen
 * @create: 2021-07-23 15:43
 **/

import com.tuanche.directselling.model.AfterSaleActivityCoupon;

import java.io.Serializable;

/**
 * @Author lvsen
 * @Description
 * @Date 2021/7/23
 **/
public class AfterSaleActivityCouponDto extends AfterSaleActivityCoupon implements Serializable {

    private String getStartTimeStr;

    private String getEndTimeStr;

    public String getGetStartTimeStr() {
        return getStartTimeStr;
    }

    public void setGetStartTimeStr(String getStartTimeStr) {
        this.getStartTimeStr = getStartTimeStr;
    }

    public String getGetEndTimeStr() {
        return getEndTimeStr;
    }

    public void setGetEndTimeStr(String getEndTimeStr) {
        this.getEndTimeStr = getEndTimeStr;
    }
}
