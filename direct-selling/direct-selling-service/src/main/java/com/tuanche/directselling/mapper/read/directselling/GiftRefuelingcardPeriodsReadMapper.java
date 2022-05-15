package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.GiftRefuelingcardPeriodsDto;
import com.tuanche.directselling.vo.GiftRefuelingcardPeriodsVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2020-05-27 15:28
 */
@Repository
public interface GiftRefuelingcardPeriodsReadMapper {


    /**
     * 根据ID获取油卡场次配置
     * @author HuangHao
     * @CreatTime 2020-05-28 10:34
     * @param id
     * @return com.tuanche.directselling.dto.GiftRefuelingcardPeriodsDto
     */
    GiftRefuelingcardPeriodsDto getById( Integer id);
    /**
     * 根据大场次ID获取油卡场次配置
     * @author HuangHao
     * @CreatTime 2020-05-28 10:34
     * @param id
     * @return com.tuanche.directselling.dto.GiftRefuelingcardPeriodsDto
     */
    GiftRefuelingcardPeriodsDto getByPeriodsId(Integer periodsId);
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
    List<GiftRefuelingcardPeriodsDto> getFuelCardPeriodsConfigList(GiftRefuelingcardPeriodsVo refuelingcardPeriodsVo);
}
