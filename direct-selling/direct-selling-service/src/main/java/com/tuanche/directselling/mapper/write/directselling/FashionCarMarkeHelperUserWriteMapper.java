package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.FashionCarMarkeHelperUser;

public interface FashionCarMarkeHelperUserWriteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FashionCarMarkeHelperUser record);

    int insertSelective(FashionCarMarkeHelperUser record);

    int updateByPrimaryKeySelective(FashionCarMarkeHelperUser record);

    int updateByPrimaryKey(FashionCarMarkeHelperUser record);

    Integer updateHelperUserToBuy(FashionCarMarkeHelperUser helperUser);
}