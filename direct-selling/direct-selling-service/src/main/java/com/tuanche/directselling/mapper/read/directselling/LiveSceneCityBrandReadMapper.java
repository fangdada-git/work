package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.model.LiveSceneCityBrand;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LiveSceneCityBrandReadMapper {

    List<LiveSceneCityBrand> selectLiveSceneCityBrandBySceneId(@Param("sceneId") Integer sceneId);

    List<LiveSceneCityBrand> selectLiveSceneCityBrandBySceneIds(@Param("sceneIds") List<Integer> sceneIds);

    List<LiveSceneCityBrand> selectLiveSceneCityBrandBySceneIdAndType(@Param("sceneId") Integer sceneId, @Param("type") Integer type);
}