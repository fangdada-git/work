package com.tuanche.directselling.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class FissionUser implements Serializable {

    public FissionUser() {}

    /**
     *
     */
    private Integer id;

    /**
     * 裂变活动ID
     */
    private Integer fissionId;

    /**
     * 用户微信openid
     */
    private String userWxOpenId;
    /**
     * 用户微信UnionId
     */
    private String userWxUnionId;

    private List<String> userWxOpenIdList;

    private String activityName;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户微信头像
     */
    private String userWxHead;

    /**
     * 城市id
     */
    private Integer cityId;

    /**
     * 是否删除 0未删除 1 删除
     */
    private Boolean deleteFlag;

    /**
     * 创建时间
     */
    private Date createDt;

    /**
     * 更新时间
     */
    private Date updateDt;

    /**
     * fission_user
     */
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFissionId() {
        return fissionId;
    }

    public void setFissionId(Integer fissionId) {
        this.fissionId = fissionId;
    }

    public String getUserWxOpenId() {
        return userWxOpenId;
    }

    public void setUserWxOpenId(String userWxOpenId) {
        this.userWxOpenId = userWxOpenId;
    }

    public String getUserWxUnionId() {
        return userWxUnionId;
    }

    public void setUserWxUnionId(String userWxUnionId) {
        this.userWxUnionId = userWxUnionId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public String getUserWxHead() {
        return userWxHead;
    }

    public void setUserWxHead(String userWxHead) {
        this.userWxHead = userWxHead;
    }

    public List<String> getUserWxOpenIdList() {
        return userWxOpenIdList;
    }

    public void setUserWxOpenIdList(List<String> userWxOpenIdList) {
        this.userWxOpenIdList = userWxOpenIdList;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}