package com.tuanche.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.arch.util.utils.DateUtils;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.api.FissionActivityGoodsRecordService;
import com.tuanche.directselling.api.FissionActivityService;
import com.tuanche.directselling.api.FissionGoodsHelperService;
import com.tuanche.directselling.api.LiveSceneService;
import com.tuanche.directselling.model.FissionActivityGoodsRecord;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.eap.api.utils.ResultDto;
import com.tuanche.manubasecenter.api.CarBaseService;
import com.tuanche.manubasecenter.dto.TcResponseCode;
import com.tuanche.merchant.business.api.commodity.ICommodityBusinessService;
import com.tuanche.merchant.business.dto.request.commodity.CommodityWithBusinessQueryRequestDto;
import com.tuanche.merchant.business.dto.response.BaseResponseDto;
import com.tuanche.merchant.business.dto.response.commodity.CommodityResponseDto;
import com.tuanche.merchant.pojo.dto.commodity.PageableDto;
import com.tuanche.merchant.pojo.dto.enums.BusinessTypeEnum;
import com.tuanche.merchant.pojo.dto.enums.CommodityStatusEnum;
import com.tuanche.merchant.pojo.dto.enums.ResultEnum;
import com.tuanche.web.service.PeriodsService;
import com.tuanche.web.util.DirectCommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: direct-selling
 * @description: ${description}
 * @author: fxq
 * @create: 2020-09-25 11:09
 **/

@Controller
@RequestMapping("/fission/goods")
public class FissionGoodsController {

    @Reference
    private LiveSceneService liveSceneService;
    @Reference
    private ICommodityBusinessService iCommodityBusinessService;
    @Autowired
    private PeriodsService periodsService;
    @Reference
    private FissionGoodsHelperService fissionGoodsHelperService;
    @Reference
    private FissionActivityService fissionActivityService;
    @Reference
    CarBaseService carBaseService;
    @Reference
    FissionActivityGoodsRecordService fissionActivityGoodsRecordService;

    /**
     * @description: 跳转裂变商品列表页
     * @param:
     * @return:
     * @author: fxq
     * @date: 2020/9/25 11:49
     */
    @RequestMapping("/toFissionGoodsList")
    public String toFissionGoodsList(ModelMap modelMap, Integer fissionId, Integer periodsId) {
        modelMap.addAttribute("fissionId", fissionId);
        modelMap.addAttribute("periodsId", periodsId);
        return "fission/activity/config-goods-list";
    }

    /**
     * @description: 获取能够关联的裂变商品列表页
     * @param:
     * @return:
     * @author: fxq
     * @date: 2020/9/25 11:14
     */
    @RequestMapping("/getFissionGoodsList")
    @ResponseBody
    public PageResult getFissionGoodsList(PageResult<CommodityWithBusinessQueryRequestDto> pageResult, CommodityWithBusinessQueryRequestDto queryRequestDto, String goodsAddDate, Integer commodityId) {
        PageResult PageList = new PageResult();
        try {
            //已关联商品
            ResultDto result = getFissionGoodsIdList(queryRequestDto.getActivityId());
            if (result!=null && result.getResult()!=null) {
                List<Integer> ids = (List<Integer>) result.getResult();
                if (!CollectionUtils.isEmpty(ids)) queryRequestDto.setExcludedCommodityIds(ids);
            }
            //未关联商品（根据条件搜索）
            queryRequestDto.setPageIndex(pageResult.getPage());
            queryRequestDto.setPageSize(pageResult.getLimit());
            queryRequestDto.setCommodityStatusEnum(CommodityStatusEnum.ONSALE);
            queryRequestDto.setBusinessTypeEnum(BusinessTypeEnum.FISSION);
            if (!StringUtil.isEmpty(goodsAddDate)) queryRequestDto.setBeginCreateDt(DateUtils.stringToDate(goodsAddDate, DateUtils.YYYY_MM_DD_HH_MM_SS));
            queryRequestDto.setActivityId(null);
            queryRequestDto.setCommodityIds(commodityId==null?null : Arrays.asList(new Integer[]{commodityId}));
            BaseResponseDto<PageableDto<CommodityResponseDto>> responseDto = iCommodityBusinessService.getCommodityList(queryRequestDto);
            if (responseDto.getCode().equals(ResultEnum.SUCCESS.getCode()) && responseDto.getData() !=null && !CollectionUtils.isEmpty(responseDto.getData().getList())) {
                PageList.setCount(responseDto.getData().getTotalCount());
                PageList.setData(responseDto.getData().getList());
            } else {
                PageList.setCount(0);
                PageList.setData(null);
            }
        } catch (Exception e) {
            DirectCommonUtil.addError("FissionGoodsController", "getFissionGoodsList", "获取裂变商品列表页 error", e);
        }
        PageList.setCode("0");
        return PageList;
    }

    /**
     * @description: 跳转商品添加页面
     * @param:
     * @return:
     * @author: fxq
     * @date: 2020/9/28 15:45
     */
    @RequestMapping("/toAddGoods")
    public String toAddGoods(ModelMap modelMap, Integer goodsId, Integer fissionId) {
        modelMap.addAttribute("fissionId" , fissionId);
        return "fission/activity/config-goods-edit2";
    }

    /**
      * @description : 根据裂变活动id查询关联的商品
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/3/8 18:26
      */
    @RequestMapping("/getFissionGoodsListByFissionId")
    @ResponseBody
    public PageResult getFissionGoodsListByFissionId(PageResult<CommodityWithBusinessQueryRequestDto> pageResult, CommodityWithBusinessQueryRequestDto queryRequestDto) {
        PageResult PageList = new PageResult();
        try {
            if (queryRequestDto.getActivityId()!=null) {
                FissionActivityGoodsRecord goodsRecord = new FissionActivityGoodsRecord();
                goodsRecord.setFissionId(queryRequestDto.getActivityId());
                List<FissionActivityGoodsRecord> goodsRecordList = fissionActivityGoodsRecordService.getFissionActivityGoodsRecordList(goodsRecord);
                if (!CollectionUtils.isEmpty(goodsRecordList)) {
                    queryRequestDto.setPageIndex(pageResult.getPage());
                    queryRequestDto.setPageSize(pageResult.getLimit());
                    queryRequestDto.setCommodityStatusEnum(null);
                    queryRequestDto.setBusinessTypeEnum(BusinessTypeEnum.FISSION);
                    List<Integer> commodityIds = new ArrayList<>();
                    goodsRecordList.forEach(v->{commodityIds.add(v.getGoodsId());});
                    queryRequestDto.setCommodityIds(commodityIds);
                    queryRequestDto.setActivityId(null);
                    BaseResponseDto<PageableDto<CommodityResponseDto>> responseDto = iCommodityBusinessService.getCommodityList(queryRequestDto);
                    if (responseDto.getCode().equals(ResultEnum.SUCCESS.getCode()) && responseDto.getData() !=null) {
                        PageList.setCount(Long.valueOf(responseDto.getData().getTotalCount()));
                        PageList.setData(responseDto.getData().getList());
                    }
                }
            }

        } catch (Exception e) {
            DirectCommonUtil.addError("FissionGoodsController", "getFissionGoodsListByFissionId", "根据裂变活动id查询关联的商品 error", e);
        }
        PageList.setCode("0");
        return PageList;
    }

    /**
      * @description : 获取裂变活动关联商品
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/3/23 14:30
      */
    @RequestMapping("/getFissionGoodsIdList")
    @ResponseBody
    public ResultDto getFissionGoodsIdList(Integer fissionId) {
        ResultDto dto = new ResultDto();
        dto.setCode(TcResponseCode.OK.getIndex());
        List<Integer> ids = new ArrayList<>();
        try {
            if (fissionId!=null) {
                FissionActivityGoodsRecord goodsRecord = new FissionActivityGoodsRecord();
                goodsRecord.setFissionId(fissionId);
                List<FissionActivityGoodsRecord> goodsRecordList = fissionActivityGoodsRecordService.getFissionActivityGoodsRecordList(goodsRecord);
                if (!CollectionUtils.isEmpty(goodsRecordList)) {
                    ids = goodsRecordList.stream().map(FissionActivityGoodsRecord::getGoodsId).collect(Collectors.toList());
                }
            } else {
                dto = DirectCommonUtil.addParamNull();
            }
        } catch (Exception e) {
            dto = DirectCommonUtil.addError("FissionGoodsController","getFissionGoodsIdList", "获取裂变活动关联商品 error", e);
        }
        dto.setResult(ids);
        return dto;
    }

    /**
      * @description : 添加裂变活动关联商品
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/3/8 18:31
      */
    @RequestMapping("/addActivityGoodsRecord")
    @ResponseBody
    public ResultDto addActivityGoodsRecord(Integer fissionId, String goodsIds) {
        ResultDto dto = new ResultDto();
        dto.setCode(TcResponseCode.OK.getIndex());
        try {
            if (fissionId !=null && fissionId>0) {
                int n = fissionActivityGoodsRecordService.addActivityGoodsRecord(fissionId, goodsIds);
                if (n==0) {
                    dto = DirectCommonUtil.addParamNull();
                }
            } else {
                dto = DirectCommonUtil.addResultInfo(TcResponseCode.RESPONSE.getIndex(), "添加失败");
            }
        } catch (Exception e) {
            dto = DirectCommonUtil.addError("FissionGoodsController","addActivityGoodsRecord", "添加裂变活动关联商品 error", e);
        }
        return dto;
    }


    /**
      * @description : 删除已关联商品
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/3/23 12:16
      */
    @RequestMapping("/delGoods")
    @ResponseBody
    public ResultDto delGoods(Integer fissionId, Integer goodsId) {
        ResultDto dto = new ResultDto();
        dto.setCode(TcResponseCode.OK.getIndex());
        dto.setMsg("成功");
        try {
            if (fissionId !=null && fissionId>0 && goodsId !=null && goodsId>0) {
                fissionActivityGoodsRecordService.delActivityGoodsRecord(fissionId, goodsId);
            } else {
                dto = DirectCommonUtil.addParamNull();
            }
        } catch (Exception e) {
            dto = DirectCommonUtil.addError("FissionGoodsController","editActivityGoodsRecord", "删除已关联商品 error", e);
        }
        return dto;
    }


}
