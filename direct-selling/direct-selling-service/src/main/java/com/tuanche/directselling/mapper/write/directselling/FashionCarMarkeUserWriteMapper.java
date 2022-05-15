package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.FashionCarMarkeUser;

public interface FashionCarMarkeUserWriteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FashionCarMarkeUser record);

    int insertSelective(FashionCarMarkeUser record);

    int updateByPrimaryKeySelective(FashionCarMarkeUser record);

    int updateByPrimaryKey(FashionCarMarkeUser record);
}