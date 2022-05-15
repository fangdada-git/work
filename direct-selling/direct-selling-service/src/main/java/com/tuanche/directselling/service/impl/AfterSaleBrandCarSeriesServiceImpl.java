package com.tuanche.directselling.service.impl;

import com.tuanche.directselling.api.AfterSaleBrandCarSeriesService;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleBrandReadMapper;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleCarSeriesReadMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleBrandWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleCarSeriesWriteMapper;
import com.tuanche.directselling.model.AfterSaleBrand;
import com.tuanche.directselling.model.AfterSaleCarSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/10/12 15:12
 **/
public class AfterSaleBrandCarSeriesServiceImpl implements AfterSaleBrandCarSeriesService {

    @Autowired
    private AfterSaleBrandReadMapper afterSaleBrandReadMapper;
    @Autowired
    private AfterSaleCarSeriesReadMapper afterSaleCarSeriesReadMapper;
    @Autowired
    private AfterSaleBrandWriteMapper afterSaleBrandWriteMapper;
    @Autowired
    private AfterSaleCarSeriesWriteMapper afterSaleCarSeriesWriteMapper;

    @Override
    public int insertBrand(AfterSaleBrand afterSaleBrand) {
        return afterSaleBrandWriteMapper.insertSelective(afterSaleBrand);
    }

    @Override
    public int insertCarSeries(AfterSaleCarSeries afterSaleCarSeries) {
        return afterSaleCarSeriesWriteMapper.insertSelective(afterSaleCarSeries);
    }

    @Override
    @CacheEvict(value = "afterSale:brand", key = "#afterSaleBrand.originalBrandName")
    public int updateBrand(AfterSaleBrand afterSaleBrand) {
        return afterSaleBrandWriteMapper.updateByPrimaryKeySelective(afterSaleBrand);
    }

    @Override
    @CacheEvict(value = "afterSale:carSeries", key = "#afterSaleCarSeries.brandId+':'+#afterSaleCarSeries.originalCarSeriesName")
    public int updateCarSeries(AfterSaleCarSeries afterSaleCarSeries) {
        return afterSaleCarSeriesWriteMapper.updateByPrimaryKeySelective(afterSaleCarSeries);
    }

    @Override
    @Cacheable(value = "afterSale:brand", key = "#originalBrandName")
    public AfterSaleBrand getBrand(String originalBrandName) {
        return afterSaleBrandReadMapper.selectByOriginalBrandName(originalBrandName);
    }

    @Override
    @Cacheable(value = "afterSale:carSeries", key = "#brandId+':'+#originalCarSeriesName")
    public AfterSaleCarSeries getCarSeries(Integer brandId, String originalCarSeriesName) {
        return afterSaleCarSeriesReadMapper.selectByBrandIdAndOriginalCarSeriesName(brandId, originalCarSeriesName);
    }
}
