package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.GiftRefuelingcardGiftrecordDto;
import com.tuanche.directselling.model.GiftRefuelingcardGiftrecord;
import com.tuanche.directselling.vo.GiftRefuelingcardGiftrecordVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * GiftRefuelingcardGiftrecordDAO继承基类
 */
@Repository
public interface GiftRefuelingcardGiftrecordReadMapper {

    /**
     * 获取油卡赠送记录列表
     * @author HuangHao
     * @CreatTime 2020-05-12 17:57
     * @param
     * @return java.util.List<com.tuanche.directselling.model.GiftRefuelingcardGiftrecord>
     */
    List<GiftRefuelingcardGiftrecordDto> getGiftRefuelingcardGiftrecordList(GiftRefuelingcardGiftrecordVo giftrecord);

    /**
     * 获取经销商赠送油卡总数
     * @author HuangHao
     * @CreatTime 2020-05-13 15:58
     * @param giftrecord
     * @return int
     */
    int getDealerGiveTotal(GiftRefuelingcardGiftrecordVo giftrecord);

    /**
     * 根据经销商ID获取赠送记录
     * @author HuangHao
     * @CreatTime 2020-05-14 17:06
     * @param id
     * @return com.tuanche.directselling.model.GiftRefuelingcardGiftrecord
     */
    GiftRefuelingcardGiftrecord getGiftRefuelingcardGiftrecordById(Integer id);

    /**
     * 获取赠送记录和油卡卡号
     * @author HuangHao
     * @CreatTime 2020-05-14 18:10
     * @param id
     * @return com.tuanche.directselling.dto.GiftRefuelingcardGiftrecordDto
     */
    GiftRefuelingcardGiftrecordDto getGiftrecordAndRefuelingCode(Integer id);
}