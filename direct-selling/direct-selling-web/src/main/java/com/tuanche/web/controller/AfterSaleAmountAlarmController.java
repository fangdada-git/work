package com.tuanche.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.tuanche.directselling.api.AfterSaleAmountAlarmService;
import com.tuanche.directselling.api.AfterSaleAmountRechargeRecordService;
import com.tuanche.directselling.model.AfterSaleAmountAlarm;
import com.tuanche.directselling.model.AfterSaleAmountRechargeRecord;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.eap.api.utils.ResultDto;
import com.tuanche.inner.sso.core.conf.Conf;
import com.tuanche.inner.sso.core.user.XxlUser;
import com.tuanche.web.util.CommonLogUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@Controller
@RequestMapping("/afterSale/alarm")
public class AfterSaleAmountAlarmController {

    @Reference
    private AfterSaleAmountAlarmService afterSaleAmountAlarmService;
    @Reference
    private AfterSaleAmountRechargeRecordService afterSaleAmountRechargeRecordService;

    @RequestMapping("toAlarmPage")
    public String toAlarmPage(ModelMap modelMap) {
        AfterSaleAmountAlarm amountAlarm = afterSaleAmountAlarmService.getAfterSaleAmountAlarm();
        if (amountAlarm==null) {
            amountAlarm= new AfterSaleAmountAlarm();
        }
        modelMap.addAttribute("amountAlarm",amountAlarm);
        return "after_sale/activity/alarm_page";
    }

    /**
      * @description : 编辑预警
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/11/23 15:39
      */
    @RequestMapping("edit")
    @ResponseBody
    public ResultDto edit(HttpServletRequest request, AfterSaleAmountAlarm amountAlarm) {
        ResultDto dto = new ResultDto();
        dto.setCode(StatusCodeEnum.SUCCESS.getCode());
        CommonLogUtil.addInfo("编辑预警", "start：", JSON.toJSONString(amountAlarm));
        try {
            XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
            AfterSaleAmountAlarm afterSaleAmountAlarm = afterSaleAmountAlarmService.getAfterSaleAmountAlarm();
            if (afterSaleAmountAlarm!=null) {
                BigDecimal accruingAmounts = afterSaleAmountAlarm.getAccruingAmounts().add(amountAlarm.getRechargeAmount());
                amountAlarm.setAccruingAmounts(accruingAmounts);
                amountAlarm.setPaymentAmount(afterSaleAmountAlarm.getPaymentAmount());
            } else {
                amountAlarm.setAccruingAmounts(amountAlarm.getRechargeAmount());
            }
            AfterSaleAmountRechargeRecord amountRechargeRecord = new AfterSaleAmountRechargeRecord();
            amountRechargeRecord.setAccruingAmounts(amountAlarm.getAccruingAmounts());
            amountRechargeRecord.setRechargeAmount(amountAlarm.getRechargeAmount());
            amountRechargeRecord.setCreateBy(xxlUser.getId());
            afterSaleAmountRechargeRecordService.addAfterSaleAmountRechargeRecord(amountRechargeRecord);
            if (amountAlarm.getId()==null) {
                afterSaleAmountAlarmService.addAfterSaleAmountAlarm(amountAlarm);
            } else {

                afterSaleAmountAlarmService.updateAfterSaleAmountAlarm(amountAlarm);
            }
        }catch (Exception e) {
            dto.setCode(StatusCodeEnum.ERROR.getCode());
            dto.setMsg(StatusCodeEnum.ERROR.getText());
            CommonLogUtil.addError("AfterSaleAmountAlarmController","编辑预警error", e);
        }
        return dto;
    }


}
