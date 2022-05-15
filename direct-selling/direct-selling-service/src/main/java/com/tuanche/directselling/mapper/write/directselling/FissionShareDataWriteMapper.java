package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.FissionShareData;

/**
 * FissionShareDataMapper
 */
public interface FissionShareDataWriteMapper{

    int deleteByPrimaryKey(Integer id);

    int insertSelective(FissionShareData record);

    FissionShareData selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FissionShareData record);

    int deleteShareDataByFId(Integer fissionId);
}