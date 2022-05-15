package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.model.GiftRefuelingcardPeriodsGiftNum;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2020-05-28 11:02
 */
public interface GiftRefuelingcardPeriodsGiftNumReadMapper {

    /**
     * 根据赠送油卡场次表ID获取油卡规则列表
     * @author HuangHao
     * @CreatTime 2020-05-28 10:56
     * @param rcPeriodsId
     * @return java.util.List<com.tuanche.directselling.model.GiftRefuelingcardPeriodsCommodity>
     */
    List<GiftRefuelingcardPeriodsGiftNum> getByRcPeriodsId(Integer rcPeriodsId);

    /**
     * 根据价格和大场次ID获取配置的油卡赠送数量
     * @author HuangHao
     * @CreatTime 2020-06-03 18:08
     * @param commodityPrice
     * @param periodsId
     * @return com.tuanche.directselling.model.GiftRefuelingcardPeriodsGiftNum
     */
    GiftRefuelingcardPeriodsGiftNum getGiftNumByPriceAndPeriodsId(@Param("commodityPrice") BigDecimal commodityPrice, @Param("periodsId") Integer periodsId);
}
