package com.tuanche.web.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tuanche.arch.po.MemberPo;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.eap.api.utils.ResultDto;
import com.tuanche.manubasecenter.dto.TcResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @program: direct-selling
 * @description: ${description}
 * @author: fxq
 * @create: 2020-09-24 16:42
 **/
public class DirectCommonUtil {

    //裂变销售登陆信息 + id
    public static final String FISSION_SESSION_SALE_REDIS = "fission:session:sale:id:";
    //裂变销售登陆token + token
    public static final String FISSION_SALE_LOGIN_TOKEN_REDIS = "fission:sale:login:token:";
    //裂变销售登陆验证码 + 手机号
    public static final String FISSION_SALE_LOGIN_VERIFY_CODE = "fission:sale:login:verify:code:";
    //裂变销售登陆验证码错误次数 + 手机号
    public static final String FISSION_SALE_LOGIN_VERIFY_NUM = "fission:sale:login:verify:num:";
    //裂变销售登陆成功返回token key
    public static final String FISSION_SALE_LOGIN_TOKEN = "fissionsalelogintoken";
    //销售支付对赌金 saleid-openid
    public static final String FISSION_SALE_OPENID_REDIS = "fission:sale:openid:id:";
    //用户购买商品-销售任务key
    public static final String FISSION_USER_SALE_TASK_ORDERNO = "fission:user:sale:task:orderno:";

    public static final String SYSTEM_NAME = "DIRECT-SELLING-WEB";

    public static TcResponse setTcResponse (int code, String msg, long st, Object result) {
        return new TcResponse(code,System.currentTimeMillis()-st,msg,result);
    }

    public static TcResponse setTcResponseAndLog (int code, String msg, long st, String clsaaName, String methodName, Object param) {
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, SYSTEM_NAME, clsaaName,methodName, msg, param!=null && !(param instanceof String) ? JSON.toJSONString(param) : param);
        return setTcResponse(code,msg,st,0);
    }
    public static TcResponse setTcResponseAndLogReturnResult (int code, String msg, long st, String clsaaName, String methodName, Object param, Object result) {
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, SYSTEM_NAME, clsaaName,methodName, msg, param!=null && !(param instanceof String) ? JSON.toJSONString(param) : param);
        return setTcResponse(code,msg,st,result);
    }

    /**
     * @description: 写错误日志并且返回TcResponse
     * @param:
     * @return:
     * @author: fxq
     * @date: 2020/9/25 10:54
     */
    public static TcResponse addErrorLog (String clsaaName, String errorMsg, Exception e, long st, String param) {
        StaticLogUtils.error(SystemLogType.LOG_SYS_B, SYSTEM_NAME, clsaaName, errorMsg, param,e);
        return setTcResponse(StatusCodeEnum.ERROR.getCode(), StatusCodeEnum.ERROR.getText(), st, 0);
    }
    /**
      * @description : 返回无数据
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/7/20 21:26
      */
    public static TcResponse returnResultNull (String clsaaName,String methodName,String msg,Object param, long st) {
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, SYSTEM_NAME, clsaaName,methodName, msg, param!=null && !(param instanceof String) ? JSON.toJSONString(param) : param);
        return new TcResponse(StatusCodeEnum.RESULE_DATA_NONE.getCode(),System.currentTimeMillis()-st,StatusCodeEnum.RESULE_DATA_NONE.getText(),0);
    }
    /**
      * @description : 返回参数错误
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/7/20 21:27
      */
    public static TcResponse returnParamError (String clsaaName,String methodName,String msg,Object param, long st) {
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, SYSTEM_NAME, clsaaName,methodName, msg, param!=null && !(param instanceof String) ? JSON.toJSONString(param) : param);
        return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),System.currentTimeMillis()-st,StatusCodeEnum.PARAM_IS_INVALID.getText(),0);
    }

    /**
     * @description: 获取c端登陆用户id
     * @param:
     * @return:
     * @author: fxq
     * @date: 2020/9/25 10:54
     */
    public static Integer getMemberPoId(HttpServletRequest request){
        MemberPo memberPo =  (MemberPo)request.getAttribute("logined_user");
        if(memberPo==null||memberPo.getId()==null){
            return null;
        }else{
            return memberPo.getId();
        }
    }

    /**
     * @description: 获取c端登陆用户
     * @param:
     * @return:
     * @author: fxq
     * @date: 2020/9/25 10:54
     */
    public static MemberPo getMemberPo(HttpServletRequest request){
//        MemberPo memberPo = new MemberPo();
//        memberPo.setId(1);
//        memberPo.setName("111");
//        memberPo.setPhone(Long.valueOf("18310228011"));
//        return memberPo;
        return (MemberPo)request.getAttribute("logined_user");
    }

    /**
     * 发送消息 text/html;charset=utf-8
     * @param response
     * @param str
     * @throws Exception
     */
    public static void sendMessage(HttpServletResponse response, String str) throws Exception {
        response.setContentType("text/html; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(str);
        writer.close();
        response.flushBuffer();
    }

    /**
     * 将某个对象转换成json格式并发送到客户端
     * @param response
     * @param obj
     * @throws Exception
     */
    public static void sendJsonMessage(HttpServletResponse response, Object obj) throws Exception {
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat));
        writer.close();
        response.flushBuffer();
    }

    //后端系统返回
    //系统错误
    public static ResultDto addError(String className, String methodName, String errorLogMsg, Exception e) {
        ResultDto dto = new ResultDto();
        dto.setCode(StatusCodeEnum.ERROR.getCode());
        dto.setMsg(StatusCodeEnum.ERROR.getText());
        CommonLogUtil.addError(className+"-" + methodName, errorLogMsg, e);
        return dto;
    }
    //后端系统返回
    //参数错误
    public static ResultDto addParamNull () {
        ResultDto dto = new ResultDto();
        dto.setCode(StatusCodeEnum.PARAM_IS_INVALID.getCode());
        dto.setMsg(StatusCodeEnum.PARAM_IS_INVALID.getText());
        return dto;
    }
    //后端系统返回
    //无数据
    public static ResultDto addResultNull () {
        ResultDto dto = new ResultDto();
        dto.setCode(StatusCodeEnum.RESULE_DATA_NONE.getCode());
        dto.setMsg("无数据");
        return dto;
    }
    //后端系统返回
    public static ResultDto addResultInfo (int code, String msg) {
        ResultDto dto = new ResultDto();
        dto.setCode(code);
        dto.setMsg(msg);
        return dto;
    }

}
