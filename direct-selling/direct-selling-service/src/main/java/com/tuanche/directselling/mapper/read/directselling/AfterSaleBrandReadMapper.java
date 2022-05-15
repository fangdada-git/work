package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.AfterSaleBrandCarSeriesDto;
import com.tuanche.directselling.model.AfterSaleBrand;
import org.apache.ibatis.annotations.Param;

public interface AfterSaleBrandReadMapper {

    AfterSaleBrand selectByPrimaryKey(Integer id);

    AfterSaleBrand selectByOriginalBrandName(@Param("originalBrandName") String originalBrandName);

    AfterSaleBrandCarSeriesDto selectByOriginalName(@Param("originalBrandName") String originalBrandName, @Param("originalCarSeriesName") String originalCarSeriesName);
}