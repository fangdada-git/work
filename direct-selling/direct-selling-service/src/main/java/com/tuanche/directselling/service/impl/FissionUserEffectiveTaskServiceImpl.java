package com.tuanche.directselling.service.impl;

import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.exception.RedisException;
import com.tuanche.directselling.api.FissionUserEffectiveTaskService;
import com.tuanche.directselling.constant.BaseCacheKeys;
import com.tuanche.directselling.mapper.read.directselling.FissionUserEffectiveTaskReadMapper;
import com.tuanche.directselling.mapper.write.directselling.FissionUserEffectiveTaskWriteMapper;
import com.tuanche.directselling.model.FissionSale;
import com.tuanche.directselling.model.FissionSaleTaskStatistics;
import com.tuanche.directselling.model.FissionUserEffectiveTask;
import com.tuanche.directselling.util.ConstantsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2020-09-23 16:38
 */
@Service
public class FissionUserEffectiveTaskServiceImpl implements FissionUserEffectiveTaskService {

    @Autowired
    FissionUserEffectiveTaskWriteMapper fissionUserEffectiveTaskWriteMapper;
    @Autowired
    FissionUserEffectiveTaskReadMapper fissionUserEffectiveTaskReadMapper;


    /**
     * 新增用户有效的任务
     * @author HuangHao
     * @CreatTime 2020-09-23 16:37
     * @param effectiveTask
     * @return int
     */
    public Long insertFissionUserEffectiveTask(FissionUserEffectiveTask task){
        if(task == null || StringUtils.isEmpty(task.getUserWxUnionId())){
            throw new NullPointerException("用户UnionId不能为空");
        }
        if(task.getFissionId() == null){
            throw new NullPointerException("裂变活动ID不能为空");
        }
        if(task.getTaskId() == null){
            throw new NullPointerException("任务ID不能为空");
        }
        fissionUserEffectiveTaskWriteMapper.insertFissionUserEffectiveTask(task);
        return task.getId();
    }

    /**
     * 用户是否完成了某一件任务
     * @author HuangHao
     * @CreatTime 2020-09-23 17:11
     * @param fissionId 裂变活动ID
     * @param userWxUnionId 用户微信UnionId
     * @param taskId    任务ID列表
     * @return java.lang.Integer -1:缺少参数 0：不存在 1：存在
     */
    public int taskIsCompletedInFission(Integer fissionId,String userWxUnionId,List<Integer> taskIds){
        if(fissionId == null || StringUtils.isEmpty(userWxUnionId) || taskIds == null || taskIds.size() < 1){
            return -1;
        }
        Integer hav = fissionUserEffectiveTaskWriteMapper.taskIsCompletedInFission(userWxUnionId,fissionId,taskIds);
        return hav==null?0:hav;
    }

     /**
     * 统计销售的每一项任务的总积分
     * @author HuangHao
     * @CreatTime 2020-09-28 16:13
     * @param fissionIds
     * @return java.util.List<com.tuanche.directselling.model.FissionSaleTaskStatistics>
     */
     @Override
     public List<FissionSaleTaskStatistics> statisticsSaleTaskIntegrals(List<Integer> fissionIds){
         if(fissionIds == null || fissionIds.size() < 1){
             return null;
         }
         return fissionUserEffectiveTaskWriteMapper.statisticsSaleTaskIntegrals(fissionIds);
     }

     /**
      * @description: 分享活动页是否打开过
      * @param effectiveTask
      * @return: java.lang.Boolean
      * @author: dudg
      * @date: 2020/11/30 17:49
     */
     public Boolean shareAcitvityOpened(FissionUserEffectiveTask effectiveTask){
         Integer openCount = fissionUserEffectiveTaskReadMapper.selectShareTaskCount(effectiveTask);
         return openCount > 0;
     }
}
