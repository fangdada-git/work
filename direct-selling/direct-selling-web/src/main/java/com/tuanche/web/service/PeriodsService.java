package com.tuanche.web.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.tuanche.consol.dubbo.ActivityConfigService;
import com.tuanche.consol.dubbo.enums.MethodEnum;
import com.tuanche.consol.dubbo.enums.ResultCodeEnum;
import com.tuanche.consol.dubbo.enums.SourcesEnum;
import com.tuanche.consol.dubbo.enums.VersionEnum;
import com.tuanche.consol.dubbo.enums.activityConfig.ActivityConfigFormEnum;
import com.tuanche.consol.dubbo.enums.activityConfig.ActivityConfigStatusEnum;
import com.tuanche.consol.dubbo.enums.activityConfig.ActivityConfigTypeEnum;
import com.tuanche.consol.dubbo.vo.activityConfig.ActivityConfigRequestVo;
import com.tuanche.consol.dubbo.vo.activityConfig.ActivityConfigResponseVo;
import com.tuanche.consol.dubbo.vo.base.ConsoleServiceParamVo;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: PeriodsService
 * @Description: C端场次服务
 * @Author: GongBo
 * @Date: 2020/4/17 12:09
 * @Version 1.0
 **/
@Service
public class PeriodsService {

    @Reference
    ActivityConfigService activityConfigService;

    /**
     * @param pageResult
     * @param activityConfigRequestVo
     * @return com.tuanche.directselling.utils.PageResult
     * @Author GongBo
     * @Description 查询场次列表
     * @Date 12:17 2020/4/17
     **/
    public PageResult queryList(PageResult<ActivityConfigResponseVo> pageResult, ActivityConfigRequestVo activityConfigRequestVo) {
        activityConfigRequestVo.setDeleteTag(0);
        activityConfigRequestVo.setAllFlagType(1);
        ConsoleServiceParamVo<ActivityConfigRequestVo, ActivityConfigResponseVo> consoleServiceParamVo =
                getBuild(activityConfigRequestVo, SourcesEnum.PC, MethodEnum.ACTIVITY_CONFIG_QUERY);

        ConsoleServiceParamVo responseVo = activityConfigService.service(consoleServiceParamVo);

        List<ActivityConfigResponseVo> responseDataList = responseVo.getResponseDataList();
        PageInfo<ActivityConfigResponseVo> page = new PageInfo<ActivityConfigResponseVo>(responseDataList);
        pageResult.setCount(page.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
        pageResult.setMsg("");
        pageResult.setData(responseDataList);
        return pageResult;
    }

    /**
     * @param activityConfigRequestVo
     * @return com.tuanche.directselling.utils.PageResult
     * @Author GongBo
     * @Description 查询场次详情
     * @Date 12:17 2020/4/17
     **/
    public ActivityConfigResponseVo queryById(ActivityConfigRequestVo activityConfigRequestVo) {

        // 调用根据id查询活动的方法
        ConsoleServiceParamVo<ActivityConfigRequestVo, ActivityConfigResponseVo> consoleServiceParamVo =
                getBuild(activityConfigRequestVo, SourcesEnum.PC, MethodEnum.ACTIVITY_CONFIG_QUERY_BY_ID);
        ConsoleServiceParamVo responseVo = activityConfigService.service(consoleServiceParamVo);

        ActivityConfigResponseVo responseData = (ActivityConfigResponseVo) responseVo.getResponseData();
        return responseData;
    }

    /**
     * @param activityConfigRequestVo
     * @return com.tuanche.directselling.utils.PageResult
     * @Author GongBo
     * @Description 添加场次
     * @Date 12:17 2020/4/17
     **/
    public Integer save(ActivityConfigRequestVo activityConfigRequestVo) {

        // 调用创建活动的方法
        // activityConfigRequestVo 相当于业务参数对象
        ConsoleServiceParamVo<ActivityConfigRequestVo, ActivityConfigResponseVo> consoleServiceParamVo =
                getBuild(activityConfigRequestVo, SourcesEnum.PC, MethodEnum.ACTIVITY_CONFIG_ADD);
        ConsoleServiceParamVo responseVo = activityConfigService.service(consoleServiceParamVo);

        if(responseVo.getResultCodeEnum().hashCode() ==
                ResultCodeEnum.OK.hashCode()){
            ActivityConfigResponseVo responseData = (ActivityConfigResponseVo) responseVo.getResponseData();
            return responseData.getId();
        }else{
            return responseVo.getResultCodeEnum().hashCode();
        }
    }

    /**
     * @param activityConfigRequestVo
     * @return com.tuanche.directselling.utils.PageResult
     * @Author GongBo
     * @Description 编辑场次
     * @Date 12:17 2020/4/17
     **/
    public Integer edit(ActivityConfigRequestVo activityConfigRequestVo) {

        // 调用编辑活动的方法
        // activityConfigRequestVo 相当于业务参数对象
        ConsoleServiceParamVo<ActivityConfigRequestVo, ActivityConfigResponseVo> consoleServiceParamVo =
                getBuild(activityConfigRequestVo, SourcesEnum.PC, MethodEnum.ACTIIVTY_CONFIG_EDIT);
        ConsoleServiceParamVo responseVo = activityConfigService.service(consoleServiceParamVo);

        if(responseVo.getResultCodeEnum().hashCode() ==
                ResultCodeEnum.OK.hashCode()){
            ActivityConfigResponseVo responseData = (ActivityConfigResponseVo) responseVo.getResponseData();
            return responseData.getId();
        }else{
            return responseVo.getResultCodeEnum().hashCode();
        }

    }

    /**
     * @param activityConfigRequestVo
     * @return com.tuanche.directselling.utils.PageResult
     * @Author GongBo
     * @Description 查询场次集合(未开始和进行中)
     * @Date 12:17 2020/4/17
     **/
    public List<ActivityConfigResponseVo> getValidActivityList(ActivityConfigRequestVo activityConfigRequestVo) {
        activityConfigRequestVo.setDeleteTag(0);
        activityConfigRequestVo.setActivityStatusEnum(ActivityConfigStatusEnum.UN_END);
        activityConfigRequestVo.setActivityFormEnum(ActivityConfigFormEnum.ONLINE);
        activityConfigRequestVo.setActivityTypeEnum(ActivityConfigTypeEnum.ONLINE_ACTIVITY);
        ConsoleServiceParamVo<ActivityConfigRequestVo, ActivityConfigResponseVo> consoleServiceParamVo =
                getBuild(activityConfigRequestVo, SourcesEnum.PC, MethodEnum.ACTIVITY_CONFIG_QUERY_BY_STATUS);
        ConsoleServiceParamVo responseVo = activityConfigService.service(consoleServiceParamVo);
        List<ActivityConfigResponseVo> responseDataList = responseVo.getResponseDataList();
        return responseDataList;
    }

    /**
     * @param activityConfigRequestVo
     * @return com.tuanche.directselling.utils.PageResult
     * @Author GongBo
     * @Description 查询场次集合
     * @Date 12:17 2020/4/17
     **/
    public List<ActivityConfigResponseVo> getActivityList(ActivityConfigRequestVo activityConfigRequestVo) {
        activityConfigRequestVo.setDeleteTag(0);
        return getActivityListAll(activityConfigRequestVo);
    }

    public List<ActivityConfigResponseVo> getActivityListAll(ActivityConfigRequestVo activityConfigRequestVo) {
        ConsoleServiceParamVo<ActivityConfigRequestVo, ActivityConfigResponseVo> consoleServiceParamVo =
                getBuild(activityConfigRequestVo, SourcesEnum.PC, MethodEnum.ACTIVITY_CONFIG_QUERY);

        ConsoleServiceParamVo responseVo = activityConfigService.service(consoleServiceParamVo);

        List<ActivityConfigResponseVo> responseDataList = responseVo.getResponseDataList();
        return responseDataList;
    }
    /**
     * 查询场次集合 - 分页
     * @author HuangHao
     * @CreatTime 2020-06-12 16:08
     * @param activityConfigRequestVo
     * @return java.util.List<com.tuanche.consol.dubbo.vo.activityConfig.ActivityConfigResponseVo>
     */
    public List<ActivityConfigResponseVo> getActivityListByPage(ActivityConfigRequestVo activityConfigRequestVo) {
        activityConfigRequestVo.setDeleteTag(0);
        ConsoleServiceParamVo<ActivityConfigRequestVo, ActivityConfigResponseVo> consoleServiceParamVo =
                getBuild(activityConfigRequestVo, SourcesEnum.PC, MethodEnum.ACTIVITY_CONFIG_QUERY);

        ConsoleServiceParamVo responseVo = activityConfigService.service(consoleServiceParamVo);

        PageInfo<ActivityConfigResponseVo> pageInfo = (PageInfo<ActivityConfigResponseVo>) responseVo.getResponseData();

        return pageInfo.getList();
    }

    public Map<Integer, ActivityConfigResponseVo> getActivityMap (List<Integer> PeriodsIds) {
        Map<Integer, ActivityConfigResponseVo> map = new HashMap<>();
        ActivityConfigRequestVo requestVo = new ActivityConfigRequestVo();
        requestVo.setIds(PeriodsIds);
        List<ActivityConfigResponseVo> list = getActivityListAll(requestVo);
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(v-> {
                map.put(v.getId(),v);
            });
        }
        return map;
    }

    /**
     * @description: 查询当前场次信息
     * @return: void
     * @author: dudg
     * @date: 2020/5/6 16:09
    */
    public ActivityConfigResponseVo getCurrentActivityInfo(){
        ActivityConfigRequestVo activityConfigRequestVo = new ActivityConfigRequestVo();
        activityConfigRequestVo.setActivityFormEnum(ActivityConfigFormEnum.ONLINE);
        activityConfigRequestVo.setActivityTypeEnum(ActivityConfigTypeEnum.ONLINE_ACTIVITY);
        activityConfigRequestVo.setDeleteTag(0);
        ConsoleServiceParamVo<ActivityConfigRequestVo, ActivityConfigResponseVo> consoleServiceParamVo =
                getBuild(activityConfigRequestVo, SourcesEnum.H5, MethodEnum.ACTIVITY_CONFIG_QUERY_CURRENT_INFO);
        ConsoleServiceParamVo responseVo = activityConfigService.service(consoleServiceParamVo);

        if(ResultCodeEnum.OK == responseVo.getResultCodeEnum()){
            ActivityConfigResponseVo responseData = (ActivityConfigResponseVo) responseVo.getResponseData();
            return responseData;
        }
        return null;
    }

    /**
     * @param activityConfigRequestVo
     * @param sourcesEnum
     * @param actiivtyConfigEdit
     * @return com.tuanche.consol.dubbo.vo.base.ConsoleServiceParamVo
     * @Author GongBo
     * @Description 封装场次业务参数
     * @Date 12:27 2020/4/17
     **/
    private ConsoleServiceParamVo getBuild(ActivityConfigRequestVo activityConfigRequestVo, SourcesEnum sourcesEnum, MethodEnum actiivtyConfigEdit) {
        return ConsoleServiceParamVo.builder()
                .sources(sourcesEnum)
                .method(actiivtyConfigEdit)
                .version(VersionEnum.v1_0)
                .requestData(activityConfigRequestVo)
                .build();
    }

    public List<ActivityConfigResponseVo> getActivityConfigList (List<Integer> periodsIdList, Integer deleteFlag) {
        List<ActivityConfigResponseVo> list = new ArrayList<>();
        ActivityConfigRequestVo activityConfigRequestVo = new ActivityConfigRequestVo();
        activityConfigRequestVo.setDeleteTag(deleteFlag);
        activityConfigRequestVo.setIds(periodsIdList);
        // 调用删除活动的方法
        // activityConfigRequestVo 相当于业务参数对象
        ConsoleServiceParamVo<ActivityConfigRequestVo, ActivityConfigResponseVo> consoleServiceParamVo = ConsoleServiceParamVo.builder()
                .sources(SourcesEnum.PC)
                .method(MethodEnum.ACTIVITY_CONFIG_QUERY)
                .version(VersionEnum.v1_0)
                .requestData(activityConfigRequestVo)
                .build();
        ConsoleServiceParamVo responseVo = activityConfigService.service(consoleServiceParamVo);
        if (CollectionUtil.isNotEmpty(responseVo.getResponseDataList())) {
            list = responseVo.getResponseDataList();
        }
        return list;
    }
}
