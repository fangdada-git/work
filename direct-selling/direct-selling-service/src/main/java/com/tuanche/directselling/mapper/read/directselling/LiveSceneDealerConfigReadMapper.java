package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.DealerRefuelCarActivityInfoDto;
import com.tuanche.directselling.dto.LiveSceneDealerConfigDto;
import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.LiveSceneDealerConfig;
import com.tuanche.directselling.vo.LiveSceneDealerConfigVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * LiveSceneDealerConfigDAO继承基类
 */
@Repository
public interface LiveSceneDealerConfigReadMapper extends MyBatisBaseDao<LiveSceneDealerConfig, Integer> {

    LiveSceneDealerConfig selectByLiveSceneDealerConfig(LiveSceneDealerConfig liveSceneDealerConfig);

    int selectByLiveSceneDealerConfigCount(LiveSceneDealerConfig liveSceneDealerConfig);

    List<LiveSceneDealerConfigDto> selectSceneDealerConfigList(Integer sceneId);

    /**
     * 获取场次活动经销商配置列表
     * @author HuangHao
     * @CreatTime 2020-05-29 15:57
     * @param liveSceneDealerConfig
     * @return java.util.List<com.tuanche.directselling.model.LiveSceneDealerConfig>
     */
    List<LiveSceneDealerConfigDto> getLiveSceneDealerConfigList(LiveSceneDealerConfigVo liveSceneDealerConfig);
    /**
     * 经销商油卡统计
     * @author HuangHao
     * @CreatTime 2020-06-02 11:32
     * @param liveSceneDealerConfig
     * @return java.util.List<com.tuanche.directselling.dto.LiveSceneDealerConfigDto>
     */
    List<LiveSceneDealerConfigDto> getDealerRefuelCardStatistics(LiveSceneDealerConfigVo liveSceneDealerConfig);
    /**
     * 获取经销商参加过直播活动的大场次
     * @author HuangHao
     * @CreatTime 2020-06-02 18:10
     * @param dealerId
     * @return java.util.List<com.tuanche.directselling.model.LiveSceneDealerConfig>
     */
    List<LiveSceneDealerConfigDto> getDealerPeriods(Integer dealerId);

    /**
     * 获取经销商的赠送油卡活动信息
     * @author HuangHao
     * @CreatTime 2020-06-03 10:50
     * @param
     * @return com.tuanche.directselling.dto.DealerRefuelCarActivityInfoDto
     */
    DealerRefuelCarActivityInfoDto getDealerRefuelCarActivityInfo(LiveSceneDealerConfig liveSceneDealerConfig);
}