package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.exception.RedisException;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.directselling.api.FissionActivityService;
import com.tuanche.directselling.api.FissionSaleService;
import com.tuanche.directselling.api.FissionUserEffectiveTaskService;
import com.tuanche.directselling.api.FissionUserService;
import com.tuanche.directselling.api.FissionUserTaskRecordService;
import com.tuanche.directselling.constant.BaseCacheKeys;
import com.tuanche.directselling.dto.FissionActivityDto;
import com.tuanche.directselling.dto.FissionSubscribeOrLiveCountDto;
import com.tuanche.directselling.dto.FissionUserTaskRecordDetailDto;
import com.tuanche.directselling.enums.FissionChannel;
import com.tuanche.directselling.enums.FissionTaskRecordStatus;
import com.tuanche.directselling.enums.FissionTaskType;
import com.tuanche.directselling.mapper.read.directselling.FissionSaleReadMapper;
import com.tuanche.directselling.mapper.sharding.FissionUserTaskRecordMapper;
import com.tuanche.directselling.model.FissionAwardRule;
import com.tuanche.directselling.model.FissionSale;
import com.tuanche.directselling.model.FissionUser;
import com.tuanche.directselling.model.FissionUserEffectiveTask;
import com.tuanche.directselling.model.FissionUserReward;
import com.tuanche.directselling.model.FissionUserTaskRecord;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.util.ConstantsUtil;
import com.tuanche.directselling.utils.GlobalConstants;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.FissionUserTaskRecordDetailVo;
import com.tuanche.directselling.vo.FissionUserTaskRecordSaleVo;
import com.tuanche.directselling.vo.FissionUserTaskRecordVo;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.model.CsCompany;
import org.apache.shardingsphere.api.hint.HintManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author???HuangHao
 * @CreatTime 2020-09-23 14:33
 */
@Service(retries = 0)
public class FissionUserTaskRecordServiceImpl implements FissionUserTaskRecordService {


    private static final String KEW_WORD = "??????????????????";
    /**
     * 1??????
     */
    private final long ONE_MINUTE = 60000;
    /**
     * 1??????
     */
    private final long ONE_HOUR = ONE_MINUTE * 60;
    /**
     * 1???
     */
    private final long ONE_DAY = ONE_HOUR * 24;
    /**
     * 2???
     */
    private final long TWO_DAYS = ONE_DAY * 2;
    /**
     * 3???
     */
    private final long THREE_DAYS = ONE_DAY * 3;
    private final SimpleDateFormat COMPLETE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final SimpleDateFormat SHORT_DATE_FORMAT = new SimpleDateFormat("MM???dd??? HH:mm");
    @Autowired
    FissionSaleService fissionSaleService;
    @Autowired
    FissionUserService fissionUserService;
    @Reference
    FissionActivityService fissionActivityService;
    @Autowired
    FissionUserEffectiveTaskService fissionUserEffectiveTaskService;
    @Autowired
    FissionUserRewardServiceImpl fissionUserRewardServiceImpl;
    @Autowired
    FissionSaleServiceImpl fissionSaleServiceImpl;
    @Autowired
    FissionUserTaskRecordMapper fissionUserTaskRecordMapper;
    @Autowired
    @Qualifier("ClusterRedisService")
    private RedisService redisService;
    @Reference
    CompanyBaseService companyBaseService;
    @Autowired
    private FissionSaleReadMapper fissionSaleReadMapper;

    /**
     * ????????????????????????
     * @author HuangHao
     * @CreatTime 2020-09-23 17:23
     * @param task
     * @return com.tuanche.arch.web.model.TcResponse
     */
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    @Override
    public TcResponse completeTask(FissionUserTaskRecordVo task){

        CommonLogUtil.addInfo(null, KEW_WORD+"?????????????????????????????????"+ JSON.toJSONString(task));
        if(task == null || StringUtils.isEmpty(task.getUserWxUnionId())){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"????????????UnionId????????????");
        }
        if(!ConstantsUtil.isWxUnionId(task.getUserWxUnionId())){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"?????????????????????UnionId");
        }
        if(task.getFissionId() == null){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"????????????ID????????????");
        }
        if(task.getTaskId() == null){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"??????ID????????????");
        }
        if(FissionTaskType.TASK_1.getCode().equals(task.getTaskId()) && task.getSource() == null){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"????????????????????????");
        }
        if(FissionTaskType.TASK_1.getCode().equals(task.getTaskId()) && StringUtils.isEmpty(task.getIp())){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"??????IP????????????");
        }
        if(FissionTaskType.TASK_1.getCode().equals(task.getTaskId()) && (task.getCityId() == null || task.getCityId() < 1)){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"??????????????????");
        }
        FissionActivityDto activity = fissionActivityService.getFissionActivityDtoById(task.getFissionId());
        Date now = new Date();
        //???????????????????????????????????????????????????????????????
        if(activity == null || activity.getEndTime() == null || now.after(activity.getEndTime())){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"??????????????????");
        }
        Integer fissionId = task.getFissionId();
        //???????????????????????????????????????????????????
        if(task.getChannel() == null || task.getChannel() < 2){
            //????????????????????????(?????????)??????
            FissionUserTaskRecord firstUnnaturalChannel = getFirstUnnaturalChannel(task);
            CommonLogUtil.addInfo(null, "??????????????????,????????????:"+fissionId+",??????:"+task.getTaskId()+",??????:"+task.getUserWxUnionId()+",??????????????????????????????(?????????)??????:"+(firstUnnaturalChannel==null));
            //??????????????????(?????????)?????????????????????????????????????????????
            if(firstUnnaturalChannel != null && firstUnnaturalChannel.getChannel() != null){
                task.setChannel(firstUnnaturalChannel.getChannel());
                //???????????????????????????????????????????????????
                if(FissionChannel.CHANNEL_2.getChannel().equals(firstUnnaturalChannel.getChannel())){
                    task.setSaleWxUnionId(firstUnnaturalChannel.getSaleWxUnionId());
                }
            }
        //?????????????????????????????????????????????
        }else if(FissionChannel.CHANNEL_2.getChannel().equals(task.getChannel()) || FissionChannel.CHANNEL_3.getChannel().equals(task.getChannel())){
            FissionUserTaskRecord taskRecord =new FissionUserTaskRecord();
            BeanUtils.copyProperties(task, taskRecord);
            setFirstUnnaturalChannelCache(taskRecord, null);
        }


        String saleWxUnionId = task.getSaleWxUnionId();
        String userWxUnionId = task.getUserWxUnionId();
        String shareWxUnionId = task.getShareWxUnionId();
        StringBuilder sb = new StringBuilder(128);
        sb.append(KEW_WORD);
        sb.append(",????????????:");sb.append(task.getFissionId());
        sb.append(",??????:");sb.append(task.getTaskId());
        sb.append(",??????:");sb.append(saleWxUnionId);
        sb.append(",?????????:");sb.append(shareWxUnionId);
        sb.append(",??????:");sb.append(userWxUnionId);
        String kw = sb.toString();
        CommonLogUtil.addInfo(null, kw+"????????????");
        //???????????????UnionId
        boolean hasSale = ConstantsUtil.isWxUnionId(task.getSaleWxUnionId());
        if(!hasSale){
            task.setSaleWxUnionId(null);
        }else{
            //???????????????????????????
            if(!fissionSaleServiceImpl.hasSale(task.getFissionId(), saleWxUnionId)){
                CommonLogUtil.addInfo(null, kw+"?????????????????????????????????");
                return new TcResponse(StatusCodeEnum.USER_NOT_EXIST.getCode(),"???????????????");
            }
        }

        //????????????????????????
        boolean shareIsSale= false;
        //??????????????????UnionId
        boolean hasShareUser = ConstantsUtil.isWxUnionId(task.getShareWxUnionId());
        if(!hasShareUser){
            task.setShareWxUnionId(null);
        }else{
            //????????????????????????
            shareIsSale=fissionSaleServiceImpl.hasSale(task.getFissionId(), shareWxUnionId);
        }
        task.setId(null);
        //??????????????????
        int userExist = addFissionUser(task);
        //??????????????????
        FissionTaskRecordStatus taskRecordStatus = effectiveTask(task, activity, hasSale, hasShareUser, shareIsSale, kw,userExist);
        task.setOngoingData(taskRecordStatus.getStatus());
        //??????????????????
        boolean isEffectiveTask = taskRecordStatus.getStatus().equals(FissionTaskRecordStatus.STATUS_2.getStatus())?true:false;
        //????????????????????????
        fissionUserTaskRecordMapper.insertFissionUserTaskRecord(task);
        //??????????????????????????????????????????
        cacheActivityBeginTask(task);
        //????????????ID
        Long taskRecordId = task.getId();
        //?????????????????????????????????
        reward(task, activity, hasSale, hasShareUser, shareIsSale, kw, taskRecordId,isEffectiveTask);
        CommonLogUtil.addInfo(null, kw+"???????????????????????????");
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(),"??????");
    }

    @Override
    public PageResult selectFissionUserTaskRecordDetail(int page, int limit, Integer fissionId, Integer taskId, Integer saleId) {
        PageResult<FissionUserTaskRecordSaleVo> pageResult = new PageResult<>();
        FissionSale fissionSaleParam = new FissionSale();
        fissionSaleParam.setFissionId(fissionId);
        fissionSaleParam.setSaleId(saleId);
        FissionSale fissionSale = fissionSaleReadMapper.getFissionSale(fissionSaleParam);
        PageHelper.startPage(page, limit);
        List<FissionUserTaskRecordDetailDto> fissionActivitySaleDtoList = fissionUserTaskRecordMapper.selectFissionUserTaskRecordDetailDto(fissionId, fissionSale == null ? "" : fissionSale.getSaleWxUnionId(), taskId);
        List<FissionUserTaskRecordSaleVo> fissionUserTaskRecordSaleDetailVos = new ArrayList<>();
        FissionUserTaskRecordSaleVo fissionUserTaskRecordSaleDetailVo = null;
        //????????????
        List<FissionAwardRule> fissionAwardRuleList = fissionActivityService.getAwardRuleListByFissionId(fissionId, GlobalConstants.FISSION_AWARD_RULE_TYPE_B);
        FissionAwardRule fissionAwardRule = null;
        for (FissionAwardRule far : fissionAwardRuleList) {
            if (far.getTaskCode().equals(String.valueOf(taskId))) {
                fissionAwardRule = far;
                break;
            }
        }
        List<String> userWxUnionIds = new ArrayList<>();
        for (FissionUserTaskRecordDetailDto fissionActivitySaleDto : fissionActivitySaleDtoList) {
            userWxUnionIds.add(fissionActivitySaleDto.getUserWxUnionId());
        }
        //????????????????????????ids
        List<String> liveUserWxUnionIds = new ArrayList<>();
        List<String> subscribeUserWxUnionIds = new ArrayList<>();
        //????????????
        if (FissionTaskType.TASK_2.getCode().equals(taskId)) {
            //???????????????
            if (!userWxUnionIds.isEmpty()) {
                liveUserWxUnionIds = fissionUserTaskRecordMapper.selectSubscribeOrLiveUserWxUnionIds(fissionId, FissionTaskType.TASK_4.getCode(), userWxUnionIds);
            }
        }

        if (FissionTaskType.TASK_4.getCode().equals(taskId)) {
            //???????????????
            if (!userWxUnionIds.isEmpty()) {
                subscribeUserWxUnionIds = fissionUserTaskRecordMapper.selectSubscribeOrLiveUserWxUnionIds(fissionId, FissionTaskType.TASK_2.getCode(), userWxUnionIds);
            }
        }
        long now = System.currentTimeMillis();
        BigDecimal integral = fissionAwardRule == null ? BigDecimal.ZERO : fissionAwardRule.getAward();
        for (FissionUserTaskRecordDetailDto fissionActivitySaleDto : fissionActivitySaleDtoList) {
            fissionUserTaskRecordSaleDetailVo = new FissionUserTaskRecordSaleVo();
            BeanUtils.copyProperties(fissionActivitySaleDto, fissionUserTaskRecordSaleDetailVo);
            fissionUserTaskRecordSaleDetailVo.setId(String.valueOf(fissionActivitySaleDto.getId()));
            long time = fissionActivitySaleDto.getCreateDt().getTime();
            long diffTime = now - time;
            String createDt = COMPLETE_DATE_FORMAT.format(fissionActivitySaleDto.getCreateDt());
            String timeHHmm = createDt.substring(11, 16);
            //1?????????????????????XX?????????
            if (diffTime < ONE_MINUTE) {
                fissionUserTaskRecordSaleDetailVo.setDateDesc(diffTime / 1000 + "??????");
            } else if (diffTime >= ONE_MINUTE && diffTime < ONE_HOUR) {
                //??????1????????????1??????????????????XX????????????
                fissionUserTaskRecordSaleDetailVo.setDateDesc(diffTime / ONE_MINUTE + "?????????");
            } else if (diffTime >= ONE_HOUR && diffTime < THREE_DAYS) {
                //??????1????????????3?????????????????????????????????
                if (diffTime <= ONE_DAY) {
                    fissionUserTaskRecordSaleDetailVo.setDateDesc("?????? " + timeHHmm);
                } else if (diffTime <= TWO_DAYS) {
                    fissionUserTaskRecordSaleDetailVo.setDateDesc("?????? " + timeHHmm);
                } else {
                    fissionUserTaskRecordSaleDetailVo.setDateDesc("?????? " + timeHHmm);
                }
            } else {
                fissionUserTaskRecordSaleDetailVo.setDateDesc(SHORT_DATE_FORMAT.format(fissionActivitySaleDto.getCreateDt()));
            }
            //??????????????????
            if (fissionActivitySaleDto.getOngoingData() == 2 && fissionActivitySaleDto.getChannel() == 2) {
                fissionUserTaskRecordSaleDetailVo.setIntegral(integral);
            } else {
                fissionUserTaskRecordSaleDetailVo.setIntegral(BigDecimal.ZERO);
            }

            if (!liveUserWxUnionIds.isEmpty()) {
                if (liveUserWxUnionIds.contains(fissionActivitySaleDto.getUserWxUnionId())) {
                    fissionUserTaskRecordSaleDetailVo.setLive(true);
                } else {
                    fissionUserTaskRecordSaleDetailVo.setLive(false);
                }

            }
            if (!subscribeUserWxUnionIds.isEmpty()) {
                if (subscribeUserWxUnionIds.contains(fissionActivitySaleDto.getUserWxUnionId())) {
                    fissionUserTaskRecordSaleDetailVo.setSubscribe(true);
                } else {
                    fissionUserTaskRecordSaleDetailVo.setSubscribe(false);
                }
            }
            fissionUserTaskRecordSaleDetailVos.add(fissionUserTaskRecordSaleDetailVo);
        }
        PageInfo<FissionUserTaskRecordSaleVo> pageInfo = new PageInfo(fissionActivitySaleDtoList);
        pageResult.setCount(pageInfo.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode());
        pageResult.setMsg(StatusCodeEnum.SUCCESS.getText());
        pageResult.setData(fissionUserTaskRecordSaleDetailVos);
        return pageResult;
    }

    @Override
    public PageResult selectFissionUserTaskRecordDetailVoWithSaleName(int page, int limit, Integer fissionId, Integer taskId, String saleName, String dealerName, Integer isEffective) {
        PageResult<FissionUserTaskRecordDetailVo> pageResult = new PageResult<>();
        PageHelper.startPage(page, limit);
        List<Integer> csDealerIds = new ArrayList<>();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(dealerName)) {
            csDealerIds = companyBaseService.getCsCompanyIdByName(dealerName);
        }
        List<FissionUserTaskRecordDetailDto> fissionActivitySaleDtoList = fissionUserTaskRecordMapper.selectFissionUserTaskRecordDetailDtoWithSaleName(fissionId, taskId, saleName, csDealerIds, isEffective);
        List<FissionUserTaskRecordDetailVo> fissionUserTaskRecordDetailVos = new ArrayList<>();
        FissionUserTaskRecordDetailVo fissionUserTaskRecordDetailVo = null;
        //????????????
        List<FissionAwardRule> fissionAwardRuleList = fissionActivityService.getAwardRuleListByFissionId(fissionId, GlobalConstants.FISSION_AWARD_RULE_TYPE_B);
        FissionAwardRule fissionAwardRule = null;
        for (FissionAwardRule far : fissionAwardRuleList) {
            if (far.getTaskCode().equals(String.valueOf(taskId))) {
                fissionAwardRule = far;
                break;
            }
        }
        //???????????????
        Set<Integer> dealerIds = new HashSet<>();
        for (FissionUserTaskRecordDetailDto fissionActivitySaleDto : fissionActivitySaleDtoList) {
            dealerIds.add(fissionActivitySaleDto.getDealerId());
        }
        Map<Integer, CsCompany> companyMap= companyBaseService.getCompanyMap(new ArrayList<>(dealerIds));
        BigDecimal integral = fissionAwardRule == null ? BigDecimal.ZERO : fissionAwardRule.getAward();
        for (FissionUserTaskRecordDetailDto fissionActivitySaleDto : fissionActivitySaleDtoList) {
            fissionUserTaskRecordDetailVo = new FissionUserTaskRecordDetailVo();
            BeanUtils.copyProperties(fissionActivitySaleDto, fissionUserTaskRecordDetailVo);
            CsCompany csCompany = companyMap.get(fissionActivitySaleDto.getDealerId());
            if (csCompany != null) {
                fissionUserTaskRecordDetailVo.setDealerName(csCompany.getCompanyName());
            }
            //??????????????????
            if (fissionActivitySaleDto.getOngoingData() == 2 && fissionActivitySaleDto.getChannel() == 2) {
                fissionUserTaskRecordDetailVo.setIntegral(integral);
            } else {
                fissionUserTaskRecordDetailVo.setIntegral(BigDecimal.ZERO);
            }
            fissionUserTaskRecordDetailVos.add(fissionUserTaskRecordDetailVo);
        }
        PageInfo<FissionUserTaskRecordDetailVo> pageInfo = new PageInfo(fissionActivitySaleDtoList);
        pageResult.setCount(pageInfo.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode());
        pageResult.setMsg(StatusCodeEnum.SUCCESS.getText());
        pageResult.setData(fissionUserTaskRecordDetailVos);
        return pageResult;
    }

    @Override
    public FissionSubscribeOrLiveCountDto selectFissionSubscribeOrLiveCount(Integer fissionId) {
        //??????????????????
        Integer subscribeCount = fissionUserTaskRecordMapper.selectSubscribeOrLiveUserCount(fissionId, FissionTaskType.TASK_2.getCode());
        //?????????????????????????????????
        Integer subscribeLiveCount = fissionUserTaskRecordMapper.selectSubscribeLiveUserCount(fissionId);
        //??????????????????
        Integer liveCount = fissionUserTaskRecordMapper.selectSubscribeOrLiveUserCount(fissionId, FissionTaskType.TASK_4.getCode());
        FissionSubscribeOrLiveCountDto fissionSubscribeOrLiveCountDto = new FissionSubscribeOrLiveCountDto();
        fissionSubscribeOrLiveCountDto.setSubscribeCount(subscribeCount);
        fissionSubscribeOrLiveCountDto.setSubscribeLiveCount(subscribeLiveCount);
        fissionSubscribeOrLiveCountDto.setLiveCount(liveCount);
        return fissionSubscribeOrLiveCountDto;

    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param activity
     * @return boolean
     * @author HuangHao
     * @CreatTime 2021-02-19 17:48
     */
    public boolean ongoing(FissionActivityDto activity) {
        Date now = new Date();
        return now.after(activity.getStartTime()) &&
                GlobalConstants.FISSION_ON_STATUS_1.equals(activity.getOnState());
    }

    /**
     * ??????????????????
     * @author HuangHao
     * @CreatTime 2020-12-29 11:33
     */
    public int addFissionUser(FissionUserTaskRecordVo task){
        FissionUser fissionUser = new FissionUser();
        fissionUser.setUserWxOpenId(task.getUserWxOpenId());
        fissionUser.setUserWxUnionId(task.getUserWxUnionId());
        fissionUser.setNickName(task.getNickName());
        fissionUser.setFissionId(task.getFissionId());
        fissionUser.setCityId(task.getCityId());
        fissionUser.setUserWxHead(task.getUserWxHead());
        return fissionUserService.addFissionUser(fissionUser);
    }

    /**
     * ????????????????????????
     * @author HuangHao
     * @CreatTime 2021-02-19 15:37
     * @param task
     * @param activity
     * @param hasSale
     * @param hasShareUser
     * @param shareIsSale
     * @param kw
     * @param userExist
     * @return boolean
     */
    public FissionTaskRecordStatus effectiveTask(FissionUserTaskRecordVo task,FissionActivityDto activity,boolean hasSale,boolean hasShareUser,boolean shareIsSale,String kw,int userExist){
        boolean isStarted = ongoing(activity);
        //???????????????
        if(!isStarted){
            CommonLogUtil.addInfo(null, kw+"????????????????????????????????????????????????"+activity.getStartTime()+"??????????????????"+activity.getOnState());
            return FissionTaskRecordStatus.STATUS_0;
        }
        //????????????????????????????????????????????????
        if(task.getUserWxUnionId().equals(task.getSaleWxUnionId())){
            CommonLogUtil.addInfo(null, kw+"???????????????????????????????????????????????????");
            return FissionTaskRecordStatus.STATUS_1;
        }
        //???????????????C???????????????????????????
        boolean userIsSale=fissionSaleServiceImpl.hasSale(task.getFissionId(), task.getUserWxUnionId());
        if(userIsSale){
            CommonLogUtil.addInfo(null, kw+"?????????????????????C????????????????????????????????????");
            return FissionTaskRecordStatus.STATUS_1;
        }
        //??????????????????
        boolean isEffectiveTask = false;
        //??????1???????????????
        if(FissionTaskType.TASK_1.getCode().equals(task.getTaskId())){
            boolean exist = hasActivityBeginTask(task);
            if(!exist){
                isEffectiveTask = true;
            }
            //?????????
            fissionActivityService.getOrIncrEffectNum(task.getFissionId(),  GlobalConstants.FissionEffectTypeEnum.BROWSE_NUM, false);
        //??????2???????????????
        }else if(FissionTaskType.TASK_2.getCode().equals(task.getTaskId()) ){
            boolean exist = hasActivityBeginTask(task);
            if(!exist){
                isEffectiveTask = true;
            }
            //?????????
            fissionActivityService.getOrIncrEffectNum(task.getFissionId(), GlobalConstants.FissionEffectTypeEnum.SUBSCRIBE_NUM, false);
        //??????3????????????????????????
        }else if(FissionTaskType.TASK_3.getCode().equals(task.getTaskId())){
            boolean exist = hasActivityBeginTask(task);
            if(!exist){
                isEffectiveTask = true;
            }
        //??????4???????????????
        }else if(FissionTaskType.TASK_4.getCode().equals(task.getTaskId()) ){
            boolean exist = hasActivityBeginTask(task);
            if(!exist){
                isEffectiveTask = true;
            }
        //??????5?????????????????????
        }else if(FissionTaskType.TASK_5.getCode().equals(task.getTaskId())){
            boolean exist = hasActivityBeginTask(task);
            if(!exist){
                isEffectiveTask = true;
            }
        }
        //????????????
        return isEffectiveTask?FissionTaskRecordStatus.STATUS_2:FissionTaskRecordStatus.STATUS_1;
    }
    /**
     * ?????? C???????????????
     * @author HuangHao
     * @CreatTime 2020-12-10 10:17
     * @param task
     * @param activity
     * @param hasSale
     * @param shareIsSale
     * @param hasShareUser
     * @param kw
     * @param taskRecordId
     * @return void
     */
    public void reward(FissionUserTaskRecordVo task, FissionActivityDto activity, boolean hasSale, boolean hasShareUser, boolean shareIsSale, String kw, Long taskRecordId, boolean isEffectiveTask) {
        FissionUserEffectiveTask effectiveTask = new FissionUserEffectiveTask();
        effectiveTask.setUserWxUnionId(task.getUserWxUnionId());
        effectiveTask.setSaleWxUnionId(task.getSaleWxUnionId());
        effectiveTask.setShareWxUnionId(task.getShareWxUnionId());
        effectiveTask.setFissionId(task.getFissionId());
        effectiveTask.setTaskId(task.getTaskId());
        effectiveTask.setTaskRecordId(taskRecordId);
        effectiveTask.setSharerIsSale(shareIsSale);
        boolean hasReward = activity.getcAwardFlag() == 1;
        CommonLogUtil.addInfo(null, kw+"????????????C??????????????????????????????????????????????????????????????????"+isEffectiveTask+"????????????????????????"+hasShareUser+"??????????????????????????????"+shareIsSale+"???????????????????????????"+hasReward);
        //????????????
        if(isEffectiveTask){
            //?????????UnionId??????????????????????????????????????????
            if(hasSale){
                fissionUserEffectiveTaskService.insertFissionUserEffectiveTask(effectiveTask);
            }
            //??????????????????????????????????????????????????????C??????????????????????????????????????????????????????????????????????????????????????????C?????????????????????
            if(hasShareUser && !shareIsSale && hasReward && !task.getShareWxUnionId().equals(task.getUserWxUnionId())){
                userTaskReward(shareIsSale,task, activity, taskRecordId);
            }
        }
    }
    /**
     * ??????C???????????????
     * @author HuangHao
     * @CreatTime 2020-10-13 14:14
     * @param task
     * @param activity
     * @param taskRecordId
     * @return void
     */
    private void userTaskReward(boolean shareIsSale,FissionUserTaskRecordVo task,FissionActivityDto activity,Long taskRecordId){
        FissionUserReward reward = new FissionUserReward();
        reward.setFissionId(task.getFissionId());
        reward.setUserWxUnionId(task.getShareWxUnionId());
        reward.setTaskId(task.getTaskId());
        reward.setTaskRecordId(taskRecordId);
        fissionUserRewardServiceImpl.userTaskReward(reward, activity);
    }


    /**
     * @description: ???????????????
     * @return: void
     * @author: dudg
     * @date: 2020/11/30 18:01
    */
    private void handleShareCount(FissionUserTaskRecordVo task){
        FissionUserEffectiveTask effectiveTask = new FissionUserEffectiveTask();
        effectiveTask.setFissionId(task.getFissionId());
        effectiveTask.setShareWxUnionId(task.getShareWxUnionId());
        Boolean hasOpen = fissionUserEffectiveTaskService.shareAcitvityOpened(effectiveTask);
        if(!hasOpen){
            //?????????
            fissionActivityService.getOrIncrEffectNum(task.getFissionId(),  GlobalConstants.FissionEffectTypeEnum.SHARE_NUM, false);
        }
    }

    /**
     * ????????????????????????????????????
     * @author HuangHao
     * @CreatTime 2021-04-08 16:38
     * @param FissionUserTaskRecord
     * @return com.tuanche.directselling.model.FissionUserTaskRecord
     */
    FissionUserTaskRecord getFirstUnnaturalChannel(FissionUserTaskRecord taskRecord){
        boolean hasFirstUnnaturalChannel = false;
        String key = getFirstUnnaturalChannelCacheKey(taskRecord);
        try {
            FissionUserTaskRecord fissionUserTaskRecord = redisService.get(key, FissionUserTaskRecord.class);
            if(fissionUserTaskRecord == null){
                HintManager hintManager = null;
                try {
                    hintManager = HintManager.getInstance() ;
                    hintManager.setMasterRouteOnly();
                }catch (Exception e){

                }
                fissionUserTaskRecord = fissionUserTaskRecordMapper.getFirstUnnaturalChannel(taskRecord);
                if(hintManager != null)hintManager.close();
                Integer exptime = BaseCacheKeys.FISSION_FISSIONID_USER_FIRST_UNNATURAL_CHANNEL.getExpire();
                if(fissionUserTaskRecord == null){
                    fissionUserTaskRecord = new FissionUserTaskRecord();
                    exptime=3600000;//????????????1?????????
                }
                setFirstUnnaturalChannelCache(fissionUserTaskRecord, exptime);
                return fissionUserTaskRecord;
            }else{
                return fissionUserTaskRecord;
            }

        } catch (RedisException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void setFirstUnnaturalChannelCache(FissionUserTaskRecord taskRecord,Integer exptime){
        try {
            String key = getFirstUnnaturalChannelCacheKey(taskRecord);
            FissionUserTaskRecord fissionUserTaskRecord = redisService.get(key, FissionUserTaskRecord.class);
            //?????????????????????????????????
            if(fissionUserTaskRecord == null || (fissionUserTaskRecord != null &&  fissionUserTaskRecord.getChannel() == null)){
                if(exptime == null){
                    exptime= BaseCacheKeys.FISSION_FISSIONID_USER_FIRST_UNNATURAL_CHANNEL.getExpire();
                }
                redisService.set(key, taskRecord,exptime);
            }
        } catch (RedisException e) {
            e.printStackTrace();
        }
    }

    public String getFirstUnnaturalChannelCacheKey(FissionUserTaskRecord taskRecord){
        return MessageFormat.format(BaseCacheKeys.FISSION_FISSIONID_USER_FIRST_UNNATURAL_CHANNEL.getKey(), taskRecord.getFissionId(), taskRecord.getUserWxUnionId());
    }

    /**
     * ????????????????????????????????????????????????
     * @author HuangHao
     * @CreatTime 2021-02-20 17:34
     * @param task
     * @return int
     */
    public boolean hasActivityBeginTask(FissionUserTaskRecordVo task){
        Integer fissionId=task.getFissionId(),taskId=task.getTaskId();
        String userWxUnionId=task.getUserWxUnionId();
        String key = cacheUserRewardTaskKey(task);
        try {
            String t = redisService.get(key,String.class);
            CommonLogUtil.addInfo(null, "getUserWxUnionId???"+task.getUserWxUnionId()+"???ConstantsUtil.TRUE.equals(t):"+ConstantsUtil.TRUE.equals(t));
            if(!ConstantsUtil.TRUE.equals(t)){
                FissionUserTaskRecord taskRecord = new FissionUserTaskRecord();
                taskRecord.setFissionId(fissionId);
                taskRecord.setTaskId(taskId);
                taskRecord.setUserWxUnionId(userWxUnionId);
                HintManager hintManager = null;
                try {
                    hintManager = HintManager.getInstance() ;
                    hintManager.setMasterRouteOnly();
                }catch (Exception e){}
                Integer has = fissionUserTaskRecordMapper.hasActivityBeginTask(taskRecord);
                if(hintManager != null)hintManager.close();
                CommonLogUtil.addInfo(null, "getUserWxUnionId???"+task.getUserWxUnionId()+"???has:"+has);
                if(has != null){
                    return true;
                }
            }else{
                return true;
            }
        } catch (RedisException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * ??????????????????????????????????????????
     * @author HuangHao
     * @CreatTime 2021-02-20 17:32
     * @param task
     * @return void
     */
    private void cacheActivityBeginTask(FissionUserTaskRecordVo task){
        try {
            if(task.getOngoingData() > 0) {
                redisService.set(cacheUserRewardTaskKey(task), ConstantsUtil.TRUE, BaseCacheKeys.FISSION_FISSIONID_USER_TASK_ISCOMPLETE.getExpire());
            }
        } catch (RedisException e) {
            e.printStackTrace();
        }
    }
    /**
     * ?????????KEY
     * @author HuangHao
     * @CreatTime 2021-02-20 17:33
     * @param task
     * @return java.lang.String
     */
    private String cacheUserRewardTaskKey(FissionUserTaskRecordVo task){
        Integer fissionId=task.getFissionId(),taskId=task.getTaskId();
        String userWxUnionId=task.getUserWxUnionId();
        return MessageFormat.format(BaseCacheKeys.FISSION_FISSIONID_USER_TASK_ISCOMPLETE.getKey(), fissionId,userWxUnionId,taskId);
    }
}

