package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.LiveScene;
import org.springframework.stereotype.Repository;

/**
 * LiveSceneDAO继承基类
 */
@Repository
public interface LiveSceneWriteMapper extends MyBatisBaseDao<LiveScene, Integer> {
}