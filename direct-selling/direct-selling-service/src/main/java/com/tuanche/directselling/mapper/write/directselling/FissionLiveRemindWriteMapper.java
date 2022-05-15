package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.FissionLiveRemind;

/**
 * FissionLiveRemindReadMapper
 */
public interface FissionLiveRemindWriteMapper {

    int deleteByPrimaryKey(Long id);

    int insertSelective(FissionLiveRemind record);

    FissionLiveRemind selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FissionLiveRemind record);

    /**
     * 更新提醒状态
     * @param liveId
     * @return
     */
    int updateRemindStateByLiveId(Integer liveId);
}