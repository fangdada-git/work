package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.model.FissionActivityData;
import org.apache.ibatis.annotations.Param;

/**
 * @description 裂变任务数据readmapper
 * @date 2020/9/22 16:54
 * @author lvsen
 */
public interface FissionActivityDataReadMapper {

    FissionActivityData selectByFissionId(@Param("fissionId")Integer fissionId);

}