package com.tuanche.directselling.model;

import java.io.Serializable;

/**
    * 团车直卖场次活动表
    */
public class LiveSceneCityBrand implements Serializable {
    private Integer id;

    /**
    * 场次活动ID
    */
    private Integer sceneId;

    /**
    * 城市ID，品牌ID
    */
    private Integer dataId;

    /**
    * 城市名称，品牌名称
    */
    private String dataValue;

    /**
    * 数据类型0:城市 1:品牌
    */
    private Integer dataType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSceneId() {
        return sceneId;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }
}