package com.tuanche.directselling.api;

import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.directselling.model.AfterSaleUserShare;

/**
 * @author：HuangHao
 * @CreatTime 2021-07-21 11:03
 */
public interface AfterSaleUserShareService {

    /**
     * 新增分享记录
     * @author HuangHao
     * @CreatTime 2021-07-21 10:52
     * @param record
     * @return com.tuanche.arch.web.model.TcResponse
     */
    TcResponse insert(AfterSaleUserShare record);
}
