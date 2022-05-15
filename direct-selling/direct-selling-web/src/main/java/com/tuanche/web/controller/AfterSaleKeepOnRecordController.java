package com.tuanche.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.directselling.api.AfterSaleActivityService;
import com.tuanche.directselling.api.AfterSaleKeepOnRecordService;
import com.tuanche.directselling.api.FashionCarWinningNumberService;
import com.tuanche.directselling.dto.AfterSaleActivityDto;
import com.tuanche.directselling.dto.AfterSaleKeepOnRecordBrandCarSeriesDto;
import com.tuanche.directselling.dto.AfterSaleKeepOnRecordDto;
import com.tuanche.directselling.model.AfterSaleActivity;
import com.tuanche.directselling.model.AfterSaleKeepOnRecord;
import com.tuanche.directselling.utils.BeanCopyUtil;
import com.tuanche.directselling.utils.DateUtil;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.district.api.dto.output.DistrictOutputExpandDto;
import com.tuanche.eap.api.bean.manufacturer.CsCompany;
import com.tuanche.eap.api.service.manufacturer.CsCompanyService;
import com.tuanche.inner.sso.core.user.XxlUser;
import com.tuanche.web.service.AfterSaleKeepOnRecordExcelService;
import com.tuanche.web.util.ExportExcel;
import com.tuanche.web.util.ExportExcelCallback;
import com.tuanche.web.util.RefuelCardExcelUtil;
import com.tuanche.web.vo.AfterSaleKeepOnRecordBrandCarSeriesVo;
import com.tuanche.web.vo.AfterSaleKeepOnRecordVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author：HuangHao
 * @CreatTime 2021-08-05 14:06
 */
@Controller
@RequestMapping("/afterSale/keepOnRecord")
public class AfterSaleKeepOnRecordController extends BaseController {

    /**
     * 上传文件最大2M(B)
     */
    private final long MAX_FILE_SIZE = 2097152;
    @Reference
    AfterSaleActivityService afterSaleActivityService;
    @Reference(timeout = 30000, retries = 0)
    AfterSaleKeepOnRecordService afterSaleKeepOnRecordService;
    @Autowired
    AfterSaleKeepOnRecordExcelService afterSaleKeepOnRecordExcelService;
    @Reference
    FashionCarWinningNumberService fashionCarWinningNumberService;
    @Reference
    CsCompanyService csCompanyService;
    @Autowired
    @Qualifier("ClusterRedisService")
    private RedisService redisService;
    @Value("${after.sale.import.member.max.count}")
    private int maxImportAfterSaleCount;

    /**
     * 同时进行流失客户导入操作数(防止内存溢出)
     */
    private final String MAX_IMPORT_AFTER_SALE_COUNT_KEY = "aftersale:import:max:count";
    /**
     * 生存时间
     */
    private final int MAX_IMPORT_AFTER_SALE_COUNT_EXPIRE_KEY = 300;

    @RequestMapping("/keepOnRecordIndex")
    public String keepOnRecordIndex(ModelMap modelMap, Integer activityId) {
        AfterSaleActivityDto afterSaleActivityDto = afterSaleActivityService.getAfterSaleActivityDtoById(activityId);
        modelMap.addAttribute("afterSaleActivity", afterSaleActivityDto);
        return "after_sale/activity/keep-on-record-list";
    }

    /**
     * 根据ID获取记录
     *
     * @param id
     * @return com.tuanche.directselling.model.AfterSaleKeepOnRecord
     * @author HuangHao
     * @CreatTime 2021-08-05 11:10
     */
    @RequestMapping("/getById")
    @ResponseBody
    TcResponse getById(Integer id) {
        AfterSaleKeepOnRecord keepOnRecord = afterSaleKeepOnRecordService.getById(id);
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), 0, keepOnRecord, StatusCodeEnum.SUCCESS.getText());
    }

    /**
     * 获取备案记录列表-分页
     *
     * @param afterSaleKeepOnRecord
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleKeepOnRecord>
     * @author HuangHao
     * @CreatTime 2021-08-05 11:10
     */
    @RequestMapping("/list")
    @ResponseBody
    PageResult<AfterSaleKeepOnRecord> getKeepOnRecordListByPage(PageResult<AfterSaleKeepOnRecord> pageResult, AfterSaleKeepOnRecord keepOnRecord) {
        PageResult pageList = afterSaleKeepOnRecordService.getKeepOnRecordListByPage(pageResult, keepOnRecord);
        pageList.setData(BeanCopyUtil.copyListProperties(pageList.getData(), AfterSaleKeepOnRecordVo::new));
        pageList.setCode("0");
        return pageList;
    }

    @RequestMapping("/export")
    @ResponseBody
    public void export(HttpServletRequest request, HttpServletResponse response, AfterSaleKeepOnRecord keepOnRecord) {
        List<AfterSaleKeepOnRecord> keepOnRecordList = afterSaleKeepOnRecordService.getKeepOnRecordList(keepOnRecord);
        Map<String, String> titleValueMap = new LinkedHashMap<>();
        titleValueMap.put("CityName", "城市");
        titleValueMap.put("UserName", "姓名");
        titleValueMap.put("UserPhone", "手机号");
        titleValueMap.put("BrandName", "车辆品牌");
        titleValueMap.put("BrandId", "品牌id");
        titleValueMap.put("CarSeriesName", "车系");
        titleValueMap.put("CarSeriesId", "车系id");
        titleValueMap.put("DealerName", "经销商全称");
        titleValueMap.put("DealerId", "经销商id");
        titleValueMap.put("LicencePlate", "车牌号");
        titleValueMap.put("BusinessId", "业务id");
        titleValueMap.put("BuyCarTime", "购车时间");
        titleValueMap.put("LatestEnterTime", "最后进店时间");
        titleValueMap.put("DataSource", "数据源");
        List<Integer> dealerIds = new ArrayList<>();
        List<Integer> cityIds = new ArrayList<>();
        for (AfterSaleKeepOnRecord record : keepOnRecordList) {
            dealerIds.add(record.getDealerId());
        }
        Map<Integer, CsCompany> companyMap = null;
        Map<Integer, String> cityMap = null;
        if (!dealerIds.isEmpty()) {
            List<CsCompany> companiesByDealerIds = csCompanyService.getCompanysByDealerIds(dealerIds);
            companyMap = companiesByDealerIds.stream().collect(Collectors.toMap(CsCompany::getId, Function.identity(), (key1, key2) -> key2));
            for (CsCompany csCompany : companiesByDealerIds) {
                cityIds.add(csCompany.getCityId());
            }
        }
        if (!cityIds.isEmpty()) {
            List<DistrictOutputExpandDto> district = iDistrictApiService.getDistrict(cityIds, null);
            cityMap = district.stream().collect(Collectors.toMap(DistrictOutputExpandDto::getId, DistrictOutputExpandDto::getName, (key1, key2) -> key2));
        }

        List<AfterSaleKeepOnRecordDto> dtos = new ArrayList();
        AfterSaleKeepOnRecordDto dto = null;
        for (AfterSaleKeepOnRecord record : keepOnRecordList) {
            dto = new AfterSaleKeepOnRecordDto();
            BeanUtils.copyProperties(record, dto);
            if (companyMap != null && companyMap.get(record.getDealerId()) != null) {
                CsCompany company = companyMap.get(record.getDealerId());
                if (cityMap != null) {
                    String cityName = cityMap.get(company.getCityId());
                    if (cityName != null) {
                        dto.setCityName(cityName);
                    }
                }
                dto.setDealerName(company.getCompanyName());
            }

            dto.setBusinessId(27);
            dtos.add(dto);
        }
        Map<String, ExportExcelCallback> callBackMap = new HashMap<>(16);
        callBackMap.put("BuyCarTime", (ExportExcelCallback<AfterSaleKeepOnRecord>) object -> {
            if (object.getBuyCarTime() == null) {
                return "";
            } else {
                return DateUtil.formart(object.getBuyCarTime(), DateUtil.FORMART_YMD);
            }

        });
        callBackMap.put("LatestEnterTime", (ExportExcelCallback<AfterSaleKeepOnRecord>) object -> {
            if (object.getLatestEnterTime() == null) {
                return "";
            } else {
                return DateUtil.formart(object.getLatestEnterTime(), DateUtil.FORMART_YMD);
            }
        });
        callBackMap.put("SyncStatus", (ExportExcelCallback<AfterSaleKeepOnRecord>) object -> {
            if (object.getSyncStatus() == 0) {
                return "未同步";
            }
            if (object.getSyncStatus() == 1) {
                return "已同步";
            }
            return "";
        });
        callBackMap.put("SyncTime", (ExportExcelCallback<AfterSaleKeepOnRecord>) object -> {
            if (object.getSyncTime() == null) {
                return "";
            } else {
                return DateUtil.formart(object.getSyncTime(), DateUtil.FORMART_YMD_HMS);
            }
        });
        callBackMap.put("DataSource", (ExportExcelCallback<AfterSaleKeepOnRecord>) object -> {
            if (object.getDataSource() != null) {
                if (object.getDataSource() == 0) {
                    return "本店";
                }
                if (object.getDataSource() == 1) {
                    return "他店";
                }
            }
            return "";
        });
        ExportExcel.exportExcel("流失用户导出.xls", titleValueMap, callBackMap, dtos, request, response);
    }

    /**
     * 品牌车系匹配列表
     *
     * @param pageResult
     * @param keepOnRecord
     * @return
     */
    @RequestMapping("/brandCarSeriesList")
    @ResponseBody
    PageResult<AfterSaleKeepOnRecordBrandCarSeriesVo> getKeepOnRecordBrandCarSeriesListByPage(PageResult<AfterSaleKeepOnRecordBrandCarSeriesDto> pageResult, AfterSaleKeepOnRecordBrandCarSeriesDto keepOnRecord) {
        PageResult pageList = afterSaleKeepOnRecordService.getKeepOnRecordBrandCarSeriesListByPage(pageResult, keepOnRecord);
        pageList.setData(BeanCopyUtil.copyListProperties(pageList.getData(), AfterSaleKeepOnRecordBrandCarSeriesVo::new));
        pageList.setCode("0");
        return pageList;
    }

    /**
     * 删除
     *
     * @param id
     * @return int
     * @author HuangHao
     * @CreatTime 2021-08-05 14:01
     */
    @RequestMapping("/deleteById")
    @ResponseBody
    TcResponse deleteById(Integer id) {
        afterSaleKeepOnRecordService.deleteById(id);
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText());
    }

    /**
     * 删除
     *
     * @param id
     * @return int
     * @author HuangHao
     * @CreatTime 2021-08-05 14:01
     */
    @RequestMapping("/deleteAll")
    @ResponseBody
    TcResponse deleteAll(Integer activityId, Integer userType) {
        return afterSaleKeepOnRecordService.deleteByActivityIdAndUserType(activityId, userType);
    }

    /**
     * 更新
     *
     * @param afterSaleKeepOnRecord
     * @return int
     * @author HuangHao
     * @CreatTime 2021-08-05 14:02
     */
    @RequestMapping("/updateById")
    @ResponseBody
    TcResponse updateById(AfterSaleKeepOnRecord afterSaleKeepOnRecord) {
        return afterSaleKeepOnRecordService.updateById(afterSaleKeepOnRecord);
    }

    /**
     * 编辑品牌车型
     *
     * @param afterSaleKeepOnRecord
     * @return
     */
    @RequestMapping("/updateBrandCarSeriesById")
    @ResponseBody
    TcResponse updateBrandCarSeriesById(AfterSaleKeepOnRecord afterSaleKeepOnRecord) {
        return afterSaleKeepOnRecordService.updateBrandCarSeriesById(afterSaleKeepOnRecord);
    }

    /**
     * 导入备案用户
     *
     * @param file
     * @return com.tuanche.arch.web.model.TcResponse
     * @author HuangHao
     * @CreatTime 2020-06-01 18:14
     */
    @RequestMapping("/keepOnRecordUpload")
    @ResponseBody
    public TcResponse refuelcardUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file, Integer activityId, Integer userType) throws Exception {
        Long incr = redisService.incr(MAX_IMPORT_AFTER_SALE_COUNT_KEY);
        if (incr.intValue() > maxImportAfterSaleCount) {
            return new TcResponse(StatusCodeEnum.SYSTEM_INNER_ERROR.getCode(), "当前导入服务繁忙,请5分钟之后再试");
        }
        redisService.expire(MAX_IMPORT_AFTER_SALE_COUNT_KEY, MAX_IMPORT_AFTER_SALE_COUNT_EXPIRE_KEY);
        String name = file.getOriginalFilename();
        if (!RefuelCardExcelUtil.isExcel(name)) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "只支持.xls和.xlsx的excel文件");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "文件大小不可以超过2MB");
        }
        XxlUser xxlUser = getLoginUser(request);
        AfterSaleActivity activity = afterSaleActivityService.getAfterSaleActivityById(activityId);
        AfterSaleKeepOnRecordExcelService.ExcelList<AfterSaleKeepOnRecord> excelList = afterSaleKeepOnRecordExcelService.analysis(file, activity.getDealerId(), activityId, xxlUser.getEmpName(), afterSaleKeepOnRecordService.keepOnRecordMap(activityId), userType);
        //有效数据
        List<AfterSaleKeepOnRecord> list = excelList.getData();
        int successNum = 0;
        if (list.size() > 0) {
            successNum = afterSaleKeepOnRecordService.batchInsert(list);
        }
        //成功条数
        //int successNum = insertNum;
        String br = "<br>";
        List<String> invalidData = excelList.getInvalidData();
        StringBuilder msg = new StringBuilder();
        msg.append("上传总数：");
        msg.append(excelList.getTotal() > 0 ? excelList.getTotal() - 1 : 0);
        msg.append("条");
        msg.append(br);
        msg.append("上传成功：");
        msg.append(successNum);
        msg.append("条，上传失败：");
        //失败条数
        int invalidNum = invalidData.size();
        Set<String> set = new LinkedHashSet<>();
        set.addAll(invalidData);
        msg.append(invalidNum);
        msg.append("条");
        msg.append(br);
        if (invalidNum > 0) {
            msg.append("失败原因：");
            msg.append(br);
            for (String r : set) {
                msg.append(r);
                msg.append(br);
            }
        }
        redisService.decr(MAX_IMPORT_AFTER_SALE_COUNT_KEY);
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), msg.toString());

    }


    /**
     * 同步数据
     *
     * @param afterSaleKeepOnRecord
     * @return
     */
    @RequestMapping("/syncData")
    @ResponseBody
    TcResponse syncData(AfterSaleKeepOnRecord afterSaleKeepOnRecord) {
        return afterSaleKeepOnRecordService.syncData(afterSaleKeepOnRecord);
    }

    @RequestMapping("/getSyncDataStatus")
    @ResponseBody
    TcResponse getSyncDataStatus(AfterSaleKeepOnRecord afterSaleKeepOnRecord) throws Exception {
        long t1 = System.currentTimeMillis();
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), System.currentTimeMillis() - t1, StatusCodeEnum.SUCCESS.getText(), afterSaleKeepOnRecordService.getSyncDataStatus(afterSaleKeepOnRecord));
    }

    /**
     * 同步数据
     *
     * @param afterSaleKeepOnRecord
     * @return
     */
    @RequestMapping("/getUnmatchedData")
    @ResponseBody
    TcResponse getUnmatchedData(AfterSaleKeepOnRecord afterSaleKeepOnRecord) {
        long t1 = System.currentTimeMillis();
        List<AfterSaleKeepOnRecordBrandCarSeriesDto> unmatchedData = afterSaleKeepOnRecordService.getUnmatchedData(afterSaleKeepOnRecord);
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), System.currentTimeMillis() - t1, StatusCodeEnum.SUCCESS.getText(), unmatchedData);
    }


}
