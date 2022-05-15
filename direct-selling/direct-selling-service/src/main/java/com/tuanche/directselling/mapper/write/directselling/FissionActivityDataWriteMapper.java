package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.FissionActivityData;

public interface FissionActivityDataWriteMapper {
    int deleteByPrimaryKey(Integer id);

    /**
     * 根据裂变id删除
     */
    int deleteByFissionId(Integer fissionId);

    int insert(FissionActivityData record);

    int insertSelective(FissionActivityData record);

    int updateByPrimaryKeySelective(FissionActivityData record);

    int updateByPrimaryKey(FissionActivityData record);

    /**
     * 更新真实数
     * @param record
     * @return
     */
    int updateEffectNumByFissionId(FissionActivityData record);
}