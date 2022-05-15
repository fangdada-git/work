package com.tuanche.directselling.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class LiveGuideEffect implements Serializable {

    private static final long serialVersionUID = -207467469982406256L;

    private Integer id;

    //导购姓名
    private String guideName;

    //导购淘宝昵称
    private String guideTbNick;

    //截止今日粉丝数
    private Integer totalFans;

    //直播场次活动
    private Integer feedCount;

    //总直播时长
    private BigDecimal totalTimeLenth;

    //直接引导成交金额
    private BigDecimal alipayAmt;


    private Date createDt;

    private Date updateDt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGuideName() {
        return guideName;
    }

    public void setGuideName(String guideName) {
        this.guideName = guideName == null ? null : guideName.trim();
    }

    public String getGuideTbNick() {
        return guideTbNick;
    }

    public void setGuideTbNick(String guideTbNick) {
        this.guideTbNick = guideTbNick == null ? null : guideTbNick.trim();
    }

    public Integer getTotalFans() {
        return totalFans;
    }

    public void setTotalFans(Integer totalFans) {
        this.totalFans = totalFans;
    }

    public Integer getFeedCount() {
        return feedCount;
    }

    public void setFeedCount(Integer feedCount) {
        this.feedCount = feedCount;
    }

    public BigDecimal getTotalTimeLenth() {
        return totalTimeLenth;
    }

    public void setTotalTimeLenth(BigDecimal totalTimeLenth) {
        this.totalTimeLenth = totalTimeLenth;
    }

    public BigDecimal getAlipayAmt() {
        return alipayAmt;
    }

    public void setAlipayAmt(BigDecimal alipayAmt) {
        this.alipayAmt = alipayAmt;
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