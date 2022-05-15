package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.FissionDealerOrderAmountDto;
import com.tuanche.directselling.dto.FissionDealerSettlementAccountDto;
import com.tuanche.directselling.dto.FissionGoodsOrderDto;
import com.tuanche.directselling.dto.FissionGoodsOrderRecordDto;
import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.FissionGoodsOrderRecord;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface FissionGoodsOrderRecordReadMapper extends MyBatisBaseDao<FissionGoodsOrderRecord, Integer> {

    List<FissionGoodsOrderRecord> getFissionGoodsOrderRecordList(FissionGoodsOrderRecord fissionGoodsOrderRecord);

    int getFissionGoodsOrderRecordCount(FissionGoodsOrderRecord fissionGoodsOrderRecord);

    FissionGoodsOrderRecord getFissionGoodsOrderRecordByInfo(FissionGoodsOrderRecord fissionGoodsOrderRecord);

    List<FissionGoodsOrderDto> getFissionGoodsOrderRecordListByOrderNos(@Param("oederNos") List<String> oederNos);

    FissionGoodsOrderDto getOrderByOrderTradeId(@Param("orderTradeId") String orderTradeId, @Param("tradeType") Integer tradeType);

    List<FissionDealerSettlementAccountDto> getFissionGoodsListByFissionIdAndDealerId(@Param("recordList") List<FissionGoodsOrderRecord> recordList);

    /**
     * 根据裂变活动ID获取经销商的已核销的订单金额
     * @author HuangHao
     * @CreatTime 2021-04-21 18:06
     * @param fissionId
     * @return java.util.Map<java.lang.Integer,java.math.BigDecimal>
     */
    @MapKey("dealerId")
    Map<Integer, FissionGoodsOrderRecord> getDealerWriteOffOrderAmountByFissionId(@Param("fissionId") Integer fissionId);
    /**
     * 获取经销商的核销信息（核销订单数，核销订单金额等）
     * @author HuangHao
     * @CreatTime 2021-04-26 11:34
     * @param fissionId
     * @return java.util.List<com.tuanche.directselling.dto.FissionGoodsOrderRecordDto>
     */
    List<FissionGoodsOrderRecordDto> getDealerWriteOffData(@Param("recordList") List<FissionGoodsOrderRecord> recordList);
}