package com.tuanche.directselling.job;

import com.tuanche.directselling.service.impl.AfterSaleOrderRecordServiceImpl;
import com.tuanche.directselling.util.CommonLogUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 *
 * @author HuangHao
 * @CreatTime 2020-10-13 16:04
 * @param
 * @return
 */
@JobHandler(value="afterSaleActivityUnfinishedTaskReminderJob")
@Component
public class AfterSaleActivityUnfinishedTaskReminderJob extends IJobHandler {

    @Autowired
    AfterSaleOrderRecordServiceImpl afterSaleOrderRecordServiceImpl;

    public static String LOG_KEY_WORD = "售后特卖-未完任务的备案用户提醒";

    @Override
    public ReturnT<String> execute(String pram) throws Exception {
        CommonLogUtil.addInfo(null, LOG_KEY_WORD +"，参数："+pram);
        Integer day = 0;
        if(!StringUtils.isEmpty(pram)){
            day = Integer.valueOf(pram);
        }
        //未完任务的备案用户提醒
        afterSaleOrderRecordServiceImpl.unfinishedTaskReminder(day);
        CommonLogUtil.addInfo(null, LOG_KEY_WORD +"，执行完成");
        //定时器执行调用的方法
        return SUCCESS;
    }

}
