package com.tuanche.directselling.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @Author lvsen
 * @Description 半价车活动vo
 * @Date 2021/9/17
 **/
public class FashionHalfPriceCarActivityVo implements Serializable {

    /**
     * 倒计时（秒）
     */
    private long countdowns;

    /**
     * 活动开始时间
     */
    private String formalDateStartStr;

    private long formalDateStart;
    /**
     * 活动结束时间
     */
    private String formalDateEndStr;

    private long formalDateEnd;

    /**
     * 参加活动品牌列表
     */
    private List<FashionHalfPriceBrandVo> halfPriceBrandVoList;

    /**
     * 是否抽过中奖码
     */
    private boolean winNumFlag;
    /**
     * 二维码
     */
    private String qwUrl;
    /**
     * 活动开始标识（预热就算开始 1未开始，2进行中，3已结束）
     */
    private Integer activityStatus;

    public long getCountdowns() {
        return countdowns;
    }

    public void setCountdowns(long countdowns) {
        this.countdowns = countdowns;
    }

    public String getFormalDateStartStr() {
        return formalDateStartStr;
    }

    public void setFormalDateStartStr(String formalDateStartStr) {
        this.formalDateStartStr = formalDateStartStr;
    }

    public String getFormalDateEndStr() {
        return formalDateEndStr;
    }

    public void setFormalDateEndStr(String formalDateEndStr) {
        this.formalDateEndStr = formalDateEndStr;
    }

    public List<FashionHalfPriceBrandVo> getHalfPriceBrandVoList() {
        return halfPriceBrandVoList;
    }

    public void setHalfPriceBrandVoList(List<FashionHalfPriceBrandVo> halfPriceBrandVoList) {
        this.halfPriceBrandVoList = halfPriceBrandVoList;
    }

    public long getFormalDateStart() {
        return formalDateStart;
    }

    public void setFormalDateStart(long formalDateStart) {
        this.formalDateStart = formalDateStart;
    }

    public long getFormalDateEnd() {
        return formalDateEnd;
    }

    public void setFormalDateEnd(long formalDateEnd) {
        this.formalDateEnd = formalDateEnd;
    }

    public boolean isWinNumFlag() {
        return winNumFlag;
    }

    public void setWinNumFlag(boolean winNumFlag) {
        this.winNumFlag = winNumFlag;
    }

    public String getQwUrl() {
        return qwUrl;
    }

    public void setQwUrl(String qwUrl) {
        this.qwUrl = qwUrl;
    }

    public Integer getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(Integer activityStatus) {
        this.activityStatus = activityStatus;
    }

}
