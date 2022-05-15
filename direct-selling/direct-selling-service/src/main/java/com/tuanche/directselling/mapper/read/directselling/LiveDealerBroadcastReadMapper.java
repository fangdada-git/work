package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.LiveDealerBroadcastDto;
import com.tuanche.directselling.dto.LiveInfoDto;
import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.LiveDealerBroadcast;
import com.tuanche.directselling.vo.LiveParameterVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LiveDealerBroadcastReadMapper extends MyBatisBaseDao<LiveDealerBroadcast, Integer> {

    /**
     * @description: 最新直播间
     * @param:
     * @return:
     * @author: fxq
     * @date: 2020/4/7 18:56
     */
    List<LiveDealerBroadcast> getLiveDealerBroadByAnchorIds(@Param("anchorIds") List<Long> anchorIds);

    /**
     * @description: 昨天观看数最大直播间
     * @param:
     * @return:
     * @author: fxq
     * @date: 2020/4/7 18:55
     */
    List<LiveDealerBroadcast> getLiveDealerBroadViewByAnchorIds(@Param("anchorIds") List<Long> anchorIds);

    /**
     * 
     * getLiveBroadcastInprogress(根据场次和城市获取正在直播的数据)
     * @param  @param liveDealerBroadcastDto
     * @param  @return    设定文件
     * @return List<LiveDealerBroadcastDto>
     * @Exception 异常对象
     * @since  CodingExample　Ver(编码范例查看) 1.1
     */
	List<LiveDealerBroadcastDto> getLiveBroadcastInprogressList(LiveDealerBroadcastDto liveDealerBroadcastDto);
	
	/**
	 * 
	 * getLiveBroadcastPlayback(根据场次和城市获取回放数据)
	 * @param  @param liveDealerBroadcastDto
	 * @param  @return    设定文件
	 * @return List<LiveDealerBroadcastDto>
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	List<LiveDealerBroadcastDto> getLiveBroadcastPlaybackList(LiveDealerBroadcastDto liveDealerBroadcastDto);
	
	/**
	 * 
	 * getLiveBroadcastForBrand(根据场次和活动时间和品牌获取直播和回放数据数量)
	 * @param  @param liveDealerBroadcastDto
	 * @param  @return    设定文件
	 * @return List<LiveDealerBroadcastDto>
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	List<LiveDealerBroadcastDto> getLiveBroadcastForBrandList(LiveDealerBroadcastDto liveDealerBroadcastDto);
	

    /***
     * @description: 根据场次id获取直播中数据
     * @param periodsId
     * @return: java.util.List<com.tuanche.directselling.dto.LiveInfoDto>
     * @author: dudg
     * @date: 2020/4/29 17:11
    */
    List<LiveInfoDto> getLivingRollNoticeByPeriodsId(Integer periodsId);

    /**
     * @description: 取指定主播ids和场次起止时间直播数据（如果优先直播中其次观看量最大）
     * @param liveIds
     * @return: java.util.List<com.tuanche.directselling.dto.LiveInfoDto>
     * @author: dudg
     * @date: 2020/4/30 14:10
    */
    List<LiveInfoDto> getLivingOrPlayBackInfosByAnchorAndPeriods(LiveParameterVo parameterVo);

	/**
	 * @description: 返回指定公司直播号下场次时间范围内最大观看数回放直播信息
	 * @param liveDealerBroadcastDto
	 * @return: com.tuanche.directselling.dto.LiveDealerBroadcastDto
	 * @author: dudg
	 * @date: 2020/5/9 10:33
	*/
	LiveDealerBroadcastDto getMaxViewCountPlayBackByPeriodsTime(LiveDealerBroadcastDto liveDealerBroadcastDto);

	/**
	 * @description: 预热阶段获取直播列表数据（直播中：城市号&参展经销商，回放：每个参展经销商观看数最大一条）
	 * @param liveDealerBroadcastDto
	 * @return: java.util.List<com.tuanche.directselling.dto.LiveDealerBroadcastDto>
	 * @author: dudg
	 * @date: 2020/5/10 16:17
	*/
	List<LiveDealerBroadcastDto> getPreheatLivingAndPlayBackList(LiveDealerBroadcastDto liveDealerBroadcastDto);

	/**
	 * 根据条件取团车主直播账号下直播数据
	 * @param liveDealerBroadcastDto
	 * @return
	 */
	List<LiveDealerBroadcastDto> getTcAnchorLiveList(LiveParameterVo liveParameterVo);
}