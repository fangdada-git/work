package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.model.GiftRefuelingcardPeriodsCommodity;

import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2020-05-28 10:55
 */
public interface GiftRefuelingcardPeriodsCommodityReadMapper {

    /**
     * 根据赠送油卡场次表ID获取商品类型列表
     * @author HuangHao
     * @CreatTime 2020-05-28 10:56
     * @param rcPeriodsId
     * @return java.util.List<com.tuanche.directselling.model.GiftRefuelingcardPeriodsCommodity>
     */
    List<GiftRefuelingcardPeriodsCommodity> getByRcPeriodsId(Integer rcPeriodsId);
    /**
     * 获取场次配置的商品类型列表
     * @author HuangHao
     * @CreatTime 2020-06-11 18:17
     * @param periodsId
     * @return java.util.List<com.tuanche.directselling.model.GiftRefuelingcardPeriodsCommodity>
     */
    List<GiftRefuelingcardPeriodsCommodity> getCommodityTypeByPeriodsId(Integer periodsId);
}
