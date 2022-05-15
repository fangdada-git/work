package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.model.FissionSaleTaskStatistics;
import com.tuanche.directselling.model.FissionUserEffectiveTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2020-09-23 17:05
 */
@Repository
public interface FissionUserEffectiveTaskReadMapper {


    /**
     * 用户是否完成了某一些任务
     * @author HuangHao
     * @CreatTime 2020-09-23 17:06
     * @param effectiveTask
     * @return java.lang.Integer
     */
    Integer taskIsCompletedInFission(@Param("userWxUnionId")String userWxUnionId,@Param("fissionId")Integer fissionId,@Param("taskIds")List<Integer> taskIds);

    /**
     * 统计销售的每一项任务的总积分
     * @author HuangHao
     * @CreatTime 2020-09-28 16:13
     * @param fissionIds
     * @return java.util.List<com.tuanche.directselling.model.FissionSaleTaskStatistics>
     */
    List<FissionSaleTaskStatistics> statisticsSaleTaskIntegrals(@Param("fissionIds")List<Integer> fissionIds);

    /**
     * @description: 根据活动id&分享人id 查询打开页面记录数
     * @param effectiveTask
     * @return: java.lang.Integer
     * @author: dudg
     * @date: 2020/11/30 17:43
    */
    Integer selectShareTaskCount(FissionUserEffectiveTask effectiveTask);
}
