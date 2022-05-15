package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.FissionSaleTaskStatistics;
import com.tuanche.directselling.model.FissionUserEffectiveTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2020-09-23 16:36
 */
@Repository
public interface FissionUserEffectiveTaskWriteMapper {

    /**
     * 用户是否完成了某一些任务-主从同步有延时，所以从写库查
     * @author HuangHao
     * @CreatTime 2020-09-23 17:06
     * @param effectiveTask
     * @return java.lang.Integer
     */
    Integer taskIsCompletedInFission(@Param("userWxUnionId")String userWxUnionId, @Param("fissionId")Integer fissionId, @Param("taskIds") List<Integer> taskIds);

    /**
     * 统计销售的每一项任务的总积分
     * @author HuangHao
     * @CreatTime 2020-09-28 16:13
     * @param fissionIds
     * @return java.util.List<com.tuanche.directselling.model.FissionSaleTaskStatistics>
     */
    List<FissionSaleTaskStatistics> statisticsSaleTaskIntegrals(@Param("fissionIds")List<Integer> fissionIds);


    /**
     * 新增用户有效的任务
     * @author HuangHao
     * @CreatTime 2020-09-23 16:37
     * @param effectiveTask
     * @return int
     */
    int insertFissionUserEffectiveTask(FissionUserEffectiveTask effectiveTask);
    /**
     * 更新用户有效的任务
     * @author HuangHao
     * @CreatTime 2020-09-23 16:37
     * @param effectiveTask
     * @return int
     */
    int updateFissionUserEffectiveTask(FissionUserEffectiveTask effectiveTask);
}
