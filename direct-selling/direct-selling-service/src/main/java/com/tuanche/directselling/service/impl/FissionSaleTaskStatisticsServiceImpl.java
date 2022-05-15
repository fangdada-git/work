package com.tuanche.directselling.service.impl;

import com.tuanche.directselling.api.FissionSaleService;
import com.tuanche.directselling.api.FissionSaleTaskStatisticsService;
import com.tuanche.directselling.api.FissionUserEffectiveTaskService;
import com.tuanche.directselling.enums.AwardRuleType;
import com.tuanche.directselling.mapper.read.directselling.FissionAwardRuleReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FissionSaleTaskStatisticsReadMapper;
import com.tuanche.directselling.mapper.write.directselling.FissionSaleTaskStatisticsWriteMapper;
import com.tuanche.directselling.model.FissionAwardRule;
import com.tuanche.directselling.model.FissionSale;
import com.tuanche.directselling.model.FissionSaleTaskStatistics;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.util.ConstantsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author：HuangHao
 * @CreatTime 2020-09-28 17:56
 */
@Service
public class FissionSaleTaskStatisticsServiceImpl implements FissionSaleTaskStatisticsService {


    @Autowired
    FissionUserEffectiveTaskService fissionUserEffectiveTaskService;
    @Autowired
    FissionSaleTaskStatisticsReadMapper fissionSaleTaskStatisticsReadMapper;
    @Autowired
    FissionSaleTaskStatisticsWriteMapper fissionSaleTaskStatisticsWriteMapper;
    @Autowired
    FissionAwardRuleReadMapper fissionAwardRuleReadMapper;
    @Autowired
    FissionSaleService fissionSaleService;

    public static String LOG_KEW_WORD = "裂变任务积分统计";

    /**
     * 统计销售积分
     * @author HuangHao
     * @CreatTime 2020-09-29 17:54
     * @param
     * @return void
     */
    public void salesTaskStatistics(List<Integer> fissionIds){
        long stime = System.currentTimeMillis();
        CommonLogUtil.addInfo(null, FissionSaleTaskStatisticsServiceImpl.LOG_KEW_WORD+"开启中的裂变任务："+fissionIds);
        if(fissionIds != null){
            //获取完成任务的销售
            List<FissionSaleTaskStatistics> saleTaskStatistics = fissionUserEffectiveTaskService.statisticsSaleTaskIntegrals(fissionIds);
            if(saleTaskStatistics != null){
                //统计表里存在的销售(key=fissionId_saleWxUnionId)
                Map<String,String> fissionSales = fissionSaleTaskStatisticsWriteMapper.existingSales(fissionIds);
                //新增的map（数据结构：销售微信UnionId->参加的裂变活动->裂变活动每个任务产生的积分）
                Map<String,Map<Integer,Map<Integer,FissionSaleTaskStatistics>>> insertSaleStatisticsMap = new HashMap<>();
                //更新的列表
                List<FissionSaleTaskStatistics> updateSaleStatisticsList = new ArrayList<>();
                //第一次统计
                if(fissionSales == null){
                    //所有销售插入裂变配置的任务
                    for (FissionSaleTaskStatistics sale : saleTaskStatistics) {
                        insertSaleStatisticsData(insertSaleStatisticsMap, sale);
                    }
                }else{
                     //找出统计表里没有的销售插入裂变配置的任务
                    for (FissionSaleTaskStatistics sale : saleTaskStatistics) {
                        String mapKey = sale.getFissionId()+"_"+sale.getSaleWxUnionId();
                        //等于null则说明销售在该场裂变任务下还未进行过统计，则新增，否则更新
                        if(fissionSales.get(mapKey)==null){
                            insertSaleStatisticsData(insertSaleStatisticsMap, sale);
                        }else{
                            updateSaleStatisticsList.add(sale);
                        }
                    }
                }
                //新增统计
                if(insertSaleStatisticsMap.size() > 0){
                    addSaleTaskStatistics(fissionIds, insertSaleStatisticsMap);
                }
                //更新统计
                if(updateSaleStatisticsList.size() > 0){
                    updateSaleStatistics(updateSaleStatisticsList);
                }

            }
        }
        long etime = System.currentTimeMillis();
        CommonLogUtil.addInfo(null, FissionSaleTaskStatisticsServiceImpl.LOG_KEW_WORD+"统计完成，耗时："+(etime-stime));
    }


    /**
     * 更新销售统计
     * @author HuangHao
     * @CreatTime 2020-09-29 17:05
     * @param fissionIds
     * @param insertMap
     * @return void
     */
    private void updateSaleStatistics(List<FissionSaleTaskStatistics> updateList){
        int listSize = updateList.size();
        if(listSize > 0){
            //数量小于等于BATCH_PAGE_SIZE则直接更新，否则分批更新
            if(listSize <= ConstantsUtil.BATCH_PAGE_SIZE){
                fissionSaleTaskStatisticsWriteMapper.updateBatchTaskIntegral(updateList);
            }else {
                int pageNum = ConstantsUtil.batchNum(listSize);
                for (int i = 0; i < pageNum; i++) {
                    int toIndex = (i+1)*ConstantsUtil.BATCH_PAGE_SIZE;
                    if(toIndex>listSize){
                        toIndex =listSize;
                    }
                    List<FissionSaleTaskStatistics> batchList =updateList.subList(i*ConstantsUtil.BATCH_PAGE_SIZE, toIndex);
                    fissionSaleTaskStatisticsWriteMapper.updateBatchTaskIntegral(batchList);
                }
            }
        }
    }
    /**
     * 新增销售统计
     * @author HuangHao
     * @CreatTime 2020-09-29 17:05
     * @param fissionIds
     * @param insertMap
     * @return void
     */
    private void addSaleTaskStatistics(List<Integer> fissionIds,Map<String,Map<Integer,Map<Integer,FissionSaleTaskStatistics>>> insertMap){
        //B端裂变规则
        Map<Integer,List<FissionAwardRule>> ruleMap = getFissionAwardRuleMap(fissionIds);
        if(ruleMap != null){
            List<FissionSaleTaskStatistics> insertList = new ArrayList<>();
            BigDecimal zero = new BigDecimal("0");
            insertMap.forEach((key,value) -> {
                String saleWxUnionId = key;
                value.forEach((fissionId,taskStatisticsMap) -> {
                    FissionSale fissionSale = new FissionSale();
                    fissionSale.setFissionId(fissionId);
                    fissionSale.setSaleWxUnionId(saleWxUnionId);
                    FissionSale sale = fissionSaleService.getFissionSale(fissionSale);
                    if(sale != null && sale.getSaleId() != null){
                        List<FissionAwardRule> ruleList = ruleMap.get(fissionId);
                        //插入所有配置的任务到销售统计表
                        ruleList.forEach(rule -> {
                            Integer taskId = Integer.valueOf(rule.getTaskCode());
                            FissionSaleTaskStatistics saleTaskStatistics = taskStatisticsMap.get(taskId);
                            //未完成的任务插入数据
                            if(saleTaskStatistics == null){
                                saleTaskStatistics = new FissionSaleTaskStatistics();
                                saleTaskStatistics.setFissionId(fissionId);
                                saleTaskStatistics.setSaleWxUnionId(saleWxUnionId);
                                saleTaskStatistics.setTaskId(taskId);
                                saleTaskStatistics.setFinishTaskTotal(0);
                                saleTaskStatistics.setTaskIntegral(0);
                                saleTaskStatistics.setWhetherCompleteTask(false);
                                //如果任务的基础目标就是0则直接完成任务
                                if(zero.compareTo(rule.getAwardRule()) == 0){
                                    saleTaskStatistics.setWhetherCompleteTask(true);
                                }else{
                                    saleTaskStatistics.setWhetherCompleteTask(false);
                                }
                            }
                            saleTaskStatistics.setDealerId(sale.getDealerId());
                            saleTaskStatistics.setSaleId(sale.getSaleId());
                            insertList.add(saleTaskStatistics);
                        });
                    }

                });
            });
            int listSize = insertList.size();
            if(listSize > 0){
                //数量小于等于BATCH_PAGE_SIZE则直接插入，否则分批插入
                if(listSize <= ConstantsUtil.BATCH_PAGE_SIZE){
                    fissionSaleTaskStatisticsWriteMapper.batchInsert(insertList);
                }else {
                    int pageNum = ConstantsUtil.batchNum(listSize);
                    for (int i = 0; i < pageNum; i++) {
                        int toIndex = (i+1)*ConstantsUtil.BATCH_PAGE_SIZE;
                        if(toIndex>listSize){
                            toIndex =listSize;
                        }
                        List<FissionSaleTaskStatistics> batchList =insertList.subList(i*ConstantsUtil.BATCH_PAGE_SIZE, toIndex);
                        fissionSaleTaskStatisticsWriteMapper.batchInsert(batchList);
                    }
                }
            }
        }
    }
    /**
     * 整理好需要插入的数据
     * 数据结构：销售UnionId->参加的裂变活动->裂变活动每个任务产生的积分
     * key=销售微信UnionId，
     * value：key=裂变活动ID
     * value：value：key=任务ID,
     * value：value：value 销售单个任务的统计
     * @author HuangHao
     * @CreatTime 2020-09-29 17:47
     * @param insertSaleStatisticsMap
     * @param sale
     * @return void
     */
    private void insertSaleStatisticsData(Map<String,Map<Integer,Map<Integer,FissionSaleTaskStatistics>>> insertSaleStatisticsMap, FissionSaleTaskStatistics sale ){
        Map<Integer,Map<Integer,FissionSaleTaskStatistics>> saleTaskStatisticsMap = insertSaleStatisticsMap.get(sale.getSaleWxUnionId());
        Map<Integer,FissionSaleTaskStatistics> taskStatisticsMap = null;
        if(saleTaskStatisticsMap == null || saleTaskStatisticsMap.get(sale.getFissionId()) == null){
            saleTaskStatisticsMap = new HashMap<>();
            taskStatisticsMap = new HashMap<>();
        }else{
            taskStatisticsMap = saleTaskStatisticsMap.get(sale.getFissionId());
        }
        taskStatisticsMap.put(sale.getTaskId(), sale);
        saleTaskStatisticsMap.put(sale.getFissionId(), taskStatisticsMap);
        insertSaleStatisticsMap.put(sale.getSaleWxUnionId(), saleTaskStatisticsMap);
    }

    /**
     * 获取B端裂变活动规则
     * @author HuangHao
     * @CreatTime 2020-09-29 10:59
     * @param fissionIds
     * @return java.util.Map<java.lang.Integer,java.util.List<com.tuanche.directselling.model.FissionAwardRule>>
     */
    private Map<Integer,List<FissionAwardRule>> getFissionAwardRuleMap(List<Integer> fissionIds){
        List<FissionAwardRule> awardRules = fissionAwardRuleReadMapper.selectAwardRuleByFissionIdsAndType(AwardRuleType.B.getType(),fissionIds);
        Map<Integer,List<FissionAwardRule>> ruleMap = null;
        if(awardRules != null){
            ruleMap = new HashMap<>();
            for (FissionAwardRule awardRule : awardRules) {
                List<FissionAwardRule> list = ruleMap.get(awardRule.getFissionId());
                if(list == null){
                    list = new ArrayList<>();
                }
                list.add(awardRule);
                ruleMap.put(awardRule.getFissionId(), list);
            }
        }
        return ruleMap;
    }

}
