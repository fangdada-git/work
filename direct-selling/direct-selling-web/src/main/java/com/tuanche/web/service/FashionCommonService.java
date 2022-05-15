package com.tuanche.web.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.consol.dubbo.OnlineFestivalFacadeService;
import com.tuanche.consol.dubbo.bean.ConsolParametersVo;
import com.tuanche.consol.dubbo.enums.OnlineFestivalMethodEnum;
import com.tuanche.consol.dubbo.vo.carFashion.CarFashionActivityInfoReqVo;
import com.tuanche.consol.dubbo.vo.carFashion.CarFashionInfoEntityResVo;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FashionCommonService {

    @Reference
    private OnlineFestivalFacadeService onlineFestivalFacadeService;

    public CarFashionInfoEntityResVo getCarFashionInfoEntityResVo(Integer periodsId) {
        ConsolParametersVo vo = new ConsolParametersVo(OnlineFestivalMethodEnum.API_QUERY_ACTIVITY_INFO);
        vo.setBusinessType(ConsolParametersVo.BUSINESS_TYPE.API_QUERY_ACTIVITY_INFO);
        CarFashionActivityInfoReqVo carFashionActivityInfoReqVo = new CarFashionActivityInfoReqVo();
        carFashionActivityInfoReqVo.setActivityId(periodsId);
        vo.setInfo("param", JSONObject.toJSONString(carFashionActivityInfoReqVo));
        ConsolParametersVo service = onlineFestivalFacadeService.service(vo);
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FashionCommonService","getCarFashionInfoEntityResVo 潮车集调用C端入参"
                 + JSON.toJSONString(vo),  "潮车集调用C端返回 " , JSON.toJSONString(service));
        CarFashionInfoEntityResVo carFashionInfoEntityResVo = null;
        if ("0000".equals(service.getResCode())) {
            Map<String, Object> serviceMap = service.getMap();
            if (serviceMap != null) {
                String data = (String) serviceMap.get("data");
                if (data != null) {
                    carFashionInfoEntityResVo = JSONObject.parseObject(data, CarFashionInfoEntityResVo.class);
                }
            }
        }
        return carFashionInfoEntityResVo;
    }

}
