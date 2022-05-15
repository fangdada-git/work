package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tuanche.directselling.api.GiftRefuelingcardService;
import com.tuanche.directselling.dto.GiftRefuelingcardUsedStatisticsDto;
import com.tuanche.directselling.mapper.read.directselling.GiftRefuelingcardReadMapper;
import com.tuanche.directselling.mapper.write.directselling.GiftRefuelingcardWriteMapper;
import com.tuanche.directselling.model.GiftRefuelingcard;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author：HuangHao
 * @CreatTime 2020-06-01 18:11
 */
@Service(timeout = 300000,retries = 0)
public class GiftRefuelingcardServiceImpl implements GiftRefuelingcardService {

    @Autowired
    GiftRefuelingcardReadMapper giftRefuelingcardReadMapper;
    @Autowired
    GiftRefuelingcardWriteMapper giftRefuelingcardWriteMapper;

    /**
     * 油卡使用统计
     * @author HuangHao
     * @CreatTime 2020-06-01 18:10
     * @param
     * @return com.tuanche.directselling.dto.GiftRefuelingcardUsedStatisticsDto
     */
    @Override
    public GiftRefuelingcardUsedStatisticsDto refuelCardUsedStatistics(){
        return giftRefuelingcardReadMapper.refuelCardUsedStatistics();
    }
    /**
     * 批量新增油卡信息
     * @author HuangHao
     * @CreatTime 2020-05-14 11:06
     * @param giftRefuelingcard
     * @return int
     */
    @Override
    public int batchInsert(List<GiftRefuelingcard> list){
        if(list == null || list.size() < 1){
            return 0;
        }
        return giftRefuelingcardWriteMapper.batchInsert(list);
    }

    /**
     * 获取所有油卡卡号
     * @author HuangHao
     * @CreatTime 2020-06-10 14:59
     * @param
     * @return java.util.HashMap<java.lang.String,java.lang.String>
     */
    @Override
    public Map<String,Integer> getAllCardMap(){
        List<String> list = giftRefuelingcardReadMapper.getAllCards();
        if(list == null ||  list.size() < 1){
            return null;
        }
        Map<String,Integer> cardsMap = new HashMap<>(list.size());
        for(String card:list){
            cardsMap.put(card, 1);
        }
        return cardsMap;
    }
}
