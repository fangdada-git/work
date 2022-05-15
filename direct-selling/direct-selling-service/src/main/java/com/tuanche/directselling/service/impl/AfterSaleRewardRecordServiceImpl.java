package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.exception.RedisException;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.api.*;
import com.tuanche.directselling.constant.BaseCacheKeys;
import com.tuanche.directselling.dto.*;
import com.tuanche.directselling.enums.AfterSaleRewardTypeEnums;
import com.tuanche.directselling.enums.AfterSaleRewardUserType;
import com.tuanche.directselling.enums.AfterSaleUserType;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleAgentReadMapper;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleAmountAlarmReadMapper;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleKeepOnRecordReadMapper;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleRewardRecordReadMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleAmountAlarmWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleOrderRecordWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleRewardRecordWriteMapper;
import com.tuanche.directselling.model.*;
import com.tuanche.directselling.service.WechatWorkServiceImpl;
import com.tuanche.directselling.util.*;
import com.tuanche.directselling.utils.*;
import com.tuanche.directselling.vo.WeChatPaymentVo;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.api.SmsVerifyCodeService;
import com.tuanche.manubasecenter.api.WxTemplateMessageBaseService;
import com.tuanche.manubasecenter.dto.SmsDto;
import com.tuanche.manubasecenter.dto.WxDataDto;
import com.tuanche.manubasecenter.dto.WxTemplateInfoDto;
import com.tuanche.manubasecenter.model.CsCompany;
import com.tuanche.manubasecenter.util.EmailUtil;
import com.tuanche.merchant.api.payment.IPaymentService;
import com.tuanche.merchant.business.api.commodity.ICommodityBusinessService;
import com.tuanche.merchant.business.dto.request.PageableRequestDto;
import com.tuanche.merchant.business.dto.response.BaseResponseDto;
import com.tuanche.merchant.business.dto.response.commodity.CommodityDetailBusinessResponseDto;
import com.tuanche.merchant.pojo.dto.commodity.PageableDto;
import com.tuanche.merchant.pojo.dto.enums.ResultEnum;
import com.tuanche.merchant.pojo.dto.request.payment.OrderPaymentQueryRequestDto;
import com.tuanche.merchant.pojo.dto.response.payment.PaymentResponseDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.weixin4j.pay.redpack.EnterprisePay;
import org.weixin4j.pay.redpack.EnterprisePayResult;
import org.weixin4j.service.WxPayService;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * @author：HuangHao
 * @CreatTime 2021-07-21 17:50
 */
@Service(retries = 0,timeout = 20000)
public class AfterSaleRewardRecordServiceImpl implements AfterSaleRewardRecordService{

    @Value("${aftersale_pay_mchid}")
    private String aftersale_pay_mchid;
    @Value("${after.sale.activity.index.myCoupon.url}")
    private String index_myCoupon_url;
    @Value("${sms_system_code}")
    private String sms_system_code;
    @Value("${sms_business_code}")
    private String sms_business_code;
    @Value("${sms_business_template_notify}")
    private Integer sms_business_template_notify;
    @Value("${aftersale_app_id}")
    private String aftersale_app_id;
    @Value("#{${after_sale_push_template_ids}}")
    private Map<String, Map<String, String>> after_sale_push_template_map;
    @Autowired
    AfterSaleRewardRecordReadMapper afterSaleRewardRecordReadMapper;
    @Autowired
    AfterSaleKeepOnRecordReadMapper afterSaleKeepOnRecordReadMapper;
    @Autowired
    AfterSaleUserService afterSaleUserService;
    @Autowired
    AfterSaleActivityService afterSaleActivityService;
    @Autowired
    AfterSaleOrderRecordWriteMapper afterSaleOrderRecordWriteMapper;
    @Autowired
    AfterSaleAgentReadMapper afterSaleAgentReadMapper;
    @Autowired
    AfterSaleAgentService afterSaleAgentService;
    @Autowired
    AfterSaleOrderRecordService afterSaleOrderRecordService;
    @Autowired
    AfterSaleCouponService afterSaleCouponService;
    @Autowired
    AfterSaleRewardRecordWriteMapper afterSaleRewardRecordWriteMapper;
    @Autowired
    AfterSaleKeepOnRecordService afterSaleKeepOnRecordService;
    @Autowired
    RedisLockService redisLockService;
    @Reference
    WxTemplateMessageBaseService wxTemplateMessageBaseService;
    @Reference
    private ICommodityBusinessService iCommodityBusinessService;
    @Autowired
    @Qualifier("ClusterRedisService")
    private RedisService redisService;
    @Reference
    WxPayService wxPayService;
    @Reference
    private SmsVerifyCodeService smsVerifyCodeService;
    @Reference
    CompanyBaseService companyBaseService;
    @Reference
    private IPaymentService iPaymentService;
    @Autowired
    private WechatWorkServiceImpl wechatWorkServiceImpl;
    @Autowired
    ExecutorService executorService;
    @Autowired
    private AfterSaleAmountAlarmReadMapper afterSaleAmountAlarmReadMapper;
    @Autowired
    private AfterSaleAmountAlarmWriteMapper afterSaleAmountAlarmWriteMapper;

    static BigDecimal hundred = new BigDecimal("100");
    //微信转账最低支付限额-30分=0.3元
    static int minimum_payment_limit = 30;
    static String logKeyWord = "售后特卖奖励";
    static String SUCCESS = "SUCCESS";

    /**
     * 售后特卖奖励
     * @author HuangHao
     * @CreatTime 2021-07-25 10:05
     * @param order
     * @return com.tuanche.commons.util.ResultDto
     */
    public ResultDto reward(AfterSaleOrderRecord order){
        String uuid = UUID.randomUUID().toString();
        String keyWord = "售后特卖奖励，请求ID:"+order.getUserWxUnionId()+"_logid";
        String log = keyWord+"，活动ID："+order.getActivityId()+
                "，购买人："+order.getUserWxUnionId()+
                "，分享人："+order.getShareWxUnionId()+
                "，代理人："+order.getAgentWxUnionId()+
                "，购买人姓名："+order.getUserName()+
                "，购买人手机："+order.getUserPhone()+
                "，订单编号："+order.getOrderCode();
        CommonLogUtil.addInfo(null, log+"，开始", order);
        ResultDto resultDto = new ResultDto();
        AfterSaleOrderRecord updateOrder = null;
        List<AfterSaleRewardRecord> rewardRecordList = new ArrayList<AfterSaleRewardRecord>();
        RedisLockDto redisLockDto = null;
        String lockKey = null;
        try {
            Integer activityId = order.getActivityId();
            String orderCode = order.getOrderCode();
            //用户微信unionid
            String userWxUnionId = order.getUserWxUnionId();
            Date now = new Date();
            //获取活动信息
            AfterSaleActivityDto afterSaleActivityDto = afterSaleActivityService.getCacheAfterSaleActivityDtoById(activityId);
            if(afterSaleActivityDto == null || afterSaleActivityDto.getSaleEndTime() == null|| now.after(afterSaleActivityDto.getSaleEndTime())){
                resultDto.setCode(StatusCodeEnum.PARAM_IS_INVALID.getCode());
                resultDto.setMsg("活动已经结束");
                CommonLogUtil.addInfo(null, keyWord+"，活动已结束，结束时间"+afterSaleActivityDto.getSaleEndTime(), null);
                return resultDto;
            }
            if(afterSaleActivityDto.getOnState() == 0){
                resultDto.setCode(StatusCodeEnum.PARAM_IS_INVALID.getCode());
                CommonLogUtil.addInfo(null, keyWord+"，活动未开启", null);
                resultDto.setMsg("活动还未开启");
                return resultDto;
            }

            updateOrder = new AfterSaleOrderRecord();
            //代理人微信unionid
            String agentWxUnionId = order.getAgentWxUnionId();
            //是否有代理人
            boolean hasAgent = !StringUtil.isEmpty(agentWxUnionId);
            //是否是团车电销代理人
            boolean isTelesales = false;
            AfterSaleAgent afterSaleAgent = null;
            if(hasAgent){
                afterSaleAgent = afterSaleAgentService.getDealerOrTelesalesAgent(activityId, agentWxUnionId);
                if(afterSaleAgent == null){
                    resultDto.setCode(StatusCodeEnum.PARAM_IS_INVALID.getCode());
                    CommonLogUtil.addInfo(null, keyWord+"，未获取到代理人信息", null);
                    resultDto.setMsg("未获取到代理人信息");
                    return resultDto;
                }else if(AfterSaleConstants.AgentType.TYPE_0.getCode().equals(afterSaleAgent.getType())){
                    //代理人是车商销则渠道也设置成：代理人
                    updateOrder.setChannel(AfterSaleConstants.ChannelType.TYPE_1.getCode());
                }else if(AfterSaleConstants.AgentType.TYPE_1.getCode().equals(afterSaleAgent.getType())){
                    isTelesales = true;
                    //代理人是团车电销则渠道也设置成：团车电销
                    updateOrder.setChannel(AfterSaleConstants.ChannelType.TYPE_2.getCode());
                }
            }
            //分享人微信unionid
            String shareWxUnionId = order.getShareWxUnionId();
            //分享人是否是自己
            boolean shaerIsYourself = false;
            //是否有分享人
            boolean hasSharer = !StringUtil.isEmpty(shareWxUnionId);
            AfterSaleUser shareUserInfo = null;
            if(hasSharer){
                shaerIsYourself = shareWxUnionId.equals(userWxUnionId);
                lockKey = MessageFormat.format(BaseCacheKeys.AFTER_SALE_ACTIVITYID_REWARDRECORD_SHARER_LOCK.getKey(),activityId,shareWxUnionId);
                redisLockDto = redisLockService.lock(lockKey,BaseCacheKeys.AFTER_SALE_ACTIVITYID_REWARDRECORD_SHARER_LOCK.getExpire(),12*1000);
                shareUserInfo = afterSaleUserService.getUser(activityId, shareWxUnionId);
                if(shareUserInfo == null){
                    resultDto.setCode(StatusCodeEnum.PARAM_IS_INVALID.getCode());
                    CommonLogUtil.addInfo(null, keyWord+"，未获取到分享人信息", null);
                    resultDto.setMsg("未获取到分享人信息");
                    return resultDto;
                }
            }
            Integer orderId = order.getId();
            //检测订单是已经发放过奖励
            String orderKey = ApiBaseCacheKeys.AFTER_SALE_ORDER_REWARD.getKey()+orderId;
            long timeout = DateUtil.getDiffSecond(now, afterSaleActivityDto.getSaleEndTime())+3600;
            String isOk = redisService.set(orderKey, "1", "NX", "EX", timeout);
            if(!ConstantsUtil.OK.equals(isOk)){
                updateOrder = null;
                resultDto.setCode(StatusCodeEnum.PARAM_IS_INVALID.getCode());
                resultDto.setMsg("该订单已经发放过奖励");
                CommonLogUtil.addInfo(null, keyWord+"，该订单已经发放过奖励", null);
                return resultDto;
            }
            //用户微信openid
            String userWxOpenId = order.getUserWxOpenId();

            //分享人是否是代理人
            boolean sharerIsAgent = false;
            if(hasSharer && hasAgent && agentWxUnionId.equals(shareWxUnionId)){
                sharerIsAgent = true;
            }
            //业务类型 1 聚力保客  2汽修厂
            Integer businessType = afterSaleActivityDto.getBusinessType();
            businessType = businessType==null?AfterSaleConstants.BusinessType.TYPE_1.getCode():businessType;
            //二级裂变是否给代理人奖励
            boolean secondaryFission = AfterSaleConstants.BusinessType.TYPE_1.getCode().equals(businessType);
            //代理人额外奖励，邀请人数
            Integer agentAwardInviterCount = afterSaleActivityDto.getAgentAwardInviterCount();
            //额外奖励
            BigDecimal agentAwardExtra = afterSaleActivityDto.getAgentAwardExtra();
            //备案用户需裂变用户数
            Integer goodsFissionCount = afterSaleActivityDto.getGoodsFissionCount();
            int keepOnRecordFissionNum = goodsFissionCount==null||goodsFissionCount<0?0:goodsFissionCount;
            //分享人是否有订单
            boolean sharerHasOrder = false;
            //分享人订单信息
            AfterSaleOrderRecord shareUserOrder = null;
            //分享人是否是备案人
            boolean shareIsKeepOnRecordUser = false;
            if(hasSharer && !shaerIsYourself){
                shareUserOrder = afterSaleOrderRecordService.getBuyerOrder(activityId, shareWxUnionId,AfterSaleConstants.ActivityCoupon.TYPE_PROMOTION_CARD.getCode());
                if(shareUserOrder != null){
                    sharerHasOrder=hasOrder(shareUserOrder);
                    shareIsKeepOnRecordUser = shareUserOrder!=null && shareUserOrder.getUserType() != null && shareUserOrder.getUserType().equals(AfterSaleUserType.KEEP_ON_USER.getType())?true:false;
                }
            }
            //获取用户类型  0：备案用户 1：流失用户 2：普通用户
            Integer userType = order.getUserType();
            //购买用户是否是备案用户
            boolean isKeepOnRecord = order.getKeepOnRecordUser();
            //当前购买人的层级
            if(sharerIsAgent || shareUserOrder == null || shareUserOrder.getFissionLevel()==null){
                updateOrder.setFissionLevel(1);
            }else{
                updateOrder.setFissionLevel(shareUserOrder.getFissionLevel()+1);
            }
            //是否有礼品券
            boolean hasGiftCoupon = !StringUtils.isEmpty(afterSaleActivityDto.getGiftCouponName()) && afterSaleActivityDto.getGiftCouponName().trim().length()>0;
            int fissionLevel = updateOrder.getFissionLevel().intValue();
            CommonLogUtil.addInfo(null, keyWord+"，代理人是否是团车电销："+isTelesales+"，购买人的用户类型："+userType+
                    "，购买人是否是备案用户："+isKeepOnRecord+"，分享人是否是代理人："+sharerIsAgent+
                    "，分享人是否是备案用户："+shareIsKeepOnRecordUser+"，分享人是否存在订单且没有退款："+sharerHasOrder+
                    "，备案人需要裂变的人数："+keepOnRecordFissionNum+
                    "，当前用户的购买层级："+fissionLevel+"，是否有礼品券："+hasGiftCoupon, null);
            String buyRewardDesc = "邀请好友参与立得"+afterSaleActivityDto.getBuyerAwardBase()+"元奖励，" +
                    "每邀请"+afterSaleActivityDto.getShareAwardInviterCount()+"人额外得"+afterSaleActivityDto.getShareAwardExtra()+"元，上不封顶！";
            //备案用户
            if(isKeepOnRecord){
                //如果备案人需要裂变的人数>0 则初始默认未完成裂变任务
                if(keepOnRecordFissionNum>0){
                    updateOrder.setFinishFissionTask(false);
                }else{
                    updateOrder.setFinishFissionTask(true);
                }
                String msg = "欢迎"+order.getLicencePlate()+"车主购买保养服务卡，您需要邀请"+afterSaleActivityDto.getGoodsFissionCount()+"位用户拼团卡券才可生效，否则将在活动结束后自动退款。微信打开购买页转朋友拼团，朋友与您均得「现金奖励」"+(hasGiftCoupon?"和「"+afterSaleActivityDto.getGiftCouponName()+"」礼券！":"！");
                sendMessage(order.getUserPhone(),msg,order.getCityId());
                CommonLogUtil.addInfo(null, keyWord+"，购买人是备案用户，无奖励", null);
                //备案用户的购买奖励-未生效
                ineffectiveReward(keyWord, activityId, AfterSaleRewardUserType.USER_TYPE2,
                        AfterSaleRewardTypeEnums.REWARD_TYPE2, userWxUnionId,
                        afterSaleActivityDto.getBuyerAwardBase(), orderCode, rewardRecordList);
                if(hasGiftCoupon){
                    //备案用户的礼品券-未生效
                    ineffectiveReward(keyWord, activityId, AfterSaleRewardUserType.USER_TYPE2,
                            AfterSaleRewardTypeEnums.REWARD_TYPE3, userWxUnionId,
                            BigDecimal.ONE, orderCode, rewardRecordList);
                }

            //普通用户
            }else{
                updateOrder.setFinishFissionTask(true);
                if(hasGiftCoupon){
                    ///分享层级大于直接给礼券否则未生效
                    if(fissionLevel>1){
                        issueGiftCertificates(keyWord,AfterSaleConstants.PushType.TYPE2.getCode(),orderCode, activityId, userWxUnionId,userWxOpenId,
                                AfterSaleRewardUserType.USER_TYPE2.getType(),afterSaleActivityDto,rewardRecordList);
                    }else{
                        //分享人是代理人-礼品券-未生效
                        ineffectiveReward(keyWord, activityId, AfterSaleRewardUserType.USER_TYPE2,
                                AfterSaleRewardTypeEnums.REWARD_TYPE3, userWxUnionId,
                                BigDecimal.ONE, orderCode, rewardRecordList);
                    }
                }
                CommonLogUtil.addInfo(null, keyWord+"，发放购买人的购买奖励", null);
                 //购买人发放购买奖励
                buyerReward(keyWord,buyRewardDesc,
                                        afterSaleActivityDto.getBuyerAwardBase(),
                                        AfterSaleRewardTypeEnums.REWARD_TYPE2.getType(),
                                        afterSaleActivityDto, order, order.getUserWxOpenId(), rewardRecordList);
                //购买推广卡通知
                promotionCardNotice(keyWord,AfterSaleConstants.PushType.TYPE2.getCode(),order, afterSaleActivityDto);
            }
            //如果是自己购买自己的不给分享人和代理人奖励
            if(!shaerIsYourself && hasSharer){
                //分享人奖励 :【分享人不是代理人】
                if(!sharerIsAgent){
                    //分享人是否完成了裂变任务
                    boolean shareFinishFissionTask = shareUserOrder==null || shareUserOrder.getFinishFissionTask() == null?false:shareUserOrder.getFinishFissionTask();
                    //【分享人不是备案人（未购买的分享人也不是备案人）】或者【分享人是备案人但需要裂变的人数=0】或者【分享人是备案人但已经完成了裂变任务】
                    if(!shareIsKeepOnRecordUser || keepOnRecordFissionNum == 0 || (shareIsKeepOnRecordUser && shareFinishFissionTask)){
                        CommonLogUtil.addInfo(null, keyWord+"，分享人奖励已购买-发放礼券", null);
                        //分享人有订单才给礼券
                        if(sharerHasOrder){
                            issueGiftCertificates(keyWord,AfterSaleConstants.PushType.TYPE3.getCode(),shareUserOrder.getOrderCode(), activityId, shareWxUnionId,shareUserInfo.getUserWxOpenId(),
                                    AfterSaleRewardUserType.USER_TYPE3.getType(),afterSaleActivityDto,rewardRecordList);
                            CommonLogUtil.addInfo(null, keyWord+"，分享人的【分享奖励】发放", null);
                        }
                        CommonLogUtil.addInfo(null, keyWord+"，发放分享人的分享奖励", null);
                        //分享人的分享奖励
                        sharerReward(keyWord,"您推荐的["+order.getLicencePlate() + "]红包奖励，继续推荐再得红包",
                                afterSaleActivityDto.getShareAwardBase(),
                                AfterSaleRewardTypeEnums.REWARD_TYPE1.getType(),
                                afterSaleActivityDto, order, shareUserInfo.getUserWxOpenId(), rewardRecordList);
                        //分享人的额外邀请人数
                        Integer shareAwardInviterCount = afterSaleActivityDto.getShareAwardInviterCount();
                        BigDecimal shareAwardExtra = afterSaleActivityDto.getShareAwardExtra();
                        //判断是否发放额外奖励：【邀请的额外奖励人数>0】 且【额外奖励金额>0】
                        if (shareAwardInviterCount != null && shareAwardInviterCount > 0 &&
                                shareAwardExtra != null && shareAwardExtra.compareTo(BigDecimal.ZERO) > 0) {
                            AfterSaleOrderRecord shareOrderRecord = new AfterSaleOrderRecord();
                            shareOrderRecord.setActivityId(activityId);
                            shareOrderRecord.setShareWxUnionId(shareWxUnionId);
                            if(shareUserOrder != null && shareUserOrder.getPayTime() != null){
                                shareOrderRecord.setPayTime(shareUserOrder.getPayTime());
                            }
                            //裂变的人数
                            int fissionTotal = afterSaleOrderRecordService.getFissionTotal(shareOrderRecord);
                            //是否发放额外奖励
                            boolean isExtraReward = fissionTotal % shareAwardInviterCount.intValue() == 0;
                            CommonLogUtil.addInfo(null, keyWord+"，分享人当前已经裂变的非备案人数："+fissionTotal+"，每额外邀请人数："+shareAwardInviterCount.intValue()+"，已邀请："+fissionTotal+"人，是否发放额外奖励："+isExtraReward+"，额外奖励金额："+shareAwardExtra, null);
                            if (fissionTotal>0 && isExtraReward) {
                                //分享人的分享额外奖励
                                sharerReward(keyWord,"您推荐第"+fissionTotal+"人红包奖励，继续推荐再得红包",
                                        shareAwardExtra,
                                        AfterSaleRewardTypeEnums.REWARD_TYPE4.getType(),
                                        afterSaleActivityDto, order, shareUserInfo.getUserWxOpenId(), rewardRecordList);
                            }
                        }else{
                            CommonLogUtil.addInfo(null, keyWord+"，分享人达不到额外奖励条件，不发放额外奖励", null);
                        }
                        //如果【分享人是备案人】 且 【需要裂变的人数>0】 且 【未完成裂变任务】 补发奖励
                    }else if(shareIsKeepOnRecordUser && keepOnRecordFissionNum>0 && !shareFinishFissionTask){
                        AfterSaleOrderRecord afterSaleOrderRecord = new AfterSaleOrderRecord();
                        afterSaleOrderRecord.setActivityId(activityId);
                        afterSaleOrderRecord.setShareWxUnionId(shareWxUnionId);
                        afterSaleOrderRecord.setPayTime(shareUserOrder.getPayTime());
                        List<AfterSaleOrderRecord> fissionList = afterSaleOrderRecordService.getFissionList(afterSaleOrderRecord);
                        //裂变的人数
                        int fissionTotal = (fissionList==null?0:fissionList.size());
                        boolean finishFissionTask = fissionTotal == keepOnRecordFissionNum;
                        CommonLogUtil.addInfo(null, keyWord+"，【分享人是备案人】-备案分享人是否完成了裂变任务："+finishFissionTask, null);
                        //如果备案人已邀请的裂变人数刚好等于 需要裂变的人数 则给代理人发奖励
                        if(fissionTotal>0 && finishFissionTask){
                            //补发放成功更改备案【分享人的订单】表【是否完成了裂变任务】字段为【已完成】
                            AfterSaleOrderRecord orderRecord = new AfterSaleOrderRecord();
                            orderRecord.setFinishFissionTask(true);
                            orderRecord.setId(shareUserOrder.getId());
                            Integer updateNum = afterSaleOrderRecordService.updateAfterSaleOrderRecord(orderRecord);

                            CommonLogUtil.addInfo(null, keyWord+"，【分享人是备案人】-补发分享人的礼品券", null);
                            //补发分享人的礼券
                            issueGiftCertificates(keyWord,AfterSaleConstants.PushType.TYPE3.getCode(),shareUserOrder.getOrderCode(), activityId, shareWxUnionId,shareUserInfo.getUserWxOpenId(),
                                    AfterSaleRewardUserType.USER_TYPE3.getType(),afterSaleActivityDto,rewardRecordList);

                            //补发分享人的购买奖励
                            CommonLogUtil.addInfo(null, keyWord+"，【分享人是备案人】-分享人的购买奖励-当前购买人的", null);
                            buyerReward(keyWord,buyRewardDesc,
                                    afterSaleActivityDto.getBuyerAwardBase(),
                                    AfterSaleRewardTypeEnums.REWARD_TYPE2.getType(),
                                    afterSaleActivityDto, shareUserOrder, shareUserInfo.getUserWxOpenId(), rewardRecordList);

                            //【有代理人】且 【代理人不是电销的】且【裂变级别<4（其实就是2和3的级别）】且
                            if(hasAgent && !isTelesales && fissionLevel < 4 ){
                                //【给代理人发放2级裂变奖励】或者【不发放2级奖励但是当前是2级（级别=2说明分享人的级别是1）】
                                if(secondaryFission || (!secondaryFission && fissionLevel==2)){
                                    //补发代理人分享奖励-当前分享人的
                                    CommonLogUtil.addInfo(null, keyWord+"，【分享人是备案人】-补发代理人分享奖励-当前分享人的", null);
                                    agentReward(keyWord, activityId,"您推荐的[" + shareUserOrder.getLicencePlate() + "]红包奖励，继续推荐再得红包",
                                            afterSaleActivityDto.getAgentAwardBase(),
                                            AfterSaleRewardTypeEnums.REWARD_TYPE1.getType(),
                                            shareUserOrder, afterSaleAgent, rewardRecordList);
                                }
                                //如果当前的级别=2说明分享人的级别是1，因此会触发额外奖励机制
                                if(fissionLevel==2){
                                    agentExtraReward(keyWord,activityId,agentWxUnionId,afterSaleAgent.getCreateDt(),agentAwardInviterCount,
                                            agentAwardExtra,shareUserOrder,
                                            afterSaleAgent,rewardRecordList);
                                }
                            }else{
                                CommonLogUtil.addInfo(null, keyWord+"，【分享人是备案人】-代理人是团车电销或者超出裂变级别，不补发奖励", null);
                            }
                            //补发分享人和代理人的分享奖励 ,补发的次数=备案人需要裂变的次数
                            for (int i = 0; i < fissionList.size(); i++) {
                                AfterSaleOrderRecord buyerOrder = fissionList.get(i);
                                CommonLogUtil.addInfo(null, keyWord+"，【分享人是备案人】-补发分享人的分享奖励，第"+(i+1)+"次", null);
                                sharerReward(keyWord,"您推荐的["+buyerOrder.getLicencePlate() + "]红包奖励，继续推荐再得红包",
                                        afterSaleActivityDto.getShareAwardBase(),
                                        AfterSaleRewardTypeEnums.REWARD_TYPE1.getType(),
                                        afterSaleActivityDto, buyerOrder, shareUserInfo.getUserWxOpenId(), rewardRecordList);
                                //购买人是否【是普通用户或完成裂变的备案用户】
                                boolean buyerFinishFissionTask =  buyerOrder.getFinishFissionTask()!=null && buyerOrder.getFinishFissionTask();
                                //【有代理人】且 【代理人不是电销的】且【是普通用户或完成裂变的备案用户】且【当前的裂变级别=2】且【给代理人发放2级裂变奖励】的话补发奖励
                                if(hasAgent && !isTelesales && buyerFinishFissionTask && fissionLevel ==2 && secondaryFission){
                                    CommonLogUtil.addInfo(null, keyWord+"，【分享人是备案人】-补发代理人的分享奖励-分享人邀请的，第"+(i+1)+"次", null);
                                    //补发代理人分享基础奖励-分享人邀请的
                                    agentReward(keyWord,activityId,"您推荐的["+buyerOrder.getLicencePlate() + "]红包奖励，继续推荐再得红包",
                                            afterSaleActivityDto.getAgentAwardBase(),
                                            AfterSaleRewardTypeEnums.REWARD_TYPE1.getType(),
                                            buyerOrder, afterSaleAgent, rewardRecordList);
                                }
                            }
                            //补发分享人的额外奖励
                            //分享人的额外邀请人数
                            Integer shareAwardInviterCount = afterSaleActivityDto.getShareAwardInviterCount();
                            BigDecimal shareAwardExtra = afterSaleActivityDto.getShareAwardExtra();
                            //判断是否发放额外奖励：【邀请的额外奖励人数>0】 且【额外奖励金额>0】
                            if (shareAwardInviterCount != null && shareAwardInviterCount > 0 &&
                                    shareAwardExtra != null && shareAwardExtra.compareTo(BigDecimal.ZERO) > 0) {
                                int inviterCount = shareAwardInviterCount.intValue();
                                //是否发放额外奖励
                                boolean isExtraReward = fissionTotal >= inviterCount;
                                CommonLogUtil.addInfo(null, keyWord+"，【补发分享人的分享额外奖励】，分享人当前已经裂变的非备案人数："+fissionTotal+"，每额外邀请人数："+inviterCount+"，已邀请："+fissionTotal+"人，是否发放额外奖励："+isExtraReward, null);
                                if (isExtraReward) {
                                    int erIndex = fissionTotal/inviterCount;
                                    for (int i = 1; i <= erIndex; i++) {
                                        int num = i*inviterCount;
                                        CommonLogUtil.addInfo(null, keyWord+"，【补发分享人的分享额外奖励】，额外推荐的第"+num+"人", null);
                                        //分享人的分享额外奖励
                                        sharerReward(keyWord,"您推荐第"+num+"人红包奖励，继续推荐再得红包",
                                                shareAwardExtra,
                                                AfterSaleRewardTypeEnums.REWARD_TYPE4.getType(),
                                                afterSaleActivityDto, order, shareUserInfo.getUserWxOpenId(), rewardRecordList);
                                    }
                                }
                            }else{
                                CommonLogUtil.addInfo(null, keyWord+"，分享人达不到额外奖励条件，不发放额外奖励", null);
                            }
                        }else{
                            //备案人未完成裂变任务-分享奖励未生效
                            ineffectiveReward(keyWord, activityId, AfterSaleRewardUserType.USER_TYPE3,
                                    AfterSaleRewardTypeEnums.REWARD_TYPE1, shareWxUnionId,
                                    afterSaleActivityDto.getShareAwardBase(), orderCode, rewardRecordList);
                        }
                    }else{
                        CommonLogUtil.addInfo(null, keyWord+"，【分享人是代理人】-不发放分享奖励", null);
                    }
                }else{
                    CommonLogUtil.addInfo(null, keyWord+"，分享人不满足发放奖励条件，不发放分享奖励", null);
                }
                // 代理人的奖励:【有代理人】且【代理人不是团车电销】且【分享人是代理人】或者【分享人不是代理人但分享人有订单】且【给代理人发放2级裂变奖励】且【裂变级别<3】
                if(hasAgent && !isTelesales && (sharerIsAgent ||(!sharerIsAgent && sharerHasOrder && secondaryFission && fissionLevel<3))) {
                    //（【购买人不是备案人】且（【当前的购买级别=1】或者（【当前的购买级别=2】且【给代理人发放2级裂变奖励】且（【分享人不是备案人】或者【分享人是备案人但需要裂变的人数=0】或者 【享人是备案人但已经完成了裂变任务】）））
                    if( !isKeepOnRecord &&
                            (fissionLevel==1 || (fissionLevel==2 &&
                                    (!shareIsKeepOnRecordUser ||
                                            (shareIsKeepOnRecordUser && (keepOnRecordFissionNum == 0 || (shareUserOrder != null && shareUserOrder.getFinishFissionTask() != null && shareUserOrder.getFinishFissionTask()))))))){
                        CommonLogUtil.addInfo(null, keyWord + "，代理人的分享奖励发放", null);
                        //发放代理人分享基础奖励
                        agentReward(keyWord, activityId,"您推荐的[" + order.getLicencePlate() + "]红包奖励，继续推荐再得红包",
                                afterSaleActivityDto.getAgentAwardBase(),
                                AfterSaleRewardTypeEnums.REWARD_TYPE1.getType(),
                                 order, afterSaleAgent, rewardRecordList);
                        //【分享人是代理人】  且【邀请的额外奖励人数>0】 且【额外奖励金额>0】
                        if (sharerIsAgent) {
                            //代理人额外奖励
                            agentExtraReward(keyWord,activityId,agentWxUnionId,afterSaleAgent.getCreateDt(),agentAwardInviterCount,
                                    agentAwardExtra,order,
                                    afterSaleAgent,rewardRecordList);
                        } else {
                            CommonLogUtil.addInfo(null, keyWord + "，代理人达不到额外奖励条件，不发放额外奖励", null);
                        }
                    } else {
                        //代理人的分享奖励-未生效
                        ineffectiveReward(keyWord, activityId, AfterSaleRewardUserType.USER_TYPE1,
                                AfterSaleRewardTypeEnums.REWARD_TYPE1, agentWxUnionId,
                                afterSaleActivityDto.getAgentAwardBase(), orderCode, rewardRecordList);
                        CommonLogUtil.addInfo(null, keyWord + "，达不到代理人奖励条件，不发放奖励", null);
                    }
                } else {
                    CommonLogUtil.addInfo(null, keyWord + "，【无代理人】或者【分享人无订单】或者【分享人有订单但超过2级裂变】，不发放奖励", null);
                }
            }
        }catch (Exception e){
            CommonLogUtil.addError(null, keyWord+" 发生系统错误", e);
            e.printStackTrace();
        }finally {
            //保存奖励记录
            if(rewardRecordList.size() > 0){
                List<AfterSaleRewardRecord> updateRewardRecordList = new ArrayList<>();
                List<AfterSaleRewardRecord> addRewardRecordList = new ArrayList<>();
                for (AfterSaleRewardRecord record : rewardRecordList) {
                    if(record.getId() != null){
                        updateRewardRecordList.add(record);
                    }else{
                        addRewardRecordList.add(record);
                    }
                }
                if(addRewardRecordList.size() > 0){
                    afterSaleRewardRecordWriteMapper.batchInsert(addRewardRecordList);
                }
                if(updateRewardRecordList.size() > 0){
                    afterSaleRewardRecordWriteMapper.batchUpdate(updateRewardRecordList);
                }
            }
            if(updateOrder !=null){
                updateOrder.setId(order.getId());
                afterSaleOrderRecordWriteMapper.updateByPrimaryKeySelective(updateOrder);
                if (updateOrder.getChannel()!=null && updateOrder.getChannel().equals(AfterSaleConstants.ChannelType.TYPE_2.getCode())) {
                    AfterSaleOrderRecordDto dto = new AfterSaleOrderRecordDto();
                    dto.setActivityId(order.getActivityId());
                    dto.setOrderCode(order.getOrderCode());
                    dto.setUserWxUnionId(order.getUserWxUnionId());
                    wechatWorkServiceImpl.setAfterSaleOrderWechatworkConcact(dto);
                }
            }
            if(redisLockDto != null){
                redisLockService.unLock(lockKey,redisLockDto);
            }
        }
        resultDto.setCode(StatusCodeEnum.SUCCESS.getCode());
        resultDto.setMsg(StatusCodeEnum.SUCCESS.getText());
        return resultDto;
    }

    /**
     * 代理人的额外奖励
     * @return void
     */
    public void agentExtraReward(String keyWord,Integer activityId,String agentWxUnionId,Date createDt,Integer agentAwardInviterCount,
                   BigDecimal agentAwardExtra,AfterSaleOrderRecord order,
                   AfterSaleAgent afterSaleAgent,List<AfterSaleRewardRecord> rewardRecordList){
        if (agentAwardInviterCount == null || agentAwardInviterCount < 1 ||
                agentAwardExtra == null || agentAwardExtra.compareTo(BigDecimal.ZERO) < 1){
            CommonLogUtil.addInfo(null, keyWord + "，代理人的额外奖励邀请人数为"+agentAwardInviterCount+
                    "，额外奖励金额为："+agentAwardExtra+"，不发放额外奖励：" , null);
            return;
        }
        AfterSaleOrderRecord afterSaleOrderRecord = new AfterSaleOrderRecord();
        afterSaleOrderRecord.setActivityId(activityId);
        afterSaleOrderRecord.setShareWxUnionId(agentWxUnionId);
        afterSaleOrderRecord.setCreateDt(createDt);
        afterSaleOrderRecord.setKeepOnRecordUser(true);
        //裂变的人数
        int fissionTotal = afterSaleOrderRecordService.getFissionTotal(afterSaleOrderRecord);
        //是否发放额外奖励
        boolean isExtraReward = fissionTotal % agentAwardInviterCount.intValue() == 0;
        CommonLogUtil.addInfo(null, keyWord + "，代理人当前已经裂变的非备案人数：" + fissionTotal + "，额外邀请人数：" + agentAwardInviterCount.intValue() + "，是否发放额外奖励：" + isExtraReward, null);
        if (fissionTotal>0 && isExtraReward) {
            //发放代理人额外奖励
            agentReward(keyWord, activityId,"您推荐第" + fissionTotal + "人红包奖励，继续推荐再得红包",
                    agentAwardExtra,
                    AfterSaleRewardTypeEnums.REWARD_TYPE4.getType(),
                    order, afterSaleAgent, rewardRecordList);
        }
    }

    /**
     * 发放礼品券
     * @author HuangHao
     * @CreatTime 2021-07-23 16:15
     * @param orderCode
     * @param activityId
     * @param unionId
     * @return void
     */
    public void issueGiftCertificates(String keyWord,int pushType,String orderCode,Integer activityId, String wxUnionId,String wxOpenId,
                                      Integer userType,AfterSaleActivityDto activityDto,List<AfterSaleRewardRecord> rewardRecordList){
        if(!StringUtils.isEmpty(activityDto.getGiftCouponName()) && activityDto.getGiftCouponName().trim().length() > 0){
            Integer rewardType = AfterSaleRewardTypeEnums.REWARD_TYPE3.getType();
            if(hasRewardRecord(rewardRecordList,orderCode,userType,rewardType)){
                return ;
            }
            AfterSaleRewardRecord rewardRecord = getRewardRecordByType(activityId, wxUnionId,rewardType);
            boolean sendStatus = false;
            //没发过奖励或未生效或失败才发
            if(rewardRecord == null ||
                    (rewardRecord.getPayStatus() != null &&
                            (AfterSaleConstants.RewardPayStatus.PayStatus2.getCode()==rewardRecord.getPayStatus().intValue() ||
                             AfterSaleConstants.RewardPayStatus.PayStatus4.getCode()==rewardRecord.getPayStatus().intValue()))) {
                sendStatus = true;
                if(rewardRecord ==null){
                    rewardRecord = new AfterSaleRewardRecord();
                    rewardRecord.setActivityId(activityId);
                    rewardRecord.setUserType(userType);
                    rewardRecord.setUserWxUnionId(wxUnionId);
                    rewardRecord.setRewardType(rewardType);
                    rewardRecord.setQuantity(BigDecimal.ONE);
                    rewardRecord.setOrderCode(orderCode);
                }
                //发放礼品券
                try {
                    ResultDto couponResultDto= afterSaleCouponService.giveOutCoupon(orderCode, AfterSaleConstants.CouponGiveOutType.USER_GIFT);
                    if(StatusCodeEnum.SUCCESS.getCode() == couponResultDto.getCode()){
                        rewardRecord.setPayStatus(AfterSaleConstants.RewardPayStatus.PayStatus1.getCode());
                        rewardRecord.setFailureReason(AfterSaleConstants.ErrCode.SUCCESS.getCode());
                        rewardRecord.setErrCode(AfterSaleConstants.ErrCode.SUCCESS.getCode());
                    }else{
                        rewardRecord.setPayStatus(AfterSaleConstants.RewardPayStatus.PayStatus2.getCode());
                        rewardRecord.setFailureReason(couponResultDto.getMsg());
                        rewardRecord.setErrCode(AfterSaleConstants.ErrCode.TC_FAIL.getCode());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    CommonLogUtil.addError(null, keyWord+"，发放礼品券的AfterSaleRewardRecordServiceImpl.issueGiftCertificates()方法发生系统错误，", e);
                    rewardRecord.setPayStatus(AfterSaleConstants.RewardPayStatus.PayStatus2.getCode());
                    rewardRecord.setFailureReason("礼品券方法发生系统错误");
                    rewardRecord.setErrCode(AfterSaleConstants.ErrCode.TC_ERROR.getCode());
                }
                rewardRecordList.add(rewardRecord);
                ResultDto resultDto = giftCouponNotice(keyWord,pushType,orderCode, wxOpenId, activityDto);
                CommonLogUtil.addInfo(null, keyWord+"，发放礼品券成功，微信通知结果：", resultDto);
            }
            CommonLogUtil.addInfo(null, keyWord+"，是否发放礼品券："+sendStatus, null);
        }else{
            CommonLogUtil.addInfo(null, keyWord+"，无礼券信息，不发放礼券", null);
        }

    }


    /**
     * 购买推广卡通知
     * @author HuangHao
     * @CreatTime 2021-07-27 14:33
     * @param pushType
     * @param orderCode
     * @param wxOpenId
     * @param activityDto
     * @param phone
     * @return void
     */
    public void promotionCardNotice(String keyWord,int pushType,AfterSaleOrderRecord order,AfterSaleActivityDto activityDto){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                giftCouponNotice(keyWord,AfterSaleConstants.PushType.TYPE1.getCode(),order.getOrderCode(), order.getUserWxOpenId(), activityDto);
                String dealerName = activityDto.getDealerName()==null?"":activityDto.getDealerName();
                String msg = dealerName+"欢迎"+order.getLicencePlate()+
                        "车主"+order.getOrderMoney()+"元买"+getCommodityName(activityDto.getGoodsId())+
                        "，微信打开购买页转朋友拼团，朋友与您均得「现金奖励」"+(StringUtils.isEmpty(activityDto.getGiftCouponName())?"":"和「"+activityDto.getGiftCouponName()+"」礼券！");
                sendMessage(order.getUserPhone(),msg,order.getCityId());
            }
        });
    }

    //发送短信
    public void sendMessage(String phone,String msg,Integer cityId){
        SmsDto smsDto = new SmsDto();
        smsDto.setCityId(cityId==null?10:cityId);
        smsDto.setSystemCode(sms_system_code);
        smsDto.setBusinessCode(sms_business_code);
        smsDto.setPhones(phone);
        smsDto.setTemplateId(sms_business_template_notify);
        smsDto.setParamArray(new String[]{msg});
        ResultDto resultDto = smsVerifyCodeService.sendSmsByEmptyTemplant(smsDto);
        CommonLogUtil.addInfo(null, "售后特卖发短信，号码："+phone+"，发送内容："+msg+"，结果：", resultDto);
    }


    /**
     * 礼品券发放通知
     * @author HuangHao
     * @CreatTime 2021-07-25 17:14
     * @param orderCode
     * @param wxOpenId
     * @param activityDto
     * @return void
     */
    public ResultDto giftCouponNotice(String keyWord,int pushType,String orderCode,String wxOpenId,AfterSaleActivityDto activityDto){
        ResultDto resultDto = new ResultDto();
        String first = "尊敬的用户，您已购买成功，请点击查收卡券！";
        String keyword1="";
        String keyword4="";
        if(pushType == AfterSaleConstants.PushType.TYPE1.getCode()){
            first = "尊敬的用户，您已购买成功，请点击查收卡券！";
            String commodityName = getCommodityName(activityDto.getGoodsId());
            keyword1=commodityName;
            keyword4=commodityName;
        }else if(pushType == AfterSaleConstants.PushType.TYPE2.getCode()){
            first = "拼单成功，奖励您车"+activityDto.getGiftCouponName()+"，请点击查收卡券";
            keyword4=activityDto.getGiftCouponName();
            keyword1=activityDto.getGiftCouponName();
        }else if(pushType == AfterSaleConstants.PushType.TYPE3.getCode()){
            first = "尊敬的用户，您已邀请朋友购买成功，请点击查收卡券！";
            keyword1=activityDto.getGiftCouponName();
            keyword4=activityDto.getGiftCouponName();
        }
        try {
            WxDataDto wxDataDto = new WxDataDto(
                    first,
                    keyword1,
                    orderCode,
                    DateUtil.formart(activityDto.getOfflineConvertEndTime(),DateUtil.FORMART_YMD),
                    keyword4,
                    null,
                    "请到"+(activityDto.getDealerName()==null?"":activityDto.getDealerName())+
                            "兑换使用 电话："+(activityDto.getDealerTel()==null?"":activityDto.getDealerTel())+
                            " 地址："+(activityDto.getDealerAddress()==null?"":activityDto.getDealerAddress())+
                            "  时间："+DateUtil.formart(activityDto.getOfflineConvertStartTime(),DateUtil.FORMART_YMD)+"至"+DateUtil.formart(activityDto.getOfflineConvertEndTime(),DateUtil.FORMART_YMD));

            WxTemplateInfoDto templateInfoDto = new WxTemplateInfoDto(
                    wxOpenId
                    , after_sale_push_template_map.get("lipinquan").get("templateid")
                    , index_myCoupon_url+activityDto.getId(),
                    null,
                    wxDataDto);
            resultDto = wxTemplateMessageBaseService.sendNew(aftersale_app_id, templateInfoDto);
        }catch (Exception e){
            resultDto.setMsg("微信推送信息发生系统错误");
            resultDto.setCode(StatusCodeEnum.ERROR.getCode());
            CommonLogUtil.addInfo(null, keyWord+"，微信推送信息发生系统错误", null);
        }
        return resultDto;
    }

    /**
     * 获取商品名称
     * @author HuangHao
     * @CreatTime 2021-07-27 14:36
     * @param
     * @return java.lang.String
     */
    public String getCommodityName(Integer goodsId){
        BaseResponseDto<CommodityDetailBusinessResponseDto> goodsDetail = iCommodityBusinessService.getCommodityDetailWithExtendByCommodityId(goodsId);
        return goodsDetail!=null&&goodsDetail.getData()!=null?goodsDetail.getData().getCommodityName():"";
    }


    /**
     * 代理人发放现金奖励
     * @author HuangHao
     * @CreatTime 2021-07-23 11:11
     * @param desc  奖励描述
     * @param rewardAmount  奖励金额
     * @param rewardType    奖励类型
     * @param hasAgent      是否有代理人
     * @param sharerIsAgent 代理人是否是分享人
     * @param afterSaleActivityDto  活动信息
     * @param order 订单信息
     * @param afterSaleAgent 代理人信息
     * @param shareUserOrder    分享人订单信息
     * @param rewardRecordList
     * @return com.tuanche.commons.util.ResultDto
     */
    public ResultDto agentReward(String keyWord,Integer activityId,String desc,BigDecimal rewardAmount,Integer rewardType,
                                 AfterSaleOrderRecord order,
                                 AfterSaleAgent afterSaleAgent,
                                 List<AfterSaleRewardRecord> rewardRecordList){
        ResultDto resultDto = new ResultDto();
        //代理人奖励 【有代理人】且 【奖励金额大于0】 才给代理人奖励
        if(afterSaleAgent!=null  &&  rewardAmount != null && rewardAmount.compareTo(BigDecimal.ZERO) > 0){
            Integer userType = AfterSaleRewardUserType.USER_TYPE1.getType();
            if(hasRewardRecord(rewardRecordList,order.getOrderCode(),userType,rewardType)){
                return resultDto;
            }
            AfterSaleRewardRecord rewardRecord = getOrderRewardRecord(activityId, order.getOrderCode(),userType,rewardType);
            //没发过奖励或未生效或失败才发
            if(rewardRecord == null ||
                    (rewardRecord.getPayStatus() != null &&
                            (AfterSaleConstants.RewardPayStatus.PayStatus2.getCode()==rewardRecord.getPayStatus().intValue() ||
                             AfterSaleConstants.RewardPayStatus.PayStatus4.getCode()==rewardRecord.getPayStatus().intValue()))) {
                String orderCode = order==null?null:order.getOrderCode();
                WeChatPaymentVo agentWeChatPaymentVo = new WeChatPaymentVo();
                agentWeChatPaymentVo.setOpenId(afterSaleAgent.getAgentWxOpenId());
                agentWeChatPaymentVo.setOrder_no(orderCode);
                int extraAmount = 0;
                agentWeChatPaymentVo.setDesc(desc);
                //发放金额=基础奖励金额
                agentWeChatPaymentVo.setAmount(hundred.multiply(rewardAmount).intValue());
                if(rewardRecord ==null){
                    rewardRecord = new AfterSaleRewardRecord();
                    rewardRecord.setActivityId(afterSaleAgent.getActivityId());
                    rewardRecord.setUserType(userType);
                    rewardRecord.setUserWxUnionId(afterSaleAgent.getAgentWxUnionId());
                    rewardRecord.setRewardType(rewardType);
                    rewardRecord.setQuantity(rewardAmount);
                    rewardRecord.setOrderCode(orderCode);
                }
                //去微信支付
                resultDto = wechatPayment(keyWord,agentWeChatPaymentVo,rewardRecord);
                CommonLogUtil.addInfo(null, keyWord+"，代理人发放奖励，支付结果："+JSON.toJSONString(resultDto), null);

                rewardRecordList.add(rewardRecord);
            }

        }
        return resultDto;
    }
    /**
     * 分享人发放现金奖励
     * @author HuangHao
     * @CreatTime 2021-07-23 11:11
     * @param desc  奖励描述
     * @param rewardAmount  奖励金额
     * @param rewardType    奖励类型
     * @param afterSaleActivityDto.  活动信息
     * @param order 订单信息
     * @param afterSaleAgent 代理人信息
     * @param shareUserOrder    分享人订单信息
     * @param rewardRecordList
     * @return com.tuanche.commons.util.ResultDto
     */
    public ResultDto sharerReward(String keyWord,String desc,BigDecimal rewardAmount,Integer rewardType,
                                 AfterSaleActivityDto afterSaleActivityDto,AfterSaleOrderRecord order,
                                 String payWxOpenId,
                                 List<AfterSaleRewardRecord> rewardRecordList){
        ResultDto resultDto = new ResultDto();
        // 【奖励金额大于0】 才给分享人奖励
        if( rewardAmount != null && rewardAmount.compareTo(BigDecimal.ZERO) > 0) {
            Integer userType = AfterSaleRewardUserType.USER_TYPE3.getType();
            if(hasRewardRecord(rewardRecordList,order.getOrderCode(),userType,rewardType)){
                return resultDto;
            }
            AfterSaleRewardRecord rewardRecord = getOrderRewardRecord(order.getActivityId(), order.getOrderCode(),userType,rewardType);
            //没发过奖励或未生效或失败才发
            if(rewardRecord == null ||
                    (rewardRecord.getPayStatus() != null &&
                            (AfterSaleConstants.RewardPayStatus.PayStatus2.getCode()==rewardRecord.getPayStatus().intValue() ||
                             AfterSaleConstants.RewardPayStatus.PayStatus4.getCode()==rewardRecord.getPayStatus().intValue()))) {
                WeChatPaymentVo agentWeChatPaymentVo = new WeChatPaymentVo();
                agentWeChatPaymentVo.setOpenId(payWxOpenId);
                agentWeChatPaymentVo.setOrder_no(order.getOrderCode());
                int extraAmount = 0;
                agentWeChatPaymentVo.setDesc(desc);
                //发放金额=基础奖励金额+额外奖励金额
                agentWeChatPaymentVo.setAmount(hundred.multiply(rewardAmount).intValue());
                if(rewardRecord ==null){
                    rewardRecord = new AfterSaleRewardRecord();
                    rewardRecord.setActivityId(order.getActivityId());
                    rewardRecord.setUserType(userType);
                    rewardRecord.setUserWxUnionId(order.getShareWxUnionId());
                    rewardRecord.setRewardType(rewardType);
                    rewardRecord.setQuantity(rewardAmount);
                    rewardRecord.setOrderCode(order.getOrderCode());
                }
                //去微信支付
                resultDto = wechatPayment(keyWord, agentWeChatPaymentVo, rewardRecord);
                CommonLogUtil.addInfo(null, keyWord + "，分享人发放奖励，支付结果：" + JSON.toJSONString(resultDto), null);
                rewardRecordList.add(rewardRecord);
            }
        }
        return resultDto;
    }
    /**
     * 当前购买人发放现金奖励
     * @author HuangHao
     * @CreatTime 2021-07-23 11:11
     * @param desc  奖励描述
     * @param rewardAmount  奖励金额
     * @param rewardType    奖励类型
     * @param afterSaleActivityDto.  活动信息
     * @param order 订单信息
     * @param afterSaleAgent 代理人信息
     * @param rewardRecordList
     * @return com.tuanche.commons.util.ResultDto
     */
    public ResultDto buyerReward(String keyWord,String desc,BigDecimal rewardAmount,Integer rewardType,
                                 AfterSaleActivityDto afterSaleActivityDto,AfterSaleOrderRecord order,
                                 String payWxOpenId,List<AfterSaleRewardRecord> rewardRecordList){
        ResultDto resultDto = new ResultDto();
        // 【奖励金额大于0】 才给分享人奖励
        if( rewardAmount != null && rewardAmount.compareTo(BigDecimal.ZERO) > 0){
            Integer userType = AfterSaleRewardUserType.USER_TYPE2.getType();
            if(hasRewardRecord(rewardRecordList,order.getOrderCode(),userType,rewardType)){
                return resultDto;
            }
            AfterSaleRewardRecord rewardRecord = getOrderRewardRecord(order.getActivityId(), order.getOrderCode(),userType,rewardType);
            //没发过奖励或未生效或失败才发
            if(rewardRecord == null ||
                    (rewardRecord.getPayStatus() != null &&
                            (AfterSaleConstants.RewardPayStatus.PayStatus2.getCode()==rewardRecord.getPayStatus().intValue() ||
                             AfterSaleConstants.RewardPayStatus.PayStatus4.getCode()==rewardRecord.getPayStatus().intValue()))) {
                WeChatPaymentVo agentWeChatPaymentVo = new WeChatPaymentVo();
                agentWeChatPaymentVo.setOpenId(payWxOpenId);
                agentWeChatPaymentVo.setOrder_no(order.getOrderCode());
                int extraAmount = 0;
                agentWeChatPaymentVo.setDesc(desc);
                //发放金额=基础奖励金额+额外奖励金额
                agentWeChatPaymentVo.setAmount(hundred.multiply(rewardAmount).intValue());
                if(rewardRecord ==null){
                    rewardRecord = new AfterSaleRewardRecord();
                    rewardRecord.setActivityId(order.getActivityId());
                    rewardRecord.setUserType(userType);
                    rewardRecord.setUserWxUnionId(order.getUserWxUnionId());
                    rewardRecord.setRewardType(rewardType);
                    rewardRecord.setQuantity(rewardAmount);
                    rewardRecord.setOrderCode(order.getOrderCode());
                }
                //去微信支付
                resultDto = wechatPayment(keyWord,agentWeChatPaymentVo,rewardRecord);
                CommonLogUtil.addInfo(null, keyWord+"，购买人发放奖励，支付结果："+JSON.toJSONString(resultDto), null);
                rewardRecordList.add(rewardRecord);
            }
        }
        return resultDto;
    }

    /**
     * 检查订单奖励是否在rewardRecordList里
     * @author HuangHao
     * @CreatTime 2021-11-03 16:05
     * @param rewardRecordList
     * @param orderCode
     * @param userType
     * @param rewardType
     * @return boolean
     */
    public static boolean hasRewardRecord(List<AfterSaleRewardRecord> rewardRecordList,
                                          String orderCode, Integer userType, Integer rewardType){
        boolean has = false;
        if(!rewardRecordList.isEmpty()){
            long count = rewardRecordList.stream().filter(re-> re.getOrderCode().equals(orderCode) &&
                    re.getUserType().equals(userType) && re.getRewardType().equals(rewardType)).count();
            if(count>0){
                has=true;
            }
        }
        return has;
    }
    /**
     * 获取用户类型  0：备案用户 1：流失用户 2：普通用户 3：车商代理人
     * @author HuangHao
     * @CreatTime 2021-07-21 18:03
     * @param afterSaleKeepOnRecord
     * @return int
     */
    public Integer getUserType(AfterSaleOrderRecord order){
        AfterSaleAgent afterSaleAgent = afterSaleAgentService.getAfterSaleAgent(order.getActivityId(), order.getUserWxUnionId());
        if(afterSaleAgent != null){
            return AfterSaleConstants.UserType.USER_TYPE_3.getCode();
        }
        AfterSaleKeepOnRecord afterSaleKeepOnRecord = new AfterSaleKeepOnRecord();
        afterSaleKeepOnRecord.setActivityId(order.getActivityId());
        afterSaleKeepOnRecord.setLicencePlate(order.getLicencePlate());
        afterSaleKeepOnRecord.setUserPhone(order.getUserPhone());
        Integer userType = afterSaleKeepOnRecordService.getUserType(afterSaleKeepOnRecord);
        return userType==null?AfterSaleConstants.UserType.USER_TYPE_2.getCode():userType;
    }

    /**
     * 是否有订单
     * @author HuangHao
     * @CreatTime 2021-07-23 15:29
     * @param order
     * @return boolean
     */
    public boolean hasOrder(AfterSaleOrderRecord order){
        if(order != null && order.getOrderStatus() != 5  && order.getOrderStatus() != 6
                && order.getOrderStatus() != 7  && order.getOrderStatus() != 13){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 是否有退款订单
     * @author HuangHao
     * @CreatTime 2021-07-23 15:29
     * @param order
     * @return boolean
     */
    public boolean hasRefundOrder(AfterSaleOrderRecord order){
        if(order != null && (order.getOrderStatus() == 5  || order.getOrderStatus() == 6  ||
                order.getOrderStatus() == 7  || order.getOrderStatus() == 13)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 未生效的奖励记录
     * @author HuangHao
     * @return void
     */
    public void ineffectiveReward(String keyWord,Integer activityId,AfterSaleRewardUserType userType,AfterSaleRewardTypeEnums rewardType,
                  String userWxUnionId,BigDecimal quantity,String orderCode,List<AfterSaleRewardRecord> rewardRecordList){
        AfterSaleRewardRecord agentRewardRecord = new AfterSaleRewardRecord();
        if(hasRewardRecord(rewardRecordList,orderCode,userType.getType(),rewardType.getType())){
            return ;
        }
        CommonLogUtil.addInfo(null, keyWord+"，奖励未生效，活动ID："+activityId+"，订单code："+orderCode+"，用户类型："+userType.getName()+
                "，奖励类型："+rewardType.getName()+"，奖励数量："+quantity, null);
        //本次奖励看是否有过记录，如果有就不记录了
        if(AfterSaleRewardTypeEnums.REWARD_TYPE3.getType()==rewardType.getType()){
            AfterSaleRewardRecord rewardRecord = getRewardRecordByType(activityId, userWxUnionId,rewardType.getType());
            if(rewardRecord != null) {
                CommonLogUtil.addInfo(null, keyWord+"，用户已经产生了礼品券记录，不生成未生效记录，"+JSON.toJSONString(rewardRecord));
                return;
            }
        }else{
            AfterSaleRewardRecord rewardRecord = getOrderRewardRecord(activityId, orderCode,userType.getType(),rewardType.getType());
            if(rewardRecord != null) {
                CommonLogUtil.addInfo(null, keyWord+"，用户已经产生了"+rewardType.getName()+"记录，不生成未生效记录，"+JSON.toJSONString(rewardRecord));
                return;
            }
        }
        agentRewardRecord.setActivityId(activityId);
        agentRewardRecord.setUserType(userType.getType());
        agentRewardRecord.setUserWxUnionId(userWxUnionId);
        agentRewardRecord.setRewardType(rewardType.getType());
        agentRewardRecord.setQuantity(quantity);
        agentRewardRecord.setOrderCode(orderCode);
        agentRewardRecord.setPayStatus(AfterSaleConstants.RewardPayStatus.PayStatus4.getCode());
        agentRewardRecord.setFailureReason(AfterSaleConstants.RewardPayStatus.PayStatus4.getKey());
        agentRewardRecord.setErrCode(AfterSaleConstants.ErrCode.TC_INEFFECTIVE.getCode());
        rewardRecordList.add(agentRewardRecord);
    }
    /**
     * 售后特卖微信付款
     * @author HuangHao
     * @CreatTime 2021-07-22 10:20
     * @param weChatPaymentVo
     * @return com.tuanche.commons.util.ResultDto
     */
    public synchronized ResultDto wechatPayment(String keyWord,WeChatPaymentVo weChatPaymentVo,AfterSaleRewardRecord record)  {
        ResultDto resultDto = new ResultDto();
        String payNonceStr = UUID.randomUUID().toString().replaceAll("-", "");;
        record.setWxTradeTime(new Date());
        String logKey = "微信付款,标识："+payNonceStr;
        CommonLogUtil.addInfo(logKey, keyWord+"请求参数：", weChatPaymentVo);
        try{
            if (StringUtil.isEmpty(weChatPaymentVo.getOpenId())){
                String failureReason = "收款人微信openId不能为空";
                CommonLogUtil.addInfo(logKey, keyWord+"，微信转账失败，原因："+failureReason, weChatPaymentVo);
                resultDto.setMsg(failureReason);
                resultDto.setCode(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode());
                record.setPayStatus(AfterSaleConstants.RewardPayStatus.PayStatus2.getCode());
                record.setFailureReason(failureReason);
                record.setErrCode(AfterSaleConstants.ErrCode.TC_FAIL.getCode());
                return resultDto;
            }

            if(weChatPaymentVo.getAmount()<minimum_payment_limit){
                String failureReason = "付款金额必须不能小于"+((double)minimum_payment_limit/hundred.intValue())+"元";
                CommonLogUtil.addInfo(logKey, keyWord+"，微信转账失败，原因："+failureReason, weChatPaymentVo);
                record.setPayStatus(AfterSaleConstants.RewardPayStatus.PayStatus2.getCode());
                record.setFailureReason(failureReason);
                resultDto.setMsg(failureReason);
                resultDto.setCode(StatusCodeEnum.PARAM_IS_INVALID.getCode());
                record.setErrCode(AfterSaleConstants.ErrCode.TC_FAIL.getCode());
                return resultDto;
            }
            //交易流水号-同一个交易流水号只能支付一次
            String partner_trade_no = null;
            if(StringUtils.isEmpty(record.getPartnerTradeNo())){
                partner_trade_no = UUID.randomUUID().toString().replaceAll("-", "");
                record.setPartnerTradeNo(partner_trade_no);
            }else{
                partner_trade_no =record.getPartnerTradeNo();
            }
            //1.构造付款信息
            EnterprisePay payRequest = new EnterprisePay();
            payRequest.setMch_appid(aftersale_app_id);
            payRequest.setMchid(aftersale_pay_mchid);
            payRequest.setNonce_str(payNonceStr);
            payRequest.setPartner_trade_no(partner_trade_no);
            payRequest.setOpenid(weChatPaymentVo.getOpenId());
            payRequest.setAmount(weChatPaymentVo.getAmount());
            payRequest.setCheck_name("NO_CHECK");
            payRequest.setDesc(weChatPaymentVo.getDesc());
            //2.调用微信转账
            CommonLogUtil.addInfo(logKey, keyWord+"，调用微信转账接口参数：", payRequest);
            EnterprisePayResult payResponse = null;

            payResponse = wxPayService.enterprisePayToBalance(payRequest);
//            CommonLogUtil.addInfo(logKey, keyWord+"，调用微信转账接口返回：", payResponse);
            if(!AfterSaleConstants.ErrCode.SUCCESS.getCode().equalsIgnoreCase(payResponse.getResult_code())){
//                CommonLogUtil.addInfo(logKey, keyWord+"，微信转账失败，原因："+payResponse.getResult_code(), null);
                record.setPayStatus(AfterSaleConstants.RewardPayStatus.PayStatus2.getCode());
                record.setFailureReason(payResponse.getErr_code_des());
                //获取微信内部产生的付款单号
                record.setWxTradeNo(payResponse.getPayment_no());
                record.setWxPayTime(FuncUtil.StringToDate(payResponse.getPayment_time()));
                record.setErrCode(payResponse.getErr_code());
                resultDto.setMsg(payResponse.getErr_code_des());
                resultDto.setCode(StatusCodeEnum.INTERFACE_INNER_INVOKE_ERROR.getCode());
                return resultDto;
            }else{
                record.setWxTradeNo(payResponse.getPayment_no());
                record.setWxPayTime(FuncUtil.StringToDate(payResponse.getPayment_time()));
                record.setPayStatus(AfterSaleConstants.RewardPayStatus.PayStatus1.getCode());
                record.setFailureReason(payResponse.getResult_code());
                record.setErrCode(payResponse.getResult_code());
                CommonLogUtil.addInfo(logKey, keyWord+"，微信转账成功", null);
            }
            //返回交易结果
            resultDto.setResult(payResponse);
            resultDto.setCode(StatusCodeEnum.SUCCESS.getCode());
            resultDto.setMsg(StatusCodeEnum.SUCCESS.getText());
        }catch (Exception e){
            e.printStackTrace();
            CommonLogUtil.addError(null, keyWord+"，微信转账发生异常",e);
            resultDto.setCode(StatusCodeEnum.ERROR.getCode());
            record.setPayStatus(AfterSaleConstants.RewardPayStatus.PayStatus2.getCode());
            record.setFailureReason("微信转账发生异常");
            record.setErrCode(AfterSaleConstants.ErrCode.TC_ERROR.getCode());
        }
        return resultDto;
    }

    /**
     * 获取订单的奖励
     * @author HuangHao
     * @CreatTime 2021-10-26 11:05
     * @param activityId
     * @param orderCode
     * @param userType
     * @param rewardType
     * @return com.tuanche.directselling.model.AfterSaleRewardRecord
     */
    public AfterSaleRewardRecord getOrderRewardRecord(Integer activityId, String orderCode, int userType, int rewardType){
        AfterSaleRewardRecord rewardRecord = null;
        String key = MessageFormat.format(BaseCacheKeys.AFTER_SALE_ACTIVITYID_ORDER_USERTYPE_REWARDTYPE.getKey(), activityId, orderCode,userType,rewardType);
        try {
            rewardRecord = redisService.get(key, AfterSaleRewardRecord.class);
            if(rewardRecord == null){
                AfterSaleRewardRecord afterSaleRewardRecord = new AfterSaleRewardRecord();
                afterSaleRewardRecord.setUserType(userType);
                afterSaleRewardRecord.setActivityId(activityId);
                afterSaleRewardRecord.setOrderCode(orderCode);
                afterSaleRewardRecord.setRewardType(rewardType);
                rewardRecord = afterSaleRewardRecordWriteMapper.getRewardRecord(afterSaleRewardRecord);
                if(rewardRecord != null && rewardRecord.getPayStatus() != null && AfterSaleConstants.RewardPayStatus.PayStatus1.getCode()==rewardRecord.getPayStatus().intValue()){
                    long exptime = BaseCacheKeys.AFTER_SALE_ACTIVITYID_ORDER_USERTYPE_REWARDTYPE.getExpire();
                    redisService.set(key, rewardRecord, exptime);
                }
            }
        } catch (RedisException e) {
            e.printStackTrace();
        }
        return rewardRecord;
    }
    /**
     * 查询发放的奖励
     * @author HuangHao
     * @CreatTime 2021-07-25 16:46
     * @param activityId
     * @param wxUnionId
     * @param rewardType
     * @return com.tuanche.directselling.model.AfterSaleRewardRecord
     */
    public AfterSaleRewardRecord getRewardRecordByType(Integer activityId, String wxUnionId, Integer rewardType){
        AfterSaleRewardRecord rewardRecord = null;
        String key = getRewardRecordCacheKey(activityId, wxUnionId, rewardType);
        try {
            rewardRecord = redisService.get(key, AfterSaleRewardRecord.class);
            if(rewardRecord == null){
                AfterSaleRewardRecord afterSaleRewardRecord = new AfterSaleRewardRecord();
                afterSaleRewardRecord.setActivityId(activityId);
                afterSaleRewardRecord.setUserWxUnionId(wxUnionId);
                afterSaleRewardRecord.setRewardType(rewardType);
                rewardRecord = afterSaleRewardRecordWriteMapper.getRewardRecord(afterSaleRewardRecord);
                if(rewardRecord != null && rewardRecord.getPayStatus() != null && AfterSaleConstants.RewardPayStatus.PayStatus1.getCode()==rewardRecord.getPayStatus().intValue() ){
                    cacHeRewardRecord(rewardRecord);
                }
            }
        } catch (RedisException e) {
            e.printStackTrace();
        }
        return rewardRecord;
    }

    /**
     * 缓存奖励记录
     * @author HuangHao
     * @CreatTime 2021-08-02 16:18
     * @param rewardRecord
     * @return void
     */
    public void cacHeRewardRecord(AfterSaleRewardRecord rewardRecord){
        String key = getRewardRecordCacheKey(rewardRecord.getActivityId(), rewardRecord.getUserWxUnionId(), rewardRecord.getRewardType());
        try {
            redisService.set(key, rewardRecord, BaseCacheKeys.AFTER_SALE_ACTIVITYID_REWARDRECORD_USER_REWARDTYPE.getExpire());
        } catch (RedisException e) {
            e.printStackTrace();
        }
    }

    public static String getRewardRecordCacheKey(Integer activityId, String wxUnionId,Integer rewardType){
        return MessageFormat.format(BaseCacheKeys.AFTER_SALE_ACTIVITYID_REWARDRECORD_USER_REWARDTYPE.getKey(), activityId, wxUnionId,rewardType);
    }
    public static String getRewardRecordByOrderCacheKey(Integer activityId, String orderCode, Integer userType, Integer rewardType){
        return MessageFormat.format(BaseCacheKeys.AFTER_SALE_ACTIVITYID_ORDER_USERTYPE_REWARDTYPE.getKey(), activityId, orderCode,userType,rewardType);
    }

    /**
     * 根据ID更新
     * @author HuangHao
     * @CreatTime 2021-08-02 16:12
     * @param afterSaleRewardRecord
     * @return int
     */
    public int updateById(AfterSaleRewardRecord afterSaleRewardRecord){
        if(afterSaleRewardRecord == null || afterSaleRewardRecord.getId() == null){
            return 0;
        }
        int updateNum = afterSaleRewardRecordWriteMapper.updateById(afterSaleRewardRecord);
        if(updateNum>0){
            AfterSaleRewardRecord record = afterSaleRewardRecordWriteMapper.getRewardRecordById(afterSaleRewardRecord.getId());
            cacHeRewardRecord(record);
        }
        return updateNum;
    }

    /**
     * 更新奖励状态
     * @author HuangHao
     * @CreatTime 2021-08-02 16:26
     * @param activityId    活动ID
     * @param wxUnionId     用户微信unionid
     * @param rewardType    奖励类型
     * @param payStatus     状态
     * @return int
     */
    public int updatePayStatus(Integer activityId, String wxUnionId,AfterSaleRewardTypeEnums rewardType,AfterSaleConstants.RewardPayStatus payStatus){
        AfterSaleRewardRecord rewardRecord = getRewardRecordByType(activityId, wxUnionId, rewardType.getType());
        if(rewardRecord == null){
            return 0;
        }
        AfterSaleRewardRecord record = new AfterSaleRewardRecord();
        record.setId(rewardRecord.getId());
        record.setPayStatus(payStatus.getCode());
        return updateById(record);
    }

    /**
     * 获取正在进行中的且是已开启状态的活动已发放的奖励
     * @author HuangHao
     * @CreatTime 2021-08-09 17:42
     * @param
     * @return com.tuanche.directselling.dto.AfterSaleRewardRecordAmountAlarmDto
     */
    public void amountAlarm(boolean firstSendTask){
        List<AfterSaleRewardRecordAmountAlarmDto> list = afterSaleRewardRecordReadMapper.amountAlarm();
        if(!CollectionUtils.isEmpty(list)){
            try {
            Date now = new Date();
            for (AfterSaleRewardRecordAmountAlarmDto amountAlarm : list) {
                if(amountAlarm.getBudget() != null && amountAlarm.getBudget().compareTo(BigDecimal.ZERO)>0
                    && amountAlarm.getAmountAlarm() != null && amountAlarm.getAmountAlarm().compareTo(BigDecimal.ZERO)>0
                    && amountAlarm.getAmountUsed() != null && amountAlarm.getAmountUsed().compareTo(BigDecimal.ZERO)>0){
                    String key = MessageFormat.format(BaseCacheKeys.AFTER_SALE_AMOUNT_ALARM_FIRST_ACTIVITYID.getKey(), amountAlarm.getActivityId());
                    if(firstSendTask){
                        String obj = redisService.get(key, String.class);
                        //如果缓存不为空则说明已经发过第一次预警，不再发，
                        if(obj != null){
                            continue;
                        }
                    }
                    BigDecimal balance = amountAlarm.getBudget().subtract(amountAlarm.getAmountUsed());
                    //如果余额小于等于预警金额则报警
                    if(balance.compareTo(amountAlarm.getAmountAlarm())<=0){
                        long timeout = DateUtil.getDiffSecond(now, amountAlarm.getSaleEndTime())+3600;
                        int expTime = 0;
                        if(timeout>Integer.MAX_VALUE){
                            expTime=Integer.MAX_VALUE;
                        }else{
                            expTime=(int)timeout;
                        }
                        redisService.setex(key, expTime,"1");
                        String msg = amountAlarm.getActivityName()+"预算剩余"+balance+"元，请注意使用额度并及时补充预算";
                        if(!StringUtils.isEmpty(amountAlarm.getAlarmPhone())){
                            String[] phones = amountAlarm.getAlarmPhone().split("##");
                            for (String phone : phones) {
                                sendMessage(phone,msg,null);
                            }
                        }

                        if(!StringUtils.isEmpty(amountAlarm.getAlarmEmail())){
                            EmailUtil.send(amountAlarm.getActivityName()+"预算报警", Arrays.asList(amountAlarm.getAlarmEmail().split("##")), null, msg);
                        }

                    }
                }

            }
            }catch (RedisException e){

            }
        }
    }
    /**
     * 补发失败了的奖励
     * @author HuangHao
     * @CreatTime 2021-08-20 14:56
     * @param
     * @return java.util.List<com.tuanche.directselling.dto.AfterSaleRewardRecordFailDto>
     */
    public void reissueReward(Integer activityId) {
        AfterSaleRewardRecord afterSaleRewardRecord = new AfterSaleRewardRecord();
        if(activityId != null){
            afterSaleRewardRecord.setActivityId(activityId);
        }
        String keyWord="补发售后特卖用户奖励:"+UUID.randomUUID().toString();
        CommonLogUtil.addInfo(null, keyWord+"，开始，参数："+activityId);
        List<AfterSaleRewardRecordFailDto> list = afterSaleRewardRecordReadMapper.getFailRewardRecordList(afterSaleRewardRecord);
        CommonLogUtil.addInfo(null, keyWord+"，待补发列表："+JSON.toJSONString(list));
        AfterSaleActivityDto afterSaleActivityDto = afterSaleActivityService.getCacheAfterSaleActivityDtoById(activityId);
        if(!CollectionUtils.isEmpty(list) && afterSaleActivityDto != null){
            //获取活动信息
            List<AfterSaleRewardRecord> updateList = new ArrayList<>();
            for (AfterSaleRewardRecordFailDto recordFailDto : list) {
                try {
                    String kw = keyWord+"_"+recordFailDto.getOrderCode();
                    int rewardType = recordFailDto.getRewardType().intValue();
                    //礼品券补发
                    if(AfterSaleRewardTypeEnums.REWARD_TYPE3.getType()==rewardType){
                        List<AfterSaleRewardRecord> rewardRecordList = new ArrayList<>();
                        Integer userType =recordFailDto.getUserType();
                        int pushType;
                        if(AfterSaleRewardUserType.USER_TYPE2.getType()==userType.intValue()){
                            pushType=AfterSaleConstants.PushType.TYPE2.getCode();
                        }else{
                            pushType=AfterSaleConstants.PushType.TYPE3.getCode();
                        }
                        //给购买人发放购买礼品券
                        issueGiftCertificates(kw,pushType,recordFailDto.getOrderCode(),
                                activityId, recordFailDto.getUserWxUnionId(),recordFailDto.getUserWxOpenId(),
                                userType,afterSaleActivityDto,rewardRecordList);
                        if(rewardRecordList.size()>0){
                            AfterSaleRewardRecord record = rewardRecordList.get(0);
                            CommonLogUtil.addInfo(null, kw+"，礼品券补发结果："+JSON.toJSONString(record));
                            updateList.add(record);
                        }else{
                            CommonLogUtil.addInfo(null, kw+"，已经发券了，不补发：");
                        }
                    //现金奖励补发
                    }else{
                        if (!StringUtils.isEmpty(recordFailDto.getUserWxOpenId()) && recordFailDto.getQuantity() != null) {
                            WeChatPaymentVo agentWeChatPaymentVo = new WeChatPaymentVo();
                            agentWeChatPaymentVo.setOpenId(recordFailDto.getUserWxOpenId());
                            agentWeChatPaymentVo.setOrder_no(recordFailDto.getOrderCode());
                            String desc = "";
                            if (AfterSaleRewardTypeEnums.REWARD_TYPE1.getType() == rewardType) {
                                desc = "您推荐的[" + recordFailDto.getLicencePlate() + "]红包奖励，继续推荐再得红包";
                            } else if (AfterSaleRewardTypeEnums.REWARD_TYPE2.getType() == rewardType) {
                                desc = "您购买成功，获得购买红包奖励，邀请亲友购买再得红包";
                            } else if (AfterSaleRewardTypeEnums.REWARD_TYPE4.getType() == rewardType) {
                                desc = "您推荐的[" + recordFailDto.getLicencePlate() + "]额外奖励，继续推荐再得红包";
                            }
                            agentWeChatPaymentVo.setDesc(desc);
                            //发放金额=基础奖励金额

                            agentWeChatPaymentVo.setAmount(hundred.multiply(recordFailDto.getQuantity()).intValue());
                            AfterSaleRewardRecord agentRewardRecord = new AfterSaleRewardRecord();
                            agentRewardRecord.setPartnerTradeNo(recordFailDto.getPartnerTradeNo());
                            agentRewardRecord.setWxNonceStr(recordFailDto.getWxNonceStr());
                            //去微信支付
                            ResultDto resultDto = wechatPayment(kw, agentWeChatPaymentVo, agentRewardRecord);
                            CommonLogUtil.addInfo(null, kw+"，支付结果："+JSON.toJSONString(resultDto));
                            agentRewardRecord.setId(recordFailDto.getId());
                            updateList.add(agentRewardRecord);
                        }else{
                            CommonLogUtil.addInfo(null, kw+"，补发失败-信息不全，微信openId："+recordFailDto.getUserWxOpenId());
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    CommonLogUtil.addError(null, keyWord+"，系统发异常", e);
                }
            }
            CommonLogUtil.addInfo(null, keyWord+"，实际补发人数："+updateList.size());
            if(updateList.size()>0){
                afterSaleRewardRecordWriteMapper.batchUpdate(updateList);
            }
        }
    }
    /**
     * 用户奖励统计
     * @author HuangHao
     * @CreatTime 2021-08-18 11:27
     * @param
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleUserRewardStatistics>
     */
    public List<AfterSaleUserRewardStatistics> userRewardStatistics(List<Integer> activityIds){
        if(CollectionUtils.isEmpty(activityIds)){
            return null;
        }
        return afterSaleRewardRecordReadMapper.userRewardStatistics(activityIds);
    }

    /**
     * 按活动统计礼品券发放总数
     * @author HuangHao
     * @CreatTime 2021-08-23 11:13
     * @param list
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    public Map<String, ResultMapDto> giftCertificatesIssuedMap(List<Integer> list){
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return afterSaleRewardRecordReadMapper.giftCertificatesIssuedMap(list);
    }
    /**
     * 获取奖励总额
     * @author HuangHao
     * @CreatTime 2021-08-23 11:13
     * @param list
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    public Map<String, ResultMapDto> getRewardTotalMap(List<Integer> activityIds){
        if(CollectionUtils.isEmpty(activityIds)){
            return null;
        }
        return afterSaleRewardRecordReadMapper.getRewardTotalMap(activityIds);
    }
    /**
     * 获取获得奖励的分享数
     * @author HuangHao
     * @CreatTime 2021-08-23 11:13
     * @param list
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    public Map<String, ResultMapDto> getShareTotalMap(List<AfterSaleUserRewardStatistics> list){
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return afterSaleRewardRecordReadMapper.getShareTotalMap(list);
    }

    /**
     * 获取用户的奖励明细-分页
     *
     * @param
     * @return java.util.List<com.tuanche.directselling.dto.AfterSaleUserRewardStatisticsDetailDto>
     * @author HuangHao
     * @CreatTime 2021-08-23 15:14
     */
    @Override
    public PageResult<AfterSaleUserRewardStatisticsDetailDto> getUserRewardDetailListByPage(PageResult<AfterSaleUserRewardStatisticsDetailDto> pageResult, Integer activityId, String userWxUnionId) {
        if (activityId == null || StringUtils.isEmpty(userWxUnionId)) {
            return null;
        }
        PageHelper.startPage(pageResult.getPage(), pageResult.getLimit());
        AfterSaleRewardRecord afterSaleRewardRecord = new AfterSaleRewardRecord();
        afterSaleRewardRecord.setActivityId(activityId);
        afterSaleRewardRecord.setUserWxUnionId(userWxUnionId);
        List<AfterSaleUserRewardStatisticsDetailDto> list = afterSaleRewardRecordReadMapper.getUserRewardDetailList(afterSaleRewardRecord);
        Set<Integer> dealerIds = new HashSet<>();
        Set<String> orderNos = new HashSet<>();
        for (AfterSaleUserRewardStatisticsDetailDto dto : list) {
            dealerIds.add(dto.getDealerId());
            orderNos.add(dto.getOrderCode());
        }
        Map<Integer, CsCompany> csCompanyMap = null;
        if (!dealerIds.isEmpty()) {
            csCompanyMap = companyBaseService.getCompanyMap(new ArrayList<>(dealerIds));
        }
        Map<String, PaymentResponseDto> payMap = new HashMap<>(16);
        if (!orderNos.isEmpty()) {
            //订单流水信息
            PageableRequestDto requestDto = new PageableRequestDto();
            OrderPaymentQueryRequestDto paymentQueryRequestDto = new OrderPaymentQueryRequestDto();
            paymentQueryRequestDto.setOrderCodeList(new ArrayList<>(orderNos));
            requestDto.setQuery(paymentQueryRequestDto);
            requestDto.setPageIndex(1);
            requestDto.setPageSize(orderNos.size());

            BaseResponseDto<PageableDto<PaymentResponseDto>> pageableDtoBaseResponseDto = iPaymentService.listOrderPayment(requestDto);
            if (pageableDtoBaseResponseDto.getCode().equals(ResultEnum.SUCCESS.getCode()) && org.apache.commons.collections4.CollectionUtils.isNotEmpty(pageableDtoBaseResponseDto.getData().getList())) {
                pageableDtoBaseResponseDto.getData().getList().forEach(v -> {
                    payMap.put(v.getOrderCode(), v);
                });
            }
        }
        for (AfterSaleUserRewardStatisticsDetailDto dto : list) {
            if (csCompanyMap != null) {
                CsCompany company = csCompanyMap.get(dto.getDealerId());
                if (company != null) {
                    dto.setDealerName(company.getCompanyName());

                }
            }
            PaymentResponseDto paymentResponseDto = payMap.get(dto.getOrderCode());
            if (paymentResponseDto != null) {
                dto.setTradeNo(paymentResponseDto.getPayment3SerialNo());
            }
        }
        pageResult.setCount(new PageInfo<>(list).getTotal());
        pageResult.setCode("0");
        pageResult.setMsg("");
        pageResult.setData(list);
        return pageResult;
    }

    /**
      * @description : 定时统计售后特卖已发放红包(十分钟跨度，取整十，eg:10:00-20:00, 20:00-30:00)
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/11/23 18:12
      */
    @Override
    public void getamountAlarmByTime (String params) {
        CommonLogUtil.addInfo("AfterSaleRewardRecordServiceImpl", "定时统计售后特卖已发放红包 start", params);
        Date startTime = null;
        Date endTime = null;
        if (StringUtil.isEmpty(params)) {
            GregorianCalendar calendar = new GregorianCalendar();
            String endtTimeStr = DateUtil.formart(calendar.getTime(), DateUtil.FORMART_YMD_HMS);
            endTime = DateUtil.parseDate(endtTimeStr.substring(0,endtTimeStr.length()-4)+"0:00", DateUtil.FORMART_YMD_HMS);
            calendar.add(Calendar.MINUTE, -10);
            String startTimeStr = DateUtil.formart(calendar.getTime(), DateUtil.FORMART_YMD_HMS);
            startTime = DateUtil.parseDate(startTimeStr.substring(0,startTimeStr.length()-4)+"0:00", DateUtil.FORMART_YMD_HMS);
        } else {
            startTime = DateUtil.parseDate(params.split(",")[0], DateUtil.FORMART_YMD_HMS);
            endTime = DateUtil.parseDate(params.split(",")[1], DateUtil.FORMART_YMD_HMS);
        }
        AfterSaleRewardRecordAmountAlarmDto afterSaleRewardRecordAmountAlarmDto = afterSaleRewardRecordReadMapper.getamountAlarmByTime(startTime, endTime);
        CommonLogUtil.addInfo("AfterSaleRewardRecordServiceImpl", "定时统计售后特卖已发放红包 时间段内发放红包", JSON.toJSONString(afterSaleRewardRecordAmountAlarmDto));
        if (afterSaleRewardRecordAmountAlarmDto!=null && afterSaleRewardRecordAmountAlarmDto.getAmountUsed()!=null) {
            AfterSaleAmountAlarm afterSaleAmountAlarm = afterSaleAmountAlarmReadMapper.getAfterSaleAmountAlarm();
            if (afterSaleAmountAlarm==null) {
                afterSaleAmountAlarm = new AfterSaleAmountAlarm();
                afterSaleAmountAlarm.setPaymentAmount(afterSaleRewardRecordAmountAlarmDto.getAmountUsed());
                afterSaleAmountAlarmWriteMapper.insertSelective(afterSaleAmountAlarm);
            } else {
                BigDecimal paymentAmount = afterSaleAmountAlarm.getPaymentAmount().add(afterSaleRewardRecordAmountAlarmDto.getAmountUsed());
                afterSaleAmountAlarm.setPaymentAmount(paymentAmount);
                afterSaleAmountAlarmWriteMapper.updateByPrimaryKeySelective(afterSaleAmountAlarm);
            }
        }
        CommonLogUtil.addInfo("AfterSaleRewardRecordServiceImpl", "定时统计售后特卖已发放红包 end");
    }


}
