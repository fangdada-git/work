package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.exception.RedisException;
import com.tuanche.directselling.api.FissionUserService;
import com.tuanche.directselling.constant.BaseCacheKeys;
import com.tuanche.directselling.constant.Constants;
import com.tuanche.directselling.dto.FissionUserRewardDto;
import com.tuanche.directselling.dto.FissionUserRewardReconciliationDto;
import com.tuanche.directselling.dto.UserRewardReconciliationDto;
import com.tuanche.directselling.mapper.read.directselling.FissionActivityReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FissionTradeRecordReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FissionUserReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FissionUserRewardReadMapper;
import com.tuanche.directselling.mapper.write.directselling.FissionUserWriteMapper;
import com.tuanche.directselling.model.FissionTradeRecord;
import com.tuanche.directselling.model.FissionUser;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.util.EmojiFilter;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.district.api.dto.input.DistrictBaseInputDto;
import com.tuanche.district.api.dto.output.DistrictOutputBaseDto;
import com.tuanche.district.api.service.IDistrictApiService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author：HuangHao
 * @CreatTime 2020-09-23 16:09
 */
@Service
public class FissionUserServiceImpl implements FissionUserService {

    private static String FISSION_USER_LOCK = "fission:user:lock:";
    private static String FISSION_USER_INFO = "fission:user:info:";
    @Autowired
    FissionUserReadMapper fissionUserReadMapper;
    @Autowired
    FissionUserWriteMapper fissionUserWriteMapper;
    @Autowired
    private FissionUserServiceImpl fissionUserServiceImpl;
    @Autowired
    @Qualifier("ClusterRedisService")
    private RedisService redisService;
    @Autowired
    private FissionActivityReadMapper fissionActivityReadMapper;
    @Autowired
    private FissionUserRewardReadMapper fissionUserRewardReadMapper;
    @Autowired
    private FissionTradeRecordReadMapper fissionTradeRecordReadMapper;
    @Reference
    private IDistrictApiService iDistrictApiService;

    /**
     * 新增裂变活动用户
     *
     * @param fissionUser
     * @return int 返回1表示新增成功
     * @author HuangHao
     * @CreatTime 2020-09-24 18:22
     */
    @Override
    public int addFissionUser(FissionUser fissionUser) {
        if (fissionUser == null || StringUtils.isEmpty(fissionUser.getUserWxUnionId())) {
            return 0;
        }
        if (fissionUser.getFissionId() == null) {
            return 0;
        }
        String key = BaseCacheKeys.FISSION_USER_LOCK.getKey() + fissionUser.getFissionId() + "_" + fissionUser.getUserWxUnionId();
        Integer res = 0;
        String lock = null;
        try {
            try {
                lock = redisService.set(key, "0", "NX", "EX", BaseCacheKeys.FISSION_USER_LOCK.getExpire());
            } catch (RedisException e) {
                e.printStackTrace();
            }
            if (Constants.OK.equals(lock)) {
                FissionUser fissionUserInfo = getFissionUser(fissionUser);
                if (fissionUserInfo == null) {
                    String nickName = null;
                    if (StringUtils.isEmpty(fissionUser.getNickName())) {
                        nickName = "匿名";
                    } else {
                        //微信昵称特殊字符过滤
                        nickName = EmojiFilter.filterEmoji(fissionUser.getNickName());
                    }
                    fissionUser.setNickName(nickName);
                    res = fissionUserWriteMapper.insertFissionUser(fissionUser);
                    setCacheFissionUser(fissionUser);
                }
            }
        } finally {
            if (lock != null) {
                try {
                    redisService.del(key);
                } catch (RedisException e) {
                    e.printStackTrace();
                }
            }
        }
        return res.intValue();
    }

    /**
     * 先从缓存取，没有再从写库取
     *
     * @param fissionUser
     * @return com.tuanche.directselling.model.FissionUser
     * @author HuangHao
     * @CreatTime 2020-12-28 17:22
     */
    @Override
    public FissionUser getFissionUser(FissionUser fissionUser) {
        String key = cacheKey(fissionUser);
        FissionUser user = null;
        try {
            user = redisService.get(key, FissionUser.class);
            if (user == null) {
                user = fissionUserWriteMapper.getFissionUser(fissionUser);
                if (user != null) {
                    setCacheFissionUser(user);
                }
            }
        } catch (RedisException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * 设置缓存
     *
     * @param fissionUser
     * @return void
     * @author HuangHao
     * @CreatTime 2020-12-28 17:31
     */
    public void setCacheFissionUser(FissionUser fissionUser) {
        String key = cacheKey(fissionUser);
        try {
            redisService.set(key, fissionUser, BaseCacheKeys.FISSION_USER_INFO_CACHE.getExpire());
        } catch (RedisException e) {
            e.printStackTrace();
        }
    }

    //缓存的key
    public String cacheKey(FissionUser fissionUser) {
        return BaseCacheKeys.FISSION_USER_INFO_CACHE.getKey() + fissionUser.getFissionId() + ":" + fissionUser.getUserWxUnionId();
    }


    /**
     * 用户是否存在于裂变活动中
     *
     * @param userWxOpenId
     * @param fissionId
     * @return java.lang.Integer
     * @author HuangHao
     * @CreatTime 2020-09-23 16:11
     */
    private Integer userExistInFissionId(String userWxOpenId, Integer fissionId) {
        return fissionUserReadMapper.userExistInFissionId(userWxOpenId, fissionId);
    }

    /**
     * 新增裂变用户
     *
     * @param fissionUser
     * @return int 返回0则插入失败，否则返回用户ID
     * @author HuangHao
     * @CreatTime 2020-09-23 15:57
     */
    private int insertFissionUser(FissionUser fissionUser) {
        String nickName = EmojiFilter.filterEmoji(fissionUser.getNickName());
        fissionUser.setNickName(nickName);
        fissionUserWriteMapper.insertFissionUser(fissionUser);
        return fissionUser.getId();
    }

    @Override
    public List<FissionUser> getUserWxInfo(FissionUser fissionUser) {
        return fissionUserReadMapper.getUserWxInfo(fissionUser);
    }

    @Override
    public List<FissionUserRewardReconciliationDto> getUserRewardReconciliationDtoList(UserRewardReconciliationDto userRewardReconciliationDto) {
        List<FissionUser> fissionUserList = fissionUserReadMapper.selectFissionRewardUser(userRewardReconciliationDto);
        return fissionUserServiceImpl.getFissionUserRewardReconciliationDtoList(userRewardReconciliationDto, fissionUserList);
    }

    @Override
    public PageResult getUserRewardReconciliationDtoPage(UserRewardReconciliationDto userRewardReconciliationDto) {
        PageResult<FissionUserRewardReconciliationDto> pageResult = new PageResult<>();
        PageHelper.startPage(userRewardReconciliationDto.getPage(), userRewardReconciliationDto.getLimit());
        List<FissionUser> fissionUserList = fissionUserReadMapper.selectFissionRewardUser(userRewardReconciliationDto);
        List<FissionUserRewardReconciliationDto> reconciliationDtoList = fissionUserServiceImpl.getFissionUserRewardReconciliationDtoList(userRewardReconciliationDto, fissionUserList);
        PageInfo<FissionUserRewardReconciliationDto> pageInfo = new PageInfo(reconciliationDtoList);
        pageResult.setCount(pageInfo.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode());
        pageResult.setMsg(StatusCodeEnum.SUCCESS.getText());
        pageResult.setData(reconciliationDtoList);
        return pageResult;
    }

    private List<FissionUserRewardReconciliationDto> getFissionUserRewardReconciliationDtoList(UserRewardReconciliationDto userRewardReconciliationDto, List<FissionUser> fissionUserList) {
        List<FissionUserRewardReconciliationDto> fissionUserRewardReconciliationDtoList = new ArrayList<>();
        if (fissionUserList.isEmpty()) {
            return fissionUserRewardReconciliationDtoList;
        }
        List<String> userWxUnionIds = new ArrayList<>();
        List<Integer> cityIds = new ArrayList<>();
        for (FissionUser fissionUser : fissionUserList) {
            userWxUnionIds.add(fissionUser.getUserWxUnionId());
            cityIds.add(fissionUser.getCityId());
        }
        Map<Integer, DistrictOutputBaseDto> districtMap = null;
        if (!cityIds.isEmpty()) {
            DistrictBaseInputDto districtBaseInputDto = new DistrictBaseInputDto();
            districtBaseInputDto.setIds(cityIds);
            List<DistrictOutputBaseDto> districtOutputBaseDtos = iDistrictApiService.getBaseDistrictList(districtBaseInputDto);
            districtMap = districtOutputBaseDtos.stream().collect(Collectors.toMap(DistrictOutputBaseDto::getId, Function.identity(), (key1, key2) -> key2));
        }
        List<FissionUserRewardDto> fissionUserRewardDtoList = fissionUserRewardReadMapper.selectFissionUserRewardByWxUnionIds(userRewardReconciliationDto.getFissionId(), userWxUnionIds, userRewardReconciliationDto.getPayBeginTime(), userRewardReconciliationDto.getPayEndTime());
        Map<String, List<FissionUserRewardDto>> fissionUserRewardDtoMap = new HashMap<>((int) (userWxUnionIds.size() / 0.75f + 1f));
        List<Integer> tradeRecordIds = new ArrayList<>();
        for (FissionUserRewardDto fissionUserRewardDto : fissionUserRewardDtoList) {
            if (fissionUserRewardDtoMap.get(fissionUserRewardDto.getUserWxUnionId()) == null) {
                fissionUserRewardDtoMap.put(fissionUserRewardDto.getUserWxUnionId(), new ArrayList<>());
            }
            fissionUserRewardDtoMap.get(fissionUserRewardDto.getUserWxUnionId()).add(fissionUserRewardDto);
            if (StringUtils.isNotBlank(fissionUserRewardDto.getTradeRecordIds())) {
                String[] tradeRecordIdsStr = fissionUserRewardDto.getTradeRecordIds().split(",");
                for (String tradeRecordId : tradeRecordIdsStr) {
                    tradeRecordIds.add(Integer.parseInt(tradeRecordId));
                }
            }
        }
        Map<Long, FissionTradeRecord> fissionTradeRecordMap = new HashMap<>((int) (tradeRecordIds.size() / 0.75f + 1f));
        if (!tradeRecordIds.isEmpty()) {
            List<FissionTradeRecord> fissionTradeRecords = fissionTradeRecordReadMapper.selectFissionUserPaymentByIds(tradeRecordIds);
            for (FissionTradeRecord fissionTradeRecord : fissionTradeRecords) {
                fissionTradeRecordMap.put(fissionTradeRecord.getId(), fissionTradeRecord);
            }
        }
        FissionUserRewardReconciliationDto rewardReconciliationDto = null;
        for (FissionUser fissionUser : fissionUserList) {
            rewardReconciliationDto = new FissionUserRewardReconciliationDto();
            rewardReconciliationDto.setUserWxUnionId(fissionUser.getUserWxUnionId());
            rewardReconciliationDto.setActivityName(fissionUser.getActivityName());
            rewardReconciliationDto.setNickName(fissionUser.getNickName());
            if (districtMap != null) {
                DistrictOutputBaseDto districtOutputBaseDto = districtMap.get(fissionUser.getCityId());
                if (districtOutputBaseDto != null) {
                    rewardReconciliationDto.setCityName(districtOutputBaseDto.getName());
                }
            }
            fillTaskProperty(fissionUser, rewardReconciliationDto, fissionUserRewardDtoMap, fissionTradeRecordMap);
            fissionUserRewardReconciliationDtoList.add(rewardReconciliationDto);
        }
        return fissionUserRewardReconciliationDtoList;
    }

    /**
     * 裂变的5个任务金额赋值
     *
     * @param fissionUser             裂变C端用户实体
     * @param fissionUserRewardDtoMap 裂变C端用户奖励实体map
     */
    private void fillTaskProperty(FissionUser fissionUser, FissionUserRewardReconciliationDto rewardReconciliationDto, Map<String, List<FissionUserRewardDto>> fissionUserRewardDtoMap, Map<Long, FissionTradeRecord> fissionTradeRecordMap) {
        List<FissionUserRewardDto> fissionUserRewardDtoList = fissionUserRewardDtoMap.get(fissionUser.getUserWxUnionId());
        BigDecimal totalRewardAmount = BigDecimal.ZERO;
        List<Long> tradeRecordIds = new ArrayList<>();
        if (fissionUserRewardDtoList == null) {
            rewardReconciliationDto.setTotalRewardAmount(totalRewardAmount);
            return;
        }
        BigDecimal successRewardAmount = BigDecimal.ZERO;
        BigDecimal failRewardAmount = BigDecimal.ZERO;
        BigDecimal pageView = BigDecimal.ZERO;
        BigDecimal subscribeLive = BigDecimal.ZERO;
        BigDecimal activityPageProduct = BigDecimal.ZERO;
        BigDecimal liveView = BigDecimal.ZERO;
        BigDecimal livePageProduct = BigDecimal.ZERO;
        for (FissionUserRewardDto fissionUserRewardDto : fissionUserRewardDtoList) {
            totalRewardAmount = totalRewardAmount.add(fissionUserRewardDto.getRewardAmount());
            if (fissionUserRewardDto.getPaymentOrNot()) {
                successRewardAmount = successRewardAmount.add(fissionUserRewardDto.getRewardAmount());
            } else {
                failRewardAmount = failRewardAmount.add(fissionUserRewardDto.getRewardAmount());
            }
            if (StringUtils.isNotBlank(fissionUserRewardDto.getTradeRecordIds())) {
                String[] tradeRecordIdsStr = fissionUserRewardDto.getTradeRecordIds().split(",");
                for (String id : tradeRecordIdsStr) {
                    tradeRecordIds.add(Long.parseLong(id));
                }
            }
            switch (fissionUserRewardDto.getTaskId()) {
                case 1:
                    pageView = pageView.add(fissionUserRewardDto.getRewardAmount());
                    break;
                case 2:
                    subscribeLive = subscribeLive.add(fissionUserRewardDto.getRewardAmount());
                    break;
                case 3:
                    activityPageProduct = activityPageProduct.add(fissionUserRewardDto.getRewardAmount());
                    break;
                case 4:
                    liveView = liveView.add(fissionUserRewardDto.getRewardAmount());
                    break;
                case 5:
                    livePageProduct = livePageProduct.add(fissionUserRewardDto.getRewardAmount());
                    break;
                default:
                    CommonLogUtil.addInfo("不存在的裂变任务ID", "不存在的裂变任务ID," + fissionUserRewardDto.getTaskId());
            }
        }
        rewardReconciliationDto.setPageView(pageView);
        rewardReconciliationDto.setSubscribeLive(subscribeLive);
        rewardReconciliationDto.setActivityPageProduct(activityPageProduct);
        rewardReconciliationDto.setLiveView(liveView);
        rewardReconciliationDto.setLivePageProduct(livePageProduct);
        if (!tradeRecordIds.isEmpty()) {
            fillTradeProperty(rewardReconciliationDto, tradeRecordIds, fissionTradeRecordMap);
        }
        rewardReconciliationDto.setTotalRewardAmount(totalRewardAmount);
        rewardReconciliationDto.setSuccessRewardAmount(successRewardAmount);
        rewardReconciliationDto.setFailRewardAmount(failRewardAmount);
    }

    /**
     * 交易相关属性赋值
     *
     * @param rewardReconciliationDto 最终赋值对象
     * @param tradeRecordIds          交易记录ID
     * @param fissionTradeRecordMap   相关交易记录
     */
    private void fillTradeProperty(FissionUserRewardReconciliationDto rewardReconciliationDto, List<Long> tradeRecordIds, Map<Long, FissionTradeRecord> fissionTradeRecordMap) {
        Set<String> tradeNos = new HashSet<>();
        for (Long tradeRecordId : tradeRecordIds) {
            FissionTradeRecord fissionTradeRecord = fissionTradeRecordMap.get(tradeRecordId);
            if (fissionTradeRecord == null) {
                continue;
            }
            rewardReconciliationDto.setPayTime(fissionTradeRecord.getPayTime());
            if (fissionTradeRecord.getTradeNo() != null) {
                tradeNos.add(fissionTradeRecord.getTradeNo());
            }
        }
        rewardReconciliationDto.setTradeNo(StringUtils.join(tradeNos.toArray(), ","));
    }
}
