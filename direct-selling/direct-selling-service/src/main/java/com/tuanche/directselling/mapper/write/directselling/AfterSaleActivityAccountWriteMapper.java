package com.tuanche.directselling.mapper.write.directselling;


import com.tuanche.directselling.model.AfterSaleActivityAccount;

public interface AfterSaleActivityAccountWriteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AfterSaleActivityAccount record);

    int insertSelective(AfterSaleActivityAccount record);

    AfterSaleActivityAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AfterSaleActivityAccount record);

    int updateByPrimaryKey(AfterSaleActivityAccount record);
}