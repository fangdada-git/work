package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.model.GiftRefuelingcardGiftrecordCards;

import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2020-06-04 11:54
 */
public interface GiftRefuelingcardGiftrecordCardsReadMapper {

    /**
     * 根据赠送油卡记录ID获取赠送的油卡列表
     * @author HuangHao
     * @CreatTime 2020-06-04 11:54
     * @param giftrecordId
     * @return com.tuanche.directselling.model.GiftRefuelingcardGiftrecordCards
     */
    List<GiftRefuelingcardGiftrecordCards> getCardsByGiftrecordId(Integer giftrecordId);
}
