package com.tuanche.web.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.directselling.api.FissionActivityService;
import com.tuanche.directselling.api.FissionDictService;
import com.tuanche.directselling.api.FissionSaleService;
import com.tuanche.directselling.api.FissionSalesOrderService;
import com.tuanche.directselling.api.FissionStatisticsService;
import com.tuanche.directselling.api.FissionTradeRecordService;
import com.tuanche.directselling.api.FissionUserTaskRecordService;
import com.tuanche.directselling.dto.FissionSalePayDto;
import com.tuanche.directselling.dto.FissionSubscribeOrLiveCountDto;
import com.tuanche.directselling.enums.FissionDictType;
import com.tuanche.directselling.model.FissionDict;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.vo.CsUserActivityVo;
import com.tuanche.directselling.vo.CsUserIntegralListVo;
import com.tuanche.directselling.vo.CsUserVo;
import com.tuanche.directselling.vo.FissionActivityDetailVo;
import com.tuanche.directselling.vo.FissionActivityNameTimeVo;
import com.tuanche.directselling.vo.FissionActivityTaskVo;
import com.tuanche.directselling.vo.FissionSaleScoreVo;
import com.tuanche.directselling.vo.FissionSaleWalletVo;
import com.tuanche.directselling.vo.WeChatPaymentVo;
import com.tuanche.manubasecenter.enums.ManufacturerUser;
import com.tuanche.manubasecenter.model.CsUser;
import com.tuanche.web.service.FissionLoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: FissionActivityManuController
 * @Description: 裂变活动B端前台接口
 * @Author: ZhangYiXin
 * @Date: 2020/9/23 18:33
 * @Version 1.0
 **/
@RestController
@RequestMapping("/api/fission/manu")
public class FissionActivityManuController extends BaseController {

    @Reference
    private FissionSaleService fissionSaleService;
    @Reference
    private FissionSalesOrderService fissionSalesOrderService;
    @Reference
    private FissionActivityService fissionActivityService;
    @Reference
    private FissionTradeRecordService fissionTradeRecordService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private FissionLoginServiceImpl fissionLoginService;
    @Reference
    private FissionStatisticsService fissionStatisticsService;
    @Reference
    private FissionUserTaskRecordService fissionUserTaskRecordService;
    @Reference
    private FissionDictService fissionDictService;

    /**
     * 活动页列表
     *
     * @return TcResponse
     */
    @GetMapping("/list")
    public TcResponse getFissionActivityList(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        long st = System.currentTimeMillis();
        CsUser csUser = fissionLoginService.getLoginUser(request);
        PageResult pageResult = null;
        if (csUser != null) {
            if (page == null || page < 1) {
                page = 1;
            }
            if (limit == null) {
                limit = 10;
            }
            pageResult = fissionActivityService.getActivitySaleByDealerId(page, limit, csUser.getDealerId(), csUser.getId());
            pageResult.setPage(page);
            pageResult.setLimit(limit);
        }
        String uNmae= csUser!=null?csUser.getUname():"";
        return success(new CsUserActivityVo(uNmae, pageResult), st);
    }

    /**
     * 活动详情
     *
     * @param fissionId 活动ID
     * @return
     */
    @GetMapping("/detail")
    public TcResponse getFissionActivityList(@RequestParam(value = "id") Integer fissionId) {
        long st = System.currentTimeMillis();
        CsUser csUser = fissionLoginService.getLoginUser(request);
        FissionActivityDetailVo fissionActivityDetailVo = null;
        if (csUser != null) {
            fissionActivityDetailVo = fissionSaleService.getActivitySaleDetail(csUser.getDealerId(), fissionId, csUser.getId());
        }
        return success(fissionActivityDetailVo, st);
    }

    /**
     * 销售个人信息+积分
     *
     * @return TcResponse
     */
    @GetMapping("/info")
    public TcResponse getFissionActivitySaleInfo() {
        long st = System.currentTimeMillis();
        CsUser csUser = fissionLoginService.getLoginUser(request);
        if (csUser != null) {
            CsUserVo csUserVo = new CsUserVo();
            csUserVo.setId(csUser.getId());
            csUserVo.setUlevel(ManufacturerUser.getNameByType(csUser.getUlevel()));
            csUserVo.setUname(csUser.getUname());
            csUserVo.setDealerName(csUser.getDealerName());
            csUserVo.setIntegral(fissionSaleService.getSaleIntegralBySaleId(csUser.getId()));
            return success(csUserVo, st);
        } else {
            return null;
        }
    }

    /**
     * 销售积分列表
     *
     * @return TcResponse
     */
    @GetMapping("/integral/list")
    public TcResponse getFissionActivityIntegralList(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        long st = System.currentTimeMillis();
        CsUser csUser = fissionLoginService.getLoginUser(request);
        PageResult pageResult = null;
        if (csUser != null) {
            if (page == null || page < 1) {
                page = 1;
            }
            if (limit == null) {
                limit = 10;
            }
            pageResult = fissionSaleService.getSaleIntegralListByBySaleId(page, limit, csUser.getId());
            pageResult.setPage(page);
            pageResult.setLimit(limit);
        }
        return success(new CsUserIntegralListVo(fissionSaleService.getSaleIntegralBySaleId(csUser.getId()), pageResult), st);
    }


    /**
     * 销售钱包页
     *
     * @return TcResponse
     */
    @GetMapping("/wallet")
    public TcResponse getFissionSaleWallet(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        long st = System.currentTimeMillis();
        CsUser csUser = fissionLoginService.getLoginUser(request);
        FissionSaleWalletVo fissionSaleWalletVo = null;
        if (csUser != null) {
            if (page == null || page < 1) {
                page = 1;
            }
            if (limit == null) {
                limit = 10;
            }
            fissionSaleWalletVo = fissionSaleService.selectFissionSaleWallet(page, limit, csUser.getId());
        }
        return success(fissionSaleWalletVo, st);
    }

    /**
     * 销售订单页
     *
     * @return TcResponse
     */
    @GetMapping("/order")
    public TcResponse getFissionSaleOrder(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        long st = System.currentTimeMillis();
        CsUser csUser = fissionLoginService.getLoginUser(request);
        PageResult pageResult = null;
        if (csUser != null) {
            if (page == null || page < 1) {
                page = 1;
            }
            if (limit == null) {
                limit = 10;
            }
            pageResult = fissionSalesOrderService.selectSaleOrder(page, limit, csUser.getId());
            pageResult.setPage(page);
            pageResult.setLimit(limit);
        }
        return success(pageResult, st);
    }


    /**
     * 销售参加过的活动
     *
     * @return TcResponse
     */
    @GetMapping("/activity")
    public TcResponse getActivity() {
        long st = System.currentTimeMillis();
        CsUser csUser = fissionLoginService.getLoginUser(request);
        List<FissionActivityNameTimeVo> fissionActivityNameVos = fissionActivityService.getFissionActivityList(csUser.getId());
        return success(fissionActivityNameVos, st);
    }


    /**
     * 销售的个人成绩
     *
     * @return TcResponse
     */
    @GetMapping("/score")
    public TcResponse getFissionSaleScore(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit, @RequestParam(value = "id", required = false) Integer fissionId) {
        long st = System.currentTimeMillis();
        CsUser csUser = fissionLoginService.getLoginUser(request);
        FissionSaleScoreVo fissionSaleScoreVo = null;
        if (csUser != null) {
            fissionSaleScoreVo = fissionSaleService.getPersonalIntegralVo(csUser.getDealerId(), csUser.getId(), fissionId);
        }
        return success(fissionSaleScoreVo, st);
    }

    /**
     * @param weChatPaymentVo
     * @description: 销售提现
     * @return: com.tuanche.arch.web.model.TcResponse
     * @author: dudg
     * @date: 2020/9/28 17:41
     */
    @RequestMapping("/saleWithDrawal")
    public TcResponse saleWithDrawal(WeChatPaymentVo weChatPaymentVo) {
        CsUser csUser = fissionLoginService.getLoginUser(request);
        FissionSalePayDto fissionSalePayDto = new FissionSalePayDto();
        fissionSalePayDto.setSaleId(csUser.getId());
        fissionSalePayDto.setSaleWxOpenId(weChatPaymentVo.getOpenId());
        return fissionSaleService.saleWithdrawal(fissionSalePayDto);
    }


    /**
     * 任务分享结果的数据明细
     *
     * @return TcResponse
     */
    @GetMapping("/taskRecord/list")
    public TcResponse getFissionUserTaskRecordDetail(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit, @RequestParam(value = "fissionId") Integer fissionId, @RequestParam(value = "taskId") Integer taskId) {
        long st = System.currentTimeMillis();
        CsUser csUser = fissionLoginService.getLoginUser(request);
        PageResult pageResult = null;
        if (page == null || page < 1) {
            page = 1;
        }
        if (limit == null) {
            limit = 10;
        }
        if (csUser != null) {
            pageResult = fissionUserTaskRecordService.selectFissionUserTaskRecordDetail(page, limit, fissionId, taskId, csUser.getId());
        }
        pageResult.setPage(page);
        pageResult.setLimit(limit);
        return success(pageResult, st);
    }


    /**
     * 预约直播、观看直播、预约并观看直播
     *
     * @return TcResponse
     */
    @GetMapping("/subscribeOrLiveCount")
    public TcResponse getFissionSubscribeOrLiveCount(@RequestParam(value = "fissionId") Integer fissionId) {
        long st = System.currentTimeMillis();
        FissionSubscribeOrLiveCountDto fissionSubscribeOrLiveCountDto = fissionUserTaskRecordService.selectFissionSubscribeOrLiveCount(fissionId);
        return success(fissionSubscribeOrLiveCountDto, st);
    }

    /**
     * 任务名称列表
     *
     * @return TcResponse
     */
    @GetMapping("/task/list")
    public TcResponse getTaskList() {
        long st = System.currentTimeMillis();
        List<FissionDict> fissionDictList = fissionDictService.getFissionDictByType(FissionDictType.LB.getType());
        List<FissionActivityTaskVo> taskVoList = new ArrayList<>();
        FissionActivityTaskVo fissionActivityTaskVo = null;
        for (FissionDict fd : fissionDictList) {
            fissionActivityTaskVo = new FissionActivityTaskVo();
            fissionActivityTaskVo.setTaskId(Integer.parseInt(fd.getCode()));
            fissionActivityTaskVo.setTaskName(fd.getName());
            taskVoList.add(fissionActivityTaskVo);
        }
        return success(taskVoList, st);
    }
}
