package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.FissionActivityExtend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FissionActivityExtendWriteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FissionActivityExtend record);

    int batchInsert(@Param("extendList") List<FissionActivityExtend> extendList);

    int insertSelective(FissionActivityExtend record);

    int updateByPrimaryKeySelective(FissionActivityExtend record);

    int updateByPrimaryKey(FissionActivityExtend record);

    int updateByFissionIdAndType(FissionActivityExtend record);
}