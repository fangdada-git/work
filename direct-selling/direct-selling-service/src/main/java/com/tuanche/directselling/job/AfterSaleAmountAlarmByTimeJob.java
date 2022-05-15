package com.tuanche.directselling.job;

import com.tuanche.directselling.service.impl.AfterSaleRewardRecordServiceImpl;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
  * @description : 定时统计售后特卖已发放红包(十分钟跨度，取整十，eg:10:00-20:00, 20:00-30:00)
  * @author : fxq
  * @param :
  * @return :
  * @date : 2021/11/23 18:54
  */
@JobHandler(value="afterSaleAmountAlarmByTimeJob")
@Component
public class AfterSaleAmountAlarmByTimeJob  extends IJobHandler {

    @Autowired
    private AfterSaleRewardRecordServiceImpl afterSaleRewardRecordServiceImpl;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        afterSaleRewardRecordServiceImpl.getamountAlarmByTime(s);
        return SUCCESS;
    }
}
