package com.tuanche.directselling.api;

import com.tuanche.directselling.dto.GiftRefuelingcardUsedStatisticsDto;
import com.tuanche.directselling.model.GiftRefuelingcard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author：HuangHao
 * @CreatTime 2020-06-01 18:12
 */
public interface GiftRefuelingcardService {

    /**
     * 油卡使用统计
     * @author HuangHao
     * @CreatTime 2020-06-01 18:10
     * @param
     * @return com.tuanche.directselling.dto.GiftRefuelingcardUsedStatisticsDto
     */
    GiftRefuelingcardUsedStatisticsDto refuelCardUsedStatistics();

    /**
     * 批量新增油卡信息
     * @author HuangHao
     * @CreatTime 2020-05-14 11:06
     * @param giftRefuelingcard
     * @return int
     */
    int batchInsert(List<GiftRefuelingcard> list);
    /**
     * 获取所有油卡卡号
     * @author HuangHao
     * @CreatTime 2020-06-10 14:59
     * @param
     * @return java.util.HashMap<java.lang.String,java.lang.String>
     */
    Map<String,Integer> getAllCardMap();
}
