package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.GiftRefuelingcard;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * GiftRefuelingcardDAO继承基类
 */
@Repository
public interface GiftRefuelingcardWriteMapper {

    /**
     * 更新油卡信息
     * @author HuangHao
     * @CreatTime 2020-05-14 11:06
     * @param giftRefuelingcard
     * @return int
     */
    int updateGiftRefuelingcard(GiftRefuelingcard giftRefuelingcard);
    /**
     * 批量新增油卡信息
     * @author HuangHao
     * @CreatTime 2020-05-14 11:06
     * @param giftRefuelingcard
     * @return int
     */
    int batchInsert(List<GiftRefuelingcard> list);
    /**
     * 根据油卡卡号更新油卡状态
     * @author HuangHao
     * @CreatTime 2020-06-04 10:27
     * @param state
     * @param refuelingCodes
     * @return int
     */
    int updateStateByRefuelingCodes(@Param("state")Integer state, @Param("refuelingCodes")List<String> refuelingCodes);
}