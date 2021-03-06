package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tuanche.directselling.api.FissionActivityService;
import com.tuanche.directselling.api.FissionStatisticsService;
import com.tuanche.directselling.dto.FissionActivityIncomeDto;
import com.tuanche.directselling.dto.FissionStatisticBigDecimalDto;
import com.tuanche.directselling.dto.FissionStatisticIntDto;
import com.tuanche.directselling.enums.AwardRuleType;
import com.tuanche.directselling.enums.CompleteTaskStatus;
import com.tuanche.directselling.enums.PageSourceType;
import com.tuanche.directselling.enums.StatDataType;
import com.tuanche.directselling.mapper.read.directselling.FissionDataViewReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FissionSaleDataViewReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FissionSalesOrderReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FissionStatisticsReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FissionUserDataViewReadMapper;
import com.tuanche.directselling.mapper.sharding.FissionUserTaskRecordMapper;
import com.tuanche.directselling.mapper.write.directselling.FissionActivityWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.FissionDataViewWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.FissionSaleDataViewWriteMapper;
import com.tuanche.directselling.mapper.write.directselling.FissionUserDataViewWriteMapper;
import com.tuanche.directselling.model.FissionActivity;
import com.tuanche.directselling.model.FissionAwardRule;
import com.tuanche.directselling.model.FissionDataView;
import com.tuanche.directselling.model.FissionSaleDataView;
import com.tuanche.directselling.model.FissionUserDataView;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.util.StatisticsTask;
import com.tuanche.directselling.util.StatisticsTaskThread;
import com.tuanche.directselling.utils.BeanCopyUtil;
import com.tuanche.directselling.utils.GlobalConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2020/9/28 16:37
 **/
@Service
public class FissionStatisticsServiceImpl implements FissionStatisticsService {

    /**
     * ?????????????????????
     */
    private final Integer BATCH_LIMIT = 1000;
    /**
     * B????????????????????????(?????????)
     */
    private final Integer SALE_TASK_NUM = 41;
    /**
     * C????????????????????????(?????????)
     */
    private final Integer USER_TASK_NUM = 22;
    /**
     * ???????????????????????????(?????????)
     */
    private final Integer CHANNEL_TASK_NUM = 21;

    /**
     * sql????????????
     */
    private final String DISTINCT_TYPE_SALE = "sale";
    private final String DISTINCT_TYPE_SHARE = "share";
    private final String DISTINCT_TYPE_USER = "user";
    /**
     * ???????????????null
     */
    private final String IS_NULL = "isNull";
    private final String IS_NOT_NULL = "isNotNull";
    /**
     * sale_wx_union_id ???????????? share_wx_union_id
     */
    private final String IS_EQUALS_SHARE = "isEqualsShare";
    private final String IS_NOT_EQUALS_SHARE = "isNotEqualsShare";
    /**
     * ??????id
     */
    private final Integer TASK_ID_1 = 1;
    private final Integer TASK_ID_2 = 2;
    private final Integer TASK_ID_3 = 3;
    private final Integer TASK_ID_4 = 4;
    private final Integer TASK_ID_5 = 5;
    /**
     * B?????????????????????
     */
    private final int B_GEN = 1;
    /**
     * C?????????????????????
     */
    private final int C_GEN = 2;
    /**
     * B?????????-??????
     */
    private final int DATA_TYPE_5 = 5;

    /**
     * B?????????-????????????
     */
    private final int DATA_TYPE_6 = 6;
    @Autowired
    private FissionStatisticsServiceImpl fissionStatisticsServiceImpl;
    @Autowired
    private FissionStatisticsService fissionStatisticsService;
    @Autowired
    private FissionStatisticsReadMapper fissionStatisticsReadMapper;
    @Autowired
    private FissionUserTaskRecordMapper fissionUserTaskRecordMapper;
    @Autowired
    private FissionDataViewWriteMapper fissionDataViewWriteMapper;
    @Autowired
    private FissionSaleDataViewWriteMapper fissionSaleDataViewWriteMapper;
    @Autowired
    private FissionUserDataViewWriteMapper fissionUserDataViewWriteMapper;
    @Autowired
    private FissionUserDataViewReadMapper fissionUserDataViewReadMapper;
    @Autowired
    private FissionSaleDataViewReadMapper fissionSaleDataViewReadMapper;
    @Autowired
    private FissionActivityWriteMapper fissionActivityWriteMapper;
    @Autowired
    private FissionSalesOrderReadMapper fissionSalesOrderReadMapper;
    @Autowired
    private FissionActivityService fissionActivityService;
    @Autowired
    private FissionDataViewReadMapper fissionDataViewReadMapper;
    @Autowired
    private ExecutorService executorService;

    @Override
    public void updateSaleDataView() {
        CommonLogUtil.addInfo(null, "??????????????????????????????");
        //??????????????????BATCH_LIMIT??? ???????????????list
        List<FissionActivity> fissionActivities = fissionStatisticsReadMapper.selectStatNotGenerate(null, B_GEN, BATCH_LIMIT);
        while (!fissionActivities.isEmpty()) {
            Integer fissionId = fissionActivities.get(fissionActivities.size() - 1).getId();
            for (FissionActivity fissionActivity : fissionActivities) {
                saleDataHandle(fissionActivity.getId(), fissionActivity);
            }
            fissionActivities = fissionStatisticsReadMapper.selectStatNotGenerate(fissionId, B_GEN, BATCH_LIMIT);
        }
        CommonLogUtil.addInfo(null, "??????????????????????????????");
    }

    @Override
    public void updateUserDataView() {
        CommonLogUtil.addInfo(null, "??????????????????????????????");
        //??????????????????BATCH_LIMIT??? ???????????????list
        List<FissionActivity> fissionActivities = fissionStatisticsReadMapper.selectStatNotGenerate(null, C_GEN, BATCH_LIMIT);
        while (!fissionActivities.isEmpty()) {
            Integer fissionId = fissionActivities.get(fissionActivities.size() - 1).getId();
            for (FissionActivity fissionActivity : fissionActivities) {
                userDataHandle(fissionActivity.getId(), fissionActivity);
            }
            fissionActivities = fissionStatisticsReadMapper.selectStatNotGenerate(fissionId, C_GEN, BATCH_LIMIT);
        }
        CommonLogUtil.addInfo(null, "??????????????????????????????");
    }

    @Override
    public void updateDataView(int statGenerate, int dataType, int channel) {
        CommonLogUtil.addInfo(null, String.format("??????????????????[%d],????????????[%d]???????????????", channel, dataType));
        //??????????????????BATCH_LIMIT??? ???????????????list
        List<FissionActivity> fissionActivities = fissionStatisticsReadMapper.selectStatNotGenerate(null, statGenerate, BATCH_LIMIT);
        while (!fissionActivities.isEmpty()) {
            Integer fissionId = fissionActivities.get(fissionActivities.size() - 1).getId();
            for (FissionActivity fissionActivity : fissionActivities) {
                dataHandleByDataType(fissionActivity.getId(), fissionActivity, statGenerate, dataType, channel);
            }
            fissionActivities = fissionStatisticsReadMapper.selectStatNotGenerate(fissionId, statGenerate, BATCH_LIMIT);
        }
        CommonLogUtil.addInfo(null, String.format("????????????[%d],????????????[%d]?????????????????????", channel, dataType));
    }

    /**
     * List<FissionActivity> ??? Map<Integer, FissionActivity>
     *
     * @param fissionActivities
     * @return
     */
    private Map<Integer, FissionActivity> getFissionActivityMap(List<FissionActivity> fissionActivities) {
        Map<Integer, FissionActivity> fissionActivityMap = new HashMap<>((int) (BATCH_LIMIT / 0.75f + 1f));
        for (FissionActivity fissionActivity : fissionActivities) {
            fissionActivityMap.putIfAbsent(fissionActivity.getId(), fissionActivity);
        }
        return fissionActivityMap;
    }

    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    @Override
    public void insertFissionSaleDataView(FissionSaleDataView fissionSaleDataView) {
        fissionSaleDataView.setCreateDt(new Date());
        fissionSaleDataViewWriteMapper.insertSelective(fissionSaleDataView);
        FissionDataView fissionDataView = new FissionDataView();
        BeanCopyUtil.copyProperties(fissionSaleDataView, fissionDataView);
        fissionDataView.setDataType(StatDataType.STAT_B.getType());
        fissionDataViewWriteMapper.insertSelective(fissionDataView);
    }

    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    @Override
    public void updateFissionSaleDataView(Long updateDataViewId, Long updateSaleDataVieId, FissionSaleDataView fissionSaleDataView) {
        fissionSaleDataView.setUpdateDt(new Date());
        fissionSaleDataView.setId(updateSaleDataVieId);
        fissionSaleDataViewWriteMapper.updateByPrimaryKeySelective(fissionSaleDataView);
        FissionDataView fissionDataView = new FissionDataView();
        BeanCopyUtil.copyProperties(fissionSaleDataView, fissionDataView);
        fissionDataView.setId(updateDataViewId);
        fissionDataViewWriteMapper.updateByPrimaryKeySelective(fissionDataView);
    }


    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    @Override
    public void insertFissionUserDataView(FissionUserDataView fissionUserDataView) {
        fissionUserDataView.setCreateDt(new Date());
        fissionUserDataViewWriteMapper.insertSelective(fissionUserDataView);
        FissionDataView fissionDataView = new FissionDataView();
        BeanCopyUtil.copyProperties(fissionUserDataView, fissionDataView);
        fissionDataView.setDataType(StatDataType.STAT_C.getType());
        fissionDataViewWriteMapper.insertSelective(fissionDataView);
    }

    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    @Override
    public void updateFissionUserDataView(Long updateDataViewId, Long updateUserDataVieId, FissionUserDataView fissionUserDataView) {
        fissionUserDataView.setUpdateDt(new Date());
        fissionUserDataView.setId(updateUserDataVieId);
        fissionUserDataViewWriteMapper.updateByPrimaryKeySelective(fissionUserDataView);
        FissionDataView fissionDataView = new FissionDataView();
        BeanCopyUtil.copyProperties(fissionUserDataView, fissionDataView);
        fissionDataView.setId(updateDataViewId);
        fissionDataViewWriteMapper.updateByPrimaryKeySelective(fissionDataView);
    }


    /**
     * ??????FissionDataView???map??????,Map<fissionId, Map<dataType,List<FissionDataView>>>
     *
     * @param fissionId
     * @return
     */
    @Override
    public Map<Integer, List<FissionDataView>> getFissionDataViewMap(Integer fissionId) {
        List<FissionDataView> fissionDataViewData = fissionDataViewReadMapper.selectFissionDataViewByFissionId(fissionId);
        Map<Integer, List<FissionDataView>> fissionDataViewDataMap = new HashMap<>(8);
        for (FissionDataView fissionSaleDataView : fissionDataViewData) {
            List<FissionDataView> fissionDataViewMap = fissionDataViewDataMap.get(fissionSaleDataView.getDataType());
            if (fissionDataViewMap == null) {
                fissionDataViewDataMap.put(fissionSaleDataView.getDataType(), new ArrayList<>());
            }
            fissionDataViewDataMap.get(fissionSaleDataView.getDataType()).add(fissionSaleDataView);
        }
        return fissionDataViewDataMap;
    }


    /**
     * ??????C???????????????
     *
     * @param fissionId
     * @param fissionActivity
     */
    private void userDataHandle(Integer fissionId, FissionActivity fissionActivity) {
        ConcurrentHashMap<String, Object> userDataViewMap = new ConcurrentHashMap(32);
        ConcurrentHashMap<String, Object> noShareDataViewMap = new ConcurrentHashMap(32);
        CyclicBarrier barrier = new CyclicBarrier(USER_TASK_NUM, () -> {
            Map<Integer, List<FissionDataView>> fissionDataViewMap = fissionStatisticsService.getFissionDataViewMap(fissionId);
            dataViewHandle(fissionId, userDataViewMap, fissionDataViewMap, StatDataType.STAT_C.getType(), true);
            dataViewHandle(fissionId, noShareDataViewMap, fissionDataViewMap, StatDataType.STAT_C.getType(), false);
            Date now = new Date();
            FissionUserDataView fissionUserDataViewData = fissionUserDataViewReadMapper.selectByFissionId(fissionId);
            FissionUserDataView result = new FissionUserDataView();
            for (String key : userDataViewMap.keySet()) {
                resultHandle(key, userDataViewMap, result, FissionUserDataView.class);
            }
            //??????
            result.setFissionId(fissionId);
            if (fissionUserDataViewData == null) {
                result.setCreateDt(now);
                fissionUserDataViewWriteMapper.insertSelective(result);
            } else {
                Long updateUserDataVieId = null;
                Long updateDataViewId = null;
                if (!fissionUserDataViewData.getDeleteFlag()) {
                    updateUserDataVieId = fissionUserDataViewData.getId();
                }
                //?????????????????????????????????
                Map<Integer, List<FissionDataView>> fissionDataViewMapList = fissionDataViewMap;
                if (fissionDataViewMapList != null && fissionDataViewMapList.get(StatDataType.STAT_C.getType()) != null) {
                    List<FissionDataView> fissionDataViewList = fissionDataViewMapList.get(StatDataType.STAT_C.getType());
                    for (FissionDataView fdv : fissionDataViewList) {
                        if (!fdv.getDeleteFlag()) {
                            updateDataViewId = fdv.getId();
                            break;
                        }
                    }
                }
                if (updateDataViewId == null || updateUserDataVieId == null) {
                    result.setCreateDt(now);
                    fissionUserDataViewWriteMapper.insertSelective(result);
                } else {
                    result.setId(updateUserDataVieId);
                    fissionUserDataViewWriteMapper.updateByPrimaryKeySelective(result);
                }
            }

            //??????stat_generate??????
            if (fissionActivity != null && fissionActivity.getEndTime().before(now)) {
                FissionActivity fissionActivityUpdate = new FissionActivity();
                fissionActivityUpdate.setUpdateDt(now);
                fissionActivityUpdate.setId(fissionActivity.getId());
                fissionActivityUpdate.setStatGenerate(C_GEN);
                fissionActivityWriteMapper.updateByPrimaryKeySelective(fissionActivityUpdate);
            }
        });
        //????????????
        invokeTask(barrier, () -> putTaskResult("PrizePool", userDataViewMap, fissionStatisticsReadMapper.selectPrizePool(AwardRuleType.C.getType(), fissionId)));
        invokeTask(barrier, () -> putTaskResult("Issued", userDataViewMap, fissionStatisticsReadMapper.selectIssued(fissionId)));
        invokeTask(barrier, () -> putTaskResult("CustomerCount", userDataViewMap, fissionStatisticsReadMapper.selectCustomerCount(fissionId)));
        invokeTask(barrier, () -> putTaskResult("PageView", userDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_1, null, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("UniqueVisitor", userDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(null, fissionId, TASK_ID_1, null, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("ShareCount", userDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_SHARE, fissionId, TASK_ID_1, null, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("SharePageCount", userDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_SHARE, fissionId, TASK_ID_1, null, null, null, PageSourceType.ACTIVITY_PAGE.getType(), null)));
        invokeTask(barrier, () -> putTaskResult("SharePosterCount", userDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_SHARE, fissionId, TASK_ID_1, null, null, null, PageSourceType.POSTER.getType(), null)));
        invokeTask(barrier, () -> putTaskResult("ActivityOrderCount", userDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(null, fissionId, TASK_ID_3, null, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("ActivityProductUserCount", userDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_3, null, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("LiveProductCount", userDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(null, fissionId, TASK_ID_5, null, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("LiveProductUserCount", userDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_5, null, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("LiveUserCount", userDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_4, null, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("SubscribeUserCount", userDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_2, null, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("SubscribeLiveUserCount", userDataViewMap, fissionUserTaskRecordMapper.selectSubscribeLiveUserCountStatistic(fissionId, null, null, null, null)));

        invokeTask(barrier, () -> putTaskResult("ActivityOrderCount", noShareDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(null, fissionId, TASK_ID_3, null, IS_NULL, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("ActivityProductUserCount", noShareDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_3, null, IS_NULL, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("LiveProductCount", noShareDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(null, fissionId, TASK_ID_5, null, IS_NULL, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("LiveProductUserCount", noShareDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_5, null, IS_NULL, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("LiveUserCount", noShareDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_4, null, IS_NULL, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("SubscribeUserCount", noShareDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_2, null, IS_NULL, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("SubscribeLiveUserCount", noShareDataViewMap, fissionUserTaskRecordMapper.selectSubscribeLiveUserCountStatistic(fissionId, null, IS_NULL, null, null)));
    }

    /**
     * ??????????????????
     *
     * @param fissionId
     * @param fissionActivity
     */
    private void saleDataHandle(Integer fissionId, FissionActivity fissionActivity) {
        ConcurrentHashMap<String, Object> saleAllDataViewMap = new ConcurrentHashMap(32);
        //?????????????????????B????????????????????????????????????
        ConcurrentHashMap<String, Object> saleLevelDataViewMap = new ConcurrentHashMap(32);
        //??????????????????????????????????????????
        ConcurrentHashMap<String, Object> saleOtherDataViewMap = new ConcurrentHashMap(32);
        CyclicBarrier barrier = new CyclicBarrier(SALE_TASK_NUM, () -> {
            FissionSaleDataView fissionSaleDataViewData = fissionSaleDataViewReadMapper.selectByFissionId(fissionId);
            Map<Integer, List<FissionDataView>> fissionDataViewMap = fissionStatisticsService.getFissionDataViewMap(fissionId);
            FissionSaleDataView result = new FissionSaleDataView();
            for (String key : saleAllDataViewMap.keySet()) {
                resultHandle(key, saleAllDataViewMap, result, FissionSaleDataView.class);
            }
            Date now = new Date();
            result.setFissionId(fissionId);
            if (fissionSaleDataViewData == null) {
                fissionStatisticsService.insertFissionSaleDataView(result);
            } else {
                Long updateSaleDataVieId = null;
                Long updateDataViewId = null;
                if (!fissionSaleDataViewData.getDeleteFlag()) {
                    updateSaleDataVieId = fissionSaleDataViewData.getId();
                }
                //?????????????????????????????????
                Map<Integer, List<FissionDataView>> fissionDataViewMapList = fissionDataViewMap;
                if (fissionDataViewMapList != null && fissionDataViewMapList.get(StatDataType.STAT_B.getType()) != null) {
                    List<FissionDataView> fissionDataViewList = fissionDataViewMapList.get(StatDataType.STAT_B.getType());
                    for (FissionDataView fdv : fissionDataViewList) {
                        if (!fdv.getDeleteFlag()) {
                            updateDataViewId = fdv.getId();
                            break;
                        }
                    }
                }
                if (updateDataViewId == null || updateSaleDataVieId == null) {
                    fissionStatisticsService.insertFissionSaleDataView(result);
                } else {
                    fissionStatisticsService.updateFissionSaleDataView(updateDataViewId, updateSaleDataVieId, result);
                }
            }

            dataViewHandle(fissionId, saleLevelDataViewMap, fissionDataViewMap, DATA_TYPE_5, false);
            dataViewHandle(fissionId, saleOtherDataViewMap, fissionDataViewMap, DATA_TYPE_6, false);
            //??????stat_generate??????
            if (fissionActivity != null && fissionActivity.getEndTime().before(now)) {
                FissionActivity fissionActivityUpdate = new FissionActivity();
                fissionActivityUpdate.setUpdateDt(now);
                fissionActivityUpdate.setId(fissionActivity.getId());
                fissionActivityUpdate.setStatGenerate(B_GEN);
                fissionActivityWriteMapper.updateByPrimaryKeySelective(fissionActivityUpdate);
            }
        });
        //????????????
        invokeTask(barrier, () -> putTaskResult("PrizePool", saleAllDataViewMap, getPrizePool(fissionId)));
        invokeTask(barrier, () -> putTaskResult("TotalIntegral", saleAllDataViewMap, fissionStatisticsReadMapper.selectTotalIntegral(fissionId)));
        invokeTask(barrier, () -> putTaskResult("FinishSalesmanCount", saleAllDataViewMap, fissionStatisticsReadMapper.selectSaleCount(CompleteTaskStatus.COMPLETE.getStatus(), fissionId)));
        invokeTask(barrier, () -> putTaskResult("SalesmanCount", saleAllDataViewMap, fissionStatisticsReadMapper.selectSaleCount(null, fissionId)));
        invokeTask(barrier, () -> putTaskResult("MinMaxIncome", saleAllDataViewMap, fissionStatisticsReadMapper.selectMinMaxIncome(fissionId)));

        invokeTask(barrier, () -> putTaskResult("PageView", saleAllDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_1, IS_NOT_NULL, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("UniqueVisitor", saleAllDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(null, fissionId, TASK_ID_1, IS_NOT_NULL, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("ShareCount", saleAllDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_SALE, fissionId, TASK_ID_1, IS_NOT_NULL, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("SharePageCount", saleAllDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_SALE, fissionId, TASK_ID_1, IS_NOT_NULL, null, null, PageSourceType.ACTIVITY_PAGE.getType(), null)));
        invokeTask(barrier, () -> putTaskResult("SharePosterCount", saleAllDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_SALE, fissionId, TASK_ID_1, IS_NOT_NULL, null, null, PageSourceType.POSTER.getType(), null)));
        invokeTask(barrier, () -> putTaskResult("ActivityOrderCount", saleAllDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(null, fissionId, TASK_ID_3, IS_NOT_NULL, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("ActivityProductUserCount", saleAllDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_3, IS_NOT_NULL, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("LiveProductCount", saleAllDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(null, fissionId, TASK_ID_5, IS_NOT_NULL, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("LiveProductUserCount", saleAllDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_5, IS_NOT_NULL, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("LiveUserCount", saleAllDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_4, IS_NOT_NULL, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("SubscribeUserCount", saleAllDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_2, IS_NOT_NULL, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("SubscribeLiveUserCount", saleAllDataViewMap, fissionUserTaskRecordMapper.selectSubscribeLiveUserCountStatistic(fissionId, IS_NOT_NULL, null, null, null)));

        invokeTask(barrier, () -> putTaskResult("PageView", saleLevelDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_1, IS_EQUALS_SHARE, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("UniqueVisitor", saleLevelDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(null, fissionId, TASK_ID_1, IS_EQUALS_SHARE, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("ShareCount", saleLevelDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_SALE, fissionId, TASK_ID_1, IS_EQUALS_SHARE, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("SharePageCount", saleLevelDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_SALE, fissionId, TASK_ID_1, IS_EQUALS_SHARE, null, null, PageSourceType.ACTIVITY_PAGE.getType(), null)));
        invokeTask(barrier, () -> putTaskResult("SharePosterCount", saleLevelDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_SALE, fissionId, TASK_ID_1, IS_EQUALS_SHARE, null, null, PageSourceType.POSTER.getType(), null)));
        invokeTask(barrier, () -> putTaskResult("ActivityOrderCount", saleLevelDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(null, fissionId, TASK_ID_3, IS_EQUALS_SHARE, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("ActivityProductUserCount", saleLevelDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_3, IS_EQUALS_SHARE, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("LiveProductCount", saleLevelDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(null, fissionId, TASK_ID_5, IS_EQUALS_SHARE, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("LiveProductUserCount", saleLevelDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_5, IS_EQUALS_SHARE, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("LiveUserCount", saleLevelDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_4, IS_EQUALS_SHARE, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("SubscribeUserCount", saleLevelDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_2, IS_EQUALS_SHARE, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("SubscribeLiveUserCount", saleLevelDataViewMap, fissionUserTaskRecordMapper.selectSubscribeLiveUserCountStatistic(fissionId, IS_EQUALS_SHARE, null, null, null)));

        invokeTask(barrier, () -> putTaskResult("PageView", saleOtherDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_1, IS_NOT_EQUALS_SHARE, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("UniqueVisitor", saleOtherDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(null, fissionId, TASK_ID_1, IS_NOT_EQUALS_SHARE, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("ShareCount", saleOtherDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_SALE, fissionId, TASK_ID_1, IS_NOT_EQUALS_SHARE, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("SharePageCount", saleOtherDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_SALE, fissionId, TASK_ID_1, IS_NOT_EQUALS_SHARE, null, null, PageSourceType.ACTIVITY_PAGE.getType(), null)));
        invokeTask(barrier, () -> putTaskResult("SharePosterCount", saleOtherDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_SALE, fissionId, TASK_ID_1, IS_NOT_EQUALS_SHARE, null, null, PageSourceType.POSTER.getType(), null)));
        invokeTask(barrier, () -> putTaskResult("ActivityOrderCount", saleOtherDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(null, fissionId, TASK_ID_3, IS_NOT_EQUALS_SHARE, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("ActivityProductUserCount", saleOtherDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_3, IS_NOT_EQUALS_SHARE, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("LiveProductCount", saleOtherDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(null, fissionId, TASK_ID_5, IS_NOT_EQUALS_SHARE, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("LiveProductUserCount", saleOtherDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_5, IS_NOT_EQUALS_SHARE, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("LiveUserCount", saleOtherDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_4, IS_NOT_EQUALS_SHARE, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("SubscribeUserCount", saleOtherDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_2, IS_NOT_EQUALS_SHARE, null, null, null, null)));
        invokeTask(barrier, () -> putTaskResult("SubscribeLiveUserCount", saleOtherDataViewMap, fissionUserTaskRecordMapper.selectSubscribeLiveUserCountStatistic(fissionId, IS_NOT_EQUALS_SHARE, null, null, null)));
    }

    /**
     * ??????data_type???????????????
     *
     * @param fissionId
     * @param fissionActivity
     */
    private void dataHandleByDataType(Integer fissionId, FissionActivity fissionActivity, Integer statGenerate, Integer dataType, Integer channel) {
        ConcurrentHashMap<String, Object> noSaleDataViewMap = new ConcurrentHashMap(32);
        ConcurrentHashMap<String, Object> noShareDataViewMap = new ConcurrentHashMap(32);
        CyclicBarrier barrier = new CyclicBarrier(CHANNEL_TASK_NUM, () -> {
            Map<Integer, List<FissionDataView>> fissionDataViewMap = fissionStatisticsService.getFissionDataViewMap(fissionId);
            dataViewHandle(fissionId, noSaleDataViewMap, fissionDataViewMap, dataType, true);
            dataViewHandle(fissionId, noShareDataViewMap, fissionDataViewMap, dataType, false);
            Date now = new Date();
            //??????stat_generate??????
            if (fissionActivity != null && fissionActivity.getEndTime().before(now)) {
                FissionActivity fissionActivityUpdate = new FissionActivity();
                fissionActivityUpdate.setUpdateDt(now);
                fissionActivityUpdate.setId(fissionActivity.getId());
                fissionActivityUpdate.setStatGenerate(statGenerate);
                fissionActivityWriteMapper.updateByPrimaryKeySelective(fissionActivityUpdate);
            }
        });
        //????????????
        invokeTask(barrier, () -> putTaskResult("PageView", noSaleDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_1, null, null, null, null, channel)));
        invokeTask(barrier, () -> putTaskResult("UniqueVisitor", noSaleDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(null, fissionId, TASK_ID_1, null, null, null, null, channel)));
        if (channel == 2) {
            invokeTask(barrier, () -> putTaskResult("ShareCount", noSaleDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_SALE, fissionId, TASK_ID_1, null, null, null, null, channel)));
            invokeTask(barrier, () -> putTaskResult("SharePageCount", noSaleDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_SALE, fissionId, TASK_ID_1, null, null, null, PageSourceType.ACTIVITY_PAGE.getType(), channel)));
            invokeTask(barrier, () -> putTaskResult("SharePosterCount", noSaleDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_SALE, fissionId, TASK_ID_1, null, null, null, PageSourceType.POSTER.getType(), channel)));

        } else {
            invokeTask(barrier, () -> putTaskResult("ShareCount", noSaleDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_SHARE, fissionId, TASK_ID_1, null, null, null, null, channel)));
            invokeTask(barrier, () -> putTaskResult("SharePageCount", noSaleDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_SHARE, fissionId, TASK_ID_1, null, null, null, PageSourceType.ACTIVITY_PAGE.getType(), channel)));
            invokeTask(barrier, () -> putTaskResult("SharePosterCount", noSaleDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_SHARE, fissionId, TASK_ID_1, null, null, null, PageSourceType.POSTER.getType(), channel)));
        }
        invokeTask(barrier, () -> putTaskResult("ActivityOrderCount", noSaleDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(null, fissionId, TASK_ID_3, null, null, null, null, channel)));
        invokeTask(barrier, () -> putTaskResult("ActivityProductUserCount", noSaleDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_3, null, null, null, null, channel)));
        invokeTask(barrier, () -> putTaskResult("LiveProductCount", noSaleDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(null, fissionId, TASK_ID_5, null, null, null, null, channel)));
        invokeTask(barrier, () -> putTaskResult("LiveProductUserCount", noSaleDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_5, null, null, null, null, channel)));
        invokeTask(barrier, () -> putTaskResult("LiveUserCount", noSaleDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_4, null, null, null, null, channel)));
        invokeTask(barrier, () -> putTaskResult("SubscribeUserCount", noSaleDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_2, null, null, null, null, channel)));
        invokeTask(barrier, () -> putTaskResult("SubscribeLiveUserCount", noSaleDataViewMap, fissionUserTaskRecordMapper.selectSubscribeLiveUserCountStatistic(fissionId, null, null, null, channel)));

        invokeTask(barrier, () -> putTaskResult("PageView", noShareDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_1, null, IS_NULL, null, null, channel)));
        invokeTask(barrier, () -> putTaskResult("UniqueVisitor", noShareDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(null, fissionId, TASK_ID_1, null, IS_NULL, null, null, channel)));
        invokeTask(barrier, () -> putTaskResult("ActivityOrderCount", noShareDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(null, fissionId, TASK_ID_3, null, IS_NULL, null, null, channel)));
        invokeTask(barrier, () -> putTaskResult("ActivityProductUserCount", noShareDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_3, null, IS_NULL, null, null, channel)));
        invokeTask(barrier, () -> putTaskResult("LiveProductCount", noShareDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(null, fissionId, TASK_ID_5, null, IS_NULL, null, null, channel)));
        invokeTask(barrier, () -> putTaskResult("LiveProductUserCount", noShareDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_5, null, IS_NULL, null, null, channel)));
        invokeTask(barrier, () -> putTaskResult("LiveUserCount", noShareDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_4, null, IS_NULL, null, null, channel)));
        invokeTask(barrier, () -> putTaskResult("SubscribeUserCount", noShareDataViewMap, fissionUserTaskRecordMapper.selectStatisticData(DISTINCT_TYPE_USER, fissionId, TASK_ID_2, null, IS_NULL, null, null, channel)));
        invokeTask(barrier, () -> putTaskResult("SubscribeLiveUserCount", noShareDataViewMap, fissionUserTaskRecordMapper.selectSubscribeLiveUserCountStatistic(fissionId, null, IS_NULL, null, channel)));
    }

    /**
     * ??????data_view?????????
     *
     * @param dataViewMap
     * @param fissionDataViewMap
     * @param dataType
     * @param isShare
     */
    private void dataViewHandle(Integer fissionId, ConcurrentHashMap<String, Object> dataViewMap, Map<Integer, List<FissionDataView>> fissionDataViewMap, Integer dataType, boolean isShare) {
        FissionDataView result = new FissionDataView();
        for (String key : dataViewMap.keySet()) {
            resultHandle(key, dataViewMap, result, FissionDataView.class);
        }
        Date now = new Date();
        //??????????????????
        result.setFissionId(fissionId);
        Map<Integer, List<FissionDataView>> fissionDataViewMapList = fissionDataViewMap;
        if (fissionDataViewMapList == null || fissionDataViewMapList.get(dataType) == null) {
            result.setCreateDt(now);
            result.setDataType(dataType);
            result.setShare(isShare);
            fissionDataViewWriteMapper.insertSelective(result);
        } else {
            Long updateDataViewId = null;
            //?????????????????????????????????
            List<FissionDataView> fissionDataViewList = fissionDataViewMapList.get(dataType);
            for (FissionDataView fdv : fissionDataViewList) {
                if (!fdv.getDeleteFlag() && fdv.isShare() == isShare) {
                    updateDataViewId = fdv.getId();
                    break;
                }
            }
            result.setDataType(dataType);
            if (updateDataViewId == null) {
                result.setCreateDt(now);
                result.setShare(isShare);
                fissionDataViewWriteMapper.insertSelective(result);
            } else {
                result.setId(updateDataViewId);
                result.setUpdateDt(now);
                result.setShare(isShare);
                fissionDataViewWriteMapper.updateByPrimaryKeySelective(result);
            }
        }

    }

    /**
     * ?????????????????????PrizePool
     *
     * @param fissionId
     * @return
     */
    private FissionStatisticBigDecimalDto getPrizePool(Integer fissionId) {
        FissionStatisticBigDecimalDto fissionStatisticBigDecimalDto = fissionStatisticsReadMapper.selectPrizePool(AwardRuleType.B.getType(), fissionId);
        if (fissionStatisticBigDecimalDto == null) {
            fissionStatisticBigDecimalDto = new FissionStatisticBigDecimalDto();
            fissionStatisticBigDecimalDto.setValue(BigDecimal.ZERO);
            return fissionStatisticBigDecimalDto;
        }
        FissionStatisticIntDto fissionStatisticIntDto = fissionSalesOrderReadMapper.selectSaleJoinCounts(fissionId);

        FissionAwardRule fissionAwardRuleB = null;
        List<FissionAwardRule> fissionAwardRuleList = fissionActivityService.getAwardRuleListByFissionId(fissionStatisticBigDecimalDto.getFissionId(), GlobalConstants.FISSION_AWARD_RULE_TYPE_B);
        if (!fissionAwardRuleList.isEmpty()) {
            fissionAwardRuleB = fissionAwardRuleList.get(0);
        }
        if (fissionAwardRuleB == null) {
            fissionAwardRuleB = new FissionAwardRule();
            fissionAwardRuleB.setPersonMoney(BigDecimal.ZERO);
            fissionAwardRuleB.setPrizePool(BigDecimal.ZERO);
        }
        BigDecimal joinCount = BigDecimal.ZERO;
        if (fissionStatisticIntDto != null) {
            joinCount = new BigDecimal(fissionStatisticIntDto.getValue());
        }
        BigDecimal prizePool = fissionAwardRuleB.getPrizePool().add(fissionAwardRuleB.getPersonMoney().multiply(joinCount));
        fissionStatisticBigDecimalDto.setValue(prizePool);
        return fissionStatisticBigDecimalDto;
    }

    /**
     * ???dataViewMap????????????
     *
     * @param key
     * @param dataViewMap
     * @param taskResult
     */
    private void putTaskResult(String key, ConcurrentHashMap<String, Object> dataViewMap, Object taskResult) {
        if (taskResult != null) {
            dataViewMap.put(key, taskResult);
        }
    }

    /**
     * ????????????????????????
     *
     * @param barrier
     * @param statisticsTask
     */
    private void invokeTask(CyclicBarrier barrier, StatisticsTask statisticsTask) {
//        executorService.execute(new StatisticsTaskThread(barrier, statisticsTask));
        Thread thread = new Thread(new StatisticsTaskThread(barrier, statisticsTask));
        thread.start();
    }

    /**
     * ???????????????????????????????????????
     *
     * @param methodName
     * @param dataViewMap
     * @param obj
     * @param clazz
     */
    private void resultHandle(String methodName, ConcurrentHashMap<String, Object> dataViewMap, Object obj, Class clazz) {
        Object object = dataViewMap.get(methodName);
        if (object instanceof FissionStatisticBigDecimalDto) {
            FissionStatisticBigDecimalDto stat = (FissionStatisticBigDecimalDto) object;
            if (stat != null && stat.getFissionId() != null) {
                valueHandle(stat.getFissionId(), stat.getValue() == null ? BigDecimal.ZERO : stat.getValue(), methodName, obj, clazz);
            }
        } else if (object instanceof FissionStatisticIntDto) {
            FissionStatisticIntDto stat = (FissionStatisticIntDto) object;
            if (stat != null && stat.getFissionId() != null) {
                valueHandle(stat.getFissionId(), stat.getValue() == null ? 0 : stat.getValue(), methodName, obj, clazz);
            }
        } else if (object instanceof FissionActivityIncomeDto) {
            FissionActivityIncomeDto fissionActivityIncomeDto = (FissionActivityIncomeDto) object;
            if (fissionActivityIncomeDto != null && fissionActivityIncomeDto.getFissionId() == null) {
                valueHandle(fissionActivityIncomeDto.getFissionId(), fissionActivityIncomeDto.isFinished() == 0 ? fissionActivityIncomeDto.getMaxEstimatedIncome() : fissionActivityIncomeDto.getMaxRealIncome(), "PrizeMax", obj, clazz);
                valueHandle(fissionActivityIncomeDto.getFissionId(), fissionActivityIncomeDto.isFinished() == 0 ? fissionActivityIncomeDto.getMinEstimatedIncome() : fissionActivityIncomeDto.getMinRealIncome(), "PrizeMin", obj, clazz);
            }
        }
    }

    /**
     * ????????????????????????
     *
     * @param fissionId
     * @param value
     * @param methodName
     * @param obj
     * @param clazz
     */
    private void valueHandle(Integer fissionId, Object value, String methodName, Object obj, Class clazz) {
        try {
            Method method = null;
            Method[] methods = clazz.getDeclaredMethods();
            String mn = "set" + methodName;
            for (Method methodArray : methods) {
                if (mn.equals(methodArray.getName())) {
                    method = methodArray;
                    break;
                }
            }
            //????????????????????? ????????????
            if (method == null && clazz.getSuperclass() != null) {
                methods = clazz.getSuperclass().getDeclaredMethods();
                for (Method methodArray : methods) {
                    if (mn.equals(methodArray.getName())) {
                        method = methodArray;
                        break;
                    }
                }
            }
            if (method == null) {
                return;
            }
            //??????
            method.invoke(obj, value);
        } catch (Exception e) {
            CommonLogUtil.addError("????????????-????????????error", "valueHandle", e);
        }
    }
}
