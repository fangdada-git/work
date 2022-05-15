package com.tuanche.directselling.dto;

import com.tuanche.directselling.model.LiveSceneDealerBrand;
import com.tuanche.directselling.model.LiveSceneDealerConfig;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: LiveSceneDealerConfigDto
 * @Description: 场次活动经销商配置dto
 * @Author: GongBo
 * @Date: 2020/5/21 11:27
 * @Version 1.0
 **/
public class LiveSceneDealerConfigDto extends LiveSceneDealerConfig implements Serializable {

    /**
     * 品牌名称逗号隔开
     **/
    private String brandNames;
    private List<LiveSceneDealerBrand> dealerBrandList;
    //经销商已赠送油卡数量
    private Integer presentedCardNum;
    //大场次名称
    private String periodsName;
    //城市ID
    private Integer cityId;
    //城市名称
    private String cityName;

    public String getBrandNames() {
        return brandNames;
    }

    public void setBrandNames(String brandNames) {
        this.brandNames = brandNames;
    }

    public List<LiveSceneDealerBrand> getDealerBrandList() {
        return dealerBrandList;
    }

    public void setDealerBrandList(List<LiveSceneDealerBrand> dealerBrandList) {
        this.dealerBrandList = dealerBrandList;
    }

    public Integer getPresentedCardNum() {
        return presentedCardNum;
    }

    public void setPresentedCardNum(Integer presentedCardNum) {
        this.presentedCardNum = presentedCardNum;
    }

    public String getPeriodsName() {
        return periodsName;
    }

    public void setPeriodsName(String periodsName) {
        this.periodsName = periodsName;
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
}
