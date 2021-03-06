package com.tuanche.web.api.aftersale.v2;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.tuanche.arch.util.IPUtil;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.api.*;
import com.tuanche.directselling.dto.*;
import com.tuanche.directselling.model.AfterSaleActivityAccount;
import com.tuanche.directselling.model.AfterSaleOrderRecord;
import com.tuanche.directselling.utils.AfterSaleConstants;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.AfterSaleActivitySimpleVo;
import com.tuanche.directselling.vo.AfterSaleActivityVo;
import com.tuanche.directselling.vo.CreatePosterVo;
import com.tuanche.framework.base.constant.CommonCode;
import com.tuanche.framework.base.entity.DataTransferObject;
import com.tuanche.web.api.BaseController;
import com.tuanche.web.util.CommonLogUtil;
import com.tuanche.web.util.DirectCommonUtil;
import com.tuanche.web.util.Globals;
import com.tuanche.web.util.ParameterUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;

import static com.tuanche.directselling.utils.AfterSaleConstants.SubAccountVerifyState.STATE_111;

/**
 * @author lvsen
 * @description
 * @date 2021/7/20 16:20
 */
@RequestMapping("/api/v2/afterSale")
@RestController
public class AfterSaleActivityFont2Controller extends BaseController {

    @Reference
    AfterSaleActivityService afterSaleActivityService;
    @Reference
    private AfterSaleUserService afterSaleUserService;
    @Reference
    private AfterSaleOrderRecordService afterSaleOrderRecordService;
    @Reference
    private AfterSaleActivityAccountService accountService;
    @Autowired
    ExecutorService executorService;
    /**
     * ????????????????????????
     *
     * @param request
     * @param activityVo
     * @return
     */
    @RequestMapping(value = "/getActivityInfoForApi", method = RequestMethod.POST)
    public TcResponse getActivityInfoForApi(HttpServletRequest request, @RequestBody AfterSaleActivityVo activityVo) {
        CommonLogUtil.addInfo("????????????????????????-???????????????????????? start", "?????????", JSON.toJSONString(activityVo));
        if (Objects.isNull(activityVo) || Objects.isNull(activityVo.getActivityId())) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "????????????");
        }
        if (StringUtil.isEmpty(activityVo.getUserWxUnionId())) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "UserWxUnionId??????");
        }
        if (StringUtil.isEmpty(activityVo.getUserWxOpenId())) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "UserWxOpenId??????");
        }
        if (StringUtil.isEmpty(activityVo.getUserWxHead())) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "??????????????????");
        }
        if (StringUtil.isEmpty(activityVo.getNickName())) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "??????????????????");
        }
        saveBrowseData(request, activityVo);
        return afterSaleActivityService.getAfterSaleActivityInfoForApi(activityVo);
    }

    /**
     * ??????????????????
     *
     * @param request
     * @param activityVo
     */
    private void saveBrowseData(HttpServletRequest request, AfterSaleActivityVo activityVo) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                AfterSaleUserBrowseDto afterSaleUserBrowseDto = new AfterSaleUserBrowseDto();
                afterSaleUserBrowseDto.setActivityId(activityVo.getActivityId());
                afterSaleUserBrowseDto.setAgentWxUnionId(activityVo.getAgentWxUnionId());
                afterSaleUserBrowseDto.setShareWxUnionId(activityVo.getShareWxUnionId());
                afterSaleUserBrowseDto.setNickName(activityVo.getNickName());
                afterSaleUserBrowseDto.setUserWxUnionId(activityVo.getUserWxUnionId());
                afterSaleUserBrowseDto.setUserWxOpenId(activityVo.getUserWxOpenId());
                afterSaleUserBrowseDto.setUserWxHead(activityVo.getUserWxHead());
                String referer = request.getHeader("Referer");
                afterSaleUserBrowseDto.setPageUrl(referer);
                String ip = IPUtil.getRequestIp(request);
                afterSaleUserBrowseDto.setIp(ip);
                afterSaleUserService.userBrowse(afterSaleUserBrowseDto);
            }
        });
    }

    /**
     * ???????????????????????????
     *
     * @param request
     * @param activityApiVo
     * @return
     */
    @RequestMapping(value = "/getActivityDealerList")
    public TcResponse getActivityDealerList(HttpServletRequest request, @RequestBody AfterSaleActivityVo activityApiVo) {
        return afterSaleActivityService.getAfterSaleActivityDealerList(activityApiVo);
    }

    /**
     * ????????????
     *
     * @param request
     * @param activityApiVo
     * @return
     */
    @RequestMapping(value = "/getActivityList")
    public TcResponse getActivityList(HttpServletRequest request, @RequestBody AfterSaleActivityVo activityApiVo) {
        return afterSaleActivityService.getAfterSaleActivityList(activityApiVo);
    }

    /**
     * ???????????????
     *
     * @return
     */
    @PostMapping("/createPoster")
    public TcResponse createPoster(HttpServletRequest request, @RequestBody CreatePosterVo createPosterVo) {
        long st = System.currentTimeMillis();
        if (createPosterVo == null || StringUtil.isEmpty(createPosterVo.getShareUrl())) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "????????????????????????");
        }
        if (StringUtils.isEmpty(createPosterVo.getPosterUrl())) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "?????????????????????");
        }
        String postUrl = afterSaleActivityService.createPostUrl(createPosterVo.getShareUrl(), createPosterVo.getPosterUrl());

        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), System.currentTimeMillis() - st, StatusCodeEnum.SUCCESS.getText(),
                (Object)(StringUtil.isEmpty(postUrl) ? createPosterVo.getPosterUrl() : postUrl));
    }

    /**
     *  ????????????
     *
     * @param request
     * @param activityVo
     * @return
     */
    @RequestMapping(value = "/getSaleRewardSlideshow", method = RequestMethod.POST)
    public TcResponse getSaleRewardSlideshow(HttpServletRequest request, AfterSaleActivitySimpleVo activityVo) {
        long st = System.currentTimeMillis();
        CommonLogUtil.addInfo("????????????????????????-???????????????????????? start", "?????????", JSON.toJSONString(activityVo));
        if (Objects.isNull(activityVo) || Objects.isNull(activityVo.getActivityId())) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "????????????");
        }
        List<AfterSaleRewardSlideshowDto> saleRewardSlideshowList = afterSaleActivityService.getSaleRewardSlideshow(activityVo.getActivityId());
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), System.currentTimeMillis() - st, StatusCodeEnum.SUCCESS.getText(), saleRewardSlideshowList);
    }

    /**
     * @description : ???????????????
     * @author : lvsen
     */
    @RequestMapping(value = "/redPackList", method = RequestMethod.POST)
    public com.tuanche.manubasecenter.dto.TcResponse redPackList(PageResult<AfterSaleRedPacketListDto> pageResult, AfterSaleOrderRecord afterSaleOrderRecord) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "AfterSaleActivityFont2Controller", "redPackList", "????????????????????? start ", JSON.toJSONString(afterSaleOrderRecord));
        PageResult<AfterSaleRedPacketListDto> pageList = new PageResult<>();
        if (Objects.isNull(afterSaleOrderRecord) || Objects.isNull(afterSaleOrderRecord.getActivityId())) {
            return DirectCommonUtil.returnResultNull("AfterSaleActivityFont2Controller", "redPackList", "?????????????????????", pageList, st);
        }
        try {
            pageList = afterSaleOrderRecordService.getAfterSaleRedPackListByPage(pageResult, afterSaleOrderRecord);
        } catch (Exception e) {
            return DirectCommonUtil.addErrorLog("AfterSaleActivityFont2Controller", "????????????????????? error", e, st, JSON.toJSONString(afterSaleOrderRecord));
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "AfterSaleActivityFont2Controller", "redPackList = " + JSON.toJSONString(pageList), "????????????????????? end ", System.currentTimeMillis() - st);
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, pageList);
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @RequestMapping(value = "/getRedPacketListByUser", method = RequestMethod.POST)
    public com.tuanche.manubasecenter.dto.TcResponse  getRedPacketListByUser(PageResult<AfterSaleRedPacketDto> pageResult, AfterSaleActivityVo activityVo) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "AfterSaleActivityFont2Controller", "getRedPacketListByUser", "?????????????????????????????? start ", JSON.toJSONString(activityVo));
        PageResult<AfterSaleRedPacketDto> pageList = new PageResult<>();
        if (Objects.isNull(activityVo) || Objects.isNull(activityVo.getActivityId()) || Objects.isNull(activityVo.getUserWxUnionId())) {
            return DirectCommonUtil.returnResultNull("AfterSaleActivityFont2Controller", "getRedPacketListByUser", "??????????????????????????????", pageList, st);
        }
        try {
            pageList = afterSaleOrderRecordService.getSaleRedPacketsByUser(pageResult, activityVo);
        } catch (Exception e) {
            return DirectCommonUtil.addErrorLog("AfterSaleActivityFont2Controller", "????????????????????? error", e, st, JSON.toJSONString(activityVo));
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "AfterSaleActivityFont2Controller", "getRedPacketListByUser", "?????????????????????????????? end = " + JSON.toJSONString(pageList), System.currentTimeMillis() - st);
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, pageList);
    }

    /**
     *  ??????????????????????????????
     * @param request
     * @return
     */
    @RequestMapping(value = "/subAccountNotice")
    @ResponseBody
    public String subAccountNotice(HttpServletRequest request) {
        CommonLogUtil.addInfo("?????????????????????????????????","","starting ----");
        final DataTransferObject<List<Map<String, Object>>> dto = new DataTransferObject<List<Map<String, Object>>>();
        try {
            String result = ParameterUtil.getBodyHeadMapFromDesForInter2SubAccount(request);
            CommonLogUtil.addInfo("?????????????????????????????????","","???????????????" + result);
            if (result != null) {
                List<OrderBackInfo> list = JSON.parseArray(result, OrderBackInfo.class);
                CommonLogUtil.addInfo("?????????????????????????????????", "????????????", JSON.toJSONString(list));
                //??????  ?????????????????????
                final List<Map<String, Object>> rstList = new ArrayList<Map<String, Object>>();
                for (OrderBackInfo backInfo : list) {
                    Map<String, Object> map = updateAccoutVerifyState(backInfo);
                    if ("10000".equals(map.get("code").toString())) {
                        rstList.add(map);
                    }
                }
                dto.setData(rstList);
                String returnInfo = ParameterUtil.encrypt(dto.toString());
                return returnInfo;
            } else {
                dto.setErrCode(CommonCode.PAR_EXCEPTION.getCode());
                dto.setMsg(CommonCode.PAR_EXCEPTION.getMsg());
                return ParameterUtil.encrypt(dto.toString());
            }
        } catch (Exception e) {
            StaticLogUtils.error(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "AfterSaleActivityController", "subAccountNotice", "??????????????????????????????error", e);
            dto.setErrCode(CommonCode.PAR_EXCEPTION.getCode());
            dto.setMsg(CommonCode.PAR_EXCEPTION.getMsg());
            return ParameterUtil.encrypt(dto.toString());
        }
    }


    /**
     * ??????
     * @param backInfo
     * @return
     */
    public Map<String, Object> updateAccoutVerifyState(OrderBackInfo backInfo) {
        CommonLogUtil.addInfo("?????????????????????????????????", "??????backInfo", JSON.toJSONString(backInfo));
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 10001);
        resultMap.put("id", backInfo.getId());
        AfterSaleActivityAccount account = new AfterSaleActivityAccount();
        String updateFlag = "";
        if (backInfo.getBizId().startsWith("1_")) {
            updateFlag = "pay";
            account.setWxTransactionId(backInfo.getBizId());
        } else if (backInfo.getBizId().startsWith("2_")) {
            updateFlag = "refund";
            account.setRefundId(backInfo.getBizId());
        }
        List<AfterSaleActivityAccount> activityAccountList = accountService.getAfterSaleActivityAccount(account);
        if (CollectionUtils.isEmpty(activityAccountList)) {
            //??? orderTradeId ????????????
            CommonLogUtil.addInfo("?????????????????????????????????", "?????????????????????????????? ????????????", JSON.toJSONString(backInfo));
            resultMap.put("code", 10001);
            resultMap.put("id", backInfo.getId());
            return resultMap;
        }
        //??????
        for (AfterSaleActivityAccount activityAccount : activityAccountList) {
            if ("pay".equals(updateFlag)) {
                if ("10000".equals(backInfo.getStatus())) {
                    activityAccount.setVerifyState(AfterSaleConstants.SubAccountVerifyState.STATE_100.getCode());
                    activityAccount.setFailReason("");
                    activityAccount.setSubAccountTransactionDetailId(backInfo.getSerialNo());
                    resultMap.put("code", 10000);
                } else {
                    activityAccount.setVerifyState(AfterSaleConstants.SubAccountVerifyState.STATE_200.getCode());
                    activityAccount.setFailReason(backInfo.getStatusDesc());
                }
            } else if ("refund".equals(updateFlag)) {
                if ("10000".equals(backInfo.getStatus())) {
                    String paySubState = activityAccount.getVerifyState().substring(0, 2);
                    activityAccount.setVerifyState(paySubState + "1");
                    activityAccount.setFailReason("");
                    resultMap.put("code", 10000);
                    if (activityAccount.getVerifyState().equals(STATE_111.getCode())) {
                        AfterSaleActivityDto activityDto = new AfterSaleActivityDto();
                        activityDto.setId(activityAccount.getActivityId());
                        activityDto.setUpdateDt(new Date());
                        activityDto.setUpdateBy(1);
                        activityDto.setOnSubAccount(AfterSaleConstants.SubAccountType.VERIFY.getCode());
                        afterSaleActivityService.openCloseSubAccount(activityDto);
                    } else {
                        //101 121 ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????id??????????????????????????????????????????
                        AfterSaleActivityAccount newAccount = new AfterSaleActivityAccount();
                        BeanUtils.copyProperties(activityAccount, newAccount);
                        newAccount.setId(null);
                        newAccount.setDeleteFlag(true);
                        activityAccount.setOrderNo("");
                        activityAccount.setVerifyState(AfterSaleConstants.SubAccountVerifyState.STATE_000.getCode());
                        activityAccount.setWxTransactionId("");
                        activityAccount.setFailReason("");
                        activityAccount.setSubAccountTransactionDetailId("");
                        activityAccount.setRefundId("");
                        activityAccount.setOrderMoney(BigDecimal.ZERO);
                        accountService.saveAfterSaleActivityAccount(newAccount, null);
                    }
                } else {
                    String paySubState = activityAccount.getVerifyState().substring(0, 2);
                    activityAccount.setVerifyState(paySubState + "2");
                    activityAccount.setFailReason(backInfo.getStatusDesc());
                }
            }
            accountService.updateAfterSaleActivityAccount(activityAccount, 1);
        }
        CommonLogUtil.addInfo("?????????????????????????????????", "??????????????????????????????_?????? ", JSON.toJSONString(activityAccountList));
        return resultMap;
    }

}
