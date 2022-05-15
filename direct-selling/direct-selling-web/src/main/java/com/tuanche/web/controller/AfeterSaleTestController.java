package com.tuanche.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.exception.RedisException;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.directselling.api.*;
import com.tuanche.directselling.dto.AfterSaleUserBrowseDto;
import com.tuanche.directselling.model.AfterSaleOrderRecord;
import com.tuanche.directselling.model.AfterSaleUserShare;
import com.tuanche.directselling.utils.AfterSaleConstants;
import com.tuanche.directselling.utils.ApiBaseCacheKeys;
import com.tuanche.framework.util.IPUtil;
import com.tuanche.web.api.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 给测试用来测试售后特卖奖励的controller
 *
 * @param
 * @author HuangHao
 * @CreatTime 2021-08-02 16:40
 * @return
 */
@Controller
@RequestMapping("/afterSale/manual/test/")
public class AfeterSaleTestController extends BaseController {

    @Reference
    AfterSaleRewardRecordService afterSaleRewardRecordService;
    @Reference
    AfterSaleUserService afterSaleUserService;
    @Reference
    AfterSaleOrderRecordService afterSaleOrderRecordService;
    @Reference
    AfterSaleUserShareService afterSaleUserShareService;
    @Reference
    FashionCarWinningNumberService fashionCarWinningNumberService;
    @Autowired
    @Qualifier("ClusterRedisService")
    private RedisService redisService;

    //订单收到发放奖励
    @RequestMapping("/reward")
    @ResponseBody
    public ResultDto reward(Integer id) {
        AfterSaleOrderRecord record = afterSaleOrderRecordService.getById(id);
        //推广卡才计算奖励
        if (record != null && record.getOrderType() != null && AfterSaleConstants.OrderType.PROMOTION_CARD.getCode().equals(record.getOrderType())) {
            String key = ApiBaseCacheKeys.AFTER_SALE_ORDER_REWARD.getKey()+id;
            try {
                redisService.del(key);
            } catch (RedisException e) {
                e.printStackTrace();
            }
            return afterSaleRewardRecordService.reward(record);
        }
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(200);
        return resultDto;
    }

	
	//订单收到发放奖励
    @RequestMapping("/test")
    @ResponseBody
    public ResultDto test(Integer id) {
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(200);
        return resultDto;
    }


}
