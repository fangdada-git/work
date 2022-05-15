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
 * @author：HuangHao
 * @CreatTime 2020-09-23 14:33
 */
@Service(retries = 0)
public class FissionUserTaskRecordServiceImpl implements FissionUserTaskRecordService {


    private static final String KEW_WORD = "裂变活动服务";
    /**
     * 1分钟
     */
    private final long ONE_MINUTE = 60000;
    /**
     * 1小时
     */
    private final long ONE_HOUR = ONE_MINUTE * 60;
    /**
     * 1天
     */
    private final long ONE_DAY = ONE_HOUR * 24;
    /**
     * 2天
     */
    private final long TWO_DAYS = ONE_DAY * 2;
    /**
     * 3天
     */
    private final long THREE_DAYS = ONE_DAY * 3;
    private final SimpleDateFormat COMPLETE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final SimpleDateFormat SHORT_DATE_FORMAT = new SimpleDateFormat("MM月dd日 HH:mm");
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
     * 用户完成任务方法
     * @author HuangHao
     * @CreatTime 2020-09-23 17:23
     * @param task
     * @return com.tuanche.arch.web.model.TcResponse
     */
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    @Override
    public TcResponse completeTask(FissionUserTaskRecordVo task){

        CommonLogUtil.addInfo(null, KEW_WORD+"接收到请求，请求参数："+ JSON.toJSONString(task));
        if(task == null || StringUtils.isEmpty(task.getUserWxUnionId())){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"用户微信UnionId不能为空");
        }
        if(!ConstantsUtil.isWxUnionId(task.getUserWxUnionId())){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"非法的用户微信UnionId");
        }
        if(task.getFissionId() == null){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"裂变活动ID不能为空");
        }
        if(task.getTaskId() == null){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"任务ID不能为空");
        }
        if(FissionTaskType.TASK_1.getCode().equals(task.getTaskId()) && task.getSource() == null){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"页面来源不能为空");
        }
        if(FissionTaskType.TASK_1.getCode().equals(task.getTaskId()) && StringUtils.isEmpty(task.getIp())){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"用户IP不能为空");
        }
        if(FissionTaskType.TASK_1.getCode().equals(task.getTaskId()) && (task.getCityId() == null || task.getCityId() < 1)){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"城市不能为空");
        }
        FissionActivityDto activity = fissionActivityService.getFissionActivityDtoById(task.getFissionId());
        Date now = new Date();
        //如果未获取到该裂变活动，或该裂变活动已结束
        if(activity == null || activity.getEndTime() == null || now.after(activity.getEndTime())){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"该活动已结束");
        }
        Integer fissionId = task.getFissionId();
        //如果渠道为空则是自然渠道（裸连接）
        if(task.getChannel() == null || task.getChannel() < 2){
            //获取第一个非自然(裸连接)渠道
            FissionUserTaskRecord firstUnnaturalChannel = getFirstUnnaturalChannel(task);
            CommonLogUtil.addInfo(null, "自然渠道用户,裂变活动:"+fissionId+",任务:"+task.getTaskId()+",用户:"+task.getUserWxUnionId()+",是否获取到一个非自然(裸连接)渠道:"+(firstUnnaturalChannel==null));
            //如果有非自然(裸连接)渠道则把当前空渠道设置为该渠道
            if(firstUnnaturalChannel != null && firstUnnaturalChannel.getChannel() != null){
                task.setChannel(firstUnnaturalChannel.getChannel());
                //如果是销售渠道还要把该销售赋值给它
                if(FissionChannel.CHANNEL_2.getChannel().equals(firstUnnaturalChannel.getChannel())){
                    task.setSaleWxUnionId(firstUnnaturalChannel.getSaleWxUnionId());
                }
            }
        //如果是销售渠道和运营渠道就缓存
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
        sb.append(",裂变活动:");sb.append(task.getFissionId());
        sb.append(",任务:");sb.append(task.getTaskId());
        sb.append(",销售:");sb.append(saleWxUnionId);
        sb.append(",分享人:");sb.append(shareWxUnionId);
        sb.append(",用户:");sb.append(userWxUnionId);
        String kw = sb.toString();
        CommonLogUtil.addInfo(null, kw+"开始处理");
        //是否有销售UnionId
        boolean hasSale = ConstantsUtil.isWxUnionId(task.getSaleWxUnionId());
        if(!hasSale){
            task.setSaleWxUnionId(null);
        }else{
            //验证销售是否已报名
            if(!fissionSaleServiceImpl.hasSale(task.getFissionId(), saleWxUnionId)){
                CommonLogUtil.addInfo(null, kw+"，销售不存，非法的请求");
                return new TcResponse(StatusCodeEnum.USER_NOT_EXIST.getCode(),"销售不存在");
            }
        }

        //分享人是否是销售
        boolean shareIsSale= false;
        //是否有分享人UnionId
        boolean hasShareUser = ConstantsUtil.isWxUnionId(task.getShareWxUnionId());
        if(!hasShareUser){
            task.setShareWxUnionId(null);
        }else{
            //分享人是否是销售
            shareIsSale=fissionSaleServiceImpl.hasSale(task.getFissionId(), shareWxUnionId);
        }
        task.setId(null);
        //新增裂变用户
        int userExist = addFissionUser(task);
        //有效任务校验
        FissionTaskRecordStatus taskRecordStatus = effectiveTask(task, activity, hasSale, hasShareUser, shareIsSale, kw,userExist);
        task.setOngoingData(taskRecordStatus.getStatus());
        //是否有效任务
        boolean isEffectiveTask = taskRecordStatus.getStatus().equals(FissionTaskRecordStatus.STATUS_2.getStatus())?true:false;
        //保存任务流水记录
        fissionUserTaskRecordMapper.insertFissionUserTaskRecord(task);
        //缓存活动是开启状态的用户任务
        cacheActivityBeginTask(task);
        //任务记录ID
        Long taskRecordId = task.getId();
        //销售和分享人的奖励计算
        reward(task, activity, hasSale, hasShareUser, shareIsSale, kw, taskRecordId,isEffectiveTask);
        CommonLogUtil.addInfo(null, kw+"，任务记录保存成功");
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(),"成功");
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
        //任务奖励
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
        //属于看直播的记录ids
        List<String> liveUserWxUnionIds = new ArrayList<>();
        List<String> subscribeUserWxUnionIds = new ArrayList<>();
        //预约直播
        if (FissionTaskType.TASK_2.getCode().equals(taskId)) {
            //观看直播的
            if (!userWxUnionIds.isEmpty()) {
                liveUserWxUnionIds = fissionUserTaskRecordMapper.selectSubscribeOrLiveUserWxUnionIds(fissionId, FissionTaskType.TASK_4.getCode(), userWxUnionIds);
            }
        }

        if (FissionTaskType.TASK_4.getCode().equals(taskId)) {
            //预约直播的
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
            //1分钟内的展示“XX秒前”
            if (diffTime < ONE_MINUTE) {
                fissionUserTaskRecordSaleDetailVo.setDateDesc(diffTime / 1000 + "秒前");
            } else if (diffTime >= ONE_MINUTE && diffTime < ONE_HOUR) {
                //大于1分钟小于1小时的展示“XX分钟前”
                fissionUserTaskRecordSaleDetailVo.setDateDesc(diffTime / ONE_MINUTE + "分钟前");
            } else if (diffTime >= ONE_HOUR && diffTime < THREE_DAYS) {
                //大于1小时且在3天内的展示“今天几点”
                if (diffTime <= ONE_DAY) {
                    fissionUserTaskRecordSaleDetailVo.setDateDesc("今天 " + timeHHmm);
                } else if (diffTime <= TWO_DAYS) {
                    fissionUserTaskRecordSaleDetailVo.setDateDesc("昨天 " + timeHHmm);
                } else {
                    fissionUserTaskRecordSaleDetailVo.setDateDesc("前天 " + timeHHmm);
                }
            } else {
                fissionUserTaskRecordSaleDetailVo.setDateDesc(SHORT_DATE_FORMAT.format(fissionActivitySaleDto.getCreateDt()));
            }
            //积分是否有效
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
        //任务奖励
        List<FissionAwardRule> fissionAwardRuleList = fissionActivityService.getAwardRuleListByFissionId(fissionId, GlobalConstants.FISSION_AWARD_RULE_TYPE_B);
        FissionAwardRule fissionAwardRule = null;
        for (FissionAwardRule far : fissionAwardRuleList) {
            if (far.getTaskCode().equals(String.valueOf(taskId))) {
                fissionAwardRule = far;
                break;
            }
        }
        //查经销商名
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
            //积分是否有效
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
        //预约直播人数
        Integer subscribeCount = fissionUserTaskRecordMapper.selectSubscribeOrLiveUserCount(fissionId, FissionTaskType.TASK_2.getCode());
        //预约直播并观看直播人数
        Integer subscribeLiveCount = fissionUserTaskRecordMapper.selectSubscribeLiveUserCount(fissionId);
        //观看直播人数
        Integer liveCount = fissionUserTaskRecordMapper.selectSubscribeOrLiveUserCount(fissionId, FissionTaskType.TASK_4.getCode());
        FissionSubscribeOrLiveCountDto fissionSubscribeOrLiveCountDto = new FissionSubscribeOrLiveCountDto();
        fissionSubscribeOrLiveCountDto.setSubscribeCount(subscribeCount);
        fissionSubscribeOrLiveCountDto.setSubscribeLiveCount(subscribeLiveCount);
        fissionSubscribeOrLiveCountDto.setLiveCount(liveCount);
        return fissionSubscribeOrLiveCountDto;

    }

    /**
     * 活动时间已开始且是【已开启】状态
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
     * 新增裂变用户
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
     * 判断是否有效任务
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
        //不是进行中
        if(!isStarted){
            CommonLogUtil.addInfo(null, kw+"，活动未开始或未开启，开始时间："+activity.getStartTime()+"，开启状态："+activity.getOnState());
            return FissionTaskRecordStatus.STATUS_0;
        }
        //销售自己完成自己的任务不计算积分
        if(task.getUserWxUnionId().equals(task.getSaleWxUnionId())){
            CommonLogUtil.addInfo(null, kw+"，销售自己完成自己的任务不计算奖励");
            return FissionTaskRecordStatus.STATUS_1;
        }
        //销售不当做C端用户，不计算积分
        boolean userIsSale=fissionSaleServiceImpl.hasSale(task.getFissionId(), task.getUserWxUnionId());
        if(userIsSale){
            CommonLogUtil.addInfo(null, kw+"，销售不能当做C端用户，该任务不计算奖励");
            return FissionTaskRecordStatus.STATUS_1;
        }
        //是否有效任务
        boolean isEffectiveTask = false;
        //任务1：浏览页面
        if(FissionTaskType.TASK_1.getCode().equals(task.getTaskId())){
            boolean exist = hasActivityBeginTask(task);
            if(!exist){
                isEffectiveTask = true;
            }
            //浏览数
            fissionActivityService.getOrIncrEffectNum(task.getFissionId(),  GlobalConstants.FissionEffectTypeEnum.BROWSE_NUM, false);
        //任务2：预约直播
        }else if(FissionTaskType.TASK_2.getCode().equals(task.getTaskId()) ){
            boolean exist = hasActivityBeginTask(task);
            if(!exist){
                isEffectiveTask = true;
            }
            //预约数
            fissionActivityService.getOrIncrEffectNum(task.getFissionId(), GlobalConstants.FissionEffectTypeEnum.SUBSCRIBE_NUM, false);
        //任务3：购买活动页商品
        }else if(FissionTaskType.TASK_3.getCode().equals(task.getTaskId())){
            boolean exist = hasActivityBeginTask(task);
            if(!exist){
                isEffectiveTask = true;
            }
        //任务4：观看直播
        }else if(FissionTaskType.TASK_4.getCode().equals(task.getTaskId()) ){
            boolean exist = hasActivityBeginTask(task);
            if(!exist){
                isEffectiveTask = true;
            }
        //任务5：购买直播商品
        }else if(FissionTaskType.TASK_5.getCode().equals(task.getTaskId())){
            boolean exist = hasActivityBeginTask(task);
            if(!exist){
                isEffectiveTask = true;
            }
        }
        //有效任务
        return isEffectiveTask?FissionTaskRecordStatus.STATUS_2:FissionTaskRecordStatus.STATUS_1;
    }
    /**
     * 销售 C端奖励计算
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
        CommonLogUtil.addInfo(null, kw+"，开始给C端分享人计算奖励，本次任务是否属于有效任务："+isEffectiveTask+"，是否有分享人："+hasShareUser+"，分享人是否是销售："+shareIsSale+"，是否有现金奖励："+hasReward);
        //有效任务
        if(isEffectiveTask){
            //有销售UnionId的时候才会往有效表里插入数据
            if(hasSale){
                fissionUserEffectiveTaskService.insertFissionUserEffectiveTask(effectiveTask);
            }
            //有【分享人】且【分享人不是销售】且【C端有现金奖励】且【用户不是在自己分享的页面完成的任务】才计算C端分享人的奖励
            if(hasShareUser && !shareIsSale && hasReward && !task.getShareWxUnionId().equals(task.getUserWxUnionId())){
                userTaskReward(shareIsSale,task, activity, taskRecordId);
            }
        }
    }
    /**
     * 计算C端用户奖励
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
     * @description: 处理分享数
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
            //分享数
            fissionActivityService.getOrIncrEffectNum(task.getFissionId(),  GlobalConstants.FissionEffectTypeEnum.SHARE_NUM, false);
        }
    }

    /**
     * 获取第一个非自然渠道记录
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
                    exptime=3600000;//空值缓存1个小时
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
            //渠道为空的情况下才设置
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
     * 判断用户是否有在活动开启后的任务
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
            CommonLogUtil.addInfo(null, "getUserWxUnionId："+task.getUserWxUnionId()+"，ConstantsUtil.TRUE.equals(t):"+ConstantsUtil.TRUE.equals(t));
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
                CommonLogUtil.addInfo(null, "getUserWxUnionId："+task.getUserWxUnionId()+"，has:"+has);
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
     * 缓存活动是开启状态的用户任务
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
     * 缓存的KEY
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

