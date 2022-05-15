package com.tuanche.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.directselling.api.FissionActivityService;
import com.tuanche.directselling.api.FissionDataViewService;
import com.tuanche.directselling.api.FissionUserTaskRecordService;
import com.tuanche.directselling.model.FissionActivity;
import com.tuanche.directselling.model.FissionDataView;
import com.tuanche.directselling.model.FissionSaleDataView;
import com.tuanche.directselling.model.FissionUserDataView;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.vo.FissionActivityRankVo;
import com.tuanche.directselling.vo.FissionAllSaleRankVo;
import com.tuanche.directselling.vo.FissionUserRewardRankVo;
import com.tuanche.web.api.BaseController;
import com.tuanche.web.util.ExportExcel;
import com.tuanche.web.util.ExportExcelCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: FissionDataViewController
 * @Description: B/C端裂变数据概览
 * @Author: ZhangYiXin
 * @Date: 2020/9/23 11:17
 * @Version 1.0
 **/
@Controller
@RequestMapping("/fission/data/view")
public class FissionDataViewController extends BaseController {

    @Reference
    private FissionDataViewService fissionDataViewService;
    @Reference
    private FissionActivityService fissionActivityService;

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Reference
    private FissionUserTaskRecordService fissionUserTaskRecordService;

    /**
     * B端数据概览
     *
     * @return
     */
    @GetMapping("/business")
    public String getBusinessFissionDataView() {
        return "fission/statistics/sale-data-view";
    }

    /**
     * B端数据排行
     *
     * @return
     */
    @GetMapping("/business/rank")
    public String getBusinessRank() {
        return "fission/statistics/sale-rank";
    }


    /**
     * B端数据排行
     *
     * @return
     */
    @GetMapping("/business/reward")
    public String getBusinessReward() {
        return "fission/statistics/sale-reward";
    }


    /**
     * C端数据概览页面
     *
     * @return
     */
    @GetMapping("/user")
    public String getFissionUserDataView() {
        return "fission/statistics/user-data-view";
    }


    /**
     * 裂变明细数据页面
     *
     * @return
     */
    @GetMapping("/recordPage")
    public String getFissionUserTaskRecordPage() {
        return "fission/statistics/record";
    }

    /**
     * 活动下拉选数据
     *
     * @param page
     * @param limit
     * @param keyword
     * @return
     */
    @GetMapping("/business/activity/select")
    @ResponseBody
    public PageResult getBusinessActivitySelect(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam(value = "keyword", required = false) String keyword) {
        PageResult<FissionActivity> pageResult = new PageResult<>();
        FissionActivity fissionActivity = new FissionActivity();
        fissionActivity.setActivityName(keyword);
        pageResult.setPage(page);
        pageResult.setPage(limit);
        PageResult pageList = fissionActivityService.findFissionActivityByPage(page, limit, keyword);
        pageList.setCode(0);
        return pageList;
    }

    /**
     * B端概览数据
     *
     * @param id
     * @return
     */
    @GetMapping("/business/detail")
    @ResponseBody
    public ResultDto getBusinessActivityDetail(@RequestParam("id") Integer id) {
        ResultDto resultDto = new ResultDto();
        FissionSaleDataView fissionSaleDataView = fissionDataViewService.getBusinessFissionDataViewByFissionId(id);
        resultDto.setResult(fissionSaleDataView);
        return resultDto;
    }

    /**
     * 销售排行 前10
     *
     * @param id
     * @return
     */
    @GetMapping("/business/rank/sale")
    @ResponseBody
    public ResultDto getBusinessRankSale(@RequestParam("id") Integer id) {
        ResultDto resultDto = new ResultDto();
        List<FissionActivityRankVo> rankVos = fissionDataViewService.getBusinessRankSale(id);
        resultDto.setResult(rankVos);
        return resultDto;
    }

    /**
     * 所有销售排行
     *
     * @param page
     * @param limit
     * @param id    裂变活动ID
     * @return
     */
    @GetMapping("/business/rank/sale/all")
    @ResponseBody
    public PageResult getBusinessRankSaleAll(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("id") Integer id, @RequestParam(value = "companyName", required = false) String companyName) {
        PageResult pageResult = fissionDataViewService.getBusinessRankAllSale(page, limit, id, companyName);
        pageResult.setCode(0);
        return pageResult;
    }

    /**
     * 所有销售排行 导出
     *
     * @param id
     * @param companyName
     */
    @GetMapping("/business/rank/sale/all/export")
    @ResponseBody
    public void getBusinessRankSaleAll(@RequestParam("id") Integer id, @RequestParam(value = "companyName", required = false) String companyName) {
        FissionActivity fissionActivity = fissionActivityService.getFissionActivityById(id);
        if (fissionActivity == null) {
            return;
        }
        Map<String, String> titleValueMap = new LinkedHashMap<>();
        titleValueMap.put("Rank", "排行");
        titleValueMap.put("SaleName", "销售姓名");
        titleValueMap.put("SalePhone", "手机号");
        titleValueMap.put("DealerName", "所属经销商");
        titleValueMap.put("RealIncome", "实际收入(元)");
        titleValueMap.put("EstimatedIncome", "预计收入(元)");
        titleValueMap.put("TaskIntegral", "总积分数");
        titleValueMap.put("WhetherCompleteTask", "是否完成基础目标");
        titleValueMap.put("PageView", "浏览页面数");
        titleValueMap.put("SubscribeLive", "预约直播数");
        titleValueMap.put("ActivityPageProduct", "活动页购买商品数");
        titleValueMap.put("LiveView", "观看直播数");
        titleValueMap.put("LivePageProduct", "直播间购买商品数");
        Map<String, ExportExcelCallback> callBackMap = new HashMap<>(4);
        callBackMap.put("WhetherCompleteTask", (ExportExcelCallback<FissionAllSaleRankVo>) object -> {
            if (object.getWhetherCompleteTask()) {
                return "是";
            }
            return "否";
        });
        ExportExcel.exportExcel(String.format("[%s]销售排行.xls", fissionActivity.getActivityName()), titleValueMap, callBackMap, fissionDataViewService.getBusinessRankAllSale(id, companyName), request, response);
    }

    /**
     * 前10经销商排行
     *
     * @param id
     * @return
     */
    @GetMapping("/business/rank/dealer")
    @ResponseBody
    public ResultDto getBusinessRankDealer(@RequestParam("id") Integer id) {
        ResultDto resultDto = new ResultDto();
        List<FissionActivityRankVo> rankVos = fissionDataViewService.getBusinessRankDealer(id);
        resultDto.setResult(rankVos);
        return resultDto;
    }

    /**
     * 所有经销商排行
     *
     * @param page
     * @param limit
     * @param id    裂变活动ID
     * @return
     */
    @GetMapping("/business/rank/dealer/all")
    @ResponseBody
    public PageResult getBusinessRankAllSale(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("id") Integer id, @RequestParam(value = "companyName", required = false) String companyName) {
        PageResult pageResult = fissionDataViewService.getBusinessRankAllDealer(page, limit, id, companyName);
        pageResult.setCode(0);
        return pageResult;
    }

    @GetMapping("/business/rank/dealer/all/export")
    @ResponseBody
    public void getBusinessRankDealerAll(@RequestParam("id") Integer id, @RequestParam(value = "companyName", required = false) String companyName) {
        FissionActivity fissionActivity = fissionActivityService.getFissionActivityById(id);
        if (fissionActivity == null) {
            return;
        }
        Map<String, String> titleValueMap = new LinkedHashMap<>();
        titleValueMap.put("Rank", "排行");
        titleValueMap.put("DealerName", "经销商名称");
        titleValueMap.put("SaleCount", "参与销售人数");
        titleValueMap.put("RealIncome", "实际收入(元)");
        titleValueMap.put("EstimatedIncome", "预计收入(元)");
        titleValueMap.put("TaskIntegral", "总积分数");
        titleValueMap.put("FinishTaskIntegral", "完成任务积分数");
        titleValueMap.put("PageView", "浏览页面数");
        titleValueMap.put("SubscribeLive", "预约直播数");
        titleValueMap.put("ActivityPageProduct", "活动页购买商品数");
        titleValueMap.put("LiveView", "观看直播数");
        titleValueMap.put("LivePageProduct", "直播间购买商品数");
        ExportExcel.exportExcel(String.format("[%s]经销商排行.xls", fissionActivity.getActivityName()), titleValueMap, fissionDataViewService.getBusinessRankAllDealer(id, companyName), request, response);
    }

    /**
     * 销售顾问奖金发放统计
     *
     * @return
     */
    @GetMapping("/business/reward/list")
    @ResponseBody
    public PageResult getBusinessReward(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("id") Integer id) {
        PageResult pageResult = fissionDataViewService.getBusinessRewardList(page, limit, id);
        pageResult.setCode(0);
        return pageResult;
    }

    @GetMapping("/business/reward/list/export")
    @ResponseBody
    public void getBusinessRewardExport(@RequestParam("id") Integer id) {
        FissionActivity fissionActivity = fissionActivityService.getFissionActivityById(id);
        if (fissionActivity == null) {
            return;
        }
        Map<String, String> titleValueMap = new LinkedHashMap<>();
        titleValueMap.put("SaleName", "销售姓名");
        titleValueMap.put("SalePhone", "手机号");
        titleValueMap.put("DealerName", "所属经销商");
        titleValueMap.put("RealIncome", "累计奖励金额(元)");
        titleValueMap.put("WaitingWithdrawal", "未体现金额");
        titleValueMap.put("Withdrawaled", "已提现金额");
        ExportExcel.exportExcel(String.format("[%s]销售顾问奖金发放统计.xls", fissionActivity.getActivityName()), titleValueMap, fissionDataViewService.getBusinessRewardList(id), request, response);
    }

    /**
     * 根据裂变活动ID查询C端数据概览
     *
     * @return
     */
    @GetMapping("/user/detail")
    @ResponseBody
    public ResultDto getFissionUserDataViewDetail(@RequestParam("id") Integer id) {
        ResultDto resultDto = new ResultDto();
        FissionUserDataView fissionUserDataView = fissionDataViewService.getCustomerFissionDataViewByFissionId(id);
        resultDto.setResult(fissionUserDataView);
        return resultDto;
    }

    /**
     * 根据裂变活动ID查询数据概览
     *
     * @return
     */
    @GetMapping("/{id}/{dataType}")
    @ResponseBody
    public ResultDto getFissionDataViewByFissionIdAndTypeAndIsShare(@PathVariable("id") Integer id, @PathVariable("dataType") Integer dataType) {
        ResultDto resultDto = new ResultDto();
        List<FissionDataView> fissionDataView = fissionDataViewService.getFissionDataViewByFissionIdAndTypeAndIsShare(id, dataType, null);
        resultDto.setResult(fissionDataView);
        return resultDto;
    }

    /**
     * C端裂变奖金排行
     *
     * @param id        裂变活动id
     * @param channelId 渠道id
     * @return
     */
    @GetMapping("/user/reward/rank")
    @ResponseBody
    public ResultDto getUserRewardRank(@RequestParam("id") Integer id, @RequestParam(value = "channelId", required = false) Integer channelId) {
        ResultDto resultDto = new ResultDto();
        List<FissionUserRewardRankVo> userRewardRank = fissionDataViewService.getUserRewardRank(id, channelId);
        resultDto.setResult(userRewardRank);
        return resultDto;
    }


    /**
     * 裂变明细数据
     *
     * @return
     */
    @GetMapping("/record")
    @ResponseBody
    public PageResult getFissionUserTaskRecord(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit,
                                               @RequestParam("fissionId") Integer fissionId,
                                               @RequestParam("taskId") Integer taskId,
                                               @RequestParam(value = "saleName", required = false) String saleName,
                                               @RequestParam(value = "companyName", required = false) String companyName,
                                               @RequestParam(value = "isEffective", required = false) Integer isEffective) {
        PageResult pageResult = fissionUserTaskRecordService.selectFissionUserTaskRecordDetailVoWithSaleName(page, limit, fissionId, taskId, saleName, companyName, isEffective);
        pageResult.setCode(0);
        return pageResult;
    }
}
