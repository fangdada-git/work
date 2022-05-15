package com.tuanche.web.api;

import com.tuanche.arch.web.model.TcPageInfo;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.directselling.utils.StatusCodeEnum;

/**
 * @class: BaseController
 * @description: 公共类
 * @author: dudg
 * @create: 2019-12-02 16:34
 */
public class BaseController {

    protected TcResponse response(StatusCodeEnum resultCode, Object result, long QTime) {
        return new TcResponse(resultCode.getCode(),System.currentTimeMillis()-QTime,resultCode.getText(),result);
    }

    protected TcResponse response(StatusCodeEnum resultCode, Object result, long QTime, TcPageInfo tcPageInfo) {
        return new TcResponse(resultCode.getCode(),System.currentTimeMillis()-QTime,resultCode.getText(),result, tcPageInfo);
    }

    protected TcResponse success(Object result, long QTime){
        return response(StatusCodeEnum.SUCCESS,result,QTime);
    }

    /**
     * 无数据返回
     * @return
     */
    protected TcResponse noData(long QTime) {
        return response(StatusCodeEnum.RESULE_DATA_NONE,null,QTime);
    }

    /**
     * 参数无效
     */
    protected TcResponse paramInvalid(long QTime){
        return response(StatusCodeEnum.PARAM_IS_INVALID, null, QTime);
    }

    protected TcResponse systemError(long QTime) {
        return response(StatusCodeEnum.SYSTEM_INNER_ERROR,null,QTime);
    }
}
