package com.tuanche.web.service.kafka;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tuanche.arch.kafka.consumer.KafkaBaseConsumer;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.api.*;
import com.tuanche.directselling.enums.FissionGoodsOrderStatus;
import com.tuanche.directselling.model.AfterSaleOrderRecord;
import com.tuanche.directselling.model.FissionGoodsHelperUser;
import com.tuanche.directselling.model.FissionGoodsOrderRecord;
import com.tuanche.directselling.model.FissionUser;
import com.tuanche.directselling.utils.AfterSaleConstants;
import com.tuanche.directselling.utils.DateUtil;
import com.tuanche.directselling.vo.FissionUserTaskRecordVo;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.api.WxTemplateMessageBaseService;
import com.tuanche.manubasecenter.dto.CsCompanyDetailDto;
import com.tuanche.manubasecenter.dto.WxDataDto;
import com.tuanche.manubasecenter.dto.WxTemplateInfoDto;
import com.tuanche.merchant.business.api.order.IOrderService;
import com.tuanche.merchant.business.dto.request.BaseQueryRequestDto;
import com.tuanche.merchant.business.dto.response.BaseResponseDto;
import com.tuanche.merchant.business.dto.response.order.business.OrderExtendWithBusinessResponseDto;
import com.tuanche.merchant.pojo.dto.enums.BusinessTypeEnum;
import com.tuanche.merchant.pojo.dto.enums.ServiceTypeEnum;
import com.tuanche.web.service.PayServiceImpl;
import com.tuanche.web.util.CommonLogUtil;
import com.tuanche.web.util.DirectCommonUtil;
import com.tuanche.web.util.Globals;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PreDestroy;
import java.util.*;

/**
 * @description : ??????????????????|????????????????????????
 * @author : fxq
 * @param :
 * @return :
 * @date : 2021/1/18 17:24
 */
@Service
public class GoodsOrderPaySuccessConsumer extends KafkaBaseConsumer<String, String> implements CommandLineRunner {

    @Value("${kafka.config.merchant-order-pay-success-notice.group-name}")
    private String groupName;
    @Value("${kafka.config.zookeeper-connect}")
    private String zkConnect;
    @Value("${kafka.config.merchant-order-pay-success-notice.topic-name}")
    private String topic;
    @Value("${kafka.config.merchant-order-pay-success-notice.topic-count}")
    private int topicCount;

    @Reference
    private FissionGoodsOrderRecordservice fissionGoodsOrderRecordservice;
    @Reference
    private FissionNoticeProducerService fissionNoticeProducerService;
    @Autowired
    private PayServiceImpl payServiceImpl;
    @Reference
    private FissionUserTaskRecordService fissionUserTaskRecordService;
    @Reference
    private IOrderService iOrderService;
    @Reference
    private FissionGoodsHelperUserService fissionGoodsHelperUserService;
    @Reference
    private FissionUserService fissionUserService;
    @Reference
    private AfterSaleOrderRecordService afterSaleOrderRecordService;
    @Reference
    private AfterSaleCouponService afterSaleCouponService;
    @Reference
    private CompanyBaseService companyBaseService;
    @Value("${refund_template_id}")
    private String refund_template_id;
    @Value("${aftersale_appid}")
    private String aftersale_appid;
    @Reference
    WxTemplateMessageBaseService wxTemplateMessageBaseService;

    @Bean
    public ConsumerConnector videoCallBackConnector(){
        com.tuanche.arch.kafka.consumer.KafkaConsumerFactoryBean consumerFactoryBean = new com.tuanche.arch.kafka.consumer.KafkaConsumerFactoryBean();
        consumerFactoryBean.setGroupName(groupName);
        consumerFactoryBean.setZookeeperConnect(zkConnect);
        return consumerFactoryBean.getObject();
    }

    /**
     * kafka ????????????
     * @param message
     */
    @Override
    public void consumeMsg(String message) {
        try {
            StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "GoodsOrderPaySuccessConsumer", "consumeMsg", "kafka??????????????????-????????????????????????????????????  start", message);
            if (StringUtils.isNotEmpty(message)) {
                JSONObject obj = JSON.parseObject(message);
                String orderNo = (String) obj.get("order_code");
                String  businessType = (String) obj.get("businessType");
                String keyWord = "kafka?????????????????????????????????:"+orderNo+"-kaf,????????????businessType???"+businessType+"-kafka";
                if (StringUtils.isEmpty(businessType)) {
                    CommonLogUtil.addInfo(null, keyWord+"??????kafka?????????businessType??????", null);
                    return;
                }

                CommonLogUtil.addInfo(null, keyWord+",??????????????????"+message, null);
                String transform =  obj.getString("transform");
                // ??????????????????????????????
                String wxTransactionId = obj.getString("payment3SerialNo");
                // ?????????transaction_id?????????????????????seriesNo???trade_id
                String tcTransactionId = obj.getString("trade_id");
                //??????
                if(BusinessTypeEnum.FISSION.name().equals(businessType)){
                    CommonLogUtil.addInfo(null, keyWord+"-???????????????", null);
                    if(StringUtils.isEmpty(orderNo)){
                        CommonLogUtil.addInfo(null, keyWord+"-??????????????????????????????????????????", null);
                        return;
                    }
                    BaseQueryRequestDto<String> baseQueryRequestDto = new BaseQueryRequestDto<>();
                    baseQueryRequestDto.setQuery(orderNo);
                    baseQueryRequestDto.setServiceTypeEnum(ServiceTypeEnum.BUSINESS);
                    BaseResponseDto<OrderExtendWithBusinessResponseDto> orderResponseDto = iOrderService.getOrderByOrderCode(baseQueryRequestDto, OrderExtendWithBusinessResponseDto.class);
                    StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "GoodsOrderPaySuccessConsumer", "consumeMsg", "????????????????????????????????????  ????????????", JSON.toJSONString(orderResponseDto));
                    if (orderResponseDto!=null && orderResponseDto.getData()!=null && orderResponseDto.getData().getOrderId()!=null) {
                        OrderExtendWithBusinessResponseDto orderExtendDto = orderResponseDto.getData();
                        //??????
                        if (orderExtendDto.getBusinessTypeEnum().equals(BusinessTypeEnum.FISSION)) {
                            FissionGoodsOrderRecord orderRecord = new FissionGoodsOrderRecord();
                            orderRecord.setOrderNo(orderNo);
                            List<FissionGoodsOrderRecord> fissionGoodsOrderRecordList = fissionGoodsOrderRecordservice.getFissionGoodsOrderRecordList(orderRecord);
                            if (!CollectionUtils.isEmpty(fissionGoodsOrderRecordList)) {
                                fissionOrderPaySuccess(orderExtendDto, transform, fissionGoodsOrderRecordList.get(0));
                            } else {
                                StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "GoodsOrderPaySuccessConsumer", "consumeMsg", "??????????????????????????????????????????  ?????????????????????", message);
                            }
                        }
                    } else {
                        StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "GoodsOrderPaySuccessConsumer", "consumeMsg", keyWord+"??????????????????????????????????????????  ????????????", message);
                    }
                //????????????
                }else if(BusinessTypeEnum.SALE.name().equals(businessType)) {
                    CommonLogUtil.addInfo(null, keyWord+"-?????????????????????", null);
                    if(StringUtils.isEmpty(orderNo)){
                        CommonLogUtil.addInfo(null, keyWord+"-??????????????????????????????????????????", null);
                        return;
                    }
                    List<AfterSaleOrderRecord> afterSaleOrderRecordList = afterSaleOrderRecordService.getAfterSaleOrderRecordByOrderCodes(Arrays.asList(orderNo));
                    if (CollectionUtils.isEmpty(afterSaleOrderRecordList)) {
                        StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "GoodsOrderPaySuccessConsumer", "consumeMsg", "????????????????????????????????????????????????  ?????????????????????", message);
                    }
                    AfterSaleOrderRecord afterSaleOrderRecord = afterSaleOrderRecordList.get(0);
                    //??????
                    if (transform.equals("PAYMENT")) {
                        //????????????????????????????????????
                        AfterSaleOrderRecord record = new AfterSaleOrderRecord();
                        record.setId(afterSaleOrderRecord.getId());
                        record.setOrderStatus(AfterSaleConstants.OrderStatus.PAID.getCode());
                        record.setPayTime(obj.getDate("transform_time"));
                        record.setWxTransactionId(wxTransactionId);
                        record.setTcTransactionId(tcTransactionId);
                        CommonLogUtil.addInfo(null, keyWord+"-????????????", record);
                        afterSaleOrderRecordService.updateAfterSaleOrderPaySuccess(record);
                        //??????
                    } else if (transform.equals("EXPIRED")) {
                        //?????????????????????????????????
                        AfterSaleOrderRecord record = new AfterSaleOrderRecord();
                        record.setId(afterSaleOrderRecord.getId());
                        record.setOrderStatus(AfterSaleConstants.OrderStatus.TRADE_CLOSED.getCode());
                        afterSaleOrderRecordService.updateAfterSaleOrderRecord(record);
                        //??????
                    }else if (transform.equals("REFUND")) {
                        String status = obj.getString("status");//??????
                        if(StringUtils.isEmpty(status)){
                            CommonLogUtil.addInfo(null, keyWord+"-??????????????????????????????????????????", null);
                            return;
                        }
                        AfterSaleOrderRecord updateOrder = new AfterSaleOrderRecord();
                        updateOrder.setId(afterSaleOrderRecord.getId());
                        //????????????
                        if("10000".equals(status)){
                            updateOrder.setOrderStatus(AfterSaleConstants.OrderStatus.REFUND_SUCCESS_HAND.getCode());
                            updateOrder.setSubAccountStatus(AfterSaleConstants.OrderSubAccountStatus.STATUS_7.getCode());
                            Date subAccountReturnTime = null;
                            if(obj.getDate("refundTime") != null){
                                subAccountReturnTime=obj.getDate("refundTime");
                            }else{
                                subAccountReturnTime=new Date();
                            }
                            updateOrder.setSubAccountReturnTime(subAccountReturnTime);
                            updateOrder.setSubAccountReturnNo(obj.getString("refund3SerialNo"));
                            CommonLogUtil.addInfo(null, keyWord+"-????????????", null);
                            delCoupon(afterSaleOrderRecord);
                        }else if(afterSaleOrderRecord.getSubAccountStatus() != null && afterSaleOrderRecord.getSubAccountStatus()>1){
                            String statusDesc = obj.getString("statusDesc");
                            CommonLogUtil.addInfo(null, keyWord+"-????????????????????????"+statusDesc, null);
                            updateOrder.setSubAccountStatus(AfterSaleConstants.OrderSubAccountStatus.STATUS_8.getCode());
                            updateOrder.setSubAccountDesc(statusDesc);
                        }
                        afterSaleOrderRecordService.updateAfterSaleOrderRecord(updateOrder);

                    } else {
                        CommonLogUtil.addInfo(null, keyWord+"-?????????????????????transform???"+transform, null);
                    }
                }else {
                    CommonLogUtil.addInfo(null, keyWord+"-???????????????????????????businessType???"+businessType, null);
                }
            }
            StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "GoodsOrderPaySuccessConsumer", "consumeMsg", "????????????????????????????????????  ??????????????????", message);
        } catch (Exception e) {
            StaticLogUtils.error(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "GoodsOrderPaySuccessConsumer", "???????????????????????????????????? error",message,e);
        }
    }

    //???????????????
    public void delCoupon(AfterSaleOrderRecord record1){
        String orderCode= record1.getOrderCode();
        String keyWord = "kafka?????????????????????????????????:"+orderCode+"-kaf";
        Date date = new Date();
        //???????????????
        afterSaleCouponService.delCoupon(orderCode, Arrays.asList(AfterSaleConstants.CouponType.GIFT.getCode()));
        //??????????????????
        CsCompanyDetailDto company = companyBaseService.getCsCompanyDetail(record1.getDealerId());
        WxDataDto dataDto = new WxDataDto("???????????????????????????????????????????????????????????????",
                orderCode,
                company.getCompanyName(),
                "????????????",
                record1.getOrderMoney().toString(),
                DateUtil.formart(date, DateUtil.FORMART_YMD_HMS),
                company.getCompanyName()+" ?????????"+company.getTel()+" ?????????"+company.getAddress());
        WxTemplateInfoDto templateInfoDto = new WxTemplateInfoDto(record1.getUserWxOpenId(), refund_template_id,null, null, dataDto);
        com.tuanche.commons.util.ResultDto resultDto = wxTemplateMessageBaseService.sendNew(aftersale_appid, templateInfoDto);
        CommonLogUtil.addInfo("AfterSaleOrderController",keyWord+"???????????????????????????", JSON.toJSONString(resultDto));
    }

    private void fissionOrderPaySuccess (OrderExtendWithBusinessResponseDto order, String transform, FissionGoodsOrderRecord goodsOrderRecord) {
        try {
            StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "GoodsOrderPaySuccessConsumer", "consumeMsg", "??????????????????????????????????????????  ing", JSON.toJSONString(order));
            //??????
            if (transform.equals("EXPIRED")) {
                StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "GoodsOrderPaySuccessConsumer", "consumeMsg", "??????????????????????????????????????????  ??????????????????", "");
                return;
            } else {
                //????????????????????????
                FissionGoodsOrderRecord orderRecord = new FissionGoodsOrderRecord();
                orderRecord.setOrderNo(order.getOrderCode());
                orderRecord.setOrderStatus(transform.equals("PAYMENT")?FissionGoodsOrderStatus.PAID.getType():FissionGoodsOrderStatus.REFUND_SUCCESS.getType());
                fissionGoodsOrderRecordservice.updateFissionGoodsOrderRecordByOrderNo(orderRecord);
                if (transform.equals("PAYMENT")) {
                    //??????????????????????????????
                    if (order.getActivityId()!=null && order.getActivityId()>0) {
                        FissionGoodsHelperUser helperUser = new FissionGoodsHelperUser();
                        helperUser.setFissionId(goodsOrderRecord.getFissionId());
                        helperUser.setGoodsId(goodsOrderRecord.getGoodsId());
                        helperUser.setUserWxUnionId(goodsOrderRecord.getUserWxUnionId());
                        helperUser.setBuyGoods(Globals.FISSION_GOODS.have_purchased);
                        fissionGoodsHelperUserService.updateGoodsHelperUserToBuy(helperUser);
                    }
                    //??????????????????????????????????????????
                    if ((order.getLiveId()!=null && order.getLiveId()>0) || (order.getActivityId()!=null && order.getActivityId()>0)) {
                        JSONObject json = new JSONObject();
                        if (order.getActivityId()!=null && order.getActivityId()>0) json.put("fissionId", order.getActivityId());
                        if (order.getLiveId()!=null && order.getLiveId()>0) json.put("broadcastRoomId", order.getLiveId());
                        json.put("userId", order.getUserId());
                        json.put("userName", StringUtil.isEmpty(order.getReceiverName()) ? order.getUserNick() : order.getReceiverName());
                        json.put("userPhone", order.getReceiverPhone()==null ? order.getUserPhone() : order.getReceiverPhone());
                        if (StringUtils.isNotEmpty(goodsOrderRecord.getUserWxUnionId())) {
                            json.put("unionId", goodsOrderRecord.getUserWxUnionId());
                            FissionUser user = new FissionUser();
                            user.setUserWxUnionId(goodsOrderRecord.getUserWxUnionId());
                            FissionUser fissionUser = fissionUserService.getFissionUser(user);
                            if (fissionUser!=null) {
                                json.put("userPic", fissionUser.getUserWxHead());
                            }
                        }
                        //broadcastRoomId  userId  userPhone  userName
                        fissionNoticeProducerService.sendLiveOrderMessage(JSON.toJSONString(json));
                    }
                }
            }
        } catch (Exception e) {
            StaticLogUtils.error(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "orderPaySuccess", "?????????????????????????????????????????????????????????????????????error",JSON.toJSONString(order), e);
        }
        if (transform.equals("PAYMENT")) {
            //?????????????????????redis??????????????????
            FissionUserTaskRecordVo recordVo = payServiceImpl.getFissionUserTaskRecord(order.getOrderCode());
            if (recordVo!=null) {
                try {
                    com.tuanche.arch.web.model.TcResponse response = fissionUserTaskRecordService.completeTask(recordVo);
                    StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "GoodsOrderPaySuccessConsumer", "consumeMsg", "??????????????????????????????????????????  ?????????????????????", JSON.toJSONString(response));
                }catch (Exception e) {
                    StaticLogUtils.error(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "orderPaySuccess", "???????????????????????????????????????????????????????????????redis??????????????????error",JSON.toJSONString(recordVo), e);
                }
            }
        }
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

