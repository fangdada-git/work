package com.tuanche.directselling.api;

import com.tuanche.directselling.dto.LiveDealerPlaybackDto;
import com.tuanche.directselling.model.LiveDealerAnchor;
import com.tuanche.directselling.model.LiveDealerPlayback;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.vo.LiveDealerAnchorVo;
import com.tuanche.district.api.dto.output.DistrictOutputBaseDto;

import java.util.List;

/**
 * @program: direct-selling
 * @description: ${description}
 * @author: fxq
 * @create: 2020-03-27 13:02
 **/
public interface LiveDealerAnchorService {

    List<LiveDealerAnchor> LiveDealerAnchorRanking(LiveDealerAnchorVo liveDealerAnchorVo);

    int insertLoginCookie(String cookieStr);

    int saveLiveDealerAnchor(LiveDealerAnchor liveDealerAnchor);

    int updateLiveDealerAnchor(LiveDealerAnchor liveDealerAnchor);

    List<LiveDealerAnchor> getAnchorLiveDataByCityId(LiveDealerAnchorVo liveDealerAnchorVo);

    List<DistrictOutputBaseDto> getAnchorCityList();

    /**
     * @param dealerId
     * @return com.tuanche.directselling.model.LiveDealerAnchor
     * @Author GongBo
     * @Description 根据经销商ID 获取直播信息
     * @Date 13:29 2020/4/29
     **/
    LiveDealerAnchor getLiveDealerAnchorByDealerId(Integer dealerId);

    LiveDealerAnchor getLiveDealerAnchorByInfo(LiveDealerAnchor liveDealerAnchor);

    /**
     * @param dealerId
     * @return java.util.List<com.tuanche.directselling.model.LiveDealerPlayback>
     * @Author GongBo
     * @Description 根据经销商ID  获取直播回放
     * @Date 13:32 2020/4/29
     **/
    List<LiveDealerPlaybackDto> getLiveDealerPlayback(Integer dealerId);

    /**
     * @param liveDealerPlayback
     * @return int
     * @Author GongBo
     * @Description 新增经销商直播回放
     * @Date 13:37 2020/4/29
     **/
    int saveLiveDealerPlayback(LiveDealerPlayback liveDealerPlayback);

    /**
     * @param liveDealerAnchor
     * @return: List<LiveDealerAnchor>
     * @description:
     * @author: czy
     * @create: 2020/4/30 16:03
     **/
    PageResult findAnchorList(PageResult<LiveDealerAnchor> pageResult, LiveDealerAnchor liveDealerAnchor);

    /**
     * @param pageResult
     * @param liveDealerAnchor
     * @return com.tuanche.directselling.utils.PageResult
     * @Author GongBo
     * @Description 账号活动列表
     * @Date 11:17 2020/6/11
     **/
    PageResult findAnchorSceneList(PageResult<LiveDealerAnchor> pageResult, LiveDealerAnchor liveDealerAnchor);

    List<LiveDealerAnchor> findAnchorListAll(LiveDealerAnchor liveDealerAnchor);

    /**
     * @param liveDealerAnchor
     * @return java.util.List<com.tuanche.directselling.model.LiveDealerAnchor>
     * @Author GongBo
     * @Description 查询账号活动集合
     * @Date 14:50 2020/6/12
     **/
    List<LiveDealerAnchor> findAnchorSceneListAll(LiveDealerAnchor liveDealerAnchor);

    /**
     * @param dealerId,activityId
     * @return: LiveDealerPlaybackDto
     * @description: 获取直播片段信息
     * @author: czy
     * @create: 2020/5/6 13:31
     **/
    LiveDealerPlaybackDto getInfoByDealerIdAndActivityId(Integer dealerId, Integer activityId);

    /**
     * @param liveDealerPlayback
     * @return:
     * @description: 更新直播片段信息
     * @author: czy
     * @create: 2020/5/6 15:28
     **/
    int updateLiveDealerPlayback(LiveDealerPlayback liveDealerPlayback);

    LiveDealerAnchor getLiveDealerAnchorById(Integer id);

    /**
     * @param liveDealerAnchor
     * @return: 账号操作
     * @description:
     * @author: czy
     * @create: 2020/5/25 14:12
     **/
    int saveAndUpdateLiveDealerAnchor(LiveDealerAnchor liveDealerAnchor);

    void updateByPrimaryKeySelective (LiveDealerAnchor liveDealerAnchor);

    /***
     * @description: 主播ids转化团车经销商ids
     * @param anchorIds
     * @return: java.util.List<java.lang.Integer>
     * @author: dudg
     * @date: 2020/6/13 10:40
     */
    List<Integer> transformDealerIds(List<Long> anchorIds);
}
