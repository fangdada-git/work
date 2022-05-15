package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.AfterSaleCarSeries;
import org.apache.ibatis.annotations.Param;

public interface AfterSaleCarSeriesWriteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AfterSaleCarSeries record);

    int insertSelective(AfterSaleCarSeries record);

    int updateByPrimaryKeySelective(AfterSaleCarSeries record);

    int updateByPrimaryKey(AfterSaleCarSeries record);

    void deleteByOriginalCarSeriesName(@Param("brandId") Integer brandId, @Param("originalCarSeriesName") String originalCarSeriesName);

    AfterSaleCarSeries selectByBrandIdAndOriginalCarSeriesName(@Param("brandId") Integer brandId, @Param("originalCarSeriesName") String originalCarSeriesName);
}