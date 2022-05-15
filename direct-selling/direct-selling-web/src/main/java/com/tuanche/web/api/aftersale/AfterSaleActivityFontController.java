package com.tuanche.web.api.aftersale;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.tuanche.arch.util.IPUtil;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.api.AfterSaleActivityService;
import com.tuanche.directselling.api.AfterSaleUserService;
import com.tuanche.directselling.dto.AfterSaleUserBrowseDto;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.AfterSaleActivityVo;
import com.tuanche.directselling.vo.CreatePosterVo;
import com.tuanche.web.api.BaseController;
import com.tuanche.web.util.CommonLogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

/**
 * @author lvsen
 * @description
 * @date 2021/7/20 16:20
 */
@RequestMapping("/api/afterSale")
@RestController
public class AfterSaleActivityFontController extends BaseController {

    @Reference
    AfterSaleActivityService afterSaleActivityService;
    @Reference
    private AfterSaleUserService afterSaleUserService;
    @Autowired
    ExecutorService executorService;

    /**
     * 获取售后活动信息
     *
     * @param request
     * @param activityVo
     * @return
     */
    @RequestMapping(value = "/getActivityInfoForApi", method = RequestMethod.POST)
    public TcResponse getActivityInfoForApi(HttpServletRequest request, @RequestBody AfterSaleActivityVo activityVo) {
        CommonLogUtil.addInfo("售后服务活动首页-获取活动数据接口 start", "入参：", JSON.toJSONString(activityVo));
        if (Objects.isNull(activityVo) && Objects.isNull(activityVo.getActivityId())) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "活动无效");
        }
        if (StringUtil.isEmpty(activityVo.getUserWxUnionId())) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "UserWxUnionId为空");
        }
        if (StringUtil.isEmpty(activityVo.getUserWxOpenId())) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "UserWxOpenId为空");
        }
        if (StringUtil.isEmpty(activityVo.getUserWxHead())) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "用户头像为空");
        }
        if (StringUtil.isEmpty(activityVo.getNickName())) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "用户昵称为空");
        }

        saveBrowseData(request, activityVo);
        return afterSaleActivityService.getAfterSaleActivityInfoForApi(activityVo);
    }

    /**
     * 保存浏览数据
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
     * 售后特卖经销商列表
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
     * 活动列表
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
     * 生成海报图
     *
     * @return
     */
    @PostMapping("/createPoster")
    public TcResponse createPoster(HttpServletRequest request, @RequestBody CreatePosterVo createPosterVo) {
        long st = System.currentTimeMillis();
        if (createPosterVo == null || StringUtil.isEmpty(createPosterVo.getShareUrl())) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "分享地址不能为空");
        }
        if (StringUtils.isEmpty(createPosterVo.getPosterUrl())) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "海报图不能为空");
        }
        String postUrl = afterSaleActivityService.createPostUrl(createPosterVo.getShareUrl(), createPosterVo.getPosterUrl());

        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), System.currentTimeMillis() - st, StatusCodeEnum.SUCCESS.getText(),
                (Object)(StringUtil.isEmpty(postUrl) ? createPosterVo.getPosterUrl() : postUrl));
    }
}
