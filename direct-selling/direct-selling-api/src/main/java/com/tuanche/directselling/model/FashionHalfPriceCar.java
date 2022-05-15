package com.tuanche.directselling.model;

import java.io.Serializable;
import java.util.Date;

/**
 *  半价车
 */
public class FashionHalfPriceCar implements Serializable {
    private Integer id;
    /**
     * 场次ID
     */
    private Integer periodsId;
    /**
     * 潮车集活动id
     */
    private Integer carFashionId;
    /**
     * 活动开始时间
     */
    private Date activityStart;
    /**
     * 活动结束时间
     */
    private Date activityEnd;
    /**
     * 活动日期
     */
    private Date activityDate;
    /**
     * 品牌id
     */
    private Integer brandId;
    /**
     * 体彩开奖号设置
     */
    private Integer winningNumber;
    /**
     * string类型中奖码
     */
    private String winningNumberStr;

    private Integer creatBy;

    private Date creatDt;

    private Integer updateBy;

    private Date updateDt;

    private Integer deleteFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCarFashionId() {
        return carFashionId;
    }

    public void setCarFashionId(Integer carFashionId) {
        this.carFashionId = carFashionId;
    }

    public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getWinningNumber() {
        return winningNumber;
    }

    public void setWinningNumber(Integer winningNumber) {
        this.winningNumber = winningNumber;
    }

    public Integer getCreatBy() {
        return creatBy;
    }

    public void setCreatBy(Integer creatBy) {
        this.creatBy = creatBy;
    }

    public Date getCreatDt() {
        return creatDt;
    }

    public void setCreatDt(Date creatDt) {
        this.creatDt = creatDt;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getPeriodsId() {
        return periodsId;
    }

    public void setPeriodsId(Integer periodsId) {
        this.periodsId = periodsId;
    }

    public Date getActivityStart() {
        return activityStart;
    }

    public void setActivityStart(Date activityStart) {
        this.activityStart = activityStart;
    }

    public Date getActivityEnd() {
        return activityEnd;
    }

    public void setActivityEnd(Date activityEnd) {
        this.activityEnd = activityEnd;
    }

    public String getWinningNumberStr() {
        return winningNumberStr;
    }

    public void setWinningNumberStr(String winningNumberStr) {
        this.winningNumberStr = winningNumberStr;
    }
}