package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tuanche.directselling.api.FissionDataViewService;
import com.tuanche.directselling.api.FissionStatisticsService;
import com.tuanche.directselling.dto.FissionSaleDto;
import com.tuanche.directselling.dto.FissionStatisticBigDecimalDto;
import com.tuanche.directselling.dto.FissionUserRewardRankDto;
import com.tuanche.directselling.dto.FissionUserTaskRecordCountDto;
import com.tuanche.directselling.dto.FissionUserTaskRecordStatDto;
import com.tuanche.directselling.enums.WithdrawalState;
import com.tuanche.directselling.mapper.read.directselling.FissionActivityReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FissionDataViewReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FissionSaleDataViewReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FissionSaleReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FissionSaleTaskStatisticsReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FissionStatisticsReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FissionUserDataViewReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FissionUserRewardReadMapper;
import com.tuanche.directselling.mapper.sharding.FissionUserTaskRecordMapper;
import com.tuanche.directselling.model.FissionActivity;
import com.tuanche.directselling.model.FissionDataView;
import com.tuanche.directselling.model.FissionSale;
import com.tuanche.directselling.model.FissionSaleDataView;
import com.tuanche.directselling.model.FissionUserDataView;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.FissionActivityRankVo;
import com.tuanche.directselling.vo.FissionAllDealerRankVo;
import com.tuanche.directselling.vo.FissionAllSaleRankVo;
import com.tuanche.directselling.vo.FissionSaleRewardVo;
import com.tuanche.directselling.vo.FissionUserRewardRankVo;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.api.UserBaseService;
import com.tuanche.manubasecenter.model.CsCompany;
import com.tuanche.manubasecenter.model.CsUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 裂变活动B/C数据概览service
 * @Author: ZhangYiXin
 * @Date: 2020/9/23 12:03
 * @Version 1.0
 **/
@Service
public class FissionDataViewServiceImpl implements FissionDataViewService {
    @Reference
    CompanyBaseService companyBaseService;
    @Autowired
    private FissionDataViewReadMapper fissionDataViewReadMapper;
    @Autowired
    private FissionSaleDataViewReadMapper fissionSaleDataViewMapper;
    @Autowired
    private FissionUserDataViewReadMapper fissionUserDataViewMapper;
    @Autowired
    private FissionSaleReadMapper fissionSaleReadMapper;
    @Autowired
    private FissionActivityReadMapper fissionActivityReadMapper;
    @Autowired
    private FissionSaleTaskStatisticsReadMapper fissionSaleTaskStatisticsReadMapper;
    @Reference
    private UserBaseService userBaseService;
    @Autowired
    private FissionUserRewardReadMapper fissionUserRewardReadMapper;
    @Autowired
    private FissionStatisticsReadMapper fissionStatisticsReadMapper;
    @Autowired
    private FissionUserTaskRecordMapper fissionUserTaskRecordMapper;
    @Reference
    private FissionStatisticsService fissionStatisticsService;

    @Override
    public FissionSaleDataView getBusinessFissionDataViewByFissionId(Integer fissionId) {
        FissionSaleDataView fissionSaleDataView = fissionSaleDataViewMapper.selectByFissionId(fissionId);
        if (fissionSaleDataView != null) {
            //财务已审核 或 已打款金额
            FissionStatisticBigDecimalDto statisticBigDecimalDto = fissionStatisticsReadMapper.selectPrizeActualByFissionId(fissionId, null, null);
            if (statisticBigDecimalDto == null) {
                fissionSaleDataView.setPrizeActual(BigDecimal.ZERO);
            } else {
                fissionSaleDataView.setPrizeActual(statisticBigDecimalDto.getValue());
            }
        }
        return fissionSaleDataView;
    }

    @Override
    public FissionUserDataView getCustomerFissionDataViewByFissionId(Integer fissionId) {
        FissionUserDataView fissionUserDataView = fissionUserDataViewMapper.selectByFissionId(fissionId);
        if (fissionUserDataView != null) {
            if (fissionUserDataView.getCustomerCount() != 0) {
                //人均奖金
                fissionUserDataView.setAveragePrize(fissionUserDataView.getIssued().divide(new BigDecimal(fissionUserDataView.getCustomerCount()), 2, BigDecimal.ROUND_HALF_UP));
            } else {
                fissionUserDataView.setAveragePrize(new BigDecimal(0));
            }
        }
        return fissionUserDataView;
    }

    @Override
    public List<FissionDataView> getFissionDataViewByFissionIdAndTypeAndIsShare(Integer fissionId, Integer dataType, Boolean isShare) {
        return fissionDataViewReadMapper.selectByFissionId(fissionId, dataType, isShare);
    }

    @Override
    public List<FissionActivityRankVo> getBusinessRankSale(Integer fissionId) {
        FissionActivity fissionActivity = fissionActivityReadMapper.selectByPrimaryKey(fissionId);
        if (fissionActivity == null) {
            return null;
        }
        List<FissionActivityRankVo> saleRankVo = new ArrayList<>();
        List<FissionSale> saleRank = fissionSaleReadMapper.selectFissionSaleRank(fissionId);
        Date now = new Date();
        //活动状态
        boolean isInProgress = false;
        if (fissionActivity.getStartTime().before(now) && fissionActivity.getEndTime().after(now)) {
            isInProgress = true;
        }
        FissionActivityRankVo fissionActivityRankVo = null;
        int rank = 1;
        BigDecimal zero = new BigDecimal(0);
        for (FissionSale fissionSale : saleRank) {
            fissionActivityRankVo = new FissionActivityRankVo();
            fissionActivityRankVo.setEstimatedIncome(fissionSale.getEstimatedIncome());
            fissionActivityRankVo.setRealIncome(fissionSale.getRealIncome());
            //是否在活动中
            if (isInProgress) {
                fissionActivityRankVo.setIncome(fissionSale.getEstimatedIncome() == null ? zero : fissionSale.getEstimatedIncome());
            } else {
                fissionActivityRankVo.setIncome(fissionSale.getRealIncome() == null ? zero : fissionSale.getRealIncome());
            }
            fissionActivityRankVo.setIntegral(fissionSale.getTaskIntegral());
            fissionActivityRankVo.setName(fissionSale.getSaleName());
            fissionActivityRankVo.setRank(rank);
            saleRankVo.add(fissionActivityRankVo);
            rank++;
        }
        return saleRankVo;
    }

    @Override
    public PageResult getBusinessRankAllSale(int page, int limit, Integer fissionId, String companyName) {
        List<Integer> csDealerIds = new ArrayList<>();
        if (StringUtils.isNotBlank(companyName)) {
            csDealerIds = companyBaseService.getCsCompanyIdByName(companyName);
        }
        return getBusinessRankAllSale(page, limit, fissionId, csDealerIds, null);
    }

    @Override
    public PageResult getBusinessRankAllSale(int page, int limit, Integer fissionId, List<Integer> csDealerIds, List<Integer> saleIds) {
        PageResult<FissionAllSaleRankVo> pageResult = new PageResult<>();
        PageHelper.startPage(page, limit);
        List<FissionSale> fissionSales = fissionSaleReadMapper.selectFissionAllSaleRank(fissionId, csDealerIds, saleIds);
        if (fissionSales.isEmpty()) {
            return pageResult;
        }
        List<FissionAllSaleRankVo> fissionAllSaleRankVos = new ArrayList<>();
        FissionAllSaleRankVo fissionAllSaleRankVo = null;
        List<Integer> saleList = new ArrayList<>();
        List<Integer> dealerIds = new ArrayList<>();
        List<String> saleWxUnionIdList = new ArrayList<>();
        for (FissionSale fissionSale : fissionSales) {
            saleList.add(fissionSale.getSaleId());
            dealerIds.add(fissionSale.getDealerId());
            saleWxUnionIdList.add(fissionSale.getSaleWxUnionId());
        }
        Map<Integer, CsCompany> csCompanyMap = companyBaseService.getCompanyMap(dealerIds);
        List<FissionUserTaskRecordCountDto> saleTaskStatistics = fissionUserTaskRecordMapper.selectUserTaskRecordByFissionIdAndSaleWxUnionIds(fissionId, saleWxUnionIdList);
        Map<String, Map<Integer, FissionUserTaskRecordCountDto>> saleTaskMap = new HashMap<>((int) (saleTaskStatistics.size() / 0.75f) + 1);
        //查询结果转为Map<saleWxUnionId,Map<taskId,FissionUserTaskRecordCountDto>>
        for (FissionUserTaskRecordCountDto statistics : saleTaskStatistics) {
            String saleWxUnionId = statistics.getSaleWxUnionId();
            Map<Integer, FissionUserTaskRecordCountDto> statisticsMap = saleTaskMap.get(saleWxUnionId);
            if (statisticsMap == null) {
                statisticsMap = new HashMap<>(8);
                statisticsMap.put(statistics.getTaskId(), statistics);
                saleTaskMap.put(saleWxUnionId, statisticsMap);
            } else {
                statisticsMap.put(statistics.getTaskId(), statistics);
            }
        }
        Map<Integer, CsUser> csUserMap = userBaseService.getCsUserByIds(saleList);
        int rank = 1;
        //转成fissionAllSaleRankVos
        for (FissionSale fissionSale : fissionSales) {
            Map<Integer, FissionUserTaskRecordCountDto> statisticsMap = saleTaskMap.get(fissionSale.getSaleWxUnionId());
            fissionAllSaleRankVo = new FissionAllSaleRankVo();
            BeanUtils.copyProperties(fissionSale, fissionAllSaleRankVo);
            if (statisticsMap != null) {
                fissionAllSaleRankVo.setPageView(statisticsMap.get(1) != null ? statisticsMap.get(1).getCount() : 0);
                fissionAllSaleRankVo.setSubscribeLive(statisticsMap.get(2) != null ? statisticsMap.get(2).getCount() : 0);
                fissionAllSaleRankVo.setActivityPageProduct(statisticsMap.get(3) != null ? statisticsMap.get(3).getCount() : 0);
                fissionAllSaleRankVo.setLiveView(statisticsMap.get(4) != null ? statisticsMap.get(4).getCount() : 0);
                fissionAllSaleRankVo.setLivePageProduct(statisticsMap.get(5) != null ? statisticsMap.get(5).getCount() : 0);
            } else {
                fissionAllSaleRankVo.setPageView(0);
                fissionAllSaleRankVo.setSubscribeLive(0);
                fissionAllSaleRankVo.setActivityPageProduct(0);
                fissionAllSaleRankVo.setLiveView(0);
                fissionAllSaleRankVo.setLivePageProduct(0);
            }
            CsCompany csCompany = csCompanyMap.get(fissionSale.getDealerId());
            fissionAllSaleRankVo.setDealerName(csCompany == null ? "" : csCompany.getCompanyName());
            fissionAllSaleRankVo.setRank(rank + ((page - 1) * limit));
            CsUser csUser = csUserMap.get(fissionSale.getSaleId());
            fissionAllSaleRankVo.setSalePhone(csUser == null ? "" : csUser.getUphone());
            fissionAllSaleRankVos.add(fissionAllSaleRankVo);
            rank++;
        }
        PageInfo<FissionAllSaleRankVo> pageInfo = new PageInfo(fissionSales);
        pageResult.setCount(pageInfo.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode());
        pageResult.setMsg(StatusCodeEnum.SUCCESS.getText());
        pageResult.setPage(page);
        pageResult.setLimit(limit);
        pageResult.setData(fissionAllSaleRankVos);
        return pageResult;
    }

    @Override
    public List<FissionAllSaleRankVo> getBusinessRankAllSale(Integer fissionId, String companyName) {
        List<FissionAllSaleRankVo> fissionAllSaleRankVos = new ArrayList<>();
        List<Integer> csDealerIds = new ArrayList<>();
        if (StringUtils.isNotBlank(companyName)) {
            csDealerIds = companyBaseService.getCsCompanyIdByName(companyName);
        }
        List<FissionSale> fissionSales = fissionSaleReadMapper.selectFissionAllSaleRank(fissionId, csDealerIds, null);
        if (fissionSales.isEmpty()) {
            return fissionAllSaleRankVos;
        }
        FissionAllSaleRankVo fissionAllSaleRankVo = null;
        List<Integer> saleIds = new ArrayList<>();
        List<Integer> dealerIds = new ArrayList<>();
        List<String> saleWxUnionIdList = new ArrayList<>();
        for (FissionSale fissionSale : fissionSales) {
            saleIds.add(fissionSale.getSaleId());
            dealerIds.add(fissionSale.getDealerId());
            saleWxUnionIdList.add(fissionSale.getSaleWxUnionId());
        }
        Map<Integer, CsCompany> csCompanyMap = companyBaseService.getCompanyMap(dealerIds);
        List<FissionUserTaskRecordCountDto> saleTaskStatistics = fissionUserTaskRecordMapper.selectUserTaskRecordByFissionIdAndSaleWxUnionIds(fissionId, saleWxUnionIdList);
        Map<String, Map<Integer, FissionUserTaskRecordCountDto>> saleTaskMap = new HashMap<>((int) (saleTaskStatistics.size() / 0.75f) + 1);
        //查询结果转为Map<saleWxUnionId,Map<taskId,FissionUserTaskRecordCountDto>>
        for (FissionUserTaskRecordCountDto statistics : saleTaskStatistics) {
            String saleWxUnionId = statistics.getSaleWxUnionId();
            Map<Integer, FissionUserTaskRecordCountDto> statisticsMap = saleTaskMap.get(saleWxUnionId);
            if (statisticsMap == null) {
                statisticsMap = new HashMap<>(8);
                statisticsMap.put(statistics.getTaskId(), statistics);
                saleTaskMap.put(saleWxUnionId, statisticsMap);
            } else {
                statisticsMap.put(statistics.getTaskId(), statistics);
            }
        }
        Map<Integer, CsUser> csUserMap = userBaseService.getCsUserByIds(saleIds);
        int rank = 1;
        //转成fissionAllSaleRankVos
        for (FissionSale fissionSale : fissionSales) {
            Map<Integer, FissionUserTaskRecordCountDto> statisticsMap = saleTaskMap.get(fissionSale.getSaleWxUnionId());
            fissionAllSaleRankVo = new FissionAllSaleRankVo();
            BeanUtils.copyProperties(fissionSale, fissionAllSaleRankVo);
            if (statisticsMap != null) {
                fissionAllSaleRankVo.setPageView(statisticsMap.get(1) != null ? statisticsMap.get(1).getCount() : 0);
                fissionAllSaleRankVo.setSubscribeLive(statisticsMap.get(2) != null ? statisticsMap.get(2).getCount() : 0);
                fissionAllSaleRankVo.setActivityPageProduct(statisticsMap.get(3) != null ? statisticsMap.get(3).getCount() : 0);
                fissionAllSaleRankVo.setLiveView(statisticsMap.get(4) != null ? statisticsMap.get(4).getCount() : 0);
                fissionAllSaleRankVo.setLivePageProduct(statisticsMap.get(5) != null ? statisticsMap.get(5).getCount() : 0);
            } else {
                fissionAllSaleRankVo.setPageView(0);
                fissionAllSaleRankVo.setSubscribeLive(0);
                fissionAllSaleRankVo.setActivityPageProduct(0);
                fissionAllSaleRankVo.setLiveView(0);
                fissionAllSaleRankVo.setLivePageProduct(0);
            }
            CsCompany csCompany = csCompanyMap.get(fissionSale.getDealerId());
            fissionAllSaleRankVo.setDealerName(csCompany == null ? "" : csCompany.getCompanyName());
            fissionAllSaleRankVo.setRank(rank);
            fissionAllSaleRankVo.setSalePhone(csUserMap.get(fissionSale.getSaleId()).getUphone());
            fissionAllSaleRankVos.add(fissionAllSaleRankVo);
            rank++;
        }
        return fissionAllSaleRankVos;
    }

    @Override
    public List<FissionActivityRankVo> getBusinessRankDealer(Integer fissionId) {
        FissionActivity fissionActivity = fissionActivityReadMapper.selectByPrimaryKey(fissionId);
        if (fissionActivity == null) {
            return null;
        }
        Date now = new Date();
        //是否在活动中
        boolean isInProgress = false;
        if (fissionActivity.getStartTime().before(now) && fissionActivity.getEndTime().after(now)) {
            isInProgress = true;
        }


        List<FissionActivityRankVo> dealerRankVo = new ArrayList<>();
        List<FissionSale> dealerRank = fissionSaleReadMapper.selectFissionDealerRank(fissionId);
        List<Integer> dealerIds = new ArrayList<>();
        if (!dealerRank.isEmpty()) {
            for (FissionSale fSale : dealerRank) {
                dealerIds.add(fSale.getDealerId());
            }
        }
        Map<Integer, CsCompany> csCompanyMap = companyBaseService.getCompanyMap(dealerIds);
        FissionActivityRankVo fissionActivityRankVo = null;
        int rank = 1;
        BigDecimal zero = new BigDecimal(0);
        //转换为List<FissionActivityRankVo>
        for (FissionSale fissionSale : dealerRank) {
            fissionActivityRankVo = new FissionActivityRankVo();
            fissionActivityRankVo.setEstimatedIncome(fissionSale.getEstimatedIncome());
            fissionActivityRankVo.setRealIncome(fissionSale.getRealIncome());
            if (isInProgress) {
                fissionActivityRankVo.setIncome(fissionSale.getEstimatedIncome() == null ? zero : fissionSale.getEstimatedIncome());
            } else {
                fissionActivityRankVo.setIncome(fissionSale.getRealIncome() == null ? zero : fissionSale.getRealIncome());
            }
            fissionActivityRankVo.setIntegral(fissionSale.getTaskIntegral());
            CsCompany csCompany = csCompanyMap.get(fissionSale.getDealerId());
            if (csCompany != null) {
                fissionActivityRankVo.setName(csCompany.getCompanyName());
            }
            fissionActivityRankVo.setRank(rank);
            dealerRankVo.add(fissionActivityRankVo);
            rank++;
        }
        return dealerRankVo;
    }

    @Override
    public PageResult getBusinessRankAllDealer(int page, int limit, Integer fissionId, String companyName) {
        PageResult<FissionAllDealerRankVo> pageResult = new PageResult<>();
        List<Integer> csDealerIds = new ArrayList<>();
        if (StringUtils.isNotBlank(companyName)) {
            csDealerIds = companyBaseService.getCsCompanyIdByName(companyName);
        }
        PageHelper.startPage(page, limit);
        List<FissionSaleDto> fissionDealers = fissionSaleReadMapper.selectFissionAllDealerRank(fissionId, csDealerIds);
        if (fissionDealers.isEmpty()) {
            return pageResult;
        }
        List<FissionAllDealerRankVo> fissionAllDealerRankVos = new ArrayList<>();
        FissionAllDealerRankVo fissionAllDealerRankVo = null;
        List<Integer> dealerIds = new ArrayList<>();
        for (FissionSaleDto fissionSale : fissionDealers) {
            dealerIds.add(fissionSale.getDealerId());
        }
        Map<Integer, CsCompany> csCompanyMap = companyBaseService.getCompanyMap(dealerIds);
        List<FissionUserTaskRecordCountDto> dealerTaskStatistics = fissionUserTaskRecordMapper.selectUserTaskRecordByFissionIdAndDealerIdIds(fissionId, dealerIds);
        Map<Integer, Map<Integer, FissionUserTaskRecordCountDto>> dealerTaskMap = new HashMap<>((int) (dealerTaskStatistics.size() / 0.75f) + 1);
        //查询结果转为Map<dealerId,Map<taskId,FissionUserTaskRecordCountDto>>
        for (FissionUserTaskRecordCountDto statistics : dealerTaskStatistics) {
            int dealerId = statistics.getDealerId();
            Map<Integer, FissionUserTaskRecordCountDto> statisticsMap = dealerTaskMap.get(dealerId);
            if (statisticsMap == null) {
                statisticsMap = new HashMap<>(8);
                statisticsMap.put(statistics.getTaskId(), statistics);
                dealerTaskMap.put(dealerId, statisticsMap);
            } else {
                statisticsMap.put(statistics.getTaskId(), statistics);
            }
        }
        int rank = 1;
        //转成fissionAllDealerRankVos
        for (FissionSale fissionDealer : fissionDealers) {
            Map<Integer, FissionUserTaskRecordCountDto> statisticsMap = dealerTaskMap.get(fissionDealer.getDealerId());
            fissionAllDealerRankVo = new FissionAllDealerRankVo();
            BeanUtils.copyProperties(fissionDealer, fissionAllDealerRankVo);
            if (statisticsMap != null) {
                fissionAllDealerRankVo.setPageView(statisticsMap.get(1) != null ? statisticsMap.get(1).getCount() : 0);
                fissionAllDealerRankVo.setSubscribeLive(statisticsMap.get(2) != null ? statisticsMap.get(2).getCount() : 0);
                fissionAllDealerRankVo.setActivityPageProduct(statisticsMap.get(3) != null ? statisticsMap.get(3).getCount() : 0);
                fissionAllDealerRankVo.setLiveView(statisticsMap.get(4) != null ? statisticsMap.get(4).getCount() : 0);
                fissionAllDealerRankVo.setLivePageProduct(statisticsMap.get(5) != null ? statisticsMap.get(5).getCount() : 0);
            } else {
                fissionAllDealerRankVo.setPageView(0);
                fissionAllDealerRankVo.setSubscribeLive(0);
                fissionAllDealerRankVo.setActivityPageProduct(0);
                fissionAllDealerRankVo.setLiveView(0);
                fissionAllDealerRankVo.setLivePageProduct(0);
            }
            CsCompany csCompany = csCompanyMap.get(fissionDealer.getDealerId());
            fissionAllDealerRankVo.setDealerName(csCompany == null ? "" : csCompany.getCompanyName());
            fissionAllDealerRankVo.setRank(rank + ((page - 1) * limit));
            fissionAllDealerRankVos.add(fissionAllDealerRankVo);
            rank++;
        }
        PageInfo<FissionAllSaleRankVo> pageInfo = new PageInfo(fissionDealers);
        pageResult.setCount(pageInfo.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode());
        pageResult.setMsg(StatusCodeEnum.SUCCESS.getText());
        pageResult.setPage(page);
        pageResult.setLimit(limit);
        pageResult.setData(fissionAllDealerRankVos);
        return pageResult;
    }

    @Override
    public List<FissionAllDealerRankVo> getBusinessRankAllDealer(Integer fissionId, String companyName) {
        List<FissionAllDealerRankVo> fissionAllDealerRankVos = new ArrayList<>();
        List<Integer> csDealerIds = new ArrayList<>();
        if (StringUtils.isNotBlank(companyName)) {
            csDealerIds = companyBaseService.getCsCompanyIdByName(companyName);
        }
        List<FissionSaleDto> fissionDealers = fissionSaleReadMapper.selectFissionAllDealerRank(fissionId, csDealerIds);
        if (fissionDealers.isEmpty()) {
            return fissionAllDealerRankVos;
        }
        FissionAllDealerRankVo fissionAllDealerRankVo = null;
        List<Integer> dealerIds = new ArrayList<>();
        for (FissionSaleDto fissionSale : fissionDealers) {
            dealerIds.add(fissionSale.getDealerId());
        }
        Map<Integer, CsCompany> csCompanyMap = companyBaseService.getCompanyMap(dealerIds);
        List<FissionUserTaskRecordCountDto> dealerTaskStatistics = fissionUserTaskRecordMapper.selectUserTaskRecordByFissionIdAndDealerIdIds(fissionId, dealerIds);
        Map<Integer, Map<Integer, FissionUserTaskRecordCountDto>> dealerTaskMap = new HashMap<>((int) (dealerTaskStatistics.size() / 0.75f) + 1);
        //查询结果转为Map<dealerId,Map<taskId,FissionUserTaskRecordCountDto>>
        for (FissionUserTaskRecordCountDto statistics : dealerTaskStatistics) {
            int dealerId = statistics.getDealerId();
            Map<Integer, FissionUserTaskRecordCountDto> statisticsMap = dealerTaskMap.get(dealerId);
            if (statisticsMap == null) {
                statisticsMap = new HashMap<>(8);
                statisticsMap.put(statistics.getTaskId(), statistics);
                dealerTaskMap.put(dealerId, statisticsMap);
            } else {
                statisticsMap.put(statistics.getTaskId(), statistics);
            }
        }
        int rank = 1;
        //转成fissionAllDealerRankVo
        for (FissionSale fissionDealer : fissionDealers) {
            Map<Integer, FissionUserTaskRecordCountDto> statisticsMap = dealerTaskMap.get(fissionDealer.getDealerId());
            fissionAllDealerRankVo = new FissionAllDealerRankVo();
            BeanUtils.copyProperties(fissionDealer, fissionAllDealerRankVo);
            if (statisticsMap != null) {
                fissionAllDealerRankVo.setPageView(statisticsMap.get(1) != null ? statisticsMap.get(1).getCount() : 0);
                fissionAllDealerRankVo.setSubscribeLive(statisticsMap.get(2) != null ? statisticsMap.get(2).getCount() : 0);
                fissionAllDealerRankVo.setActivityPageProduct(statisticsMap.get(3) != null ? statisticsMap.get(3).getCount() : 0);
                fissionAllDealerRankVo.setLiveView(statisticsMap.get(4) != null ? statisticsMap.get(4).getCount() : 0);
                fissionAllDealerRankVo.setLivePageProduct(statisticsMap.get(5) != null ? statisticsMap.get(5).getCount() : 0);
            } else {
                fissionAllDealerRankVo.setPageView(0);
                fissionAllDealerRankVo.setSubscribeLive(0);
                fissionAllDealerRankVo.setActivityPageProduct(0);
                fissionAllDealerRankVo.setLiveView(0);
                fissionAllDealerRankVo.setLivePageProduct(0);
            }
            CsCompany csCompany = csCompanyMap.get(fissionDealer.getDealerId());
            fissionAllDealerRankVo.setDealerName(csCompany == null ? "" : csCompany.getCompanyName());
            fissionAllDealerRankVo.setRank(rank);
            fissionAllDealerRankVos.add(fissionAllDealerRankVo);
            rank++;
        }
        return fissionAllDealerRankVos;
    }

    @Override
    public PageResult getBusinessRewardList(int page, int limit, Integer fissionId) {
        PageResult<FissionSaleRewardVo> pageResult = new PageResult<>();
        PageHelper.startPage(page, limit);
        List<FissionSale> fissionSales = fissionSaleReadMapper.selectFissionSaleReward(fissionId);
        if (fissionSales.isEmpty()) {
            return pageResult;
        }
        List<FissionSaleRewardVo> saleRewardVos = new ArrayList<>();
        FissionSaleRewardVo saleRewardVo = null;
        List<Integer> saleIds = new ArrayList<>();
        List<Integer> dealerIds = new ArrayList<>();
        for (FissionSale fissionSale : fissionSales) {
            saleIds.add(fissionSale.getSaleId());
            dealerIds.add(fissionSale.getDealerId());
        }
        Map<Integer, CsCompany> csCompanyMap = companyBaseService.getCompanyMap(dealerIds);
        Map<Integer, CsUser> csUserMap = userBaseService.getCsUserByIds(saleIds);
        //转换为List<FissionSaleRewardVo>
        for (FissionSale fissionSale : fissionSales) {
            saleRewardVo = new FissionSaleRewardVo();
            BeanUtils.copyProperties(fissionSale, saleRewardVo);
            CsCompany csCompany = csCompanyMap.get(fissionSale.getDealerId());
            CsUser csUser = csUserMap.get(fissionSale.getSaleId());
            saleRewardVo.setDealerName(csCompany == null ? "" : csCompany.getCompanyName());
            saleRewardVo.setSaleName(csUser == null ? "" : csUser.getUname());
            saleRewardVo.setSalePhone(csUserMap.get(fissionSale.getSaleId()).getUphone());
            saleRewardVos.add(saleRewardVo);
        }
        PageInfo<FissionAllSaleRankVo> pageInfo = new PageInfo(fissionSales);
        pageResult.setCount(pageInfo.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode());
        pageResult.setMsg(StatusCodeEnum.SUCCESS.getText());
        pageResult.setPage(page);
        pageResult.setLimit(limit);
        pageResult.setData(saleRewardVos);
        return pageResult;
    }


    @Override
    public List<FissionSaleRewardVo> getBusinessRewardList(Integer fissionId) {
        List<FissionSaleRewardVo> saleRewardVos = new ArrayList<>();
        List<FissionSale> fissionSales = fissionSaleReadMapper.selectFissionSaleReward(fissionId);
        if (fissionSales.isEmpty()) {
            return saleRewardVos;
        }
        FissionSaleRewardVo saleRewardVo = null;
        List<Integer> saleIds = new ArrayList<>();
        List<Integer> dealerIds = new ArrayList<>();
        for (FissionSale fissionSale : fissionSales) {
            saleIds.add(fissionSale.getSaleId());
            dealerIds.add(fissionSale.getDealerId());
        }
        Map<Integer, CsCompany> csCompanyMap = companyBaseService.getCompanyMap(dealerIds);
        Map<Integer, CsUser> csUserMap = userBaseService.getCsUserByIds(saleIds);
        BigDecimal zero = new BigDecimal(0);
        //转换成List<FissionSaleRewardVo>
        for (FissionSale fissionSale : fissionSales) {
            saleRewardVo = new FissionSaleRewardVo();
            BeanUtils.copyProperties(fissionSale, saleRewardVo);
            CsCompany csCompany = csCompanyMap.get(fissionSale.getDealerId());
            CsUser csUser = csUserMap.get(fissionSale.getSaleId());
            saleRewardVo.setDealerName(csCompany == null ? "" : csCompany.getCompanyName());
            saleRewardVo.setSaleName(csUser == null ? "" : csUser.getUname());
            saleRewardVo.setSalePhone(csUserMap.get(fissionSale.getSaleId()).getUphone());
            if (WithdrawalState.WAITING_WITHDRAWAL.getStatus() == saleRewardVo.getWithdrawalState()) {
                saleRewardVo.setWaitingWithdrawal(fissionSale.getRealIncome());
                saleRewardVo.setWithdrawaled(zero);
            } else {
                saleRewardVo.setWaitingWithdrawal(zero);
                saleRewardVo.setWithdrawaled(fissionSale.getRealIncome());
            }
            saleRewardVos.add(saleRewardVo);
        }
        return saleRewardVos;
    }

    @Override
    public List<FissionUserRewardRankVo> getUserRewardRank(Integer fissionId, Integer channelId) {
        List<FissionUserRewardRankVo> userRewardRankVos = new ArrayList<>();
        List<FissionUserRewardRankDto> userRewardRankDtos = fissionUserTaskRecordMapper.selectFissionUserRewardRank(fissionId, channelId);
        List<String> shareWxUnionIds = new ArrayList<>();
        for (FissionUserRewardRankDto fissionUserRewardRankDto : userRewardRankDtos) {
            shareWxUnionIds.add(fissionUserRewardRankDto.getUserWxUnionId());
        }
        if (shareWxUnionIds.isEmpty()) {
            return userRewardRankVos;
        }
        List<FissionUserTaskRecordStatDto> userTaskRecordStatDtos = fissionUserTaskRecordMapper.selectUserTaskRecordStat(fissionId, channelId, shareWxUnionIds);
        userTaskRecordStatDtos.addAll(fissionUserTaskRecordMapper.selectDistinctUserTaskRecordStat(fissionId, channelId, shareWxUnionIds));
        Map<String, Map<Integer, FissionUserTaskRecordStatDto>> userTaskMapMap = new HashMap<>(16);
        for (FissionUserTaskRecordStatDto userTaskRecordStatDto : userTaskRecordStatDtos) {
            Map<Integer, FissionUserTaskRecordStatDto> userTaskMap = userTaskMapMap.get(userTaskRecordStatDto.getShareWxUnionId());
            if (userTaskMap == null) {
                Map<Integer, FissionUserTaskRecordStatDto> map = new HashMap<>(8);
                map.put(userTaskRecordStatDto.getTaskId(), userTaskRecordStatDto);
                userTaskMapMap.put(userTaskRecordStatDto.getShareWxUnionId(), map);
            } else {
                userTaskMap.put(userTaskRecordStatDto.getTaskId(), userTaskRecordStatDto);
            }
        }
        FissionUserRewardRankVo fissionUserRewardRankVo = null;
        int rank = 1;
        //转换为List<FissionUserRewardRankVo>
        for (FissionUserRewardRankDto fissionUserRewardRankDto : userRewardRankDtos) {
            fissionUserRewardRankVo = new FissionUserRewardRankVo();
            fissionUserRewardRankVo.setRank(rank);
            Map<Integer, FissionUserTaskRecordStatDto> map = userTaskMapMap.get(fissionUserRewardRankDto.getUserWxUnionId());
            if (map == null) {
                fissionUserRewardRankVo.setInviteUserCount(0);
                fissionUserRewardRankVo.setInviteSubscribeCount(0);
                fissionUserRewardRankVo.setInviteUserBuyCount(0);
            } else {
                fissionUserRewardRankVo.setInviteUserCount(getValue(map.get(1)));
                fissionUserRewardRankVo.setInviteSubscribeCount(getValue(map.get(2)));
                fissionUserRewardRankVo.setInviteUserBuyCount(getValue(map.get(3)) + getValue(map.get(5)));
            }
            fissionUserRewardRankVo.setNickName(fissionUserRewardRankDto.getNickName());
            fissionUserRewardRankVo.setRealIncome(fissionUserRewardRankDto.getRewardAmount());
            userRewardRankVos.add(fissionUserRewardRankVo);
            rank++;
        }
        return userRewardRankVos;
    }

    private int getValue(FissionUserTaskRecordStatDto userTaskRecordStatDto) {
        if (userTaskRecordStatDto == null) {
            return 0;
        } else {
            return userTaskRecordStatDto.getValue();
        }
    }

}
