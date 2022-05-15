package com.tuanche.directselling.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.directselling.model.AfterSaleAmountAlarm;
import com.tuanche.directselling.service.impl.AfterSaleAmountAlarmServiceImpl;
import com.tuanche.manubasecenter.api.SmsVerifyCodeService;
import com.tuanche.manubasecenter.dto.SmsDto;
import com.tuanche.manubasecenter.util.EmailUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
  * @description : 售后特卖业务余额预警
  * @author : fxq
  * @param :
  * @return :
  * @date : 2021/11/23 18:54
  */
@JobHandler(value="afterSaleAmountAlarmSendSmsAndEmailJob")
@Component
public class AfterSaleAmountAlarmSendSmsAndEmailJob extends IJobHandler {

    @Autowired
    private AfterSaleAmountAlarmServiceImpl afterSaleAmountAlarmServiceImpl;
    @Reference
    private SmsVerifyCodeService smsVerifyCodeService;

    private String sms_system_code;
    @Value("${sms_business_code}")
    private String sms_business_code;
    @Value("${sms_business_template_notify}")
    private Integer sms_business_template_notify;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        AfterSaleAmountAlarm afterSaleAmountAlarm = afterSaleAmountAlarmServiceImpl.getAfterSaleAmountAlarm();
        if (afterSaleAmountAlarm !=null && afterSaleAmountAlarm.getAlarmAmount()!=null && afterSaleAmountAlarm.getAmount().compareTo(afterSaleAmountAlarm.getAlarmAmount())<1) {
            //邮件提醒
            if (StringUtils.isNotEmpty(afterSaleAmountAlarm.getAlarmEmail()) && afterSaleAmountAlarm.getAlarmEmail().split(",").length>0) {
                EmailUtil.send("聚力保客余额不足提醒", Arrays.asList(afterSaleAmountAlarm.getAlarmEmail().split(",")), "聚力保客业务余额为"+afterSaleAmountAlarm.getAmount()+"，已达到预警值，请尽快充值");
            }
            //短信提醒
            if (StringUtils.isNotEmpty(afterSaleAmountAlarm.getAlarmPhone()) && afterSaleAmountAlarm.getAlarmEmail().split(",").length>0) {
                SmsDto smsDto = new SmsDto();
                smsDto.setCityId(10);
                smsDto.setSystemCode(sms_system_code);
                smsDto.setBusinessCode(sms_business_code);
                smsDto.setPhones(StringUtils.join(afterSaleAmountAlarm.getAlarmPhone().split(","), ","));
                smsDto.setTemplateId(sms_business_template_notify);
                smsDto.setParamArray(new String[]{"聚力保客业务余额"+afterSaleAmountAlarm.getAmount()+"，已达到预警值，请尽快充值"});
                ResultDto resultDto = smsVerifyCodeService.sendSmsByEmptyTemplant(smsDto);
            }
        }
        return SUCCESS;
    }
}
