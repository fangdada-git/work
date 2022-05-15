package com.tuanche.directselling.mapper.read.directselling;


import com.tuanche.directselling.model.FissionDataView;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FissionDataViewReadMapper {
    int deleteByPrimaryKey(Long id);

    int insert(FissionDataView record);

    int insertSelective(FissionDataView record);

    FissionDataView selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FissionDataView record);

    int updateByPrimaryKey(FissionDataView record);

    List<FissionDataView> selectByFissionIds(List<Integer> fissionIds);

    List<FissionDataView> selectFissionDataViewByFissionId(@Param("fissionId") Integer fissionId);

    List<FissionDataView> selectByFissionId(@Param("fissionId") Integer fissionId, @Param("dataType") Integer dataType, @Param("isShare") Boolean isShare);
}