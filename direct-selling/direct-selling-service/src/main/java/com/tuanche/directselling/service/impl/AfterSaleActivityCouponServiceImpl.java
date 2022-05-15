package com.tuanche.directselling.service.impl;

import com.tuanche.directselling.mapper.read.directselling.AfterSaleActivityCouponReadMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AfterSaleActivityCouponServiceImpl {

    @Autowired
    private AfterSaleActivityCouponReadMapper afterSaleActivityCouponReadMapper;

    /**
      * @description : 活动商品应发卡券数量
      * @author : fxq
      * @param : type 1:推广卡  2：套餐卡
      * @return :
      * @date : 2021/12/18 14:28
      */
    public Integer getCouponCount (Integer activityId, Integer goodsId, Integer type) {
        if (activityId==null || type==null || (type==2&& goodsId==null)) return 0;
        Integer couponCount = afterSaleActivityCouponReadMapper.getCouponCount(activityId, goodsId, type);
        return couponCount==null?0:couponCount;
    }

}
