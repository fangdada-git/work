package com.tuanche.directselling.mapper.write.directselling;


import com.tuanche.directselling.model.FissionSaleDataView;

public interface FissionSaleDataViewWriteMapper {
    int deleteByPrimaryKey(Long id);

    int insert(FissionSaleDataView record);

    int insertSelective(FissionSaleDataView record);

    FissionSaleDataView selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FissionSaleDataView record);

    int updateByPrimaryKey(FissionSaleDataView record);
}