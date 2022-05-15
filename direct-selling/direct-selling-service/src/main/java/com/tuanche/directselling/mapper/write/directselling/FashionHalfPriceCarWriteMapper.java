package com.tuanche.directselling.mapper.write.directselling;


import com.tuanche.directselling.model.FashionHalfPriceCar;

public interface FashionHalfPriceCarWriteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FashionHalfPriceCar record);

    int insertSelective(FashionHalfPriceCar record);

    FashionHalfPriceCar selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FashionHalfPriceCar record);

    int updateByPrimaryKey(FashionHalfPriceCar record);
}