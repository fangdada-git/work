package com.tuanche.directselling.job;

import com.tuanche.directselling.api.FissionActivityService;
import com.tuanche.directselling.api.FissionSaleService;
import com.tuanche.directselling.service.impl.FissionActivityServiceImpl;
import com.tuanche.directselling.service.impl.FissionSaleServiceImpl;
import com.tuanche.directselling.service.impl.FissionSaleTaskStatisticsServiceImpl;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.util.ConstantsUtil;
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
 * 裂变活动销售实际收入统计
 * @author HuangHao
 * @CreatTime 2020-10-13 16:04
 * @param
 * @return
 */
@JobHandler(value="fissionSaleRealIncomeJob")
@Component
public class FissionSaleRealIncomeJobHandler extends IJobHandler {

    @Autowired
    FissionSaleTaskStatisticsServiceImpl fissionSaleTaskStatisticsServiceImpl;
    @Autowired
    FissionActivityServiceImpl fissionActivityServiceImpl;
    @Autowired
    FissionSaleServiceImpl fissionSaleServiceImpl;

    @Override
    public ReturnT<String> execute(String pram) throws Exception {
        CommonLogUtil.addInfo(null, FissionSaleTaskStatisticsServiceImpl.LOG_KEW_WORD+"实际收入计算，定时任务开始，参数："+pram);
        //获取前一天结束的裂变活动ID
        List<Integer> fissionIds = new ArrayList<>();
        if(!StringUtils.isEmpty(pram)){
            if(pram.length()>10 && DateUtil.isDate(pram.split(" ")[0])){
                //获取前一天结束的裂变活动ID
                fissionIds = fissionActivityServiceImpl.getEndAndConfirmActivityIds(pram);
            }else{
                String[] ids = pram.split(",");
                for (String id : ids) {
                    if(pram.matches("^[0-9]*[1-9][0-9]*$")){
                        fissionIds.add(Integer.valueOf(id));
                    }
                }
            }
        }else{
            //获20分钟前的裂变活动ID
            fissionIds = fissionActivityServiceImpl.getEndAndConfirmActivityIds(ConstantsUtil.getMinuteDateStr(-20, "yyyy-MM-dd HH:mm:00"));
        }

        //前一天结束的
//        List<Integer> fissionIds = fissionActivityService.getEndedYesterdayAndConfirmIds();
        if(fissionIds == null || fissionIds.size() == 0){
            CommonLogUtil.addInfo(null, FissionSaleTaskStatisticsServiceImpl.LOG_KEW_WORD+"实际收入计算-失败，原因：fissionIds为空");
            return SUCCESS;
        }
        //销售积分计算
        fissionSaleTaskStatisticsServiceImpl.salesTaskStatistics(fissionIds);
        //销售实际收入计算
        fissionSaleServiceImpl.realIncome(fissionIds);
        CommonLogUtil.addInfo(null, FissionSaleTaskStatisticsServiceImpl.LOG_KEW_WORD+"实际收入计算，定时任务结束");
        //定时器执行调用的方法
        return SUCCESS;
    }

}
