package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.AfterSaleCouponRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AfterSaleCouponRecordWriteMapper extends MyBatisBaseDao<AfterSaleCouponRecord, Integer> {

    Integer updateAfterSaleCouponByUserCouponId(AfterSaleCouponRecord saleCouponRecord);

    void addAfterSaleCouponList(@Param("list") List<AfterSaleCouponRecord> list);

    void delCoupon(@Param("userWxUnionId") String userWxUnionId, @Param("orderCode") String orderCode, @Param("couponTypeList") List<Integer> couponTypeList);

    void updateByOrderCode(@Param("orderCode") String orderCode, @Param("couponType") Integer couponType,@Param("couponStatus") Integer couponStatus);

    void updateStatusByIds(@Param("couponStatus") Integer couponStatus, @Param("ids") List<Integer> ids);
}