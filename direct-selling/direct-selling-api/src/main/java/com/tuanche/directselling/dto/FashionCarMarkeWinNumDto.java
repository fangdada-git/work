package com.tuanche.directselling.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author lvsen
 * @Description 中奖用户
 * @Date 2021/9/16
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FashionCarMarkeWinNumDto implements Serializable {

    private Integer periodsId;

    private Integer carFashionId;

    private Integer id;
    /**
     * 中奖码
     */
    private Integer winNum;
    /**
     * 活动日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date activityDate;
    /**
     * 中奖码所属人
     */
    private Integer userId;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户手机号
     */
    private String userPhone;
    /**
     * 品牌ID
     */
    private Integer brandId;
    /**
     * 品牌名称
     */
    private String brandName;

    public Integer getPeriodsId() {
        return periodsId;
    }

    public void setPeriodsId(Integer periodsId) {
        this.periodsId = periodsId;
    }

    public Integer getCarFashionId() {
        return carFashionId;
    }

    public void setCarFashionId(Integer carFashionId) {
        this.carFashionId = carFashionId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWinNum() {
        return winNum;
    }

    public void setWinNum(Integer winNum) {
        this.winNum = winNum;
    }

    public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
