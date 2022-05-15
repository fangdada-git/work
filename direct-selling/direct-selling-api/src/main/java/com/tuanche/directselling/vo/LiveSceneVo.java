package com.tuanche.directselling.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @Author gongbo
 * @Description 团车直卖-场次活动vo
 * @Date 2020年3月4日17:11:37
 **/
public class LiveSceneVo implements Serializable {

    /**
     * 场次ID
     **/
    private Integer periodsId;

    /**
     * 场次ID集合
     **/
    private List<Integer> periodsIdList;

    /**
     * 场次标题
     **/
    private String periodsName;

    /**
     * 场次活动ID
     **/
    private Integer sceneId;

    /**
     * 场次活动主题
     **/
    private String sceneTitle;

    /**
     * 城市ID
     **/
    private Integer cityId;

    /**
     * 参展城市ID
     **/
    private Integer joinCityId;

    /**
     * 开始时间
     **/
    private String beginTime;

    /**
     * 结束时间
     **/
    private String endTime;

    /**
     * 排除场次活动ID
     **/
    private Integer excludeId;

    /**
     * 导播ID
     **/
    private Integer directorId;

    /**
     * 导播姓名
     **/
    private String directorName;

    /**
     * 场次活动状态
     **/
    private Integer sceneState;

    /**
     * 上下架状态
     **/
    private Integer upState;

    /**
     * 场次活动ID集合
     **/
    private List<Integer> sceneIdList;

    /**
     * 品牌ID集合
     **/
    private List<Integer> brandIdList;

    /**
     * 经销商ID集合
     **/
    private List<Integer> dealerIdList;

    /**
     * 城市ID集合
     **/
    private List<Integer> cityIdList;

    /**
     * 城市ids
     **/
    private String cityIds;

    /**
     * 城市名称
     **/
    private String[] cityNames;

    /**
     * 品牌ids
     **/
    private String[] brandIds;

    /**
     * 品牌名称ids
     **/
    private String[] brandNames;

    /**
     * 预热开始时间
     **/
    private String readyBeginTime;

    /**
     * 预热结束时间
     **/
    private String readyEndTime;

    /**
     * 正式开始时间
     **/
    private String formalBeginTime;

    /**
     * 正式结束时间
     **/
    private String formalEndTime;

    /**
     * 品牌ID
     **/
    private Integer brandId;
    /**
     * 直播场次分组 1：是
     **/
    private Integer periodsGroup;

    /**
     * 是否发送服务通知（0 不发送，1发送）
     */
    private Integer sendFlag;

    public String getSceneTitle() {
        return sceneTitle;
    }

    public void setSceneTitle(String sceneTitle) {
        this.sceneTitle = sceneTitle;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
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

    public Integer getExcludeId() {
        return excludeId;
    }

    public void setExcludeId(Integer excludeId) {
        this.excludeId = excludeId;
    }

    public Integer getSceneId() {
        return sceneId;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }

    public Integer getDirectorId() {
        return directorId;
    }

    public void setDirectorId(Integer directorId) {
        this.directorId = directorId;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public Integer getSceneState() {
        return sceneState;
    }

    public void setSceneState(Integer sceneState) {
        this.sceneState = sceneState;
    }

    public Integer getUpState() {
        return upState;
    }

    public void setUpState(Integer upState) {
        this.upState = upState;
    }

    public List<Integer> getSceneIdList() {
        return sceneIdList;
    }

    public void setSceneIdList(List<Integer> sceneIdList) {
        this.sceneIdList = sceneIdList;
    }

    public List<Integer> getBrandIdList() {
        return brandIdList;
    }

    public void setBrandIdList(List<Integer> brandIdList) {
        this.brandIdList = brandIdList;
    }

    public List<Integer> getDealerIdList() {
        return dealerIdList;
    }

    public void setDealerIdList(List<Integer> dealerIdList) {
        this.dealerIdList = dealerIdList;
    }

    public String getCityIds() {
        return cityIds;
    }

    public void setCityIds(String cityIds) {
        this.cityIds = cityIds;
    }

    public String[] getCityNames() {
        return cityNames;
    }

    public void setCityNames(String[] cityNames) {
        this.cityNames = cityNames;
    }

    public String getReadyBeginTime() {
        return readyBeginTime;
    }

    public void setReadyBeginTime(String readyBeginTime) {
        this.readyBeginTime = readyBeginTime;
    }

    public String getReadyEndTime() {
        return readyEndTime;
    }

    public void setReadyEndTime(String readyEndTime) {
        this.readyEndTime = readyEndTime;
    }

    public String getFormalBeginTime() {
        return formalBeginTime;
    }

    public void setFormalBeginTime(String formalBeginTime) {
        this.formalBeginTime = formalBeginTime;
    }

    public String getFormalEndTime() {
        return formalEndTime;
    }

    public void setFormalEndTime(String formalEndTime) {
        this.formalEndTime = formalEndTime;
    }

    public Integer getPeriodsId() {
        return periodsId;
    }

    public void setPeriodsId(Integer periodsId) {
        this.periodsId = periodsId;
    }

    public String getPeriodsName() {
        return periodsName;
    }

    public void setPeriodsName(String periodsName) {
        this.periodsName = periodsName;
    }

    public List<Integer> getPeriodsIdList() {
        return periodsIdList;
    }

    public void setPeriodsIdList(List<Integer> periodsIdList) {
        this.periodsIdList = periodsIdList;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getPeriodsGroup() {
        return periodsGroup;
    }

    public void setPeriodsGroup(Integer periodsGroup) {
        this.periodsGroup = periodsGroup;
    }

    public String[] getBrandIds() {
        return brandIds;
    }

    public void setBrandIds(String[] brandIds) {
        this.brandIds = brandIds;
    }

    public String[] getBrandNames() {
        return brandNames;
    }

    public void setBrandNames(String[] brandNames) {
        this.brandNames = brandNames;
    }

    public List<Integer> getCityIdList() {
        return cityIdList;
    }

    public void setCityIdList(List<Integer> cityIdList) {
        this.cityIdList = cityIdList;
    }

    public Integer getJoinCityId() {
        return joinCityId;
    }

    public void setJoinCityId(Integer joinCityId) {
        this.joinCityId = joinCityId;
    }

    public Integer getSendFlag() {
        return sendFlag;
    }

    public void setSendFlag(Integer sendFlag) {
        this.sendFlag = sendFlag;
    }
}
