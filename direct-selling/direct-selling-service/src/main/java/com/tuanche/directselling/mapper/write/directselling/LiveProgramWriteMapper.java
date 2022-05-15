package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.LiveProgram;
import org.springframework.stereotype.Repository;

/**
 * LiveProgramDAO继承基类
 */
@Repository
public interface LiveProgramWriteMapper extends MyBatisBaseDao<LiveProgram, Integer> {
}