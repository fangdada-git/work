package com.tuanche.directselling.job;

import com.tuanche.directselling.api.AfterSaleRewardRecordService;
import com.tuanche.directselling.service.impl.AfterSaleRewardRecordServiceImpl;
import com.tuanche.directselling.util.CommonLogUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 售后特卖奖励补发任务
 * @author HuangHao
 * @CreatTime 2020-10-13 16:04
 * @param
 * @return
 */
@JobHandler(value="afterSaleRewardRecordReissueJob")
@Component
public class AfterSaleRewardRecordReissueJob extends IJobHandler {

    @Autowired
    AfterSaleRewardRecordService afterSaleRewardRecordService;
    @Autowired
    AfterSaleActivityStatisticsJob afterSaleActivityStatisticsJob;

    public static String LOG_KEY_WORD = "售后特卖活动奖励补发定时任务";

    @Override
    public ReturnT<String> execute(String pram) throws Exception {
        CommonLogUtil.addInfo(null, "售后特卖奖励补发任务，参数："+pram);
        List<Integer> activityIds = afterSaleActivityStatisticsJob.getActivityIds(pram);
        if(activityIds == null || activityIds.size() == 0){
            CommonLogUtil.addInfo(null, LOG_KEY_WORD +"-失败，原因：活动ID（activityIds）为空");
            return SUCCESS;
        }
        CommonLogUtil.addInfo(null, LOG_KEY_WORD +"-补发活动ID：",activityIds);
        for (Integer activityId : activityIds) {
            afterSaleRewardRecordService.reissueReward(activityId);
        }
        //定时器执行调用的方法
        return SUCCESS;
    }

}
