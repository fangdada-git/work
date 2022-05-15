package com.tuanche.directselling.mapper.read.directselling;


import com.tuanche.directselling.model.FashionHalfPriceCar;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface FashionHalfPriceCarReadMapper {

    FashionHalfPriceCar selectByPrimaryKey(Integer id);

    /**
     * 根据潮车集id查半价车配置信息
     *
     * @param fashionId
     * @return
     */
    List<FashionHalfPriceCar> selectListByPeriodsId(Integer periodsId);

    List<FashionHalfPriceCar> getNotFilledActivity(Integer periodsId);

    /**
     * 未录入中奖码的数据
     *
     * @param day 日期 yyyy-MM-dd
     * @return
     */
    List<FashionHalfPriceCar> selectNotInputWinNum(@Param("day") String day);

    /**
     * 已录入中奖码的数据
     *
     * @param day 日期 yyyy-MM-dd
     * @return
     */
    List<FashionHalfPriceCar> selectInputtedWinNum(@Param("day") String day);

    /**
     * 根据时间查活动
     *
     * @param day 日期 yyyy-MM-dd
     * @return
     */
    List<FashionHalfPriceCar> selectActivityByDay(@Param("day") String day);

    Integer getBrandHalfpriceFlag(@Param("periodsId") Integer periodsId,@Param("brandId") Integer brandId);

    List<FashionHalfPriceCar> getHalfPriceCarListByPeriodsIdAndDateList(@Param("periodsId")Integer periodsId, @Param("activityDateList")List<Date> activityDateList);
}