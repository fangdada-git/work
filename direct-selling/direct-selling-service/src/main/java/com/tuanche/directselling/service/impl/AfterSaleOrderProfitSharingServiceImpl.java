package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.tuanche.arch.commonmodel.resp.TcRpcResponse;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.directselling.api.AfterSaleActivityAccountService;
import com.tuanche.directselling.api.AfterSaleActivityService;
import com.tuanche.directselling.api.AfterSaleOrderRecordService;
import com.tuanche.directselling.dto.AfterSaleActivityAccountDto;
import com.tuanche.directselling.dto.AfterSaleOrderRecordDto;
import com.tuanche.directselling.job.AfterSaleOrderProfitSharingJob;
import com.tuanche.directselling.model.AfterSaleActivity;
import com.tuanche.directselling.model.AfterSaleActivityAccount;
import com.tuanche.directselling.model.AfterSaleOrderRecord;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.util.ConstantsUtil;
import com.tuanche.directselling.utils.AfterSaleConstants;
import com.tuanche.directselling.utils.ApiBaseCacheKeys;
import com.tuanche.directselling.utils.DateUtil;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.pay.out.enumeration.ProfitSharingDetailResultEnum;
import com.tuanche.pay.out.enumeration.ProfitSharingTypeEnum;
import com.tuanche.pay.out.service.PayProfitsharingService;
import com.tuanche.pay.out.vo.TcPayProfitsharingDetailVo;
import com.tuanche.pay.out.vo.TcPayProfitsharingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * 售后特卖订单分账服务
 * @author：HuangHao
 * @CreatTime 2021-12-13 10:48
 */
@Service
public class AfterSaleOrderProfitSharingServiceImpl {

    @Autowired
    AfterSaleOrderRecordServiceImpl afterSaleOrderRecordServiceImpl;
    @Autowired
    @Qualifier("ClusterRedisService")
    private RedisService redisService;
    @Autowired
    AfterSaleActivityService afterSaleActivityService;
    @Autowired
    AfterSaleActivityAccountService afterSaleActivityAccountService;
    @Reference(timeout = 1200000)
    PayProfitsharingService payProfitsharingService;
    @Autowired
    ExecutorService executorService;


    /**
     * 分账
     * @author HuangHao
     * @CreatTime 2021-12-14 13:50
     * @param afterSaleOrderRecordDto
     * @return void
     */
    public void distributeMoney(AfterSaleOrderRecordDto afterSaleOrderRecordDto){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                String logid = UUID.randomUUID().toString()+"-dm";
                List<AfterSaleOrderRecord> list = afterSaleOrderRecordServiceImpl.getUndistributeAccountOrderList(afterSaleOrderRecordDto);
                CommonLogUtil.addInfo(null, logid + "-开始分账，分账订单：",list);
                if (!CollectionUtils.isEmpty(list)) {
                    BigDecimal oneHundred = new BigDecimal("100");
                    int size = (int)(list.size() * 1.5);
                    Set<String> tradeIds = new HashSet<>(size);
                    Set<Integer> activityIds =new HashSet<>(size);
                    for (AfterSaleOrderRecord orderRecord : list) {
                        activityIds.add(orderRecord.getActivityId());
                        if(!StringUtils.isEmpty(orderRecord.getTcTransactionId())){
                            tradeIds.add(orderRecord.getTcTransactionId());
                        }
                    }
                    Map<Integer, AfterSaleActivityAccount> accountMap = afterSaleActivityAccountService.getActivityAccountMapByActivityIds(activityIds);
                    Map<Integer, AfterSaleActivity> activityMap = afterSaleActivityService.getAfterSaleActivityMap(new ArrayList<>(activityIds));
                    List<AfterSaleOrderRecord> updateOrderList = new ArrayList<>(size);
                    CommonLogUtil.addInfo(null, logid + "-开始查询待分账订单，参数：",tradeIds);
                    TcRpcResponse<Map<String, BigDecimal>> wxOrderAmountResponse = payProfitsharingService.queryProfilesharingAmount(tradeIds);
                    Map<String, BigDecimal> wxOrderAmountMap = wxOrderAmountResponse.getData();
                    if (wxOrderAmountMap == null || wxOrderAmountMap.size()<1) {
                        CommonLogUtil.addInfo(null, logid + "-未获取到待分账订单");
                        return;
                    }else{
                        CommonLogUtil.addInfo(null, logid + "-待分账订单",wxOrderAmountResponse);
                    }

                    for (int i = 1; i <= list.size(); i++) {
                        AfterSaleOrderRecord orderRecord = list.get(i - 1);
                        AfterSaleOrderRecord updateOrder = new AfterSaleOrderRecord();
                        updateOrder.setId(orderRecord.getId());
                        Integer activityId = orderRecord.getActivityId();
                        String orderCode = orderRecord.getOrderCode();
                        String tcTransactionId = orderRecord.getTcTransactionId();
                        String keyWord = "分账：" + orderCode + "-dm，交易流水号：" + tcTransactionId + "-dm，调用分账接口";
                        try {
                            AfterSaleActivity activity = activityMap.get(activityId);
                            if (isProfitSharing(activity)) {
                                AfterSaleActivityAccount activityAccount = accountMap.get(activityId);
                                if (activityAccount.getSubAccountRatio() != null) {
                                    BigDecimal tcSubAccountRatio = activityAccount.getSubAccountRatio();
                                    //经销商分账比例=（100-团车分账比例）/100
                                    BigDecimal dealerSubAccountRatio = (oneHundred.subtract(tcSubAccountRatio)).divide(oneHundred);
                                    //可分账金额
                                    BigDecimal wxOrderAmount = wxOrderAmountMap.get(tcTransactionId);
                                    if(wxOrderAmount == null){
                                        CommonLogUtil.addInfo(null, keyWord + "-未查到待分账金额，不分账");
                                        continue;
                                    }
                                    //订单金额
                                    BigDecimal orderMoney = orderRecord.getOrderMoney();
                                    //分账金额
                                    BigDecimal distributionAmount = orderMoney.multiply(dealerSubAccountRatio).setScale(2, BigDecimal.ROUND_HALF_UP);
                                    CommonLogUtil.addInfo(null, keyWord + "-分账比例：" + dealerSubAccountRatio+"，微信订单待分账金额："+wxOrderAmount+"，订单金额："+orderMoney+"，计算的分账金额："+distributionAmount);
                                    //如果分账金额大于可分账金额则以可分账金额为准
                                    if (distributionAmount.compareTo(wxOrderAmount) == 1) {
                                        distributionAmount = wxOrderAmount;
                                    }
                                    CommonLogUtil.addInfo(null, keyWord + "-实际分账金额："+distributionAmount);
                                    updateOrder.setSubAccountAmount(distributionAmount);
                                    //分账描述
                                    String desc = StringUtils.isEmpty(orderRecord.getLicencePlate()) ? "" : "【" + orderRecord.getLicencePlate() + "】购买套餐分账";
                                    TcRpcResponse<TcPayProfitsharingVo> profitsharingResponse = profitsharing(orderCode, orderRecord.getTcTransactionId(), activityAccount.getMchId(), distributionAmount, desc);
                                    CommonLogUtil.addInfo(null, keyWord + "-分账接口返回结果：" + JSON.toJSONString(profitsharingResponse));
                                    TcPayProfitsharingVo payProfitsharingVo = profitsharingResponse.getData();
                                    if (profitsharingResponse == null || payProfitsharingVo==null || CollectionUtils.isEmpty(payProfitsharingVo.getPayProfitsharingDetailVoList())) {
                                        updateOrder.setSubAccountStatus(AfterSaleConstants.OrderSubAccountStatus.STATUS_5.getCode());
                                        updateOrder.setSubAccountDesc(profitsharingResponse.getMsg());
                                        CommonLogUtil.addInfo(null, keyWord + "-分账失败,原因：" + profitsharingResponse.getMsg());
                                    } else {
                                        List<TcPayProfitsharingDetailVo> payProfitsharingDetailVoList = payProfitsharingVo.getPayProfitsharingDetailVoList();
                                        TcPayProfitsharingDetailVo profitsharingDetailVo = payProfitsharingDetailVoList.get(0);
                                        if (ProfitSharingDetailResultEnum.PENDING.getValue().equals(profitsharingDetailVo.getResult())) {
                                            updateOrder.setSubAccountStatus(AfterSaleConstants.OrderSubAccountStatus.STATUS_3.getCode());
                                            updateOrder.setSubAccountDesc(AfterSaleConstants.OrderSubAccountStatus.STATUS_3.getKey());
                                            CommonLogUtil.addInfo(null, keyWord + "-分账处理中");
                                        } else if (ProfitSharingDetailResultEnum.SUCCESS.getValue().equals(profitsharingDetailVo.getResult())) {
                                            updateOrder.setSubAccountStatus(AfterSaleConstants.OrderSubAccountStatus.STATUS_4.getCode());
                                            updateOrder.setSubAccountDesc("分账成功");
                                            updateOrder.setSubAccountTime(profitsharingDetailVo.getFinishTime() == null ? new Date() : profitsharingDetailVo.getFinishTime());
                                            updateOrder.setSubAccountTransactionDetailId(profitsharingDetailVo.getWxDetailId());
                                            CommonLogUtil.addInfo(null, keyWord + "-分账成功");
                                        } else {
                                            updateOrder.setSubAccountStatus(AfterSaleConstants.OrderSubAccountStatus.STATUS_5.getCode());
                                            updateOrder.setSubAccountDesc(profitsharingDetailVo.getFailReason());
                                            CommonLogUtil.addInfo(null, keyWord + "-分账失败,原因：" + profitsharingDetailVo.getFailReason());
                                        }
                                    }
                                } else {
                                    updateOrder.setSubAccountStatus(AfterSaleConstants.OrderSubAccountStatus.STATUS_5.getCode());
                                    String failReason = "未配置分账比例";
                                    updateOrder.setSubAccountDesc(failReason);
                                    CommonLogUtil.addInfo(null, keyWord + "-失败，原因：" + failReason + "，活动信息：" + JSON.toJSONString(activity));
                                }
                            } else {
                                updateOrder.setSubAccountStatus(AfterSaleConstants.OrderSubAccountStatus.STATUS_5.getCode());
                                String failReason = "该活动不分账";
                                updateOrder.setSubAccountDesc(failReason);
                                CommonLogUtil.addInfo(null, keyWord + "-失败，原因：" + failReason);
                            }
                            updateOrderList.add(updateOrder);
                            //每执行20个暂停1秒，应对微信30QPS
                            if (i % 20 == 0) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            CommonLogUtil.addError(null, keyWord + "-失败，原因：distributeMoney方法发生系统错误", e);
                        }
                    }
                    CommonLogUtil.addInfo(null, logid + "-更新订单：" ,updateOrderList);
                    //更新分账结果
                    if (updateOrderList.size() > 0) {
                        int num = afterSaleOrderRecordServiceImpl.updateBatchByIds(updateOrderList);
                        CommonLogUtil.addInfo(null, logid + "-更新订单条数："+num);
                    }
                }
            }
        });
    }


    /**
     *  分账
     * @author HuangHao
     * @CreatTime 2021-12-20 11:56
     * @param orderNo   团车订单号
     * @param wxTransactionId 微信支付流水号
     * @param activity  活动
     * @param distributionAmount    分账金额（元）
     * @param desc  分账描述
     * @return com.tuanche.arch.commonmodel.resp.TcRpcResponse<com.tuanche.pay.out.vo.TcPayProfitsharingVo>
     */
    public TcRpcResponse<TcPayProfitsharingVo> profitsharing(String orderNo,String wxTransactionId,String mchId,BigDecimal distributionAmount,String desc){
        String keyWord = "分账："+orderNo+"-dm，支付流水号："+wxTransactionId+"-dm，调用分账接口";
        if(StringUtils.isEmpty(orderNo)){
            CommonLogUtil.addInfo(null, keyWord+"-失败，原因：订单号为空");
            return new TcRpcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"订单号为空");
        }
        if(StringUtils.isEmpty(wxTransactionId)){
            CommonLogUtil.addInfo(null, keyWord+"-失败，原因：支付流水号为空");
            return new TcRpcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"支付流水号为空");
        }
        if(StringUtils.isEmpty(mchId)){
            CommonLogUtil.addInfo(null, keyWord+"-失败，原因：收账商户号未配置");
            return new TcRpcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"收账商户号未配置");
        }
        if(distributionAmount == null){
            CommonLogUtil.addInfo(null, keyWord+"-失败，原因：分账金额为空");
            return new TcRpcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"分账金额为空");
        }
        try {
            TcPayProfitsharingVo payProfitsharingVo = new TcPayProfitsharingVo();
            payProfitsharingVo.setProfitSharingTypeEnum(ProfitSharingTypeEnum.SINGLE);//单次分账
            List<TcPayProfitsharingDetailVo> profitsharingDetailVoList = new ArrayList<>();
            TcPayProfitsharingDetailVo profitsharingDetailVo = new TcPayProfitsharingDetailVo();
            profitsharingDetailVo.setTradeBusinessDetail(orderNo);//支付订单号
            profitsharingDetailVo.setAccount(mchId);//分账接收商户号
            profitsharingDetailVo.setAccountType("MERCHANT_ID");//商户

            profitsharingDetailVo.setAmount(distributionAmount);
            profitsharingDetailVo.setDescription(desc);
            profitsharingDetailVoList.add(profitsharingDetailVo);
            payProfitsharingVo.setPayProfitsharingDetailVoList(profitsharingDetailVoList);
            payProfitsharingVo.setPayTradeId(wxTransactionId);
            CommonLogUtil.addInfo(null, keyWord+"-开始调用分账接口，请求参数："+ JSON.toJSONString(payProfitsharingVo));
            return payProfitsharingService.profitsharing("104", orderNo, orderNo, payProfitsharingVo);
        } catch (Exception e) {
            e.printStackTrace();
            CommonLogUtil.addError(null, keyWord+"-失败，原因：调用分账接口发生系统错误",e);
            return new TcRpcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"调用分账接口发生系统错误");
        }
    }


    /**
     * 验证活动是否分账
     * @author HuangHao
     * @CreatTime 2021-12-20 15:48
     * @param activity
     * @return boolean
     */
    public boolean isProfitSharing(AfterSaleActivity activity){
        if(activity !=null && activity.getOnSubAccount()!=null && activity.getOnSubAccount() > 0){
            return true;
        }else{
            return false;
        }
    }
}
