package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.LivePeriodDealerDto;
import com.tuanche.directselling.dto.LiveSceneCityDto;
import com.tuanche.directselling.dto.LiveSceneDataReportDto;
import com.tuanche.directselling.dto.LiveSceneDto;
import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.LiveScene;
import com.tuanche.directselling.vo.LiveSceneBrandVo;
import com.tuanche.directselling.vo.LiveSceneVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * LiveSceneDAO继承基类
 */
@Repository
public interface LiveSceneReadMapper extends MyBatisBaseDao<LiveScene, Integer> {

    List<LiveSceneDto> selectLiveSceneDtoList(LiveSceneVo liveSceneVo);

    List<LiveScene> selectLiveSceneList(LiveSceneVo liveSceneVo);

    List<LiveSceneCityDto> getLiveSceneCityDtoList(@Param("liveSceneVo") LiveSceneVo liveSceneVo);

    Map<String, Object> getLiveInfoByPeriodsId(@Param("periodsId") Integer periodsId);

    LiveScene getLastLiveScenePeriodsId();

    List<LiveSceneDataReportDto> getSceneDataReportByPeriodsId(@Param("periodsId") Integer periodsId,
                                                               @Param("cityId") Integer cityId, @Param("brandId") Integer brandId);

    List<LiveSceneCityDto> getPeriodsCityListByPeriodsId(@Param("periodsId") Integer periodsId);

    List<LiveSceneBrandVo> getPeriodsBrandListByPeriodsId(@Param("periodsId") Integer periodsId);

    List<LivePeriodDealerDto> getPeriodSceneDealerList(LivePeriodDealerDto periodDealerDto);

    /**
     * 获取裂变经销商列表
     * @author HuangHao
     * @CreatTime 2021-03-12 15:48
     * @param periodDealerDto
     * @return java.util.List<com.tuanche.directselling.dto.LivePeriodDealerDto>
     */
    List<LivePeriodDealerDto> getFissionDealerList(LivePeriodDealerDto periodDealerDto);


    List<LiveSceneDto> selectLiveSceneDtoPage(LiveSceneVo liveSceneVo);

    List<LiveScene> getLiveSceneNotFinished(@Param("timeNow") Date timeNow);

    List<LiveSceneDto> selectLiveSceneTitleList(LiveSceneVo liveSceneVo);

    List<LiveScene> getLiveSceneListByLiveScene(LiveSceneVo liveSceneVo);

    /**
     * 获取经销商参加的小场次列表
     * @author HuangHao
     * @CreatTime 2021-04-20 17:52
     * @param dealerId
     * @return java.util.List<com.tuanche.directselling.model.LiveScene>
     */
    List<LiveScene> getDealerSceneList(@Param("dealerId") Integer dealerId);
}