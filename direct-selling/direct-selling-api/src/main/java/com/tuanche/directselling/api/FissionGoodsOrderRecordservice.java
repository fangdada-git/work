package com.tuanche.directselling.api;

import com.alibaba.fastjson.JSONObject;
import com.tuanche.directselling.dto.FissionDealerSettlementAccountDto;
import com.tuanche.directselling.dto.FissionGoodsOrderDto;
import com.tuanche.directselling.dto.FissionGoodsOrderRecordDto;
import com.tuanche.directselling.dto.FissionGoodsOrderSoucreDto;
import com.tuanche.directselling.model.FissionGoodsOrderRecord;
import com.tuanche.directselling.utils.PageResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface FissionGoodsOrderRecordservice {

    List<FissionGoodsOrderRecord> getFissionGoodsOrderRecordList (FissionGoodsOrderRecord fissionGoodsOrderRecord);

    FissionGoodsOrderRecord getFissionGoodsOrderRecordByInfo (FissionGoodsOrderRecord fissionGoodsOrderRecord);

    int getFissionGoodsOrderRecordCount (FissionGoodsOrderRecord fissionGoodsOrderRecord);

    int addFissionGoodsOrderRecord (FissionGoodsOrderRecord fissionGoodsOrderRecord);

    int updateFissionGoodsOrderRecordById (FissionGoodsOrderRecord fissionGoodsOrderRecord);
    int updateFissionGoodsOrderRecordByOrderNo (FissionGoodsOrderRecord fissionGoodsOrderRecord);

    Map<String, FissionGoodsOrderDto> getFissionGoodsOrderRecordListByOrderNos (List<String> orderNos);

    FissionGoodsOrderDto getOrderByOrderTradeId(String orderTradeId, Integer TradeType);

    PageResult getFissionGoodsList(PageResult<FissionGoodsOrderDto> pageResult, FissionGoodsOrderDto goodsOrderDto);

    List<JSONObject> getLiveList (List<Integer> liveIds, Integer roomStatus, Integer fissionId);

    Map<Integer, JSONObject> getLiveMap (List<Integer> liveIds);

    Map<String, FissionDealerSettlementAccountDto> getFissionGoodsOrderAmountMapByFissionIdAndDealerId(List<FissionGoodsOrderRecord> recordList);

    /**
     * 根据裂变活动ID获取经销商的已核销的订单金额
     * @author HuangHao
     * @CreatTime 2021-04-21 18:06
     * @param fissionId
     * @return java.util.Map<java.lang.Integer,java.math.BigDecimal>
     */
    Map<Integer, FissionGoodsOrderRecord> getDealerWriteOffOrderAmountByFissionId(Integer fissionId);

    /**
     * 获取经销商的核销信息（核销订单数，核销订单金额等）
     * @author HuangHao
     * @CreatTime 2021-04-26 11:34
     * @param fissionId
     * @return java.util.List<com.tuanche.directselling.dto.FissionGoodsOrderRecordDto>
     */
    Map<String, FissionGoodsOrderRecordDto> getDealerWriteOffData(List<FissionGoodsOrderRecord> recordList);

    public List<FissionGoodsOrderSoucreDto> getOrderSource();
}
