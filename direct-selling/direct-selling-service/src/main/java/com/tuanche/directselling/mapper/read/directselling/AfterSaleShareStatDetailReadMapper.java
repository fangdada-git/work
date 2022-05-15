package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.model.AfterSaleShareStatDetail;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface AfterSaleShareStatDetailReadMapper {

    AfterSaleShareStatDetail selectByActivityIdAndDate(@Param("activityId") Integer activityId, @Param("dateTime") Date dateTime);

    List<AfterSaleShareStatDetail> selectByActivityIdAndBetweenDate(@Param("activityId") Integer activityId, @Param("startDateTime") Date startDateTime, @Param("endDateTime") Date endDateTime);
}