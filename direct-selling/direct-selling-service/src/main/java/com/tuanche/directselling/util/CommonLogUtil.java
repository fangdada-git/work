package com.tuanche.directselling.util;

import com.alibaba.fastjson.JSON;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;

import java.text.MessageFormat;


/**
 * create by: dudg
 * description: 日志辅助类
 * create time: 2019/7/3 15:48
 * @return
 */
public class CommonLogUtil {
    /**
     * 日志输出中的 系统名称  -- 一般为当前项目的名称 英文大写
     */
    public static final String SYSTEM_NAME = "DIRECT_SELLING-SERVICE";

    public static void addInfo(String key, String msg) {
        StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[2];
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, SYSTEM_NAME, MessageFormat.format("invoke-class:[{0}],invoke-method:[{1}]",stackTrace.getClassName(),stackTrace.getMethodName()), key, msg, "");
    }

    public static void addInfo(String key, String msg, Object value) {
        StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[2];

        if(value == null || value instanceof String){
            StaticLogUtils.info(SystemLogType.LOG_SYS_B, SYSTEM_NAME, MessageFormat.format("invoke-class:[{0}],invoke-method:[{1}]",stackTrace.getClassName(),stackTrace.getMethodName()), key, msg, value);
        }
        else{
            StaticLogUtils.info(SystemLogType.LOG_SYS_B, SYSTEM_NAME, MessageFormat.format("invoke-class:[{0}],invoke-method:[{1}]",stackTrace.getClassName(),stackTrace.getMethodName()), key, msg, JSON.toJSONString(value));
        }
    }

    public static void addError(String key, String msg, Throwable value) {
        StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[2];
        StaticLogUtils.error(SystemLogType.LOG_SYS_B, SYSTEM_NAME, MessageFormat.format("invoke-class:[{0}],invoke-method:[{1}]",stackTrace.getClassName(),stackTrace.getMethodName()), key, msg, value);
    }
}
