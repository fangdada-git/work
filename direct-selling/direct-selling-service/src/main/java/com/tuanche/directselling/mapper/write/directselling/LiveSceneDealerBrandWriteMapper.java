package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.LiveSceneDealerBrand;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * LiveSceneDealerBrandDAO继承基类
 */
@Repository
public interface LiveSceneDealerBrandWriteMapper extends MyBatisBaseDao<LiveSceneDealerBrand, Integer> {

    void deleteByLiveSceneDealerBrand(LiveSceneDealerBrand liveSceneDealerBrand);

    void deleteByNotInDealerIds(@Param("sceneId") Integer sceneId, @Param("dealerIds") List<Integer> dealerIds, @Param("updateUserId") Integer updateUserId, @Param("updateUserName") String updateUserName, @Param("updateDt") Date updateDt);

    void deleteByNotInBrandIds(@Param("sceneId") Integer sceneId, @Param("brandIds") List<Integer> brandIds, @Param("updateUserId") Integer updateUserId, @Param("updateUserName") String updateUserName, @Param("updateDt") Date updateDt);

    void deleteByNotInCityIds(@Param("sceneId") Integer sceneId, @Param("cityIds") List<Integer> cityIds, @Param("updateUserId") Integer updateUserId, @Param("updateUserName") String updateUserName, @Param("updateDt") Date updateDt);
}