package com.tuanche.directselling.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.consol.dubbo.ActivityConfigService;
import com.tuanche.consol.dubbo.enums.MethodEnum;
import com.tuanche.consol.dubbo.enums.SourcesEnum;
import com.tuanche.consol.dubbo.enums.VersionEnum;
import com.tuanche.consol.dubbo.vo.activityConfig.ActivityConfigRequestVo;
import com.tuanche.consol.dubbo.vo.activityConfig.ActivityConfigResponseVo;
import com.tuanche.consol.dubbo.vo.base.ConsoleServiceParamVo;
import org.springframework.stereotype.Service;

/**
 * @author：HuangHao
 * @CreatTime 2020-06-02 15:08
 */
@Service
public class PeriodsService {
    @Reference
    ActivityConfigService activityConfigService;

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
}
