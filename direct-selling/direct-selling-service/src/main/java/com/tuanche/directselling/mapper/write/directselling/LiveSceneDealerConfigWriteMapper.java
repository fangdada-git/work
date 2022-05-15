package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.LiveSceneDealerConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * LiveSceneDealerConfigDAO继承基类
 */
@Repository
public interface LiveSceneDealerConfigWriteMapper extends MyBatisBaseDao<LiveSceneDealerConfig, Integer> {

    void deleteByLiveSceneDealerConfig(LiveSceneDealerConfig liveSceneDealerConfig);

    /**
     * 根据IDS批量更新经销商赠送油卡上限
     * @author HuangHao
     * @CreatTime 2020-05-29 16:39
     * @param refuelingCardNum
     * @param ids
     * @return int
     */
    int updateRefuelingCardNumByIds(@Param("refuelingCardNum") Integer refuelingCardNum,
                                    @Param("ids") List<Integer> ids, @Param("updateUserName") String updateUserName);
    /**
     * 根据场次批量设置赠送油卡上限
     * @author HuangHao
     * @CreatTime 2020-06-17 16:09
     * @param refuelingCardNum
     * @param periodsId
     * @param updateUserName
     * @return int
     */
    int updateRefuelingCardNumByPeriodsId(@Param("refuelingCardNum") Integer refuelingCardNum,
                                    @Param("periodsId") Integer periodsId, @Param("updateUserName") String updateUserName);


    void deleteByNotInDealerIds(@Param("sceneId") Integer sceneId, @Param("dealerIds") List<Integer> dealerIds, @Param("updateUserId") Integer updateUserId, @Param("updateUserName") String updateUserName, @Param("updateDt") Date updateDt);

}