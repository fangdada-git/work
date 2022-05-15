package com.tuanche.directselling.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class LiveEffect implements Serializable {

    private static final long serialVersionUID = -3892840667807398199L;

    private Integer id;

    //直播间名称
    private String feedTitle;

    //导购姓名
    private String guideName;

    //导购淘宝昵称
    private String guideTbNick;

    //观看时长 - 观看
    private BigDecimal readTimeAll;

    //观看总PV - 观看
    private Integer pv;

    //互动PV - 互动
    private Integer pvHuDong;

    //评论PV - 互动
    private Integer pvPing;

    //点赞PV - 互动
    private Integer pvZan;

    //分享PV - 互动
    private Integer pvShare;

    //关注PV - 互动
    private Integer pvGuanzhu;

    //直接引导PV - 转化
    private Integer ipv;

    //直接引导成交金额 - 转化
    private BigDecimal alipayAmt;

    //直接引导成交笔数 - 转化
    private Integer alipayCnt;

    //直播日期
    private Date startTime;

    private Date createDt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFeedTitle() {
        return feedTitle;
    }

    public void setFeedTitle(String feedTitle) {
        this.feedTitle = feedTitle == null ? null : feedTitle.trim();
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

    public BigDecimal getReadTimeAll() {
        return readTimeAll;
    }

    public void setReadTimeAll(BigDecimal readTimeAll) {
        this.readTimeAll = readTimeAll;
    }

    public Integer getPv() {
        return pv;
    }

    public void setPv(Integer pv) {
        this.pv = pv;
    }

    public Integer getPvHuDong() {
        return pvHuDong;
    }

    public void setPvHuDong(Integer pvHuDong) {
        this.pvHuDong = pvHuDong;
    }

    public Integer getPvPing() {
        return pvPing;
    }

    public void setPvPing(Integer pvPing) {
        this.pvPing = pvPing;
    }

    public Integer getPvZan() {
        return pvZan;
    }

    public void setPvZan(Integer pvZan) {
        this.pvZan = pvZan;
    }

    public Integer getPvShare() {
        return pvShare;
    }

    public void setPvShare(Integer pvShare) {
        this.pvShare = pvShare;
    }

    public Integer getPvGuanzhu() {
        return pvGuanzhu;
    }

    public void setPvGuanzhu(Integer pvGuanzhu) {
        this.pvGuanzhu = pvGuanzhu;
    }

    public Integer getIpv() {
        return ipv;
    }

    public void setIpv(Integer ipv) {
        this.ipv = ipv;
    }

    public BigDecimal getAlipayAmt() {
        return alipayAmt;
    }

    public void setAlipayAmt(BigDecimal alipayAmt) {
        this.alipayAmt = alipayAmt;
    }

    public Integer getAlipayCnt() {
        return alipayCnt;
    }

    public void setAlipayCnt(Integer alipayCnt) {
        this.alipayCnt = alipayCnt;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }
}