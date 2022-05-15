package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.directselling.api.AfterSaleActivityStatisticsService;
import com.tuanche.directselling.api.AfterSaleStatService;
import com.tuanche.directselling.dto.AfterSaleActivityChannelStatisticsDto;
import com.tuanche.directselling.dto.AfterSaleActivityDataBaseDto;
import com.tuanche.directselling.dto.AfterSaleActivityDataDetailDto;
import com.tuanche.directselling.dto.AfterSaleActivityDataVerifyDto;
import com.tuanche.directselling.dto.AfterSaleAgentOrderSalesStatDto;
import com.tuanche.directselling.dto.AfterSaleAgentStatDetailDto;
import com.tuanche.directselling.dto.AfterSaleAgentStatDto;
import com.tuanche.directselling.dto.AfterSaleDealerPaymentVerifyDto;
import com.tuanche.directselling.dto.AfterSaleOrderPersonRecordDto;
import com.tuanche.directselling.dto.AfterSaleUserBrowseStatDto;
import com.tuanche.directselling.dto.AfterSaleUserShareStatDto;
import com.tuanche.directselling.enums.AfterSaleAgentSortType;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleActivityReadMapper;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleActivityStatisticsReadMapper;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleAgentReadMapper;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleOrderRecordReadMapper;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleShareStatDetailReadMapper;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleUserBrowseReadMapper;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleUserShareReadMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleShareStatDetailWriteMapper;
import com.tuanche.directselling.model.AfterSaleActivity;
import com.tuanche.directselling.model.AfterSaleActivityChannelStatistics;
import com.tuanche.directselling.model.AfterSaleAgent;
import com.tuanche.directselling.model.AfterSaleShareStatDetail;
import com.tuanche.directselling.util.AfterSaleAgentStatSort;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.utils.AfterSaleConstants;
import com.tuanche.directselling.utils.GlobalConstants;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.AfterSaleActivityDataVerifyVo;
import com.tuanche.eap.api.bean.manufacturer.CsCompany;
import com.tuanche.eap.api.bean.manufacturer.CsDealerFinancial;
import com.tuanche.eap.api.dto.manufacturer.CsCompanyDto;
import com.tuanche.eap.api.enums.DealerFinancialAccountTypeEnum;
import com.tuanche.eap.api.service.manufacturer.CsCompanyService;
import com.tuanche.eap.api.service.manufacturer.CsDealerFinancialService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/7/20 21:43
 **/
@Service
public class AfterSaleStatServiceImpl implements AfterSaleStatService {

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    AfterSaleUserBrowseReadMapper afterSaleUserBrowseReadMapper;
    @Autowired
    AfterSaleUserShareReadMapper afterSaleUserShareReadMapper;
    @Autowired
    AfterSaleOrderRecordReadMapper afterSaleOrderRecordReadMapper;
    @Autowired
    AfterSaleAgentReadMapper afterSaleAgentReadMapper;
    @Autowired
    AfterSaleActivityStatisticsReadMapper afterSaleActivityStatisticsReadMapper;
    @Autowired
    AfterSaleStatService afterSaleStatService;
    @Reference
    CsDealerFinancialService dealerFinancialService;
    @Reference
    CsCompanyService csCompanyService;
    @Autowired
    @Qualifier("ClusterRedisService")
    RedisService redisService;
    @Autowired
    AfterSaleActivityStatisticsService afterSaleActivityStatisticsService;
    @Autowired
    AfterSaleActivityReadMapper afterSaleActivityReadMapper;
    @Autowired
    AfterSaleShareStatDetailReadMapper afterSaleShareStatDetailReadMapper;
    @Autowired
    AfterSaleShareStatDetailWriteMapper afterSaleShareStatDetailWriteMapper;

    @Override
    public List<AfterSaleAgentStatDto> selectShareStat(Integer activityId, Date startTime, Date endTime, AfterSaleAgentSortType afterSaleAgentSortType) {
        Map<String, AfterSaleAgentOrderSalesStatDto> packageCardMap = afterSaleOrderRecordReadMapper.selectAgentSalesPackageCardCount(startTime, endTime, activityId);
        Map<String, AfterSaleAgentOrderSalesStatDto> forwardPackageCardMap = afterSaleOrderRecordReadMapper.selectAgentSalesForwardPackageCardCount(startTime, endTime, activityId);
        Map<String, AfterSaleUserBrowseStatDto> uniqueVisitDtoMap = afterSaleUserBrowseReadMapper.selectUniqueVisit(startTime, endTime, activityId);
        Map<String, AfterSaleUserShareStatDto> shareDtoMap = afterSaleUserShareReadMapper.selectShareCount(startTime, endTime, activityId);
        Set<String> agentWxUnionIds = new HashSet<>();
        agentWxUnionIds.addAll(packageCardMap.keySet());
        agentWxUnionIds.addAll(uniqueVisitDtoMap.keySet());
        agentWxUnionIds.addAll(shareDtoMap.keySet());
        agentWxUnionIds.addAll(forwardPackageCardMap.keySet());
        List<AfterSaleAgentStatDto> afterSaleAgentStatDtos = new ArrayList<>();
        if (agentWxUnionIds.isEmpty()) {
            return afterSaleAgentStatDtos;
        }
        Map<String, AfterSaleAgent> afterSaleAgentMap = afterSaleAgentReadMapper.selectByAgentWxUnionIds(activityId, new ArrayList<>(agentWxUnionIds));
        AfterSaleAgentStatDto afterSaleAgentStatDto = null;
        int shareTotal = 0;
        int uniqueVisitTotal = 0;
        int packageCardTotal = 0;
        int forwardPackageCardTotal = 0;
        for (Map.Entry<String, AfterSaleAgent> entry : afterSaleAgentMap.entrySet()) {
            AfterSaleAgent afterSaleAgent = entry.getValue();
            afterSaleAgentStatDto = new AfterSaleAgentStatDto();
            afterSaleAgentStatDto.setId(afterSaleAgent.getId());
            afterSaleAgentStatDto.setName(afterSaleAgent.getName());
            afterSaleAgentStatDto.setPhone(afterSaleAgent.getPhone());
            afterSaleAgentStatDto.setPosition(afterSaleAgent.getPosition());
            afterSaleAgentStatDto.setWxHead(afterSaleAgent.getWxHead());
            afterSaleAgentStatDto.setPageViewCount(0);

            if (packageCardMap == null) {
                afterSaleAgentStatDto.setPackageCardCount(0);
            } else {
                AfterSaleAgentOrderSalesStatDto afterSaleAgentOrderSalesStatDto = packageCardMap.get(entry.getKey());
                if (afterSaleAgentOrderSalesStatDto == null) {
                    afterSaleAgentStatDto.setPackageCardCount(0);
                } else {
                    packageCardTotal += afterSaleAgentOrderSalesStatDto.getValue();
                    afterSaleAgentStatDto.setPackageCardCount(afterSaleAgentOrderSalesStatDto.getValue());
                }
            }

            if (forwardPackageCardMap == null) {
                afterSaleAgentStatDto.setForwardCount(0);
            } else {
                AfterSaleAgentOrderSalesStatDto afterSaleAgentOrderSalesStatDto = forwardPackageCardMap.get(entry.getKey());
                if (afterSaleAgentOrderSalesStatDto == null) {
                    afterSaleAgentStatDto.setForwardCount(0);
                } else {
                    forwardPackageCardTotal += afterSaleAgentOrderSalesStatDto.getValue();
                    afterSaleAgentStatDto.setForwardCount(afterSaleAgentOrderSalesStatDto.getValue());
                }
            }

            if (uniqueVisitDtoMap == null) {
                afterSaleAgentStatDto.setUniqueVisitorCount(0);
            } else {
                AfterSaleUserBrowseStatDto afterSaleUserBrowseStatDto = uniqueVisitDtoMap.get(entry.getKey());
                if (afterSaleUserBrowseStatDto == null) {
                    afterSaleAgentStatDto.setUniqueVisitorCount(0);
                } else {
                    uniqueVisitTotal += afterSaleUserBrowseStatDto.getValue();
                    afterSaleAgentStatDto.setUniqueVisitorCount(afterSaleUserBrowseStatDto.getValue());
                }
            }
            if (shareDtoMap == null) {
                afterSaleAgentStatDto.setShareCount(0);
            } else {
                AfterSaleUserShareStatDto afterSaleUserBrowseStatDto = shareDtoMap.get(entry.getKey());
                if (afterSaleUserBrowseStatDto == null) {
                    afterSaleAgentStatDto.setShareCount(0);
                } else {
                    shareTotal += afterSaleUserBrowseStatDto.getValue();
                    afterSaleAgentStatDto.setShareCount(afterSaleUserBrowseStatDto.getValue());
                }
            }
            afterSaleAgentStatDtos.add(afterSaleAgentStatDto);
        }
        Collections.sort(afterSaleAgentStatDtos, new AfterSaleAgentStatSort(afterSaleAgentSortType));
        AfterSaleAgentStatDto dto = new AfterSaleAgentStatDto();
        dto.setName("汇总");
        dto.setShareCount(shareTotal);
        dto.setUniqueVisitorCount(uniqueVisitTotal);
        dto.setPackageCardCount(packageCardTotal);
        dto.setForwardCount(forwardPackageCardTotal);
        afterSaleAgentStatDtos.add(0, dto);
        return afterSaleAgentStatDtos;
    }

    @Override
    @Transactional(value = "writeDataSourceTransactionManager", rollbackFor = Exception.class)
    public void createShareStatDetail(Integer activityId) {
        Date now = new Date();
        if (activityId == null) {
            Date nearDate = getBeforeNearDate(null);
            List<AfterSaleActivity> ongoingActivities = afterSaleActivityReadMapper.getOngoingActivities(new Date());
            for (AfterSaleActivity ongoingActivity : ongoingActivities) {
                CommonLogUtil.addInfo("售后特卖", "售后特卖黄金推广期数据定时生成,活动ID" + ongoingActivity.getId() + "时间：" + nearDate.getTime());
                int agentSalesPackageCardTotal = afterSaleOrderRecordReadMapper.selectAgentSalesPackageCardTotal(ongoingActivity.getSaleStartTime(), nearDate, ongoingActivity.getId());
                int uniqueVisitTotal = afterSaleUserBrowseReadMapper.selectUniqueVisitTotal(ongoingActivity.getSaleStartTime(), nearDate, ongoingActivity.getId());
                int shareCountTotal = afterSaleUserShareReadMapper.selectShareCountTotal(ongoingActivity.getSaleStartTime(), nearDate, ongoingActivity.getId());
                AfterSaleShareStatDetail detail = afterSaleShareStatDetailReadMapper.selectByActivityIdAndDate(ongoingActivity.getId(), nearDate);
                if (detail == null) {
                    AfterSaleShareStatDetail addDetail = new AfterSaleShareStatDetail();
                    addDetail.setActivityId(ongoingActivity.getId());
                    addDetail.setDateTime(nearDate);
                    addDetail.setShareCount(shareCountTotal);
                    addDetail.setUniqueVisitCount(uniqueVisitTotal);
                    addDetail.setSaleCardCount(agentSalesPackageCardTotal);
                    addDetail.setCreateDt(now);
                    afterSaleShareStatDetailWriteMapper.insertSelective(addDetail);
                } else {
                    AfterSaleShareStatDetail updateDetail = new AfterSaleShareStatDetail();
                    updateDetail.setId(detail.getId());
                    updateDetail.setActivityId(ongoingActivity.getId());
                    updateDetail.setDateTime(nearDate);
                    updateDetail.setShareCount(shareCountTotal);
                    updateDetail.setUniqueVisitCount(uniqueVisitTotal);
                    updateDetail.setSaleCardCount(agentSalesPackageCardTotal);
                    updateDetail.setUpdateDt(now);
                    afterSaleShareStatDetailWriteMapper.updateById(updateDetail);
                }
            }
        } else {
            AfterSaleActivity afterSaleActivity = afterSaleActivityReadMapper.selectByPrimaryKey(activityId);
            Date startTime = getAfterNearDate(afterSaleActivity.getSaleStartTime());
            Date endTime = now.before(afterSaleActivity.getSaleEndTime()) ? getAfterNearDate(now) : getAfterNearDate(afterSaleActivity.getSaleEndTime());
            List<Date> timeList = getTimeList(startTime, endTime);
            for (Date time : timeList) {
                int agentSalesPackageCardTotal = afterSaleOrderRecordReadMapper.selectAgentSalesPackageCardTotal(afterSaleActivity.getSaleStartTime(), time, activityId);
                int uniqueVisitTotal = afterSaleUserBrowseReadMapper.selectUniqueVisitTotal(afterSaleActivity.getSaleStartTime(), time, activityId);
                int shareCountTotal = afterSaleUserShareReadMapper.selectShareCountTotal(afterSaleActivity.getSaleStartTime(), time, activityId);
                AfterSaleShareStatDetail detail = afterSaleShareStatDetailReadMapper.selectByActivityIdAndDate(activityId, time);
                if (detail == null) {
                    AfterSaleShareStatDetail addDetail = new AfterSaleShareStatDetail();
                    addDetail.setActivityId(activityId);
                    addDetail.setDateTime(time);
                    addDetail.setShareCount(shareCountTotal);
                    addDetail.setUniqueVisitCount(uniqueVisitTotal);
                    addDetail.setSaleCardCount(agentSalesPackageCardTotal);
                    addDetail.setCreateDt(now);
                    afterSaleShareStatDetailWriteMapper.insertSelective(addDetail);
                } else {
                    AfterSaleShareStatDetail updateDetail = new AfterSaleShareStatDetail();
                    updateDetail.setId(detail.getId());
                    updateDetail.setActivityId(activityId);
                    updateDetail.setDateTime(time);
                    updateDetail.setShareCount(shareCountTotal);
                    updateDetail.setUniqueVisitCount(uniqueVisitTotal);
                    updateDetail.setSaleCardCount(agentSalesPackageCardTotal);
                    updateDetail.setUpdateDt(now);
                    afterSaleShareStatDetailWriteMapper.updateById(updateDetail);
                }
            }
        }
    }

    @Override
    public List<AfterSaleAgentStatDetailDto> selectShareStatDetail(Integer activityId, Date startTime, Date endTime) {
        AfterSaleActivity afterSaleActivity = afterSaleActivityReadMapper.selectByPrimaryKey(activityId);
        List<AfterSaleAgentStatDetailDto> afterSaleAgentStatDetailDtos = new ArrayList<>();
        if (activityId == null || afterSaleActivity == null) {
            return afterSaleAgentStatDetailDtos;
        }
        if (startTime == null) {
            startTime = afterSaleActivity.getSaleStartTime();
        } else {
            if (startTime.before(afterSaleActivity.getSaleStartTime())) {
                startTime = afterSaleActivity.getSaleStartTime();
            }
        }
        if (endTime == null) {
            endTime = afterSaleActivity.getSaleEndTime();
        } else {
            if (endTime.after(afterSaleActivity.getSaleEndTime())) {
                endTime = afterSaleActivity.getSaleEndTime();
            }
        }
        startTime = getBeforeNearDate(startTime);
        endTime = getAfterNearDate(endTime);

        List<AfterSaleShareStatDetail> afterSaleShareStatDetails = afterSaleShareStatDetailReadMapper.selectByActivityIdAndBetweenDate(activityId, startTime, endTime);
        AfterSaleAgentStatDetailDto statDetailDto = null;
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        for (AfterSaleShareStatDetail afterSaleShareStatDetail : afterSaleShareStatDetails) {
            statDetailDto = new AfterSaleAgentStatDetailDto();
            statDetailDto.setDateTime(sdf.format(afterSaleShareStatDetail.getDateTime()));
            statDetailDto.setUniqueVisitorCount(afterSaleShareStatDetail.getUniqueVisitCount());
            statDetailDto.setPackageCardCount(afterSaleShareStatDetail.getSaleCardCount());
            statDetailDto.setShareCount(afterSaleShareStatDetail.getShareCount());
            if (statDetailDto.getUniqueVisitorCount() == 0) {
                statDetailDto.setConversionRate("0%");
            } else {
                statDetailDto.setConversionRate(numberFormat.format(statDetailDto.getPackageCardCount().doubleValue() / statDetailDto.getUniqueVisitorCount().doubleValue() * 100d) + "%");
            }
            afterSaleAgentStatDetailDtos.add(statDetailDto);
        }
        return afterSaleAgentStatDetailDtos;
    }

    private Date getBeforeNearDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int minute = calendar.get(Calendar.MINUTE);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MINUTE, minute / 15 * 15);
        return calendar.getTime();
    }

    private Date getAfterNearDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int minute = calendar.get(Calendar.MINUTE);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (minute % 15 == 0) {
            calendar.set(Calendar.MINUTE, minute);
        } else {
            calendar.add(Calendar.MINUTE, (minute / 15 + 1) * 15);
        }
        return calendar.getTime();
    }

    private List<Date> getTimeList(Date startTime, Date endTime) {
        Calendar calendar = Calendar.getInstance();
        List<Date> times = new ArrayList<>();
        while (startTime.compareTo(endTime) <= 0) {
            times.add(startTime);
            calendar.setTime(startTime);
            calendar.add(Calendar.MINUTE, 15);
            startTime = calendar.getTime();
        }
        return times;
    }

    @Override
    public List<AfterSaleOrderPersonRecordDto> selectSaleOrderByAgent(AfterSaleOrderPersonRecordDto personRecordDto) {
        return afterSaleOrderRecordReadMapper.selectSaleOrderByAgent(personRecordDto);
    }

    @Override
    public Map<String, AfterSaleAgentOrderSalesStatDto> selectAgentSalesCount(Date startTime, Date endTime, Integer dealerId, Integer activityId) {
        return afterSaleOrderRecordReadMapper.selectAgentSalesCount(startTime, endTime, dealerId, activityId);
    }

    @Override
    public Map<Integer, AfterSaleAgentOrderSalesStatDto> selectAgentSalesCountByChannel(Date startTime, Date endTime, Integer dealerId, Integer activityId) {
        return afterSaleOrderRecordReadMapper.selectAgentSalesCountByChannel(startTime, endTime, dealerId, activityId);
    }

    @Override
    public Map<String, AfterSaleAgentOrderSalesStatDto> selectAgentSalesForwardCount(Date startTime, Date endTime, Integer dealerId, Integer activityId) {
        return afterSaleOrderRecordReadMapper.selectAgentSalesForwardCount(startTime, endTime, dealerId, activityId);
    }

    @Override
    public int selectAgentSalesForwardCountNonAgent(Date startTime, Date endTime, Integer dealerId, Integer activityId) {
        return afterSaleOrderRecordReadMapper.selectAgentSalesForwardCountNonAgent(startTime, endTime, dealerId, activityId);
    }

    @Override
    public Integer selectPageView(Date startTime, Date endTime, Integer dealerId, Integer activityId) {
        return afterSaleUserBrowseReadMapper.selectPageViewSum(startTime, endTime, dealerId, activityId);
    }

    @Override
    public Integer selectUniqueVisit(Date startTime, Date endTime, Integer dealerId, Integer activityId) {
        return afterSaleUserBrowseReadMapper.selectUniqueVisitSum(startTime, endTime, dealerId, activityId);
    }

    @Override
    public List<AfterSaleActivityDataVerifyVo> selectAfterSaleActivityDataVerify(AfterSaleActivityDataBaseDto afterSaleActivityDataBaseDto) {
        List<AfterSaleActivityDataVerifyDto> afterSaleActivityDataVerifyDtos = afterSaleActivityStatisticsReadMapper.selectAfterSaleActivityDataVerify(afterSaleActivityDataBaseDto);
        List<Integer> dealerIds = new ArrayList<>();
        if (CollectionUtils.isEmpty(afterSaleActivityDataVerifyDtos)) {
            return null;
        }
        List<AfterSaleActivityDataVerifyVo> result = new ArrayList<>(afterSaleActivityDataVerifyDtos.size() * 2);
        AfterSaleActivityDataVerifyVo vo = null;
        for (AfterSaleActivityDataVerifyDto dto : afterSaleActivityDataVerifyDtos) {
            dealerIds.add(dto.getDealerId());
            vo = new AfterSaleActivityDataVerifyVo();
            BeanUtils.copyProperties(dto, vo);
            if (dto.getUv() == 0) {
                vo.setPromotionPercent(BigDecimal.ZERO);
            } else {
                //推广转化率  推广卡售卖数（包括退款） / 点击人数
                vo.setPromotionPercent(new BigDecimal(String.valueOf(dto.getPromotionCardTotal().floatValue() / dto.getUv().floatValue())));
            }
            if (dto.getPromotionCardTotal() == 0) {
                vo.setVisitorsPercent(BigDecimal.ZERO);
                vo.setRefundPercent(BigDecimal.ZERO);
                vo.setAgentFissionTotalPercent(BigDecimal.ZERO);
                vo.setAutonomousFissionTotalPercent(BigDecimal.ZERO);
                vo.setKeepOnRecordUserTotalPercent(BigDecimal.ZERO);
            } else {
                //到店率 = 到店人数 / 推广卡售出数（包括退款）
                vo.setVisitorsPercent(new BigDecimal(String.valueOf(dto.getVisitorsTotal().floatValue() / dto.getPromotionCardTotal().floatValue())));
                //退款率 = 总退款数 / 推广卡售出数（包括退款）
                vo.setRefundPercent(new BigDecimal(String.valueOf(dto.getPromotionCardRefundsTotal().floatValue() / dto.getPromotionCardTotal().floatValue())));
                //备案人数占比 = 备案人购买数 / 总售出推广卡人数
                vo.setKeepOnRecordUserTotalPercent(new BigDecimal(String.valueOf(dto.getKeepOnRecordUserTotal().floatValue() / dto.getPromotionCardTotal().floatValue())));
            }
            if (dto.getVisitorsTotal() == 0) {
                vo.setPackagePercent(BigDecimal.ZERO);
            } else {
                //套餐转化率 = 套餐售出数 / 到店人数
                vo.setPackagePercent(new BigDecimal(String.valueOf(dto.getPackageCardTotal().floatValue() / dto.getVisitorsTotal().floatValue())));
            }
            result.add(vo);

        }
        if (!dealerIds.isEmpty()) {
            List<CsCompany> companiesByDealerIds = csCompanyService.getCompanysByDealerIds(dealerIds);
            Map<Integer, String> companyNameMap = companiesByDealerIds.stream().collect(Collectors.toMap(CsCompany::getId, CsCompany::getCompanyName, (key1, key2) -> key2));
            for (AfterSaleActivityDataVerifyVo dto : result) {
                String companyName = companyNameMap.get(dto.getDealerId());
                if (companyName != null) {
                    dto.setDealerName(companyName);
                }
            }
        }
        return result;
    }


    @Override
    public PageResult selectAfterSaleActivityDataVerifyPage(int page, int limit, AfterSaleActivityDataBaseDto afterSaleActivityDataBaseDto) {
        PageHelper.startPage(page, limit);
        List<AfterSaleActivityDataVerifyVo> afterSaleActivityDataVerifyDtos = afterSaleStatService.selectAfterSaleActivityDataVerify(afterSaleActivityDataBaseDto);
        PageResult<AfterSaleActivityDataVerifyVo> pageResult = new PageResult<>();
        PageInfo<AfterSaleActivityDataVerifyVo> pageInfo = new PageInfo<>(afterSaleActivityDataVerifyDtos);
        pageResult.setCount(pageInfo.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode());
        pageResult.setMsg("");
        pageResult.setData(afterSaleActivityDataVerifyDtos);
        return pageResult;
    }

    @Override
    public AfterSaleActivityDataVerifyDto selectAfterSaleActivityDataVerifySum(AfterSaleActivityDataBaseDto afterSaleActivityDataBaseDto) {
        return afterSaleActivityStatisticsReadMapper.selectAfterSaleActivityDataVerifySum(afterSaleActivityDataBaseDto);
    }

    @Override
    public List<AfterSaleDealerPaymentVerifyDto> selectAfterSaleDealerPaymentVerify(AfterSaleActivityDataBaseDto afterSaleActivityDataBaseDto) {
        List<AfterSaleDealerPaymentVerifyDto> afterSaleDealerPaymentVerifyDtos = afterSaleActivityStatisticsReadMapper.selectAfterSaleDealerPaymentVerify(afterSaleActivityDataBaseDto);
        List<Integer> dealerIds = new ArrayList<>();
        for (AfterSaleDealerPaymentVerifyDto dto : afterSaleDealerPaymentVerifyDtos) {
            dealerIds.add(dto.getDealerId());
        }
        if (!dealerIds.isEmpty()) {
            List<CsCompany> companiesByDealerIds = csCompanyService.getCompanysByDealerIds(dealerIds);
            Map<Integer, String> companyNameMap = companiesByDealerIds.stream().collect(Collectors.toMap(CsCompany::getId, CsCompany::getCompanyName, (key1, key2) -> key2));
            for (AfterSaleDealerPaymentVerifyDto dto : afterSaleDealerPaymentVerifyDtos) {
                String companyName = companyNameMap.get(dto.getDealerId());
                if (companyName != null) {
                    dto.setDealerName(companyName);
                }
            }
        }
        Map<Integer, List<CsDealerFinancial>> idListMap = dealerFinancialService.selectDealerFinancialByDealerIds(dealerIds);
        for (AfterSaleDealerPaymentVerifyDto dto : afterSaleDealerPaymentVerifyDtos) {
            List<CsDealerFinancial> dealerFinancials = idListMap.get(dto.getDealerId());
            if (dealerFinancials != null) {
                for (CsDealerFinancial csDealerFinancial : dealerFinancials) {
                    if (DealerFinancialAccountTypeEnum.AccountType0.getType() == csDealerFinancial.getAccountType()) {
                        dto.setCardBank(csDealerFinancial.getCardBank());
                        dto.setCardBankAddress(csDealerFinancial.getCardBankAddress());
                        dto.setCardNumber(csDealerFinancial.getCardNumber());
                        break;
                    }
                }
            }
        }
        return afterSaleDealerPaymentVerifyDtos;
    }

    @Override
    public PageResult selectAfterSaleDealerPaymentVerifyPage(int page, int limit, AfterSaleActivityDataBaseDto afterSaleActivityDataBaseDto) {
        PageHelper.startPage(page, limit);
        List<AfterSaleDealerPaymentVerifyDto> afterSaleActivityDataVerifyDtos = afterSaleStatService.selectAfterSaleDealerPaymentVerify(afterSaleActivityDataBaseDto);
        PageResult<AfterSaleDealerPaymentVerifyDto> pageResult = new PageResult<>();
        PageInfo<AfterSaleDealerPaymentVerifyDto> pageInfo = new PageInfo<>(afterSaleActivityDataVerifyDtos);
        pageResult.setCount(pageInfo.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode());
        pageResult.setMsg("");
        pageResult.setData(afterSaleActivityDataVerifyDtos);
        return pageResult;
    }

    @Override
    public AfterSaleActivityDataDetailDto selectAfterSaleActivityDataDetailDto(Integer activityId) {
        return afterSaleActivityStatisticsReadMapper.selectAfterSaleActivityDataDetailDto(activityId);
    }

    @Override
    public int selectAfterSaleActivityCount(AfterSaleActivityDataBaseDto afterSaleActivityDataBaseDto) {
        return afterSaleActivityStatisticsReadMapper.selectAfterSaleActivityCount(afterSaleActivityDataBaseDto);
    }

    /**
     * 活动数据统计详情
     *
     * @param activityId
     * @return com.tuanche.directselling.dto.AfterSaleActivityDataDetailDto
     * @author HuangHao
     * @CreatTime 2021-09-17 17:02
     */
    public AfterSaleActivityDataDetailDto getActivityDataDetailDto(Integer activityId) {
        AfterSaleActivityDataDetailDto activityDataDetailDto = afterSaleStatService.selectAfterSaleActivityDataDetailDto(activityId);
        if (activityDataDetailDto != null && activityDataDetailDto.getDealerId() != null) {
            CsCompanyDto csCompanyDto = csCompanyService.getCompanyById(activityDataDetailDto.getDealerId());
            if (csCompanyDto != null) {
                activityDataDetailDto.setDealerName(csCompanyDto.getCompanyName());
            }
        }
        List<AfterSaleActivityChannelStatistics> channelStatisticsList = afterSaleActivityStatisticsService.getListByActivityId(activityId);
        List<AfterSaleActivityChannelStatisticsDto> channelList = new ArrayList<>(AfterSaleConstants.ChannelType.values().length * 2);
        for (AfterSaleConstants.ChannelType t : AfterSaleConstants.ChannelType.values()) {
            AfterSaleActivityChannelStatisticsDto channelStatisticsDto = new AfterSaleActivityChannelStatisticsDto();
            channelStatisticsDto.setChannelName(t.getKey());
            if (channelStatisticsList != null) {
                List<AfterSaleActivityChannelStatistics> channelData = channelStatisticsList.stream().filter(channelStatistics -> channelStatistics.getChannel() == t.getCode()).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(channelData)) {
                    BeanUtils.copyProperties(channelData.get(0), channelStatisticsDto);
                }
                channelStatisticsDto.setPromotionCardsSellPercent(GlobalConstants.percentConversion(channelStatisticsDto.getPromotionCardTotal(), activityDataDetailDto.getPromotionCardTotal()).toString() + "%");
                int promotionCardRefundsTotal = channelStatisticsDto.getPromotionCardRefundsTotalActive() + channelStatisticsDto.getPromotionCardRefundsTotalPassive();
                channelStatisticsDto.setPromotionCardRefundsTotal(promotionCardRefundsTotal);
                channelStatisticsDto.setPackagePercent(GlobalConstants.percentConversion(channelStatisticsDto.getPackageCardTotal(), channelStatisticsDto.getVisitorsTotal()).toString() + "%");
                channelList.add(channelStatisticsDto);
            }
        }
        activityDataDetailDto.setChannelList(channelList);
        return activityDataDetailDto;
    }
}
