package com.tuanche.directselling.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: FissionActivitySale
 * @Description: 裂变活动和销售信息
 * @Author: ZhangYiXin
 * @Date: 2020/9/23 18:03
 * @Version 1.0
 **/
public class FissionActivitySaleVo implements Serializable {
    /**
     * 裂变活动ID
     */
    private Integer id;

    /**
     * 活动名称
     */
    private String activityName;
    /**
     * 销售ID
     */
    @JsonIgnore
    private Integer saleId;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 是否参与活动
     */
    private boolean isJoin;

    /**
     * 头图
     */
    private String headPicUrl;

    /**
     * 0：未开始 1：进行中 2：已结束
     */
    private Integer activityStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Date getStartTime() {
        return startTime;
    }


    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }


    public Date getEndTime() {
        return endTime;
    }


    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public boolean getJoin() {
        return saleId != null;
    }

    public void setJoin(boolean join) {
        isJoin = join;
    }

    public Integer getSaleId() {
        return saleId;
    }

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }

    public boolean isJoin() {
        return isJoin;
    }

    public String getHeadPicUrl() {
        return headPicUrl;
    }

    public void setHeadPicUrl(String headPicUrl) {
        this.headPicUrl = headPicUrl;
    }

    public Integer getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(Integer activityStatus) {
        this.activityStatus = activityStatus;
    }
}
