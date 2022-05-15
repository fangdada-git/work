package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.FissionShareData;

import java.util.List;

/**
 * FissionShareDataMapper
 */
public interface FissionShareDataReadMapper {

    FissionShareData selectByPrimaryKey(Integer id);
    /**
     * 取已生成过的分享数据
     * @param shareData
     * @return
     */
    List<FissionShareData> selectShareData(FissionShareData shareData);
}