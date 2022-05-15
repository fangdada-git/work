package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.GiftRefuelingcardUsedStatisticsDto;
import com.tuanche.directselling.model.GiftRefuelingcard;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * GiftRefuelingcardDAO继承基类
 */
@Repository
public interface GiftRefuelingcardReadMapper {

    /**
     * 获取指定条数的未赠送油卡
     * @author HuangHao
     * @CreatTime 2020-05-13 18:39
     * @param
     * @return com.tuanche.directselling.model.GiftRefuelingcard
     */
    List<GiftRefuelingcard> getNoGiftRefuelingcardList(Integer limit);

    /**
     * 根据赠送油卡记录ID获取油卡卡号
     * @author HuangHao
     * @CreatTime 2020-05-14 17:34
     * @param recordId
     * @return java.lang.String
     */
    String getRefuelingCodeByRecordId(Integer recordId);

    /**
     * 油卡使用统计
     * @author HuangHao
     * @CreatTime 2020-06-01 18:10
     * @param
     * @return com.tuanche.directselling.dto.GiftRefuelingcardUsedStatisticsDto
     */
    GiftRefuelingcardUsedStatisticsDto refuelCardUsedStatistics();

    /**
     * 获取所有油卡卡号
     * @author HuangHao
     * @CreatTime 2020-06-10 14:59
     * @param
     * @return List<String>
     */
    List<String> getAllCards();
}