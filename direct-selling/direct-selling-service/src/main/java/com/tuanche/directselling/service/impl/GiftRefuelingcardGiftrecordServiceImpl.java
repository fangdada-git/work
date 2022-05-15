package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.exception.RedisException;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.arch.web.model.TcResponseHeader;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.directselling.api.GiftRefuelingcardGiftrecordService;
import com.tuanche.directselling.constant.BaseCacheKeys;
import com.tuanche.directselling.dto.DealerRefuelCarActivityInfoDto;
import com.tuanche.directselling.dto.GiftRefuelingCardActivityDto;
import com.tuanche.directselling.dto.GiftRefuelingcardGiftrecordDto;
import com.tuanche.directselling.dto.RefuelingCardConfig;
import com.tuanche.directselling.mapper.read.directselling.*;
import com.tuanche.directselling.mapper.write.directselling.GiftRefuelingcardGiftrecordCardsWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.GiftRefuelingcardGiftrecordWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.GiftRefuelingcardWriteMapper;
import com.tuanche.directselling.model.*;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.util.FuncUtil;
import com.tuanche.directselling.utils.GlobalConstants;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.GiftRefuelingcardGiftrecordVo;
import com.tuanche.eap.api.dto.manufacturer.CsDirectSellingClueDto;
import com.tuanche.eap.api.service.project.DirectSellingClueService;
import com.tuanche.framework.base.util.DateUtil;
import com.tuanche.framework.base.util.JsonUtil;
import com.tuanche.manubasecenter.api.SmsVerifyCodeService;
import com.tuanche.manubasecenter.constant.ManuBaseConstants;
import com.tuanche.manubasecenter.dto.SmsDto;
import com.tuanche.merchant.api.order.PlatformOrderService;
import com.tuanche.merchant.pojo.dto.enums.CommodityTypeEnum;
import com.tuanche.merchant.pojo.dto.enums.PlatformTypeEnum;
import com.tuanche.merchant.pojo.dto.enums.TmTradeStatus;
import com.tuanche.merchant.pojo.dto.order.BusiOrderQueryDto;
import com.tuanche.merchant.pojo.dto.order.PlatformOrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 赠送油卡活动
 * @author：HuangHao
 * @CreatTime 2020-05-12 18:07
 */
@Service(retries = 0)
public class GiftRefuelingcardGiftrecordServiceImpl implements GiftRefuelingcardGiftrecordService {

    @Value("${gift_refuelingcard_config}")
    private String gift_refuelingcard_config;
    @Value("${sms_system_code}")
    private String sms_system_code;
    @Value("${sms_business_code}")
    private String sms_business_code;
    @Value("${sms_business_template_notify}")
    private Integer sms_business_template_notify;
    @Autowired
    GiftRefuelingcardGiftrecordReadMapper giftRefuelingcardGiftrecordReadMapper;
    @Autowired
    GiftRefuelingcardGiftrecordWriteMapper giftRefuelingcardGiftrecordWriteMapper;
    @Autowired
    GiftRefuelingcardReadMapper giftRefuelingcardReadMapper;
    @Autowired
    GiftRefuelingcardWriteMapper giftRefuelingcardWriteMapper;
    @Autowired
    GiftRefuelingcardPeriodsGiftNumReadMapper giftRefuelingcardPeriodsGiftNumReadMapper;
    @Reference
    PlatformOrderService platformOrderService;
    @Reference
    SmsVerifyCodeService smsVerifyCodeService;
    @Reference
    DirectSellingClueService directSellingClueService;
    @Autowired
    ExecutorService executorService;
    @Autowired
    LiveSceneDealerConfigReadMapper liveSceneDealerConfigReadMapper;
    @Autowired
    GiftRefuelingcardGiftrecordCardsReadMapper giftRefuelingcardGiftrecordCardsReadMapper;
    @Autowired
    GiftRefuelingcardGiftrecordCardsWriteMapper giftRefuelingcardGiftrecordCardsWriteMapper;
    @Autowired
    GiftRefuelingcardPeriodsCommodityReadMapper giftRefuelingcardPeriodsCommodityReadMapper;
    @Autowired
    @Qualifier("ClusterRedisService")
    private RedisService redisService;



    /**
     * 根据经销商ID获取赠送记录
     * @author HuangHao
     * @CreatTime 2020-05-14 17:06
     * @param id
     * @return com.tuanche.directselling.model.GiftRefuelingcardGiftrecord
     */
    GiftRefuelingcardGiftrecord getGiftRefuelingcardGiftrecordById(Integer id){
        if(id==null || id < 1){
            return null;
        }
        return giftRefuelingcardGiftrecordReadMapper.getGiftRefuelingcardGiftrecordById(id);
    }

    /**
     * 获取油卡赠送记录列表-分页
     * @author HuangHao
     * @CreatTime 2020-05-12 17:57
     * @param
     * @return java.util.List<com.tuanche.directselling.model.GiftRefuelingcardGiftrecord>
     */
    @Override
    public PageResult<GiftRefuelingcardGiftrecordDto> getGiftRefuelingcardGiftrecordByPage(GiftRefuelingcardGiftrecordVo giftrecord) {
        PageHelper.startPage(giftrecord.getPage(), giftrecord.getLimit(), "id desc");
        List<GiftRefuelingcardGiftrecordDto> list = giftRefuelingcardGiftrecordReadMapper.getGiftRefuelingcardGiftrecordList(giftrecord);
        PageInfo<GiftRefuelingcardGiftrecordDto> page = new PageInfo<GiftRefuelingcardGiftrecordDto>(list);
        PageResult<GiftRefuelingcardGiftrecordDto> pageResult = new PageResult<GiftRefuelingcardGiftrecordDto>();
        pageResult.setCount(page.getTotal());
        pageResult.setPage(giftrecord.getPage());
        pageResult.setLimit(giftrecord.getLimit());
        pageResult.setCode(String.valueOf(StatusCodeEnum.SUCCESS.getCode()));
        pageResult.setMsg(null);
        pageResult.setData(list);
        return pageResult;
    }

    /**
     * 用户到店核销
     * @author HuangHao
     * @CreatTime 2020-05-14 14:24
     * @param phone
     * @param dealerId
     * @return com.tuanche.arch.web.model.TcResponse
     */
    @Override
    public TcResponse writeOff(String phone,Integer dealerId,Integer periodsId){
        String keyWord = "赠送油卡活动用户到店核销，经销ID:"+dealerId;
        CommonLogUtil.addInfo(keyWord, "开始，手机号："+phone);
        if(!FuncUtil.isMobile(phone)){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"手机号不合法");
        }else if (phone == null){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"经销商ID不能为空");
        }
        //检查活动是否已结束
        GiftRefuelingCardActivityDto activityDto = getActinityInfo(dealerId,periodsId);
        if (!activityDto.isOngoing()){
            CommonLogUtil.addInfo(keyWord, activityDto.getMsg());
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),activityDto.getMsg());
        }
        //检查用户是否已经赠送过油卡
        TcResponse givenState = checkUserGivenState(phone, dealerId, periodsId);
        if(StatusCodeEnum.SUCCESS.getCode() != givenState.getResponseHeader().getStatus()){
            CommonLogUtil.addInfo(keyWord, "核销失败，原因："+givenState.getResponseHeader().getMessage());
            return givenState;
        }
        //检查该手机号是否已收货
        TcResponse confirmReceipt = checkUserConfirmReceipt(dealerId,activityDto.getInfo().getPeriodsId(),phone);
        CommonLogUtil.addInfo(keyWord, "赠送完成，返回结果："+JSONObject.toJSONString(confirmReceipt));
        return confirmReceipt;
    }



    /**
     * 赠送油卡
     * @author HuangHao
     * @CreatTime 2020-05-14 11:26
     * @param giftrecord
     * @return com.tuanche.arch.web.model.TcResponse
     */
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    public TcResponse giveRefuelingcard(GiftRefuelingcardGiftrecordVo giftrecord){
        String keyWord = "赠送油卡";
        TcResponse response = new TcResponse();
        TcResponseHeader responseHeader = new TcResponseHeader();
        CommonLogUtil.addInfo(keyWord, "开始，请求参数"+JSONObject.toJSONString(giftrecord));
        if (giftrecord.getDealerId() == null){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"经销商ID不能为空");
        }else if (giftrecord.getPlatformOrderId() == null){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"收货订单ID不能为空");
        }else if (giftrecord.getCbId() == null){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"品牌不能为空");
        }else if (giftrecord.getCsId()== null){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"车型不能为空");
        }else if (giftrecord.getPeriodsId() == null){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"大场次ID不能为空");
        }

        Integer dealerId = giftrecord.getDealerId();
        keyWord = "赠送油卡，经销商ID:"+dealerId;
        //检查活动是否已结束
        GiftRefuelingCardActivityDto activityDto = getActinityInfo(dealerId,giftrecord.getPeriodsId());
        if (!activityDto.isOngoing()){
            CommonLogUtil.addInfo(keyWord, "赠送失败，原因："+activityDto.getMsg());
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),activityDto.getMsg());
        }
        //取出缓存的订单-减少前端传递的参数
        String orderData = null;
        String key = BaseCacheKeys.REFUELING_CARD_ORDER_CACHE.getKey()+giftrecord.getPlatformOrderId();
        try {
            orderData = redisService.get(key, String.class);
        } catch (RedisException e) {
            e.printStackTrace();
        }
        //获取订单信息
        PlatformOrderDto platformOrderDto =null;
        String orderErrorMsg = "赠送失败，原因：该订单不存在，订单ID:"+giftrecord.getPlatformOrderId();
        if(StringUtils.isEmpty(orderData)){
            CommonLogUtil.addInfo(keyWord, orderErrorMsg);
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),orderErrorMsg);
        }else{
            platformOrderDto = JsonUtil.json2Entity(orderData, PlatformOrderDto.class);
            if(platformOrderDto == null || platformOrderDto.getOrderMoney() == null){
                CommonLogUtil.addInfo(keyWord, orderErrorMsg);
                return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),orderErrorMsg);
            }
        }
        String phone = platformOrderDto.getPhone();
        //检查手机号是否已经赠送过
        TcResponse givenState = checkUserGivenState(phone, dealerId, giftrecord.getPeriodsId());
        if(StatusCodeEnum.SUCCESS.getCode() != givenState.getResponseHeader().getStatus()){
            CommonLogUtil.addInfo(keyWord, "赠送失败，原因："+givenState.getResponseHeader().getMessage());
            return givenState;
        }
        //获取订单金额对应的赠送油卡数量
        GiftRefuelingcardPeriodsGiftNum giftNum = giftRefuelingcardPeriodsGiftNumReadMapper.getGiftNumByPriceAndPeriodsId(platformOrderDto.getOrderMoney(),giftrecord.getPeriodsId());
        if(giftNum == null || giftNum.getGiftNum() == null){
            String msg = "订单金额不符合送油卡的条件，无法赠送，订单金额："+platformOrderDto.getOrderMoney();
            CommonLogUtil.addInfo(keyWord, msg);
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }

        //获取油卡
        List<GiftRefuelingcard> refuelingcardList = giftRefuelingcardReadMapper.getNoGiftRefuelingcardList(giftNum.getGiftNum());
        //库存不足
        if(refuelingcardList == null || refuelingcardList.size() < giftNum.getGiftNum()){
            String msg = "油卡库存不足，请线下联系团车工作人员";
            CommonLogUtil.addInfo(keyWord, "赠送失败，原因："+msg);
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        //如果订单所赠送的油卡大于经销商剩余可赠送油卡
        }else if(refuelingcardList.size() > activityDto.getInfo().getSurplusCarNum()){
            String msg = "本场剩余可赠油卡数量不足，无法赠送，赠送数量："+refuelingcardList.size()+"，剩余可赠送数量："+activityDto.getInfo().getSurplusCarNum();
            CommonLogUtil.addInfo(keyWord, "赠送失败，原因："+msg);
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }
        giftrecord.setOrderChannel(platformOrderDto.getPlatformType().value());
        giftrecord.setCommodityType(platformOrderDto.getCommodityType().value());
        giftrecord.setCommodityTypeName(platformOrderDto.getCommodityType().tag());
        //新增赠送记录
        Date now = new Date();
        giftrecord.setPhone(phone);
        giftrecord.setName(platformOrderDto.getReceiverName());
        giftrecord.setPeriodsId(activityDto.getInfo().getPeriodsId());
        giftrecord.setPeriodsName(activityDto.getInfo().getPeriodsName());
        giftrecord.setSendTime(now);
        giftrecord.setCreateDt(now);
        giftrecord.setOrderMoney(platformOrderDto.getOrderMoney());
        giftrecord.setDeleteFlag(false);
        giftRefuelingcardGiftrecordWriteMapper.insertGiftRefuelingcardGiftrecord(giftrecord);
        //取出卡号
        List<String> updateStateCardList = new ArrayList<>(refuelingcardList.size());
        List<GiftRefuelingcardGiftrecordCards> recordCardList = new ArrayList<>(refuelingcardList.size());
        StringBuilder sendMsgCodes = new StringBuilder();
        for(GiftRefuelingcard card:refuelingcardList){
            GiftRefuelingcardGiftrecordCards recordCard = new GiftRefuelingcardGiftrecordCards();
            recordCard.setRefuelingCode(card.getRefuelingCode());
            recordCard.setGiftrecordId(giftrecord.getId());
            recordCardList.add(recordCard);
            updateStateCardList.add(card.getRefuelingCode());
            if(sendMsgCodes.length() < 1){
                sendMsgCodes.append(card.getRefuelingCode());
            }else {
                sendMsgCodes.append(",");
                sendMsgCodes.append(card.getRefuelingCode());
            }
        }
        CommonLogUtil.addInfo(keyWord, "插入赠送记录："+JSONObject.toJSONString(giftrecord));
        //插入赠送卡号记录
        giftRefuelingcardGiftrecordCardsWriteMapper.batchInsert( recordCardList);
        //更新油卡赠送状态为已赠送
        giftRefuelingcardWriteMapper.updateStateByRefuelingCodes(GlobalConstants.REFUELING_CARD_STATE_2, updateStateCardList);
        CommonLogUtil.addInfo(keyWord, "更新油卡赠送状态，更新的油卡卡号："+JSONObject.toJSONString(updateStateCardList));
        //发送短信
        TcResponse sendMsgState = sendMsg(phone, giftrecord.getCityId(), sendMsgCodes.toString());
        if(StatusCodeEnum.SUCCESS.getCode() != sendMsgState.getResponseHeader().getStatus()){
            CommonLogUtil.addInfo(keyWord, "赠送成功，但发送短信失败，原因：无法发送短信，短信接口返回结果："+sendMsgState.getResponseHeader().getMessage());
            return sendMsgState;
        }

        //线索插入线索池
        CsDirectSellingClueDto clueDto = new CsDirectSellingClueDto();
        clueDto.setPeriodsId(activityDto.getInfo().getPeriodsId());
        clueDto.setPhone(Long.valueOf(phone));
        clueDto.setBrandId(giftrecord.getCbId());
        clueDto.setDealerId(dealerId);
        clueDto.setClueSource(14);
        clueDto.setName(giftrecord.getName());
        clueDto.setStyleId(giftrecord.getCsId());
        clueDto.setSignupTime(now);
        sellingClueApply(clueDto);

        responseHeader.setStatus(StatusCodeEnum.SUCCESS.getCode());
        responseHeader.setMessage(StatusCodeEnum.SUCCESS.getText());
        response.setResponseHeader(responseHeader);
        CommonLogUtil.addInfo(keyWord, "油卡赠送完成，返回结果："+JSONObject.toJSONString(response));
        try {
            redisService.del(key);
        } catch (RedisException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 重发短信
     * @author HuangHao
     * @CreatTime 2020-05-14 18:12
     * @param id
     * @param cityId
     * @return com.tuanche.arch.web.model.TcResponse
     */
    public TcResponse reSendMsg(Integer id,Integer cityId){
        String msg = "此用户未赠送过油卡，不能重发短信";
        String keyWord = "重发短信，重发赠送记录ID:"+id;
        CommonLogUtil.addInfo(keyWord, "开始");
        if(id==null || id < 1){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }
        //获取赠送记录
        GiftRefuelingcardGiftrecordDto giftrecord = giftRefuelingcardGiftrecordReadMapper.getGiftrecordAndRefuelingCode(id);
        if (giftrecord == null){
            CommonLogUtil.addInfo(keyWord, "重发失败，原因："+msg);
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }
        //获取赠送的卡号
        List<GiftRefuelingcardGiftrecordCards> cards = giftRefuelingcardGiftrecordCardsReadMapper.getCardsByGiftrecordId(id);
        if (cards == null || cards.size() <1){
            CommonLogUtil.addInfo(keyWord, "重发失败，原因：获取到赠送的卡号");
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),msg);
        }
        //拼接卡号
        StringBuilder sendMsgCodes = new StringBuilder();
        for(GiftRefuelingcardGiftrecordCards card:cards){
            if(sendMsgCodes.length() < 1){
                sendMsgCodes.append(card.getRefuelingCode());
            }else {
                sendMsgCodes.append(",");
                sendMsgCodes.append(card.getRefuelingCode());
            }
        }
        //发送短信
        TcResponse sendMsgState = sendMsg(giftrecord.getPhone(), cityId, sendMsgCodes.toString());
        CommonLogUtil.addInfo(keyWord, "重发完成，手机号:"+giftrecord.getPhone()+"，短信发送结果："+sendMsgState.getResponseHeader().getMessage());
        return sendMsgState;
    }

    /**
     * 发送短信
     * @author HuangHao
     * @CreatTime 2020-05-14 17:12
     * @param phone
     * @param cityId
     * @param refuelingCodes
     * @return com.tuanche.arch.web.model.TcResponse
     */
    public TcResponse sendMsg(String phone,Integer cityId,String refuelingCodes){
        //发送短信
        SmsDto smsDto = new SmsDto();
        smsDto.setCityId(cityId==null?10:cityId);
        smsDto.setSystemCode(sms_system_code);
        smsDto.setBusinessCode(sms_business_code);
        smsDto.setPhones(phone);
        smsDto.setTemplateId(sms_business_template_notify);
        smsDto.setParamArray(new String[]{"恭喜您，您兑换的100元加油卡卡号为："+refuelingCodes+"，请妥善保管并尽快使用"});
        ResultDto resultDto = smsVerifyCodeService.sendSmsByEmptyTemplant(smsDto);
        String keyWord = "赠送油卡发送短信，手机号："+phone;
        CommonLogUtil.addInfo(keyWord, "短信发送结果："+JSONObject.toJSONString(resultDto)+"发送内容："+JSONObject.toJSONString(smsDto));
        if(resultDto == null || resultDto.getCode() == null || !ManuBaseConstants.RESULT_CODE_10000.equals(resultDto.getCode())){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"短信发送失败，请稍后再试");
        }
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(),"发送成功");
    }

    /**
     * 检查用户是否已经赠送过油卡
     * @author HuangHao
     * @CreatTime 2020-05-14 14:05
     * @param phone
     * @param dealerId
     * @param activityDto
     * @return com.tuanche.arch.web.model.TcResponse
     */
    public TcResponse checkUserGivenState(String phone,Integer dealerId,Integer periodsId){
        GiftRefuelingcardGiftrecordVo checkGiftrecord = new GiftRefuelingcardGiftrecordVo();
        //checkGiftrecord.setDealerId(dealerId);
        checkGiftrecord.setPhone(phone);
        checkGiftrecord.setPeriodsId(periodsId);
        //检查手机号是否已经赠送过
        int giveTotal = giftRefuelingcardGiftrecordReadMapper.getDealerGiveTotal(checkGiftrecord);
        if(giveTotal>0){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"该用户已赠送过油卡，不可重复赠送");
        }
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(),StatusCodeEnum.SUCCESS.getText());
    }
    /**
     * 检查该手机号是否已收货
     * @author HuangHao
     * @CreatTime 2020-05-14 14:08
     * @param phone
     * @param activityDto
     * @return com.tuanche.arch.web.model.TcResponse
     */
    public TcResponse checkUserConfirmReceipt(Integer dealerId,Integer periodsId,String phone){
        TcResponse response = new TcResponse();
        TcResponseHeader responseHeader = new TcResponseHeader();
        //检查该手机号是否已收货
        PlatformOrderDto platformOrder = getConfirmReceipt(dealerId, periodsId,phone);
        if(platformOrder == null){
            responseHeader.setStatus(StatusCodeEnum.PARAM_IS_INVALID.getCode());
            responseHeader.setMessage("未找到符合条件的订单，请确认用户提供了正确的手机号，且已在淘宝确认收货。订单状态同步会有短时间延迟，请稍后再查");
            response.setResponseHeader(responseHeader);
            return response;
        }
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(),StatusCodeEnum.SUCCESS.getText(),platformOrder);
    }
    /**
     * 获取用户确认收货信息
     * @author HuangHao
     * @CreatTime 2020-05-13 18:23
     * @param phone
     * @param platformOrderId
     * @return java.util.List<com.tuanche.merchant.pojo.dto.order.PlatformOrderDto>
     */
    public PlatformOrderDto getConfirmReceipt(Integer dealerId,Integer periodsId,String phone){
        String keyWord = "获取用户订单，手机号："+phone+"，经销商ID："+dealerId+"，场次："+periodsId;
        BusiOrderQueryDto busiOrderQueryDto = new BusiOrderQueryDto();
        busiOrderQueryDto.setDealerId(dealerId);
        busiOrderQueryDto.setPeriodsId(periodsId);
        busiOrderQueryDto.setPhone(phone);
        //获取场次下的商品类型
        List<GiftRefuelingcardPeriodsCommodity> commodityList = giftRefuelingcardPeriodsCommodityReadMapper.getCommodityTypeByPeriodsId(periodsId);
        CommonLogUtil.addInfo(keyWord, "本场次配置的商品类型："+JSONObject.toJSONString(commodityList));
        if(commodityList == null && commodityList.size() < 1){
            return null;
        }

        List<CommodityTypeEnum> commodityTypeList = new ArrayList<>();
        for(GiftRefuelingcardPeriodsCommodity commodity:commodityList){
            CommodityTypeEnum commodityTypeEnum = CommodityTypeEnum.get(commodity.getCommodityType());
            if(commodityTypeEnum != null){
                commodityTypeList.add(commodityTypeEnum);
            }
        }
        busiOrderQueryDto.setCommodityTypeList(commodityTypeList);
        List<TmTradeStatus> statusList = new ArrayList<>();
        statusList.add(TmTradeStatus.TRADE_FINISHED);
        busiOrderQueryDto.setStatusList(statusList);
        PlatformOrderDto platformOrderDto = null;
        CommonLogUtil.addInfo(keyWord, "获取用户订单信息，请求参数："+JSONObject.toJSONString(busiOrderQueryDto));
        //检查该手机号是否已收货
        TcResponse tbOrder = platformOrderService.queryOrder(busiOrderQueryDto,1,20);

        CommonLogUtil.addInfo(keyWord, "获取用户订单信息，请求结果："+JSONObject.toJSONString(tbOrder));
        if(tbOrder != null && StatusCodeEnum.SUCCESS.getCode() == tbOrder.getResponseHeader().getStatus()){
            List<PlatformOrderDto> list = null;
            if(tbOrder.getResponse().getResult() != null){
                list = (List<PlatformOrderDto>) tbOrder.getResponse().getResult();
                //一个订单直接获取
                if(list.size() == 1){
                    platformOrderDto=list.get(0);
                //大于1个订单
                }else{
                    PlatformOrderDto tmallPlatformOrderDto = null;
                    PlatformOrderDto youzanPlatformOrderDto = null;
                    //天猫淘宝订单价格最高的订单
                    BigDecimal tmallMaxOrderMoney = new BigDecimal("-10000");
                    //有赞订单价格最高的订单
                    BigDecimal youzanMaxOrderMoney = new BigDecimal("-10000");
                    //有赞赠送油卡数量
                    Integer youZanGiftNum = 0;
                    //淘宝天猫赠送油卡数量
                    Integer tmallGiftNum = 0;
                    for(PlatformOrderDto orderDto:list){
                        GiftRefuelingcardPeriodsGiftNum refuelGiftNum = giftRefuelingcardPeriodsGiftNumReadMapper.getGiftNumByPriceAndPeriodsId(orderDto.getOrderMoney(), periodsId);
                        //如果配置了赠送油卡规则
                        if(refuelGiftNum != null){
                            //天猫淘宝订单
                            if(PlatformTypeEnum.TMALL.value().equals(orderDto.getPlatformType().value())){
                                //如果查询出来的赠送数量大于上一个赠送数量或者等于上一个赠送数量而且订单金额价格比上一个高
                                if(refuelGiftNum.getGiftNum() > tmallGiftNum ||
                                   (refuelGiftNum.getGiftNum() == tmallGiftNum && tmallMaxOrderMoney.compareTo(orderDto.getOrderMoney()) == -1)){
                                    tmallMaxOrderMoney =orderDto.getOrderMoney();
                                    tmallPlatformOrderDto = orderDto;
                                    tmallGiftNum = refuelGiftNum.getGiftNum();
                                }
                                //有赞订单
                            }else if(PlatformTypeEnum.YOU_ZAN.value().equals(orderDto.getPlatformType().value())){
                                //如果查询出来的赠送数量大于上一个赠送数量或者等于上一个赠送数量而且订单金额价格比上一个高
                                if(refuelGiftNum.getGiftNum() > youZanGiftNum ||
                                        (refuelGiftNum.getGiftNum() == youZanGiftNum && youzanMaxOrderMoney.compareTo(orderDto.getOrderMoney()) == -1)){
                                    youzanMaxOrderMoney =orderDto.getOrderMoney();
                                    youzanPlatformOrderDto = orderDto;
                                    youZanGiftNum = refuelGiftNum.getGiftNum();
                                }
                            }
                        }

                    }
                    CommonLogUtil.addInfo(keyWord, "淘宝天猫最高赠送油卡数量："+tmallGiftNum+"，订单金额："+tmallMaxOrderMoney+"，有赞最高赠送油卡数量："+youZanGiftNum+"，订单金额："+youzanMaxOrderMoney);
                    //淘宝天猫有订单赠送数量大于有赞无订单赠送数量
                    if(tmallGiftNum != 0 && tmallGiftNum > youZanGiftNum){
                        platformOrderDto = tmallPlatformOrderDto;
                    //有赞有订单赠送数量大于淘宝天猫无订单赠送数量
                    }else if(youZanGiftNum != 0 && youZanGiftNum > tmallGiftNum){
                        platformOrderDto = youzanPlatformOrderDto;
                    //有赞和淘宝天猫赠送数量不等于0且赠送数量相同
                    }else if(tmallGiftNum != 0 && youZanGiftNum != 0 && tmallGiftNum == youZanGiftNum){
                            //如果淘宝天猫订单金额等于或大于有赞订单金额则给淘宝天猫订单
                            if(tmallMaxOrderMoney.compareTo(youzanMaxOrderMoney) >= 0){
                                platformOrderDto = tmallPlatformOrderDto;
                                //否则给有赞订单
                            }else{
                                platformOrderDto = youzanPlatformOrderDto;
                            }
                    }
                }
            }
        }
        CommonLogUtil.addInfo(keyWord, "最终赠送的订单："+JSONObject.toJSONString(platformOrderDto));
        if(platformOrderDto != null){
            try {
                //缓存订单-减少前端传递的参数和增加安全性
                String key = BaseCacheKeys.REFUELING_CARD_ORDER_CACHE.getKey()+platformOrderDto.getPlatformOrderId();
                redisService.setex(key, BaseCacheKeys.REFUELING_CARD_ORDER_CACHE.getExpire(), JsonUtil.Object2Json(platformOrderDto));
            } catch (RedisException e) {
                e.printStackTrace();
            }
        }
        return platformOrderDto;

    }

    /**
     * 经销商获取油卡赠送活动信息
     * @author HuangHao
     * @CreatTime 2020-05-13 16:19
     * @param dealerId
     * @return com.tuanche.directselling.dto.GiftRefuelingCardActivityDto
     */
    @Override
    public GiftRefuelingCardActivityDto getActinityInfo(Integer dealerId,Integer periodsId){
        return ongoing(dealerId,periodsId);
    }

    /**
     *  赠送油卡活动是否还在进行中
     * @author HuangHao
     * @CreatTime 2020-05-13 15:40
     * @param
     * @return com.tuanche.directselling.dto.GiftRefuelingCardActivityDto
     */
    public GiftRefuelingCardActivityDto ongoing(Integer dealerId,Integer periodsId){
        GiftRefuelingCardActivityDto activityDto = new GiftRefuelingCardActivityDto();
        activityDto.setOngoing(false);
        Date now = new Date();
        RefuelingCardConfig config = getConfig();
        //如果配置为空则默认30天
        if(config.getTermOfValidity() == null){
            config.setTermOfValidity(30);
        }
        LiveSceneDealerConfig liveSceneDealerConfig = new LiveSceneDealerConfig();
        liveSceneDealerConfig.setDealerId(dealerId);
        liveSceneDealerConfig.setPeriodsId(periodsId);
        DealerRefuelCarActivityInfoDto activityInfoDto = getDealerRefuelCarActivityInfo(liveSceneDealerConfig);
        Date endTime = null;
        //未配置油卡规则
        if(activityInfoDto == null || activityInfoDto.getBeginTime() == null){
            activityDto.setMsg("该场次未配置赠送油卡活动");
        //活动时间未开始
        }else if(now.before(activityInfoDto.getBeginTime())){
            activityDto.setMsg("活动还未开始，开始时间为："+ DateUtil.date2Str(activityInfoDto.getBeginTime(), "yyyy-MM-dd"));
        //活动时间已过
        }else if(activityInfoDto.getEndTime() == null){
            activityDto.setMsg("活动已经结束");
        //活动时间已过
        }else if((endTime=DateUtil.getTime(activityInfoDto.getEndTime(),config.getTermOfValidity())).before(now)){
            activityDto.setMsg("赠送油卡的截止日期为："+ DateUtil.date2Str(endTime, "yyyy-MM-dd") +"，时间已过，不可赠送");
        //已赠送完
        }else if(activityInfoDto.getSurplusCarNum() < 1){
            activityDto.setMsg("本场直播可赠油卡上限为"+activityInfoDto.getRefuelingCardNum()+"张，您已赠送完");
        //活动进行中
        }else {
            activityDto.setOngoing(true);
            activityDto.setMsg("本场直播可赠油卡上限为"+activityInfoDto.getRefuelingCardNum()+"张，" +
                    "还剩"+activityInfoDto.getSurplusCarNum()+"张可赠。赠送油卡的截止时间为："+ DateUtil.date2Str(endTime, "yyyy-MM-dd") +"，请尽快赠送");
            activityDto.setInfo(activityInfoDto);
        }
        return activityDto;
    }




    /**
     * 获取油卡配置信息
     * @author HuangHao
     * @CreatTime 2020-05-13 15:43
     * @param
     * @return com.tuanche.directselling.dto.RefuelingCardConfig
     */
    public RefuelingCardConfig getConfig(){
        RefuelingCardConfig config = JSONObject.parseObject(gift_refuelingcard_config,RefuelingCardConfig.class);
        return config;
    }
    /**
     * 获取经销商的赠送油卡活动信息
     * @author HuangHao
     * @CreatTime 2020-06-03 10:50
     * @param
     * @return com.tuanche.directselling.dto.DealerRefuelCarActivityInfoDto
     */
    DealerRefuelCarActivityInfoDto getDealerRefuelCarActivityInfo(LiveSceneDealerConfig liveSceneDealerConfig){
        if (liveSceneDealerConfig == null || liveSceneDealerConfig.getDealerId() == null || liveSceneDealerConfig.getPeriodsId()==null){
            return null;
        }
        return liveSceneDealerConfigReadMapper.getDealerRefuelCarActivityInfo(liveSceneDealerConfig);
    }

    /**
     * 线索入池
     * @author HuangHao
     * @CreatTime 2020-05-15 15:58
     * @param periodsId
     * @param phone
     * @param brandId
     * @param dealerId
     * @return void
     */
    public void sellingClueApply(CsDirectSellingClueDto clueDto){
        executorService.execute(()->{
            String keyWord = "赠送油卡用户进入线程池";
            ResultDto resultDto = directSellingClueService.existPhoneInSellingClue(clueDto.getPeriodsId(), clueDto.getPhone(), clueDto.getDealerId());
            CommonLogUtil.addInfo(keyWord, "检查是否已经进入线索池："+JSONObject.toJSONString(resultDto));
            //如果线索池没有此条线索则把这条线索入池
            if(resultDto != null && resultDto.getCode() != null && resultDto.getCode().equals(10000) && !(Boolean) resultDto.getResult()){

                ResultDto addResult = directSellingClueService.saveDirectSellingClue(clueDto);
                CommonLogUtil.addInfo(keyWord, "插入线索池，插入结果："+JSONObject.toJSONString(addResult));
            }
        });

    }
}
