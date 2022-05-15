package com.tuanche.directselling.job;

import com.tuanche.directselling.service.impl.AfterSaleRewardRecordServiceImpl;
import com.tuanche.directselling.service.impl.FissionActivityServiceImpl;
import com.tuanche.directselling.service.impl.FissionSaleServiceImpl;
import com.tuanche.directselling.service.impl.FissionSaleTaskStatisticsServiceImpl;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.framework.base.util.DateUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 售后特卖余额预警-第一次预警任务
 * @author HuangHao
 * @CreatTime 2020-10-13 16:04
 * @param
 * @return
 */
@JobHandler(value="afterSaleAmountAlarmFirstJob")
@Component
public class AfterSaleAmountAlarmFirstJob extends IJobHandler {

    @Autowired
    AfterSaleRewardRecordServiceImpl afterSaleRewardRecordServiceImpl;

    @Override
    public ReturnT<String> execute(String pram) throws Exception {
        CommonLogUtil.addInfo(null, "售后特卖余额预警-第一次预警任务，参数："+pram);
        afterSaleRewardRecordServiceImpl.amountAlarm(true);
        //定时器执行调用的方法
        return SUCCESS;
    }

}
