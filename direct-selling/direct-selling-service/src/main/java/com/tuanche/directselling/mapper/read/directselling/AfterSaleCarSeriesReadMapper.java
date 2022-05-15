package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.model.AfterSaleCarSeries;
import org.apache.ibatis.annotations.Param;

public interface AfterSaleCarSeriesReadMapper {
    AfterSaleCarSeries selectByPrimaryKey(Integer id);

    AfterSaleCarSeries selectByBrandIdAndOriginalCarSeriesName(@Param("brandId") Integer brandId, @Param("originalCarSeriesName") String originalCarSeriesName);
}