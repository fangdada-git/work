package com.tuanche.web.api.aftersale;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.directselling.api.AfterSaleOrderRecordService;
import com.tuanche.directselling.api.AfterSaleRewardRecordService;
import com.tuanche.directselling.api.AfterSaleUserService;
import com.tuanche.directselling.api.AfterSaleUserShareService;
import com.tuanche.directselling.api.FashionCarWinningNumberService;
import com.tuanche.directselling.dto.AfterSaleUserBrowseDto;
import com.tuanche.directselling.model.AfterSaleOrderRecord;
import com.tuanche.directselling.model.AfterSaleUserShare;
import com.tuanche.framework.util.IPUtil;
import com.tuanche.web.api.BaseController;
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
@RequestMapping("/api/afterSale/agent/test")
public class AfterSaleTestController extends BaseController {

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

    @RequestMapping("/reward")
    @ResponseBody
    public ResultDto reward(Integer id) {
//        AfterSaleOrderRecord order = new AfterSaleOrderRecord();
        /*order.setOrderCode("DDBH20210725003");
        order.setActivityId(5);
        order.setDealerId(107606);
        order.setGoodsId(1236534);
        order.setOrderMoney(new BigDecimal("59.9"));
        order.setOrderType(1);
        order.setOrderStatus(3);
        order.setUserName("普通用户2");
        order.setUserPhone("18511111113");
        order.setAgentWxUnionId("oiYMg1g44ZipAAI6H6JjxDPOAksY");
        order.setShareWxUnionId("oiYMg1mZwMtHdm8oF9fNIj--CQ_8");
        order.setUserWxUnionId("oiYMg1mrXbvFHH0Jf3YcgkkAhUV8");
        order.setUserWxOpenId("oPe6Jt5uPaXrA4DZMKOc5aIm1UjM");*/

        AfterSaleOrderRecord record = new AfterSaleOrderRecord();
        record.setId(id);
        afterSaleOrderRecordService.updateAfterSaleOrderPaySuccess(record);
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(200);
        return resultDto;
    }

    /**
     * 新增用户浏览记录
     * @author HuangHao
     * @CreatTime 2021-07-21 17:09
     * @param
     * @return com.tuanche.arch.web.model.TcResponse
     */
    @RequestMapping("/userShare")
    @ResponseBody
    public TcResponse userShare(HttpServletRequest request){
        AfterSaleUserBrowseDto afterSaleUserBrowseDto = new AfterSaleUserBrowseDto();
        afterSaleUserBrowseDto.setActivityId(5);
        afterSaleUserBrowseDto.setAgentWxUnionId("oiYMg1g44ZipAAI6H6JjxDPOAksY");
        afterSaleUserBrowseDto.setShareWxUnionId("oiYMg1mrXbvFHH0Jf3YcgkkAhUV8");
        afterSaleUserBrowseDto.setNickName("普通用户4");
        afterSaleUserBrowseDto.setUserWxUnionId("oiYMg1lRM7zyhnp6qN3vXrgjdySY");
        afterSaleUserBrowseDto.setUserWxOpenId("oPe6Jt6Ym0SrIe5iHwsH21OlRs4Q");
        afterSaleUserBrowseDto.setUserWxHead("https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKturiaZCpWiaO60xp79XKlXQZtvctrtAOvlibY8icDcib3R3LMmmsa1FfuNghicjfbTnbRdSwWsFwqXsBg/132");
        String ip = IPUtil.getRequestIp(request);
        afterSaleUserBrowseDto.setIp(ip);
        afterSaleUserBrowseDto.setPageUrl(request.getHeader("Referer"));
//        afterSaleUserBrowseDto.setChanel(1);
        return afterSaleUserService.userBrowse(afterSaleUserBrowseDto);

    }

    @RequestMapping("/userbrowse")
    @ResponseBody
    public TcResponse userbrowse(HttpServletRequest request, AfterSaleUserShare record) {
        String ip = IPUtil.getRequestIp(request);
        record.setIp(ip);
        record.setPageUrl(request.getHeader("Referer"));
        return afterSaleUserShareService.insert(record);

    }


//    @RequestMapping("/createWinningNumber")
//    @ResponseBody
//    public TcResponse createWinningNumber(HttpServletRequest request) {
//        long st = System.currentTimeMillis();
//        String num = "";
//        try {
//            num = fashionCarWinningNumberService.createWinningNumber(1000037, (int) (Math.random() * 1000 + 1), 1, 1);
//        } catch (WinningNumberException e) {
//            e.printStackTrace();
//        }
//        return success(num, st);
//
//    }
//
//
//    @RequestMapping("/processWinningNumber")
//    @ResponseBody
//    public TcResponse processWinningNumber(HttpServletRequest request) {
//        long st = System.currentTimeMillis();
//        fashionCarWinningNumberService.processWinningNumber();
//        return success(0, st);
//
//    }
//
//    @RequestMapping("/chooseWinningNumber")
//    @ResponseBody
//    public TcResponse chooseWinningNumber(HttpServletRequest request) {
//        long st = System.currentTimeMillis();
//        fashionCarWinningNumberService.chooseWinningNumber();
//        return success(0, st);
//
//    }

}
