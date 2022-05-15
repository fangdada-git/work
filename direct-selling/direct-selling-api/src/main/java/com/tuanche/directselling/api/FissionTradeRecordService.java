package com.tuanche.directselling.api;

import com.tuanche.commons.util.ResultDto;
import com.tuanche.directselling.model.FissionTradeRecord;
import com.tuanche.directselling.vo.WeChatPaymentVo;

import java.util.List;

/**
 * @interface: FissionTradeRecordService
 * @description: 交易记录接口
 * @author: dudg
 * @create: 2020-09-24 11:44
 */
public interface FissionTradeRecordService {

    int insertSelective(FissionTradeRecord tradeRecord);

    int updateByPkeySelective(FissionTradeRecord tradeRecord);

    /**
     * @param weChatPaymentVo
     * @description: 微信付款
     * @return: void
     * @author: dudg
     * @date: 2020/9/25 10:06
     */
    ResultDto wechatPayment(WeChatPaymentVo weChatPaymentVo) throws Exception;

    List<FissionTradeRecord> getFissionTradeRecordList (FissionTradeRecord record);

    FissionTradeRecord getFissionTradeRecordByInfo (FissionTradeRecord record);

    List<FissionTradeRecord> getFissionTradeRecordListByWrite(FissionTradeRecord fissionTradeRecord);
}
