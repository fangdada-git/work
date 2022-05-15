package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.model.FissionDict;

import java.util.List;

/**
 * @author lvsen
 * @description 裂变字典mapper
 * @date 2020/9/22 16:56
 */
public interface FissionDictReadMapper {

    FissionDict selectByPrimaryKey(Integer id);

    List<FissionDict> selectByType(Short type);

}