package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.GiftRefuelingcardPeriods;
import org.springframework.stereotype.Repository;

/**
 * @author：HuangHao
 * @CreatTime 2020-05-28 10:17
 */
@Repository
public interface GiftRefuelingcardPeriodsWriteMapper {

    /**
     * 新增油卡配置
     * @author HuangHao
     * @CreatTime 2020-05-28 10:21
     * @param giftRefuelingcardPeriods
     * @return int
     */
    int insertGiftRefuelingcardPeriods(GiftRefuelingcardPeriods giftRefuelingcardPeriods);
    /**
     * 更新油卡配置
     * @author HuangHao
     * @CreatTime 2020-05-28 10:21
     * @param giftRefuelingcardPeriods
     * @return int
     */
    int updateGiftRefuelingcardPeriods(GiftRefuelingcardPeriods giftRefuelingcardPeriods);
}
