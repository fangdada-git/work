package com.tuanche.directselling.mapper.read.directselling;

import com.tuanche.directselling.dto.AfterSaleAgentDto;
import com.tuanche.directselling.model.AfterSaleAgent;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AfterSaleAgentReadMapper {
    AfterSaleAgent selectByPrimaryKey(Integer id);

    @MapKey("agentWxUnionId")
    Map<String, AfterSaleAgent> selectByAgentWxUnionIds(@Param("activityId") Integer activityId, @Param("wxUnionIds") List<String> wxUnionIds);

    AfterSaleAgent getAfterSaleAgent(AfterSaleAgent afterSaleAgent);

    List<AfterSaleAgentDto> getAfterSaleList(@Param("activityId") Integer activityId, @Param("agentType") Integer agentType, @Param("nameOrPhone") String nameOrPhone);

    List<AfterSaleAgent> getAfterSaleAgentList(AfterSaleAgent agent);
}