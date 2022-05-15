package com.tuanche.directselling.api;

import com.tuanche.commons.util.ResultDto;
import com.tuanche.directselling.dto.AfterSaleCouponDto;
import com.tuanche.directselling.dto.ResultMapDto;
import com.tuanche.directselling.model.AfterSaleCouponRecord;
import com.tuanche.directselling.model.AfterSaleUserRewardStatistics;
import com.tuanche.directselling.utils.AfterSaleConstants;
import com.tuanche.directselling.utils.PageResult;

import java.util.List;
import java.util.Map;

public interface AfterSaleCouponService {

    List<AfterSaleCouponDto> getAfterSaleCouponList (AfterSaleCouponRecord saleCouponRecord);

    PageResult<AfterSaleCouponDto> getAfterSaleCouponListByPage(PageResult<AfterSaleCouponDto> pageResult, AfterSaleCouponRecord afterSaleCouponRecord);

    AfterSaleCouponDto getAfterSaleCouponByUserCouponId (Integer userCouponId, String userWxUnionId);

    void addAfterSaleCouponList (List<AfterSaleCouponRecord> list);

    Integer addAfterSaleCoupon (AfterSaleCouponRecord saleCouponRecord);

    Integer updateAfterSaleCoupon (AfterSaleCouponRecord saleCouponRecord);

    Integer updateAfterSaleCouponByUserCouponId (AfterSaleCouponRecord saleCouponRecord);

    /**
     * @description : 发放卡券
     * @author : fxq
     * @param : orderCode 订单编码
     * @param : couponGiveOutType 卡券发放类型  枚举 AfterSaleConstants.CouponGiveOutType
     * @return :
     * @date : 2021/7/22 22:01
     */
    ResultDto giveOutCoupon (String orderCode, AfterSaleConstants.CouponGiveOutType couponGiveOutType);

    void delCoupon(String orderCode, List<Integer> couponTypeList);

    ResultDto scanGetCoupon(String orderCodes, AfterSaleConstants.CouponType couponType, AfterSaleConstants.CouponStatus couponStatus);

    /**
     * 已核销待发券列表
     *
     * @return
     */
    List<AfterSaleCouponDto> getAfterSaleCouponWrittenOffList(AfterSaleCouponDto afterSaleCouponDto);

    /**
     * 获取核销礼品券数量
     * @author HuangHao
     * @CreatTime 2021-08-24 13:54
     * @param list
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    Map<String, ResultMapDto> getWriteOffGiftVoucherMap(List<Integer> activityIds);

    /**
     * 获取已经核销了礼品券的用户
     * @author HuangHao
     * @CreatTime 2021-08-24 13:54
     * @param list
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    Map<String, ResultMapDto> getWriteOffGgiftCertificatesUserMap(List<AfterSaleUserRewardStatistics> list);

    ResultDto supplyAgainCoupon (Integer activityId,String orderCode, Integer activityStart);

    Map<String, Integer> getOrderCouponCount(String orderCode, Integer activityId, Integer goodsId, Integer type);

}
