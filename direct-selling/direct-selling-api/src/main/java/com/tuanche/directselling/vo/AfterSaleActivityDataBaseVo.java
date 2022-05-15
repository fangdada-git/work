package com.tuanche.directselling.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 活动数据、活动数据对账基础dto
 *
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/8/18 10:59
 **/
public class AfterSaleActivityDataBaseVo implements Serializable {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 活动ID
     */
    private Integer activityId;
    /**
     * 经销商id
     */
    private Integer dealerId;

    /**
     * 经销商名称
     */
    private String dealerName;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 售卖开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date saleStartTime;
    /**
     * 售卖结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date saleEndTime;
    /**
     * 线下兑换开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date offlineConvertStartTime;

    /**
     * 线下兑换结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date offlineConvertEndTime;
    /**
     * 开启状态
     */
    private Short onState;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Date getSaleStartTime() {
        return saleStartTime;
    }

    public void setSaleStartTime(Date saleStartTime) {
        this.saleStartTime = saleStartTime;
    }

    public Date getSaleEndTime() {
        return saleEndTime;
    }

    public void setSaleEndTime(Date saleEndTime) {
        this.saleEndTime = saleEndTime;
    }

    public Short getOnState() {
        return onState;
    }

    public void setOnState(Short onState) {
        this.onState = onState;
    }

    public Date getStartTime() {
        if (startTime == null) {
            return null;
        }
        try {
            return sdf.parse(startTime);
        } catch (ParseException e) {
            return null;
        }
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        if (endTime == null) {
            return null;
        }
        try {
            return sdf.parse(endTime);
        } catch (ParseException e) {
            return null;
        }
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Date getOfflineConvertStartTime() {
        return offlineConvertStartTime;
    }

    public void setOfflineConvertStartTime(Date offlineConvertStartTime) {
        this.offlineConvertStartTime = offlineConvertStartTime;
    }

    public Date getOfflineConvertEndTime() {
        return offlineConvertEndTime;
    }

    public void setOfflineConvertEndTime(Date offlineConvertEndTime) {
        this.offlineConvertEndTime = offlineConvertEndTime;
    }
}
