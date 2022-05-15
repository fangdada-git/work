package com.tuanche.directselling.model;

import java.io.Serializable;
import java.util.Date;

public class FissionUserEffectiveTask implements Serializable {
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
     * 分享人是否是销售 0：否 1：是
     */
    private Boolean sharerIsSale;
    /**
     * 任务id
     */
    private Integer taskId;

    /**
     * 任务记录表id
     */
    private Long taskRecordId;

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
     * fission_user_effective_task
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

    public Boolean getSharerIsSale() {
        return sharerIsSale;
    }

    public void setSharerIsSale(Boolean sharerIsSale) {
        this.sharerIsSale = sharerIsSale;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Long getTaskRecordId() {
        return taskRecordId;
    }

    public void setTaskRecordId(Long taskRecordId) {
        this.taskRecordId = taskRecordId;
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