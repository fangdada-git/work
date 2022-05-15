package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.GiftRefuelingcardGiftrecord;
import org.springframework.stereotype.Repository;

/**
 * GiftRefuelingcardGiftrecordDAO继承基类
 */
@Repository
public interface GiftRefuelingcardGiftrecordWriteMapper {

    /**
     * 新增赠送记录
     * @author HuangHao
     * @CreatTime 2020-05-14 10:57
     * @param giftrecord
     * @return int
     */
    int insertGiftRefuelingcardGiftrecord(GiftRefuelingcardGiftrecord giftrecord);

    /**
     * 更新赠送记录
     * @author HuangHao
     * @CreatTime 2020-05-14 10:57
     * @param giftrecord
     * @return int
     */
    int updateGiftRefuelingcardGiftrecord(GiftRefuelingcardGiftrecord giftrecord);
}