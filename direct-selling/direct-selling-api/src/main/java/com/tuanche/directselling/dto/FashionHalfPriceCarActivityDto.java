package com.tuanche.directselling.dto;


import java.io.Serializable;

/**
 * @Author lvsen
 * @Description 半价车活动dto
 * @Date 2021/9/17
 **/
public class FashionHalfPriceCarActivityDto implements Serializable {
    /**
     * 场次id
     */
    private Integer periodsId;

    /**
     * 微信UserWxUnionId
     */
    private String userWxUnionId;

    /**
     * 用户openId
     */
    private String userWxOpenId;

    public Integer getPeriodsId() {
        return periodsId;
    }

    public void setPeriodsId(Integer periodsId) {
        this.periodsId = periodsId;
    }

    public String getUserWxUnionId() {
        return userWxUnionId;
    }

    public void setUserWxUnionId(String userWxUnionId) {
        this.userWxUnionId = userWxUnionId;
    }

    public String getUserWxOpenId() {
        return userWxOpenId;
    }

    public void setUserWxOpenId(String userWxOpenId) {
        this.userWxOpenId = userWxOpenId;
    }
}
