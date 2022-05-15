package com.tuanche.directselling.vo;

import java.io.Serializable;

/**
 * @Author gongbo
 * @Description 团车直卖-场次油卡配置vo
 * @Date 2020年5月28日13:48:27
 **/
public class LiveGiftConfigVo implements Serializable {

    /**
     * 主键ID
     **/
    private Integer id;

    /**
     * 场次ID
     **/
    private Integer periodsId;

    /**
     * 场次名称
     **/
    private String periodsName;

    /**
     * 开始时间
     **/
    private String beginTime;

    /**
     * 结束时间
     **/
    private String endTime;

    /**
     * 适用商品集合
     **/
    private String commodityList;

    /**
     * 规则集合
     **/
    private String ruleMapList;

    /**
     * 经销商名称
     **/
    private String companyName;

    /**
     * 城市ID
     **/
    private Integer cityId;

    /**
     * home page
     */
    private int page;

    /**
     * size of each page
     */
    private int limit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPeriodsId() {
        return periodsId;
    }

    public void setPeriodsId(Integer periodsId) {
        this.periodsId = periodsId;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCommodityList() {
        return commodityList;
    }

    public void setCommodityList(String commodityList) {
        this.commodityList = commodityList;
    }

    public String getRuleMapList() {
        return ruleMapList;
    }

    public void setRuleMapList(String ruleMapList) {
        this.ruleMapList = ruleMapList;
    }

    public String getPeriodsName() {
        return periodsName;
    }

    public void setPeriodsName(String periodsName) {
        this.periodsName = periodsName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
