package com.tuanche.directselling.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: direct-selling
 * @description: ${description}
 * @author: fxq
 * @create: 2020-03-27 13:05
 **/
public class LiveDealerAnchorVo implements Serializable {

    //主播类型  1：销冠主播 2：人气主播 3：活力主播（默认1）
    private String type = "1";
    //主播类型  1：销冠主播 2：人气主播 3：活力主播（默认1）  4:城市  5：城市下直播间数据
    private Integer int_type = 1;

    //百城主播排行榜比例系数
    private Integer ratio;

    //排行时间
    private Date date;

    //主播城市id
    private Integer cityId;

    //主播类型 1. 经销商主播  2. 团车主播
    private Integer anchorType;

    /**
     * 进行中/最近直播场次id
     */
    private Integer periodsId;
    /**
     * 场次开始时间
     */
    private Date beginTime;
    /**
     * 场次结束时间
     */
    private Date endTime;

    public void setType(String type) {
        this.type = type;
    }

    public Integer getInt_type() {
        return int_type;
    }

    public void setInt_type(Integer int_type) {
        this.int_type = int_type;
    }

    public Integer getRatio() {
        return ratio;
    }

    public void setRatio(Integer ratio) {
        this.ratio = ratio;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getType() {
        return type;
    }

    public Integer getAnchorType() {
        return anchorType;
    }

    public void setAnchorType(Integer anchorType) {
        this.anchorType = anchorType;
    }

    public Integer getPeriodsId() {
        return periodsId;
    }

    public void setPeriodsId(Integer periodsId) {
        this.periodsId = periodsId;
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
}