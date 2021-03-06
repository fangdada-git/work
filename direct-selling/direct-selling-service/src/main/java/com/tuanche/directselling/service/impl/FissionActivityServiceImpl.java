package com.tuanche.directselling.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.exception.RedisException;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.broadcast.rpc.enums.BroadcastRoomMethodEnum;
import com.tuanche.broadcast.rpc.service.BroadcastRoomBusinessFacadeService;
import com.tuanche.broadcast.rpc.vo.BroadcastRoomRequestDto;
import com.tuanche.broadcast.rpc.vo.BroadcastRoomResponseDto;
import com.tuanche.broadcast.rpc.vo.BroadcastRpcConstants;
import com.tuanche.broadcast.rpc.vo.BroadcastRpcVo;
import com.tuanche.commons.util.Resources;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.api.FissionActivityService;
import com.tuanche.directselling.constant.BaseCacheKeys;
import com.tuanche.directselling.dto.FissionActivityApiDto;
import com.tuanche.directselling.dto.FissionActivityDto;
import com.tuanche.directselling.dto.FissionActivityExtendDto;
import com.tuanche.directselling.dto.FissionActivitySaleDto;
import com.tuanche.directselling.dto.FissionDealerDto;
import com.tuanche.directselling.enums.FissionActivityStatus;
import com.tuanche.directselling.enums.FissionOnStatus;
import com.tuanche.directselling.mapper.read.directselling.FissionActivityDataReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FissionActivityExtendReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FissionActivityReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FissionAwardRuleReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FissionLiveRemindReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FissionShareDataReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FissionUserReadMapper;
import com.tuanche.directselling.mapper.write.directselling.FissionActivityDataWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.FissionActivityExtendWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.FissionActivityWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.FissionAwardRuleWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.FissionLiveRemindWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.FissionShareDataWriteMapper;
import com.tuanche.directselling.model.FissionActivity;
import com.tuanche.directselling.model.FissionActivityData;
import com.tuanche.directselling.model.FissionActivityExtend;
import com.tuanche.directselling.model.FissionAwardRule;
import com.tuanche.directselling.model.FissionLiveRemind;
import com.tuanche.directselling.model.FissionShareData;
import com.tuanche.directselling.model.FissionUser;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.util.EmojiFilter;
import com.tuanche.directselling.util.FuncUtil;
import com.tuanche.directselling.util.ImageUtil;
import com.tuanche.directselling.util.QRCodeGenerator;
import com.tuanche.directselling.utils.BeanCopyUtil;
import com.tuanche.directselling.utils.GlobalConstants;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.FissionActivityApiVo;
import com.tuanche.directselling.vo.FissionActivityAwardVo;
import com.tuanche.directselling.vo.FissionActivityExtendVo;
import com.tuanche.directselling.vo.FissionActivityNameTimeVo;
import com.tuanche.directselling.vo.FissionActivitySaleVo;
import com.tuanche.directselling.vo.FissionActivityVo;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.api.SmsVerifyCodeService;
import com.tuanche.manubasecenter.api.WxTemplateMessageBaseService;
import com.tuanche.manubasecenter.dto.SmsDto;
import com.tuanche.manubasecenter.dto.WxMiniprogramDto;
import com.tuanche.manubasecenter.dto.WxSubscribeDto;
import com.tuanche.manubasecenter.dto.WxUserInfoDto;
import com.tuanche.manubasecenter.model.CsCompany;
import com.tuanche.manubasecenter.model.simpleInfo.CsCompanySimpleInfo;
import com.tuanche.tdn.interfaces.dto.wx.RespDto;
import com.tuanche.tdn.interfaces.dto.wx.WxSubscribeSceneInfoDto;
import com.tuanche.tdn.interfaces.dto.wx.em.SubscribeMsgEnum;
import com.tuanche.tdn.interfaces.service.wx.WxSubscribeMsgService;
import com.tuanche.upload.FtpUtil;
import com.tuanche.upload.UploadProperties;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * @ClassName: FissionActivityServiceImpl
 * @Description: ????????????service
 * @Author: ZhangYiXin
 * @Date: 2020/9/23 18:23
 * @Version 1.0
 **/
@Service
public class FissionActivityServiceImpl implements FissionActivityService {

    private static String tmpFilePath = UploadProperties.getString("temfilePath");
    @Autowired
    FissionActivityWriteMapper fissionActivityWriteMapper;
    @Autowired
    FissionActivityDataReadMapper dataReadMapper;
    @Autowired
    FissionActivityDataWriteMapper dataWriteMapper;
    @Autowired
    FissionActivityExtendReadMapper extendReadMapper;
    @Autowired
    FissionActivityExtendWriteMapper extendWriteMapper;
    @Autowired
    FissionAwardRuleReadMapper awardRuleReadMapper;
    @Autowired
    FissionAwardRuleWriteMapper awardRuleWriteMapper;
    @Reference
    WxTemplateMessageBaseService wxTemplateMessageBaseService;
    @Autowired
    @Qualifier("executorService")
    ExecutorService executorService;
    @Reference
    BroadcastRoomBusinessFacadeService broadcastRoomBusinessFacadeService;
    @Autowired
    FissionUserReadMapper fissionUserReadMapper;
    @Autowired
    FissionShareDataReadMapper fissionShareDataReadMapper;
    @Autowired
    FissionShareDataWriteMapper fissionShareDataWriteMapper;
    @Reference
    WxSubscribeMsgService wxSubscribeMsgService;
    @Autowired
    FissionActivityExtendReadMapper fissionActivityExtendReadMapper;
    @Autowired
    private FissionActivityReadMapper fissionActivityReadMapper;
    @Autowired
    private FissionLiveRemindReadMapper liveRemindReadMapper;
    @Autowired
    private FissionLiveRemindWriteMapper liveRemindWriteMapper;
    @Autowired
    @Qualifier("ClusterRedisService")
    private RedisService redisService;
    @Value("${fission_share_title}")
    private String fissionShareTitle;
    @Value("${fission_share_text}")
    private String fissionShareText;
    // ?fissionId={0}&saleWxOpenId={1}&shareWxOpenId={2}
    @Value("${fission_share_url}")
    private String fissionShareUrl;
    @Value("${fission_app_id}")
    private String fission_app_id;
    @Value("${fission_subscribe_msg_template_id}")
    private String fission_subscribe_msg_template_id;
    /**
     * ?????????????????????
     */
    @Value("${tuanche_activity_appid}")
    private String tuanche_activity_appid;
    /**
     * ???????????????????????????????????????id
     */
    @Value("${tuanche_activity_subscribe_msg_template_id}")
    private String tuanche_activity_subscribe_msg_template_id;
    @Value("${sms_system_code}")
    private String sms_system_code;
    @Value("${sms_business_code}")
    private String sms_business_code;
    @Value("${sms_business_template_notify}")
    private Integer sms_business_template_notify;
    @Value("${wx_subscribe_scene_bizcode}")
    private String bizcode;
    @Value("${tuanche_live_minprogram_appid}")
    private String tuanche_live_minprogram_appid;
    @Value("${tuanche_live_minprogram_pagepath}")
    private String tuanche_live_minprogram_pagepath;
    @Value("${tuanche_activity_mini_subscribe_msg_template_id}")
    private String tuanche_activity_mini_subscribe_msg_template_id;
    @Value("${tuanche_activity_mini_publish_flag}")
    private String tuanche_activity_mini_publish_flag;
    @Reference
    private SmsVerifyCodeService smsVerifyCodeService;
    @Reference
    private CompanyBaseService companyBaseService;

    @Override
    public PageResult findFissionActivityByPage(PageResult<FissionActivity> pageResult, FissionActivity fissionActivity) {
        PageHelper.startPage(pageResult.getPage(), pageResult.getLimit());
        List<FissionActivity> liveFissionActivityDtoList = fissionActivityReadMapper.selectFissionActivityList(fissionActivity);
        PageInfo<FissionActivity> page = new PageInfo<>(liveFissionActivityDtoList);
        pageResult.setCount(page.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
        pageResult.setMsg("");
        pageResult.setData(liveFissionActivityDtoList);
        return pageResult;
    }

    @Override
    public List<FissionActivityVo> findFissionActivityList(FissionActivity fissionActivity) {
        if (fissionActivity == null) {
            fissionActivity = new FissionActivity();
            fissionActivity.setOnState(GlobalConstants.FISSION_ON_STATUS_1);
        }
        List<FissionActivityVo> liveFissionActivityVoList = fissionActivityReadMapper.selectFissionActivityVoList(fissionActivity);
        return liveFissionActivityVoList;
    }

    @Override
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    public Integer saveFissionActivity(FissionActivity fissionActivity) {
        if (fissionActivity != null && fissionActivity.getId() == null) {
            int flag = fissionActivityWriteMapper.insertSelective(fissionActivity);
            // ??????C????????????????????????
            BroadcastRpcVo resultVo = broadcastRoomBusiness(fissionActivity, BroadcastRoomMethodEnum.API_CREATE_ROOM_BY_FISSION);
            if (!BroadcastRpcConstants.RESULT_SUCCESS.equals(resultVo.getResCode())) {
                CommonLogUtil.addInfo("FissionActivityService", "???????????????????????????", JSON.toJSONString(resultVo));
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return 0;
            }
            fissionActivity.setBroadcastRoomId(Integer.valueOf(resultVo.getString("data")));
            fissionActivityWriteMapper.updateByPrimaryKeySelective(fissionActivity);
            return flag;
        } else if (fissionActivity != null && fissionActivity.getId() != null) {
            FissionActivity activity = getFissionActivityById(fissionActivity.getId());
            activity.setPeriodsId(fissionActivity.getPeriodsId());
            activity.setActivityName(fissionActivity.getActivityName());
            BroadcastRpcVo resultVo = broadcastRoomBusiness(activity, BroadcastRoomMethodEnum.API_UPDATE_ROOM_BY_FISSION);
            CommonLogUtil.addInfo(null, "=========????????????????????????????????????????????????????????????????????????" + JSON.toJSONString(resultVo));
            if (!BroadcastRpcConstants.RESULT_SUCCESS.equals(resultVo.getResCode())) {
                CommonLogUtil.addInfo("FissionActivityService", "?????????????????????????????????????????????", JSON.toJSONString(resultVo));
                return 0;
            }
            return fissionActivityWriteMapper.updateByPrimaryKeySelective(fissionActivity);
        }
        return 0;
    }

    /**
     * ??????C????????????????????????
     *
     * @return
     */
    private BroadcastRpcVo broadcastRoomBusiness(FissionActivity fissionActivity, BroadcastRoomMethodEnum broadcastRoomMethodEnum) {
        BroadcastRpcVo vo = new BroadcastRpcVo("1", broadcastRoomMethodEnum, "4");
        BroadcastRoomRequestDto paramDto = new BroadcastRoomRequestDto();
        paramDto.setFissionId(fissionActivity.getId());
        paramDto.setActivityConfigId(fissionActivity.getPeriodsId());
        paramDto.setTitle(fissionActivity.getActivityName());
        if (fissionActivity.getBroadcastRoomId() != null) {
            paramDto.setBroadcastRoomId(fissionActivity.getBroadcastRoomId());
        }
        vo.setInfo("param", JSONObject.toJSONString(paramDto));
        vo.setInfo("reqMessageId", String.valueOf(System.currentTimeMillis()));
        return broadcastRoomBusinessFacadeService.service(vo);
    }

    @Override
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    public Integer saveFissionActivityConfig(FissionActivityExtendVo fissionActivityExtendVo) {
        Date now = new Date();
        //??????????????????
        FissionActivity oldFissionActivity = fissionActivityReadMapper.selectByPrimaryKey(fissionActivityExtendVo.getFissionId());
        String detailPicUrls = fissionActivityExtendVo.getDetailPicUrl1() + "##" + fissionActivityExtendVo.getDetailPicUrl2() + "##" + fissionActivityExtendVo.getDetailPicUrl3();
        FissionActivity fissionActivity = new FissionActivity();
        fissionActivity.setId(fissionActivityExtendVo.getFissionId());
        fissionActivity.setHeadPicUrl(fissionActivityExtendVo.getHeadPicUrl());
        fissionActivity.setSharePicUrl(fissionActivityExtendVo.getSharePicUrl());
        fissionActivity.setDetailPicUrls(detailPicUrls);
        fissionActivity.setPosterUrl(fissionActivityExtendVo.getPosterUrl());
        fissionActivity.setProductTitleUrl(fissionActivityExtendVo.getProductTitleUrl());
        fissionActivity.setReservePhone(fissionActivityExtendVo.getReservePhone());
        fissionActivity.setUpdateDt(now);
        fissionActivity.setUpdateBy(fissionActivityExtendVo.getOperateUser());
        fissionActivity.setWechatTitle(fissionActivityExtendVo.getWechatTitle());
        fissionActivity.setWechatDescription(fissionActivityExtendVo.getWechatDescription());
        fissionActivity.setWechatPic(fissionActivityExtendVo.getWechatPic());
        fissionActivityWriteMapper.updateByPrimaryKeySelective(fissionActivity);
        //???????????????????????????????????????
        if (fissionActivityExtendVo.getPosterUrl() != null && !fissionActivityExtendVo.getPosterUrl().equals(oldFissionActivity.getPosterUrl())) {
            fissionShareDataWriteMapper.deleteShareDataByFId(fissionActivityExtendVo.getFissionId());
        }
        //????????????
        dataWriteMapper.deleteByFissionId(fissionActivityExtendVo.getFissionId());
        FissionActivityData fissionActivityData = new FissionActivityData();
        BeanUtils.copyProperties(fissionActivityExtendVo, fissionActivityData);
        dataWriteMapper.insertSelective(fissionActivityData);
        //????????????
        List<FissionActivityExtend> insertExtends = new ArrayList<>();
        FissionActivityExtend oldFissionExtend = new FissionActivityExtend();
        oldFissionExtend.setFissionId(fissionActivityExtendVo.getFissionId());
        oldFissionExtend.setDataType(GlobalConstants.FISSION_EXTEND_TYPE1);
        oldFissionExtend.setRelStatus(GlobalConstants.FISSION_EXTEND_REL_NO);
        extendWriteMapper.updateByFissionIdAndType(oldFissionExtend);
        List<String> cityIds = Arrays.asList(fissionActivityExtendVo.getCityIds().split(","));
        cityIds.forEach(cityId -> {
            FissionActivityExtend fissionActivityExtend = new FissionActivityExtend();
            fissionActivityExtend.setDataType(GlobalConstants.FISSION_EXTEND_TYPE1);
            fissionActivityExtend.setDataId(Integer.valueOf(cityId));
            fissionActivityExtend.setRelStatus(GlobalConstants.FISSION_EXTEND_REL_YES);
            fissionActivityExtend.setRelTime(now);
            fissionActivityExtend.setOperateUser(fissionActivityExtendVo.getOperateUser());
            fissionActivityExtend.setFissionId(fissionActivityExtendVo.getFissionId());
            insertExtends.add(fissionActivityExtend);
        });
        oldFissionExtend.setDataType(GlobalConstants.FISSION_EXTEND_TYPE2);
        oldFissionExtend.setRelStatus(GlobalConstants.FISSION_EXTEND_REL_NO);
        extendWriteMapper.updateByFissionIdAndType(oldFissionExtend);
        if (!StringUtils.isEmpty(fissionActivityExtendVo.getProductIds())) {
            List<String> productIds = Arrays.asList(fissionActivityExtendVo.getProductIds().split(","));
            productIds.forEach(productId -> {
                FissionActivityExtend fissionActivityExtend = new FissionActivityExtend();
                fissionActivityExtend.setDataType(GlobalConstants.FISSION_EXTEND_TYPE2);
                fissionActivityExtend.setDataId(Integer.valueOf(productId));
                fissionActivityExtend.setRelStatus(GlobalConstants.FISSION_EXTEND_REL_YES);
                fissionActivityExtend.setFissionId(fissionActivityExtendVo.getFissionId());
                fissionActivityExtend.setRelTime(now);
                fissionActivityExtend.setOperateUser(fissionActivityExtendVo.getOperateUser());
                insertExtends.add(fissionActivityExtend);
            });
        }
        extendWriteMapper.batchInsert(insertExtends);
        //????????????
        try {
            redisService.del(BaseCacheKeys.FISSION_ACTIVITY_CONFIG_CACHE.getKey() + fissionActivity.getId());
        } catch (RedisException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * @description ??????????????????????????????
     * @date 2020/9/22 17:10
     * @author lvsen
     */
    @Override
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    public Integer saveFissionActivityAwardRule(FissionActivityAwardVo fissionActivityAwardVo) {
        List<FissionAwardRule> awardRuleList = new ArrayList<>();
        FissionAwardRule fissionAwardRule = new FissionAwardRule();
        BeanUtils.copyProperties(fissionActivityAwardVo, fissionAwardRule);
        awardRuleList.add(fissionAwardRule);
        boolean isnullFlag = false;
        if (fissionActivityAwardVo.getBrowseAward() != null) {
            isnullFlag = true;
            fissionAwardRule.setTaskCode(GlobalConstants.FISSION_AWARD_RULE_TASK_CODE1);
            fissionAwardRule.setAward(fissionActivityAwardVo.getBrowseAward());
            fissionAwardRule.setAwardRule(fissionActivityAwardVo.getBrowseAwardRule());
        }
        if (fissionActivityAwardVo.getSubscribeAward() != null) {
            if (isnullFlag) {
                FissionAwardRule awardRule = new FissionAwardRule();
                BeanUtils.copyProperties(fissionActivityAwardVo, awardRule);
                awardRule.setTaskCode(GlobalConstants.FISSION_AWARD_RULE_TASK_CODE2);
                awardRule.setAward(fissionActivityAwardVo.getSubscribeAward());
                awardRule.setAwardRule(fissionActivityAwardVo.getSubscribeAwardRule());
                awardRuleList.add(awardRule);
            } else {
                isnullFlag = true;
                fissionAwardRule.setTaskCode(GlobalConstants.FISSION_AWARD_RULE_TASK_CODE2);
                fissionAwardRule.setAward(fissionActivityAwardVo.getSubscribeAward());
                fissionAwardRule.setAwardRule(fissionActivityAwardVo.getSubscribeAwardRule());
            }
        }
        if (fissionActivityAwardVo.getBuyActivityAward() != null) {
            if (isnullFlag) {
                FissionAwardRule awardRule = new FissionAwardRule();
                BeanUtils.copyProperties(fissionActivityAwardVo, awardRule);
                awardRule.setTaskCode(GlobalConstants.FISSION_AWARD_RULE_TASK_CODE3);
                awardRule.setAward(fissionActivityAwardVo.getBuyActivityAward());
                awardRule.setAwardRule(fissionActivityAwardVo.getBuyActivityAwardRule());
                awardRuleList.add(awardRule);
            } else {
                isnullFlag = true;
                fissionAwardRule.setTaskCode(GlobalConstants.FISSION_AWARD_RULE_TASK_CODE3);
                fissionAwardRule.setAward(fissionActivityAwardVo.getBuyActivityAward());
                fissionAwardRule.setAwardRule(fissionActivityAwardVo.getBuyActivityAwardRule());
            }
        }
        if (fissionActivityAwardVo.getWatchAward() != null) {
            if (isnullFlag) {
                FissionAwardRule awardRule = new FissionAwardRule();
                BeanUtils.copyProperties(fissionActivityAwardVo, awardRule);
                awardRule.setTaskCode(GlobalConstants.FISSION_AWARD_RULE_TASK_CODE4);
                awardRule.setAward(fissionActivityAwardVo.getWatchAward());
                awardRule.setAwardRule(fissionActivityAwardVo.getWatchAwardRule());
                awardRuleList.add(awardRule);
            } else {
                isnullFlag = true;
                fissionAwardRule.setTaskCode(GlobalConstants.FISSION_AWARD_RULE_TASK_CODE4);
                fissionAwardRule.setAward(fissionActivityAwardVo.getWatchAward());
                fissionAwardRule.setAwardRule(fissionActivityAwardVo.getWatchAwardRule());
            }
        }
        if (fissionActivityAwardVo.getBuyBroadcastAward() != null) {
            if (isnullFlag) {
                FissionAwardRule awardRule = new FissionAwardRule();
                BeanUtils.copyProperties(fissionActivityAwardVo, awardRule);
                awardRule.setTaskCode(GlobalConstants.FISSION_AWARD_RULE_TASK_CODE5);
                awardRule.setAward(fissionActivityAwardVo.getBuyBroadcastAward());
                awardRule.setAwardRule(fissionActivityAwardVo.getBuyBroadcastAwardRule());
                awardRuleList.add(awardRule);
            } else {
                fissionAwardRule.setTaskCode(GlobalConstants.FISSION_AWARD_RULE_TASK_CODE5);
                fissionAwardRule.setAward(fissionActivityAwardVo.getBuyBroadcastAward());
                fissionAwardRule.setAwardRule(fissionActivityAwardVo.getBuyBroadcastAwardRule());
            }
        }
        awardRuleWriteMapper.deleteByFissionId(fissionActivityAwardVo.getFissionId(), fissionActivityAwardVo.getRuleType());
        int flag = awardRuleWriteMapper.batchInsert(awardRuleList);
        if (flag > 0) {
            try {
                redisService.del(BaseCacheKeys.FISSION_ACTIVITY_CONFIG_CACHE.getKey() + fissionActivityAwardVo.getFissionId());
            } catch (RedisException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    @Override
    public FissionActivity getFissionActivityById(Integer fissionId) {
        return fissionActivityReadMapper.selectByPrimaryKey(fissionId);
    }

    @Override
    public List<FissionAwardRule> getAwardRuleListByFissionId(Integer fissionId, Integer type) {
        if (fissionId == null || type == null) {
            return new ArrayList<>();
        }
        FissionActivityDto activityDto = null;
        try {
            activityDto = (FissionActivityDto) redisService.get(BaseCacheKeys.FISSION_ACTIVITY_CONFIG_CACHE.getKey() + fissionId, FissionActivityDto.class);
        } catch (RedisException e) {
            e.printStackTrace();
        }
        if (activityDto != null && !CollectionUtils.isEmpty(activityDto.getAwardRuleList())) {
            return activityDto.getAwardRuleList().stream().filter(t -> Integer.valueOf(t.getRuleType()).equals(type)).collect(Collectors.toList());
        }
        List<FissionAwardRule> fissionAwardRules = awardRuleReadMapper.selectAwardRuleByFissionId(fissionId);
        return fissionAwardRules.stream().filter(t -> Integer.valueOf(t.getRuleType()).equals(type)).collect(Collectors.toList());
    }

    @Override
    public FissionActivityDto getFissionActivityDtoById(Integer fissionId) {
        if (fissionId == null) {
            return new FissionActivityDto();
        }
        FissionActivityDto activityDto = null;
        try {
            activityDto = (FissionActivityDto) redisService.get(BaseCacheKeys.FISSION_ACTIVITY_CONFIG_CACHE.getKey() + fissionId, FissionActivityDto.class);
        } catch (RedisException e) {
            e.printStackTrace();
        }
        if (activityDto != null) {
            extendDetailPic(activityDto);
            return activityDto;
        }
        activityDto = fissionActivityReadMapper.selectFissionActivityDtoById(fissionId);
        if (activityDto == null) {
            return new FissionActivityDto();
        }
        extendDetailPic(activityDto);
        if (GlobalConstants.FISSION_ON_STATUS_1.equals(activityDto.getOnState())) {
            try {
                redisService.set(BaseCacheKeys.FISSION_ACTIVITY_CONFIG_CACHE.getKey() + fissionId, activityDto, 1000 * 60 * 60 * 24);
            } catch (RedisException e) {
                e.printStackTrace();
            }
        }
        return activityDto;
    }

    /**
     * ?????????????????????
     *
     * @param activityDto
     * @return
     */
    private FissionActivityDto extendDetailPic(FissionActivityDto activityDto) {
        if (StringUtils.isEmpty(activityDto.getDetailPicUrls())) {
            return activityDto;
        }
        String[] detailPicArray = activityDto.getDetailPicUrls().split("##");
        if (detailPicArray.length == 1) {
            activityDto.setDetailPicUrl1(detailPicArray[0]);
        } else if (detailPicArray.length == 2) {
            activityDto.setDetailPicUrl1(detailPicArray[0]);
            activityDto.setDetailPicUrl2(detailPicArray[1]);
        } else if (detailPicArray.length == 3) {
            activityDto.setDetailPicUrl1(detailPicArray[0]);
            activityDto.setDetailPicUrl2(detailPicArray[1]);
            activityDto.setDetailPicUrl3(detailPicArray[2]);
        }
        return activityDto;
    }

    /**
     * @return java.lang.Integer
     * @description ???????????????/???????????????????????? joinFlag 0????????????  1??????
     * @date 2020/10/10 11:46
     * @author lvsen
     */
    @Override
    public Integer dealerJoinFission(Integer fissionId, Integer dealerId, Integer optUserId, Integer joinFlag) {
        List<FissionActivityExtend> relDealer = extendReadMapper.selectActivityExtendByTypeAndId(dealerId, fissionId, GlobalConstants.FISSION_EXTEND_TYPE3);
        if (joinFlag == 1) {
            if (CollectionUtils.isEmpty(relDealer)) {
                FissionActivityExtend extend = new FissionActivityExtend();
                extend.setOperateUser(optUserId);
                extend.setRelTime(new Date());
                extend.setFissionId(fissionId);
                extend.setDataId(dealerId);
                extend.setRelStatus(GlobalConstants.FISSION_EXTEND_REL_YES);
                extend.setDataType(GlobalConstants.FISSION_EXTEND_TYPE3);
                FissionActivity fissionActivity = fissionActivityReadMapper.selectByPrimaryKey(fissionId);
                int flag = extendWriteMapper.insertSelective(extend);
                //???????????????????????????????????????????????????
                if (GlobalConstants.FISSION_ON_STATUS_1.equals(fissionActivity.getOnState())) {
                    try {
                        redisService.del(BaseCacheKeys.FISSION_ACTIVITY_CONFIG_CACHE.getKey() + fissionActivity.getId());
                    } catch (RedisException e) {
                        e.printStackTrace();
                    }
                }
                return flag;
            }
        } else if (joinFlag == 0) {
            if (!relDealer.isEmpty()) {
                FissionActivityExtend extend = relDealer.get(0);
                extend.setRelStatus(GlobalConstants.FISSION_EXTEND_REL_NO);
                extend.setRelTime(new Date());
                extend.setOperateUser(optUserId);
                return extendWriteMapper.updateByPrimaryKeySelective(extend);
            } else {
                return 1;
            }
        }
        return 1;
    }

    /**
     * ??????????????????
     *
     * @param fissionId
     * @param optUserId
     * @return
     */
    @Override
    public Integer openFissionActivity(Integer fissionId, Date startTime, Integer optUserId) {
        Date now = new Date();
        FissionActivity fissionActivity = new FissionActivity();
        fissionActivity.setId(fissionId);
        if (now.after(startTime)) {
            fissionActivity.setStartTime(now);
        }
        fissionActivity.setOnState(GlobalConstants.FISSION_ON_STATUS_1);
        fissionActivity.setUpdateBy(optUserId);
        fissionActivity.setUpdateDt(now);
        int flag = fissionActivityWriteMapper.updateByPrimaryKeySelective(fissionActivity);
        if (flag == 1) {
            try {
                redisService.del(BaseCacheKeys.FISSION_ACTIVITY_CONFIG_CACHE.getKey() + fissionId);
            } catch (RedisException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    @Override
    public PageResult getActivitySaleByDealerId(int page, int limit, int dealerId, int saleId) {
        PageResult<FissionActivitySaleVo> pageResult = new PageResult<>();
        PageHelper.startPage(page, limit);
        List<FissionActivitySaleDto> fissionActivitySaleDtoList = fissionActivityReadMapper.selectActivitySaleByDealerId(dealerId, saleId, new Date());
        List<FissionActivitySaleVo> fissionActivitySaleVos = new ArrayList<>();
        FissionActivitySaleVo fissionActivitySaleVo = null;
        Date now = new Date();
        //??????activityStatus 0???????????? 1???????????? 2????????????
        for (FissionActivitySaleDto fissionActivitySaleDto : fissionActivitySaleDtoList) {
            fissionActivitySaleVo = new FissionActivitySaleVo();
            BeanUtils.copyProperties(fissionActivitySaleDto, fissionActivitySaleVo);
            fissionActivitySaleVo.setActivityStatus(FissionActivityStatus.IS_OVER.getStatus());
            if (fissionActivitySaleVo.getStartTime().after(now)) {
                fissionActivitySaleVo.setActivityStatus(FissionActivityStatus.NOT_BEGIN.getStatus());
            } else if (fissionActivitySaleVo.getStartTime().before(now) && fissionActivitySaleDto.getEndTime().after(now)) {
                if (FissionOnStatus.OPEN.getStatus() == fissionActivitySaleDto.getOnState()) {
                    fissionActivitySaleVo.setActivityStatus(FissionActivityStatus.IN_PROGRESS.getStatus());
                } else {
                    fissionActivitySaleVo.setActivityStatus(FissionActivityStatus.NOT_BEGIN.getStatus());
                }
            }
            fissionActivitySaleVos.add(fissionActivitySaleVo);
        }
        PageInfo<FissionActivitySaleVo> pageInfo = new PageInfo(fissionActivitySaleDtoList);
        pageResult.setCount(pageInfo.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode());
        pageResult.setMsg(StatusCodeEnum.SUCCESS.getText());
        pageResult.setData(fissionActivitySaleVos);
        return pageResult;
    }

    /**
     * @param fissionActivityDto ???FissionActivity+FissionAwardRule???
     * @description: ????????????????????????????????????
     * @return: java.util.List<com.tuanche.directselling.dto.FissionActivityDto>
     * @author: dudg
     * @date: 2020/9/27 15:35
     */
    @Override
    public List<FissionActivityDto> selectActivityDtoList(FissionActivityDto fissionActivityDto) {
        List<FissionActivityDto> fissionActivityDtos = fissionActivityReadMapper.selectFissionActiveDtoList(fissionActivityDto);
        return fissionActivityDtos;
    }

    @Override
    public List<FissionActivityNameTimeVo> getFissionActivityList(Integer saleId) {
        List<FissionActivity> fissionActivities = fissionActivityReadMapper.selectFissionActivityBySaleId(saleId);
        List<FissionActivityNameTimeVo> fissionActivitySaleVos = new ArrayList<>();
        FissionActivityNameTimeVo fissionActivityNameTimeVo = null;
        for (FissionActivity fissionActivity : fissionActivities) {
            fissionActivityNameTimeVo = new FissionActivityNameTimeVo();
            BeanUtils.copyProperties(fissionActivity, fissionActivityNameTimeVo);
            fissionActivitySaleVos.add(fissionActivityNameTimeVo);
        }
        return fissionActivitySaleVos;
    }

    /**
     * ?????????????????????????????????(??????)?????????????????????ID??????
     *
     * @param
     * @return java.util.List<java.lang.Integer>
     * @author HuangHao
     * @CreatTime 2020-09-28 17:07
     */
    @Override
    public List<Integer> getOngoingAndConfirmIds() {
        return fissionActivityReadMapper.getOngoingAndConfirmIds();
    }

    /**
     * ?????????????????????????????????(??????)?????????????????????ID??????
     *
     * @param
     * @return java.util.List<java.lang.Integer>
     * @author HuangHao
     * @CreatTime 2020-10-13 17:44
     */
    @Override
    public List<Integer> getEndedYesterdayAndConfirmIds() {
        return fissionActivityReadMapper.getEndedYesterdayAndConfirmIds();
    }

    /**
     * ??????????????????startTime??????????????????????????????????????????ID
     *
     * @param startTime ????????????
     * @return
     */
    @Override
    public List<Integer> getEndAndConfirmActivityIds(String startTime) {
        return fissionActivityReadMapper.getEndAndConfirmActivityIds(startTime);
    }

    // region ***************??????????????????api***************

    /**
     * @param activityApiVo
     * @description: ???????????????-??????????????????
     * @return: com.tuanche.arch.web.model.TcResponse
     * @author: dudg
     * @date: 2020/10/12 15:47
     */
    @Override
    public TcResponse getActivityInfoForApi(FissionActivityApiVo activityApiVo) {
        long st = System.currentTimeMillis();
        CommonLogUtil.addInfo("??????????????????-???????????????????????? start", "?????????", activityApiVo);
        FissionActivityApiDto activityApiDto = new FissionActivityApiDto();
        try {
            TcResponse validate = commValidate(activityApiVo);
            if (validate.getResponseHeader().getStatus() != StatusCodeEnum.SUCCESS.getCode()) {
                return validate;
            }
            FissionActivityDto activityDto = (FissionActivityDto) validate.getResponse().getResult();
            BeanUtils.copyProperties(activityDto, activityApiDto);

            Date now = new Date();
            if (activityDto.getEndTime().before(now)) {
                activityApiDto.setActivityStatus(2);
            } else if (activityDto.getStartTime().after(now) || activityDto.getOnState().intValue() == 0) {
                activityApiDto.setActivityStatus(0);
            } else {
                activityApiDto.setActivityStatus(1);
            }

            // ?????????????????????
            Optional<FissionActivityExtend> goodsOpt = activityDto.getFissionActivityExtend().stream().filter(n -> n.getDataType().equals(GlobalConstants.FISSION_EXTEND_TYPE2)).findAny();
            activityApiDto.setHasGoods(goodsOpt.isPresent() ? 1 : 0);

            if (!StringUtils.isEmpty(activityDto.getDetailPicUrls())) {
                activityApiDto.setDetailPics(Lists.newArrayList(activityDto.getDetailPicUrls().split("##")));
            }

            // ??????????????????
            if (!StringUtil.isEmpty(activityApiVo.getShareWxUnionId())) {
                FissionUser fissionUser = new FissionUser();
                fissionUser.setUserWxUnionId(activityApiVo.getShareWxUnionId());
                List<FissionUser> fissionUsers = fissionUserReadMapper.selectNickNameByUnionId(fissionUser);
                if (CollectionUtil.isNotEmpty(fissionUsers)) {
                    activityApiDto.setShareNick(fissionUsers.get(0).getNickName());
                }
            }

            //???????????????????????????
            BroadcastRoomResponseDto broadcastRoomInfo = getBroadcastRoomInfo(activityDto.getBroadcastRoomId());

            // ???????????????
            calculateEffectNum(activityDto, activityApiDto);
            // ??????&??????????????????????????????
            activityApiDto.setBroadcastRoomId(activityDto.getBroadcastRoomId());
            // 0 ????????? 1 ????????? 2 ?????? 3 ?????????
            if (broadcastRoomInfo != null) {
                activityApiVo.setLiveId(broadcastRoomInfo.getId());
                if (broadcastRoomInfo.getRoomStatus() == 0 && broadcastRoomInfo.getStartTime() != null) {
                    Long countDown = FuncUtil.dateSecDiff(now, broadcastRoomInfo.getStartTime());
                    // ??????????????????
                    activityApiDto.setLiveCountDown(countDown < 0 ? 0 : countDown);
                    // ????????????&wx????????????
                    activityApiDto.setAppointState(liveRemindReadMapper.existAppoint(activityApiVo));
                    if (activityApiDto.getAppointState().intValue() == 0) {
                        // fission_subscribe_msg_redirect_url ????????????
                        String subScribeMsgUrl = GlobalConstants.WEIXIN_SUBSCRIBE_MSG_URL.replace("{APPID}", tuanche_activity_appid)
                                .replace("{SCENE}", String.valueOf(getOrFreeAvailableWxScene(activityApiVo.getFissionId(), false)))
                                .replace("{TEMPLATEID}", tuanche_activity_subscribe_msg_template_id)
                                .replace("{RESERVED}", "tuanche");
                        activityApiDto.setWxSubScribeMsgUrl(subScribeMsgUrl);
                    }
                }
                activityApiDto.setLiveUrl(broadcastRoomInfo.getUrl());
                activityApiDto.setLiveState(broadcastRoomInfo.getRoomStatus());
                activityApiDto.setLiveStartTime(DateUtil.formatDateTime(broadcastRoomInfo.getStartTime()));
            }
        } catch (BeansException e) {
            e.printStackTrace();
            CommonLogUtil.addError("??????????????????-???????????????????????? error", "?????????" + JSON.toJSONString(activityApiVo), e);
            return new TcResponse(StatusCodeEnum.SYSTEM_INNER_ERROR.getCode(), StatusCodeEnum.SYSTEM_INNER_ERROR.getText());
        }
        CommonLogUtil.addInfo("??????????????????-???????????????????????? end", "?????????", activityApiDto);
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), System.currentTimeMillis() - st, StatusCodeEnum.SUCCESS.getText(), activityApiDto);
    }

    /**
     * ?????????????????????
     *
     * @param broadcastRoomId
     * @return
     */
    private BroadcastRoomResponseDto getBroadcastRoomInfo(Integer broadcastRoomId) {
        String source = "1";
        String platform = "4";

        BroadcastRoomRequestDto param = new BroadcastRoomRequestDto();
        param.setBroadcastRoomId(broadcastRoomId);

        BroadcastRpcVo vo = new BroadcastRpcVo(source, BroadcastRoomMethodEnum.API_QUERY_SIMPLE_BY_FISSION, platform);
        vo.setInfo("param", JSONObject.toJSONString(param));
        vo.setInfo("reqMessageId", String.valueOf(System.currentTimeMillis()));
        BroadcastRpcVo resultVo = broadcastRoomBusinessFacadeService.service(vo);
        if (resultVo != null) {
            if (resultVo.getResCode().equals(BroadcastRpcConstants.RESULT_SUCCESS)) {
                // ?????????
                List<BroadcastRoomResponseDto> list = JSONObject.parseArray(resultVo.getString("data"), BroadcastRoomResponseDto.class);
                return list.get(0);
            }
        }

        return null;
    }

    /**
     * ???????????????????????????
     *
     * @return
     */
    private Integer getOrFreeAvailableWxScene(Integer fissionId, Boolean isFree) {
        Integer wxScene = null;
//        String wxSceneKey = BaseCacheKeys.FISSION_WX_SUBSCRIBE_SCENE_CACHE.setParamValue(fissionId);
//        try {
//            wxScene = FuncUtil.redisGetOrSetData(redisService, wxSceneKey, Integer.class, () -> {
//                List<FissionLiveRemind> useOfWxSceneList = liveRemindReadMapper.useOfWxSceneList();
//                Optional<FissionLiveRemind> fissionSceneOpt = useOfWxSceneList.stream().filter(n -> n.getFissionId().equals(fissionId)).findAny();
//                if (fissionSceneOpt.isPresent()) {
//                    return fissionSceneOpt.get().getWxScene();
//                }
//
//                List<Integer> wxSceneList = useOfWxSceneList.stream().map(FissionLiveRemind::getWxScene).collect(Collectors.toList());
//                int rdmScene = 1;
//                // ???????????????????????????
//                while (CollectionUtil.isNotEmpty(useOfWxSceneList) && wxSceneList.contains(rdmScene)) {
//                    // ??????1-1000 ?????????
//                    rdmScene = RandomUtil.randomInt(1, 1000);
//                }
//                return rdmScene;
//            }, BaseCacheKeys.FISSION_WX_SUBSCRIBE_SCENE_CACHE.getExpire());
//        } catch (RedisException e) {
//            e.printStackTrace();
//            CommonLogUtil.addError("??????????????????????????????????????????error", e.getMessage(), e);
//        }

        WxSubscribeSceneInfoDto sceneInfoDtoParam = new WxSubscribeSceneInfoDto();
        sceneInfoDtoParam.setBizId(fissionId);
        sceneInfoDtoParam.setBizCode(bizcode);
        sceneInfoDtoParam.setStatus(1);
        if (isFree) {
            wxSubscribeMsgService.releaseSubscribeScene(sceneInfoDtoParam);
            return wxScene;
        }

        sceneInfoDtoParam.setAppid(tuanche_activity_appid);
        RespDto<WxSubscribeSceneInfoDto> subscribeScene = wxSubscribeMsgService.querySubscribeScene(sceneInfoDtoParam);
        CommonLogUtil.addInfo("???????????????????????????querySubscribeScene", "querySubscribeScene???", subscribeScene);
        if (SubscribeMsgEnum.SUCCESS.getCode().equalsIgnoreCase(subscribeScene.getReturnCode())) {
            wxScene = subscribeScene.getData().getScene();
        } else {
            subscribeScene = wxSubscribeMsgService.createSubscribeScene(sceneInfoDtoParam);
            CommonLogUtil.addInfo("???????????????????????????querySubscribeScene", "createSubscribeScene???", subscribeScene);
            if (SubscribeMsgEnum.SUCCESS.getCode().equalsIgnoreCase(subscribeScene.getReturnCode()) || SubscribeMsgEnum.SCENE_HAVE_USER.getCode().equalsIgnoreCase(subscribeScene.getReturnCode())) {
                wxScene = subscribeScene.getData().getScene();
            }
        }

        return wxScene;
    }

    /**
     * ?????????????????????
     *
     * @param activityDto
     * @param activityApiDto
     * @return
     */
    private void calculateEffectNum(FissionActivityDto activityDto, FissionActivityApiDto activityApiDto) {
        Integer realBrowseNum = activityDto.getFissionActivityData().getBrowseNum() + getOrIncrEffectNum(activityDto.getId(), GlobalConstants.FissionEffectTypeEnum.BROWSE_NUM, true);
        Integer realAppointNum = activityDto.getFissionActivityData().getSubscribeNum() + getOrIncrEffectNum(activityDto.getId(), GlobalConstants.FissionEffectTypeEnum.SUBSCRIBE_NUM, true);
        Integer realShareNum = activityDto.getFissionActivityData().getShareNum() + getOrIncrEffectNum(activityDto.getId(), GlobalConstants.FissionEffectTypeEnum.SHARE_NUM, true);

        activityApiDto.setBrowseNum(activityDto.getFissionActivityData().getBrowseBase() + realBrowseNum * activityDto.getFissionActivityData().getBrowseCoefficient());
        activityApiDto.setAppointNum(activityDto.getFissionActivityData().getSubscribeBase() + realAppointNum * activityDto.getFissionActivityData().getSubscribeCoefficient());
        activityApiDto.setShareNum(activityDto.getFissionActivityData().getShareBase() + realShareNum * activityDto.getFissionActivityData().getShareCoefficient());
    }

    /**
     * @param fissionId
     * @param effectNumType ??????????????? 1 ????????? 2 ????????? 3 ?????????
     * @param isGet
     * @description: ???/?????? ?????????????????????
     * @return: java.lang.Integer  null ?????????/??????
     * @author: dudg
     * @date: 2020/10/12 13:40
     */
    @Override
    public Integer getOrIncrEffectNum(Integer fissionId, GlobalConstants.FissionEffectTypeEnum effectNumType, Boolean isGet) {
        try {
            // hashKey
            String hKey = BaseCacheKeys.FISSION_EFFECT_NUM_CACHE.setParamValue(fissionId);
            // fieldKey
            String fKey = effectNumType.getKey();
            if (isGet) {
                Integer num = redisService.hget(hKey, fKey, Integer.class);
                return num == null ? 0 : num;
            }
            Long result = redisService.hincrby(hKey, fKey, 1);
            return result.intValue();
        } catch (RedisException e) {
            e.printStackTrace();
            CommonLogUtil.addError("??????/??????????????????????????? error", MessageFormat.format("?????????fissionId:{0}???effectNumType:{1}", fissionId, effectNumType.getCode()), e);
        }

        return 0;
    }

    /**
     * @param activityApiVo
     * @description: ???????????????-????????????????????????
     * @return: com.tuanche.arch.web.model.TcResponse
     * @author: dudg
     * @date: 2020/10/13 17:58
     */
    @Override
    public TcResponse appointLiveForApi(FissionActivityApiVo activityApiVo) {
        long st = System.currentTimeMillis();
        CommonLogUtil.addInfo("??????????????????-???????????????????????? start", "?????????", activityApiVo);
        try {
            // ????????????
            TcResponse validate = commValidate(activityApiVo);
            if (validate.getResponseHeader().getStatus() != StatusCodeEnum.SUCCESS.getCode()) {
                return validate;
            }

            if (StringUtil.isEmpty(activityApiVo.getUserWxUnionId())) {
                return new TcResponse(StatusCodeEnum.PARAM_IS_BLANK.getCode(), "????????????UnionId??????");
            }
            if (StringUtil.isEmpty(activityApiVo.getUserWxOpenId())) {
                return new TcResponse(StatusCodeEnum.PARAM_IS_BLANK.getCode(), "????????????OpenId??????");
            }
            if (FuncUtil.isNullOrEquals(activityApiVo.getWxSubScribeScene(), 0)) {
                return new TcResponse(StatusCodeEnum.PARAM_IS_BLANK.getCode(), "?????????????????????????????????");
            }
            FissionActivityDto activityDto = (FissionActivityDto) validate.getResponse().getResult();
            // ?????????????????????&??????????????????
            activityApiVo.setLiveId(activityDto.getBroadcastRoomId());
            int exist = liveRemindReadMapper.existAppoint(activityApiVo);
            // ??????????????????
            if (exist > 0) {
                return new TcResponse(StatusCodeEnum.DATA_ALREADY_EXISTED.getCode(), "???????????????");
            }

            FissionLiveRemind liveRemind = new FissionLiveRemind();
            liveRemind.setFissionId(activityDto.getId());
            liveRemind.setLiveId(activityDto.getBroadcastRoomId());
            if (!StringUtil.isEmpty(activityApiVo.getSaleWxUnionId()) && !activityApiVo.getSaleWxUnionId().equals("null")) {
                liveRemind.setSaleWxUnionId(activityApiVo.getSaleWxUnionId());
            }
            liveRemind.setUserWxOpenId(activityApiVo.getUserWxOpenId());
            if (!StringUtil.isEmpty(activityApiVo.getShareWxUnionId()) && !activityApiVo.getShareWxUnionId().equals("null")) {
                liveRemind.setShareWxUnionId(activityApiVo.getShareWxUnionId());
            }
            liveRemind.setUserWxUnionId(activityApiVo.getUserWxUnionId());
            liveRemind.setUserPhone(activityApiVo.getUserPhone());
            liveRemind.setWxScene(activityApiVo.getWxSubScribeScene());
            liveRemind.setRemindState(false);
            liveRemind.setUserId(activityApiVo.getUserId());
            liveRemind.setChannel(activityApiVo.getChannel());
            liveRemindWriteMapper.insertSelective(liveRemind);
        } catch (Exception e) {
            e.printStackTrace();
            CommonLogUtil.addError("??????????????????-???????????????????????? error", "?????????" + JSON.toJSONString(activityApiVo), e);
            return new TcResponse(StatusCodeEnum.SYSTEM_INNER_ERROR.getCode(), StatusCodeEnum.SYSTEM_INNER_ERROR.getText());
        }
        CommonLogUtil.addInfo("??????????????????-???????????????????????? end", "?????????", StatusCodeEnum.SUCCESS.getText());
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), System.currentTimeMillis() - st, StatusCodeEnum.SUCCESS.getText());
    }

    /**
     * @param activityApiVo
     * @description: ???????????????????????????
     * @return: com.tuanche.arch.web.model.TcResponse
     * @author: dudg
     * @date: 2020/10/13 12:05
     */
    @Override
    public TcResponse getShareInfoForApi(FissionActivityApiVo activityApiVo) {
        long st = System.currentTimeMillis();
        CommonLogUtil.addInfo("??????????????????-??????????????????????????? start", "?????????", activityApiVo);
        FissionActivityApiDto activityApiDto = new FissionActivityApiDto();
        try {
            TcResponse validate = commValidate(activityApiVo);
            if (validate.getResponseHeader().getStatus() != StatusCodeEnum.SUCCESS.getCode()) {
                return validate;
            }

            FissionActivityDto activityDto = (FissionActivityDto) validate.getResponse().getResult();
            // ??????url&??????&??????
            String shareUrl = MessageFormat.format(fissionShareUrl, activityApiVo.getFissionId(), activityApiVo.getSaleWxUnionId(), activityApiVo.getUserWxUnionId());
            //??????????????? ???????????????
            if (activityApiVo.getChannel() != null && activityApiVo.getChannel() != 0) {
                shareUrl = shareUrl + "&channel=" + activityApiVo.getChannel();
            }
            activityApiDto.setShareUrl(shareUrl);
            activityApiDto.setShareTitle(activityDto.getWechatTitle());
            activityApiDto.setShareText(activityDto.getWechatDescription());
            activityApiDto.setWechatPic(activityDto.getWechatPic());
            activityApiDto.setSharePicUrl(activityDto.getSharePicUrl());
            //?????????????????? ???????????????????????????/?????????
            FissionShareData fissionShareData = new FissionShareData();
            fissionShareData.setFissionId(activityApiVo.getFissionId());
            fissionShareData.setSaleWxUnionId(activityApiVo.getSaleWxUnionId());
            fissionShareData.setUserWxUnionId(activityApiVo.getUserWxUnionId());
            fissionShareData.setShareUrl(activityApiDto.getShareUrl() + "&source=2");
            List<FissionShareData> shareDataList = fissionShareDataReadMapper.selectShareData(fissionShareData);
            if (!CollectionUtil.isEmpty(shareDataList)) {
                activityApiDto.setPosterUrl(shareDataList.get(0).getSharePosterUrl());
            } else {
                createSharePic(activityApiVo, activityDto, activityApiDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonLogUtil.addError("??????????????????-??????????????????????????? error", "?????????" + JSON.toJSONString(activityApiVo), e);
            return new TcResponse(StatusCodeEnum.SYSTEM_INNER_ERROR.getCode(), StatusCodeEnum.SYSTEM_INNER_ERROR.getText());
        }
        CommonLogUtil.addInfo("??????????????????-??????????????????????????? start", "?????????", activityApiDto);
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), System.currentTimeMillis() - st, StatusCodeEnum.SUCCESS.getText(), activityApiDto);
    }

    /**
     * ???????????????&??????????????????
     *
     * @param activityDto
     * @param activityApiDto
     */
    private void createSharePic(FissionActivityApiVo activityApiVo, FissionActivityDto activityDto, FissionActivityApiDto activityApiDto) {
        try {
            File file = new File("");
            String serverPath = file.getCanonicalPath() + File.separator + tmpFilePath + File.separator;

            File dirFile = new File(serverPath);
            if (!dirFile.exists() && !dirFile.isDirectory()) {
                dirFile.mkdirs();
            }

            // template
            String templatePicPath = serverPath + UUID.randomUUID().toString() + ".jpg";
            File templageFile = new File(templatePicPath);
            ImageUtil.getUrlImageFile(activityDto.getPosterUrl(), templageFile);

            // target
            String fileName = System.currentTimeMillis() + "" + new Random().nextInt(10000) + ".jpg";
            String targetPicPath = serverPath + fileName;
            File targetPicFile = new File(targetPicPath);

            String shareUrl = activityApiDto.getShareUrl() + "&source=2";
            // qr
            String qrPicPath = serverPath + UUID.randomUUID().toString() + ".jpg";
            File qrPicFile = new File(qrPicPath);
            QRCodeGenerator.encode(shareUrl, 275, qrPicPath, true);

            // ????????????
            ImageUtil.mergeImage(templageFile.getAbsolutePath(), qrPicPath, targetPicFile.getAbsolutePath(), 120, 360);
            String tmpPath = "fission/" + new SimpleDateFormat("yyyyMMdd").format(new Date());
            // ????????????
            FtpUtil.ftpUpload(Resources.getString("ftp.host"), Resources.getString("ftp.username"),
                    Resources.getString("ftp.password"), serverPath, tmpPath, fileName);

            String httpUrl = Resources.getString("pic.url") + "/zixun/" + tmpPath + "/" + fileName;
            activityApiDto.setPosterUrl(httpUrl);
            // ??????????????????
            FissionShareData shareData = new FissionShareData(activityApiVo.getFissionId(), activityApiVo.getSaleWxUnionId(), activityApiVo.getUserWxUnionId());
            shareData.setShareUrl(shareUrl);
            shareData.setUserWxNick(EmojiFilter.filterEmoji(activityApiVo.getUserWxNick()));
            shareData.setSharePosterUrl(httpUrl);
            shareData.setDeleteFlag(false);
            shareData.setUpdateDt(new Date());
            fissionShareDataWriteMapper.insertSelective(shareData);

            if (templageFile.exists()) {
                templageFile.delete();
            }
            if (targetPicFile.exists()) {
                targetPicFile.delete();
            }
            if (qrPicFile.exists()) {
                qrPicFile.delete();
            }
        } catch (Exception e) {
            activityApiDto.setPosterUrl(activityDto.getPosterUrl());
            CommonLogUtil.addError("????????????????????????error", e.getMessage(), e);
        }
    }

    /**
     * Api????????????
     *
     * @param activityApiVo
     * @return
     */
    private TcResponse commValidate(FissionActivityApiVo activityApiVo) {
        // ????????????
        if (!FuncUtil.notNullAndEquals(activityApiVo.getFissionId(), 0)) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "????????????");
        }
        if (StringUtil.isEmpty(activityApiVo.getUserWxUnionId())) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "UserWxUnionId??????");
        }
        FissionActivityDto activityDto = getFissionActivityDtoById(activityApiVo.getFissionId());
        if (activityDto == null || activityDto.getId() == null) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "????????????");
        }
        Optional<FissionActivityExtend> nationWideOpt = activityDto.getFissionActivityExtend().stream()
                .filter(n -> n.getDataType().equals(GlobalConstants.FISSION_EXTEND_TYPE1) && n.getDataId().equals(Integer.valueOf(-1)))
                .findAny();

        if (!nationWideOpt.isPresent()) {
            if (FuncUtil.isNullOrEquals(activityApiVo.getCityId(), 0)) {
                return new TcResponse(StatusCodeEnum.PARAM_IS_BLANK.getCode(), "????????????");
            }
            Optional<FissionActivityExtend> cityOptional = activityDto.getFissionActivityExtend().stream()
                    .filter(n -> n.getDataType().equals(GlobalConstants.FISSION_EXTEND_TYPE1) && n.getDataId().equals(activityApiVo.getCityId()))
                    .findAny();
            if (!cityOptional.isPresent()) {
                return new TcResponse(StatusCodeEnum.PARAM_IS_BLANK.getCode(), "?????????????????????????????????");
            }
        }
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), "", activityDto);
    }

    /**
     * @param
     * @description: ??????????????????????????????????????????
     * @return: void
     * @author: dudg
     * @date: 2020/10/14 11:14
     */
    @Override
    public void syncActivityEffectNum() {
        long st = System.currentTimeMillis();
        // ?????????????????????????????????
        FissionActivity activity = new FissionActivity();
        activity.setOnState(GlobalConstants.FISSION_ON_STATUS_1);
        List<FissionActivity> activityList = fissionActivityReadMapper.selectFissionActivityList(activity);
        for (FissionActivity item : activityList) {
            try {
                Integer browseNum = getOrIncrEffectNum(item.getId(), GlobalConstants.FissionEffectTypeEnum.BROWSE_NUM, true);
                if (browseNum > 0) {
                    FissionActivityData activityData = new FissionActivityData();
                    activityData.setFissionId(item.getId());
                    activityData.setBrowseNum(browseNum);
                    activityData.setSubscribeNum(getOrIncrEffectNum(item.getId(), GlobalConstants.FissionEffectTypeEnum.SUBSCRIBE_NUM, true));
                    activityData.setShareNum(getOrIncrEffectNum(item.getId(), GlobalConstants.FissionEffectTypeEnum.SHARE_NUM, true));

                    if (dataWriteMapper.updateEffectNumByFissionId(activityData) > 0) {
                        redisService.del(BaseCacheKeys.FISSION_EFFECT_NUM_CACHE.setParamValue(item.getId()));
                        redisService.del(BaseCacheKeys.FISSION_ACTIVITY_CONFIG_CACHE.getKey() + item.getId());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                CommonLogUtil.addError("??????????????????????????? error", e.getMessage(), e);
            }
        }

        CommonLogUtil.addInfo("?????????????????????????????????", "??????ms???" + (System.currentTimeMillis() - st));
    }

    /**
     * @param liveId
     * @description: ???????????????????????????????????????
     * @return: void
     * @author: dudg
     * @date: 2020/10/14 16:08
     */
    @Override
    public void liveStartRemind(Integer liveId) {
        String logKey = "???????????????????????????????????????????????????" + UUID.randomUUID().toString();
        CommonLogUtil.addInfo(logKey, "??????" + liveId);
        try {
            BroadcastRoomResponseDto broadcastRoomInfo = getBroadcastRoomInfo(liveId);
            // ??????????????????
            if (broadcastRoomInfo == null) {
                CommonLogUtil.addInfo(logKey, "?????????Id ?????????");
                return;
            }

            // ????????????????????????
            FissionLiveRemind liveRemind = new FissionLiveRemind();
            liveRemind.setRemindState(false);
            liveRemind.setLiveId(liveId);
            List<FissionLiveRemind> liveRemindList = liveRemindReadMapper.selectLiveRemindList(liveRemind);

            // ????????????????????????
            List<WxSubscribeDto> wxSubscribeDtos = Lists.newArrayList();
            List<String> phones = new ArrayList<>();
            List<String> contents = new ArrayList<>();
            String liveUrl = broadcastRoomInfo.getUrl() + (broadcastRoomInfo.getUrl().contains("?") ? "&" : "?") + "fissionId={0}&saleWxUnionId={1}&shareWxUnionId={2}&channel={3}";
            String miniLiveUrl = tuanche_live_minprogram_pagepath + broadcastRoomInfo.getId() + "&fissionId={0}&saleWxUnionId={1}&shareWxUnionId={2}&channel={3}";
            Map<String, Map<String, Object>> dataMap = Maps.newHashMap();
            dataMap.put("content", wxTemplateMessageBaseService.getValMap("????????????????????????????????????????????????????????????????????????????????????"));
            WxMiniprogramDto miniProgramDto;
            for (FissionLiveRemind item : liveRemindList) {
                WxSubscribeDto subscribeDto = new WxSubscribeDto();
                //tuanche_activity_mini_publish_flag ???0 ?????????H5,???0 ??????????????????
                if (!"0".equals(tuanche_activity_mini_publish_flag)) {
                    String liveUlrMini = MessageFormat.format(miniLiveUrl, item.getFissionId(), item.getSaleWxUnionId() != null ? item.getSaleWxUnionId() : ""
                            , item.getShareWxUnionId() != null ? item.getShareWxUnionId() : "", item.getChannel() != null ? item.getChannel() : "");
                    miniProgramDto = new WxMiniprogramDto(tuanche_live_minprogram_appid, liveUlrMini);
                    subscribeDto.setMiniprogram(miniProgramDto);
                }
                subscribeDto.setTemplate_id(tuanche_activity_subscribe_msg_template_id);
                subscribeDto.setTitle("??????????????????");
                subscribeDto.setData(dataMap);
                subscribeDto.setTouser(item.getUserWxOpenId());
                subscribeDto.setScene(String.valueOf(item.getWxScene()));
                subscribeDto.setUrl(MessageFormat.format(liveUrl, item.getFissionId(), item.getSaleWxUnionId() != null ? item.getSaleWxUnionId() : "",
                        item.getShareWxUnionId() != null ? item.getShareWxUnionId() : "", item.getChannel() != null ? item.getChannel() : ""));
                wxSubscribeDtos.add(subscribeDto);

//                if (item.getUserPhone() != null) {
//                    phones.add(item.getUserPhone());
//                    contents.add("??????????????????????????????????????????????????????????????????????????????????????????\r\n" + MessageFormat.format(liveUlrMini, item.getFissionId(), item.getSaleWxOpenId(), item.getShareWxOpenId(), item.getChannel()));
//                }
            }

            if (!CollectionUtil.isEmpty(wxSubscribeDtos)) {
                double sendNum= 20;
                int sn = (int)sendNum;
                if(wxSubscribeDtos.size()>sendNum){
                    int listSize = wxSubscribeDtos.size();
                    double pageTotal = listSize/sendNum;
                    int pageIndex = (int)Math.ceil(pageTotal);
                    for (int i = 0; i < pageIndex; i++) {
                        List<WxSubscribeDto> wxSubscribeList = null;
                        if(i==pageIndex-1){
                            wxSubscribeList =wxSubscribeDtos.subList(i*sn, listSize);
                        }else {
                            wxSubscribeList =wxSubscribeDtos.subList(i*sn, (i+1)*sn);
                        }
                        //??????????????????????????????
                        wxTemplateMessageBaseService.batchSendSubscribe(StringUtil.isEmpty(tuanche_activity_appid) ? "wxc61c7c5ddb8d1318" : tuanche_activity_appid, wxSubscribeList);
                    }
                }
            }
            //sendMessages(phones, contents);
            // ??????????????????
            liveRemindWriteMapper.updateRemindStateByLiveId(liveId);

            // ???????????????
            if (!CollectionUtil.isEmpty(liveRemindList)) {
                executorService.execute(() -> {
                    liveRemindList.stream().map(FissionLiveRemind::getFissionId).distinct().collect(Collectors.toList()).forEach(n -> {
                        getOrFreeAvailableWxScene(n, true);
                    });
                });
            }
            CommonLogUtil.addInfo(logKey, "??????????????????????????????" + liveRemindList.size());
        } catch (Exception e) {
            e.printStackTrace();
            CommonLogUtil.addError(logKey, "???????????????????????????????????? error", e);
        }
    }

    /**
     * @param code
     * @description: ?????????????????????OpenId
     * @return: com.tuanche.manubasecenter.dto.WxUserInfoDto
     * @author: dudg
     * @date: 2020/10/16 18:32
     */
    @Override
    public WxUserInfoDto getWxUserInfoByCode(String code) {
        return wxTemplateMessageBaseService.getWxUserInfoByAppIdAndCode(tuanche_activity_appid, code);
    }


    @Override
    public PageResult findFissionActivityByPage(int page, int limit, String activityName) {
        PageHelper.startPage(page, limit);
        PageResult<FissionActivityNameTimeVo> pageResult = new PageResult<>();
        List<FissionActivity> liveFissionActivityDtoList = fissionActivityReadMapper.selectFissionActivityByName(activityName);
        PageInfo<FissionActivity> pageInfo = new PageInfo<>(liveFissionActivityDtoList);
        pageResult.setCount(pageInfo.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode());
        pageResult.setMsg("");
        pageResult.setData(BeanCopyUtil.copyListProperties(liveFissionActivityDtoList, FissionActivityNameTimeVo::new));
        return pageResult;
    }

    private void sendMessages(List<String> phones, List<String> contents) {
        if (phones.isEmpty()) {
            return;
        }
        SmsDto smsDto = new SmsDto();
        smsDto.setCityId(10);
        smsDto.setSystemCode(sms_system_code);
        smsDto.setBusinessCode(sms_business_code);
        smsDto.setTemplateId(sms_business_template_notify);
        smsDto.setPhones(String.join(",", phones));
        smsDto.setParamArray(contents.toArray(new String[contents.size()]));
        ResultDto resultDto = smsVerifyCodeService.sendSmsByEmptyTemplant(smsDto);
        String keyWord = "?????????????????????????????????????????????" + smsDto.getPhones();
        CommonLogUtil.addInfo(keyWord, "?????????????????????" + JSONObject.toJSONString(resultDto) + "???????????????" + JSONObject.toJSONString(smsDto));
    }

    /**
     * ????????????????????????????????????
     *
     * @param fissionActivityExtend
     * @return java.util.List<com.tuanche.directselling.model.FissionActivity>
     * @author HuangHao
     * @CreatTime 2020-12-15 14:40
     */
    @Override
    public List<FissionActivity> getDealerActivityList(FissionActivityExtend fissionActivityExtend) {
        if (fissionActivityExtend == null || fissionActivityExtend.getDataId() == null) {
            return null;
        }
        return fissionActivityExtendReadMapper.getDealerActivityList(fissionActivityExtend);
    }

    /**
     * ??????H5 ????????? ??????0??????H5?????????1???????????????
     *
     * @return
     */
    @Override
    public TcResponse activityH5OrMiniProgram() {
        // flag 0 H5  1?????????
        String flag = "0".equals(tuanche_activity_mini_publish_flag) ? "0" : "1";
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), "????????????", flag);
    }

    /**
     * ??????????????????????????????-??????
     *
     * @param fissionId
     * @return java.util.List<com.tuanche.directselling.dto.FissionActivityExtendDto>
     * @author HuangHao
     * @CreatTime 2021-04-14 13:59
     */
    @Override
    public PageResult<FissionActivityExtendDto> getActivityDealerListByPage(FissionActivityExtendDto settlementAccount) {
        PageResult<FissionActivityExtendDto> pageResult = new PageResult<>();
        if (settlementAccount == null) {
            return pageResult;
        }
        PageHelper.startPage(settlementAccount.getPage(), settlementAccount.getLimit());
        List<FissionActivityExtendDto> list = getActivityExtendByType(settlementAccount.getFissionId(), GlobalConstants.FISSION_EXTEND_TYPE3, null);
        PageInfo<FissionActivityExtendDto> page = new PageInfo<>(list);
        pageResult.setCount(page.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
        pageResult.setMsg("");
        pageResult.setData(list);
        return pageResult;
    }

    /**
     * ??????????????????????????????
     *
     * @param fissionId
     * @return java.util.List<com.tuanche.directselling.dto.FissionActivityExtendDto>
     * @author HuangHao
     * @CreatTime 2021-04-14 13:59
     */
    @Override
    public List<FissionActivityExtendDto> getActivityDealerList(Integer fissionId) {
        return getActivityExtendByType(fissionId, GlobalConstants.FISSION_EXTEND_TYPE3, null);
    }

    @Override
    public List<FissionDealerDto> selectActivityDealerList(Integer fissionId, Integer cityId, Integer dealerId) {
        List<FissionDealerDto> dealerDtoList = new ArrayList<>();
        if (fissionId != null) {
            List<FissionActivityExtendDto> list = getActivityExtendByType(fissionId, GlobalConstants.FISSION_EXTEND_TYPE3, dealerId);
            if (!CollectionUtils.isEmpty(list)) {
                List<Integer> dealerIds = list.stream().map(FissionActivityExtend::getDataId).collect(Collectors.toList());
                CsCompany company = new CsCompany();
                company.setDealerIdList(dealerIds);
                company.setCityId(cityId);
                List<CsCompanySimpleInfo> simpleInfos = companyBaseService.getDealerSimpleInfo(company);
                simpleInfos.forEach(v -> {
                    FissionDealerDto dto = new FissionDealerDto();
                    dto.setDealerId(v.getId());
                    dto.setDealerName(v.getCompanyName());
                    dto.setAddress(v.getAddress());
                    dealerDtoList.add(dto);
                });
            }
        }
        return dealerDtoList;
    }

    /**
     * ??????????????????????????????
     *
     * @param fissionActivityExtend
     * @return java.util.List<com.tuanche.directselling.model.FissionActivity>
     * @author HuangHao
     * @CreatTime 2020-12-15 14:40
     */
    public List<FissionActivityExtendDto> getActivityExtendByType(Integer fissionId, Byte type, Integer dealerId) {
        if (type == null) {
            return null;
        }
        FissionActivityExtend fissionActivityExtend = new FissionActivityExtend();
        fissionActivityExtend.setFissionId(fissionId);
        fissionActivityExtend.setDataType(type);
        fissionActivityExtend.setDataId(dealerId);
        return fissionActivityExtendReadMapper.getActivityExtendByType(fissionActivityExtend);
    }

    /**
     * ????????????????????????????????????-??????
     *
     * @param fissionId
     * @param dealerId
     * @return java.util.List<com.tuanche.directselling.dto.FissionActivityExtendDto>
     * @author HuangHao
     * @CreatTime 2021-04-21 16:00
     */
    public PageResult<FissionActivityExtendDto> getFissionDealerSettlementAccountListByPage(FissionActivityExtendDto settlementAccount) {
        PageResult<FissionActivityExtendDto> pageResult = new PageResult<>();
        if (settlementAccount == null) {
            return pageResult;
        }
        PageHelper.startPage(settlementAccount.getPage(), settlementAccount.getLimit());
        List<FissionActivityExtendDto> list = getFissionDealerSettlementAccountList(settlementAccount.getFissionId());
        PageInfo<FissionActivityExtendDto> page = new PageInfo<>(list);
        pageResult.setCount(page.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
        pageResult.setMsg("");
        pageResult.setData(list);
        return pageResult;
    }

    /**
     * ????????????????????????????????????
     *
     * @param fissionId
     * @param dealerId
     * @return java.util.List<com.tuanche.directselling.dto.FissionActivityExtendDto>
     * @author HuangHao
     * @CreatTime 2021-04-21 16:00
     */
    public List<FissionActivityExtendDto> getFissionDealerSettlementAccountList(Integer fissionId) {
        return fissionActivityExtendReadMapper.getFissionDealerSettlementAccountList(fissionId);
    }


}
