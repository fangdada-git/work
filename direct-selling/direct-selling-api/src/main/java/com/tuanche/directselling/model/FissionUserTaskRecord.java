package com.tuanche.directselling.model;

import java.io.Serializable;
import java.util.Date;

public class FissionUserTaskRecord implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 裂变活动id
     */
    private Integer fissionId;

    /**
     * 销售微信UnionId
     */
    private String saleWxUnionId;

    /**
     * 分享人微信UnionId
     */
    private String shareWxUnionId;

    /**
     * 用户微信UnionId
     */
    private String userWxUnionId;

    /**
     * 任务id
     */
    private Integer taskId;

    /**
     * 分享页面来源1：活动首页 2：海报
     */
    private Byte source;
    /**
     * 数据状态 0：未开始或未开启无积分数据 1：已开启无积分数据 2：已开启有积分数
     */
    private Integer ongoingData;
    /**
     * 渠道
     */
    private Integer channel;

    /**
     * 用户的IP地址
     */
    private String ip;

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
     * fission_user_task_record
     */
    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFissionId() {
        return fissionId;
    }

    public void setFissionId(Integer fissionId) {
        this.fissionId = fissionId;
    }

    public String getSaleWxUnionId() {
        return saleWxUnionId;
    }

    public void setSaleWxUnionId(String saleWxUnionId) {
        this.saleWxUnionId = saleWxUnionId;
    }

    public String getShareWxUnionId() {
        return shareWxUnionId;
    }

    public void setShareWxUnionId(String shareWxUnionId) {
        this.shareWxUnionId = shareWxUnionId;
    }

    public String getUserWxUnionId() {
        return userWxUnionId;
    }

    public void setUserWxUnionId(String userWxUnionId) {
        this.userWxUnionId = userWxUnionId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Byte getSource() {
        return source;
    }

    public void setSource(Byte source) {
        this.source = source;
    }

    public Integer getOngoingData() {
        return ongoingData;
    }

    public void setOngoingData(Integer ongoingData) {
        this.ongoingData = ongoingData;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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
}