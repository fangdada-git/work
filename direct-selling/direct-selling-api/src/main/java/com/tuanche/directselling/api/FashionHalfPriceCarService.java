package com.tuanche.directselling.api;

import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.consol.dubbo.vo.carFashion.CarFashionInfoEntityResVo;
import com.tuanche.directselling.dto.FashionCarMarkeWinNumDto;
import com.tuanche.directselling.dto.FashionHalfPriceCarActivityDto;
import com.tuanche.directselling.model.FashionHalfPriceCar;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.vo.FashionHalfPriceBrandVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author lvsen
 * @Description
 * @Date 2021/9/9
 **/
public interface FashionHalfPriceCarService {

    /**
     *  根据潮车集活动id查询半价车列表
     * @param fashionId
     * @return
     */
    List<FashionHalfPriceCar> getHalfPriceCarListByPeriodsId(Integer fashionId);

    /**
     * 新增半价车配置
     * @param fashionHalfPriceCarList
     * @param optUser
     */
    void saveFashionHalfPriceCar(List<FashionHalfPriceCar> fashionHalfPriceCarList,Integer optUser);

    void getNotFilledActivity(Integer periodsId);

    /**
     *  修改半价车配置
     * @param fashionHalfPriceCarList
     * @param optUser
     */
    void updateFashionHalfPriceCar(List<FashionHalfPriceCar> fashionHalfPriceCarList,Integer optUser);

    /**
     * 活动中奖用户
     * @return
     */
    PageResult<FashionCarMarkeWinNumDto> getFashionCarMarkeWinnerListByPage(PageResult<FashionCarMarkeWinNumDto> pageResult, FashionCarMarkeWinNumDto fashionCarMarkeWinNumDto,
                                                                            CarFashionInfoEntityResVo fashionActivity);

    /**
     * 半价车活动接口
     */
    TcResponse getHalfPriceActivityInfoForApi(FashionHalfPriceCarActivityDto halfPriceCarActivityDto,Integer userId);

    /**
     * 品牌列表
     * @param halfPriceCarActivityDto
     * @return
     */
    List<FashionHalfPriceBrandVo> getHalfPriceActivityBrandList(FashionHalfPriceCarActivityDto halfPriceCarActivityDto);

    Integer getBrandHalfpriceFlag(@Param("periodsId") Integer periodsId, @Param("brandId") Integer brandId);

    /**
      * @description : 根据大场次id 和 开奖日期列表 获取半价车活动信息
      * @author : fxq
      * @param : key : periodsId-activityDate(yyyy-MM-dd)  val:FashionHalfPriceCar
      * @return :
      * @date : 2021/11/2 16:16
      */
    Map<String, FashionHalfPriceCar> getHalfPriceCarListByPeriodsIdAndDateList(Integer periodsId, List<Date> activityDateList);
}
