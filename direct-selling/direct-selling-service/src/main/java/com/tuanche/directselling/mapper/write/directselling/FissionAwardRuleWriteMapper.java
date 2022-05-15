package com.tuanche.directselling.mapper.write.directselling;

import com.tuanche.directselling.model.FissionAwardRule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FissionAwardRuleWriteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FissionAwardRule record);

    int batchInsert(@Param("awardRuleList") List<FissionAwardRule> fissionAwardRules);

    int deleteByFissionId(@Param("fissionId") Integer fissionId, @Param("ruleType") Short ruleType);

    int insertSelective(FissionAwardRule record);

    int updateByPrimaryKeySelective(FissionAwardRule record);

    int updateByPrimaryKey(FissionAwardRule record);
}