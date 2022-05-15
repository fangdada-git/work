package com.tuanche.directselling.vo;

import com.tuanche.arch.util.utils.DateUtils;
import com.tuanche.directselling.model.LiveDealerBroadcast;

import java.io.Serializable;
import java.util.List;

/**
 * @class: LiveParameterVo
 * @description: 会场直播参数VO实体
 * @author: dudg
 * @create: 2020-04-30 18:25
 */
public class LiveParameterVo extends LiveDealerBroadcast implements Serializable {

    /**
     * 场次开始时间戳 毫秒
     */
    private Long startTimeStamp;
    /**
     * 场次结束时间戳 毫秒
     */
    private Long endTimeStamp;
    /**
     * 场次预告时间戳 毫秒
     */
    private Long preheatTimeStamp;

    /**
     * 场次开始时间
     */
    private String periodsStartTime;
    /**
     * 场次结束时间
     */
    private String periodsEndTime;

    private String periodsPreheatTime;

    /**
     * 场次id
     */
    private Integer periodsId;

    /**
     * 城市id
     */
    private Integer cityId;

    /**
     * 主播ids
     */
    private List<Long> anchorIds;

    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;

    /**
     * 过滤直播间id
     */
    private Long excludeLiveId;

    public Long getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(Long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public Long getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(Long endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public Long getPreheatTimeStamp() {
        return preheatTimeStamp;
    }

    public void setPreheatTimeStamp(Long preheatTimeStamp) {
        this.preheatTimeStamp = preheatTimeStamp;
    }

    public String getPeriodsStartTime() {
        if (startTimeStamp != null) {
            return DateUtils.dateToString(DateUtils.LongToDate(startTimeStamp),DateUtils.YYYY_MM_DD_HH_MM_SS);
        }
        return periodsStartTime;
    }

    public void setPeriodsStartTime(String periodsStartTime) {
        this.periodsStartTime = periodsStartTime;
    }

    public String getPeriodsEndTime() {
        if (endTimeStamp != null) {
            return DateUtils.dateToString(DateUtils.LongToDate(endTimeStamp),DateUtils.YYYY_MM_DD_HH_MM_SS);
        }
        return periodsEndTime;
    }

    public void setPeriodsEndTime(String periodsEndTime) {
        this.periodsEndTime = periodsEndTime;
    }

    public Integer getPeriodsId() {
        return periodsId;
    }

    public void setPeriodsId(Integer periodsId) {
        this.periodsId = periodsId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public List<Long> getAnchorIds() {
        return anchorIds;
    }

    public void setAnchorIds(List<Long> anchorIds) {
        this.anchorIds = anchorIds;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getPeriodsPreheatTime() {
        if (preheatTimeStamp != null) {
            return DateUtils.dateToString(DateUtils.LongToDate(preheatTimeStamp),DateUtils.YYYY_MM_DD_HH_MM_SS);
        }
        return periodsPreheatTime;
    }

    public void setPeriodsPreheatTime(String periodsPreheatTime) {
        this.periodsPreheatTime = periodsPreheatTime;
    }

    public Long getExcludeLiveId() {
        return excludeLiveId;
    }

    public void setExcludeLiveId(Long excludeLiveId) {
        this.excludeLiveId = excludeLiveId;
    }
}
