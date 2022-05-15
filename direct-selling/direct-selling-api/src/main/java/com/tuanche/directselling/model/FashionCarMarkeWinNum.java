package com.tuanche.directselling.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuanche.directselling.utils.GlobalConstants;

import java.io.Serializable;
import java.util.Date;

/**
 * 潮车集助力信息
 */
public class FashionCarMarkeWinNum implements Serializable {
    private Integer id;

    /**
     * 场次ID
     */
    private Integer periodsId;

    /**
     * 活动ID
     */
    private Integer carFashionId;

    /**
     * 品牌ID
     */
    private Integer brandId;

    /**
     * 被助力用户id、中奖码所属人
     */
    private Integer userId;

    /**
     * 0:被助力人 1:助力人
     */
    private Integer userType;
    /**
     * 活动日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date activityDate;

    /**
     * fashion_car_marke_helper_user表id,为null则表示是转盘抽中的中奖码
     */
    private Integer helpUserId;

    /**
     * 中奖码
     */
    private Integer winNum;
    private String winNumStr="";

    /**
     * 是否中奖 0否 1是
     */
    private Byte isWin;

    /**
     * 创建时间
     */
    private Date createDt;

    /**
     * 修改时间
     */
    private Date updateDt;

    /**
     * 是否删除  1：是  0：否
     */
    private Boolean deleteFlag;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getHelpUserId() {
        return helpUserId;
    }

    public void setHelpUserId(Integer helpUserId) {
        this.helpUserId = helpUserId;
    }

    public Integer getWinNum() {
        return winNum;
    }

    public void setWinNum(Integer winNum) {
        this.winNum = winNum;
    }

    public Byte getIsWin() {
        return isWin;
    }

    public void setIsWin(Byte isWin) {
        this.isWin = isWin;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
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

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getPeriodsId() {
        return periodsId;
    }

    public void setPeriodsId(Integer periodsId) {
        this.periodsId = periodsId;
    }

    public String getWinNumStr() {
        return GlobalConstants.StringFormat(8, this.winNum);
    }

    public void setWinNumStr(String winNumStr) {
        this.winNumStr = winNumStr;
    }
}