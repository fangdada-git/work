package com.tuanche.directselling.model;

import java.io.Serializable;
import java.util.Date;

/**
    * 代理人
    */
public class AfterSaleTeacher implements Serializable {
    private Integer id;

    /**
    * 代理人微信unionid
    */
    private String teacherWxUnionId;

    /**
    * 代理人微信openid
    */
    private String teacherWxOpenId;

    /**
    * 用户昵称
    */
    private String nickName;

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

    public String getTeacherWxUnionId() {
        return teacherWxUnionId;
    }

    public void setTeacherWxUnionId(String teacherWxUnionId) {
        this.teacherWxUnionId = teacherWxUnionId;
    }

    public String getTeacherWxOpenId() {
        return teacherWxOpenId;
    }

    public void setTeacherWxOpenId(String teacherWxOpenId) {
        this.teacherWxOpenId = teacherWxOpenId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

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
}