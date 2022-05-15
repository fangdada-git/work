package com.tuanche.directselling.api;

import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.directselling.dto.LiveSceneDealerConfigDto;
import com.tuanche.directselling.model.LiveSceneDealerConfig;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.vo.LiveSceneDealerConfigVo;

import java.util.List;

/**
 * @Author GongBo
 * @Description 团车直卖-场次活动经销商配置service
 * @Date 17:01 2020/3/4
 **/
public interface LiveSceneDealerConfigService {

    /**
     * @param id
     * @return com.tuanche.directselling.model.LiveSceneDealerConfig
     * @Author GongBo
     * @Description 团车直卖-获取场次活动经销商配置信息
     * @Date 10:15 2020/4/28
     **/
    LiveSceneDealerConfig getLiveSceneDealerConfigById(Integer id);

    /**
     * @Author GongBo
     * @Description 团车直卖-获取场次活动经销商配置信息
     * @Date 17:23 2020/5/12
     * @param liveSceneDealerConfig
     * @return com.tuanche.directselling.model.LiveSceneDealerConfig
     **/
    LiveSceneDealerConfig getLiveSceneDealerConfigByVo(LiveSceneDealerConfig liveSceneDealerConfig);

    int selectByLiveSceneDealerConfigCount(LiveSceneDealerConfig liveSceneDealerConfig);

    /**
     * @param liveSceneDealerConfig
     * @return void
     * @Author GongBo
     * @Description 团车直卖-保添加场次活动经销商配置
     * @Date 10:16 2020/4/28
     **/
    void insertByLiveSceneDealerConfig(LiveSceneDealerConfig liveSceneDealerConfig) throws Exception;

    /**
     * @param liveSceneDealerConfig
     * @return void
     * @Author GongBo
     * @Description 团车直卖-修改场次活动经销商配置
     * @Date 10:16 2020/4/28
     **/
    void updateByLiveSceneDealerConfig(LiveSceneDealerConfig liveSceneDealerConfig);

    void deleteByLiveSceneDealerConfig(LiveSceneDealerConfig liveSceneDealerConfig);
    /**
     * 获取场次活动经销商配置列表
     * @author HuangHao
     * @CreatTime 2020-05-29 15:57
     * @param liveSceneDealerConfig
     * @return java.util.List<com.tuanche.directselling.model.LiveSceneDealerConfig>
     */
    List<LiveSceneDealerConfigDto> getLiveSceneDealerConfigList(LiveSceneDealerConfigVo liveSceneDealerConfig);

    /**
     * 根据ID批量更新经销商赠送油卡数量
     * @author HuangHao
     * @CreatTime 2020-05-29 16:39
     * @param refuelingCardNum  赠送油卡数
     * @param ids   更新对象的ID
     * @param updateUserName   操作人
     * @return int
     */
    TcResponse batchUpdate(Integer refuelingCardNum, List<Integer> ids,String updateUserName);

    /**
     * 经销商油卡统计-不分页
     * @author HuangHao
     * @CreatTime 2020-06-02 11:32
     * @param liveSceneDealerConfig
     * @return java.util.List<com.tuanche.directselling.dto.LiveSceneDealerConfigDto>
     */
    List<LiveSceneDealerConfigDto> getDealerRefuelCardStatistics(LiveSceneDealerConfigVo liveSceneDealerConfig);

    /**
     * 经销商油卡统计-分页
     * @author HuangHao
     * @CreatTime 2020-06-02 11:32
     * @param liveSceneDealerConfig
     * @return java.util.List<com.tuanche.directselling.dto.LiveSceneDealerConfigDto>
     */
    PageResult<LiveSceneDealerConfigDto> getDealerRefuelCardStatisticsByPage(LiveSceneDealerConfigVo liveSceneDealerConfig);

    /**
     * 获取经销商参加过直播活动的大场次
     * @author HuangHao
     * @CreatTime 2020-06-02 18:10
     * @param dealerId
     * @return java.util.List<com.tuanche.directselling.model.LiveSceneDealerConfig>
     */
    List<LiveSceneDealerConfigDto> getDealerPeriods(Integer dealerId);

    /**
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-场次活动经销商数据查询
     * @Date 2020年5月21日11:41:20
     **/
    PageResult findSceneDealerConfigList(PageResult<LiveSceneDealerConfigDto> pageResult, Integer sceneId);
}
