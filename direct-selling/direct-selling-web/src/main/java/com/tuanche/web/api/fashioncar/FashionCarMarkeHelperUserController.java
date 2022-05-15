package com.tuanche.web.api.fashioncar;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.consol.dubbo.OnlineFestivalFacadeService;
import com.tuanche.consol.dubbo.bean.ConsolConstants;
import com.tuanche.consol.dubbo.bean.ConsolParametersVo;
import com.tuanche.consol.dubbo.enums.OnlineFestivalMethodEnum;
import com.tuanche.consol.dubbo.vo.carFashion.CarFashionInfoEntityResVo;
import com.tuanche.directselling.api.FashionCarMarkeHelperUserService;
import com.tuanche.directselling.api.FashionCarMarkeUserService;
import com.tuanche.directselling.api.FashionCarWinningNumberService;
import com.tuanche.directselling.dto.FashionCarMarkeHelperUserDto;
import com.tuanche.directselling.enums.FashionCarUserType;
import com.tuanche.directselling.exception.WinningNumberException;
import com.tuanche.directselling.model.FashionCarMarkeHelperUser;
import com.tuanche.directselling.model.FashionCarMarkeUser;
import com.tuanche.directselling.utils.FashionCarMarkeConstants;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.manubasecenter.dto.TcResponse;
import com.tuanche.web.service.CommonWebService;
import com.tuanche.web.service.FashionCommonService;
import com.tuanche.web.service.PayServiceImpl;
import com.tuanche.web.util.DirectCommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RequestMapping("/api/fashioncar/helper")
@RestController
public class FashionCarMarkeHelperUserController {

    @Reference
    private FashionCarMarkeHelperUserService fashionCarMarkeHelperUserService;
    @Reference
    private FashionCarMarkeUserService fashionCarMarkeUserService;
    @Reference
    private OnlineFestivalFacadeService onlineFestivalFacadeService;
    @Reference
    private FashionCarWinningNumberService fashionCarWinningNumberService;
    @Autowired
    private FashionCommonService fashionCommonService;
    @Autowired
    private CommonWebService commonWebService;
    @Autowired
    private PayServiceImpl payServiceImpl;

    /**
     * @description : 获取助力人列表
     * @param :
     * @return :
     * @author : fxq
     * @date : 2020/11/18 14:52
     */
    @RequestMapping("/getHelperUserList")
    @ResponseBody
    public TcResponse getHelperUserList (HttpServletRequest request, FashionCarMarkeHelperUser helperUser) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FashionCarMarkeHelperUserController","getHelperUserList",  "潮车集获取助力人列表 start " +st, JSON.toJSONString(helperUser));
        List<FashionCarMarkeHelperUserDto> helperList = new ArrayList<>();
        if (helperUser.getUserId()==null) helperUser.setUserId(DirectCommonUtil.getMemberPoId(request));
        if (helperUser==null || helperUser.getPeriodsId()==null || helperUser.getHelperType()==null
                || (helperUser.getHelperType().equals(FashionCarMarkeConstants.HelperType.CAR_COUPON.getCode()) && helperUser.getGoodsId()==null)
                || (helperUser.getUserWxUnionId()==null && helperUser.getUserId()==null)) {
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "参数错误", st, 0);
        }
        try {
            helperUser.setBuyFlag(FashionCarMarkeConstants.HelperBuyFlag.HAVE_NOT_PURCHASED.getCode());
            helperUser.setGrantWinNumFlag(FashionCarMarkeConstants.GrantWinNumFlag.GRANT.getCode());
            List<FashionCarMarkeHelperUser> list = fashionCarMarkeHelperUserService.getFashionCarMarkeHelperUserList(helperUser);
            if (CollectionUtils.isEmpty(list)) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "获取助力人列表 空", st,
                        "FashionCarMarkeHelperUserController","getHelperUserList", list);
            }
            List<FashionCarMarkeUser> userList = new ArrayList<>();
            list.forEach(v->{
                FashionCarMarkeUser u = new FashionCarMarkeUser();
                u.setUserId(v.getHelperUserId());
                u.setUserWxUnionId(v.getHelperWxUnionId());
                userList.add(u);
            });
            Map<String, FashionCarMarkeUser> map = fashionCarMarkeUserService.getKyeUserIdUnionIdMapByList(userList, null);
            for (FashionCarMarkeHelperUser v : list) {
                FashionCarMarkeHelperUserDto userDto = new FashionCarMarkeHelperUserDto();
                FashionCarMarkeUser markeUser = map.get(v.getHelperUserId()+"");
                if (markeUser!=null) {
                    v.setHelperWxHead(markeUser.getUserWxHead());
                    v.setHelperWxNick(markeUser.getNickName());
                }
                BeanUtils.copyProperties(v,userDto);
                FashionCarMarkeHelperUser carMarkeHelperUser = new FashionCarMarkeHelperUser();
                carMarkeHelperUser.setBuyFlag(FashionCarMarkeConstants.HelperBuyFlag.HAVE_NOT_PURCHASED.getCode());
                carMarkeHelperUser.setUserId(v.getUserId());
                carMarkeHelperUser.setPeriodsId(v.getPeriodsId());
                carMarkeHelperUser.setHelperType(FashionCarMarkeConstants.HelperType.SEMIVALENT_CAR.getCode());
                userDto.setHelperCount(fashionCarMarkeHelperUserService.getFashionCarMarkeHelperUserCount(helperUser));
                helperList.add(userDto);
            }
        } catch (Exception e) {
            return DirectCommonUtil.addErrorLog("FashionCarMarkeHelperUserController", "潮车集获取助力人列表 error", e, st, JSON.toJSONString(helperUser));
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FashionCarMarkeHelperUserController","getHelperUserList",  "潮车集获取助力人列表 end " +st, JSON.toJSONString(helperList));
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, helperList);
    }

    /**
     * @description : 获取已经助力人数
     * @param :
     * @return :
     * @author : fxq
     * @date : 2020/11/18 14:52
     */
    @RequestMapping("/getHelperUserNum")
    @ResponseBody
    public TcResponse getHelperUserNum (HttpServletRequest request, FashionCarMarkeHelperUser helperUser) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FashionCarMarkeHelperUserController","getHelperUserNum",  "潮车集获取助力人数 start " +st, JSON.toJSONString(helperUser));
        int num = 0;
        if (helperUser.getUserId()==null) helperUser.setUserId(commonWebService.getMemberPoIdByToken(request));
        if (helperUser==null || helperUser.getPeriodsId()==null || helperUser.getHelperType()==null
                || (helperUser.getHelperType().equals(FashionCarMarkeConstants.HelperType.CAR_COUPON.getCode()) && helperUser.getGoodsId()==null)
                || (helperUser.getUserWxUnionId()==null && helperUser.getUserId()==null)) {
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "参数错误", st, 0);
        }
        try {
            helperUser.setBuyFlag(FashionCarMarkeConstants.HelperBuyFlag.HAVE_NOT_PURCHASED.getCode());
            num = fashionCarMarkeHelperUserService.getFashionCarMarkeHelperUserCount(helperUser);
        } catch (Exception e) {
            return DirectCommonUtil.addErrorLog("FashionCarMarkeHelperUserController", "潮车集获取助力人数 error", e, st, JSON.toJSONString(helperUser));
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FashionCarMarkeHelperUserController","getHelperUserNum",  "潮车集获取助力人数 end " +st, num);
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, num);
    }

    /**
     * @description : 潮车集获取用户是否助力过该商品（活动维度）
     * @param :
     * @return : 0未主力
     * @return : 1已助力此商品
     * @return : 2已助力其他商品
     * @author : fxq
     * @date : 2020/11/18 14:52
     */
    @RequestMapping("/getHelperInfo")
    @ResponseBody
    public TcResponse getHelperInfo (HttpServletRequest request, FashionCarMarkeHelperUser helperUser) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FashionCarMarkeHelperUserController","getHelperGoodsId",  "潮车集获取用户是否助力过该商品 start " +st, JSON.toJSONString(helperUser));
        if (helperUser.getHelperUserId()==null) helperUser.setHelperUserId(commonWebService.getMemberPoIdByToken(request));
        if (helperUser==null || helperUser.getPeriodsId()==null || helperUser.getHelperType()==null
                || (helperUser.getHelperType().equals(FashionCarMarkeConstants.HelperType.CAR_COUPON.getCode()) && helperUser.getGoodsId()==null)
                || (StringUtil.isEmpty(helperUser.getHelperWxUnionId()) && helperUser.getHelperUserId()==null)
                || (StringUtil.isEmpty(helperUser.getUserWxUnionId()) && helperUser.getUserId()==null)
        ) {
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "参数错误", st, 0);
        }
        FashionCarMarkeHelperUserDto helperUserDto = new FashionCarMarkeHelperUserDto();
        helperUserDto.setUserId(helperUser.getUserId());
        helperUserDto.setUserWxUnionId(helperUser.getUserWxUnionId());
        try {
            //发起助力用户信息
            FashionCarMarkeUser markeUser = new FashionCarMarkeUser();
//            markeUser.setPeriodsId(helperUser.getPeriodsId());
            markeUser.setUserId(helperUser.getUserId());
            markeUser.setUserWxUnionId(helperUser.getUserWxUnionId());
            List<FashionCarMarkeUser> markeUsers = fashionCarMarkeUserService.getUserListToOr(markeUser);
            if (CollectionUtils.isNotEmpty(markeUsers)) {
                helperUserDto.setUserWxHead(markeUsers.get(0).getUserWxHead());
                helperUserDto.setUserWxNick(markeUsers.get(0).getNickName());
            }
            //助力用户是否已助力
            FashionCarMarkeHelperUser goodsHelperUser = new FashionCarMarkeHelperUser();
            goodsHelperUser.setPeriodsId(helperUser.getPeriodsId());
            goodsHelperUser.setHelperType(helperUser.getHelperType());
            goodsHelperUser.setHelperWxUnionId(helperUser.getHelperWxUnionId());
            goodsHelperUser.setHelperUserId(helperUser.getHelperUserId());
            List<FashionCarMarkeHelperUser> helperUserList = fashionCarMarkeHelperUserService.getFashionCarMarkeHelperUserList(goodsHelperUser);
            if (CollectionUtils.isNotEmpty(helperUserList)) {
                if (helperUserList.size()>1) {
                    return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "潮车集获取助力信息异常", st,
                            "FashionCarMarkeHelperUserController","getFashionCarMarkeHelperUserList", helperUser);
                }
                if ((FashionCarMarkeConstants.HelperType.CAR_COUPON.getCode().equals(helperUser.getHelperType()) && helperUser.getGoodsId().equals(helperUserList.get(0).getGoodsId()))
                        && ((!StringUtil.isEmpty(helperUser.getUserWxUnionId()) && helperUser.getUserWxUnionId().equals(helperUserList.get(0).getUserWxUnionId())) ||
                        (helperUser.getUserId()!=null && helperUser.getUserId().equals(helperUserList.get(0).getUserId())))) {
                    helperUserDto.setHelperFlag(1);
                } else {
                    helperUserDto.setHelperFlag(2);
                }
            }
        } catch (Exception e) {
            return DirectCommonUtil.addErrorLog("FashionCarMarkeHelperUserController", "潮车集获取用户是否助力过该商品（活动维度） error", e, st, JSON.toJSONString(helperUser));
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FashionCarMarkeHelperUserController","getHelperGoodsId",  "潮车集获取用户是否助力过该商品 end " +st, "");
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, helperUserDto);
    }

    /**
     * @description : 添加助力人
     * @param :
     * @return :
     * @author : fxq
     * @date : 2020/11/18 14:52
     */
    @RequestMapping("/addHelperUser")
    @ResponseBody
    public TcResponse addHelperUser (HttpServletRequest request, FashionCarMarkeHelperUser helperUser) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FashionCarMarkeHelperUserController","addHelperUser",  "潮车集添加助力人 start " +st, JSON.toJSONString(helperUser));
        if (helperUser.getHelperUserId()==null) helperUser.setHelperUserId(commonWebService.getMemberPoIdByToken(request));
        if (helperUser==null || helperUser.getPeriodsId()==null || helperUser.getHelperType()==null
                || (helperUser.getHelperType().equals(FashionCarMarkeConstants.HelperType.CAR_COUPON.getCode()) && helperUser.getGoodsId()==null)
                || helperUser.getUserId()==null
                || (helperUser.getHelperWxUnionId()==null && helperUser.getHelperUserId()==null)
                || (helperUser.getHelperWxUnionId()!=null && helperUser.getUserWxUnionId()!=null && helperUser.getUserWxUnionId().equals(helperUser.getHelperWxUnionId()))) {
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "参数错误", st, JSON.toJSONString(helperUser));
        }
        if (!FashionCarMarkeConstants.HelperType.CAR_COUPON.getCode().equals(helperUser.getHelperType()) && helperUser.getUserId() == null) {
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "当前无法助力，请让您的好友登录活动后再次分享", st, JSON.toJSONString(helperUser));
        }
        String lockKey = "fashion:car:marke:helper:lock:" +helperUser.getPeriodsId()+ ":" + helperUser.getUserId()+":"+helperUser.getHelperUserId()+":"+helperUser.getHelperWxUnionId()+":"+helperUser.getGoodsId();
        String requestId = UUID.randomUUID().toString();
        if (!payServiceImpl.tryGetDistributedLock(lockKey, requestId, 120 * 1000)) {
            return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "正在助力，请稍后", st,
                    "FashionCarMarkeHelperUserController", "addHelperUser", "");
        }
        String code = "";
        try {
            //活动是否过期
            TcResponse acticityDateResponse = verifyActivityDate (helperUser, st);
            if (acticityDateResponse!=null) {
                return acticityDateResponse;
            }
            FashionCarMarkeHelperUser user = new FashionCarMarkeHelperUser();
            user.setPeriodsId(helperUser.getPeriodsId());
            user.setHelperType(helperUser.getHelperType());
            user.setHelperWxUnionId(helperUser.getHelperWxUnionId());
            user.setHelperUserId(helperUser.getHelperUserId());
            int count = fashionCarMarkeHelperUserService.getFashionCarMarkeHelperUserCount(user);
            if (count >0) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "该场活动您已助力", st,
                        "FashionCarMarkeHelperUserController","addHelperUserList", helperUser);
            }
            helperUser.setBuyFlag(FashionCarMarkeConstants.HelperBuyFlag.HAVE_NOT_PURCHASED.getCode());
            int id = fashionCarMarkeHelperUserService.addFashionCarMarkeHelperUser(helperUser);
            if (id==0) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "助力失败", st,
                        "FashionCarMarkeHelperUserController","addHelperUserList", helperUser);
            }
            if (helperUser.getHelperType().equals(FashionCarMarkeConstants.HelperType.SEMIVALENT_CAR.getCode())) {
                //发起助力人生成中奖码
                try {
                    fashionCarWinningNumberService.createWinningNumber(helperUser.getPeriodsId(), helperUser.getUserId(), id, FashionCarUserType.HELPED.getType());
                } catch (WinningNumberException e) {
                    StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FashionCarMarkeHelperUserController", "addHelperUser", "发起助力人生成中奖码失败" + st, JSON.toJSONString(e));
//                    return DirectCommonUtil.setTcResponse(StatusCodeEnum.CREATE_FAIL.getCode(), e.getMessage(), st, code);
                }
                try {
                    //助力人生成中奖码
                    code = fashionCarWinningNumberService.createWinningNumber(helperUser.getPeriodsId(), helperUser.getHelperUserId(), id, FashionCarUserType.HELPER.getType());
                } catch (WinningNumberException e) {
                    StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FashionCarMarkeHelperUserController", "addHelperUser", "助力人生成中奖码失败" + st, JSON.toJSONString(e));
                    if (!StringUtil.isEmpty(e.getCode()) && e.getCode().equals(StatusCodeEnum.WIN_NUM_ENOUGH_ERROR.getCode()+"")) {
                        return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "您的中奖码已达到最大值，请等待中奖结果", st,
                                "FashionCarMarkeHelperUserController","addHelperUserList", helperUser);
                    }
                    return DirectCommonUtil.setTcResponse(StatusCodeEnum.CREATE_FAIL.getCode(), e.getMessage(), st, code);
                }
                //现金红包增加抽奖次数
            } else if (helperUser.getHelperType().equals(FashionCarMarkeConstants.HelperType.RED_PACKET.getCode())) {
                ConsolParametersVo vo = new ConsolParametersVo("-1", "1", OnlineFestivalMethodEnum.API_LOTTERY_CHANCE_ADD);
                Map<String, Object> map = new HashMap<>();
                map.put("activityId", helperUser.getPeriodsId());
                map.put("userId", helperUser.getUserId());
                map.put("addRepeat", 1);
                vo.setInfo("param", JSONObject.toJSONString(map));
                ConsolParametersVo service = onlineFestivalFacadeService.service(vo);
                if(ConsolConstants.RESULT_SUCCESS.equals(service.getResCode())) {
                    Map<String, Object> result = JSON.parseObject(service.getString("data"), Map.class);
                    Integer operateResult = (Integer) result.get("operateResult");
                    if (operateResult.equals(0)) {
                        return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "抽奖机会获取失败", st,
                                "FashionCarMarkeHelperUserController","addHelperUserList", helperUser);
                    }
                } else {
                    return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "抽奖机会返回："+service.getResDesc(), st,
                            "FashionCarMarkeHelperUserController","addHelperUserList", helperUser);
                }
            }
        } catch (Exception e) {
            return DirectCommonUtil.addErrorLog("FashionCarMarkeHelperUserController", "潮车集获取助力人列表 error", e, st, JSON.toJSONString(helperUser));
        } finally {
            payServiceImpl.releaseDistributedLock(lockKey, requestId);
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FashionCarMarkeHelperUserController","addHelperUser",  "潮车集添加助力人 end " +st, "");
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, code);
    }

    private TcResponse verifyActivityDate (FashionCarMarkeHelperUser helperUser, long st) {
        CarFashionInfoEntityResVo carFashionInfoEntityResVo = fashionCommonService.getCarFashionInfoEntityResVo(helperUser.getPeriodsId());
        Date date= new Date();
        if (carFashionInfoEntityResVo==null) {
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "活动异常，请稍后再试", st, JSON.toJSONString(helperUser));
        }
        if (date.before(carFashionInfoEntityResVo.getHeadDate())) {
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "活动未开始", st, JSON.toJSONString(helperUser));
        }
        if (date.after(carFashionInfoEntityResVo.getFormalDateEnd())) {
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "活动已结束", st, JSON.toJSONString(helperUser));
        }
        return null;
    }

}
