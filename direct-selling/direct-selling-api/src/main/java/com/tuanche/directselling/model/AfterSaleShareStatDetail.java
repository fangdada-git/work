package com.tuanche.directselling.model;

import java.util.Date;

public class AfterSaleShareStatDetail {
    private Integer id;

    /**
    * 活动id
    */
    private Integer activityId;

    /**
    * 数据时间
    */
    private Date dateTime;

    /**
    * 分享次数
    */
    private Integer shareCount;

    /**
    * 浏览人数
    */
    private Integer uniqueVisitCount;

    /**
    * 售卡数
    */
    private Integer saleCardCount;

    /**
    * 创建时间
    */
    private Date createDt;

    /**
    * 修改时间
    */
    private Date updateDt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public Integer getUniqueVisitCount() {
        return uniqueVisitCount;
    }

    public void setUniqueVisitCount(Integer uniqueVisitCount) {
        this.uniqueVisitCount = uniqueVisitCount;
    }

    public Integer getSaleCardCount() {
        return saleCardCount;
    }

    public void setSaleCardCount(Integer saleCardCount) {
        this.saleCardCount = saleCardCount;
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