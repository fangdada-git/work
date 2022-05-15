package com.tuanche.web.api.aftersale.v2;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.directselling.api.AfterSaleAgentService;
import com.tuanche.directselling.api.AfterSaleOrderRecordService;
import com.tuanche.directselling.api.AfterSaleStatService;
import com.tuanche.directselling.api.AfterSaleTeacherService;
import com.tuanche.directselling.dto.AfterSaleOrderPersonRecordDto;
import com.tuanche.directselling.dto.AfterSaleOrderRecordDto;
import com.tuanche.directselling.dto.AfterSaleOrderRecordWriteOffStatisticsDto;
import com.tuanche.directselling.enums.AfterSaleAgentSortType;
import com.tuanche.directselling.model.AfterSaleAgent;
import com.tuanche.directselling.utils.AfterSaleConstants.OrderStatus;
import com.tuanche.directselling.utils.AfterSaleConstants.OrderType;
import com.tuanche.directselling.utils.BeanCopyUtil;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.web.api.BaseController;
import com.tuanche.web.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@RequestMapping("/api/v2/aftersale")
@RestController
public class AfterSaleStat2Controller extends BaseController {

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Reference
    AfterSaleStatService afterSaleStatService;
    @Reference
    AfterSaleAgentService afterSaleAgentService;
    @Reference
    AfterSaleTeacherService afterSaleTeacherService;
    @Reference
    AfterSaleOrderRecordService afterSaleOrderRecordService;

    /**
     * 代理人分享数据统计
     *
     * @return
     */
    @PostMapping("/share/stat")
    public TcResponse shareStat(@RequestBody AfterSaleAgentStatSearchVo statSearchVo) {
        long st = System.currentTimeMillis();
        if (statSearchVo.getActivityId() == null) {
            return paramInvalid(st);
        }
        if (statSearchVo.getAgentWxUnionId() == null) {
            return paramInvalid(st);
        }
//        AfterSaleTeacher afterSaleTeacher = afterSaleTeacherService.selectByTeacherWxUnionId(statSearchVo.getAgentWxUnionId());
        AfterSaleAgent afterSaleAgent = afterSaleAgentService.getAfterSaleAgent(statSearchVo.getActivityId(), statSearchVo.getAgentWxUnionId());
//        if (afterSaleTeacher == null && afterSaleAgent == null) {
//            return response(StatusCodeEnum.NO_ACCESS, StatusCodeEnum.NO_ACCESS.getText(), st);
//        }
        if (afterSaleAgent == null) {
            return response(StatusCodeEnum.NO_ACCESS, StatusCodeEnum.NO_ACCESS.getText(), st);
        }
        if (!afterSaleAgent.getIsAdmin()) {
            return response(StatusCodeEnum.NO_ACCESS, StatusCodeEnum.NO_ACCESS.getText(), st);
        }
        return success(BeanCopyUtil.copyListProperties(afterSaleStatService.selectShareStat(statSearchVo.getActivityId(), getStartDate(statSearchVo.getStartTime()), getEndDate(statSearchVo.getEndTime()), AfterSaleAgentSortType.getEnumType(statSearchVo.getOrderBy())), AfterSaleAgentStatVo::new), st);
    }

    /**
     * 个人业绩
     *
     * @param statSearchVo
     * @return
     */
    @PostMapping("/person/order")
    public TcResponse personOrder(@RequestBody AfterSaleAgentStatSearchVo statSearchVo) {
        long st = System.currentTimeMillis();
        if (statSearchVo.getActivityId() == null) {
            return paramInvalid(st);
        }
        if (statSearchVo.getAgentWxUnionId() == null) {
            return paramInvalid(st);
        }
        AfterSaleOrderPersonRecordDto personRecordDto = new AfterSaleOrderPersonRecordDto();
        BeanUtils.copyProperties(statSearchVo, personRecordDto);
        List<AfterSaleOrderPersonRecordDto> afterSaleOrderPersonRecordDtos = afterSaleStatService.selectSaleOrderByAgent(personRecordDto);
        List<AfterSaleAgentOrderVo> agentOrderVos = new ArrayList<>();
        AfterSaleAgentOrderVo afterSaleAgentOrderVo = null;
        Map<String, List<AfterSaleOrderPersonRecordDto>> orderMap = new HashMap<>();
        for (AfterSaleOrderPersonRecordDto dto : afterSaleOrderPersonRecordDtos) {
            if (orderMap.get(dto.getUserWxUnionId()) == null) {
                orderMap.put(dto.getUserWxUnionId(), new ArrayList<>());
            }
            orderMap.get(dto.getUserWxUnionId()).add(dto);
        }
        for (AfterSaleOrderPersonRecordDto dto : afterSaleOrderPersonRecordDtos) {
            afterSaleAgentOrderVo = new AfterSaleAgentOrderVo();
            BeanUtils.copyProperties(dto, afterSaleAgentOrderVo);

            //订单状态:     待核销： 3:已支付 12已进店       已核销:11待发券 4:核销      已退款：7:退款完成 13退款完成(自动退款,定时任务)    8:等待买家确认收货 5:申请退款 6:退款中 1:待支付 2:支付中
            //0:待核销 1:已核销 2:购套餐 3:已退款 4:备案用户
            if (OrderStatus.PAID.getCode().equals(dto.getOrderStatus()) || OrderStatus.ARRIVE_SHOP.getCode().equals(dto.getOrderStatus())) {
                afterSaleAgentOrderVo.setOrderStatus(0);
            } else if (OrderStatus.CHECKOUT.getCode().equals(dto.getOrderStatus()) || OrderStatus.GRANT_COUPON_NON.getCode().equals(dto.getOrderStatus())) {
                afterSaleAgentOrderVo.setOrderStatus(1);
            } else if (OrderStatus.REFUND_SUCCESS_HAND.getCode().equals(dto.getOrderStatus()) || OrderStatus.REFUND_SUCCESS_TIMING.getCode().equals(dto.getOrderStatus())) {
                afterSaleAgentOrderVo.setOrderStatus(3);
            }
            //购套餐
            if (OrderType.SYNTHESIZE_CARD.getCode().equals(dto.getOrderType())) {
                afterSaleAgentOrderVo.setOrderStatus(2);
            }
            //备案
            if (dto.getKeepOnRecordUser() != null && dto.getKeepOnRecordUser()) {
                afterSaleAgentOrderVo.setKeepOnRecordUser(1);
            } else {
                afterSaleAgentOrderVo.setKeepOnRecordUser(0);
            }

//            List<AfterSaleOrderPersonRecordDto> orderPersonRecordDtos = orderMap.get(afterSaleAgentOrderVo.getUserWxUnionId());
//            if (!orderPersonRecordDtos.isEmpty()) {
//                for (AfterSaleOrderPersonRecordDto afterSaleOrderPersonRecordDto : orderPersonRecordDtos) {
//                    if (afterSaleOrderPersonRecordDto.getOrderType() == 2) {
//                        afterSaleAgentOrderVo.setOrderStatus(2);
//                        break;
//                    }
//                }
//            }

            agentOrderVos.add(afterSaleAgentOrderVo);
        }
        return success(agentOrderVos, st);
    }

    /**
     * 核销统计
     *
     * @param orderRecord
     * @return
     */
    @PostMapping("/writeOffStat")
    public TcResponse writeOffStat(@RequestBody AfterSaleWriteOffStatisticsSearchVo orderRecord) {
        long st = System.currentTimeMillis();
        AfterSaleAgent afterSaleAgent = afterSaleAgentService.getAfterSaleAgent(orderRecord.getActivityId(), orderRecord.getAgentWxUnionId());
        if (afterSaleAgent == null) {
            return response(StatusCodeEnum.NO_ACCESS, StatusCodeEnum.NO_ACCESS.getText(), st);
        }
        if (!afterSaleAgent.getIsAdmin()) {
            return response(StatusCodeEnum.NO_ACCESS, StatusCodeEnum.NO_ACCESS.getText(), st);
        }
        AfterSaleOrderRecordDto afterSaleOrderRecordDto = new AfterSaleOrderRecordDto();
        afterSaleOrderRecordDto.setActivityId(orderRecord.getActivityId());
        afterSaleOrderRecordDto.setDealerId(orderRecord.getDealerId());
        afterSaleOrderRecordDto.setStartTime(getStartDate(orderRecord.getStartTime()));
        afterSaleOrderRecordDto.setEndTime(getEndDate(orderRecord.getEndTime()));
        List<AfterSaleOrderRecordWriteOffStatisticsDto> statisticsDtos = afterSaleOrderRecordService.writeOffStatistics(afterSaleOrderRecordDto);
        return success(BeanCopyUtil.copyListProperties(statisticsDtos, AfterSaleWriteOffStatisticsVo::new), st);
    }

    private Date getStartDate(Integer startTime) {
        if (startTime == null) {
            return null;
        }
        String startTimeStr = startTime.toString();
        try {
            return sdf.parse(StringUtils.join(startTimeStr.substring(0, 4), "-", startTimeStr.substring(4, 6), "-", startTimeStr.substring(6, 8), " 00:00:00"));
        } catch (ParseException e) {
            return null;
        }
    }

    private Date getEndDate(Integer endTime) {
        if (endTime == null) {
            return null;
        }
        String endTimeStr = endTime.toString();
        try {
            return sdf.parse(StringUtils.join(endTimeStr.substring(0, 4), "-", endTimeStr.substring(4, 6), "-", endTimeStr.substring(6, 8), " 23:59:59"));
        } catch (ParseException e) {
            return null;
        }
    }

}
