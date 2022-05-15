package com.tuanche.web.api.fashioncar;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.tuanche.arch.util.DateUtils;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.directselling.api.FashionCarMarkeUserService;
import com.tuanche.directselling.api.FashionCarWinningNumberService;
import com.tuanche.directselling.api.FashionHalfPriceCarService;
import com.tuanche.directselling.dto.FashionCarMarkeWinNumResultDto;
import com.tuanche.directselling.dto.FashionCarWinningNumberDto;
import com.tuanche.directselling.enums.FashionCarUserType;
import com.tuanche.directselling.model.FashionCarMarkeUser;
import com.tuanche.directselling.model.FashionCarMarkeWinNum;
import com.tuanche.directselling.model.FashionHalfPriceCar;
import com.tuanche.directselling.utils.GlobalConstants;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.manubasecenter.api.CarBaseService;
import com.tuanche.manubasecenter.api.CityBaseService;
import com.tuanche.manubasecenter.dto.TcResponse;
import com.tuanche.storage.dto.brand.CarBrandDto;
import com.tuanche.web.service.CommonWebService;
import com.tuanche.web.service.PayServiceImpl;
import com.tuanche.web.util.DirectCommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@RequestMapping("/api/fashioncar/winning")
@RestController
public class FashionCarWinningNumberController {

    @Reference
    private FashionCarWinningNumberService fashionCarWinningNumberService;
    @Reference
    private FashionCarMarkeUserService fashionCarMarkeUserService;
    @Reference
    private CityBaseService cityBaseService;
    @Reference
    private CarBaseService carBaseService;
    @Reference
    FashionHalfPriceCarService fashionHalfPriceCarService;
    @Autowired
    private CommonWebService commonWebService;
    @Autowired
    private PayServiceImpl payServiceImpl;
    @Value("${fashion_half_price_winning_hour}")
    private Integer fashion_half_price_winning_hour;


    /**
      * @description : 中奖码列表
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/9/13 13:51
      */
    @RequestMapping("/getCodeList")
    @ResponseBody
    public TcResponse getCodeList (HttpServletRequest request, FashionCarMarkeWinNum markeWinNum) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FashionCarWinningNumberController","getCodeList",  "中奖码列表 start " +st, JSON.toJSONString(markeWinNum));
        if (markeWinNum == null || markeWinNum.getPeriodsId() == null) {
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "参数错误", st, 0);
        }
        List<FashionCarWinningNumberDto> winningNumbers = new ArrayList<>();
        try {
            winningNumbers = fashionCarWinningNumberService.getWinningNumbers(markeWinNum.getPeriodsId(), DirectCommonUtil.getMemberPoId(request));
            if (CollectionUtils.isEmpty(winningNumbers)) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "中奖码列表 空", st,
                        "FashionCarWinningNumberController","getCodeList", markeWinNum);
            }
        } catch (Exception e) {
            return DirectCommonUtil.addErrorLog("FashionCarWinningNumberController", "中奖码列表 error", e, st, JSON.toJSONString(markeWinNum));
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FashionCarWinningNumberController","getCodeList",  "中奖码列表 end " +st, JSON.toJSONString(winningNumbers));
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, winningNumbers);
    }

    /**
      * @description : 中奖人名单
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/9/13 14:07
      */
    @RequestMapping("/getUserList")
    @ResponseBody
    public TcResponse getUserList (FashionCarMarkeWinNum markeWinNum) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FashionCarWinningNumberController","getUserList",  "中奖人名单 start " +st, JSON.toJSONString(markeWinNum));
        if (markeWinNum == null || markeWinNum.getPeriodsId() == null) {
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "参数错误", st, 0);
        }
        List<FashionCarMarkeWinNumResultDto> resultList = new ArrayList<>();
        try {
            //总日期列表
            List<FashionHalfPriceCar> halfPriceCarList = fashionHalfPriceCarService.getHalfPriceCarListByPeriodsId(markeWinNum.getPeriodsId());
            //已开奖列表
            markeWinNum.setIsWin((byte) 1);
            List<FashionCarMarkeWinNum> list = fashionCarWinningNumberService.getFashionCarMarkeWinNumList(markeWinNum);
            List<Integer> userIds = new ArrayList<>();
            List<Integer> cbIds = new ArrayList<>();
            List<Date> activityDateList = new ArrayList<>();
            for (FashionHalfPriceCar halfPriceCar : halfPriceCarList) {
                FashionCarMarkeWinNumResultDto carMarkeWinNumResultDto = new FashionCarMarkeWinNumResultDto();
                carMarkeWinNumResultDto.setActivityDate(halfPriceCar.getActivityDate());
                carMarkeWinNumResultDto.setActivityDateMil(halfPriceCar.getActivityDate().getTime());
                carMarkeWinNumResultDto.setBrandId(halfPriceCar.getBrandId());
                for (FashionCarMarkeWinNum carMarkeWinNum : list) {
                        if(carMarkeWinNum.getIsWin() == 1 && halfPriceCar.getActivityDate().compareTo(carMarkeWinNum.getActivityDate())==0){
                            carMarkeWinNumResultDto.setWinning(true);
                            carMarkeWinNumResultDto.setUserId(carMarkeWinNum.getUserId());
                            userIds.add(carMarkeWinNumResultDto.getUserId());
                            activityDateList.add(carMarkeWinNumResultDto.getActivityDate());
                            carMarkeWinNumResultDto.setWinNum(carMarkeWinNum.getWinNum());
                            carMarkeWinNumResultDto.setWinNumStr(carMarkeWinNum.getWinNumStr());
                            break;
                        }
                }
                cbIds.add(carMarkeWinNumResultDto.getBrandId());
                resultList.add(carMarkeWinNumResultDto);
            }
            Map<String, FashionHalfPriceCar> map = new HashMap<>();
            if (CollectionUtils.isNotEmpty(activityDateList)) {
                map = fashionHalfPriceCarService.getHalfPriceCarListByPeriodsIdAndDateList(markeWinNum.getPeriodsId(), activityDateList);
            }
            Map<Integer, FashionCarMarkeUser> userMap = fashionCarMarkeUserService.getKyeUserIdMapByUserId(userIds, markeWinNum.getPeriodsId());
            Map<Integer, CarBrandDto> brandMap = carBaseService.getNewBrandMap(cbIds);
            for (FashionCarMarkeWinNumResultDto v : resultList) {
                if (v.isWinning()) {
                    FashionHalfPriceCar priceCar = map.get(markeWinNum.getPeriodsId() + "-" + DateUtils.dateToString(v.getActivityDate(), DateUtils.YYYY_MM_DD));
                    if (priceCar!=null && priceCar.getWinningNumber()!=null) v.setWinningNumberStr(GlobalConstants.StringFormat(5, priceCar.getWinningNumber()));
                }
                if (userMap.get(v.getUserId()) != null) {
                    FashionCarMarkeUser user = userMap.get(v.getUserId());
                    v.setUserName(user.getNickName());
                    v.setUserPic(user.getUserWxHead());
                    if (!StringUtils.isEmpty(user.getUserPhone())) {
                        v.setUserPhone(user.getUserPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
                    }
                }
                if (brandMap.get(v.getBrandId())!=null) {
                    v.setCbName(brandMap.get(v.getBrandId()).getCbName());
                    v.setCbLogo(brandMap.get(v.getBrandId()).getCbLogo());
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(v.getActivityDate());
                cal.add(Calendar.HOUR, fashion_half_price_winning_hour);
                v.setActivityDateTime(cal.getTimeInMillis());
            }
        } catch (Exception e) {
            return DirectCommonUtil.addErrorLog("FashionCarWinningNumberController", "中奖人名单 error", e, st, JSON.toJSONString(markeWinNum));
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FashionCarWinningNumberController", "getUserList", "中奖人名单 end " + st, JSON.toJSONString(resultList));
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, resultList);
    }

    /**
     * @return com.tuanche.manubasecenter.dto.TcResponse
     * @description 半价车抽奖码
     * @date 2021/9/28 15:52
     * @author lvsen
     */
    @RequestMapping("/getHalfPriceWinNum")
    @ResponseBody
    public TcResponse getHalfPriceWinNum(HttpServletRequest request, FashionCarMarkeWinNum markeWinNum) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FashionCarWinningNumberController", "getHalfPriceWinNum", "半价车=获取半价车抽奖码 start " + st, JSON.toJSONString(markeWinNum));
        if (markeWinNum.getPeriodsId() == null) {
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "参数错误", st, 0);
        }
        String winNumStr = "";
        Integer userId = DirectCommonUtil.getMemberPoId(request);
        String lockKey = "fashion:car:marke:winning:lock:" + markeWinNum.getPeriodsId() + ":" + userId + ":" + FashionCarUserType.HALF_PRICE_USER.getType();
        String requestId = UUID.randomUUID().toString();
        if (!payServiceImpl.tryGetDistributedLock(lockKey, requestId, 120 * 1000)) {
            return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "正在抽奖，请稍后", st,
                    "FashionCarMarkeHelperUserController", "addHelperUser", "");
        }
        try {
            FashionCarMarkeWinNum carMarkeWinNum = new FashionCarMarkeWinNum();
            carMarkeWinNum.setUserId(userId);
            carMarkeWinNum.setPeriodsId(markeWinNum.getPeriodsId());
            carMarkeWinNum.setUserType(FashionCarUserType.HALF_PRICE_USER.getType());
            List<FashionCarMarkeWinNum> halfPriceWinNumList = fashionCarWinningNumberService.getFashionCarMarkeWinNumList(carMarkeWinNum);
            if (CollectionUtils.isNotEmpty(halfPriceWinNumList)) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "您已抽过中奖码", st,
                        "FashionCarWinningNumberController","getHalfPriceWinNum", markeWinNum);
            }
            winNumStr = fashionCarWinningNumberService.createWinningNumber(markeWinNum.getPeriodsId(), userId, null, FashionCarUserType.HALF_PRICE_USER.getType());
        } catch (Exception e) {
            return DirectCommonUtil.addErrorLog("FashionCarWinningNumberController", "半价车=获取半价车抽奖码 error", e, st, JSON.toJSONString(markeWinNum));
        } finally {
            payServiceImpl.releaseDistributedLock(lockKey, requestId);
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FashionCarWinningNumberController", "getHalfPriceWinNum", "半价车=获取半价车抽奖码 end " + st, JSON.toJSONString(winNumStr));
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, winNumStr);
    }
}
