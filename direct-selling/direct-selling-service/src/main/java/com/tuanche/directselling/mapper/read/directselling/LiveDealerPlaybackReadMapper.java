package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.LiveDealerPlaybackDto;
import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.LiveDealerPlayback;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * LiveDealerPlaybackReadMapper继承基类
 */
@Repository
public interface LiveDealerPlaybackReadMapper extends MyBatisBaseDao<LiveDealerPlayback, Integer> {

    List<LiveDealerPlaybackDto> getLiveDealerPlaybackByDealerId(Integer dealerId);

    LiveDealerPlaybackDto getInfoByDealerIdAndActivityId(@Param("dealerId") Integer dealerId
            , @Param("activityId") Integer activityId);
}