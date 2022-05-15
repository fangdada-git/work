package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.FashionCarMarkeWinNumDto;
import com.tuanche.directselling.model.FashionCarMarkeWinNum;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface FashionCarMarkeWinNumReadMapper {

    FashionCarMarkeWinNum selectByPrimaryKey(Integer id);

    List<FashionCarMarkeWinNum> selectByActivityIdAndUserId(@Param("periodsId") Integer periodsId, @Param("userId") Integer userId);

    List<FashionCarMarkeWinNum> getFashionCarMarkeWinNumList(FashionCarMarkeWinNum markeWinNum);

    /**
     * 查询匹配的中奖码
     *
     * @param periodsId
     * @return
     */
    FashionCarMarkeWinNum selectChooseWinNum(@Param("periodsId") Integer periodsId, @Param("winNum") Integer winNum, @Param("createTime") Date createTime);

    FashionCarMarkeWinNum selectNearestWinNum(@Param("periodsId") Integer periodsId, @Param("winNum") Integer winNum);

    /**
     * 查询当天中奖码
     *
     * @param periodsId
     * @param activityDate
     * @return
     */
    FashionCarMarkeWinNum selectCurrentDayWinNum(@Param("periodsId") Integer periodsId, @Param("activityDate") LocalDate activityDate);

    /**
     * 活动中奖用户
     *
     * @param fashionCarMarkeWinNumDto
     * @return
     */
    List<FashionCarMarkeWinNumDto> selectFashionCarMarkeWinnerList(FashionCarMarkeWinNumDto fashionCarMarkeWinNumDto);

    Integer selectCountByActivityIdAndUserId(@Param("periodsId") Integer periodsId, @Param("userId") Integer userId);

    FashionCarMarkeWinNum selectWinNum(@Param("periodsId") Integer periodsId, @Param("winNum") Integer winNum);
}