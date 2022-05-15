package com.tuanche.directselling.job;

import com.tuanche.directselling.api.FissionActivityService;
import com.tuanche.directselling.api.FissionSaleService;
import com.tuanche.directselling.service.impl.FissionSaleTaskStatisticsServiceImpl;
import com.tuanche.directselling.util.CommonLogUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 裂变活动销售任务统计+预计收入统计
 * @author HuangHao
 * @CreatTime 2020-10-13 17:47
 * @param null
 * @return
 */
@JobHandler(value="fissionSaleTaskStatisticsJob")
@Component
public class FissionSaleTaskStatisticsJobHandler extends IJobHandler {

    @Autowired
    FissionSaleTaskStatisticsServiceImpl fissionSaleTaskStatisticsServiceImpl;
    @Autowired
    FissionActivityService fissionActivityService;
    @Autowired
    FissionSaleService fissionSaleService;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        CommonLogUtil.addInfo(null, FissionSaleTaskStatisticsServiceImpl.LOG_KEW_WORD+"定时任务开始");
        //正在进行中且是确认(开启)状态的裂变活动
        List<Integer> fissionIds = fissionActivityService.getOngoingAndConfirmIds();
        //销售积分计算
        fissionSaleTaskStatisticsServiceImpl.salesTaskStatistics(fissionIds);
        //销售预计收入计算
        fissionSaleService.estimatedIncome(fissionIds);
        CommonLogUtil.addInfo(null, FissionSaleTaskStatisticsServiceImpl.LOG_KEW_WORD+"定时任务结束");
        //定时器执行调用的方法
        return SUCCESS;
    }
}
