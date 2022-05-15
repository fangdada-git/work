package com.tuanche.directselling.mapper.write.directselling;


import com.tuanche.directselling.model.AfterSaleActivityPackage;

public interface AfterSaleActivityPackageWriteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AfterSaleActivityPackage record);

    int insertSelective(AfterSaleActivityPackage record);

    AfterSaleActivityPackage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AfterSaleActivityPackage record);

    int updateByPrimaryKey(AfterSaleActivityPackage record);
}