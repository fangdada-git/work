package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.exception.RedisException;
import com.tuanche.directselling.api.AfterSaleAgentService;
import com.tuanche.directselling.constant.BaseCacheKeys;
import com.tuanche.directselling.dto.AfterSaleAgentDto;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleAgentReadMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleAgentWriteMapper;
import com.tuanche.directselling.model.AfterSaleAgent;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.FissionAllSaleRankVo;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.model.CsCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import sun.management.resources.agent;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service(retries = 0,timeout = 20000)
public class AfterSaleAgentServiceImpl implements AfterSaleAgentService {

    @Reference
    CompanyBaseService companyBaseService;
    @Resource
    private AfterSaleAgentReadMapper afterSaleAgentReadMapper;
    @Resource
    private AfterSaleAgentWriteMapper afterSaleAgentWriteMapper;
    @Autowired
    @Qualifier("ClusterRedisService")
    private RedisService redisService;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return afterSaleAgentWriteMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(AfterSaleAgent record) {
        return afterSaleAgentWriteMapper.insert(record);
    }

    @Override
    public int insertSelective(AfterSaleAgent record) {
        return afterSaleAgentWriteMapper.insertSelective(record);
    }

    @Override
    public AfterSaleAgent selectByPrimaryKey(Integer id) {
        return afterSaleAgentReadMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(AfterSaleAgent record) {
        AfterSaleAgent afterSaleAgent = afterSaleAgentReadMapper.selectByPrimaryKey(record.getId());
        String key = MessageFormat.format(BaseCacheKeys.AFTER_SALE_ACTIVITYID_AGENT.getKey(), afterSaleAgent.getActivityId(), afterSaleAgent.getAgentWxUnionId());
        try {
            redisService.del(key);
        } catch (RedisException e) {
            CommonLogUtil.addError("删除代理人缓存失败", "删除代理人缓存失败", e);
        }
        return afterSaleAgentWriteMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(AfterSaleAgent record) {
        return afterSaleAgentWriteMapper.updateByPrimaryKey(record);
    }

    /**
     * 获取代理人信息-走缓存
     * @param afterSaleAgent
     * @return com.tuanche.directselling.model.AfterSaleAgent
     * @author HuangHao
     * @CreatTime 2021-07-22 17:16
     */
    @Override
    public synchronized AfterSaleAgent getAfterSaleAgent(Integer activityId, String agentWxUnionId) {
        AfterSaleAgent afterSaleAgent = null;
        if(activityId == null || StringUtils.isEmpty(agentWxUnionId)){
            return null;
        }
        try {
            String key = MessageFormat.format(BaseCacheKeys.AFTER_SALE_ACTIVITYID_AGENT.getKey(), activityId, agentWxUnionId);
            afterSaleAgent = redisService.get(key, AfterSaleAgent.class);
            if (afterSaleAgent == null) {
                AfterSaleAgent agent = new AfterSaleAgent();
                agent.setActivityId(activityId);
                agent.setAgentWxUnionId(agentWxUnionId);
                afterSaleAgent = afterSaleAgentReadMapper.getAfterSaleAgent(agent);
                if (afterSaleAgent != null) {
                    redisService.set(key, afterSaleAgent, BaseCacheKeys.AFTER_SALE_ACTIVITYID_AGENT.getExpire());
                }
            }
        } catch (RedisException e) {
            e.printStackTrace();
        }

        return afterSaleAgent;
    }

    /**
     * 获取车商或电销代理人
     * 先获取车商代理人，没获取到则获取电销代理人
     * @author HuangHao
     * @CreatTime 2021-09-03 9:51
     * @param activityId
     * @param agentWxUnionId
     * @return com.tuanche.directselling.model.AfterSaleAgent
     */
    public synchronized AfterSaleAgent getDealerOrTelesalesAgent(Integer activityId, String agentWxUnionId){
        AfterSaleAgent afterSaleAgent = getAfterSaleAgent(activityId, agentWxUnionId);
        //代理人为空则获取团车电销代理人
        if(afterSaleAgent == null) {
            afterSaleAgent = getAfterSaleAgent(0, agentWxUnionId);
        }
        return afterSaleAgent;
    }

    @Override
    public Map<String, AfterSaleAgent> selectByAgentWxUnionIds(Integer activityId, List<String> wxUnionIds) {
        return afterSaleAgentReadMapper.selectByAgentWxUnionIds(activityId, wxUnionIds);
    }

    @Override
    public PageResult getAgentList(int page, int limit, Integer activityId, Integer agentType, String nameOrPhone) {
        PageResult<AfterSaleAgentDto> pageResult = new PageResult<>();
        PageHelper.startPage(page, limit);
        List<AfterSaleAgentDto> afterSaleAgentDtos = afterSaleAgentReadMapper.getAfterSaleList(activityId, agentType, nameOrPhone);
        if (afterSaleAgentDtos.isEmpty()) {
            return pageResult;
        }
        List<Integer> dealerIds = new ArrayList<>();
        for (AfterSaleAgentDto afterSaleAgentDto : afterSaleAgentDtos) {
            dealerIds.add(afterSaleAgentDto.getDealerId());
        }
        Map<Integer, CsCompany> companyMap = companyBaseService.getCompanyMap(dealerIds);
        for (AfterSaleAgentDto afterSaleAgentDto : afterSaleAgentDtos) {
            CsCompany csCompany = companyMap.get(afterSaleAgentDto.getDealerId());
            if (csCompany != null) {
                afterSaleAgentDto.setDealerName(csCompany.getCompanyName());
            }
        }
        PageInfo<FissionAllSaleRankVo> pageInfo = new PageInfo(afterSaleAgentDtos);
        pageResult.setCount(pageInfo.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode());
        pageResult.setMsg(StatusCodeEnum.SUCCESS.getText());
        pageResult.setPage(page);
        pageResult.setLimit(limit);
        pageResult.setData(afterSaleAgentDtos);
        return pageResult;
    }

    @Override
    public List<AfterSaleAgent> getAfterSaleAgentList(AfterSaleAgent agent) {
        return afterSaleAgentReadMapper.getAfterSaleAgentList(agent);
    }
}
