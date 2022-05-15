package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.AfterSaleOrderCouponInfoDto;
import com.tuanche.directselling.dto.AfterSaleCouponDto;
import com.tuanche.directselling.dto.ResultMapDto;
import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.AfterSaleCouponRecord;
import com.tuanche.directselling.model.AfterSaleUserRewardStatistics;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AfterSaleCouponRecordReadMapper extends MyBatisBaseDao<AfterSaleCouponRecord, Integer> {

    List<AfterSaleCouponDto> getAfterSaleCouponList(AfterSaleCouponRecord saleCouponRecord);

    AfterSaleCouponDto getAfterSaleCouponByUserCouponId(@Param("userCouponId") Integer userCouponId, @Param("userWxUnionId") String userWxUnionId);

    List<AfterSaleCouponDto> getAfterSaleCouponWrittenOffList(AfterSaleCouponDto afterSaleCouponDto);

    List<AfterSaleCouponDto> getAfterSaleCouponByOrderCode(@Param("orderCode") String orderCode, @Param("couponType") Integer couponType);
    
    /**
     * 获取核销礼品券数量
     * @author HuangHao
     * @CreatTime 2021-08-24 13:54 
     * @param list 
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    @MapKey("mapKey")
    Map<String, ResultMapDto> getWriteOffGiftVoucherMap(@Param("activityIds") List<Integer> activityIds);
    /**
     * 获取已经核销了礼品券的用户
     * @author HuangHao
     * @CreatTime 2021-08-24 13:54
     * @param list
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    @MapKey("mapKey")
    Map<String, ResultMapDto> getWriteOffGgiftCertificatesUserMap(@Param("list") List<AfterSaleUserRewardStatistics> list);

    List<AfterSaleOrderCouponInfoDto> supplyAgainCoupon(@Param("activityId")Integer activityId,@Param("orderCode") String orderCode,@Param("activityStart") Integer activityStart);

    List<AfterSaleOrderCouponInfoDto> getOrderCouponCount(@Param("orderCodeList")List<String> orderCodeList);

    List<AfterSaleOrderCouponInfoDto> getActivityGoodsCouponCount(@Param("couponCountList") List<AfterSaleOrderCouponInfoDto> couponCountList);

    Map<String, Long> getCouponCountByOrderCode(String orderCode);
}