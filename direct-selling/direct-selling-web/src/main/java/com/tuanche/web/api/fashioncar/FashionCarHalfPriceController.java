package com.tuanche.web.api.fashioncar;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.directselling.api.FashionHalfPriceCarService;
import com.tuanche.directselling.dto.FashionHalfPriceCarActivityDto;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.FashionHalfPriceBrandVo;
import com.tuanche.web.service.CommonWebService;
import com.tuanche.web.util.CommonLogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * @Author lvsen
 * @Description 半价车
 * @Date 2021/9/16
 **/
@RequestMapping("/api/fashioncar/halfprice")
@RestController
public class FashionCarHalfPriceController {

    @Reference
    FashionHalfPriceCarService fashionHalfPriceCarService;
    @Autowired
    private CommonWebService commonWebService;

    /**
     * 获取半价车活动信息
     * @param request
     * @param activityVo
     * @return
     */
    @RequestMapping(value = "/getActivityInfoForApi", method = RequestMethod.POST)
    public TcResponse getActivityInfoForApi(HttpServletRequest request, FashionHalfPriceCarActivityDto activityVo) {
        CommonLogUtil.addInfo("半价车活动首页-获取活动数据接口 start", "入参：", JSON.toJSONString(activityVo));
        if (Objects.isNull(activityVo) || Objects.isNull(activityVo.getPeriodsId())) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "活动无效");
        }
        return fashionHalfPriceCarService.getHalfPriceActivityInfoForApi(activityVo, commonWebService.getMemberPoIdByToken(request));
    }

    /**
     * 获取半价车品牌列表
     * @param request
     * @param activityVo
     * @return
     */
    @RequestMapping(value = "/getBrandList", method = RequestMethod.POST)
    public TcResponse getBrandList(HttpServletRequest request, FashionHalfPriceCarActivityDto activityVo) {
        long st = System.currentTimeMillis();
        CommonLogUtil.addInfo("潮车集-获取品牌列表接口 start", "入参：", JSON.toJSONString(activityVo));
        if (Objects.isNull(activityVo) || Objects.isNull(activityVo.getPeriodsId())) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "活动无效");
        }
        List<FashionHalfPriceBrandVo> brandList = fashionHalfPriceCarService.getHalfPriceActivityBrandList(activityVo);
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), System.currentTimeMillis() - st, StatusCodeEnum.SUCCESS.getText(), brandList);
    }

    /**
      * @description : 场次品牌有无在活动时间内的半价车活动
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/10/21 14:28
      */
    @RequestMapping("/getBrandHalfpriceFlag")
    public TcResponse getBrandHalfpriceFlag(Integer periodsId, Integer brandId) {
        long st = System.currentTimeMillis();
        if (periodsId==null || brandId==null) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "参数无效");
        }
        Integer count = 0;
        try {
            count = fashionHalfPriceCarService.getBrandHalfpriceFlag(periodsId, brandId);
        }catch (Exception e) {
            StaticLogUtils.error(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FashionCarHalfPriceController", "场次品牌有无在活动时间内的半价车活动error", periodsId+"_"+brandId,e);
            return new TcResponse(StatusCodeEnum.SYSTEM_INNER_ERROR.getCode(), StatusCodeEnum.SYSTEM_INNER_ERROR.getText());
        }
        if (count==null ||  count==0) {
            return new TcResponse(StatusCodeEnum.RESULE_DATA_NONE.getCode(), System.currentTimeMillis() - st, "无半价车活动", count);
        }
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), System.currentTimeMillis() - st, StatusCodeEnum.SUCCESS.getText(), count);
    }

}
