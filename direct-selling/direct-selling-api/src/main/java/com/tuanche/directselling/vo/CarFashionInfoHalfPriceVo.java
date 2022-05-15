package com.tuanche.directselling.vo;

import com.tuanche.consol.dubbo.vo.carFashion.ActivityProductInfoResVo;
import com.tuanche.consol.dubbo.vo.carFashion.CarFashionBrandInfoEntityResVo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author lvsen
 * @Description
 * @Date 2021/9/22
 **/
public class CarFashionInfoHalfPriceVo implements Serializable {
    private Integer id;
    private Integer carFashionId;
    private Integer activityId;
    private String carFashionName;
    private String activityName;
    private Date headDate;
    private Date formalDateStart;
    private Date formalDateEnd;
    private String headDateStr;
    private String formalDateStartStr;
    private String formalDateEndStr;
    private String headDateStartStr;
    private String headDateEndStr;
    private List<ActivityProductInfoResVo> volme;
    private List<ActivityProductInfoResVo> playing;
    private List<CarFashionBrandInfoEntityResVo> brandList;
    private String qwUrl;
    private String operUrl;
    private String headImage;
    private boolean exp;
    private String halfPriceUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCarFashionId() {
        return carFashionId;
    }

    public void setCarFashionId(Integer carFashionId) {
        this.carFashionId = carFashionId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getCarFashionName() {
        return carFashionName;
    }

    public void setCarFashionName(String carFashionName) {
        this.carFashionName = carFashionName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Date getHeadDate() {
        return headDate;
    }

    public void setHeadDate(Date headDate) {
        this.headDate = headDate;
    }

    public Date getFormalDateStart() {
        return formalDateStart;
    }

    public void setFormalDateStart(Date formalDateStart) {
        this.formalDateStart = formalDateStart;
    }

    public Date getFormalDateEnd() {
        return formalDateEnd;
    }

    public void setFormalDateEnd(Date formalDateEnd) {
        this.formalDateEnd = formalDateEnd;
    }

    public String getHeadDateStr() {
        return headDateStr;
    }

    public void setHeadDateStr(String headDateStr) {
        this.headDateStr = headDateStr;
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

    public List<ActivityProductInfoResVo> getVolme() {
        return volme;
    }

    public void setVolme(List<ActivityProductInfoResVo> volme) {
        this.volme = volme;
    }

    public List<ActivityProductInfoResVo> getPlaying() {
        return playing;
    }

    public void setPlaying(List<ActivityProductInfoResVo> playing) {
        this.playing = playing;
    }

    public List<CarFashionBrandInfoEntityResVo> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<CarFashionBrandInfoEntityResVo> brandList) {
        this.brandList = brandList;
    }

    public String getQwUrl() {
        return qwUrl;
    }

    public void setQwUrl(String qwUrl) {
        this.qwUrl = qwUrl;
    }

    public String getOperUrl() {
        return operUrl;
    }

    public void setOperUrl(String operUrl) {
        this.operUrl = operUrl;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public boolean isExp() {
        return exp;
    }

    public void setExp(boolean exp) {
        this.exp = exp;
    }

    public String getHalfPriceUrl() {
        return halfPriceUrl;
    }

    public void setHalfPriceUrl(String halfPriceUrl) {
        this.halfPriceUrl = halfPriceUrl;
    }

    public String getHeadDateStartStr() {
        return headDateStartStr;
    }

    public void setHeadDateStartStr(String headDateStartStr) {
        this.headDateStartStr = headDateStartStr;
    }

    public String getHeadDateEndStr() {
        return headDateEndStr;
    }

    public void setHeadDateEndStr(String headDateEndStr) {
        this.headDateEndStr = headDateEndStr;
    }
}
