package com.tuanche.web.api.fission;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.DateUtils;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.broadcast.rpc.vo.BroadcastRoomResponseDto;
import com.tuanche.directselling.api.FissionActivityGoodsRecordService;
import com.tuanche.directselling.api.FissionActivityService;
import com.tuanche.directselling.api.FissionGoodsHelperService;
import com.tuanche.directselling.api.LiveSceneService;
import com.tuanche.directselling.dto.*;
import com.tuanche.directselling.model.FissionActivity;
import com.tuanche.directselling.model.FissionActivityGoodsRecord;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.LiveSceneVo;
import com.tuanche.manubasecenter.api.CarBaseService;
import com.tuanche.manubasecenter.dto.TcResponse;
import com.tuanche.merchant.business.api.commodity.ICommodityBusinessService;
import com.tuanche.merchant.business.api.order.IOrderService;
import com.tuanche.merchant.business.dto.request.commodity.CommodityQueryRequestDto;
import com.tuanche.merchant.business.dto.request.commodity.CommodityWithBusinessQueryRequestDto;
import com.tuanche.merchant.business.dto.response.BaseResponseDto;
import com.tuanche.merchant.business.dto.response.ImageResponseDto;
import com.tuanche.merchant.business.dto.response.commodity.CommodityBusinessResponseDto;
import com.tuanche.merchant.pojo.dto.commodity.PageableDto;
import com.tuanche.merchant.pojo.dto.enums.*;
import com.tuanche.storage.dto.brand.CarBrandDto;
import com.tuanche.web.service.FissionCommonServiceImpl;
import com.tuanche.web.util.DirectCommonUtil;
import com.tuanche.web.util.Globals;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/fission/user/goods")
public class FissionGoodsApiController {
    @Reference
    private ICommodityBusinessService iCommodityBusinessService;
    @Reference
    private FissionGoodsHelperService fissionGoodsHelperService;
    @Reference
    private FissionActivityService fissionActivityService;
    @Reference
    private IOrderService iOrderService;
    @Reference
    private CarBaseService carBaseService;
    @Autowired
    @Qualifier("ClusterRedisService")
    private RedisService redisService;
    @Autowired
    private FissionCommonServiceImpl fissionCommonService;
    @Reference
    private LiveSceneService liveSceneService;
    @Reference
    FissionActivityGoodsRecordService fissionActivityGoodsRecordService;
    //app商品详情链接
    @Value("${goods_details_url}")
    private String goods_details_url;

    /**
     * @description : 通过商品id获取商品详情
     * @param : fissionGoodsId  商品id
     * @return : 
     * @author : fxq
     * @date : 2020/10/13 11:42
     */
//    @RequestMapping("/getFissionGoodsById")
//    @ResponseBody
//    public TcResponse getFissionGoodsById (Integer fissionGoodsId, Integer fissionId, Integer roomId) {
//        long st = System.currentTimeMillis();
//        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionGoodsApiController","getFissionGoodsById",  "通过商品id获取商品详情 start " +st, fissionGoodsId+"");
//        Object result = "";
//        try {
//            if (fissionGoodsId==null || fissionGoodsId<1) {
//                return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "参数错误", st, 0);
//            }
////            if (fissionId!=null && fissionId>0) {
////                FissionActivityGoodsRecord goodsRecord = new FissionActivityGoodsRecord();
////                goodsRecord.setFissionId(fissionId);
////                goodsRecord.setGoodsId(fissionGoodsId);
////                List<FissionActivityGoodsRecord> goodsRecordList = fissionActivityGoodsRecordService.getFissionActivityGoodsRecordList(goodsRecord);
////                if (CollectionUtils.isEmpty(goodsRecordList)) {
////                    return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.RESULE_DATA_NONE.getCode(), "活动商品关联异常", st,
////                            "FissionGoodsApiController", "getFissionGoodsById", fissionGoodsId);
////                }
////
////            }
//            BaseResponseDto<CommodityDetailResponseDto> responseDto = iCommodityBusinessService.getCommodityDetailWithExtendByCommodityId(fissionGoodsId);
//            if (!responseDto.getCode().equals(ResultEnum.SUCCESS.getCode())) {
//                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.RESULE_DATA_NONE.getCode(), StatusCodeEnum.RESULE_DATA_NONE.getText(), st,
//                        "FissionGoodsApiController", "getFissionGoodsById", fissionGoodsId);
//            }
//            FissionGoods fissionGoods = new FissionGoods();
//            CommodityDetailResponseDto goods = responseDto.getData();
//            fissionGoods.setCommodityId(goods.getCommodityId());
//            fissionGoods.setCommodityName(goods.getCommodityName());
//            fissionGoods.setCommodityPrice(goods.getCommodityPrice());
//            fissionGoods.setOriginalPrice(goods.getOriginalPrice());
//            fissionGoods.setCommodityCount(goods.getCommodityCount());
//            fissionGoods.setVirtualSoldNumber(goods.getVirtualSoldNumber());
//            fissionGoods.setSoldNumber(goods.getSoldNumber());
//            fissionGoods.setTotalSoldNumber(goods.getTotalSoldNumber());
//            fissionGoods.setSeckill(goods.getCommodityType()== CommodityTypeEnum.SECKILL ? Globals.FISSION_GOODS.seckill : Globals.FISSION_GOODS.seckill_no);
//            fissionGoods.setIsPost(goods.isPost() ? Globals.FISSION_GOODS_ORDER.goods_post : Globals.FISSION_GOODS_ORDER.goods_checkout);
//            fissionGoods.setDescription(goods.getDescription());
//            fissionGoods.setHeadImages(convertFissionGoodsImageDto(goods.getHeadImages()));
//            fissionGoods.setDescpritionImages(convertFissionGoodsImageDto(goods.getDetailImages()));
//            fissionGoods.setPutAwayType(goods.getPutAwayType().equals(PutAwayTypeEnum.ONSALE) ? Globals.FISSION_GOODS.upShelf : Globals.FISSION_GOODS.upShelf_no);
//            //没有库存后查询是否有未付款商品
//            fissionGoods.setNonPay(getNonPay(roomId, goods, fissionGoodsId));
//            //查询商品关联的品牌车型列表
//            fissionGoods.setBrandDtoList(getBrandDtoList(goods));
//            //查询商品助力信息
//            fissionGoods.setIsHelper(goods.getNeedHelp());
//            fissionGoods.setHelperNum(goods.getHelpNumber());
////            fissionGoods = getGoodsHelper(fissionGoods);
//            //秒杀商品上下架状态额外判断直播间是否开始
//            fissionGoods = getPutAwayType(fissionGoods, goods.isSeckill(), fissionId);
//            fissionGoods.setFissionId(fissionId);
//            result = fissionGoods;
//        } catch (Exception e) {
//            return DirectCommonUtil.addErrorLog("FissionGoodsController", "通过商品id获取商品详情 error", e, st, fissionGoodsId+"");
//        }
//        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionGoodsApiController","getFissionGoodsById",  "通过商品id获取商品详情 end " +st, JSON.toJSONString(result));
//        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, result);
//    }

    /**
     * @description : 秒杀商品 直播间开播期间 为下架状态
     * @author : fxq
     * @param :
     * @return :
     * @date : 2021/5/18 16:05
     */
    @RequestMapping("/getGoodsPutAwayType")
    @ResponseBody
    public TcResponse getGoodsPutAwayType (Integer fissionId) {
        long st = System.currentTimeMillis();
        int result = Globals.FISSION_GOODS.upShelf;
        if (fissionId!=null && fissionId>0) {
            FissionActivity fissionActivity = fissionActivityService.getFissionActivityById(fissionId);
            BroadcastRoomResponseDto broadcastRoomInfo = fissionCommonService.getBroadcastRoomInfo(fissionActivity.getBroadcastRoomId());
            if (broadcastRoomInfo!=null && broadcastRoomInfo.getRoomStatus()!=null && !broadcastRoomInfo.getRoomStatus().equals(0)) {
                result = Globals.FISSION_GOODS.upShelf_no;
            }
        }
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, result);
    }

    //裂变商品没有库存后查询是否有未付款商品订单  1:是|有     0：否|无
//    private int getNonPay (Integer roomId, CommodityDetailResponseDto goods, Integer fissionGoodsId) {
//        Integer soldNumber = (roomId==null ? goods.getSoldNumber() : goods.getTotalSoldNumber());
//        if (goods.getCommodityCount()<=soldNumber) {
//            PageableRequestDto<FissionActivityCommodityOrderQueryRequestDto> dto = new PageableRequestDto<>();
//            FissionActivityCommodityOrderQueryRequestDto queryRequestDto = new FissionActivityCommodityOrderQueryRequestDto();
//            queryRequestDto.setTradeStatusEnum(OrderTradeStatusEnum.WAIT_BUYER_PAY);
//            queryRequestDto.setCommodityIdList(Arrays.asList(fissionGoodsId));
//            queryRequestDto.setIsExpired(Globals.FISSION_GOODS_ORDER.is_expired_non);
//            dto.setQuery(queryRequestDto);
//            dto.setBusinessTypeEnum(com.tuanche.merchant.pojo.dto.enums.BusinessTypeEnum.FISSION);
//            dto.setPageIndex(1);
//            dto.setPageSize(1);
//            BaseResponseDto<PageableDto<FissionActivityOrderWithExtendResponseDto>> orderresponseDto = iOrderService.listCommodityOrder(dto, FissionActivityOrderWithExtendResponseDto.class);
//            if (orderresponseDto.getCode().equals(ResultEnum.SUCCESS.getCode()) && orderresponseDto.getData()!=null && !CollectionUtils.isEmpty(orderresponseDto.getData().getList())) {
//                return Globals.FISSION_GOODS.goods_non_pay;
//            } else {
//                return Globals.FISSION_GOODS.goods_non_pay_no;
//            }
//        }
//        return Globals.FISSION_GOODS.goods_non_pay_no;
//    }
    //查询商品关联的品牌车型列表
//    private List<FissionBrandDto> getBrandDtoList (CommodityDetailResponseDto goods) {
//        List<FissionBrandDto> brandDtoList = new ArrayList<>();
//        if (!CollectionUtils.isEmpty(goods.getCars())) {
//            goods.getCars().forEach(car->{
//                List<FissionStyleDto> styleDtoList = new ArrayList<>();
//                if (!CollectionUtils.isEmpty(car.getStyleIds())) {
//                    Map<Integer, CarStyleDto> styleMap = carBaseService.getStyleMap(car.getStyleIds());
//                    car.getStyleIds().forEach(v->{
//                        styleDtoList.add(new FissionStyleDto(v, styleMap.get(v).getCsName()));
//                    });
//                }
//                brandDtoList.add(new FissionBrandDto(car.getBrandId(), carBaseService.getBrandName(car.getBrandId()), styleDtoList));
//            });
//        }
//        return brandDtoList;
//    }
    //查询商品助力信息
//    private FissionGoods getGoodsHelper (FissionGoods fissionGoods) {
//        FissionGoodsHelper goodsHelper = new FissionGoodsHelper();
//        goodsHelper.setGoodsId(fissionGoods.getCommodityId());
//        goodsHelper.setDeleteFlag(Globals.FISSION_GOODS.delete_no);
//        List<FissionGoodsHelper> fissionGoodsHelperList = fissionGoodsHelperService.getFissionGoodsHelperList(goodsHelper);
//        if (!CollectionUtils.isEmpty(fissionGoodsHelperList)) {
//            fissionGoods.setIsHelper(Globals.FISSION_GOODS.upShelf);
//            fissionGoods.setHelperNum(fissionGoodsHelperList.get(0).getHelperNum());
//        } else {
//            fissionGoods.setIsHelper(Globals.FISSION_GOODS.upShelf_no);
//        }
//        return fissionGoods;
//    }

    //秒杀商品上下架状态额外判断直播间是否开始
//    private FissionGoods getPutAwayType (FissionGoods fissionGoods, boolean seckill, Integer fissionId) {
//        if (seckill && fissionId!=null && fissionId>0) {
//            FissionActivity fissionActivity = fissionActivityService.getFissionActivityById(fissionId);
//            BroadcastRoomResponseDto broadcastRoomInfo = fissionCommonService.getBroadcastRoomInfo(fissionActivity.getBroadcastRoomId());
//            if (broadcastRoomInfo!=null && broadcastRoomInfo.getRoomStatus()!=null && !broadcastRoomInfo.getRoomStatus().equals(0)) {
//                fissionGoods.setPutAwayType(Globals.FISSION_GOODS.upShelf_no);
//            }
//        }
//        return fissionGoods;
//    }

    /**
     * @description : 获取裂变活动关联商品
     * @param : fissionId 活动id
     * @return : 
     * @author : fxq
     * @date : 2020/10/13 15:59
     */
    @RequestMapping("/getFissionGoodsList")
    @ResponseBody
    public TcResponse getFissionGoodsList (Integer fissionId) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionGoodsApiController","getFissionGoodsList",  "获取裂变活动关联商品 start " +st, fissionId+"");
        Object result = null;
        try {
            if (fissionId==null || fissionId<1) {
                return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "参数错误", st, result);
            }
            BaseResponseDto<PageableDto<CommodityBusinessResponseDto>> responseDto = null;
            FissionActivityGoodsRecord goodsRecord = new FissionActivityGoodsRecord();
            goodsRecord.setFissionId(fissionId);
            List<FissionActivityGoodsRecord> goodsRecordList = fissionActivityGoodsRecordService.getFissionActivityGoodsRecordList(goodsRecord);
            if (!CollectionUtils.isEmpty(goodsRecordList)) {
                List<Integer> commodityIds = goodsRecordList.stream().map(FissionActivityGoodsRecord :: getGoodsId).collect(Collectors.toList());
                CommodityQueryRequestDto queryRequestDto = new CommodityQueryRequestDto();
                queryRequestDto.setCommodityIds(commodityIds);
                queryRequestDto.setBusinessTypeEnum(BusinessTypeEnum.FISSION);
                queryRequestDto.setServiceTypeEnum(ServiceTypeEnum.BUSINESS);
                responseDto = iCommodityBusinessService.getCommodityList(queryRequestDto);
            }

            if (responseDto !=null && responseDto.getCode().equals(ResultEnum.SUCCESS.getCode()) && responseDto.getData()!=null) {
                result = responseDto.getData().getList();
            } else {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.RESULE_DATA_NONE.getCode(), StatusCodeEnum.RESULE_DATA_NONE.getText(), st,
                        "FissionGoodsApiController", "getFissionGoodsList", fissionId);
            }
        } catch (Exception e) {
            return DirectCommonUtil.addErrorLog("FissionGoodsController", "获取裂变活动关联商品 error", e, st, fissionId+"");
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionGoodsApiController","getFissionGoodsList",  "获取裂变活动关联商品 end " +st, JSON.toJSONString(result));
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, result);
    }
    
    private List<FissionGoodsImageDto> convertFissionGoodsImageDto (List<ImageResponseDto> imageRequestDtos) {
        List<FissionGoodsImageDto> dtos = null;
        if (!CollectionUtils.isEmpty(imageRequestDtos)) {
            dtos = JSON.parseArray(JSON.toJSONString(imageRequestDtos), FissionGoodsImageDto.class);
        }
        return dtos;
    }

    /**
      * @description : 获取城市下活动商品
      * @author : fxq
      * @param : defaultCity 1：查询城市  0：不查询城市   默认查城市
      * @return :
      * @date : 2021/1/19 14:45
      */
    @RequestMapping("/getFissionGoodsByCityId")
    @ResponseBody
    public TcResponse getFissionGoodsByCityId (Integer cityId, Integer defaultCity,Integer brandId) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionGoodsApiController","getFissionGoodsByCityId",  "获取城市下活动商品 start " +st, cityId+"");
        List<FissionGoodsScene> list = new ArrayList<>();
        String rediskey = "fission:goods:cityid:brandId:"+cityId+":"+brandId;
        if ((cityId==null || cityId<1) && (defaultCity==null || defaultCity==1)) {
            return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "参数错误", st, "FissionGoodsApiController", "getFissionGoodsByCityId", "");
        }
        try {
            if (redisService.exists(rediskey)) {
                list = JSON.parseArray(redisService.get(rediskey, String.class), FissionGoodsScene.class);
                if (CollectionUtils.isEmpty(list)) {
                    return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.RESULE_DATA_NONE.getCode(), "无关联商品", st, "FissionGoodsApiController", "getFissionGoodsByCityId", "");
                }
                return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, list);
            }
        }catch (Exception e) {}
        try {
            //查询城市下关联的直卖活动
            Map<Integer, LiveSceneDto> liveSceneDtoMap = getLiveSceneDtoMap(cityId, brandId);
            if (CollectionUtils.isEmpty(liveSceneDtoMap)) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.RESULE_DATA_NONE.getCode(), "无直卖活动", st, "FissionGoodsApiController", "getFissionGoodsByCityId", "");
            }
            List<Integer> sceneIds = new ArrayList<>();
            liveSceneDtoMap.values().forEach(v->{
                if (!CollectionUtils.isEmpty(v.getBrandIds())) {
                    sceneIds.add(v.getId());
                }
            });
            if (CollectionUtils.isEmpty(sceneIds)) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.RESULE_DATA_NONE.getCode(), "直卖活动未关联品牌", st, "FissionGoodsApiController", "getFissionGoodsByCityId", "");
            }
            //查询直卖活动关联的裂变活动
            Map<Integer, FissionActivityDto> fissionActivityDtoMap = getFissionActivityDtoMap(sceneIds);
            if (CollectionUtils.isEmpty(fissionActivityDtoMap)) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.RESULE_DATA_NONE.getCode(), "直卖活动无关联裂变活动", st, "FissionGoodsApiController", "getFissionGoodsByCityId", "");
            }
            // key : 裂变id-品牌id      val: map<goodsId, goods>
            Map<String, Map<Integer, CommodityBusinessResponseDto>> fissionBrandGoodsMap = new HashMap<>();
            fissionActivityDtoMap.values().forEach(v->{
                FissionActivityGoodsRecord goodsRecord = new FissionActivityGoodsRecord();
                goodsRecord.setFissionId(v.getId());
                List<FissionActivityGoodsRecord> goodsRecordList = fissionActivityGoodsRecordService.getFissionActivityGoodsRecordList(goodsRecord);
                if (!CollectionUtils.isEmpty(goodsRecordList)) {
                    CommodityWithBusinessQueryRequestDto queryRequestDto = new CommodityWithBusinessQueryRequestDto();
                    List<Integer> commodityIds = goodsRecordList.stream().map(FissionActivityGoodsRecord :: getGoodsId).collect(Collectors.toList());
                    queryRequestDto.setCommodityIds(commodityIds);
                    queryRequestDto.setPageIndex(1);
                    queryRequestDto.setPageSize(10000);
                    queryRequestDto.setCommodityStatusEnum(CommodityStatusEnum.ONSALE);
                    queryRequestDto.setCommodityTypes(Arrays.asList(CommodityTypeEnum.GENERAL));
                    queryRequestDto.setBusinessTypeEnum(BusinessTypeEnum.FISSION);
                    queryRequestDto.setServiceTypeEnum(ServiceTypeEnum.BUSINESS);
                    if (brandId==null) {
                        LiveSceneDto sceceDto = liveSceneDtoMap.get(v.getSceneId());
                        queryRequestDto.setBrandIds(sceceDto.getBrandIds());
                    } else {
                        queryRequestDto.setBrandIds(Arrays.asList(brandId));
                    }
                    BaseResponseDto<PageableDto<CommodityBusinessResponseDto>> responseDto = iCommodityBusinessService.getCommodityList(queryRequestDto);
                    if (responseDto.getCode().equals(ResultEnum.SUCCESS.getCode()) && responseDto.getData() !=null && !CollectionUtils.isEmpty(responseDto.getData().getList())) {
                        List<CommodityBusinessResponseDto> goodsList = responseDto.getData().getList();
                        goodsList.forEach(goods->{
                            goods.getCars().forEach(brand->{
                                if (brandId ==null || brand.getBrandId().equals(brandId)) {
                                    String key = v.getId()+"-"+brand.getBrandId();
                                    Map<Integer, CommodityBusinessResponseDto> goodsMap;
                                    CommodityBusinessResponseDto fissionGoods = new CommodityBusinessResponseDto();
                                    BeanUtils.copyProperties(goods, fissionGoods);
                                    fissionGoods.setActivityId(v.getId());
                                    if (fissionBrandGoodsMap.get(key)==null) {
                                        goodsMap = new HashMap<>();
                                    } else {
                                        goodsMap = fissionBrandGoodsMap.get(key);
                                    }
                                    goodsMap.put(goods.getCommodityId(), fissionGoods);
                                    fissionBrandGoodsMap.put(key, goodsMap);
                                }
                            });
                        });
                    }
                }
            });
            if (CollectionUtils.isEmpty(fissionBrandGoodsMap)) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.RESULE_DATA_NONE.getCode(), "无关联商品", st, "FissionGoodsApiController", "getFissionGoodsByCityId", "");
            }
            for (Map.Entry<String, Map<Integer, CommodityBusinessResponseDto>> entry : fissionBrandGoodsMap.entrySet()) {
                String key = entry.getKey();
                Map<Integer, CommodityBusinessResponseDto> val = entry.getValue();
                FissionGoodsScene goodsScene = new FissionGoodsScene();
                goodsScene.setFissionId(Integer.valueOf(key.split("-")[0]));
                goodsScene.setBrandId(Integer.valueOf(key.split("-")[1]));
                CarBrandDto brand = carBaseService.getBrandById(goodsScene.getBrandId());
                goodsScene.setBrandFissionName(brand.getCbName() + "品牌特惠");
                goodsScene.setBrandLogo(brand.getCbLogo());
                goodsScene.setSceneStartDateStr(DateUtils.dateToString(liveSceneDtoMap.get(fissionActivityDtoMap.get(goodsScene.getFissionId()).getSceneId()).getBeginTime(), "MM月dd日"));
                goodsScene.setSceneEndDateStr(DateUtils.dateToString(liveSceneDtoMap.get(fissionActivityDtoMap.get(goodsScene.getFissionId()).getSceneId()).getEndTime(), "MM月dd日"));
                List<FissionGoodsApp> goodsList = new ArrayList<>();
                for (CommodityBusinessResponseDto dto : val.values()) {
                    FissionGoodsApp goods = new FissionGoodsApp();
                    goods.setCommodityId(dto.getCommodityId());
                    goods.setFissionId(dto.getActivityId());
                    goods.setCommodityName(dto.getCommodityName());
                    goods.setGoodsDetailsUrl(goods_details_url.replace("{goodsId}", dto.getCommodityId()+""));
                    goods.setOriginalPrice(dto.getCommodityPrice());
                    goods.setHeadImages(convertFissionGoodsImageDto(dto.getHeadImages()));
                    goodsList.add(goods);
                }
                goodsScene.setGoodsList(goodsList);
                list.add(goodsScene);
            }
        } catch (Exception e) {
            return DirectCommonUtil.addErrorLog("FissionGoodsController", "获取城市下活动商品 error", e, st, cityId+"");
        } finally {
            try {
                redisService.set(rediskey, JSON.toJSONString(list),5*60*1000);
            } catch (Exception redisEec) {}
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionGoodsApiController","getFissionGoodsByCityId",  "获取城市下活动商品 end " +st, JSON.toJSONString(list));
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, list);
    }

    private Map<Integer, LiveSceneDto> getLiveSceneDtoMap (Integer cityId, Integer brandId) {
        Map<Integer, LiveSceneDto> map = new HashMap<>();
        PageResult<LiveSceneDto> pageResult= new PageResult<>();
        pageResult.setPage(1);
        pageResult.setLimit(30);
        LiveSceneVo sceneVo = new LiveSceneVo();
        sceneVo.setCityId(cityId);
        sceneVo.setBrandId(brandId);
        PageResult sceneListPage = liveSceneService.findSceneListPage(pageResult, sceneVo);
        if (sceneListPage!=null && !CollectionUtils.isEmpty(sceneListPage.getData())) {
            List<LiveSceneDto> sceneDtoList = sceneListPage.getData();
            sceneDtoList.forEach(v->{
                System.out.println(v.getId());
                map.put(v.getId(), v);
            });
        }
        return map;
    }
    private Map<Integer, FissionActivityDto> getFissionActivityDtoMap (List<Integer> sceneIds) {
        Map<Integer, FissionActivityDto> map = new HashMap<>();
        FissionActivityDto fissionActivityDto = new FissionActivityDto();
        fissionActivityDto.setSceneIdList(sceneIds);
        List<FissionActivityDto> fissionActivityDtos = fissionActivityService.selectActivityDtoList(fissionActivityDto);
        if (!CollectionUtils.isEmpty(fissionActivityDtos)) {
            fissionActivityDtos.forEach(v->{
                if(v.getSceneId()!=null) map.put(v.getId(), v);
            });
        }
        return map;
    }

}
