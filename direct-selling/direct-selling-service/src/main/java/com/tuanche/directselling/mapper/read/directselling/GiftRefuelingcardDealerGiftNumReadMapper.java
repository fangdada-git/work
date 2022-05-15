package com.tuanche.directselling.mapper.read.directselling;

import org.springframework.stereotype.Repository;

@Repository
public interface GiftRefuelingcardDealerGiftNumReadMapper {
    /**
     * 获取经销商的礼品赠送次数
     * @author HuangHao
     * @CreatTime 2020-05-19 10:06
     * @param null
     * @return
     */

    Integer getGiftNumByDealerId(Integer dealerId);


}