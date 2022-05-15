package com.tuanche.directselling.mapper.write.directselling;


import com.tuanche.directselling.model.AfterSaleActivityCoupon;

public interface AfterSaleActivityCouponWriteMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(AfterSaleActivityCoupon record);

    int insertSelective(AfterSaleActivityCoupon record);

    AfterSaleActivityCoupon selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AfterSaleActivityCoupon record);

    int updateByPrimaryKey(AfterSaleActivityCoupon record);

    /**
     *  根据套餐id删除卡券
     * @param record
     * @return
     */
    int deleteByPackageId(AfterSaleActivityCoupon record);
}