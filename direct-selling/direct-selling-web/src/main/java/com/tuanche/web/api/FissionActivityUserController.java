package com.tuanche.web.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.collect.Lists;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.broadcast.rpc.vo.BroadcastRoomResponseDto;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.api.FissionActivityService;
import com.tuanche.directselling.api.FissionSaleService;
import com.tuanche.directselling.api.LiveSceneDealerConfigService;
import com.tuanche.directselling.dto.FissionActivityDto;
import com.tuanche.directselling.dto.FissionDealerDto;
import com.tuanche.directselling.dto.FissionGoods;
import com.tuanche.directselling.dto.FissionGoodsImageDto;
import com.tuanche.directselling.model.FissionActivityExtend;
import com.tuanche.directselling.model.FissionSale;
import com.tuanche.directselling.utils.BeanCopyUtil;
import com.tuanche.directselling.utils.GlobalConstants;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.FissionActivityApiVo;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.dto.WxUserInfoDto;
import com.tuanche.merchant.business.api.commodity.ICommodityBusinessService;
import com.tuanche.merchant.business.api.order.IOrderService;
import com.tuanche.merchant.business.dto.request.PageableRequestDto;
import com.tuanche.merchant.business.dto.request.commodity.CommodityQueryRequestDto;
import com.tuanche.merchant.business.dto.request.order.query.CommodityOrderExtendBusinessQueryRequestDto;
import com.tuanche.merchant.business.dto.request.order.query.CommodityOrderQueryRequestDto;
import com.tuanche.merchant.business.dto.response.BaseResponseDto;
import com.tuanche.merchant.business.dto.response.commodity.CommodityResponseDto;
import com.tuanche.merchant.business.dto.response.order.business.OrderExtendWithBusinessResponseDto;
import com.tuanche.merchant.pojo.dto.commodity.PageableDto;
import com.tuanche.merchant.pojo.dto.enums.*;
import com.tuanche.merchant.pojo.dto.enums.order.OrderTradeStatusEnum;
import com.tuanche.web.service.FissionCommonServiceImpl;
import com.tuanche.web.util.DirectCommonUtil;
import com.tuanche.web.util.Globals;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @class: FissionActivityController
 * @description: 裂变活动相关接口
 * @author: dudg
 * @create: 2020-10-16 16:12
 */
@RequestMapping("/api/fission")
@RestController
public class FissionActivityUserController extends BaseController{

    @Reference
    FissionActivityService activityService;
    @Reference
    private ICommodityBusinessService iCommodityBusinessService;
    @Autowired
    private FissionCommonServiceImpl fissionCommonService;
    @Reference
    private IOrderService iOrderService;
    @Reference
    LiveSceneDealerConfigService liveSceneDealerConfigService;
    @Reference
    CompanyBaseService companyBaseService;
    @Reference
    FissionSaleService fissionSaleService;

    /**
     * @description: 裂变活动页-活动信息接口
     * @param request
     * @param activityApiVo
     * @return: com.tuanche.arch.web.model.TcResponse
     * @author: dudg
     * @date: 2020/10/16 18:19
    */
    @RequestMapping("/activityInfoForApi")
    public TcResponse activityInfoForApi(HttpServletRequest request, FissionActivityApiVo activityApiVo){
        return activityService.getActivityInfoForApi(activityApiVo);
    }


    /**
     * @description: 裂变活动页-预约直播接口
     * @param request
     * @param activityApiVo
     * @return: com.tuanche.arch.web.model.TcResponse
     * @author: dudg
     * @date: 2020/10/16 18:19
     */
    @RequestMapping("/appointLiveForApi")
    public TcResponse appointLiveForApi(HttpServletRequest request, FissionActivityApiVo activityApiVo){
        activityApiVo.setUserId(DirectCommonUtil.getMemberPoId(request));
        return activityService.appointLiveForApi(activityApiVo);
    }


    /**
     * @description: 裂变活动页-分享信息接口
     * @param request
     * @param activityApiVo
     * @return: com.tuanche.arch.web.model.TcResponse
     * @author: dudg
     * @date: 2020/10/16 18:19
     */
    @RequestMapping("/shareInfoForApi")
    public TcResponse shareInfoForApi(HttpServletRequest request, FissionActivityApiVo activityApiVo){
        return activityService.getShareInfoForApi(activityApiVo);
    }
    
    /**
     * @description: 根据授权码获取微信用户信息
     * @param request
     * @param code
     * @return: java.lang.String
     * @author: dudg
     * @date: 2020/10/16 18:23
    */
    @RequestMapping("/getWxUserInfoByCodeForApi")
    public TcResponse getWxUserInfoByCodeForApi(HttpServletRequest request, String code) {
        if (StringUtil.isEmpty(code)) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_BLANK.getCode(), "授权码为空");
        }

        WxUserInfoDto wxUserInfoDto = activityService.getWxUserInfoByCode(code);
        if (wxUserInfoDto == null) {
            return new TcResponse(StatusCodeEnum.RESULE_DATA_NONE.getCode(), "获取失败，请重试");
        }
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), wxUserInfoDto);
    }

    /**
     * @description: 获取裂变活动商品信息
     * @param activityApiVo
     * @return: com.tuanche.arch.web.model.TcResponse
     * @author: dudg
     * @date: 2020/11/6 11:19
    */
    @RequestMapping("/activityGoodsForApi")
    public TcResponse activityGoodsForApi(FissionActivityApiVo activityApiVo) {
        long st = System.currentTimeMillis();
        if(activityApiVo.getFissionId()== null || activityApiVo.getFissionId().intValue() ==0){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "活动无效");
        }
        // 验证参数
        FissionActivityDto activityDto = activityService.getFissionActivityDtoById(activityApiVo.getFissionId());
        if (activityDto == null) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "活动无效");
        }

        Optional<FissionActivityExtend> nationWideOpt = activityDto.getFissionActivityExtend().stream()
                .filter(n -> n.getDataType().equals(GlobalConstants.FISSION_EXTEND_TYPE1) && n.getDataId().equals(Integer.valueOf(-1)))
                .findAny();

        if (!nationWideOpt.isPresent()) {
            if(activityApiVo.getCityId() == null || activityApiVo.getCityId() == 0){
                return new TcResponse(StatusCodeEnum.PARAM_IS_BLANK.getCode(), "城市为空");
            }

            Optional<FissionActivityExtend> cityOptional = activityDto.getFissionActivityExtend().stream()
                    .filter(n -> n.getDataType().equals(GlobalConstants.FISSION_EXTEND_TYPE1) && n.getDataId().equals(activityApiVo.getCityId()))
                    .findAny();
            if (!cityOptional.isPresent()) {
                return new TcResponse(StatusCodeEnum.PARAM_IS_BLANK.getCode(), "当前城市无法参加此活动");
            }
        }

        // 取商品信息
        List<Integer> goodsIdList = activityDto.getFissionActivityExtend().stream().filter(n -> n.getDataType().equals(GlobalConstants.FISSION_EXTEND_TYPE2)).map(FissionActivityExtend::getDataId).collect(Collectors.toList());

        if (goodsIdList.size() > 0) {
            CommodityQueryRequestDto queryRequestDto1 = new CommodityQueryRequestDto();
            queryRequestDto1.setCommodityIds(goodsIdList);
            queryRequestDto1.setBusinessTypeEnum(BusinessTypeEnum.FISSION);
            queryRequestDto1.setCommodityStatusEnum(CommodityStatusEnum.ONSALE);
            queryRequestDto1.setContainsImage(true);
            BaseResponseDto<PageableDto<CommodityResponseDto>> commodityList = iCommodityBusinessService.getCommodityList(queryRequestDto1);
            if (ResultEnum.SUCCESS.getCode().equals(commodityList.getCode()) && commodityList.getData()!=null && !CollectionUtils.isEmpty(commodityList.getData().getList())) {
                BroadcastRoomResponseDto broadcastRoomInfo = fissionCommonService.getBroadcastRoomInfo(activityDto.getBroadcastRoomId());
                List<FissionGoods> goodsList = Lists.newArrayList();
                commodityList.getData().getList().forEach(n->{
                    FissionGoods fissionGoods = new FissionGoods();
                    BeanUtils.copyProperties(n, fissionGoods);
                    fissionGoods.setPutAwayType(CommodityStatusEnum.ONSALE.equals(n.getStatusEnum()) ? 1 : 0);
                    fissionGoods.setSeckill(n.getCommodityType()== CommodityTypeEnum.SECKILL ? Globals.FISSION_GOODS.seckill : Globals.FISSION_GOODS.seckill_no);
                    if(n.getCommodityType()!=null && n.getCommodityType().equals(CommodityTypeEnum.SECKILL) && broadcastRoomInfo != null){
                        if(broadcastRoomInfo.getRoomStatus() !=null && broadcastRoomInfo.getRoomStatus().intValue() !=0){
                            fissionGoods.setPutAwayType(0);
                        }
                    }
                    //没有库存后查询是否有未付款商品
                    if (!Objects.isNull(n.getCommodityCount()) && !Objects.isNull(n.getTotalSoldNumber()) && n.getCommodityCount() <= n.getTotalSoldNumber()) {
                        PageableRequestDto<CommodityOrderQueryRequestDto> dto = new PageableRequestDto<>();
                        CommodityOrderExtendBusinessQueryRequestDto queryRequestDto = new CommodityOrderExtendBusinessQueryRequestDto();
                        queryRequestDto.setTradeStatusEnum(OrderTradeStatusEnum.WAIT_BUYER_PAY);
                        queryRequestDto.setCommodityIdList(Arrays.asList(fissionGoods.getCommodityId()));
                        queryRequestDto.setIsExpired(Globals.FISSION_GOODS_ORDER.is_expired_non);
                        queryRequestDto.setBusinessTypeEnum(BusinessTypeEnum.FISSION);
                        dto.setQuery(queryRequestDto);
                        dto.setServiceTypeEnum(ServiceTypeEnum.BUSINESS);
                        dto.setPageIndex(1);
                        dto.setPageSize(1);
                        BaseResponseDto<PageableDto<OrderExtendWithBusinessResponseDto>> orderresponseDto = iOrderService.listCommodityOrder(dto, OrderExtendWithBusinessResponseDto.class);
                        if (orderresponseDto.getData() != null && !CollectionUtils.isEmpty(orderresponseDto.getData().getList())) {
                            fissionGoods.setNonPay(Globals.FISSION_GOODS.goods_non_pay);
                        } else {
                            fissionGoods.setNonPay(Globals.FISSION_GOODS.goods_non_pay_no);
                        }
                    }
                    fissionGoods.setIsPost(n.isPost() ? 1 : 0);
                    fissionGoods.setDescpritionImages(BeanCopyUtil.copyListProperties(n.getDetailImages(), FissionGoodsImageDto::new));
                    fissionGoods.setFissionId(activityApiVo.getFissionId());
                    fissionGoods.setIsHelper(n.getNeedHelp());
                    fissionGoods.setHelperNum(n.getHelpNumber());
                    goodsList.add(fissionGoods);
                });

                return success(goodsList, st);
            }
            else if(commodityList.getCode().equals(ResultEnum.NODATA.getCode())){
                return noData(st);
            }
            else {
                return new TcResponse(commodityList.getCode(), st, commodityList.getMsg());
            }
        }

        return noData(st);
    }

    /**
     *  动态控制H5或小程序
     * @param request
     * @param
     * @return
     */
    @RequestMapping("/activityH5OrMiniProgram")
    public TcResponse activityH5OrMiniProgram(HttpServletRequest request){
        return activityService.activityH5OrMiniProgram();
    }

    /**
      * @description : 获取经销商列表根据裂变活动id
      * @author : fxq
      * @param : fissionId  裂变id
      * @param : cityId  城市id
      * @param : saleWxUnionId  销售微信unionid
      * @param : checkAllCity  是否查询所有城市  默认查  0：不查   1:查
      * @return :
      * @date : 2021/4/14 13:53
      */
    @RequestMapping("/getDealerListByFissionId")
    public TcResponse getDealerListByFissionId(Integer fissionId,Integer cityId, String saleWxUnionId, Integer checkAllCity) {
        if (fissionId==null || fissionId<1) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_BLANK.getCode(), "裂变活动为空");
        }
        Integer dealerId = null;
        if (!StringUtil.isEmpty(saleWxUnionId) && fissionId!=null) {
            FissionSale sale = new FissionSale();
            sale.setSaleWxUnionId(saleWxUnionId);
            sale.setFissionId(fissionId);
            FissionSale fissionSale = fissionSaleService.getFissionSale(sale);
            if (fissionSale != null) dealerId = fissionSale.getDealerId();
        }
        List<FissionDealerDto> list = activityService.selectActivityDealerList(fissionId, dealerId==null ? cityId : null, dealerId);
        if (CollectionUtils.isEmpty(list) && cityId!=null && (checkAllCity==null || checkAllCity==1)) list = activityService.selectActivityDealerList(fissionId, null, null);
            if (CollectionUtils.isEmpty(list)) {
            return new TcResponse(StatusCodeEnum.RESULE_DATA_NONE.getCode(), "无经销商");
        }
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), list);
    }

}
