package com.tuanche.web.api.fission;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.framework.base.util.JsonUtil;
import com.tuanche.manubasecenter.api.SmsVerifyCodeService;
import com.tuanche.manubasecenter.constant.ManuBaseConstants;
import com.tuanche.manubasecenter.dto.TcResponse;
import com.tuanche.manubasecenter.dto.TcResponseCode;
import com.tuanche.manubasecenter.dto.VerificationCodeDto;
import com.tuanche.manubasecenter.util.ManeBaseConsoleConstants;
import com.tuanche.web.util.DirectCommonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/fission/manu/user")
public class FissionUserController {

    @Reference
    private SmsVerifyCodeService smsVerifyCodeService;

    /**
     * @description: 用户发送验证码
     * @param:
     * @return:
     * @author: fxq
     * @date: 2020/9/24 10:26
     */
    @RequestMapping("sendVerifyCode")
    @ResponseBody
    public TcResponse sendVerifyCode (String userPhone, Integer cityId) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionUserController","sendVerifyCode",  "用户发送验证码 start " +st, userPhone);
        int code = TcResponseCode.OK.getIndex();
        String msg = TcResponseCode.OK.getName();
        try {
            if (StringUtils.isEmpty(userPhone.trim()) || !ManeBaseConsoleConstants.isMobile(userPhone.trim())) {
                return DirectCommonUtil.setTcResponse(TcResponseCode.PARAM_INVALID.getIndex(), "请填写正确的手机号", st, 0);
            }
            VerificationCodeDto codeDto = new VerificationCodeDto();
            codeDto.setPhones(userPhone);
            codeDto.setCityId(cityId==null ? 10 : cityId);
            String result = smsVerifyCodeService.sendVerifyCode(codeDto);
            if (StringUtils.isEmpty(result)) {
                return DirectCommonUtil.setTcResponseAndLog(TcResponseCode.RESPONSE.getIndex(), "验证码发送失败", st,
                        "FissionUserController", "sendVerifyCode", userPhone);
            }
            com.tuanche.commons.util.ResultDto resultDto = JsonUtil.json2Entity(result, com.tuanche.commons.util.ResultDto.class);
            if (!resultDto.getCode().equals(ManuBaseConstants.RESULT_CODE_10000)) {
                return DirectCommonUtil.setTcResponseAndLog(TcResponseCode.RESPONSE.getIndex(), resultDto.getMsg(), st,
                        "FissionUserController", "sendVerifyCode", userPhone);
            }
        }catch (Exception e) {
            return DirectCommonUtil.addErrorLog("FissionUserController", "用户发送验证码 error", e, st,userPhone);
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionUserController","sendVerifyCode",  "用户发送验证码 end " +st, userPhone);
        return DirectCommonUtil.setTcResponse(code, msg, st, 0);
    }
    /**
     * @description: 用户验证验证码
     * @param:
     * @return:
     * @author: fxq
     * @date: 2020/9/24 10:26
     */
    @RequestMapping("checkVerifyCode")
    @ResponseBody
    public TcResponse checkVerifyCode (String userPhone, String verifyCode) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionUserController","checkVerifyCode",  "用户验证验证码 start " +st, userPhone+"-"+verifyCode);
        int code = TcResponseCode.OK.getIndex();
        String msg = TcResponseCode.OK.getName();
        try {
            if (!ManeBaseConsoleConstants.isMobile(userPhone.trim()) || StringUtils.isEmpty(verifyCode)) {
                return DirectCommonUtil.setTcResponse(TcResponseCode.PARAM_INVALID.getIndex(), "参数错误", st, 0);
            }
            boolean bo = smsVerifyCodeService.checkVerifyCode(userPhone, verifyCode);
            if (!bo) {
                return DirectCommonUtil.setTcResponseAndLog(TcResponseCode.RESPONSE.getIndex(), "验证码错误", st,
                        "FissionUserController", "checkVerifyCode", userPhone+"-"+verifyCode);
            }
        }catch (Exception e) {
            return DirectCommonUtil.addErrorLog("FissionUserController", "用户验证验证码 error", e, st,userPhone);
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionUserController","checkVerifyCode",  "用户验证验证码 end " +st, userPhone+"-"+verifyCode);
        return DirectCommonUtil.setTcResponse(code, msg, st, 0);
    }
    
}
