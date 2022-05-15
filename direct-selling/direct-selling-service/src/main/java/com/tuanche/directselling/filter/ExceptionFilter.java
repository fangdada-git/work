package com.tuanche.directselling.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.utils.ReflectUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.commons.util.JSONUtil;
import com.tuanche.directselling.util.CommonLogUtil;

import java.lang.reflect.Method;
import java.text.MessageFormat;

/**
 * @class: ExceptionFilter
 * @description: 全局异常处理
 * @author: dudg
 * @create: 2019-07-11 11:06
 */
@Activate(group = Constants.PROVIDER)
public class ExceptionFilter implements Filter {

    private String sysName = "directselling-service";

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            long startTime = System.currentTimeMillis();
            Result result = invoker.invoke(invocation);
            if((System.currentTimeMillis()-startTime)>200){
                CommonLogUtil.addInfo("AAA接口耗时记录",MessageFormat.format("interface:{0},method:{1},usetime(ms):{2}",invoker.getInterface().getName(),invocation.getMethodName(),System.currentTimeMillis()-startTime),"");
            }
            if (result.hasException() && GenericService.class != invoker.getInterface()) {
                try {
                    Throwable exception = result.getException();

                    // 检查异常，直接抛出
                    if (!(exception instanceof RuntimeException) && (exception instanceof Exception)) {
                        return result;
                    }
                    // 方法签名上有说明抛出非检查异常，直接抛出
                    try {
                        Method method = invoker.getInterface().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
                        Class<?>[] exceptionClassses = method.getExceptionTypes();
                        for (Class<?> exceptionClass : exceptionClassses) {
                            if (exception.getClass().equals(exceptionClass)) {
                                return result;
                            }
                        }
                    } catch (NoSuchMethodException e) {
                        return result;
                    }
                    // for the exception not found in method's signature, print ERROR message in server's log.
                    StaticLogUtils.error(SystemLogType.LOG_SYS_B,sysName,"","Got unchecked and undeclared exception which called by " + RpcContext.getContext().getRemoteHost()
                            + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName()
                            + ", exception: " + exception.getMessage(),"request params:"+ JSONUtil.toJson(invocation.getArguments()), exception);

                    //输出错误位置等详细信息
                    exception.printStackTrace();
                    // 异常类和接口类在同一jar包里，直接抛出
                    String serviceFile = ReflectUtils.getCodeBase(invoker.getInterface());
                    String exceptionFile = ReflectUtils.getCodeBase(exception.getClass());
                    if (serviceFile == null || exceptionFile == null || serviceFile.equals(exceptionFile)) {
                        return result;
                    }
                    // JDK异常，直接抛出
                    String className = exception.getClass().getName();
                    if (className.startsWith("java.") || className.startsWith("javax.")) {
                        return result;
                    }
                    // dubbo异常，直接抛出
                    if (exception instanceof RpcException) {
                        return result;
                    }

                    // 否则，包装成RuntimeException抛给客户端
                    return new RpcResult(new RuntimeException(StringUtils.toString(exception)));
                } catch (Throwable e) {
                    StaticLogUtils.error(SystemLogType.LOG_SYS_B,sysName,this.getClass().getName(),"Fail to ExceptionFilter when called by " + RpcContext.getContext().getRemoteHost()
                            + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName()
                            + "request params:"+ JSONUtil.toJson(invocation.getArguments()),e.getMessage(), e);
                    return result;
                }
            }
            return result;
        } catch (RuntimeException e) {
            StaticLogUtils.error(SystemLogType.LOG_SYS_B,sysName,this.getClass().getName(),"Got unchecked and undeclared exception which called by " + RpcContext.getContext().getRemoteHost()
                    + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName()
                    + "request params:"+ JSONUtil.toJson(invocation.getArguments()),e.getMessage(), e);
            throw e;
        }
    }


}
