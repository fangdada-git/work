package com.tuanche.directselling.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.directselling.api.FissionActivityService;
import com.tuanche.directselling.util.CommonLogUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @class: SyncFissionActivityEffectNumHandler
 * @description: 同步裂变活动效果数
 * @author: dudg
 * @create: 2020-10-19 17:53
 */

@JobHandler(value = "syncFissionActivityEffectNumHandler")
@Component
public class SyncFissionActivityEffectNumHandler extends IJobHandler {

    @Autowired
    FissionActivityService fissionActivityService;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        String logKey = "同步裂变活动效果数 xxl-job：";
        CommonLogUtil.addInfo(logKey,"开始执行");
        try {
            fissionActivityService.syncActivityEffectNum();
        } catch (Exception e) {
            e.printStackTrace();
            CommonLogUtil.addError(logKey+"发生异常：", e.getMessage(), e);
        }

        CommonLogUtil.addInfo(logKey,"执行结束");
        return SUCCESS;
    }
}
