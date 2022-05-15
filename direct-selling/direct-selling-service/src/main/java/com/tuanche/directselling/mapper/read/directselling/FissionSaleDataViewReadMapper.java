package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.model.FissionSaleDataView;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ZhangYxin
 * @description 裂变数据B端概览mapper
 * @date 2020-09-23 10:57
 */
public interface FissionSaleDataViewReadMapper {

    FissionSaleDataView selectByFissionId(@Param("fissionId") Integer fissionId);

    List<FissionSaleDataView> selectByFissionIds(@Param("fissionIds") List<Integer> fissionIds);
}