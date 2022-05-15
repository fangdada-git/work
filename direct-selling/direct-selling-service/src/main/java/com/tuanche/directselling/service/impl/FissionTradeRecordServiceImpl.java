package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.api.FissionTradeRecordService;
import com.tuanche.directselling.mapper.read.directselling.FissionTradeRecordReadMapper;
import com.tuanche.directselling.mapper.write.directselling.FissionTradeRecordWriteMapper;
import com.tuanche.directselling.model.FissionTradeRecord;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.util.FuncUtil;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.WeChatPaymentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.weixin4j.WeixinException;
import org.weixin4j.pay.redpack.EnterprisePay;
import org.weixin4j.pay.redpack.EnterprisePayQuery;
import org.weixin4j.pay.redpack.EnterprisePayQueryResult;
import org.weixin4j.pay.redpack.EnterprisePayResult;
import org.weixin4j.service.WxPayService;

import java.util.*;

/**
 * @class: FissionTradeRecordServiceImpl
 * @description: 交易记录接口服务
 * @author: dudg
 * @create: 2020-09-24 11:45
 */
@Service
public class FissionTradeRecordServiceImpl implements FissionTradeRecordService {

    @Value("${fission_pay_mchid}")
    private String fission_pay_mchid;

    @Autowired
    FissionTradeRecordWriteMapper tradeRecordWriteMapper;
    @Autowired
    FissionTradeRecordReadMapper fissionTradeRecordReadMapper;
    @Reference
    WxPayService wxPayService;

    /**
     * @description: 插入交易记录
     * @param tradeRecord
     * @return: int
     * @author: dudg
     * @date: 2020/9/24 12:00
    */
    @Override
    public int insertSelective(FissionTradeRecord tradeRecord){
        tradeRecord.setCreateDt(new Date());
        tradeRecord.setUpdateDt(new Date());
        int res = tradeRecordWriteMapper.insertSelective(tradeRecord);
        return res;
    }

    /**
     * @description: 更新交易记录
     * @param tradeRecord
     * @return: int
     * @author: dudg
     * @date: 2020/9/24 12:02
    */
    @Override
    public int updateByPkeySelective(FissionTradeRecord tradeRecord){
        int res = tradeRecordWriteMapper.updateByPrimaryKeySelective(tradeRecord);
        return res;
    }

    /**
     * @param weChatPaymentVo
     * @description: 微信付款
     * @return: void
     * @author: dudg
     * @date: 2020/9/25 10:06
     */
    @Override
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    public synchronized ResultDto wechatPayment(WeChatPaymentVo weChatPaymentVo) throws WeixinException {
        ResultDto resultDto = new ResultDto();
        String payNonceStr = UUID.randomUUID().toString().replaceAll("-", "");
        String logKey = "微信付款,标识："+payNonceStr;
        CommonLogUtil.addInfo(logKey, "请求参数：", weChatPaymentVo);

        if (StringUtil.isEmpty(weChatPaymentVo.getOpenId())){
            resultDto.setMsg("收款人微信openId不能为空");
            resultDto.setCode(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode());
            return resultDto;
        }

        if(weChatPaymentVo.getAmount() <=0){
            resultDto.setMsg("付款金额必须大于零");
            resultDto.setCode(StatusCodeEnum.PARAM_IS_INVALID.getCode());
            return resultDto;
        }

        if(!FuncUtil.notNullAndEquals(weChatPaymentVo.getTradeType(),0)){
            resultDto.setMsg("交易类型不能为空");
            resultDto.setCode(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode());
            return resultDto;
        }

        if (StringUtil.isEmpty(weChatPaymentVo.getOrder_no())){
            resultDto.setMsg("付款订单编号不能为空");
            resultDto.setCode(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode());
            return resultDto;
        }

        //1.构造付款信息
        EnterprisePay payRequest = new EnterprisePay();
        payRequest.setMch_appid(weChatPaymentVo.getAppId());
        payRequest.setMchid(fission_pay_mchid);
        payRequest.setNonce_str(payNonceStr);
        payRequest.setPartner_trade_no(weChatPaymentVo.getOrder_no());
        payRequest.setOpenid(weChatPaymentVo.getOpenId());
        payRequest.setAmount(weChatPaymentVo.getAmount());
        payRequest.setCheck_name("NO_CHECK");
        payRequest.setDesc(weChatPaymentVo.getDesc());

        //2.调用微信付款
        CommonLogUtil.addInfo(logKey, "调用微信付款接口参数：", payRequest);
        EnterprisePayResult payResponse = wxPayService.enterprisePayToBalance(payRequest);
        CommonLogUtil.addInfo(logKey, "调用微信付款接口返回：", payResponse);
        if(!payResponse.getResult_code().equalsIgnoreCase("success")){
            resultDto.setMsg(payResponse.getErr_code_des());
            resultDto.setCode(StatusCodeEnum.INTERFACE_INNER_INVOKE_ERROR.getCode());
            return resultDto;
        }

        FissionTradeRecord tradeRecord = new FissionTradeRecord();
        tradeRecord.setOrderNo(weChatPaymentVo.getOrder_no());
        tradeRecord.setPayUserId(fission_pay_mchid);
        tradeRecord.setToUserId(weChatPaymentVo.getOpenId());
        tradeRecord.setTradeAmount(weChatPaymentVo.getOriginAmount());
        tradeRecord.setTradeRemark(weChatPaymentVo.getDesc());
        tradeRecord.setReqInfo(JSON.toJSONString(payRequest));
        tradeRecord.setRespInfo(JSON.toJSONString(payResponse));
        tradeRecord.setTradeType(weChatPaymentVo.getTradeType());
        tradeRecord.setTradeStatus(payResponse.getResult_code().equalsIgnoreCase("success") ? 1 : 0);

        //3.查询付款结果
        EnterprisePayQuery payQuery = new EnterprisePayQuery();
        payQuery.setAppid(weChatPaymentVo.getAppId());
        payQuery.setMch_id(fission_pay_mchid);
        payQuery.setNonce_str(payNonceStr);
        payQuery.setPartner_trade_no(weChatPaymentVo.getOrder_no());
        EnterprisePayQueryResult payQueryResult = wxPayService.queryEnterprisePayToBalance(payQuery);

        CommonLogUtil.addInfo(logKey, "微信付款查询返回信息：", payResponse);
        if(payQueryResult.getStatus().equalsIgnoreCase("success")){
            //获取微信内部产生的付款单号
            tradeRecord.setTradeNo(payQueryResult.getDetail_id());
            tradeRecord.setPayTime(FuncUtil.StringToDate(payQueryResult.getPayment_time()));
            tradeRecord.setTradeTime(FuncUtil.StringToDate(payQueryResult.getTransfer_time()));
        }

        //4.保存交易记录
        insertSelective(tradeRecord);

        //返回交易结果
        resultDto.setResult(tradeRecord);
        resultDto.setCode(StatusCodeEnum.SUCCESS.getCode());
        return resultDto;
    }
    
    @Override
    public List<FissionTradeRecord> getFissionTradeRecordList (FissionTradeRecord record) {
        return fissionTradeRecordReadMapper.getFissionTradeRecordList(record);
    }
    @Override
    public List<FissionTradeRecord> getFissionTradeRecordListByWrite (FissionTradeRecord record) {
        return tradeRecordWriteMapper.getFissionTradeRecordListByWrite(record);
    }
    @Override
    public FissionTradeRecord getFissionTradeRecordByInfo (FissionTradeRecord record) {
        return fissionTradeRecordReadMapper.getFissionTradeRecordByInfo(record);
    }

    public Map<String, FissionTradeRecord> getFissionTradeRecordMap (FissionTradeRecord record) {
        Map<String, FissionTradeRecord> map = new HashMap<>();
        List<FissionTradeRecord> recordList = fissionTradeRecordReadMapper.getFissionTradeRecordList(record);
        recordList.forEach(v->{
            map.put(v.getOrderNo(), v);
        });
        return map;
    }

}
