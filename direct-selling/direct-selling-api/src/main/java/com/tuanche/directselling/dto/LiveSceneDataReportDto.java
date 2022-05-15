package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.LiveScene;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName: LiveSceneDataReportDto
 * @Description: 团车直卖-数据报表Dto
 * @Author: lvsen
 * @Date: 2020/3/5 9:41
 * @Version 1.0
 **/
public class LiveSceneDataReportDto implements Serializable {

    /**
     * 场次id
     */
    private Integer periodsId;
    /**
     * 场次活动城市ID
     */
    private Integer cityId;
    /**
     * 城市名称
     **/
    private String cityName;

    /**
     * 经销商ID
     */
    private Integer dealerId;
    /**
     * 经销商名称
     */
    private String dealerName;

    /**
     * 品牌名称
     */
    private String brandNames;

    /**
     * 保量目标
     */
    private int ensureSize;

    /**
     * 当前线索量
     */
    private int allClueSize;

    /**
     * 达成率
     */
    private BigDecimal reachRatio;

    /**
     * 待发送线索量
     */
    private int noSendClueSize;

    /**
     * 已发送线索量
     */
    private int sendClueSize;

    /**
     * 车型商品线索量
     */
    private int couponClueSize;

    /**
     * 非车型商品线索量
     */
    private int noCouponClueSize;

    public Integer getPeriodsId() {
        return periodsId;
    }

    public void setPeriodsId(Integer periodsId) {
        this.periodsId = periodsId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
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

    public String getBrandNames() {
        return brandNames;
    }

    public void setBrandNames(String brandNames) {
        this.brandNames = brandNames;
    }

    public int getEnsureSize() {
        return ensureSize;
    }

    public void setEnsureSize(int ensureSize) {
        this.ensureSize = ensureSize;
    }

    public int getAllClueSize() {
        return allClueSize;
    }

    public void setAllClueSize(int allClueSize) {
        this.allClueSize = allClueSize;
    }

    public BigDecimal getReachRatio() {
        return reachRatio;
    }

    public void setReachRatio(BigDecimal reachRatio) {
        this.reachRatio = reachRatio;
    }

    public int getNoSendClueSize() {
        return noSendClueSize;
    }

    public void setNoSendClueSize(int noSendClueSize) {
        this.noSendClueSize = noSendClueSize;
    }

    public int getSendClueSize() {
        return sendClueSize;
    }

    public void setSendClueSize(int sendClueSize) {
        this.sendClueSize = sendClueSize;
    }

    public int getCouponClueSize() {
        return couponClueSize;
    }

    public void setCouponClueSize(int couponClueSize) {
        this.couponClueSize = couponClueSize;
    }

    public int getNoCouponClueSize() {
        return noCouponClueSize;
    }

    public void setNoCouponClueSize(int noCouponClueSize) {
        this.noCouponClueSize = noCouponClueSize;
    }
}
