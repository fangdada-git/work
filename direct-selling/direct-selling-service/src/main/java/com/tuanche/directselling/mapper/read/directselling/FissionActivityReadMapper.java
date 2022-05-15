package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.FissionActivityDetailDto;
import com.tuanche.directselling.dto.FissionActivityDto;
import com.tuanche.directselling.dto.FissionActivitySaleDto;
import com.tuanche.directselling.model.FissionActivity;
import com.tuanche.directselling.vo.FissionActivityVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

import java.util.List;

/**
 * @author lvsen
 * @description 裂变任务mapper
 * @date 2020/9/22 16:54
 */
public interface FissionActivityReadMapper {

    FissionActivity selectByPrimaryKey(Integer id);

    /**
     * 查询裂变活动列表
     *
     * @param fissionActivity
     * @return
     */
    List<FissionActivity> selectFissionActivityList(FissionActivity fissionActivity);

    List<FissionActivityVo> selectFissionActivityVoList(FissionActivity fissionActivity);

    /**
     * 根据裂变id查询裂变任务所有配置信息
     *
     * @param fissionId
     * @return
     */
    FissionActivityDto selectFissionActivityDtoById(Integer fissionId);

    /**
     * 首页-活动页数据，根据供应商查活动信息和销售信息
     *
     * @param dealerId 经销商ID
     * @param saleId   销售ID
     * @param now      当前时间
     * @return 活动信息和销售信息
     */
    List<FissionActivitySaleDto> selectActivitySaleByDealerId(@Param("dealerId") Integer dealerId, @Param("saleId") Integer saleId, @Param("now") Date now);

    /**
     * 根据活动ID查询活动详细信息
     *
     * @param dealerId  经销商ID
     * @param fissionId 活动ID
     * @param saleId    销售ID
     * @return
     */
    FissionActivityDetailDto selectActivityDetailByFissionId(@Param("dealerId") Integer dealerId, @Param("fissionId") Integer fissionId, @Param("saleId") Integer saleId);

    /**
     * 裂变活动列表
     * 返回结构：Fission+FissionAwardRule
     *
     * @param fissionActivityDto
     * @return
     */
    List<FissionActivityDto> selectFissionActiveDtoList(FissionActivityDto fissionActivityDto);

    /**
     * 销售参加的活动
     *
     * @param saleId 销售ID
     * @return List<FissionActivity>
     */
    List<FissionActivity> selectFissionActivityBySaleId(@Param("saleId") Integer saleId);

    /**
     * 获取正在进行中且是确认(开启)状态的裂变活动ID列表
     * @author HuangHao
     * @CreatTime 2020-09-28 17:07
     * @param
     * @return java.util.List<java.lang.Integer>
     */
    List<Integer> getOngoingAndConfirmIds();
    /**
     * 获取前一天结束且是确认(开启)状态的裂变活动ID列表
     * @author HuangHao
     * @CreatTime 2020-10-13 17:44
     * @param
     * @return java.util.List<java.lang.Integer>
     */
    List<Integer> getEndedYesterdayAndConfirmIds();

    /**
     *  获取指定时间startTime到现在结束且是开启状态的活动ID
     * @param name 开始时间
     * @return
     */
    List<Integer> getEndAndConfirmActivityIds(@Param("startTime") String startTime);
    /**
     *
     * @param name 活动名称
     * @return
     */
    List<FissionActivity> selectFissionActivityByName(@Param("name") String name);
}