package com.tuanche.directselling.api;

import com.tuanche.directselling.model.FissionSaleTaskStatistics;
import com.tuanche.directselling.model.FissionUserEffectiveTask;

import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2020-09-28 16:23
 */
public interface FissionUserEffectiveTaskService {

    /**
     * 新增用户有效的任务
     * @author HuangHao
     * @CreatTime 2020-09-23 16:37
     * @param effectiveTask
     * @return int
     */
    Long insertFissionUserEffectiveTask(FissionUserEffectiveTask task);
    /**
     * 用户是否完成了某一件任务
     * @author HuangHao
     * @CreatTime 2020-09-23 17:11
     * @param fissionId 裂变活动ID
     * @param userWxOpenId 用户微信openId
     * @param taskId    任务ID列表
     * @return java.lang.Integer -1:缺少参数 0：不存在 1：存在
     */
    int taskIsCompletedInFission(Integer fissionId,String userWxOpenId,List<Integer> taskIds);
    /**
     * 统计销售的每一项任务的总积分
     * @author HuangHao
     * @CreatTime 2020-09-28 16:13
     * @param fissionIds
     * @return java.util.List<com.tuanche.directselling.model.FissionSaleTaskStatistics>
     */
    List<FissionSaleTaskStatistics> statisticsSaleTaskIntegrals(List<Integer> fissionIds);

    /**
     * @description: 分享活动页是否打开过
     * @param effectiveTask
     * @return: java.lang.Boolean
     * @author: dudg
     * @date: 2020/11/30 17:48
     */
    Boolean shareAcitvityOpened(FissionUserEffectiveTask effectiveTask);
}
