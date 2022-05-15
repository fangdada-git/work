package com.tuanche.directselling.api;

import com.tuanche.directselling.model.AfterSaleBrand;
import com.tuanche.directselling.model.AfterSaleCarSeries;

/**
 * 备案流失用户 原品牌车系 对应 团车品牌车系关系
 *
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/10/12 14:06
 **/
public interface AfterSaleBrandCarSeriesService {

    /**
     * 插入品牌
     *
     * @param afterSaleBrand
     * @return
     */
    int insertBrand(AfterSaleBrand afterSaleBrand);

    /**
     * 插入车系
     *
     * @param afterSaleCarSeries
     * @return
     */
    int insertCarSeries(AfterSaleCarSeries afterSaleCarSeries);

    /**
     * 修改品牌
     *
     * @param afterSaleBrand
     * @return
     */
    int updateBrand(AfterSaleBrand afterSaleBrand);

    /**
     * 修改车系
     *
     * @param afterSaleCarSeries
     * @return
     */
    int updateCarSeries(AfterSaleCarSeries afterSaleCarSeries);

    /**
     * 根据原品牌查询
     *
     * @param originalBrandName
     * @return
     */
    AfterSaleBrand getBrand(String originalBrandName);

    /**
     * 根据原车系查
     *
     * @param brandId
     * @param originalCarSeriesName
     * @return
     */
    AfterSaleCarSeries getCarSeries(Integer brandId, String originalCarSeriesName);
}
