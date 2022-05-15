package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tuanche.broadcast.rpc.enums.BroadcastRoomMethodEnum;
import com.tuanche.broadcast.rpc.service.MsBroadcastRoomBusinessFacadeService;
import com.tuanche.broadcast.rpc.vo.BroadcastRpcVo;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.dict.api.model.DictItemVo;
import com.tuanche.dict.api.service.DictService;
import com.tuanche.directselling.api.FissionActivityService;
import com.tuanche.directselling.api.FissionGoodsOrderRecordservice;
import com.tuanche.directselling.api.FissionTradeRecordService;
import com.tuanche.directselling.dto.FissionDealerSettlementAccountDto;
import com.tuanche.directselling.dto.FissionGoodsOrderDto;
import com.tuanche.directselling.dto.FissionGoodsOrderRecordDto;
import com.tuanche.directselling.dto.FissionGoodsOrderSoucreDto;
import com.tuanche.directselling.enums.FissionGoodsOrderStatus;
import com.tuanche.directselling.mapper.read.directselling.FissionGoodsOrderRecordReadMapper;
import com.tuanche.directselling.mapper.sharding.FissionUserTaskRecordMapper;
import com.tuanche.directselling.mapper.write.directselling.FissionGoodsOrderRecordWriteMapper;
import com.tuanche.directselling.model.FissionGoodsOrderRecord;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.utils.GlobalConstants;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.district.api.dto.output.DistrictOutputBaseDto;
import com.tuanche.manubasecenter.api.CityBaseService;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.api.UserBaseService;
import com.tuanche.manubasecenter.model.CsCompany;
import com.tuanche.manubasecenter.model.CsUser;
import com.tuanche.merchant.api.payment.IPaymentService;
import com.tuanche.merchant.business.api.commodity.ICommodityBusinessService;
import com.tuanche.merchant.business.api.order.IOrderService;
import com.tuanche.merchant.business.dto.request.PageableRequestDto;
import com.tuanche.merchant.business.dto.request.commodity.CommodityWithBusinessQueryRequestDto;
import com.tuanche.merchant.business.dto.request.order.query.CommodityOrderExtendBusinessQueryRequestDto;
import com.tuanche.merchant.business.dto.response.BaseResponseDto;
import com.tuanche.merchant.business.dto.response.commodity.CommodityResponseDto;
import com.tuanche.merchant.business.dto.response.order.CommodityOrderResponseDto;
import com.tuanche.merchant.business.dto.response.order.business.OrderExtendWithBusinessResponseDto;
import com.tuanche.merchant.pojo.dto.commodity.PageableDto;
import com.tuanche.merchant.pojo.dto.enums.BusinessTypeEnum;
import com.tuanche.merchant.pojo.dto.enums.ResultEnum;
import com.tuanche.merchant.pojo.dto.enums.ServiceTypeEnum;
import com.tuanche.merchant.pojo.dto.enums.order.OrderTradeRefundStatusEnum;
import com.tuanche.merchant.pojo.dto.enums.order.OrderTradeStatusEnum;
import com.tuanche.merchant.pojo.dto.request.payment.OrderPaymentQueryRequestDto;
import com.tuanche.merchant.pojo.dto.response.payment.PaymentResponseDto;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service
public class FissionGoodsOrderRecordserviceImpl implements FissionGoodsOrderRecordservice {

    @Autowired
    private FissionGoodsOrderRecordReadMapper fissionGoodsOrderRecordReadMapper;
    @Autowired
    private FissionGoodsOrderRecordWriteMapper fissionGoodsOrderRecordWriteMapper;
    @Autowired
    private FissionUserTaskRecordMapper fissionUserTaskRecordMapper;
    @Reference
    private IOrderService iOrderService;
    @Reference
    private IPaymentService iPaymentService;
    @Reference
    private CityBaseService cityBaseService;
    @Reference
    private CompanyBaseService companyBaseService;
    @Reference
    private FissionActivityService fissionActivityService;
    @Autowired
    private FissionGoodsOrderRecordserviceImpl fissionGoodsOrderRecordserviceImpl;
    @Reference
    private ICommodityBusinessService iCommodityBusinessService;
    @Reference
    private UserBaseService userBaseService;
    @Reference
    private FissionTradeRecordService fissionTradeRecordService;
    @Reference
    private MsBroadcastRoomBusinessFacadeService msBroadcastRoomBusinessFacadeService;
    @Reference
    private DictService dictService;

    @Override
    public List<FissionGoodsOrderRecord> getFissionGoodsOrderRecordList (FissionGoodsOrderRecord fissionGoodsOrderRecord) {
        return fissionGoodsOrderRecordReadMapper.getFissionGoodsOrderRecordList(fissionGoodsOrderRecord);
    }
    @Override
    public Map<String, FissionGoodsOrderDto> getFissionGoodsOrderRecordListByOrderNos (List<String> orderNos) {
        Map<String, FissionGoodsOrderDto> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(orderNos)) {
            List<FissionGoodsOrderDto> fissionGoodsOrderRecordListByRrderNos = fissionGoodsOrderRecordReadMapper.getFissionGoodsOrderRecordListByOrderNos(orderNos);
            if (CollectionUtils.isNotEmpty(fissionGoodsOrderRecordListByRrderNos)) {
                fissionGoodsOrderRecordListByRrderNos.forEach(v->{
                    Integer watchLiveCount = fissionUserTaskRecordMapper.selectWatchLiveCount(v.getFissionActivityId(), v.getUserWxUnionId());
                    v.setWatchLive(watchLiveCount);
                    map.put(v.getOrderNo(), v);
                });
            }
        }
        return map;
    }
    @Override
    public FissionGoodsOrderRecord getFissionGoodsOrderRecordByInfo (FissionGoodsOrderRecord fissionGoodsOrderRecord) {
        return fissionGoodsOrderRecordReadMapper.getFissionGoodsOrderRecordByInfo(fissionGoodsOrderRecord);
    }

    @Override
    public int getFissionGoodsOrderRecordCount (FissionGoodsOrderRecord fissionGoodsOrderRecord) {
        return fissionGoodsOrderRecordReadMapper.getFissionGoodsOrderRecordCount(fissionGoodsOrderRecord);
    }

    @Override
    public int addFissionGoodsOrderRecord (FissionGoodsOrderRecord fissionGoodsOrderRecord) {
        int num = 0;
        fissionGoodsOrderRecordWriteMapper.insertSelective(fissionGoodsOrderRecord);
        if (fissionGoodsOrderRecord.getId()!=null) num = fissionGoodsOrderRecord.getId();
        return num;
    }

    @Override
    public int updateFissionGoodsOrderRecordById (FissionGoodsOrderRecord fissionGoodsOrderRecord) {
        return fissionGoodsOrderRecordWriteMapper.updateByPrimaryKeySelective(fissionGoodsOrderRecord);
    }
    @Override
    public int updateFissionGoodsOrderRecordByOrderNo (FissionGoodsOrderRecord fissionGoodsOrderRecord) {
        return fissionGoodsOrderRecordWriteMapper.updateFissionGoodsOrderRecordByOrderNo(fissionGoodsOrderRecord);
    }

    @Override
    public FissionGoodsOrderDto getOrderByOrderTradeId(String orderTradeId, Integer TradeType) {
        return fissionGoodsOrderRecordReadMapper.getOrderByOrderTradeId(orderTradeId, TradeType);
    }

    /**
      * @description : 获取商品订单列表
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/1/20 10:53
      */
    @Override
    public PageResult getFissionGoodsList(PageResult<FissionGoodsOrderDto> pageResult, FissionGoodsOrderDto goodsOrderDto) {
        try {
            //根据下单经销商 和 核销经销商 查询订单编号
            Map<String, Object> map = getOrderNoList(goodsOrderDto);
            if (StatusCodeEnum.SUCCESS.getCode() != (int) map.get("code")) {
                pageResult.setCode("0");
                return pageResult;
            }
            PageableRequestDto<CommodityOrderExtendBusinessQueryRequestDto> dto = new PageableRequestDto<>();
            CommodityOrderExtendBusinessQueryRequestDto queryRequestDto = new CommodityOrderExtendBusinessQueryRequestDto();
            List<String> orderNoList = (List<String>) map.get("list");
            if (CollectionUtils.isNotEmpty(orderNoList)) queryRequestDto.setOrderCodes(orderNoList);
            queryRequestDto.setActivityId(goodsOrderDto.getFissionActivityId());
            queryRequestDto.setLiveId(goodsOrderDto.getLiveId());
            if (!StringUtil.isEmpty(goodsOrderDto.getUserPhone())) queryRequestDto.setUserPhone(goodsOrderDto.getUserPhone());
            if (!StringUtil.isEmpty(goodsOrderDto.getReceiverPhone())) queryRequestDto.setReceiverPhone(goodsOrderDto.getReceiverPhone());
            if (goodsOrderDto.getOrderChannel()!=null) queryRequestDto.setOrderChannel(Arrays.asList(goodsOrderDto.getOrderChannel()));
            if (goodsOrderDto.getOrderStatus()!=null) {
                switch(goodsOrderDto.getOrderStatus()){
                    case UNPAID :
                        queryRequestDto.setTradeStatusEnum(OrderTradeStatusEnum.WAIT_BUYER_PAY);
                        queryRequestDto.setIsExpired(0);
                        break ;
                    case PAID :
                        queryRequestDto.setRefundStatusEnum(OrderTradeRefundStatusEnum.NO_REFUND);
                        queryRequestDto.setTradeStatusEnum(OrderTradeStatusEnum.PAY_SUCCESS);
                        break ;
                    case WAIT_BUYER_CONFIRM_GOODS :
                        queryRequestDto.setTradeStatusEnum(OrderTradeStatusEnum.WAIT_SELLER_SEND_GOODS);
                        break ;
                    case APPLY_REFUND :
                        queryRequestDto.setRefundStatusEnum(OrderTradeRefundStatusEnum.WAIT_SELLER_AGREE);
                        break ;
                    case REFUND_SUCCESS :
                        queryRequestDto.setRefundStatusEnum(OrderTradeRefundStatusEnum.SUCCESS);
                        break ;
                    case CHECKOUT :
                        queryRequestDto.setTradeStatusEnum(OrderTradeStatusEnum.TRADE_FINISHED);
                        break ;
                    case TRADE_CLOSED :
                        queryRequestDto.setIsExpired(1);
                        break ;
                }
            }
            if (goodsOrderDto.getOrderSource()!=null) {
                List<Integer> orderSources = new ArrayList<>();
                orderSources.add(goodsOrderDto.getOrderSource());
                queryRequestDto.setOrderSource(orderSources);
            }
            queryRequestDto.setBeginPayDate(goodsOrderDto.getBeginPayDate());
            queryRequestDto.setEndPayDate(goodsOrderDto.getEndPayDate());
            queryRequestDto.setBeginRefundDate(goodsOrderDto.getBeginRefundDate());
            queryRequestDto.setEndRefundDate(goodsOrderDto.getEndRefundDate());
            queryRequestDto.setBusinessTypeEnum(BusinessTypeEnum.FISSION);
            dto.setQuery(queryRequestDto);
            dto.setServiceTypeEnum(ServiceTypeEnum.BUSINESS);
            dto.setPageIndex(pageResult.getPage());
            dto.setPageSize(pageResult.getLimit());
            BaseResponseDto<PageableDto<OrderExtendWithBusinessResponseDto>> orderresponseDto = iOrderService.listCommodityOrder(dto, OrderExtendWithBusinessResponseDto.class);
            if (orderresponseDto.getCode().equals(ResultEnum.SUCCESS.getCode()) && CollectionUtils.isNotEmpty(orderresponseDto.getData().getList())) {
                List<FissionGoodsOrderDto> goodsOrderDtos = new ArrayList<>();
                List<Integer> liveIds = new ArrayList<>();
                List<Integer> cityIds = new ArrayList<>();
                List<Integer> goodsIds = new ArrayList<>();
                List<Integer> saleIds = new ArrayList<>();
                List<Integer> dealerIds = new ArrayList<>();
                List<String> orderNos = new ArrayList<>();
                orderresponseDto.getData().getList().forEach(v->{
                    if (!liveIds.contains(v.getLiveId()) && v.getLiveId()!=null) liveIds.add(v.getLiveId());
                    if (!cityIds.contains(v.getOrderCityId()) && v.getOrderCityId()!=null) cityIds.add(v.getOrderCityId());
                    if (!goodsIds.contains(v.getCommodityId()) && v.getCommodityId()!=null) goodsIds.add(v.getCommodityId());
                    if (!orderNos.contains(v.getOrderCode()) && v.getOrderCode()!=null) orderNos.add(v.getOrderCode());
                });
                //直播间信息
                Map<Integer, JSONObject> liveMap = new HashMap<>();
                if (CollectionUtils.isNotEmpty(liveIds)) {
                    liveMap = getLiveMap(liveIds);
                }
                //城市
                Map<Integer, DistrictOutputBaseDto> cityMap = cityBaseService.getCityMapByIds(cityIds,1);
                //商品
                Map<Integer, CommodityResponseDto> goodsMap = new HashMap<>();
                CommodityWithBusinessQueryRequestDto query = new CommodityWithBusinessQueryRequestDto();
                query.setCommodityIds(goodsIds);
                query.setPageIndex(pageResult.getPage());
                query.setPageSize(pageResult.getLimit());
                query.setCommodityStatusEnum(null);
//                    query.setBusinessTypeEnum(BusinessTypeEnum.FISSION);
                BaseResponseDto<PageableDto<CommodityResponseDto>> goodsResponseDto = iCommodityBusinessService.getCommodityList(query);
                if (goodsResponseDto.getCode().equals(ResultEnum.SUCCESS.getCode()) && goodsResponseDto.getData() !=null
                        && CollectionUtils.isNotEmpty(goodsResponseDto.getData().getList())) {
                    goodsResponseDto.getData().getList().forEach(v->{
                        goodsMap.put(v.getCommodityId(), v);
                    });
                }
                //订单记录
                Map<String, FissionGoodsOrderDto> goodsOrderMap = fissionGoodsOrderRecordserviceImpl.getFissionGoodsOrderRecordListByOrderNos(orderNos);
                goodsOrderMap.values().forEach(v->{
                    if (!saleIds.contains(v.getSaleId()) && v.getSaleId()!=null) saleIds.add(v.getSaleId());
                    if (!saleIds.contains(v.getCancelSaleId()) && v.getCancelSaleId()!=null) saleIds.add(v.getCancelSaleId());
                    if (!dealerIds.contains(v.getDealerId()) && v.getDealerId()!=null) dealerIds.add(v.getDealerId());
                    if (!dealerIds.contains(v.getCancelDealerId()) && v.getCancelDealerId()!=null) dealerIds.add(v.getCancelDealerId());
                });
                //订单流水信息
                PageableRequestDto requestDto = new PageableRequestDto();
                OrderPaymentQueryRequestDto paymentQueryRequestDto = new OrderPaymentQueryRequestDto();
                paymentQueryRequestDto.setOrderCodeList(orderNos);
                requestDto.setQuery(paymentQueryRequestDto);
                requestDto.setPageIndex(1);
                requestDto.setPageSize(orderNos.size());
                Map<String, PaymentResponseDto> payMap = new HashMap<>();
                BaseResponseDto<PageableDto<PaymentResponseDto>> pageableDtoBaseResponseDto = iPaymentService.listOrderPayment(requestDto);
                if (pageableDtoBaseResponseDto.getCode().equals(ResultEnum.SUCCESS.getCode()) && CollectionUtils.isNotEmpty(pageableDtoBaseResponseDto.getData().getList())) {
                    pageableDtoBaseResponseDto.getData().getList().forEach(v->{
                        payMap.put(v.getOrderCode(), v);
                    });
                }
                CommonLogUtil.addInfo("FissionGoodsOrderRecordserviceImpl", "获取商品订单列表 订单流水信息", JSON.toJSONString(payMap));
                //销售
                Map<Integer, CsUser> saleMap = userBaseService.getCsUserByIds(saleIds);
                //经销商
                Map<Integer, CsCompany> companyMap = companyBaseService.getCompanyMap(dealerIds);

                for (OrderExtendWithBusinessResponseDto fissionActivityOrderResponseDto : orderresponseDto.getData().getList()) {
                    FissionGoodsOrderDto orderDto = new FissionGoodsOrderDto();
                    orderDto.setOrderNo(fissionActivityOrderResponseDto.getOrderCode());
                    orderDto.setFissionActivityId(fissionActivityOrderResponseDto.getActivityId());
                    orderDto.setLiveId(fissionActivityOrderResponseDto.getLiveId());
                    orderDto.setOrderChannel(fissionActivityOrderResponseDto.getOrderChannel());

                    orderDto = getOrderStatus(fissionActivityOrderResponseDto, orderDto);

                    orderDto.setOrderSource(fissionActivityOrderResponseDto.getOrderSource());
                    orderDto.setUserPhone(String.valueOf(fissionActivityOrderResponseDto.getUserPhone()));
                    orderDto.setUserName(fissionActivityOrderResponseDto.getUserNick());
                    orderDto.setOrderCreateDt(fissionActivityOrderResponseDto.getOrderCreateDt());
                    orderDto.setUserId(fissionActivityOrderResponseDto.getUserId());
                    orderDto.setOrderMoney(fissionActivityOrderResponseDto.getOrderMoney());
                    orderDto.setCancelDate(fissionActivityOrderResponseDto.getCheckoutTime());
                    orderDto.setOrderCreateDt(fissionActivityOrderResponseDto.getOrderCreateDt());
                    if (!StringUtil.isEmpty(fissionActivityOrderResponseDto.getReceiverName())) orderDto.setReceiverName(fissionActivityOrderResponseDto.getReceiverName());
                    if (fissionActivityOrderResponseDto.getReceiverPhone()!=null) orderDto.setReceiverPhone(String.valueOf(fissionActivityOrderResponseDto.getReceiverPhone()));
                    FissionGoodsOrderDto order = goodsOrderMap.get(fissionActivityOrderResponseDto.getOrderCode());
                    if (order !=null) {
                        orderDto.setFissionActivityName(order.getFissionActivityName());
                        orderDto.setDealerName(companyMap.get(order.getDealerId())==null?"":companyMap.get(order.getDealerId()).getCompanyName());
                        orderDto.setCancelDealerName(companyMap.get(order.getCancelDealerId())==null?"":companyMap.get(order.getCancelDealerId()).getCompanyName());
                        orderDto.setSaleName(saleMap.get(order.getSaleId())==null?"":saleMap.get(order.getSaleId()).getUname());
                        orderDto.setCancelSaleName(saleMap.get(order.getCancelSaleId())==null?"":saleMap.get(order.getCancelSaleId()).getUname());
                        orderDto.setWatchLive(order.getWatchLive());
                    }
                    PaymentResponseDto pay = payMap.get(fissionActivityOrderResponseDto.getOrderCode());
                    if (pay!=null) {
                        orderDto.setRefundTradeNo(pay.getRefund3SerialNo());
                        orderDto.setRefundDate(pay.getRefundTime());
                        orderDto.setTradeNo(pay.getPayment3SerialNo());
                        orderDto.setPayTime(pay.getPayTime());
                    }
                    DistrictOutputBaseDto city = cityMap.get(fissionActivityOrderResponseDto.getOrderCityId());
                    orderDto.setOrderCityName(city != null ? city.getName() : "");
                    CommodityResponseDto goods = goodsMap.get(fissionActivityOrderResponseDto.getCommodityId());
                    orderDto.setGoodsName(goods != null ? goods.getCommodityName() : "");
                    //写直播间名称
                    if (orderDto.getLiveId()!=null && liveMap.get(orderDto.getLiveId())!=null) {
                        orderDto.setLiveName((String) liveMap.get(orderDto.getLiveId()).get("roomTitle"));
                    }
                    goodsOrderDtos.add(orderDto);
                }
                pageResult.setCount(orderresponseDto.getData().getTotalCount());
                pageResult.setData(goodsOrderDtos);
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonLogUtil.addError("FissionGoodsOrderRecordserviceImpl.getFissionGoodsList", "获取裂变商品列表页 error", e);
        }
        pageResult.setCode("0");
        return pageResult;
    }

    //写订单状态
    private FissionGoodsOrderDto getOrderStatus (CommodityOrderResponseDto orderResponseDto, FissionGoodsOrderDto fissionGoodsOrderDto) {
        if (orderResponseDto.getIsExpired()) {
            fissionGoodsOrderDto.setOrderStatus(FissionGoodsOrderStatus.TRADE_CLOSED);
        } else if (orderResponseDto.getTradeRefundStatus()!=null && orderResponseDto.getTradeRefundStatus().equals(OrderTradeRefundStatusEnum.NO_REFUND)) {
            switch (orderResponseDto.getTradeStatus()) {
                case WAIT_BUYER_PAY :
                    fissionGoodsOrderDto.setOrderStatus(FissionGoodsOrderStatus.UNPAID);
                    break;
                case PAY_SUCCESS:
                    fissionGoodsOrderDto.setOrderStatus(FissionGoodsOrderStatus.PAID);
                    break;
                case WAIT_SELLER_SEND_GOODS:
                    fissionGoodsOrderDto.setOrderStatus(FissionGoodsOrderStatus.WAIT_SELLER_SEND_GOODS);
                    break;
                case WAIT_BUYER_CONFIRM_GOODS:
                    fissionGoodsOrderDto.setOrderStatus(FissionGoodsOrderStatus.WAIT_BUYER_CONFIRM_GOODS);
                    break;
                case TRADE_CLOSED:
                    fissionGoodsOrderDto.setOrderStatus(FissionGoodsOrderStatus.TRADE_CLOSED);
                    break;
                case TRADE_FINISHED:
                    fissionGoodsOrderDto.setCancelDate(orderResponseDto.getCheckoutTime());
                    fissionGoodsOrderDto.setOrderStatus(FissionGoodsOrderStatus.CHECKOUT);
                    break;
            }
        } else {
            switch (orderResponseDto.getTradeRefundStatus()) {
                case WAIT_SELLER_AGREE :
                    fissionGoodsOrderDto.setOrderStatus(FissionGoodsOrderStatus.APPLY_REFUND);
                    break;
                case SUCCESS:
                    fissionGoodsOrderDto.setOrderStatus(FissionGoodsOrderStatus.REFUND_SUCCESS);
                    break;
            }
        }
        return fissionGoodsOrderDto;
    }

    //根据下单经销商 和 核销经销商 查询订单编号
    private Map<String, Object> getOrderNoList (FissionGoodsOrderDto goodsOrderDto) {
        Map<String, Object> map = new HashMap<>();
        //无查询条件 直接进行下一步
        if (goodsOrderDto.getDealerId() == null && goodsOrderDto.getSaleId()==null &&
                StringUtil.isEmpty(goodsOrderDto.getDealerName()) && StringUtil.isEmpty(goodsOrderDto.getCancelDealerName())) {
            map.put("code", StatusCodeEnum.SUCCESS.getCode());
            return map;
        }
        int code = StatusCodeEnum.RESULE_DATA_NONE.getCode();
        List<String> orderNos = new ArrayList<>();
        List<Integer> orderDealerIdList = new ArrayList<>();
        //查询下单经销商
        if(goodsOrderDto.getDealerId() != null){
            orderDealerIdList.add(goodsOrderDto.getDealerId());
        }else if (!StringUtil.isEmpty(goodsOrderDto.getDealerName())) {
            CsCompany company = new CsCompany();
            company.setCompanyName(goodsOrderDto.getDealerName());
            List<CsCompany> csCompanyByName = companyBaseService.getCsCompanyByName(company);
            if (CollectionUtils.isNotEmpty(csCompanyByName)) {
                csCompanyByName.forEach(v->{
                    orderDealerIdList.add(v.getId());
                });
            }
        }
        FissionGoodsOrderRecord orderRecord = new FissionGoodsOrderRecord();
        orderRecord.setSaleId(goodsOrderDto.getSaleId());
        if(orderDealerIdList.size() > 0){
            orderRecord.setDealerIdList(orderDealerIdList);
            List<FissionGoodsOrderRecord> fissionGoodsOrderRecordList = fissionGoodsOrderRecordserviceImpl.getFissionGoodsOrderRecordList(orderRecord);
            if (CollectionUtils.isNotEmpty(fissionGoodsOrderRecordList)) {
                fissionGoodsOrderRecordList.forEach(v->{
                    orderNos.add(v.getOrderNo());
                });
                code = StatusCodeEnum.SUCCESS.getCode();
            }
        }
        //查询核销经销商
        if (!StringUtil.isEmpty(goodsOrderDto.getCancelDealerName())) {
            CsCompany company = new CsCompany();
            company.setCompanyName(goodsOrderDto.getCancelDealerName());
            List<CsCompany> csCompanyByName = companyBaseService.getCsCompanyByName(company);
            if (CollectionUtils.isNotEmpty(csCompanyByName)) {
                FissionGoodsOrderRecord record = new FissionGoodsOrderRecord();
                List<Integer> dealerIdList = new ArrayList<>();
                csCompanyByName.forEach(v->{
                    dealerIdList.add(v.getId());
                });
                if (dealerIdList.size()>0) {
                    record.setCancelDealerIdList(dealerIdList);
                    List<FissionGoodsOrderRecord> fissionGoodsOrderRecordList = fissionGoodsOrderRecordserviceImpl.getFissionGoodsOrderRecordList(record);
                    if (CollectionUtils.isNotEmpty(fissionGoodsOrderRecordList)) {
                        fissionGoodsOrderRecordList.forEach(v->{
                            orderNos.add(v.getOrderNo());
                        });
                        code = StatusCodeEnum.SUCCESS.getCode();
                    }
                }
            }
        }
        if (code == StatusCodeEnum.SUCCESS.getCode()) {
            code = StatusCodeEnum.SUCCESS.getCode();
            map.put("list", orderNos);
        }
        map.put("code", code);
        return map;
    }

    /**
      * @description : 查询直播间信息 list
      * @author : fxq
      * @param :roomStatus：0 未开始
      * @return :
      * @date : 2021/1/15 15:54
      */
    @Override
    public List<JSONObject> getLiveList (List<Integer> liveIds, Integer roomStatus, Integer fissionId) {
        List<JSONObject> liveVoList = new ArrayList<>();
        if (CollectionUtils.isEmpty(liveIds) && roomStatus==null && fissionId==null) return liveVoList;
        BroadcastRpcVo vo = new BroadcastRpcVo(BroadcastRoomMethodEnum.MS_QUERY_LIST_BY_PAGE);
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", 1);
        map.put("pageSize", Integer.MAX_VALUE);
        if (roomStatus!=null) map.put("roomStatus", roomStatus);
        if (fissionId!=null) map.put("fissionId", fissionId);
        if (CollectionUtils.isNotEmpty(liveIds)) map.put("ids", liveIds);
        vo.setInfo("param", JSONObject.toJSONString(map));
        vo.setInfo("reqMessageId", String.valueOf(System.currentTimeMillis()));
        BroadcastRpcVo result = null;
        try {
            result = msBroadcastRoomBusinessFacadeService.service(vo);
            if (result!=null && result.getInfo("data")!=null) {
                String data = (String) result.getInfo("data");
                Map<String, Object> objectMap = JSON.parseObject(data, HashMap.class);
                liveVoList = (List<JSONObject>) objectMap.get("list");
//                roomEntity.get("id"));
//                roomEntity.get("roomTitle"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liveVoList;
    }

    /**
      * @description : 查询直播间信息  map
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/1/15 15:54
      */
    @Override
    public Map<Integer, JSONObject> getLiveMap (List<Integer> liveIds) {
        Map<Integer, JSONObject> liveMap = new HashMap<>();
        List<JSONObject> liveList = getLiveList(liveIds, null, null);
        if (CollectionUtils.isNotEmpty(liveList)) {
            liveList.forEach(v->{
                liveMap.put((Integer) v.get("id"), v);
            });
        }
        return liveMap;
    }

    /**
      * @description : 根据裂变活动和经销商查询订单数量
      * @author : fxq
      * @param :
      * @return : key : fissionId + : + dealerId    eg：   12:187
      * @date : 2021/3/10 10:49
      */
    @Override
    public Map<String, FissionDealerSettlementAccountDto> getFissionGoodsOrderAmountMapByFissionIdAndDealerId(List<FissionGoodsOrderRecord> recordList) {
        Map<String, FissionDealerSettlementAccountDto> map = new HashMap<>();
        List<FissionDealerSettlementAccountDto> list = fissionGoodsOrderRecordReadMapper.getFissionGoodsListByFissionIdAndDealerId(recordList);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(v->{
                map.put(v.getFissionId()+":"+v.getDealerId(), v);
            });
        }
        return map;
    }
    /**
     * 获取经销商的核销信息（核销订单数，核销订单金额等）
     * @author HuangHao
     * @CreatTime 2021-04-26 11:34
     * @param
     * @return java.util.List<com.tuanche.directselling.dto.FissionGoodsOrderRecordDto>
     */
    @Override
    public Map<String, FissionGoodsOrderRecordDto> getDealerWriteOffData(List<FissionGoodsOrderRecord> recordList) {
        Map<String, FissionGoodsOrderRecordDto> map = new HashMap<>();
        List<FissionGoodsOrderRecordDto> list = fissionGoodsOrderRecordReadMapper.getDealerWriteOffData(recordList);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(v->{
                map.put(v.getFissionId()+":"+v.getCancelDealerId(), v);
            });
        }
        return map;
    }

    /**
     * 根据裂变活动ID获取经销商的已核销的订单金额
     * @author HuangHao
     * @CreatTime 2021-04-21 18:06
     * @param fissionId
     * @return java.util.Map<java.lang.Integer,java.math.BigDecimal>
     */
    @Override
    public Map<Integer, FissionGoodsOrderRecord> getDealerWriteOffOrderAmountByFissionId(Integer fissionId){
        if(fissionId == null){
            return null;
        }
        return fissionGoodsOrderRecordReadMapper.getDealerWriteOffOrderAmountByFissionId(fissionId);
    }

    @Override
    public List<FissionGoodsOrderSoucreDto> getOrderSource () {
        List<FissionGoodsOrderSoucreDto> list = new ArrayList<>();
        List<DictItemVo> dictItemVos = dictService.listItem("orderSource");
        if (CollectionUtils.isNotEmpty(dictItemVos)) {
            GlobalConstants.FissionOrderSource.FISSION_ORDER_SOURCE_MAP.clear();
            dictItemVos.forEach(v->{
                FissionGoodsOrderSoucreDto dto = new FissionGoodsOrderSoucreDto(v.getCode(), v.getName());
                list.add(dto);
                GlobalConstants.FissionOrderSource.FISSION_ORDER_SOURCE_MAP.put(v.getCode(), v.getName());
            });
        }
        return list;
    }

}
