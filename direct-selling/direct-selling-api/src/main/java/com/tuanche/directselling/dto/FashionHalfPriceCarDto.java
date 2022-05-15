package com.tuanche.directselling.dto;

import com.tuanche.consol.dubbo.vo.carFashion.CarFashionBrandInfoEntityResVo;
import com.tuanche.directselling.model.FashionHalfPriceCar;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author lvsen
 * @Description
 * @Date 2021/9/14
 **/
public class FashionHalfPriceCarDto implements Serializable {
    /**
     * 潮车集id
     */
    private Integer carFashionId;
    /**
     * 正式开始时间
     */
    private String formalDateStartStr;
    /**
     * 正式结束时间
     */
    private String formalDateEndStr;
    /**
     * 品牌列表
     */
    private List<CarFashionBrandInfoEntityResVo> brandList;

    private List<FashionHalfPriceCar> fashionHalfPriceCarList;

    public List<FashionHalfPriceCar> getFashionHalfPriceCarList() {
        return fashionHalfPriceCarList;
    }

    public void setFashionHalfPriceCarList(List<FashionHalfPriceCar> fashionHalfPriceCarList) {
        this.fashionHalfPriceCarList = fashionHalfPriceCarList;
    }

    public Integer getCarFashionId() {
        return carFashionId;
    }

    public void setCarFashionId(Integer carFashionId) {
        this.carFashionId = carFashionId;
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

    public List<CarFashionBrandInfoEntityResVo> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<CarFashionBrandInfoEntityResVo> brandList) {
        this.brandList = brandList;
    }
}
