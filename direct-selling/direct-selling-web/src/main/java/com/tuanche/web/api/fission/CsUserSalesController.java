package com.tuanche.web.api.fission;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.eap.api.utils.ResultDto;
import com.tuanche.framework.base.util.JsonUtil;
import com.tuanche.manubasecenter.api.SmsVerifyCodeService;
import com.tuanche.manubasecenter.constant.ManuBaseConstants;
import com.tuanche.manubasecenter.dto.TcResponse;
import com.tuanche.manubasecenter.dto.TcResponseCode;
import com.tuanche.manubasecenter.dto.VerificationCodeDto;
import com.tuanche.manubasecenter.model.CsUser;
import com.tuanche.manubasecenter.util.ManeBaseConsoleConstants;
import com.tuanche.web.service.FissionLoginServiceImpl;
import com.tuanche.web.service.RandomValidateCodeService;
import com.tuanche.web.util.DirectCommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: manu_service_api
 * @description: 裂变-经销商员工相关操作
 * @author: fxq
 * @create: 2020-09-23 18:24
 **/
@Controller
@RequestMapping("/api/fission/manu/sales")
public class CsUserSalesController {

    @Reference
    private SmsVerifyCodeService smsVerifyCodeService;
    @Autowired
    private FissionLoginServiceImpl fissionLoginServiceImpl;
    @Autowired
    private RandomValidateCodeService randomValidateCodeService;
    @Autowired
    @Qualifier("ClusterRedisService")
    private RedisService redisService;
    
    /**
     * @description: 店员发送验证码
     * @param:
     * @return:
     * @author: fxq
     * @date: 2020/9/24 10:26
     */
    @RequestMapping("sendVerifyCode")
    @ResponseBody
    public TcResponse sendVerifyCode (String userPhone, Integer cityId) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "CsUserSalesController","sendVerifyCode",  "店员发送验证码 start " +st, userPhone);
        int code = StatusCodeEnum.SUCCESS.getCode();
        String msg = StatusCodeEnum.SUCCESS.getText();
        try {
            if (StringUtils.isEmpty(userPhone.trim()) || !ManeBaseConsoleConstants.isMobile(userPhone.trim())) {
                return DirectCommonUtil.setTcResponse(TcResponseCode.PARAM_INVALID.getIndex(), "手机号不合法", st, 0);
            }
            VerificationCodeDto codeDto = new VerificationCodeDto();
            codeDto.setPhones(userPhone);
            codeDto.setCityId(cityId==null ? 10 : cityId);
            String result = smsVerifyCodeService.sendVerifyCode(codeDto);
            if (StringUtils.isEmpty(result)) {
                return DirectCommonUtil.setTcResponseAndLog(TcResponseCode.RESPONSE.getIndex(), "验证码发送失败", st,
                        "CsUserSalesController", "sendVerifyCode", userPhone);
            }
            ResultDto resultDto = JsonUtil.json2Entity(result, ResultDto.class);
            if (!resultDto.getCode().equals(ManuBaseConstants.RESULT_CODE_10000)) {
                return DirectCommonUtil.setTcResponseAndLog(TcResponseCode.RESPONSE.getIndex(), resultDto.getMsg(), st,
                        "CsUserSalesController", "sendVerifyCode", userPhone);
            }
        }catch (Exception e) {
            return DirectCommonUtil.addErrorLog("CsUserSalesController", "销售发送验证码 error", e, st,userPhone);
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "CsUserSalesController","sendVerifyCode",  "店员发送验证码 end " +st, userPhone);
        return DirectCommonUtil.setTcResponse(code, msg, st, 0);
    }

    /**
     * @description: 店员登陆
     * @param:
     * @return:
     * @author: fxq
     * @date: 2020/9/24 11:04
     */
    @RequestMapping("userLogin")
    @ResponseBody
    public TcResponse userLogin (String userPhone, String verifyCode, String picVerifyCode) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "CsUserSalesController","userLogin",  "店员登陆 start " +st, userPhone+"-"+verifyCode+"-"+picVerifyCode);
        int code = TcResponseCode.OK.getIndex();
        String msg = TcResponseCode.OK.getName();
        Map<String, String> map = new HashMap<>();
        try {
            if (!ManeBaseConsoleConstants.isMobile(userPhone.trim())) {
                return DirectCommonUtil.setTcResponse(TcResponseCode.PARAM_INVALID.getIndex(), "手机号错误", st, 0);
            }
            if (StringUtils.isEmpty(verifyCode)) {
                return DirectCommonUtil.setTcResponse(StatusCodeEnum.PHONE_VERIFY_CODE_ERROR.getCode(), "手机验证码错误", st, 0);
            }
            Integer verifyNum = redisService.get(DirectCommonUtil.FISSION_SALE_LOGIN_VERIFY_NUM + userPhone, Integer.class);
            if (verifyNum!=null && verifyNum>=3) {
                TcResponse result = checkPicVerifyCode(userPhone, picVerifyCode);
                if (StatusCodeEnum.SUCCESS.getCode() !=result.getResponseHeader().getStatus()) {
                    result.getResponse().setResult(verifyNum);
                    return result;
                } else {
                    redisService.del(DirectCommonUtil.FISSION_SALE_LOGIN_VERIFY_CODE + userPhone.trim());
                }
            }
            boolean bo = smsVerifyCodeService.checkVerifyCode(userPhone, verifyCode);
            if (!bo) {
                long num = redisService.incr(DirectCommonUtil.FISSION_SALE_LOGIN_VERIFY_NUM + userPhone);
                redisService.expire(DirectCommonUtil.FISSION_SALE_LOGIN_VERIFY_NUM + userPhone, 4*60*60*1000);
                map.put("verifyNum", num+"");
                return DirectCommonUtil.setTcResponseAndLogReturnResult(StatusCodeEnum.PHONE_VERIFY_CODE_ERROR.getCode(), "手机验证码错误", st,
                        "CsUserSalesController", "userLogin", userPhone+"-"+verifyCode, map);
            }
            ResultDto resultDto = fissionLoginServiceImpl.userLogin(userPhone);
            if (!resultDto.getCode().equals(StatusCodeEnum.SUCCESS.getCode())) {
                return DirectCommonUtil.setTcResponseAndLog(resultDto.getCode(), resultDto.getMsg(), st,
                        "CsUserSalesController", "userLogin", userPhone+"-"+verifyCode);
            }
            CsUser user = (CsUser) resultDto.getResult();
            fissionLoginServiceImpl.addLoginUserToRedis(user);
            String uuid = fissionLoginServiceImpl.addLoginUserTokenToRedis(user);
            if (StringUtils.isEmpty(uuid)) {
                return DirectCommonUtil.setTcResponseAndLog(TcResponseCode.PARAM_INVALID.getIndex(), "登陆token异常", st,
                        "CsUserSalesController", "userLogin", userPhone+"-"+verifyCode);
            }
            map.put(DirectCommonUtil.FISSION_SALE_LOGIN_TOKEN, uuid);
            map.put("ulevel", user.getUlevel()+"");
        }catch (Exception e) {
            return DirectCommonUtil.addErrorLog("CsUserSalesController", "店员登陆 error", e, st,userPhone+"-"+verifyCode);
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "CsUserSalesController","userLogin",  "店员登陆 end " +st, JSON.toJSONString(map));
        return DirectCommonUtil.setTcResponse(code, msg, st, map);
    }
    
    /**
     * @description: 店员登出
     * @param:
     * @return:
     * @author: fxq
     * @date: 2020/9/24 20:26
     */
    @RequestMapping("userLoginOut")
    @ResponseBody
    public TcResponse userLoginOut (HttpServletRequest request) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "CsUserSalesController","userLoginOut",  "店员登陆 start " +st, "");
        int code = TcResponseCode.OK.getIndex();
        String msg = TcResponseCode.OK.getName();
        try {
            fissionLoginServiceImpl.delLoginUser(request);
        }catch (Exception e) {
            return DirectCommonUtil.addErrorLog("CsUserSalesController", "店员登出 error", e, st,"");
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "CsUserSalesController","userLoginOut",  "店员登陆 end " +st, "");
        return DirectCommonUtil.setTcResponse(code, msg, st, 0);
    }

    //获取图片验证码
    @RequestMapping("/getLoginPicVerifyCode")
    @ResponseBody
    public String getLoginPicVerifyCode(HttpServletResponse response,
                                        HttpServletRequest request, String userPhone) {
        if (!ManeBaseConsoleConstants.isMobile(userPhone.trim())) {
            return "";
        }
        response.setContentType("image/jpeg");// 设置相应类型,告诉浏览器输出的内容为图片
        response.setHeader("Pragma", "No-cache");// 设置响应头信息，告诉浏览器不要缓存此内容
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Set-Cookie", "name=value; HttpOnly");//设置HttpOnly属性,防止Xss攻击
        response.setDateHeader("Expire", 0);
        try {
            randomValidateCodeService.getRandcode(request, response,userPhone.trim());// 输出图片方法
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    //验证图片验证码
    @RequestMapping(value = "/checkPicVerifyCode")
    @ResponseBody
    public TcResponse checkPicVerifyCode(String userPhone, String picVerifyCode) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "CsUserSalesController","checkPicVerifyCode",  "验证图片验证码 start " +st, userPhone+"-"+picVerifyCode);
        if (!ManeBaseConsoleConstants.isMobile(userPhone.trim())) {
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "手机号错误", st, 0);
        }
        if (StringUtils.isEmpty(picVerifyCode) || picVerifyCode.length()!=4) {
            return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.PIC_VERIFY_CODE_ERROR.getCode(), StringUtils.isEmpty(picVerifyCode) ? "请输入图形验证码": "图片验证码错误", st,"CsUserSalesController", "checkPicVerifyCode", picVerifyCode);
        }
        try {
            //1:获取redis里面的验证码信息
            String redisPicVerifyCode =  redisService.get(DirectCommonUtil.FISSION_SALE_LOGIN_VERIFY_CODE + userPhone.trim(),String.class);
            if (StringUtils.isEmpty(redisPicVerifyCode)) {
                return DirectCommonUtil.setTcResponse(StatusCodeEnum.PIC_VERIFY_CODE_ERROR.getCode(), "验证码失效，请重新获取", st, 0);
            }
            if (!picVerifyCode.toLowerCase().equals(redisPicVerifyCode.toLowerCase())) {
                redisService.del(DirectCommonUtil.FISSION_SALE_LOGIN_VERIFY_CODE + userPhone.trim());
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.PIC_VERIFY_CODE_ERROR.getCode(), "图片验证码错误", st,"CsUserSalesController", "checkPicVerifyCode", picVerifyCode.toLowerCase()+"--"+redisPicVerifyCode.toLowerCase());
            }
        } catch (Exception e) {
            return DirectCommonUtil.addErrorLog("CsUserSalesController", "验证图片验证码 error", e, st,userPhone);
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "CsUserSalesController","checkPicVerifyCode",  "验证图片验证码 end " +st, userPhone+"-"+picVerifyCode);
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, 0);
    }
}
