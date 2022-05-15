package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.LiveSceneDealerDto;
import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.LiveSceneDealerBrand;
import com.tuanche.directselling.vo.LiveSceneDealerBrandVo;
import com.tuanche.directselling.vo.LiveSceneVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * LiveSceneDealerBrandDAO继承基类
 */
@Repository
public interface LiveSceneDealerBrandReadMapper extends MyBatisBaseDao<LiveSceneDealerBrand, Integer> {
    List<LiveSceneDealerBrand> selectListSceneDealerBrandList(Integer sceneId);

    List<LiveSceneDealerBrand> selectListSceneDealerBrandListByVo(LiveSceneDealerBrandVo liveSceneDealerBrandVo);

    List<LiveSceneDealerBrand> selectBrandByDealerAndScene(@Param("sceneId") Integer sceneId, @Param("dealerId") Integer dealerId);

    /**
     * 查询一个对象列表
     */
    public List<LiveSceneDealerBrand> queryList(LiveSceneDealerBrand liveSceneDealerBrand);

    public List<LiveSceneDealerBrand> searchBrands(LiveSceneDealerBrand liveSceneDealerBrand);

    public List<LiveSceneDealerBrand> searchDealers(LiveSceneDealerBrand liveSceneDealerBrand);

    public List<LiveSceneDealerBrand> searchDealerBrands(LiveSceneDealerBrand liveSceneDealerBrand);

    public LiveSceneDealerBrand searchDealerBrand(LiveSceneDealerBrand liveSceneDealerBrand);

    List<LiveSceneDealerDto> findDealerBySceneVo(LiveSceneVo liveSceneVo);

    /**
     * @program: periodsId , cityId
     * @return: java.util.List<com.tuanche.directselling.vo.LiveSceneBrandListVo>
     * @description: 获取品牌集合 直播投放页面使用
     * @author: czy
     * @create: 2020/4/22 15:53
     **/
    List<Integer> getBrandListByPeriodsIdAndCityId(@Param("periodsId") Integer periodsId, @Param("cityId") Integer cityId);


    /**
     * @param periodsId
     * @return:
     * @description:
     * @author: czy
     * @create: 2020/4/30 10:44
     **/
    List<Integer> getBrandListByPeriodsId(@Param("periodsId") Integer periodsId);

    /**
     * @param [periodsId, dealerId, brandId]
     * @return java.util.List<java.lang.Integer>
     * @description
     * @date 2020/5/20 14:28
     * @author lvsen
     */
    List<LiveSceneDealerBrand> getLiveSceneDealerBrandListByPeriodsIdDealerIdBrandId(@Param("periodsId") Integer periodsId, @Param("dealerId") Integer dealerId, @Param("brandId") Integer brandId);

    /**
     * @param periodsId
     * @param cityId
     * @return java.util.List<LiveSceneDealerBrand>
     * @Author GongBo
     * @Description 获取场次下 品牌车型数据
     * @Date 17:47 2020/6/5
     **/
    List<LiveSceneDealerBrand> getBrandList(@Param("periodsId") Integer periodsId, @Param("cityId") Integer cityId,
                                            @Param("brandIds") List<Integer> brandIds);

    /**
     * @param periodsId
     * @param cityId
     * @param excludeBrandId
     * @param size
     * @return java.util.List<com.tuanche.directselling.model.LiveSceneDealerBrand>
     * @Author GongBo
     * @Description 随机获取场次下品牌
     * @Date 10:35 2020/6/15
     **/
    List<LiveSceneDealerBrand> getRandomBrandList(@Param("periodsId") Integer periodsId, @Param("cityId") Integer cityId,
                                            @Param("excludeBrandId") Integer excludeBrandId, @Param("size") Integer size);
}