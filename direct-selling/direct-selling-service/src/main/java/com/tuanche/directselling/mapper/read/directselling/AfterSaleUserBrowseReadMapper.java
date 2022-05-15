package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.AfterSaleUserBrowseStatDto;
import com.tuanche.directselling.dto.AfterSaleUserBrowseStatPeriodDto;
import com.tuanche.directselling.dto.ResultMapDto;
import com.tuanche.directselling.model.AfterSaleUserBrowse;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AfterSaleUserBrowseReadMapper {
    AfterSaleUserBrowse selectByPrimaryKey(Integer id);

    Integer selectLatestIdByTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 浏览次数
     *
     * @param startTime
     * @param endTime
     * @param activityId
     * @return
     */
    @MapKey("agentWxUnionId")
    Map<String, AfterSaleUserBrowseStatDto> selectPageView(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("activityId") Integer activityId);

    /**
     * 浏览人数
     *
     * @param startTime
     * @param endTime
     * @param activityId
     * @return
     */
    @MapKey("agentWxUnionId")
    Map<String, AfterSaleUserBrowseStatDto> selectUniqueVisit(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("activityId") Integer activityId);

    @MapKey("dateTime")
    Map<String, AfterSaleUserBrowseStatPeriodDto> selectPeriodUniqueVisit(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("activityId") Integer activityId);

    /**
     * 浏览次数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    Integer selectPageViewSum(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("dealerId") Integer dealerId, @Param("activityId") Integer activityId);

    /**
     * 浏览人数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    Integer selectUniqueVisitSum(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("dealerId") Integer dealerId, @Param("activityId") Integer activityId);

    /**
     * 活动的浏览数量统计
     * @author HuangHao
     * @CreatTime 2021-09-29 17:24
     * @param activityIds
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    @MapKey("mapKey")
    Map<String, ResultMapDto> activityBrowseTotalMap(@Param("activityIds") List<Integer> activityIds);

    /**
     * 总浏览人数
     * @param startTime
     * @param activityId
     * @return
     */
    int selectUniqueVisitTotal(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("activityId") Integer activityId);
}