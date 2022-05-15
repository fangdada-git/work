package com.tuanche.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.directselling.api.FissionActivityService;
import com.tuanche.directselling.api.FissionSalesOrderService;
import com.tuanche.directselling.dto.FissionActivityDto;
import com.tuanche.directselling.dto.FissionSalesOrderDto;
import com.tuanche.directselling.model.FissionActivity;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.vo.FissionActivityVo;
import com.tuanche.web.util.DirectCommonUtil;
import com.tuanche.web.util.ExportExcel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/fission/sale/order")
public class FissionSaleOrderController {
    
    @Reference
    private FissionSalesOrderService fissionSalesOrderService;
    @Reference
    private FissionActivityService fissionActivityService;

    @RequestMapping("/toSaleOrderList")
    public String toFissionGoodsList(ModelMap modelMap, Integer fissionId, Integer periodsId) {
        FissionActivity fissionActivity = new FissionActivityDto();
        List<FissionActivityVo> fissionActivityList = fissionActivityService.findFissionActivityList(fissionActivity);
        modelMap.addAttribute("fissionActivityList", fissionActivityList);
        return "fission/activity/sale-order-list";
    }


    @RequestMapping("/getSaleOrderList")
    @ResponseBody
    public PageResult getSaleOrderListByPage(PageResult<FissionSalesOrderDto> pageResult, FissionSalesOrderDto fissionSalesOrderDto) {
        if (fissionSalesOrderDto.getFissionId() !=null && fissionSalesOrderDto.getFissionId()==0) fissionSalesOrderDto.setFissionId(null); 
        PageResult pageList = fissionSalesOrderService.getSaleOrderListByPage(pageResult, fissionSalesOrderDto);
        pageList.setCode("0");
        return pageList;
    }

    @RequestMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response, FissionSalesOrderDto fissionSalesOrderDto) {
        try {
            PageResult<FissionSalesOrderDto> pageResult = new PageResult<>();
            pageResult.setPage(1);
            pageResult.setLimit(10000);
            PageResult result = getSaleOrderListByPage(pageResult,  fissionSalesOrderDto);
            if (CollectionUtils.isNotEmpty(result.getData())) {
                FissionActivity activity = fissionActivityService.getFissionActivityById(fissionSalesOrderDto.getFissionId());
                Map<String, String> titleValueMap = new LinkedHashMap<>();
                titleValueMap.put("TradeNo", "微信支付流水号");
                titleValueMap.put("SaleId", "销售id");
                titleValueMap.put("SaleName", "销售名称");
                titleValueMap.put("SalePhone", "销售手机号");
                titleValueMap.put("DealerName", "经销商名称");
                titleValueMap.put("ActivityName", "裂变活动场次名称");
                titleValueMap.put("CityName", "城市");
                titleValueMap.put("OrderStatusName", "支付状态");
                titleValueMap.put("PayTime", "支付时间");
                titleValueMap.put("OrderAmount", "支付金额");
                ExportExcel.exportExcel((activity==null?"全部活动":activity.getActivityName())+"-对赌金订单.xls", titleValueMap, result.getData(), request, response);
            }
        } catch (Exception e) {
            DirectCommonUtil.addError("FissionSaleOrderController","export", "导出 error", e);
        }
    }
    
}
