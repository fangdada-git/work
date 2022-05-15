package com.tuanche.directselling.job;

import com.tuanche.directselling.api.AfterSaleActivityService;
import com.tuanche.directselling.dto.AfterSaleActivityDto;
import com.tuanche.directselling.dto.AfterSaleOrderRecordDto;
import com.tuanche.directselling.service.impl.AfterSaleOrderProfitSharingServiceImpl;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.util.ConstantsUtil;
import com.tuanche.directselling.utils.DateUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 售后特卖-分账定时任务
 * @author HuangHao
 * @CreatTime 2020-10-13 16:04
 * @param
 * @return
 */
@JobHandler(value="AfterSaleOrderProfitSharingJob")
@Component
public class AfterSaleOrderProfitSharingJob extends IJobHandler {

    @Autowired
    AfterSaleActivityStatisticsJob afterSaleActivityStatisticsJob;
    @Autowired
    AfterSaleActivityService afterSaleActivityService;
    @Autowired
    AfterSaleOrderProfitSharingServiceImpl afterSaleOrderProfitSharingServiceImpl;

    public static String LOG_KEY_WORD = "售后特卖-分账定时任务";

    @Override
    public ReturnT<String> execute(String pram) throws Exception {
        CommonLogUtil.addInfo(null, LOG_KEY_WORD+"，参数："+pram);
        try {
            AfterSaleOrderRecordDto afterSaleOrderRecordDto = new AfterSaleOrderRecordDto();
            List<Integer> activityIds = null;
            String orderCode = null;
            if(!StringUtils.isEmpty(pram) && pram.indexOf(",")==-1 && pram.length()>15){
                orderCode=pram;
            }else{
                activityIds = getActivityIds(pram);
            }
            CommonLogUtil.addInfo(null, LOG_KEY_WORD +"-活动ID：",activityIds);
            if(!CollectionUtils.isEmpty(activityIds) ){
                afterSaleOrderRecordDto.setActivityIds(activityIds);
            }
            if(!StringUtils.isEmpty(orderCode)){
                afterSaleOrderRecordDto.setOrderCode(orderCode);
            }
            String date = ConstantsUtil.getMinuteDateStr(-2, "yyyy-MM-dd HH:mm:00");
            afterSaleOrderRecordDto.setPayTime(DateUtil.parseDate(date, "yyyy-MM-dd HH:mm:00"));
            afterSaleOrderProfitSharingServiceImpl.distributeMoney(afterSaleOrderRecordDto);
        } catch (Exception e) {
            e.printStackTrace();
            return FAIL;
        }
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
            AfterSaleActivityDto afterSaleActivityDto = new AfterSaleActivityDto();
            afterSaleActivityDto.setSaleStartTimeStr(ConstantsUtil.getMinuteDateStr(minute, "yyyy-MM-dd HH:mm:00"));
            afterSaleActivityDto.setOnSubAccount(1);
            activityIds = afterSaleActivityService.getOngoingActivityIds(afterSaleActivityDto);
        }
        return activityIds;
    }
}
