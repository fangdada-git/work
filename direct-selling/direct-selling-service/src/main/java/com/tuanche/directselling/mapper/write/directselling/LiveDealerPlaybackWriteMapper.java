package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.LiveDealerPlayback;
import org.springframework.stereotype.Repository;

/**
 * LiveDealerPlaybackWriteMapper继承基类
 */
@Repository
public interface LiveDealerPlaybackWriteMapper extends MyBatisBaseDao<LiveDealerPlayback, Integer> {
}