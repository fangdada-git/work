package com.tuanche.directselling.api;

import com.tuanche.directselling.dto.LiveSceneDealerBrandDto;
import com.tuanche.directselling.model.LiveSceneDealerBrand;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.vo.LiveSceneBrandCarStyleVo;
import com.tuanche.directselling.vo.LiveSceneBrandListVo;
import com.tuanche.directselling.vo.LiveSceneBrandVo;
import com.tuanche.directselling.vo.LiveSceneCarStyleVo;
import com.tuanche.directselling.vo.LiveSceneDealerBrandVo;
import com.tuanche.directselling.vo.LiveSceneSecondBrandListVo;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: LiveSceneDealerBrandService
 * @Description: 团车直卖-场次活动经销商品牌服务
 * @Author: GongBo
 * @Date: 2020/3/6 17:53
 * @Version 1.0
 **/
public interface LiveSceneDealerBrandService {

    /**
     * @return java.lang.String
     * @Author GongBo
     * @Description 团车直卖-场次活动经销商品牌数据查询
     * @Date 2020年3月6日17:59:49
     **/
    PageResult findSceneDealerBrandList(PageResult<LiveSceneDealerBrand> pageResult, Integer sceneId);

    /**
     * @param liveSceneDealerBrandVo
     * @return java.util.List<com.tuanche.directselling.model.LiveSceneDealerBrand>
     * @Author GongBo
     * @Description 根据品牌ID &  经销商ID 获取场次活动配置经销商品牌集合
     * @Date 14:51 2020/3/9
     **/
    List<LiveSceneDealerBrand> getLiveSceneDealerBrandList(LiveSceneDealerBrandVo liveSceneDealerBrandVo);

    /**
     * @param liveSceneDealerBrand
     * @return void
     * @Author GongBo
     * @Description 团车直卖-新增场次活动经销商品牌配置
     * @Date 14:56 2020/3/9
     **/
    void insertByLiveSceneDealerBrand(LiveSceneDealerBrand liveSceneDealerBrand);

    /**
     * @param liveSceneDealerBrand
     * @return void
     * @Author GongBo
     * @Description 团车直卖-更新场次活动经销商品牌配置
     * @Date 16:00 2020/3/9
     **/
    void updateByLiveSceneDealerBrand(LiveSceneDealerBrand liveSceneDealerBrand);

    /**
     * @param id
     * @return com.tuanche.directselling.model.LiveSceneDealerBrand
     * @Author GongBo
     * @Description 团车直卖-获取经销商品牌详情
     * @Date 16:22 2020/3/9
     **/
    LiveSceneDealerBrand getLiveSceneDealerBrandById(Integer id);
    /**
     * @program:  periodsId , cityId
     * @return: java.util.List<com.tuanche.directselling.vo.LiveSceneBrandListVo>
     * @description: 获取品牌集合 直播投放页面使用
     * @author: czy
     * @create: 2020/4/22 15:53
     **/
    Map<String,List<LiveSceneBrandListVo>> getBrandListByPeriodsIdAndCityId(Integer periodsId, Integer cityId);

    /**
     * @param periodsId
     * @return:
     * @description: 根据大场次获取参展品牌列表
     * @author: czy
     * @create: 2020/4/30 10:40
     **/
    List<LiveSceneBrandVo> getBrandListByPeriodsId(Integer periodsId);

    /**
     * @param liveSceneDealerBrand
     * @return void
     * @Author GongBo
     * @Description 团车直卖-删除场次活动经销商品牌
     * @Date 2020年5月21日14:13:33
     **/
    void deleteByLiveSceneDealerBrand(LiveSceneDealerBrand liveSceneDealerBrand);

    /**
     * @Author GongBo
     * @Description 团车直卖-添加经销商配置和品牌配置
     * @Date 14:38 2020/5/22
     * @param liveSceneDealerBrandDto
     * @return java.lang.String
     **/
    String addDealerConfig(LiveSceneDealerBrandDto liveSceneDealerBrandDto) throws Exception;

    /**
     * @description 查询经销商在那个小场次下
     * @param [periodsId, dealerId, brandId]
     * @return java.util.List<com.tuanche.directselling.model.LiveSceneDealerBrand>
     * @date 2020/5/20 14:34
     * @author lvsen
     */
    List<LiveSceneDealerBrand> getLiveSceneDealerBrandListByPeriodsDealerId(Integer periodsId, Integer dealerId, Integer brandId);

    /**
     * @param periodsId
     * @param cityId
     * @return java.util.Map<java.lang.String,java.util.List<com.tuanche.directselling.vo.LiveSceneBrandListVo>>
     * @Author GongBo
     * @Description 根据场次 & 城市 & 主品牌ID  获取二级品牌  车型集合
     * @Date 17:25 2020/6/5
     **/
    Map<String, List<LiveSceneBrandVo>> getLiveSceneCarStyleList(Integer periodsId, Integer cityId, Integer masterBrandId);

    /**
     * @param periodsId
     * @param cityId
     * @param excludeBrandId
     * @param size
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @Author GongBo
     * @Description 根据场次 & 城市 & 条数 & 排除二级品牌ID  随机获取场次下的品牌
     * @Date 10:23 2020/6/15
     **/
    List<Map<String, Object>> getRandomBrands(Integer periodsId, Integer cityId, Integer excludeBrandId, Integer size);

    /**
     * @param periodsId
     * @param cityId
     * @return java.util.Map<java.lang.String,java.util.List<com.tuanche.directselling.vo.LiveSceneSecondBrandListVo>>
     * @Author GongBo
     * @Description 获取二级品牌集合
     * @Date 15:06 2020/7/7
     **/
    List<LiveSceneSecondBrandListVo> getSecondBrandListByPeriodsIdAndCityId(Integer periodsId, Integer cityId);

    /**
     * @param periodsId
     * @param cityId
     * @param secondBrandId
     * @return java.util.List<com.tuanche.directselling.vo.LiveSceneBrandCarStyleVo>
     * @Author GongBo
     * @Description 根据场次 & 城市 & 二级品牌 获取车型数据
     * @Date 15:31 2020/7/7
     **/
    List<LiveSceneCarStyleVo> getLiveSceneCarStyleListBySecondBrandId(Integer periodsId, Integer cityId, Integer secondBrandId);
}
