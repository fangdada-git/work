package com.tuanche.directselling.job;

import com.tuanche.directselling.service.impl.AfterSaleRewardRecordServiceImpl;
import com.tuanche.directselling.util.CommonLogUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 售后特卖余额预警-每天定时预警任务
 * @author HuangHao
 * @CreatTime 2020-10-13 16:04
 * @param
 * @return
 */
@JobHandler(value="afterSaleAmountAlarmTimingJob")
@Component
public class AfterSaleAmountAlarmTimingJob extends IJobHandler {

    @Autowired
    AfterSaleRewardRecordServiceImpl afterSaleRewardRecordServiceImpl;

    @Override
    public ReturnT<String> execute(String pram) throws Exception {
        CommonLogUtil.addInfo(null, "售后特卖余额预警-每天定时预警任务，参数："+pram);
        afterSaleRewardRecordServiceImpl.amountAlarm(false);
        //定时器执行调用的方法
        return SUCCESS;
    }

}
