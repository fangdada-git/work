package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.GiftRefuelingcardGiftrecordCards;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2020-06-04 10:37
 */
public interface GiftRefuelingcardGiftrecordCardsWriteMapper {

    /**
     * 批量新增
     * @author HuangHao
     * @CreatTime 2020-06-04 10:39
     * @param giftrecordId
     * @param list
     * @return int
     */
    int batchInsert(@Param("list")List<GiftRefuelingcardGiftrecordCards> list);
}
