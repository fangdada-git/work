package com.tuanche.directselling.model;

import java.io.Serializable;
import java.util.Date;

/**
 * live_scene
 * @author 
 */
public class LiveScene implements Serializable {
    private Integer id;

    /**
     * 关联场次ID
     */
    private Integer periodsId;

    /**
     * 场次活动主题
     */
    private String sceneTitle;

    /**
     * 场次活动城市ID
     */
    private Integer cityId;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 上架状态（0:下架  1:上架）
     */
    private Integer upState;

    /**
     * 主机位码
     */
    private String hostCode;

    /**
     * 直播计划ID
     */
    private String planId;

    /**
     * 预览地址
     */
    private String previewAddress;

    /**
     * 直播地址
     */
    private String liveAddress;

    /**
     * 导播ID
     */
    private Integer directorId;

    /**
     * 导播名称
     */
    private String directorName;

    /**
     * 添加人ID
     */
    private Integer createUserId;

    /**
     * 添加人姓名
     */
    private String createUserName;

    /**
     * 添加时间
     */
    private Date createDt;

    /**
     * 编辑人ID
     */
    private Integer updateUserId;

    /**
     * 编辑人姓名
     */
    private String updateUserName;

    /**
     * 编辑时间
     */
    private Date updateDt;

    /**
     * 删除状态（1:是  0:否）
     */
    private Boolean deleteState;

    /**
     * 预热开始时间
     */
    private Date readyBeginTime;

    /**
     * 预热结束时间
     */
    private Date readyEndTime;

    /**
     * 正式开始时间
     */
    private Date formalBeginTime;

    /**
     * 正式结束时间
     */
    private Date formalEndTime;

    /**
     * 是否发送服务通知（0 不发送，1发送）
     */
    private Integer sendFlag;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSceneTitle() {
        return sceneTitle;
    }

    public void setSceneTitle(String sceneTitle) {
        this.sceneTitle = sceneTitle;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getUpState() {
        return upState;
    }

    public void setUpState(Integer upState) {
        this.upState = upState;
    }

    public String getHostCode() {
        return hostCode;
    }

    public void setHostCode(String hostCode) {
        this.hostCode = hostCode;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getPreviewAddress() {
        return previewAddress;
    }

    public void setPreviewAddress(String previewAddress) {
        this.previewAddress = previewAddress;
    }

    public String getLiveAddress() {
        return liveAddress;
    }

    public void setLiveAddress(String liveAddress) {
        this.liveAddress = liveAddress;
    }

    public Integer getDirectorId() {
        return directorId;
    }

    public void setDirectorId(Integer directorId) {
        this.directorId = directorId;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public Integer getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public Boolean getDeleteState() {
        return deleteState;
    }

    public void setDeleteState(Boolean deleteState) {
        this.deleteState = deleteState;
    }

    public Date getReadyBeginTime() {
        return readyBeginTime;
    }

    public void setReadyBeginTime(Date readyBeginTime) {
        this.readyBeginTime = readyBeginTime;
    }

    public Date getReadyEndTime() {
        return readyEndTime;
    }

    public void setReadyEndTime(Date readyEndTime) {
        this.readyEndTime = readyEndTime;
    }

    public Date getFormalBeginTime() {
        return formalBeginTime;
    }

    public void setFormalBeginTime(Date formalBeginTime) {
        this.formalBeginTime = formalBeginTime;
    }

    public Date getFormalEndTime() {
        return formalEndTime;
    }

    public void setFormalEndTime(Date formalEndTime) {
        this.formalEndTime = formalEndTime;
    }

    public Integer getPeriodsId() {
        return periodsId;
    }

    public void setPeriodsId(Integer periodsId) {
        this.periodsId = periodsId;
    }

    public Integer getSendFlag() {
        return sendFlag;
    }

    public void setSendFlag(Integer sendFlag) {
        this.sendFlag = sendFlag;
    }
}