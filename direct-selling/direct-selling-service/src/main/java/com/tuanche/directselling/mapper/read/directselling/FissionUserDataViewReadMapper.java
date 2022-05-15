package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.model.FissionUserDataView;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ZhangYxin
 * @description 裂变数据C端概览mapper
 * @date 2020-09-23 10:57
 */
public interface FissionUserDataViewReadMapper {
    FissionUserDataView selectByFissionId(@Param("fissionId") Integer fissionId);

    List<FissionUserDataView> selectByFissionIds(@Param("fissionIds") List<Integer> fissionIds);
}