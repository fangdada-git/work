package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.FissionLiveRemind;
import com.tuanche.directselling.vo.FissionActivityApiVo;

import java.util.List;

/**
 * FissionLiveRemindReadMapper
 */
public interface FissionLiveRemindReadMapper {

    FissionLiveRemind selectByPrimaryKey(Long id);

    List<FissionLiveRemind> selectLiveRemindList(FissionLiveRemind liveRemind);

    /**
     * 是否预约过
     * @param activityApiVo
     * @return
     */
    int existAppoint(FissionActivityApiVo activityApiVo);

    /**
     * 使用中微信订阅消息场景值
     * @return
     */
    List<FissionLiveRemind> useOfWxSceneList();
}