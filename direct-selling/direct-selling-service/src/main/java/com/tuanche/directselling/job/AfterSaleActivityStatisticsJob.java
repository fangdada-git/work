package com.tuanche.directselling.job;

import com.tuanche.directselling.api.AfterSaleActivityService;
import com.tuanche.directselling.service.impl.AfterSaleActivityStatisticsServiceImpl;
import com.tuanche.directselling.service.impl.AfterSaleUserRewardStatisticsServiceImpl;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.util.ConstantsUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 售后特卖活动统计任务
 * @author HuangHao
 * @CreatTime 2020-10-13 16:04
 * @param
 * @return
 */
@JobHandler(value="afterSaleActivityStatisticsJob")
@Component
public class AfterSaleActivityStatisticsJob extends IJobHandler {

    @Autowired
    AfterSaleUserRewardStatisticsServiceImpl afterSaleUserRewardStatisticsServiceImpl;
    @Autowired
    AfterSaleActivityService afterSaleActivityService;
    @Autowired
    AfterSaleActivityStatisticsServiceImpl afterSaleActivityStatisticsServiceImpl;

    public static String LOG_KEY_WORD = "售后特卖活动统计定时任务";

    @Override
    public ReturnT<String> execute(String pram) throws Exception {
        CommonLogUtil.addInfo(null, LOG_KEY_WORD +"，参数："+pram);
        //获取前一天结束的裂变活动ID
        List<Integer> activityIds = getActivityIds(pram);
        if(activityIds == null || activityIds.size() == 0){
            CommonLogUtil.addInfo(null, LOG_KEY_WORD +"-失败，原因：活动ID（activityIds）为空");
            return SUCCESS;
        }
        //用户奖励统计
        afterSaleUserRewardStatisticsServiceImpl.userRewardStatistics(activityIds);
        //活动奖励统计
        afterSaleActivityStatisticsServiceImpl.activityStatistics(activityIds);
        //活动渠道奖励统计
        afterSaleActivityStatisticsServiceImpl.activityChannelStatistics(activityIds);
        CommonLogUtil.addInfo(null, LOG_KEY_WORD +"，执行完成");
        //定时器执行调用的方法
        return SUCCESS;
    }

    /**
     * 根据参数获取活动
     * @author HuangHao
     * @CreatTime 2021-10-28 18:10
     * @param pram
     * @return java.util.List<java.lang.Integer>
     */
    public List<Integer> getActivityIds(String pram){
        //获取前一天结束的裂变活动ID
        List<Integer> activityIds = new ArrayList<>();
        Integer minute=null;
        if(!StringUtils.isEmpty(pram)){
            String[] ids = pram.split(",");
            for (String id : ids) {
                if(id.matches("^[0-9]*$")){
                    activityIds.add(Integer.valueOf(id));
                }else{
                    activityIds=null;
                    minute=Integer.valueOf(id);
                    break;
                }
            }
        }
        if(minute!=null){
            //获minute分钟前的裂变活动ID
            activityIds = afterSaleActivityService.getOngoingActivityIds(ConstantsUtil.getMinuteDateStr(minute, "yyyy-MM-dd HH:mm:00"));
        }
        return activityIds;
    }
}
