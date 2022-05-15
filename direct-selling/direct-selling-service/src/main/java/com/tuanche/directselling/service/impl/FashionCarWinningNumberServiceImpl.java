package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.consol.dubbo.CommonService;
import com.tuanche.consol.dubbo.OnlineFestivalFacadeService;
import com.tuanche.consol.dubbo.bean.ConsolParametersVo;
import com.tuanche.consol.dubbo.enums.MethodEnum;
import com.tuanche.consol.dubbo.enums.OnlineFestivalMethodEnum;
import com.tuanche.consol.dubbo.enums.ResultCodeEnum;
import com.tuanche.consol.dubbo.enums.SourcesEnum;
import com.tuanche.consol.dubbo.enums.VersionEnum;
import com.tuanche.consol.dubbo.vo.base.ConsoleServiceParamVo;
import com.tuanche.consol.dubbo.vo.carFashion.CarFashionActivityInfoReqVo;
import com.tuanche.consol.dubbo.vo.carFashion.CarFashionInfoEntityResVo;
import com.tuanche.directselling.api.FashionCarWinningNumberService;
import com.tuanche.directselling.constant.BaseCacheKeys;
import com.tuanche.directselling.dto.FashionCarWinningNumberDto;
import com.tuanche.directselling.enums.FashionCarUserType;
import com.tuanche.directselling.exception.WinningNumberException;
import com.tuanche.directselling.mapper.read.directselling.FashionCarMarkeWinNumReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FashionHalfPriceCarReadMapper;
import com.tuanche.directselling.mapper.write.directselling.FashionCarMarkeUserWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.FashionCarMarkeWinNumWriteMapper;
import com.tuanche.directselling.model.FashionCarMarkeUser;
import com.tuanche.directselling.model.FashionCarMarkeWinNum;
import com.tuanche.directselling.model.FashionHalfPriceCar;
import com.tuanche.directselling.pojo.CommonHeaderVo;
import com.tuanche.directselling.pojo.LotteryDto;
import com.tuanche.directselling.pojo.LotteryResultsDto;
import com.tuanche.directselling.pojo.PlwDto;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.utils.DateUtil;
import com.tuanche.directselling.utils.StatusCodeEnum;
import org.apache.commons.lang3.time.DateUtils;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/9/3 14:47
 **/
@Service
public class FashionCarWinningNumberServiceImpl implements FashionCarWinningNumberService {

    /**
     * 每次生成中奖码数量
     */
    private final int NUM_BATCH = 200;
    /**
     * 发出50%中间码时 生产新的中奖码
     */
    private final double NUM_CREATED = 0.5;
    /**
     * 中奖码最大量
     */
    private final int MAX_NUM = 100000000;
    /**
     * 每个用户最多获得中奖码数量
     */
    private final int USER_NUM_MAX = 8;
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final static SimpleDateFormat sdfShort = new SimpleDateFormat("yyMMdd");
    @Autowired
    private RedissonClient redisson;
    @Autowired
    private FashionCarWinningNumberService fashionCarWinningNumberService;
    @Reference
    private OnlineFestivalFacadeService onlineFestivalFacadeService;
    @Autowired
    private FashionCarMarkeUserWriteMapper fashionCarMarkeUserWriteMapper;
    @Autowired
    private FashionCarMarkeWinNumReadMapper fashionCarMarkeWinNumReadMapper;
    @Autowired
    private FashionCarMarkeWinNumWriteMapper fashionCarMarkeWinNumWriteMapper;
    @Autowired
    private FashionHalfPriceCarReadMapper fashionHalfPriceCarReadMapper;
    @Autowired
    @Qualifier("ClusterRedisService")
    private RedisService redisService;
    @Value("${fashion_car_win_num_global_switch}")
    private boolean fashionCarWinNum;
    @Value("#{'${fashion_car_win_num_switch_open}'.split(',')}")
    private List<Integer> fashionCarWinNumSwitchOpen;
    @Value("#{'${fashion_car_win_num_switch_close}'.split(',')}")
    private List<Integer> fashionCarWinNumSwitchClose;
    @Value("${fashion_car_win_num_end_time}")
    private String fashionCarWinNumEndTime;
    @Reference(timeout = 9000, check = false, interfaceName = "com.tuanche.consol.dubbo.CommonService")
    public CommonService commonService;
    @Autowired
    @Qualifier("executorService")
    private ExecutorService executorService;


    @Override
    public void initWinningNumber(Integer periodsId, long expire) {
        executorService.execute(() -> {
            RLock lock = redisson.getLock(MessageFormat.format(BaseCacheKeys.FASHION_CAR_NUM_INIT_LOCK.getKey(), periodsId));
            try {
                if (lock.tryLock(BaseCacheKeys.FASHION_CAR_NUM_INIT_LOCK.getExpire(), BaseCacheKeys.FASHION_CAR_NUM_INIT_LOCK.getExpire(), TimeUnit.MILLISECONDS)) {
                    RAtomicLong sentCount = redisson.getAtomicLong(MessageFormat.format(BaseCacheKeys.FASHION_CAR_NUM_SEND_COUNT.getKey(), periodsId));
                    RAtomicLong createdCount = redisson.getAtomicLong(MessageFormat.format(BaseCacheKeys.FASHION_CAR_NUM_CREATED_COUNT.getKey(), periodsId));
                    if (createdCount.get() != 0 && sentCount.get() < createdCount.get() * NUM_CREATED) {
                        return;
                    }
                    RBlockingQueue<Integer> queue = redisson.getBlockingQueue(MessageFormat.format(BaseCacheKeys.FASHION_CAR_NUM_LIST.getKey(), periodsId));
                    try {
                        Random random = new Random();
                        List<Integer> nums = new ArrayList<>();
                        int duplicateTimes = 0;
                        while (nums.isEmpty() || nums.size() < NUM_BATCH) {
                            int num = random.nextInt(MAX_NUM);
                            RBucket<Integer> bucket = redisson.getBucket(MessageFormat.format(BaseCacheKeys.FASHION_CAR_NUM_SENT.getKey(), periodsId, num));
                            if (bucket.isExists()) {
                                duplicateTimes++;
                                if (duplicateTimes > NUM_BATCH / 2) {
                                    CommonLogUtil.addError("潮车集-半价车", "初始化中奖码重复概率已到50%", new WinningNumberException(500, "初始化中奖码重复概率已到50%"));
                                    break;
                                }
                            } else {
                                nums.add(num);
                                bucket.set(num);
                                bucket.expire(expire, TimeUnit.MILLISECONDS);
                            }
                        }
                        int batchCount = nums.size();
                        for (int i = 0; i < batchCount; i++) {
                            queue.add(nums.get(i));
                            createdCount.incrementAndGet();
                        }
                        queue.expire(expire, TimeUnit.MILLISECONDS);
                        createdCount.expire(expire, TimeUnit.MILLISECONDS);
                    } catch (Exception e) {
                        CommonLogUtil.addError("潮车集-半价车", "初始化中奖码失败", e);
                    } finally {
                        //解锁
                        if (lock.isLocked()) {
                            lock.unlock();
                        }
                    }
                }
            } catch (InterruptedException e) {
                CommonLogUtil.addError("潮车集-半价车", "初始化中奖码失败", e);
                e.printStackTrace();
            } finally {
                if (lock.isLocked()) {
                    lock.unlock();
                }
            }
        });

    }

    @Override
    public String createWinningNumber(Integer periodsId, Integer userId, Integer helpUserId, Integer userType) throws WinningNumberException {
        CarFashionInfoEntityResVo carFashionInfoEntityResVo = getCarFashionInfoEntityResVo(periodsId);
        if (carFashionInfoEntityResVo == null) {
            throw new WinningNumberException(StatusCodeEnum.WIN_NUM_NO_PERIODS_ERROR.getCode(), StatusCodeEnum.WIN_NUM_NO_PERIODS_ERROR.getText());
        }
        long now = System.currentTimeMillis();
        long end = now + carFashionInfoEntityResVo.getFormalDateEnd().getTime();
        long expire = end - now;
        if (expire <= 0) {
            CommonLogUtil.addInfo("潮车集-半价车", StatusCodeEnum.WIN_NUM_EXPIRE_ERROR.getText());
            throw new WinningNumberException(StatusCodeEnum.WIN_NUM_EXPIRE_ERROR.getCode(), StatusCodeEnum.WIN_NUM_EXPIRE_ERROR.getText());
        }

        if (userId == null) {
            CommonLogUtil.addInfo("潮车集-半价车", StatusCodeEnum.WIN_NUM_NO_USER_ERROR.getText());
            throw new WinningNumberException(StatusCodeEnum.WIN_NUM_NO_USER_ERROR.getCode(), StatusCodeEnum.WIN_NUM_NO_USER_ERROR.getText());
        }
        RAtomicLong sentCount = redisson.getAtomicLong(MessageFormat.format(BaseCacheKeys.FASHION_CAR_NUM_SEND_COUNT.getKey(), periodsId));
        RAtomicLong createdCount = redisson.getAtomicLong(MessageFormat.format(BaseCacheKeys.FASHION_CAR_NUM_CREATED_COUNT.getKey(), periodsId));
        if (createdCount.get() == 0 || sentCount.get() > createdCount.get() * NUM_CREATED) {
            fashionCarWinningNumberService.initWinningNumber(periodsId, expire);
        }
        RLock lock = redisson.getLock(MessageFormat.format(BaseCacheKeys.FASHION_CAR_NUM_SEND_LOCK.getKey(), periodsId, userId));
        lock.lock(BaseCacheKeys.FASHION_CAR_NUM_SEND_LOCK.getExpire(), TimeUnit.SECONDS);
        RList<Integer> userNums = redisson.getList(MessageFormat.format(BaseCacheKeys.FASHION_CAR_USER_NUM_LIST.getKey(), periodsId, userId));
        Integer userNum = null;
        if (userNums.size() < USER_NUM_MAX) {
            try {
                RBlockingQueue<Integer> queue = redisson.getBlockingQueue(MessageFormat.format(BaseCacheKeys.FASHION_CAR_NUM_LIST.getKey(), periodsId));
                userNum = queue.poll(5000, TimeUnit.MILLISECONDS);
                if (userNum == null) {
                    Random random = new Random();
                    userNum = random.nextInt(MAX_NUM);
                    CommonLogUtil.addError("潮车集-半价车", "5s内没返回抽奖码", new WinningNumberException(StatusCodeEnum.WIN_NUM_CREATE_TIMEOUT_ERROR.getCode(), StatusCodeEnum.WIN_NUM_CREATE_TIMEOUT_ERROR.getText()));
                }
                sentCount.incrementAndGet();
                sentCount.expire(expire, TimeUnit.MILLISECONDS);
                userNums.add(userNum);
                userNums.expire(expire, TimeUnit.MILLISECONDS);
                FashionCarMarkeWinNum fashionCarMarkeWinNum = new FashionCarMarkeWinNum();
                fashionCarMarkeWinNum.setCreateDt(new Date());
                fashionCarMarkeWinNum.setWinNum(userNum);
                fashionCarMarkeWinNum.setUserId(userId);
                fashionCarMarkeWinNum.setPeriodsId(periodsId);
                fashionCarMarkeWinNum.setHelpUserId(helpUserId);
                fashionCarMarkeWinNum.setUserType(userType);
                fashionCarMarkeWinNumWriteMapper.insertSelective(fashionCarMarkeWinNum);
            } catch (Exception e) {
                if (lock.isLocked()) {
                    lock.unlock();
                }
                CommonLogUtil.addError("潮车集-半价车", StatusCodeEnum.WIN_NUM_CREATE_ERROR.getText(), e);
                e.printStackTrace();
                throw new WinningNumberException(StatusCodeEnum.WIN_NUM_CREATE_ERROR.getCode(), StatusCodeEnum.WIN_NUM_CREATE_ERROR.getText());
            } finally {
                if (lock.isLocked()) {
                    lock.unlock();
                }
            }
        } else {
            if (lock.isLocked()) {
                lock.unlock();
            }
            CommonLogUtil.addInfo("潮车集-半价车", StatusCodeEnum.WIN_NUM_ENOUGH_ERROR.getText());
            throw new WinningNumberException(StatusCodeEnum.WIN_NUM_ENOUGH_ERROR.getCode(), StatusCodeEnum.WIN_NUM_ENOUGH_ERROR.getText());
        }

        DecimalFormat df = new DecimalFormat("00000000");
        return df.format(userNum);
    }

    @Override
    public List<FashionCarWinningNumberDto> getWinningNumbers(Integer periodsId, Integer userId) {
        List<FashionCarMarkeWinNum> nums = fashionCarMarkeWinNumReadMapper.selectByActivityIdAndUserId(periodsId, userId);
        List<FashionCarWinningNumberDto> numberDtos = new ArrayList<>();
        FashionCarWinningNumberDto dto;
        Date winTaskTime = null;
        try {
            winTaskTime = DateUtils.parseDate(LocalDate.now().toString() + " " + fashionCarWinNumEndTime, "yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean winFlag = false;
        for (FashionCarMarkeWinNum num : nums) {
            dto = new FashionCarWinningNumberDto();
            if ("1".equals(num.getIsWin().toString())) {
                winFlag = true;
            }
            dto.setWinNum(num.getWinNum());
            dto.setWinNumStr(num.getWinNumStr());
            dto.setCreateDt(num.getCreateDt().getTime());
            dto.setWinFlag("1".equals(num.getIsWin().toString()) ? true : false);
            dto.setTakeFlag(num.getCreateDt().compareTo(winTaskTime) <= 0 ? true : false);
            dto.setId(num.getId());
            numberDtos.add(dto);
        }
        //如果有一个中奖则全部都为不参与
        if (winFlag) {
            numberDtos.forEach(numberDto -> numberDto.setTakeFlag(false));
        }
        return numberDtos;
    }

    @Override
    public Integer getWinningNumberCount(Integer periodsId, Integer userId) {
        if (userId==null) return 0;
        return fashionCarMarkeWinNumReadMapper.selectCountByActivityIdAndUserId(periodsId, userId);
    }

    @Override
    public List<FashionCarMarkeWinNum> getFashionCarMarkeWinNumList(FashionCarMarkeWinNum markeWinNum) {
        return fashionCarMarkeWinNumReadMapper.getFashionCarMarkeWinNumList(markeWinNum);
    }

    @Override
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    public void chooseWinningNumber() {
        Date now = new Date();
        //已配置中奖码
        String dateStr = sdf.format(new Date());
        List<FashionHalfPriceCar> fashionHalfPriceCars = fashionHalfPriceCarReadMapper.selectInputtedWinNum(sdf.format(new Date()));
        FashionCarMarkeWinNum updateWinNum = null;
        for (FashionHalfPriceCar fashionHalfPriceCar : fashionHalfPriceCars) {
            if (getSwitchStatus(fashionHalfPriceCar.getPeriodsId())) {
                FashionCarMarkeWinNum fashionCarMarkeWinNum = fashionCarMarkeWinNumReadMapper.selectChooseWinNum(fashionHalfPriceCar.getPeriodsId(), fashionHalfPriceCar.getWinningNumber(), DateUtil.parseDate(dateStr + " " + fashionCarWinNumEndTime, "yyyy-MM-dd HH:mm:ss"));
                updateWinNum = new FashionCarMarkeWinNum();
                updateWinNum.setId(fashionCarMarkeWinNum.getId());
                updateWinNum.setUpdateDt(new Date());
                updateWinNum.setIsWin((byte) 1);
                updateWinNum.setBrandId(fashionHalfPriceCar.getBrandId());
                updateWinNum.setActivityDate(fashionHalfPriceCar.getActivityDate());
                fashionCarMarkeWinNumWriteMapper.updateByPrimaryKeySelective(updateWinNum);
            } else {
                int userId = Integer.parseInt("-" + sdfShort.format(new Date()) + ((int) (Math.random() * 900) + 100));
                CommonHeaderVo param = new CommonHeaderVo();
                param.setRandom(true);
                param.setSort(0);
                param.setStartPos(0);
                param.setSize(1);
                ConsoleServiceParamVo<String, List<Map<String, String>>> vo = ConsoleServiceParamVo.builder()
                        .sources(SourcesEnum.PC)
                        .method(MethodEnum.API_COMMON_HEAD)
                        .version(VersionEnum.v1_0)
                        .requestData(JSON.toJSONString(param))
                        .build();
                try {
                    vo = commonService.service(vo);
                    if (ResultCodeEnum.OK.equals(vo.getResultCodeEnum())) {
                        List<Map<String, String>> responseData = vo.getResponseData();
                        if (!responseData.isEmpty()) {
                            Map<String, String> stringStringMap = responseData.get(0);
                            FashionCarMarkeUser fashionCarMarkeUser = new FashionCarMarkeUser();
                            fashionCarMarkeUser.setUserId(userId);
                            fashionCarMarkeUser.setPeriodsId(fashionHalfPriceCar.getPeriodsId());
                            fashionCarMarkeUser.setUserWxUnionId("oiYMg1" + getRandomString(22));
                            fashionCarMarkeUser.setUserWxOpenId("oCAThw" + getRandomString(22));
                            fashionCarMarkeUser.setNickName(stringStringMap.get("name"));
                            fashionCarMarkeUser.setUserWxHead(stringStringMap.get("pic"));
                            fashionCarMarkeUser.setUserPhone(stringStringMap.get("phone"));
                            fashionCarMarkeUser.setCityId(10);
                            fashionCarMarkeUser.setCreateDt(now);
                            fashionCarMarkeUserWriteMapper.insertSelective(fashionCarMarkeUser);
                        }
                    }
                    FashionCarMarkeWinNum winNum = new FashionCarMarkeWinNum();
                    winNum.setPeriodsId(fashionHalfPriceCar.getPeriodsId());
                    FashionCarMarkeWinNum fashionCarMarkeWinNumUpdate = fashionCarMarkeWinNumReadMapper.selectWinNum(fashionHalfPriceCar.getPeriodsId(), fashionHalfPriceCar.getWinningNumber());
                    if (fashionCarMarkeWinNumUpdate == null) {
                        FashionCarMarkeWinNum fashionCarMarkeWinNum = fashionCarMarkeWinNumReadMapper.selectNearestWinNum(fashionHalfPriceCar.getPeriodsId(), fashionHalfPriceCar.getWinningNumber());
                        winNum.setWinNum(fashionCarMarkeWinNum.getWinNum() > fashionHalfPriceCar.getWinningNumber() ? fashionCarMarkeWinNum.getWinNum() - 1 : fashionCarMarkeWinNum.getWinNum() + 1);
                    } else {
                        int cWinNum = changeWinNum(fashionHalfPriceCar.getWinningNumber());
                        fashionCarMarkeWinNumWriteMapper.updateByWinNum(fashionHalfPriceCar.getPeriodsId(), fashionHalfPriceCar.getWinningNumber(), cWinNum);
                        winNum.setWinNum(fashionHalfPriceCar.getWinningNumber());
                    }
                    winNum.setUserType(FashionCarUserType.HALF_PRICE_USER.getType());
                    winNum.setIsWin((byte) 1);
                    winNum.setBrandId(fashionHalfPriceCar.getBrandId());
                    winNum.setActivityDate(fashionHalfPriceCar.getActivityDate());
                    winNum.setUserId(userId);
                    winNum.setUpdateDt(now);
                    winNum.setCreateDt(now);
                    fashionCarMarkeWinNumWriteMapper.insertSelective(winNum);
                } catch (Exception e) {
                    CommonLogUtil.addError("潮车集-半价车", "开奖中奖码错误", e);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }

        }
    }

    private String getRandomString(int length) {
        String str = "zxcvbnmlkjhgfdsaqw_ertyuiopQWERTYUIOPASDF-GHJKLZXCVBNM1234567890";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(64);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    @Override
    public void processWinningNumber() {
            Date now = new Date();
            List<FashionHalfPriceCar> fashionHalfPriceCars = fashionHalfPriceCarReadMapper.selectActivityByDay(sdf.format(now));
            if (fashionHalfPriceCars.isEmpty()) {
                return;
            }
            LotteryDto lotteryDto = JSONObject.parseObject(loadJSON(), new TypeReference<LotteryDto>() {
            });
            if (lotteryDto == null) {
                return;
            }
            if (!"0".equals(lotteryDto.getErrorCode())) {
                return;
            }
            LotteryResultsDto lotteryResultsDto = lotteryDto.getValue();
            if (lotteryResultsDto == null) {
                return;
            }
            PlwDto plw = lotteryResultsDto.getPlw();
            if (plw == null) {
                return;
            }
            if (plw.getLotteryDrawTime() != null && plw.getLotteryDrawTime().startsWith(sdf.format(now))) {
                try {
                    int lotteryDrawResult = Integer.parseInt(plw.getLotteryDrawResult());
                    CommonLogUtil.addInfo("潮车集-半价车", "获取中奖码成功:" + lotteryDrawResult);
                    for (FashionHalfPriceCar fashionHalfPriceCar : fashionHalfPriceCars) {
                        if (!getSwitchStatus(fashionHalfPriceCar.getPeriodsId())) {
                            FashionCarMarkeWinNum fashionCarMarkeWinNum = fashionCarMarkeWinNumReadMapper.selectWinNum(fashionHalfPriceCar.getPeriodsId(), lotteryDrawResult);
                            if (fashionCarMarkeWinNum != null) {
                                Integer newNum = changeWinNum(lotteryDrawResult);
                                fashionCarMarkeWinNumWriteMapper.updateByWinNum(fashionHalfPriceCar.getPeriodsId(), lotteryDrawResult, newNum);
                            }
                        }
                    }
                } catch (Exception e) {
                    CommonLogUtil.addError("潮车集-半价车", "处理中奖码错误", e);
                }
            }

    }

    @Override
    public Integer changeWinNum(Integer winNum) {
        if (winNum < 10) {
            return winNum == 0 ? 1 : winNum - 1;
        }
        String winNumString = String.valueOf(winNum);
        String newWinNumString = null;

        if (winNumString.length() > 2) {
            newWinNumString = stringExchange(winNumString, 1, 2);
        } else {
            newWinNumString = stringExchange(winNumString, 0, 1);
        }
        if (winNumString.equals(newWinNumString)) {
            return winNum + 1;
        } else {
            return Integer.parseInt(newWinNumString);
        }
    }

    private String stringExchange(String winNum, int index1, int index2) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < winNum.length(); i++) {
            if (i == index1) {
                if (winNum.charAt(index1) == winNum.charAt(index2)) {
                    int n = Integer.parseInt(String.valueOf(winNum.charAt(index2))) + 1;
                    if (n > 9) {
                        n = 0;
                    }
                    sb.append(n);
                } else {
                    sb.append(winNum.charAt(index2));
                }
                continue;
            }
            if (i == index2) {
                sb.append(winNum.charAt(index1));
                continue;
            }
            sb.append(winNum.charAt(i));
        }
        return sb.toString();
    }

    private String loadJSON() {
        StringBuilder json = new StringBuilder();
        BufferedReader in = null;
        try {
            URL oracle = new URL("https://webapi.sporttery.cn/gateway/lottery/getDigitalDrawInfoV1.qry?param=0,0;350133,0&isVerify=1");
            URLConnection yc = oracle.openConnection();
            in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream(), "utf-8"));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }

        } catch (Exception e) {
            CommonLogUtil.addError("潮车集-半价车", "获取中奖码错误", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return json.toString();
    }

    public CarFashionInfoEntityResVo getCarFashionInfoEntityResVo(Integer periodsId) {
        ConsolParametersVo vo = new ConsolParametersVo(OnlineFestivalMethodEnum.API_QUERY_ACTIVITY_INFO);
        vo.setBusinessType(ConsolParametersVo.BUSINESS_TYPE.API_QUERY_ACTIVITY_INFO);
        CarFashionActivityInfoReqVo carFashionActivityInfoReqVo = new CarFashionActivityInfoReqVo();
        carFashionActivityInfoReqVo.setActivityId(periodsId);
        vo.setInfo("param", JSONObject.toJSONString(carFashionActivityInfoReqVo));
        ConsolParametersVo service = onlineFestivalFacadeService.service(vo);
        CarFashionInfoEntityResVo carFashionInfoEntityResVo = null;
        if ("0000".equals(service.getResCode())) {
            Map<String, Object> serviceMap = service.getMap();
            if (serviceMap != null) {
                String data = (String) serviceMap.get("data");
                if (data != null) {
                    carFashionInfoEntityResVo = JSONObject.parseObject(data, CarFashionInfoEntityResVo.class);
                }
            }
        }
        return carFashionInfoEntityResVo;
    }

    private boolean getSwitchStatus(Integer periodsId) {
        if (fashionCarWinNumSwitchOpen.contains(periodsId)) {
            return true;
        }
        if (fashionCarWinNumSwitchClose.contains(periodsId)) {
            return false;
        }
        return fashionCarWinNum;
    }

}
