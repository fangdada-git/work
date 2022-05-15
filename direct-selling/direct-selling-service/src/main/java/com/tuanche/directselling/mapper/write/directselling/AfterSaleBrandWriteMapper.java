package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.AfterSaleBrand;
import org.apache.ibatis.annotations.Param;

public interface AfterSaleBrandWriteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AfterSaleBrand record);

    int insertSelective(AfterSaleBrand record);

    int updateByPrimaryKeySelective(AfterSaleBrand record);

    int updateByPrimaryKey(AfterSaleBrand record);

    int deleteByOriginalBrandName(@Param("originalBrandName") String originalBrandName);

    AfterSaleBrand selectByOriginalBrandName(@Param("originalBrandName") String originalBrandName);
}