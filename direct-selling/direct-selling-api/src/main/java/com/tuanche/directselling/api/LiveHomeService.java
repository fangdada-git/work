package com.tuanche.directselling.api;


import com.tuanche.directselling.dto.LiveInfoDto;
import java.util.Date;
import java.util.List;

import com.tuanche.directselling.dto.LiveDealerBroadcastDto;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.vo.LiveParameterVo;

/**
 * @class: LiveHomeService
 * @description: 直播会场接口
 * @author: dudg
 * @create: 2020-04-29 15:53
 */
public interface LiveHomeService {
    
    /**
     * 
     * getLiveBroadcastInprogress(根据城市获取正在直播的数据)
     * @param  @param cityId    设定文件
     * @return void
     * @Exception 异常对象
     * @since  CodingExample　Ver(编码范例查看) 1.1
     */
    List<LiveDealerBroadcastDto> getLiveBroadcastInprogressList(LiveDealerBroadcastDto liveDealerBroadcastDto);
    
    /**
     * 
     * getLiveBroadcastPlayback(获取直播回放的数据)
     * @param  @param periodsId
     * @param  @param cityId
     * @param  @return    设定文件
     * @return List<LiveDealerBroadcastDto>
     * @Exception 异常对象
     * @since  CodingExample　Ver(编码范例查看) 1.1
     */
	PageResult<LiveDealerBroadcastDto> getLiveBroadcastPlaybackList(PageResult<LiveDealerBroadcastDto> pageResult,
			LiveDealerBroadcastDto liveDealerBroadcastDto);
	/**
	 * 
	 * getLiveBroadcastPlayback(获取直播品牌馆数据,包括直播中和回放)
	 * @param  @param periodsId
	 * @param  @param cityId
	 * @param  @return    设定文件
	 * @return List<LiveDealerBroadcastDto>
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	PageResult<LiveDealerBroadcastDto> getLiveBroadcastForBrandList(PageResult<LiveDealerBroadcastDto> pageResult,
			LiveDealerBroadcastDto liveDealerBroadcastDto);

    /**
     * @description: 拉取实时播报数据接口   固定数据+ 直播中数据
     * @param periodsId 场次id
     * @param beginTime 场次开始时间
     * @return: java.util.List<java.lang.String> 播报文案列表
     * @author: dudg
     * @date: 2020/4/29 11:23
     */
    List<String> pullRealTimeRollNotices(Integer periodsId, Date beginTime);

    /**
     * @description: 获取指定主播ids直播中/回放数据 （如果优先直播中其次观看量最大）
     * @param parameterVo
     * @return: java.util.List<com.tuanche.directselling.dto.LiveInfoDto>
     * @author: dudg
     * @date: 2020/4/30 14:08
     */
    List<LiveInfoDto> getLivingOrPlayBackListByAnchorAndPeriods(LiveParameterVo parameterVo);

    /**
     * @description: 预热阶段获取直播列表数据（直播中：城市号&参展经销商，回放：每个参展经销商观看数最大一条）
     * @param liveDealerBroadcastDto
     * @return: java.util.List<com.tuanche.directselling.dto.LiveDealerBroadcastDto>
     * @author: dudg
     * @date: 2020/5/10 16:22
    */
    PageResult<LiveDealerBroadcastDto> getPreheatLivingAndPlayBackList(PageResult<LiveDealerBroadcastDto> pageResult,LiveDealerBroadcastDto liveDealerBroadcastDto);

	/**
	 * 【长安云车展】根据条件取团车主直播号正在直播数据
	 * @param liveParameterVo
	 * @return
	 */
	LiveDealerBroadcastDto getTcAnchorLivingBroadcast(LiveParameterVo liveParameterVo);

	/**
	 * 【长安云车展】根据条件取团车主直播号回放列表
	 * @param liveParameterVo
	 * @return
	 */
	List<LiveDealerBroadcastDto> getTcAnchorLivePlaybackList(LiveParameterVo liveParameterVo);
}
