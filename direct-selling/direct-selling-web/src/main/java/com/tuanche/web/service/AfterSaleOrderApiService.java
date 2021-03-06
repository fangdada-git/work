package com.tuanche.web.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.directselling.api.AfterSaleOrderRecordService;
import com.tuanche.directselling.dto.AfterSaleOrderRecordDto;
import com.tuanche.directselling.model.AfterSaleOrderRecord;
import com.tuanche.directselling.utils.AfterSaleConstants;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.manubasecenter.dto.TcResponse;
import com.tuanche.web.util.DirectCommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AfterSaleOrderApiService {

    @Reference
    private AfterSaleOrderRecordService afterSaleOrderRecordService;

    public TcResponse getUserBuyCheck (long st, AfterSaleOrderRecordDto afterSaleOrderRecord) {
        AfterSaleOrderRecord record = new AfterSaleOrderRecord();
        record.setActivityId(afterSaleOrderRecord.getActivityId());
        record.setGoodsId(afterSaleOrderRecord.getGoodsId());
        record.setUserWxUnionId(afterSaleOrderRecord.getUserWxUnionId());
        record.setUserPhone(afterSaleOrderRecord.getUserPhone());
        record.setLicencePlate(afterSaleOrderRecord.getLicencePlate());
        record.setOrderType(AfterSaleConstants.OrderType.PROMOTION_CARD.getCode());
        if (afterSaleOrderRecord.getOrderStatus()!=null) {
            record.setOrderStatus(afterSaleOrderRecord.getOrderStatus());
        } else {
            record.setOrderStatusList(Arrays.asList(
                    AfterSaleConstants.OrderStatus.PAID.getCode()
                    ,AfterSaleConstants.OrderStatus.CHECKOUT.getCode()
                    ,AfterSaleConstants.OrderStatus.REFUND_SUCCESS_HAND.getCode()
                    ,AfterSaleConstants.OrderStatus.GRANT_COUPON_NON.getCode()
                    ,AfterSaleConstants.OrderStatus.ARRIVE_SHOP.getCode()
            ));
        }
        List<AfterSaleOrderRecord> saleOrderRecords = afterSaleOrderRecordService.getUserBuyCheck(record);
        if (CollectionUtils.isNotEmpty(saleOrderRecords)) {
            String msg = "";
            if (afterSaleOrderRecord.getOrderStatus()!=null) {
                for (AfterSaleOrderRecord orderRecord : saleOrderRecords) {
                    if (!orderRecord.getUserWxUnionId().equals(record.getUserWxUnionId())) {
                        if (orderRecord.getUserPhone().equals(record.getUserPhone())) {
                            msg = "???????????????????????????????????????????????????";
                        }
                        if (orderRecord.getLicencePlate().equals(record.getLicencePlate())) {
                            msg = "???????????????????????????????????????????????????";
                        }
                    }
                }
            } else {
                if (saleOrderRecords.get(0).getUserWxUnionId().equals(afterSaleOrderRecord.getUserWxUnionId())) {
                    msg = "???????????????????????????????????????????????????";
                } else if (saleOrderRecords.get(0).getLicencePlate().equals(afterSaleOrderRecord.getLicencePlate())) {
                    msg = "???????????????????????????????????????????????????";
                } else if (saleOrderRecords.get(0).getUserPhone().equals(afterSaleOrderRecord.getUserPhone())) {
                    msg = "???????????????????????????????????????????????????";
                }
            }
            if (msg == "") {
                return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, "");
            }
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.DATA_ALREADY_EXISTED.getCode(), msg, st,  "");
        } else {
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, "");
        }
    }

}
