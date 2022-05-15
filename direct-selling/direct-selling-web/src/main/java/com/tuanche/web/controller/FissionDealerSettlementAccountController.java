package com.tuanche.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.tuanche.directselling.api.FissionActivityService;
import com.tuanche.directselling.api.FissionDealerSettlementAccountService;
import com.tuanche.directselling.api.FissionGoodsOrderRecordservice;
import com.tuanche.directselling.dto.FissionActivityDto;
import com.tuanche.directselling.dto.FissionActivityExtendDto;
import com.tuanche.directselling.dto.FissionDealerSettlementAccountDto;
import com.tuanche.directselling.dto.FissionDealerSettlementAccountExportDto;
import com.tuanche.directselling.dto.FissionGoodsOrderRecordDto;
import com.tuanche.directselling.model.FissionActivity;
import com.tuanche.directselling.model.FissionGoodsOrderRecord;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.SettlementAccountTypeUtil;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.FissionActivityVo;
import com.tuanche.eap.api.bean.manufacturer.CsDealerFinancial;
import com.tuanche.eap.api.service.manufacturer.CsDealerFinancialService;
import com.tuanche.eap.api.utils.ResultDto;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.model.CsCompany;
import com.tuanche.web.util.DirectCommonUtil;
import com.tuanche.web.util.ExportExcel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/fission/dealer/settlement")
public class FissionDealerSettlementAccountController {

    @Reference
    private FissionDealerSettlementAccountService fissionDealerSettlementAccountService;
    @Reference
    private FissionGoodsOrderRecordservice fissionGoodsOrderRecordservice;
    @Reference
    private CompanyBaseService companyBaseService;
    @Reference
    private FissionActivityService fissionActivityService;
    @Reference
    private CsDealerFinancialService csDealerFinancialService;

    @RequestMapping("/toPage")
    public String toPage (ModelMap modelMap) {
        FissionActivity fissionActivity = new FissionActivityDto();
        List<FissionActivityVo> fissionActivityList = fissionActivityService.findFissionActivityList(fissionActivity);
        modelMap.addAttribute("fissionActivityList", fissionActivityList);
        return "fission/dealer/settlement";
    }

    /**
      * @description : 获取裂变活动经销商结算账号
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/3/10 17:27
      */
    /*@RequestMapping("/getFissionDealerSettlementAccountList")
    @ResponseBody
    public PageResult getFissionDealerSettlementAccountList (FissionDealerSettlementAccountDto dealerSettlementAccountDto) {
        PageResult<FissionDealerSettlementAccountDto> dealerListByPage = fissionDealerSettlementAccountService.getFissionDealerSettlementAccountList(dealerSettlementAccountDto);
        if (!CollectionUtils.isEmpty(dealerListByPage.getData())) {
            List<FissionGoodsOrderRecord> recordList = new ArrayList<>();
            List<Integer> dealerids = new ArrayList<>();
            dealerListByPage.getData().forEach(v->{
                dealerids.add(v.getDealerId());
                FissionGoodsOrderRecord record = new FissionGoodsOrderRecord();
                record.setFissionId(v.getFissionId());
                record.setDealerId(v.getDealerId());
                recordList.add(record);
            });
            Map<Integer, CsCompany> companyMap = companyBaseService.getCompanyMap(dealerids);
            Map<String, FissionDealerSettlementAccountDto> orderAmountMap = fissionGoodsOrderRecordservice.getFissionGoodsOrderAmountMapByFissionIdAndDealerId(recordList);
            dealerListByPage.getData().forEach(v->{
                if (companyMap.get(v.getDealerId())!=null) v.setDealerName(companyMap.get(v.getDealerId()).getCompanyName());
                FissionDealerSettlementAccountDto accountDto = orderAmountMap.get(v.getFissionId()+":"+v.getDealerId());
                if (accountDto!=null) {
                    v.setOrderSum(accountDto.getOrderSum());
                    v.setOrderCheckoutSum(accountDto.getOrderCheckoutSum());
                    v.setOrderRefundSum(accountDto.getOrderRefundSum());
                }
            });
        }
        dealerListByPage.setCode("0");
        return dealerListByPage;
    }*/
    /**
     * @description : 获取裂变活动经销商结算账号
     * @author : fxq
     * @param :
     * @return :
     * @date : 2021/3/10 17:27
     */
    @RequestMapping("/getFissionDealerSettlementAccountList")
    @ResponseBody
    public PageResult getFissionDealerSettlementAccountList (FissionActivityExtendDto settlementAccount) {
        PageResult<FissionActivityExtendDto> dealerListByPage = fissionActivityService.getFissionDealerSettlementAccountListByPage(settlementAccount);
        //PageResult<FissionDealerSettlementAccountDto> dealerListByPage = fissionDealerSettlementAccountService.getFissionDealerSettlementAccountList(dealerSettlementAccountDto);
        if (!CollectionUtils.isEmpty(dealerListByPage.getData())) {
            List<FissionGoodsOrderRecord> recordList = new ArrayList<>();
            List<Integer> dealerids = new ArrayList<>();
            dealerListByPage.getData().forEach(v->{
                dealerids.add(v.getDataId());
                FissionGoodsOrderRecord record = new FissionGoodsOrderRecord();
                record.setFissionId(v.getFissionId());
                record.setDealerId(v.getDataId());
                recordList.add(record);
            });
            Map<Integer, CsCompany> companyMap = companyBaseService.getCompanyMap(dealerids);
            Map<String, FissionDealerSettlementAccountDto> orderAmountMap = fissionGoodsOrderRecordservice.getFissionGoodsOrderAmountMapByFissionIdAndDealerId(recordList);
            Map<String, FissionGoodsOrderRecordDto> writeOffDataMap = fissionGoodsOrderRecordservice.getDealerWriteOffData(recordList);
            dealerListByPage.getData().forEach(v->{
                if (companyMap.get(v.getDataId())!=null) v.setDealerName(companyMap.get(v.getDataId()).getCompanyName());
                FissionGoodsOrderRecord orderAmount = null;
                String key = v.getFissionId()+":"+v.getDataId();
                FissionDealerSettlementAccountDto accountDto = orderAmountMap.get(key);
                if (accountDto!=null) {
                    v.setOrderSum(accountDto.getOrderSum());
                    v.setOrderRefundSum(accountDto.getOrderRefundSum());
                }
                FissionGoodsOrderRecordDto orderRecordDto = writeOffDataMap.get(key);
                if (orderRecordDto!=null) {
                    //核销订单数
                    v.setOrderCheckoutSum(orderRecordDto.getOrderCheckoutSum());
                    //核销订单金额
                    v.setOrderAmount(orderRecordDto.getOrderAmount());
                }else {
                    v.setOrderAmount(BigDecimal.ZERO);
                }
            });
        }
        dealerListByPage.setCode("0");
        return dealerListByPage;
    }
    /**
      * @description : 经销商打款
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/3/10 18:26
      */
    @RequestMapping("/remit")
    @ResponseBody
    public ResultDto remit(String jsonString) {
        ResultDto dto = new ResultDto();
        StringBuilder msg = new StringBuilder();
        int resultCode = StatusCodeEnum.SUCCESS.getCode();
        try {
            if (StringUtils.isEmpty(jsonString)) {
                return DirectCommonUtil.addParamNull();
            }
            List<Integer> updateList = new ArrayList();
            List<FissionDealerSettlementAccountDto> list = JSON.parseArray(jsonString, FissionDealerSettlementAccountDto.class);
            for (FissionDealerSettlementAccountDto sfa : list) {
                //打款账户
                CsDealerFinancial csDealerFinancial = csDealerFinancialService.getCsDealerFinancial(sfa.getDealerId(), SettlementAccountTypeUtil.toFinancealAccountType(sfa.getAccountType()));
                if(csDealerFinancial != null && csDealerFinancial.getCardNumber() != null){
                    updateList.add(sfa.getId());
                }else{
                    msg.append("经销商【"+sfa.getDealerName()+"】打款失败，原因：该经销商没有设置结算账户</br>");
                    resultCode = StatusCodeEnum.RESULE_DATA_NONE.getCode();
                }
            }
            if(!CollectionUtils.isEmpty(updateList)){
                fissionDealerSettlementAccountService.remitBatch(updateList);
            }

        } catch (Exception e) {
            dto = DirectCommonUtil.addError("FissionDealerSettlementAccountController", "remit", "经销商打款 error", e);
        }
        dto.setCode(resultCode);
        dto.setMsg(msg.length() < 1?"成功":msg.toString());
        return dto;
    }
    //导出经销商订单
    @RequestMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response, FissionActivityExtendDto settlementAccount) {
        try {
            settlementAccount.setPage(1);
            settlementAccount.setLimit(10000);
            PageResult<FissionDealerSettlementAccountDto> result = getFissionDealerSettlementAccountList(settlementAccount);
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(result.getData())) {
                Map<String, String> titleValueMap = new LinkedHashMap<>();
                titleValueMap.put("FissionName", "活动名称");
                titleValueMap.put("DealerName", "经销商名称");
                titleValueMap.put("OrderSum", "订单总数");
                titleValueMap.put("OrderCheckoutSum", "核销订单总数");
                titleValueMap.put("OrderAmount", "核销总金额");
                titleValueMap.put("OrderRefundSum", "退款订单总数");
                titleValueMap.put("PaymentStatusName", "给经销商的打款状态");
                ExportExcel.exportExcel("经销商订单报表.xls", titleValueMap, result.getData(), request, response);
            }
        } catch (Exception e) {
            DirectCommonUtil.addError("FissionGoodsOrderController","export", "导出 error", e);
        }
    }
    /**
     * 导出结算账户
     * @author HuangHao
     * @CreatTime 2021-04-25 15:33
     * @param request
     * @param response
     * @param dealerSettlementAccountDto
     * @return void
     */
    @RequestMapping("/exportDealerSettlementAccount")
    public void exportDealerSettlementAccount(HttpServletRequest request, HttpServletResponse response, FissionDealerSettlementAccountDto dealerSettlementAccountDto) {
        try {
            List<FissionDealerSettlementAccountDto> dealerListByPage = fissionDealerSettlementAccountService.getSettlementAccountList(dealerSettlementAccountDto);
            List<FissionDealerSettlementAccountExportDto> exportDtos = new ArrayList();
            if (!CollectionUtils.isEmpty(dealerListByPage)) {
                List<FissionGoodsOrderRecord> recordList = new ArrayList<>();
                List<Integer> dealerids = new ArrayList<>();
                dealerListByPage.forEach(v -> {
                    dealerids.add(v.getDealerId());
                    FissionGoodsOrderRecord record = new FissionGoodsOrderRecord();
                    record.setFissionId(v.getFissionId());
                    record.setDealerId(v.getDealerId());
                    recordList.add(record);
                });
                Map<Integer, List<CsDealerFinancial>> dealerFinancialMap = csDealerFinancialService.selectDealerFinancialByDealerIds(dealerids);
                Map<Integer, List<CsDealerFinancial>> finalDealerFinancialMap = dealerFinancialMap == null ? new HashMap<Integer, List<CsDealerFinancial>>() : dealerFinancialMap;
                Map<Integer, CsCompany> companyMap = companyBaseService.getCompanyMap(dealerids);
                Map<String, FissionGoodsOrderRecordDto> dataMap = fissionGoodsOrderRecordservice.getDealerWriteOffData(recordList);

                FissionDealerSettlementAccountExportDto exportDto = null;
                for (FissionDealerSettlementAccountDto v : dealerListByPage) {
                    exportDto = new FissionDealerSettlementAccountExportDto();
                    FissionGoodsOrderRecord orderAmount = null;
                    if (dataMap != null && (orderAmount = dataMap.get(v.getFissionId() + ":" + v.getDealerId())) != null) {
                        exportDto.setOrderAmount(orderAmount.getOrderAmount());
                    } else {
                        exportDto.setOrderAmount(BigDecimal.ZERO);
                    }
                    //结算账户信息
                    List<CsDealerFinancial> dealerFinancialList = null;
//                    if ((dealerFinancialList = finalDealerFinancialMap.get(v.getDealerId())) != null) {
//                        CsDealerFinancial csDealerFinancial = dealerFinancialList.stream().filter(financial -> financial.getAccountType().equals(SettlementAccountTypeUtil.toFinancealAccountType(v.getAccountType()))).findFirst().orElse(null);
//                        if (csDealerFinancial != null) {
//                            exportDto.setBankCardNumber(csDealerFinancial.getCardNumber());
//                            exportDto.setBankName(csDealerFinancial.getCardBank());
//                            exportDto.setBankAddress(csDealerFinancial.getCardBankAddress());
//                        }
//                    }
                    if (companyMap.get(v.getDealerId()) != null) {
                        exportDto.setDealerName(companyMap.get(v.getDealerId()).getCompanyName());
                    }
                    FissionGoodsOrderRecordDto accountDto = dataMap.get(v.getFissionId() + ":" + v.getDealerId());
                    if (accountDto == null) {
                        exportDto.setOrderCheckoutSum(0);
                    } else {
                        exportDto.setOrderCheckoutSum(accountDto.getOrderCheckoutSum());
                    }
                    if (v.getAccountType() == 1) {
                        exportDto.setBankCardNumber(v.getBankCardNumber());
                        exportDto.setBankName(v.getBankName());
                        exportDto.setBankAddress(v.getBankAddress());
                    }
                    if (v.getAccountType() == 2) {
                        exportDto.setBankCardNameMan(v.getBankCardName());
                        exportDto.setBankCardNumberMan(v.getBankCardNumber());
                        exportDto.setBankNameMan(v.getBankName());
                    }
                    exportDto.setActivityName(v.getActivityName());
                    exportDto.setAccountTypeStr(v.getAccountTypeStr());
                    exportDtos.add(exportDto);
                }
            }
            Map<String, String> titleValueMap = new LinkedHashMap<>();
            titleValueMap.put("ActivityName", "活动场次");
            titleValueMap.put("DealerName", "经销商名称");
            titleValueMap.put("OrderAmount", "结算金额");
            titleValueMap.put("OrderCheckoutSum", "核销订单数");
            titleValueMap.put("AccountTypeStr", "账户类型");
            titleValueMap.put("BankCardNumber", "门店对公账户");
            titleValueMap.put("BankName", "开户行全称");
            titleValueMap.put("BankAddress", "开户行地址");
            titleValueMap.put("BankCardNameMan", "收款人姓名");
            titleValueMap.put("BankCardNumberMan", "收款人银行卡号");
            titleValueMap.put("BankNameMan", "开户行");
            ExportExcel.exportExcel("经销商结算账户.xls", titleValueMap, exportDtos, request, response);
        } catch (Exception e) {
            DirectCommonUtil.addError("FissionGoodsOrderController", "export", "导出 error", e);
        }
    }
}
