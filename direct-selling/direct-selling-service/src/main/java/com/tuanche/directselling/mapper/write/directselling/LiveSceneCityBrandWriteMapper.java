package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.LiveSceneCityBrand;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LiveSceneCityBrandWriteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LiveSceneCityBrand record);

    int insertSelective(LiveSceneCityBrand record);

    int updateByPrimaryKeySelective(LiveSceneCityBrand record);

    int updateByPrimaryKey(LiveSceneCityBrand record);

    int deleteNotInDataIdAndType(@Param("sceneId") Integer sceneId, @Param("dataIds") List<Integer> dataIds, @Param("type") Integer type);
}