package com.tuanche.directselling.model;

import com.tuanche.commons.util.StringUtil;

import java.io.Serializable;
import java.util.Date;

public class FashionCarMarkeUser implements Serializable {
    private Integer id;

    //大场次id
    private Integer periodsId;
    //潮车集id
    private Integer carFashionId;

    private Integer userId;

    private String userWxUnionId;

    private String userWxOpenId;

    private String nickName;

    private String userWxHead;

    private String userPhone;

    private Integer cityId;

    private Boolean deleteFlag;

    private Date createDt;

    private Date updateDt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserWxUnionId() {
        return userWxUnionId;
    }

    public void setUserWxUnionId(String userWxUnionId) {
        this.userWxUnionId = StringUtil.isEmpty(userWxUnionId) ? null : userWxUnionId.trim();
    }

    public String getUserWxOpenId() {
        return userWxOpenId;
    }

    public void setUserWxOpenId(String userWxOpenId) {
        this.userWxOpenId = StringUtil.isEmpty(userWxOpenId) ? null : userWxOpenId.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = StringUtil.isEmpty(nickName) ? null : nickName.trim();
    }

    public String getUserWxHead() {
        return userWxHead;
    }

    public void setUserWxHead(String userWxHead) {
        this.userWxHead = StringUtil.isEmpty(userWxHead) ? null : userWxHead.trim();
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
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

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}