package com.tuanche.directselling.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.directselling.api.FissionUserRewardService;
import com.tuanche.directselling.api.FissionUserService;
import com.tuanche.directselling.dto.FissionActivityDto;
import com.tuanche.directselling.dto.FissionUserRewardDto;
import com.tuanche.directselling.enums.AwardRuleType;
import com.tuanche.directselling.mapper.read.directselling.FissionUserRewardReadMapper;
import com.tuanche.directselling.mapper.write.directselling.FissionUserRewardWriteMapper;
import com.tuanche.directselling.model.FissionAwardRule;
import com.tuanche.directselling.model.FissionTradeRecord;
import com.tuanche.directselling.model.FissionUser;
import com.tuanche.directselling.model.FissionUserReward;
import com.tuanche.directselling.pojo.UserRewardPayStatusVo;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.util.FuncUtil;
import com.tuanche.directselling.util.RedisLockDto;
import com.tuanche.directselling.util.RedisLockService;
import com.tuanche.directselling.utils.*;
import com.tuanche.directselling.vo.WeChatPaymentVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.weixin4j.WeixinException;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * @class: FissionUserRewardServiceImpl
 * @description: C端用户奖励相关服务
 * @author: dudg
 * @create: 2020-09-29 15:00
 */
@Service(retries = 0)
public class FissionUserRewardServiceImpl implements FissionUserRewardService {

    @Autowired
    FissionUserRewardReadMapper fissionUserRewardReadMapper;
    @Autowired
    FissionUserRewardWriteMapper fissionUserRewardWriteMapper;
    @Autowired
    FissionTradeRecordServiceImpl fissionTradeRecordServiceImpl;
    @Autowired
    RedisLockService redisLockService;
    @Autowired
    FissionUserService fissionUserService;
    @Autowired
    @Qualifier("ClusterRedisService")
    private RedisService redisService;

    @Value("${tuanche_activity_appid}")
    private String tuanche_activity_appid;

    private static String FISSION_USER_REWARD_LOCK = "fission:user_reward:lock:";

    /**
     * @description: C端用户奖励打款(24h以内的奖励数据)
     * @param
     * @return: boolean
     * @author: dudg
     * @date: 2020/9/29 17:40
    */
    @Override
    public boolean rewardPayToUser() {
        String lockKey = "REWARDPAYTOUSER:LOCK";
        String requestId = UUID.randomUUID().toString();
        if(FuncUtil.getDistributeLock(redisService,lockKey,requestId,30*60*1000)) {
            String logKey = "用户奖励打款标识：" + requestId;
            try {
                // 获取可打款奖励列表
                List<FissionUserReward> fissionUserRewards = fissionUserRewardReadMapper.selectUserPayableList();
                if (CollectionUtils.isEmpty(fissionUserRewards)) {
                    CommonLogUtil.addInfo(logKey,"没有可打款数据");
                    return true;
                }
                // fissionId 维度
                fissionUserRewards.stream().map(FissionUserReward::getFissionId).distinct().forEach(fissionId->{
                    // UnionId维度汇总
                    Map<String, BigDecimal> userRewardMaps = fissionUserRewards.stream().filter(m-> m.getFissionId().equals(fissionId)).collect(Collectors.groupingBy(FissionUserReward::getUserWxUnionId, CollectorsUtil.summingBigDecimal(FissionUserReward::getRewardAmount)));
                    // 打款成功用户
                    Map<String, FissionTradeRecord> paySuccessUsers = Maps.newLinkedHashMap();
                    FissionUser fissionUser = new FissionUser();
                    fissionUser.setFissionId(fissionId);
                    // 微信付款
                    for (String key : userRewardMaps.keySet()) {

                        String userMsg = MessageFormat.format("裂变活动：{0}，用户：{1} ",fissionId.toString(), key);
                        try {
                            // 奖励小于0.3 不打款
                            if(userRewardMaps.get(key).compareTo(BigDecimal.valueOf(0.3))<0){
                                CommonLogUtil.addInfo(logKey, userMsg + "金额不足0.3元 不能打款");
                                continue;
                            }
                            fissionUser.setUserWxUnionId(key);
                            FissionUser userInfo = fissionUserService.getFissionUser(fissionUser);

                            WeChatPaymentVo weChatPaymentVo = new WeChatPaymentVo();
                            weChatPaymentVo.setAppId(tuanche_activity_appid);
                            weChatPaymentVo.setOpenId(userInfo.getUserWxOpenId());
                            weChatPaymentVo.setAmount(Integer.parseInt(userRewardMaps.get(key).multiply(BigDecimal.valueOf(100)).setScale(0, BigDecimal.ROUND_DOWN).toString()));
                            weChatPaymentVo.setOriginAmount(userRewardMaps.get(key));
                            weChatPaymentVo.setDesc("团车裂变活动用户奖励付款");
                            weChatPaymentVo.setTradeType(GlobalConstants.FissionTradeType.USER_REWARD.getCode());
                            weChatPaymentVo.setOrder_no(String.valueOf(OrderNoGenerateUtil.nextId()));
                            ResultDto resultDto = fissionTradeRecordServiceImpl.wechatPayment(weChatPaymentVo);
                            if (resultDto.getCode().intValue() != StatusCodeEnum.SUCCESS.getCode()) {
                                CommonLogUtil.addInfo(logKey, userMsg + "打款失败", resultDto);
                            }
                            else{
                                paySuccessUsers.put(key, (FissionTradeRecord) resultDto.getResult());
                                CommonLogUtil.addInfo(lockKey, userMsg + "打款成功，金额：" + userRewardMaps.get(key).toString());
                            }
                        } catch (WeixinException we) {
                            CommonLogUtil.addError(logKey, userMsg + "打款error", we);
                        }
                    }

                    if(!CollectionUtil.isEmpty(paySuccessUsers)){
                        List<UserRewardPayStatusVo> rewardPayStatusVos = Lists.newArrayList();
                        // 打款状态同步db
                        for (String user : paySuccessUsers.keySet()) {
                            rewardPayStatusVos.add(new UserRewardPayStatusVo(paySuccessUsers.get(user).getId(),
                                    fissionUserRewards.stream().filter(k -> k.getUserWxUnionId().equalsIgnoreCase(user)).map(FissionUserReward::getId).collect(Collectors.toList())));
                        }

                        if (!CollectionUtil.isEmpty(rewardPayStatusVos)) {
                            try {
                                int i = fissionUserRewardWriteMapper.updateUserPayStatus(rewardPayStatusVos);
                                if (i <= 0) {
                                    fissionUserRewardWriteMapper.updateUserPayStatus(rewardPayStatusVos);
                                }
                            } catch (Exception e) {
                                CommonLogUtil.addError(logKey, "打款后 更新用户打款标识 error" + JSON.toJSONString(rewardPayStatusVos), e);
                            }
                        }
                    }
                });

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                CommonLogUtil.addError(logKey,"发生错误 error", e);
            }
            finally {
                FuncUtil.releaseDistributeLock(redisService, lockKey, requestId);
            }
        }

        return false;
    }

    /**
     * @description: C端用户奖励列表
     * @param pageResult
     * @param fissionUserReward
     * @return: com.tuanche.directselling.utils.PageResult
     * @author: dudg
     * @date: 2020/9/29 18:21
    */
    @Override
    public PageResult selectUserRewardListByFissionId(PageResult<FissionUserRewardDto> pageResult, FissionUserRewardDto userRewardDto){
        if(userRewardDto.isOpenPage()){
            PageHelper.startPage(pageResult.getPage(), pageResult.getLimit(), "fur.id desc");
        }
        List<FissionUserRewardDto> userRewardDtos = fissionUserRewardReadMapper.selectUserRewardListByFissionId(userRewardDto);
        PageInfo<FissionUserRewardDto> page = new PageInfo<FissionUserRewardDto>(userRewardDtos);
        pageResult.setCount(page.getTotal());
        pageResult.setCode("0");
        pageResult.setMsg("");
        pageResult.setData(userRewardDtos);
        return pageResult;
    }

    /**
     * @description: 获取当前活动C端奖励金总额
     * @param fissionUserReward
     * @return: java.math.BigDecimal
     * @author: dudg
     * @date: 2020/10/10 12:09
    */
    @Override
    public BigDecimal selectTotalAmountByFissionId(FissionUserReward fissionUserReward){
        return fissionUserRewardReadMapper.selectTotalAmountByFissionId(fissionUserReward);
    }

    /**
     * C端用户任务奖励计算
     * @author HuangHao
     * @CreatTime 2020-10-10 17:45
     * @param reward
     * @param activityDto
     * @return void
     */
    protected void userTaskReward(FissionUserReward reward,FissionActivityDto activityDto){
        Integer taskId = reward.getTaskId();
        String key = FISSION_USER_REWARD_LOCK+activityDto.getId();
        RedisLockDto redisLockDto = redisLockService.lock(key, 10*1000, 11*1000);
        //获取不了锁就放弃
        if(!redisLockDto.isLock()){
            CommonLogUtil.addInfo(null, "未获取到裂变任务的锁，裂变任务ID:"+activityDto.getId()+"，用户微信ID："+reward.getUserWxUnionId());
            return;
        }
        try {
            //裂变奖励规则
            List<FissionAwardRule> awardRuleList = activityDto.getAwardRuleList();
            //该任务奖励金额
            BigDecimal taskAward = null;
            //用户奖金池总金额
            BigDecimal prizePool = null;
            //用户该任务奖励上限
            BigDecimal awardRule = null;
            boolean tag = true;
            for(FissionAwardRule rule:awardRuleList){
                if(AwardRuleType.C.getType() == (int)rule.getRuleType()){
                    //取第一个为用户奖金池金额
                    if (tag){
                        prizePool = rule.getPrizePool();
                        tag = false;
                    }
                    //获取任务对应的奖励金额及奖励上限
                    if(String.valueOf(taskId).equals(rule.getTaskCode())){
                        taskAward = rule.getAward();
                        awardRule = rule.getAwardRule();
                        break;
                    }
                }
            }
            BigDecimal zero = new BigDecimal("0");
            //如果该任务奖励不等于空且大于0
            if(taskAward != null && taskAward.compareTo(zero) > 0){
                //获取裂变用户某一任务奖励及裂变任务已分发的总奖金额
                FissionUserRewardDto rewardDto =fissionUserRewardWriteMapper.getRewardAmountAndTotalRewarByFissionId(reward);
                //裂变任务已分发的总奖金额
                BigDecimal totalRewar = null;
                //用户该任务获取的奖励总金额
                BigDecimal rewardAmount = null;
                if(rewardDto==null){
                    rewardAmount = zero;
                    totalRewar = rewardAmount;
                }else{
                    rewardAmount = rewardDto.getRewardAmount()==null?zero:rewardDto.getRewardAmount();
                    totalRewar = rewardDto.getTotalAmount();
                }
                CommonLogUtil.addInfo(null, "裂变任务C端计算奖励裂变ID："+reward.getFissionId()+"用户："+reward.getUserWxUnionId()+"，任务："+taskId+"，该任务奖励金额："+taskAward+"，奖金池总金额："+prizePool+"，该任务用户已获得奖励金额："+awardRule+"，裂变已发放奖励总额："+totalRewar);
                if(taskAward != null  && prizePool != null && awardRule != null){
                    //【如果奖金池金额 >  已分发的总奖金额】 而且 【已分发的总奖金额+本次任务奖励 <= 总奖金池金额】 且 【奖励未超过上限】 才计算奖励
                    if(prizePool.compareTo(totalRewar) == 1 &&
                            (taskAward.add(totalRewar)).compareTo(prizePool) <= 0 &&
                            rewardAmount.compareTo(awardRule) ==-1){
                        reward.setRewardAmount(taskAward);
                        fissionUserRewardWriteMapper.insertSelective(reward);
                    }
                }
            }
        }finally {
            redisLockService.unLock(key, redisLockDto);
        }
    }



}
