package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.mapper.MyBatisBaseDao;
import com.tuanche.directselling.model.LiveProgramDealerBrand;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * LiveProgramDealerBrandDAO继承基类
 */
@Repository
public interface LiveProgramDealerBrandReadMapper extends MyBatisBaseDao<LiveProgramDealerBrand, Integer> {
    /**
     * 查询一个对象列表
     */
    public List<LiveProgramDealerBrand> queryList(LiveProgramDealerBrand liveProgramDealerBrand);

    List<LiveProgramDealerBrand> selectDealerBrandList(Integer dealerBrandId);
}