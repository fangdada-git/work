package com.tuanche.directselling.mapper.write.directselling;


import com.tuanche.directselling.model.FissionDataView;

public interface FissionDataViewWriteMapper {
    int deleteByPrimaryKey(Long id);

    int insert(FissionDataView record);

    int insertSelective(FissionDataView record);

    FissionDataView selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FissionDataView record);

    int updateByPrimaryKey(FissionDataView record);
}