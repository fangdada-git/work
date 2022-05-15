package com.tuanche.directselling.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.district.api.dto.input.DistrictExpandInputDto;
import com.tuanche.district.api.dto.input.DistrictPlusInputDto;
import com.tuanche.district.api.dto.output.DistrictOutputPlusDto;
import com.tuanche.district.api.service.IDistrictApiService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2020-06-03 14:49
 */
@Service
public class CommonService {

    @Reference
    IDistrictApiService districtApiService;

    /**
     * 获取城市
     *
     * @param
     * @return String
     * @author liuwenlei
     * @CreatTime 2019-03-12 19:28
     */
    private DistrictOutputPlusDto getCityById(Integer cityId) {
        try {
            DistrictExpandInputDto param = new DistrictExpandInputDto();
            List<Integer> ids = Arrays.asList(cityId);
            param.setIds(ids);
            DistrictPlusInputDto arg0 = new DistrictPlusInputDto();
            arg0.setIds(ids);
            List<DistrictOutputPlusDto> res = districtApiService.getPlusDistrictList(arg0);

            if (res != null && res.size()>0) {
                return res.get(0);
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取城市名称
     * @author HuangHao
     * @CreatTime 2020-06-03 14:53
     * @param cityId
     * @return java.lang.String
     */
    public String getCityName(Integer cityId){
        DistrictOutputPlusDto city = getCityById(cityId);
        if(city != null){
            return city.getName();
        }else{
            return null;
        }
    }

}
