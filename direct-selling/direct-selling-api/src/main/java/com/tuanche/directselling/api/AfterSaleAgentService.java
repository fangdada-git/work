package com.tuanche.directselling.api;

import com.tuanche.directselling.model.AfterSaleAgent;
import com.tuanche.directselling.utils.PageResult;

import java.util.List;
import java.util.Map;

public interface AfterSaleAgentService {


    int deleteByPrimaryKey(Integer id);

    int insert(AfterSaleAgent record);

    int insertSelective(AfterSaleAgent record);

    AfterSaleAgent selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AfterSaleAgent record);

    int updateByPrimaryKey(AfterSaleAgent record);

    /**
     * 获取单个代理人信息-走缓存
     *
     * @param afterSaleAgent
     * @return com.tuanche.directselling.model.AfterSaleAgent
     * @author HuangHao
     * @CreatTime 2021-07-22 17:16
     */
    AfterSaleAgent getAfterSaleAgent(Integer activityId, String agentWxUnionId);

    /**
     * 获取车商或电销代理人
     * 先获取车商代理人，没获取到则获取电销代理人
     * @author HuangHao
     * @CreatTime 2021-09-03 9:51
     * @param activityId
     * @param agentWxUnionId
     * @return com.tuanche.directselling.model.AfterSaleAgent
     */
     AfterSaleAgent getDealerOrTelesalesAgent(Integer activityId, String agentWxUnionId);
    /**
     * 批量查代理人 根据代理人wxUnionId
     *
     * @param wxUnionIds
     * @return
     */
    Map<String, AfterSaleAgent> selectByAgentWxUnionIds(Integer activityId, List<String> wxUnionIds);

    PageResult getAgentList(int page, int limit, Integer activityId, Integer agentType, String nameOrPhone);

    List<AfterSaleAgent> getAfterSaleAgentList(AfterSaleAgent agent);
}
