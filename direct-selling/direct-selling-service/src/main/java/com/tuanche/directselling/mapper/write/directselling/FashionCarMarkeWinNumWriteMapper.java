package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.FashionCarMarkeWinNum;
import org.apache.ibatis.annotations.Param;

public interface FashionCarMarkeWinNumWriteMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(FashionCarMarkeWinNum record);

    int updateByPrimaryKeySelective(FashionCarMarkeWinNum record);

    int updateByWinNum(@Param("periodsId") Integer periodsId, @Param("winNum") Integer winNum, @Param("winNumNew") Integer winNumNew);
}