package com.tuanche.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.district.api.dto.input.DistrictBaseInputDto;
import com.tuanche.district.api.dto.input.KeyVaueParam;
import com.tuanche.district.api.dto.output.DistrictOutputBaseDto;
import com.tuanche.district.api.service.IDistrictApiService;
import com.tuanche.inner.sso.core.conf.Conf;
import com.tuanche.inner.sso.core.user.XxlUser;
import com.tuanche.manubasecenter.constant.ManuBaseConstants;
import com.tuanche.web.util.CommonLogUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @class: BaseController
 * @description: 父类控制器
 * @author: dudg
 * @create: 2019-10-26 14:45
 */
public class BaseController {

    @Reference
    IDistrictApiService iDistrictApiService;

    /**
     * @param request
     * @description: 获取海纳登录用户信息
     * @return: com.tuanche.inner.sso.core.user.XxlUser
     * @author: dudg
     * @date: 2019/11/22 14:34
     */
    public XxlUser getLoginUser(HttpServletRequest request) {
        XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
        return xxlUser;
    }

    /**
     * 成功
     *
     * @return
     */
    public ResultDto success() {
        return dynamicResult(StatusCodeEnum.SUCCESS, null);
    }

    /**
     * 成功
     *
     * @param data
     * @return
     */
    public ResultDto success(Object data) {
        return dynamicResult(StatusCodeEnum.SUCCESS, data);
    }

    /**
     * 无数据返回
     *
     * @return
     */
    public ResultDto noData() {
        return dynamicResult(StatusCodeEnum.RESULE_DATA_NONE, null);
    }

    /**
     * 参数为空
     *
     * @return
     */
    public ResultDto paramBlank() {
        return dynamicResult(StatusCodeEnum.PARAM_IS_BLANK, null);
    }

    /**
     * 参数无效
     *
     * @return
     */
    public ResultDto paramInvalid() {
        return dynamicResult(StatusCodeEnum.PARAM_IS_INVALID, null);
    }

    /**
     * 系统异常
     *
     * @return
     */
    public ResultDto systemError() {
        return dynamicResult(StatusCodeEnum.SYSTEM_INNER_ERROR, null);
    }

    public ResultDto loginError() {
        return dynamicResult(StatusCodeEnum.USER_NOT_LOGGED_IN, null);
    }

    public ResultDto programTimeError() {
        return dynamicResult(StatusCodeEnum.PROGRAM_TIME_ERROR, null);
    }


    /**
     * @description: 动态创建返回实体
     * @return: com.tuanche.commons.util.ResultDto
     * @author: dudg
     * @date: 2019/12/2 11:20
     */
    public ResultDto dynamicResult(StatusCodeEnum statusCodeEnum, Object data) {
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(statusCodeEnum.getCode());
        resultDto.setMsg(statusCodeEnum.getText());
        if (data != null) {
            resultDto.setResult(data);
        }
        return resultDto;
    }

    /**
     * @param code
     * @param message
     * @return com.tuanche.commons.util.ResultDto
     * @Author GongBo
     * @Description 自定义业务错误  提示信息返回
     * @Date 12:11 2020/3/6
     **/
    public ResultDto dynamicResult(Integer code, String message) {
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(code);
        resultDto.setMsg(message);
        return resultDto;
    }

    /**
     * @param message
     * @return com.tuanche.commons.util.ResultDto
     * @Author GongBo
     * @Description 自定义业务错误  提示信息返回
     * @Date 2020年5月29日10:05:45
     **/
    public ResultDto dynamicResult(String message) {
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(StatusCodeEnum.CREATE_FAIL.getCode());
        resultDto.setMsg(message);
        return resultDto;
    }

    /**
     * @param
     * @description: 返回所有（三、四级）城市
     * @return: java.util.List<com.tuanche.district.api.dto.output.DistrictOutputBaseDto>
     * @author: dudg
     * @date: 2019/11/22 14:32
     */
    public List<DistrictOutputBaseDto> getAllCity() {
        List<DistrictOutputBaseDto> districtOutputBaseDtosList = new ArrayList<DistrictOutputBaseDto>();
        try {
            DistrictBaseInputDto districtBaseInputDto = new DistrictBaseInputDto();
            List<Integer> levellist = new ArrayList<Integer>();
            levellist.add(ManuBaseConstants.CityLevel.THREE);
            levellist.add(ManuBaseConstants.CityLevel.FOUR);
            List<KeyVaueParam> ordersList = new ArrayList<KeyVaueParam>();
            KeyVaueParam keyVaueParam = new KeyVaueParam();
            keyVaueParam.setKey("first");
            keyVaueParam.setValue("asc");
            ordersList.add(keyVaueParam);
            districtBaseInputDto.setLevel(levellist);
            districtBaseInputDto.setOrders(ordersList);
            districtOutputBaseDtosList = iDistrictApiService.getBaseDistrictList(districtBaseInputDto);
        } catch (Exception e) {
            e.printStackTrace();
            CommonLogUtil.addError("获取所有(3、4级)城市列表", "发生错误" + e.getMessage(), e);
        }
        return districtOutputBaseDtosList;
    }

    /**
     * 获取3，4级城市名拼接数据 例:[{"id":1,"name":"北京"},{"id":2,"name":"北京,丰台"}]
     *
     * @return
     */
    protected List<Map<String, Object>> getAllCityList() {
        DistrictBaseInputDto districtBaseInputDto = new DistrictBaseInputDto();
        List<Map<String, Object>> result = new ArrayList<>();
        List<Integer> levelList = new ArrayList<>();
        levelList.add(ManuBaseConstants.CityLevel.THREE);
        levelList.add(ManuBaseConstants.CityLevel.FOUR);
        districtBaseInputDto.setLevel(levelList);
        List<DistrictOutputBaseDto> districtOutputBaseDtosList = iDistrictApiService.getBaseDistrictList(districtBaseInputDto);
        List<DistrictOutputBaseDto> level3 = new ArrayList<>();
        List<DistrictOutputBaseDto> level4 = new ArrayList<>();
        Map<Integer, List<DistrictOutputBaseDto>> level4Map = new HashMap<>();
        for (DistrictOutputBaseDto dist : districtOutputBaseDtosList) {
            if (ManuBaseConstants.CityLevel.THREE.equals(dist.getLevel())) {
                level3.add(dist);
            } else if (ManuBaseConstants.CityLevel.FOUR.equals(dist.getLevel())) {
                level4.add(dist);
                if (level4Map.get(dist.getPid()) == null) {
                    level4Map.put(dist.getPid(), new ArrayList<>());
                }
                level4Map.get(dist.getPid()).add(dist);
            }
        }
        Map<String, Object> distIdName = null;
        for (DistrictOutputBaseDto dist : level3) {
            distIdName = new HashMap<>(8);
            distIdName.put("id", dist.getId());
            distIdName.put("name", dist.getName());
            result.add(distIdName);
            List<DistrictOutputBaseDto> level4Dist = level4Map.get(dist.getId());
            if (level4Dist != null) {
                result.addAll(convertLevel4Dist(dist.getName(), level4Dist));
            }
        }
        return result;
    }

    /**
     * 处理4级城市
     *
     * @return
     */
    private List<Map<String, Object>> convertLevel4Dist(String parentName, List<DistrictOutputBaseDto> level4Dist) {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> distIdName = null;
        for (DistrictOutputBaseDto dist : level4Dist) {
            distIdName = new HashMap<>(8);
            distIdName.put("id", dist.getId());
            distIdName.put("name", parentName + "," + dist.getName());
            result.add(distIdName);
        }
        return result;
    }

}
