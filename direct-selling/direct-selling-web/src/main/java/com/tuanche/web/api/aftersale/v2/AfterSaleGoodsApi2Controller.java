package com.tuanche.web.api.aftersale.v2;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.directselling.api.AfterSaleActivityPackageService;
import com.tuanche.directselling.dto.AfterSaleActivityPackageDto;
import com.tuanche.directselling.dto.FissionGoodsImageDto;
import com.tuanche.directselling.model.AfterSaleActivityPackage;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.manubasecenter.api.CarBaseService;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.dto.CsCompanyDetailDto;
import com.tuanche.manubasecenter.dto.TcResponse;
import com.tuanche.merchant.business.api.commodity.ICommodityBusinessService;
import com.tuanche.merchant.business.dto.request.commodity.CommodityWithBusinessQueryRequestDto;
import com.tuanche.merchant.business.dto.response.BaseResponseDto;
import com.tuanche.merchant.business.dto.response.commodity.CommodityBusinessResponseDto;
import com.tuanche.merchant.business.dto.response.commodity.CommodityDetailBusinessResponseDto;
import com.tuanche.merchant.business.dto.response.commodity.CommodityResponseDto;
import com.tuanche.merchant.pojo.dto.commodity.PageableDto;
import com.tuanche.merchant.pojo.dto.enums.BusinessTypeEnum;
import com.tuanche.merchant.pojo.dto.enums.CommodityStatusEnum;
import com.tuanche.merchant.pojo.dto.enums.ServiceTypeEnum;
import com.tuanche.storage.dto.brand.CarBrandDto;
import com.tuanche.web.util.DirectCommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/after/sale/goods")
public class AfterSaleGoodsApi2Controller {

    @Reference
    private AfterSaleActivityPackageService afterSaleActivityPackageService;
    @Reference
    private ICommodityBusinessService iCommodityBusinessService;
    @Reference
    private CompanyBaseService companyBaseService;
    @Reference
    private CarBaseService carBaseService;

    /**
      * @description : 套餐列表
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/8/5 11:33
      */
    @RequestMapping("/list")
    public TcResponse list(PageResult<AfterSaleActivityPackageDto> pageResult, AfterSaleActivityPackage afterSaleActivityPackage) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "AfterSaleGoodsApiController","list",  "套餐列表 start ", JSON.toJSONString(afterSaleActivityPackage));
        PageResult<AfterSaleActivityPackageDto> pageList = new PageResult<>();
        try {
//            pageList = afterSaleActivityPackageService.getAfterSaleActivityPackageByPage(pageResult, afterSaleActivityPackage);
//            if (pageList==null || CollectionUtils.isEmpty(pageList.getData())) {
//                return DirectCommonUtil.returnResultNull("AfterSaleGoodsApiController", "list", "套餐列表无数据", afterSaleActivityPackage, st);
//            }
            List<AfterSaleActivityPackageDto> activityPackageList = afterSaleActivityPackageService.getAfterSaleActivityPackageList(afterSaleActivityPackage);
            if (CollectionUtils.isEmpty(activityPackageList)) {
                return DirectCommonUtil.returnResultNull("AfterSaleGoodsApiController", "list", "套餐列表无数据", afterSaleActivityPackage, st);
            }
            Map<Integer, Integer> goodsActivityMap = new HashMap<>();
            List<Integer> goodsIds = new ArrayList<>();
            activityPackageList.forEach(v->{
                goodsActivityMap.put(v.getGoodsId(), v.getActivityId());
                goodsIds.add(v.getGoodsId());
            });

            CommodityWithBusinessQueryRequestDto queryRequestDto = new CommodityWithBusinessQueryRequestDto();
            queryRequestDto.setCommodityIds(goodsIds);
            queryRequestDto.setBusinessTypeEnum(BusinessTypeEnum.SALE);
            queryRequestDto.setServiceTypeEnum(ServiceTypeEnum.BUSINESS);
            queryRequestDto.setCommodityStatusEnum(CommodityStatusEnum.ONSALE);
            queryRequestDto.setPageIndex(pageResult.getPage());
            queryRequestDto.setPageSize(pageResult.getLimit());
            BaseResponseDto<PageableDto<CommodityBusinessResponseDto>> commodityList = iCommodityBusinessService.getCommodityList(queryRequestDto);
            if(commodityList.getData()!=null && CollectionUtils.isNotEmpty(commodityList.getData().getList())) {
                Map<Integer, CommodityResponseDto> goodsMap = new HashMap<>();
                commodityList.getData().getList().forEach(v->{
                    goodsMap.put(v.getCommodityId(), v);
                });
                pageList.setPage(pageResult.getPage());
                pageList.setLimit(pageResult.getLimit());
                pageList.setCount(commodityList.getData().getTotalCount());
                pageList.setCode(200);
                List<AfterSaleActivityPackageDto> packageDtos = new ArrayList<>();
//                commodityList.getData().getList().forEach(v->{
//                    AfterSaleActivityPackageDto packageDto = new AfterSaleActivityPackageDto();
//                    BeanUtils.copyProperties(v, packageDto);
//                    packageDto.setGoodsId(v.getCommodityId());
//                    packageDtos.add(packageDto);
//                });
                commodityList.getData().getList().forEach(v->{
                    AfterSaleActivityPackageDto packageDto = new AfterSaleActivityPackageDto();
                    packageDto.setGoodsId(v.getCommodityId());
                    packageDto.setActivityId(goodsActivityMap.get(v.getCommodityId()));
                    packageDto.setCommodityName(v.getCommodityName());
                    packageDto.setCommodityPrice(v.getCommodityPrice());
                    packageDto.setOriginalPrice(v.getOriginalPrice());
                    packageDto.setSoldNumber(v.getSoldNumber());
                    List<FissionGoodsImageDto> headImages = JSON.parseArray(JSON.toJSONString(v.getHeadImages()), FissionGoodsImageDto.class);
                    packageDto.setHeadImages(headImages);
                    packageDtos.add(packageDto);
                });
                pageList.setData(packageDtos);
            }
        }catch (Exception e) {
            return DirectCommonUtil.addErrorLog("AfterSaleGoodsApiController", "售后特卖已购买推广卡列表 error", e, st,JSON.toJSONString(afterSaleActivityPackage));
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "AfterSaleGoodsApiController","list",  "套餐列表 end ", System.currentTimeMillis()-st);
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, pageList);
    }

    /**
      * @description : 套餐详情
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/8/5 11:45
      */
    @RequestMapping("/details")
    public TcResponse details(Integer goodsId, Integer activityId) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "AfterSaleGoodsApiController","details",  "套餐详情 start ", goodsId);
        AfterSaleActivityPackageDto packageDto = new AfterSaleActivityPackageDto();
        try {
            if (goodsId==null || activityId==null) {
                return DirectCommonUtil.returnParamError("AfterSaleGoodsApiController", "details", "参数为空", goodsId, st);
            }
            AfterSaleActivityPackage activityPackage = new AfterSaleActivityPackage();
            activityPackage.setActivityId(activityId);
            activityPackage.setGoodsId(goodsId);
            List<AfterSaleActivityPackageDto> packageList = afterSaleActivityPackageService.getAfterSaleActivityPackageList(activityPackage);
            if (CollectionUtils.isNotEmpty(packageList)){
                packageDto = packageList.get(0);
                if (packageDto.getBrandId()!=null) {
                    CarBrandDto brand = carBaseService.getBrandById(packageDto.getBrandId());
                    packageDto.setMasterBrandId(brand.getCmId());
                }
                BaseResponseDto<CommodityDetailBusinessResponseDto> commodityDetailWithExtend = iCommodityBusinessService.getCommodityDetailWithExtendByCommodityId(goodsId);
                if (commodityDetailWithExtend!=null && commodityDetailWithExtend.getData()!=null && commodityDetailWithExtend.getData().getCommodityId()!=null) {
                    CommodityDetailBusinessResponseDto commodity = commodityDetailWithExtend.getData();
                    packageDto.setCommodityName(commodity.getCommodityName());
                    List<FissionGoodsImageDto> headImages = JSON.parseArray(JSON.toJSONString(commodity.getHeadImages()), FissionGoodsImageDto.class);
                    packageDto.setHeadImages(headImages);
                    List<FissionGoodsImageDto> detailImages = JSON.parseArray(JSON.toJSONString(commodity.getDetailImages()), FissionGoodsImageDto.class);
                    packageDto.setDetailImages(detailImages);
                    packageDto.setSoldNumber(commodity.getSoldNumber());
                    packageDto.setCommodityCount(commodity.getCommodityCount());
                    packageDto.setCommodityPrice(commodity.getCommodityPrice());
                    packageDto.setOriginalPrice(commodity.getOriginalPrice());
                    packageDto.setDescription(commodity.getDescription());
                }
                if (packageDto.getDealerId()!=null) {
                    CsCompanyDetailDto company = companyBaseService.getCsCompanyDetail(packageDto.getDealerId());
                    if (company!=null) {
                        packageDto.setDealerAddress(company.getAddress());
                        packageDto.setDealerName(company.getCompanyName());
                        packageDto.setDealerTel(company.getTel());
                        packageDto.setShortName(company.getShortName());
                    }
                }
            } else {
                return DirectCommonUtil.returnResultNull("AfterSaleGoodsApiController", "details", "套餐详情无数据", goodsId+"--"+activityId, st);
            }
        }catch (Exception e) {
            return DirectCommonUtil.addErrorLog("AfterSaleGoodsApiController", "套餐详情 error", e, st,goodsId+"");
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, DirectCommonUtil.SYSTEM_NAME, "AfterSaleGoodsApiController","details",  "套餐列表 end ", System.currentTimeMillis()-st);
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, packageDto);
    }



}
