package com.tuanche.directselling.mapper.write.directselling;


import com.tuanche.directselling.model.FissionUserDataView;

public interface FissionUserDataViewWriteMapper {
    int deleteByPrimaryKey(Long id);

    int insert(FissionUserDataView record);

    int insertSelective(FissionUserDataView record);

    FissionUserDataView selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FissionUserDataView record);

    int updateByPrimaryKey(FissionUserDataView record);
}