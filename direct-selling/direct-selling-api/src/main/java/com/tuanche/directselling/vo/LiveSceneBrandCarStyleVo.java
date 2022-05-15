package com.tuanche.directselling.vo;

import java.io.Serializable;

/**
 * @Author GongBo
 * @Description 场次活动车型Vo
 * @Date 17:22 2020/6/5
 **/
public class LiveSceneBrandCarStyleVo implements Serializable {
    private Integer id;
    private String name;
    private String logo;
    private String price;
    private String pinyin;
    private Integer carLevel;
    private String carLevelName;
    private Integer level;


    public LiveSceneBrandCarStyleVo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public Integer getCarLevel() {
        return carLevel;
    }

    public void setCarLevel(Integer carLevel) {
        this.carLevel = carLevel;
    }

    public String getCarLevelName() {
        return carLevelName;
    }

    public void setCarLevelName(String carLevelName) {
        this.carLevelName = carLevelName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
