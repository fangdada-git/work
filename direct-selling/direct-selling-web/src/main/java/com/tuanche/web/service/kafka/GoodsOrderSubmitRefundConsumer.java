package com.tuanche.web.service.kafka;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tuanche.arch.kafka.consumer.KafkaBaseConsumer;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.api.FissionGoodsOrderRecordservice;
import com.tuanche.directselling.api.FissionSaleService;
import com.tuanche.directselling.dto.AfterSaleOrderRecordDto;
import com.tuanche.directselling.enums.FissionGoodsOrderStatus;
import com.tuanche.directselling.model.FissionGoodsOrderRecord;
import com.tuanche.directselling.model.FissionSale;
import com.tuanche.directselling.vo.FissionUserTaskRecordVo;
import com.tuanche.merchant.business.api.order.IOrderService;
import com.tuanche.merchant.business.dto.request.BaseQueryRequestDto;
import com.tuanche.merchant.business.dto.response.BaseResponseDto;
import com.tuanche.merchant.business.dto.response.order.CommodityOrderResponseDto;
import com.tuanche.merchant.pojo.dto.enums.BusinessTypeEnum;
import com.tuanche.merchant.pojo.dto.enums.ResultEnum;
import com.tuanche.merchant.pojo.dto.enums.ServiceTypeEnum;
import com.tuanche.web.service.PayServiceImpl;
import com.tuanche.web.util.DirectCommonUtil;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description : 商品订单下单、申请退款通知
 * @author : fxq
 * @param :
 * @return :
 * @date : 2021/5/25 15:09
 */
@Service
public class GoodsOrderSubmitRefundConsumer extends KafkaBaseConsumer<String, String> implements CommandLineRunner {

    @Value("${kafka.config.merchant-order-submit-refund-notice.group-name}")
    private String groupName;
    @Value("${kafka.config.zookeeper-connect}")
    private String zkConnect;
    @Value("${kafka.config.merchant-order-submit-refund-notice.topic-name}")
    private String topic;
    @Value("${kafka.config.merchant-order-submit-refund-notice.topic-count}")
    private int topicCount;

    @Reference
    private FissionSaleService fissionSaleService;
    @Reference
    private FissionGoodsOrderRecordservice fissionGoodsOrderRecordservice;
    @Reference
    private IOrderService iOrderService;
    @Autowired
    private PayServiceImpl payServiceImpl;

    @Bean
    public ConsumerConnector videoCallBackConnector(){
        com.tuanche.arch.kafka.consumer.KafkaConsumerFactoryBean consumerFactoryBean = new com.tuanche.arch.kafka.consumer.KafkaConsumerFactoryBean();
        consumerFactoryBean.setGroupName(groupName);
        consumerFactoryBean.setZookeeperConnect(zkConnect);
        return consumerFactoryBean.getObject();
    }

    /**
     * kafka 消费消息
     * @param message
     */
    @Override
    public void consumeMsg(String message) {
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "GoodsOrderSubmitRefundConsumer", "裂变商品订单下单、退款通知 start",message);
        try {
            if (!StringUtil.isEmpty(message)) {
                JSONObject json = JSON.parseObject(message);
                String businessType = (String) json.get("businessType");
                if ("FISSION".equals(businessType)) {
                    String callbackType = (String) json.get("callbackType");
                    String orderCode = (String) json.get("orderCode");
                    Integer activityId = null;
                    if (json.get("activityId") instanceof Integer) {
                        activityId = (Integer) json.get("activityId");
                    } else {
                        String id = (String) json.get("activityId");
                        activityId = Integer.valueOf(id);
                    }
                    if (!StringUtil.isEmpty(orderCode)) {
                        //下单
                        if ("SUBMIT_ORDER".equals(callbackType)) {
                            FissionGoodsOrderRecord fissionGoodsOrderRecord = JSON.parseObject(message, FissionGoodsOrderRecord.class);
                            fissionGoodsOrderRecord.setFissionId(activityId);
                            fissionGoodsOrderRecord.setOrderNo(orderCode);
                            fissionGoodsOrderRecord.setOrderStatus(FissionGoodsOrderStatus.UNPAID.getType());
                            addFissionGoodsOrderRecord(fissionGoodsOrderRecord);
                            String ip = (String) json.get("ip");
                            if (!StringUtil.isEmpty(ip) && fissionGoodsOrderRecord.getFissionId()!=null) {
                                FissionUserTaskRecordVo taskRecordVo = JSON.parseObject(message, FissionUserTaskRecordVo.class);
                                taskRecordVo.setFissionId(fissionGoodsOrderRecord.getFissionId());
                                taskRecordVo.setIp(ip);
                                setFissionUserTaskRecord(taskRecordVo, fissionGoodsOrderRecord.getOrderNo());
                            }
                            //退款
                        } else if ("REFUND_ORDER".equals(callbackType)) {
                            FissionGoodsOrderRecord goodsOrderRecord = new FissionGoodsOrderRecord();
                            goodsOrderRecord.setOrderNo(orderCode);
                            goodsOrderRecord.setOrderStatus(FissionGoodsOrderStatus.APPLY_REFUND.getType());
                            fissionGoodsOrderRecordservice.updateFissionGoodsOrderRecordByOrderNo(goodsOrderRecord);
                        }
                    }
//                } else if ("SALE".equals(businessType)) {
//                    StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "GoodsOrderSubmitRefundConsumer", "售后特卖商品订单下单、申请退款通知 暂无操作",message);
                } else {
                    StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "GoodsOrderSubmitRefundConsumer", "其他业务商品订单下单、申请退款通知 暂无操作",message);
                }
            }
        } catch (Exception e) {
            StaticLogUtils.error(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "GoodsOrderSubmitRefundConsumer", "裂变商品订单下单、退款通知 error",message,e);
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "GoodsOrderSubmitRefundConsumer", "裂变商品订单下单、退款通知 end","");
    }
    //写裂变商品用户订单记录
    private void addFissionGoodsOrderRecord(FissionGoodsOrderRecord fissionGoodsOrderRecord) {
        BaseQueryRequestDto<String> requestDto = new BaseQueryRequestDto<>();
        requestDto.setQuery(fissionGoodsOrderRecord.getOrderNo());
        requestDto.setServiceTypeEnum(ServiceTypeEnum.COMMON);
        BaseResponseDto<CommodityOrderResponseDto> orderByOrderCode = iOrderService.getOrderByOrderCode(requestDto, CommodityOrderResponseDto.class);
        if (orderByOrderCode.getCode().equals(ResultEnum.SUCCESS.getCode()) && orderByOrderCode.getData()!=null) {
            CommodityOrderResponseDto order = orderByOrderCode.getData();
            fissionGoodsOrderRecord.setGoodsId(order.getCommodityId());
            fissionGoodsOrderRecord.setOrderAmount(order.getOrderMoney());
            fissionGoodsOrderRecord.setUserId(order.getUserId());
            //写裂变商品用户订单记录
            if (!StringUtil.isEmpty(fissionGoodsOrderRecord.getSaleWxUnionId()) && fissionGoodsOrderRecord.getFissionId() != null) {
                FissionSale sale = new FissionSale();
                sale.setSaleWxUnionId(fissionGoodsOrderRecord.getSaleWxUnionId());
                sale.setFissionId(fissionGoodsOrderRecord.getFissionId());
                FissionSale fissionSale = fissionSaleService.getFissionSale(sale);
                if (fissionSale != null) {
                    fissionGoodsOrderRecord.setDealerId(fissionSale.getDealerId());
                    fissionGoodsOrderRecord.setSaleId(fissionSale.getSaleId());
                }
            }
        } else {
            StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "GoodsOrderSubmitRefundConsumer", "裂变商品订单下单、退款通知 写裂变商品用户订单记录 订单异常",JSON.toJSONString(orderByOrderCode));
        }
        fissionGoodsOrderRecordservice.addFissionGoodsOrderRecord(fissionGoodsOrderRecord);
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "GoodsOrderSubmitRefundConsumer", "裂变商品订单下单、退款通知 写裂变商品用户订单记录 end",JSON.toJSONString(fissionGoodsOrderRecord));
    }
    //写下单任务记录
    private void setFissionUserTaskRecord (FissionUserTaskRecordVo taskRecordVo, String orderNo) {
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "GoodsOrderSubmitRefundConsumer", "裂变商品订单下单、退款通知 写下单任务 start",JSON.toJSONString(taskRecordVo)+"___"+orderNo);
        payServiceImpl.setFissionUserTaskRecord(taskRecordVo, orderNo);
    }

    @Override
    public void run(String... args) throws Exception {
        Map<String,Integer> topicCountmap = new HashMap<>();
        topicCountmap.put(topic,Integer.valueOf(topicCount));
        StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
        StringDecoder valDecoder = new StringDecoder(new VerifiableProperties());
        Map<String, List<KafkaStream<String,String>>> consumeMap = videoCallBackConnector().createMessageStreams(topicCountmap,keyDecoder,valDecoder);
        this.subscribe(topic,topicCount,consumeMap);

    }

    @PreDestroy
    public void shutdown(){
        if(videoCallBackConnector() !=null ) videoCallBackConnector().shutdown();
        super.shutdown();
    }

}

