package com.tuanche.directselling.api;

import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.directselling.dto.EnumStrDto;
import com.tuanche.directselling.dto.GiftRefuelingcardPeriodsDto;
import com.tuanche.directselling.model.GiftRefuelingcardPeriods;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.vo.GiftRefuelingcardPeriodsVo;

import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2020-05-27 17:03
 */
public interface GiftRefuelingcardPeriodsService {

    /**
     * 根据ID获取油卡场次配置
     * @author HuangHao
     * @CreatTime 2020-05-28 10:34
     * @param id
     * @return com.tuanche.directselling.dto.GiftRefuelingcardPeriodsDto
     */
    GiftRefuelingcardPeriodsDto getById(Integer id);
    /**
     * 根据ID获取油卡场次配置详细新（商品类型列表，油卡配置列表）
     * @author HuangHao
     * @CreatTime 2020-05-28 10:34
     * @param id
     * @return com.tuanche.directselling.dto.GiftRefuelingcardPeriodsDto
     */
    GiftRefuelingcardPeriodsDto getDetailsById(Integer id);
    /**
     * 获取油卡场次配置列表-带格式化的商品类型和油卡规则
     * @author HuangHao
     * @CreatTime 2020-05-27 16:45
     * @param
     * @return java.util.List<com.tuanche.directselling.dto.GiftRefuelingcardPeriodsDto>
     */
    PageResult<GiftRefuelingcardPeriodsDto> getFuelCardPeriodsConfigListByPage(GiftRefuelingcardPeriodsVo refuelingcardPeriodsVo);

    /**
     * 获取直播商品类型列表
     * @author HuangHao
     * @CreatTime 2020-05-27 17:52
     * @param
     * @return java.util.List<com.tuanche.directselling.dto.EnumStrDto>
     */
    List<EnumStrDto> getCommodityList();

    /**
     *  新增油卡配置
     * @author HuangHao
     * @CreatTime 2020-05-28 14:42
     * @param refuelcardPeriodrs
     * @return com.tuanche.arch.web.model.TcResponse
     */
    TcResponse inster(GiftRefuelingcardPeriodsDto refuelcardPeriodrs);
    /**
     *  更新油卡配置
     * @author HuangHao
     * @CreatTime 2020-05-28 14:42
     * @param refuelcardPeriodrs
     * @return com.tuanche.arch.web.model.TcResponse
     */
    TcResponse update(GiftRefuelingcardPeriodsDto refuelcardPeriodrs);

    /**
     * 删除油卡配置
     * @author HuangHao
     * @CreatTime 2020-05-29 9:50
     * @param giftRefuelingcardPeriods
     * @return com.tuanche.arch.web.model.TcResponse
     */
    TcResponse delete(GiftRefuelingcardPeriods giftRefuelingcardPeriods);
}
