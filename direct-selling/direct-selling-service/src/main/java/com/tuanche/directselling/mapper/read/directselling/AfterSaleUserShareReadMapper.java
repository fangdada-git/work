package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.AfterSaleUserShareStatDto;
import com.tuanche.directselling.dto.AfterSaleUserShareStatPeriodDto;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.Map;

public interface AfterSaleUserShareReadMapper {

    Integer selectLatestIdByTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @MapKey("agentWxUnionId")
    Map<String, AfterSaleUserShareStatDto> selectShareCount(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("activityId") Integer activityId);

    @MapKey("dateTime")
    Map<String, AfterSaleUserShareStatPeriodDto> selectPeriodShareCount(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("activityId") Integer activityId);

    int selectShareCountTotal(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("activityId") Integer activityId);
}