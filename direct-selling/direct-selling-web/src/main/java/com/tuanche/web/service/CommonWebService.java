package com.tuanche.web.service;

import cn.hutool.core.codec.Base64Decoder;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tc.systemauth.interfaces.dto.UserDto;
import com.tc.systemauth.interfaces.dto.UserSearch;
import com.tc.systemauth.interfaces.service.AccountUserService;
import com.tuanche.arch.po.MemberPo;
import com.tuanche.arch.service.LoginRpcService;
import com.tuanche.arch.service.MemberService;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.dto.LiveSceneCityDto;
import com.tuanche.district.api.dto.input.LocateCityInputDto;
import com.tuanche.district.api.dto.output.DistrictOutputBaseDto;
import com.tuanche.district.api.dto.output.DistrictOutputPlusDto;
import com.tuanche.district.api.service.ILocateCityService;
import com.tuanche.framework.util.IPUtil;
import com.tuanche.manubasecenter.api.CityBaseService;
import com.tuanche.web.util.CommonLogUtil;
import com.tuanche.web.util.Globals;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: CommonWebService
 * @Description: 团车直卖公用服务
 * @Author: GongBo
 * @Date: 2020年3月4日18:36:30
 * @Version 1.0
 **/
@Service
public class CommonWebService {

    @Reference
    private AccountUserService accountUserService;
    @Value("${scene_director_role_id}")
    public String roleId;
    @Reference
    private MemberService memberService;
    @Reference
    private CityBaseService cityBaseService;
    @Reference
    private ILocateCityService iLocateCityService;
    @Reference
    private LoginRpcService loginRpcService;

    public List<UserDto> getDirectorList() {
        UserSearch userSearch = new UserSearch();
        List<Integer> roleIdList = new ArrayList<>();
        roleIdList.add(Integer.parseInt(roleId));
        userSearch.setRoleIdList(roleIdList);
        List<UserDto> userDtoList = accountUserService.userList(userSearch);
        return userDtoList;
    }

    /**
     * @param cityList
     * @return java.util.Map<java.lang.String , java.util.List < com.tuanche.directselling.dto.LiveSceneCityDto>>
     * @Author GongBo
     * @Description 根据首字母重新排列城市数据
     * @Date 12:31 2020/4/2
     **/
    public Map<String, List<LiveSceneCityDto>> groupByPinYinHeadChar(List<LiveSceneCityDto> cityList) {
        Map<String, List<LiveSceneCityDto>> result = new HashMap<>();
        Map<String, List<LiveSceneCityDto>> map = new HashMap<>();

        String[] chars = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M",
                "N", "P", "Q", "R", "S", "T", "W", "X", "Y", "Z"};
        Arrays.stream(chars).forEach(zm -> {
            List<LiveSceneCityDto> zmAndList = new ArrayList<>();
            for (int i = 0; i < cityList.size(); i++) {
                LiveSceneCityDto liveSceneCityDto = cityList.get(i);
                // 获取城市首字母
                if (zm.equals(liveSceneCityDto.getFirst())) {
                    zmAndList.add(liveSceneCityDto);
                }
            }
            if (zmAndList.size() > 0)
                map.put(zm, zmAndList);
        });
        // 根据first 重新排序
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x -> result.put(x.getKey(), x.getValue()));

        return result;
    }

    /**
     * @param cityList
     * @return java.util.Map<java.lang.String,java.util.List<com.tuanche.directselling.dto.LiveSceneCityDto>>
     * @Author GongBo
     * @Description 获取场次活动城市-lambda方式处理
     * @Date 15:17 2020/5/22
     **/
    public Map<String, List<LiveSceneCityDto>> groupByPinYinHeadCharByLambda(List<LiveSceneCityDto> cityList) {
        Map<String, List<LiveSceneCityDto>> result = new HashMap<String, List<LiveSceneCityDto>>();
        Map<String, List<LiveSceneCityDto>> map = cityList.parallelStream()
                .collect(Collectors.groupingBy(LiveSceneCityDto::getFirst));
        result = sortMapByKey(map);
        return result;
    }

    /**
      * @description : 根据用户手机号获取用户中心id
      * @author : fxq
      * @param :
      * @return :
      * @date : 2021/7/23 15:39
      */
    public Integer getUserIdByPhone (String phone) {
        if (!StringUtil.isEmpty(phone)) {
            MemberPo m = new MemberPo();
            m.setPhone(Long.valueOf(phone));
            MemberPo memberPo = memberService.queryMemberByParams(m);
            StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "CommonWebService", "getUserIdByPhone", "根据用户手机号获取用户中心id  查询用户", JSON.toJSONString(memberPo));
            if (memberPo==null || memberPo.getId()==null) {
                //添加新用户
                JSONObject jsonObject = memberService.addNotVerifiedMember(phone);
                StaticLogUtils.info(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "CommonWebService", "getUserIdByPhone", "根据用户手机号获取用户中心id  添加用户", JSON.toJSONString(jsonObject));
                if (jsonObject!=null) {
                    int code = (int) jsonObject.get("code");
                    if (code==200) {
                        Map<String, String> result = (Map<String, String>) jsonObject.get("result");
                        return Integer.valueOf(result.get("id"));
                    }
                }
            } else {
                return memberPo.getId();
            }
        }
        return null;
    }

    public MemberPo getMemberPoByToken (HttpServletRequest request) {
        MemberPo memberPo = new MemberPo();
        String token = request.getParameter("token");
        CommonLogUtil.addInfo("getMemberPoByToken","根据token获取用户信息，请求参数",token);
        if (!StringUtil.isEmpty(token)) {
            String s = Base64Decoder.decodeStr(token);
            memberPo = loginRpcService.queryCacheUserInfoByUToken(s);
            CommonLogUtil.addInfo("getMemberPoByToken","根据token获取用户信息，请求结果",JSON.toJSONString(memberPo));
        }
        return memberPo ==null ? new MemberPo() : memberPo;
    }
    public Integer getMemberPoIdByToken (HttpServletRequest request) {
        MemberPo memberPo = getMemberPoByToken(request);
        return memberPo.getId();
    }

    private Map<String, List<LiveSceneCityDto>> sortMapByKey(Map<String, List<LiveSceneCityDto>> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, List<LiveSceneCityDto>> sortMap = new TreeMap<String, List<LiveSceneCityDto>>(
                new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

    /**
      * @description :
      * @author : fxq
      * @param : cityName 城市名称
      * @param : longitude 经度值
      * @param : latitude 纬度值
      * @return :
      * @date : 2021/9/14 11:33
      */
    public Integer getCityIdByNameOrLongitudeLatitudeOrIp (HttpServletRequest request, String cityName, String latitude, String longitude) {
        //根据城市名称定位
        if (!StringUtil.isEmpty(cityName)) {
            DistrictOutputBaseDto city = cityBaseService.getCityByName(cityName);
            if (city!=null && city.getId()!=null) return city.getId();
        }
        //根据经纬度定位
        if (!StringUtil.isEmpty(latitude) && !StringUtil.isEmpty(longitude)) {
            try {
                LocateCityInputDto locateCityInputDto = new LocateCityInputDto();
                locateCityInputDto.setLatitude(latitude);
                locateCityInputDto.setLongitude(longitude);
                CommonLogUtil.addInfo("getCityIdByNameOrLongitudeLatitudeOrIp","调用城市定位服务-经纬度，请求参数",locateCityInputDto);
                DistrictOutputPlusDto locationCity = iLocateCityService.findCity(locateCityInputDto);
                CommonLogUtil.addInfo("getCityIdByNameOrLongitudeLatitudeOrIp","调用城市定位服务-经纬度，返回结果",locationCity);
                if (locationCity != null && locationCity.getId() != null) {
                    return locationCity.getId();
                }
            }catch (Exception e) {
                CommonLogUtil.addError("getCityIdByNameOrLongitudeLatitudeOrIp","调用城市定位服务-经纬度，error",e);
            }
        }
        //根据IP定位
        try {
            LocateCityInputDto locateCityInputDto = new LocateCityInputDto();
            locateCityInputDto.setIp(IPUtil.getRequestIp(request));
            CommonLogUtil.addInfo("getCityIdByNameOrLongitudeLatitudeOrIp","调用城市定位服务-IP，请求参数",locateCityInputDto);
            DistrictOutputPlusDto locationCity = iLocateCityService.findCity(locateCityInputDto);
            CommonLogUtil.addInfo("getCityIdByNameOrLongitudeLatitudeOrIp","调用城市定位服务-IP，返回结果",locationCity);
            if (locationCity != null && locationCity.getId() != null) {
                return locationCity.getId();
            }
        }catch (Exception e) {
            CommonLogUtil.addError("getCityIdByNameOrLongitudeLatitudeOrIp","调用城市定位服务-IP，error",e);
        }
        return null;
    }


}

class MapKeyComparator implements Comparator<String> {
    @Override
    public int compare(String str1, String str2) {
        return str1.compareTo(str2);
    }
}
