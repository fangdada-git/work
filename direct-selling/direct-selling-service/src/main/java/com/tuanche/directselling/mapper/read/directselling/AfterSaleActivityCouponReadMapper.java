package com.tuanche.directselling.mapper.read.directselling;


import com.tuanche.directselling.model.AfterSaleActivityCoupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AfterSaleActivityCouponReadMapper {

    AfterSaleActivityCoupon selectByPrimaryKey(Integer id);

    /**
     * 查询活动下抵用券
     * @param id
     * @return
     */
    List<AfterSaleActivityCoupon> selectCouponList(AfterSaleActivityCoupon afterSaleActivityCoupon);

    Integer getCouponCount (@Param("activityId") Integer activityId, @Param("goodsId") Integer goodsId,@Param("type") Integer type);

}