package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.model.FissionAwardRule;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description 裂变奖励规则
 * @date 2020/9/22 16:55
 * @author lvsen
 */
@Repository
public interface FissionAwardRuleReadMapper {

    List<FissionAwardRule> selectAwardRuleByFissionId(@Param("fissionId") Integer fissionId);

    /**
     * 获取通过裂变活动ids和类型获取规则列表
     * @author HuangHao
     * @CreatTime 2020-09-29 10:52
     * @param ruleType
     * @param fissionIds
     * @return java.util.List<com.tuanche.directselling.model.FissionAwardRule>
     */
    List<FissionAwardRule> selectAwardRuleByFissionIdsAndType(@Param("ruleType")Integer ruleType,@Param("fissionIds")List<Integer> fissionIds);
}