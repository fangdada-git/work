package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.FissionActivitySaleIntegralDto;
import com.tuanche.directselling.model.FissionSaleTaskStatistics;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 销售活动积分统计数据mapper
 *
 * @author ZhangYiXin
 * @date 2020-09-25 10:49:47
 */
@Repository
public interface FissionSaleTaskStatisticsReadMapper {

    /**
     * 销售获得积分数
     *
     * @param saleId 销售ID
     * @return 分数
     */
    Integer getSaleIntegral(@Param("saleId") Integer saleId);

    /**
     * 积分列表
     *
     * @param saleId 销售ID
     * @return 积分列表
     */
    List<FissionActivitySaleIntegralDto> selectSaleIntegralListByBySaleId(@Param("saleId") Integer saleId);

    /**
     * 根据活动ID和销售ID查询任务完成情况
     *
     * @param fissionId 活动ID
     * @param saleId    销售ID
     * @return 查询任务完成情况数据
     */
    List<FissionSaleTaskStatistics> selectTaskFinishInfoByFissionIdSaleId(@Param("fissionId") Integer fissionId, @Param("saleId") Integer saleId);

    /**
     * 活动积分总获得数
     *
     * @param fissionId 活动ID
     * @return 分数
     */
    Integer getFissionIntegral(@Param("fissionId") Integer fissionId);


    /**
     * 活动积分销售获得数
     *
     * @param saleId    销售ID
     * @param fissionId 活动ID
     * @return 分数
     */
    Integer getFissionSaleIntegral(@Param("fissionId") Integer fissionId, @Param("saleId") Integer saleId);

    /**
     * 统计表里存在的销售(key=fissionId_saleWxOpenId)
     * @author HuangHao
     * @CreatTime 2020-09-28 17:50
     * @param fissionIds
     * @return java.util.List<java.lang.String>
     */
    @MapKey("mapKey")
    Map<String,String> existingSales(@Param("fissionIds")List<Integer> fissionIds);

    /**
     * 通过裂变活动IDS获取销售总积分
     * @author HuangHao
     * @CreatTime 2020-10-09 16:02
     * @param fissionIds
     * @return java.util.List<com.tuanche.directselling.model.FissionSaleTaskStatistics>
     */
    List<FissionSaleTaskStatistics> getSaleTotalIntegralByFissionIds(@Param("fissionIds")List<Integer> fissionIds);

    /**
     * 裂变活动总积分
     * @author HuangHao
     * @CreatTime 2020-10-09 16:26
     * @param fissionIds
     * @return java.util.Map<java.lang.Integer,com.tuanche.directselling.model.FissionSaleTaskStatistics>
     */
    @MapKey("fissionId")
    Map<Integer,FissionSaleTaskStatistics> getTotalIntegralByFissionIds(@Param("fissionIds")List<Integer> fissionIds);

    /**
     * 完成销售任务的总积分
     * @author HuangHao
     * @CreatTime 2020-10-09 16:26
     * @param fissionIds
     * @return java.util.Map<java.lang.Integer,com.tuanche.directselling.model.FissionSaleTaskStatistics>
     */
    @MapKey("fissionId")
    Map<Integer,FissionSaleTaskStatistics> getCompleteTaskTotalIntegral(@Param("fissionIds")List<Integer> fissionIds);


    /**
     * 根据活动ID和销售IDs查询任务完成情况
     *
     * @param fissionId 活动ID
     * @param saleIds    销售IDs
     * @return 查询任务完成情况数据
     */
    List<FissionSaleTaskStatistics> selectSaleTaskByFissionIdSaleIds(@Param("fissionId") Integer fissionId, @Param("saleIds") List<Integer> saleIds);

    /**
     * 根据活动ID查询任务完成情况
     *
     * @param fissionId 活动ID
     * @return 查询任务完成情况数据
     */
    List<FissionSaleTaskStatistics> selectSaleTaskByFissionId(@Param("fissionId") Integer fissionId);
}