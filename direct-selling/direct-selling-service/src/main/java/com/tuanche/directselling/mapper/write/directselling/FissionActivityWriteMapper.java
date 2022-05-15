package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.FissionActivity;

public interface FissionActivityWriteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FissionActivity record);

    int insertSelective(FissionActivity record);

    int updateByPrimaryKeySelective(FissionActivity record);

    int updateByPrimaryKey(FissionActivity record);
}