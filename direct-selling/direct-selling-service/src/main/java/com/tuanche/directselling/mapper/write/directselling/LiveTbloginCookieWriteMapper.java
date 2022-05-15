package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.LiveTbloginCookie;

/**
 * LiveTbloginCookieMapper继承基类
 */
public interface LiveTbloginCookieWriteMapper extends MyBatisBaseDao<LiveTbloginCookie, Integer> {

    void delAll();
}