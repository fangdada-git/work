package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.google.common.collect.Lists;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.exception.RedisException;
import com.tuanche.arch.util.utils.IntegerUtils;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.directselling.api.FissionActivityService;
import com.tuanche.directselling.api.FissionDictService;
import com.tuanche.directselling.api.FissionSaleService;
import com.tuanche.directselling.constant.BaseCacheKeys;
import com.tuanche.directselling.dto.FissionActivityDetailDto;
import com.tuanche.directselling.dto.FissionActivitySaleIntegralDto;
import com.tuanche.directselling.dto.FissionSaleDto;
import com.tuanche.directselling.dto.FissionSaleExistDto;
import com.tuanche.directselling.dto.FissionSalePayDto;
import com.tuanche.directselling.dto.FissionSaleRewardReconciliationDto;
import com.tuanche.directselling.dto.FissionStatisticBigDecimalDto;
import com.tuanche.directselling.dto.SaleRewardReconciliationDto;
import com.tuanche.directselling.enums.AwardRuleType;
import com.tuanche.directselling.enums.CompleteTaskStatus;
import com.tuanche.directselling.enums.FissionActivityStatus;
import com.tuanche.directselling.enums.FissionDictType;
import com.tuanche.directselling.enums.FissionOnStatus;
import com.tuanche.directselling.enums.SaleOrderStatus;
import com.tuanche.directselling.enums.WithdrawalState;
import com.tuanche.directselling.mapper.read.directselling.FissionActivityReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FissionSaleReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FissionSaleTaskStatisticsReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FissionSalesOrderReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FissionStatisticsReadMapper;
import com.tuanche.directselling.mapper.write.directselling.FissionSaleWriteMapper;
import com.tuanche.directselling.model.FissionActivity;
import com.tuanche.directselling.model.FissionAwardRule;
import com.tuanche.directselling.model.FissionDict;
import com.tuanche.directselling.model.FissionSale;
import com.tuanche.directselling.model.FissionSaleTaskStatistics;
import com.tuanche.directselling.model.FissionTradeRecord;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.util.ConstantsUtil;
import com.tuanche.directselling.util.FuncUtil;
import com.tuanche.directselling.utils.GlobalConstants;
import com.tuanche.directselling.utils.OrderNoGenerateUtil;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.directselling.vo.FissionActivityDetailVo;
import com.tuanche.directselling.vo.FissionActivityInfoVo;
import com.tuanche.directselling.vo.FissionActivityPersonalGoalVo;
import com.tuanche.directselling.vo.FissionActivityPersonalIntegralVo;
import com.tuanche.directselling.vo.FissionActivityRankVo;
import com.tuanche.directselling.vo.FissionActivitySaleIntegralVo;
import com.tuanche.directselling.vo.FissionSaleScoreVo;
import com.tuanche.directselling.vo.FissionSaleVo;
import com.tuanche.directselling.vo.FissionSaleWalletVo;
import com.tuanche.directselling.vo.WeChatPaymentVo;
import com.tuanche.district.api.dto.input.DistrictBaseInputDto;
import com.tuanche.district.api.dto.output.DistrictOutputBaseDto;
import com.tuanche.district.api.service.IDistrictApiService;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.api.UserBaseService;
import com.tuanche.manubasecenter.api.WxTemplateMessageBaseService;
import com.tuanche.manubasecenter.dto.WxDataDto;
import com.tuanche.manubasecenter.dto.WxTemplateInfoDto;
import com.tuanche.manubasecenter.model.CsCompany;
import com.tuanche.manubasecenter.model.CsUser;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName: FissionSaleServiceImpl
 * @Description: 裂变活动销售相关service
 * @Author: ZhangYiXin
 * @Date: 2020/9/24 9:27
 * @Version 1.0
 **/
@Service(retries = 0)
public class FissionSaleServiceImpl implements FissionSaleService {

    /**
     * 裂变活动销售收入计算日志关键字
     */
    private static String FISSION_SALE_INCOME_LOG_KEY_WORD = "裂变活动销售收入计算";
    @Reference
    CompanyBaseService companyBaseService;
    @Reference
    UserBaseService userBaseService;
    @Autowired
    FissionActivityService fissionActivityService;
    @Autowired
    @Qualifier("executorService")
    ExecutorService executorService;
    @Reference
    WxTemplateMessageBaseService wxTemplateMessageBaseService;
    @Autowired
    private FissionActivityReadMapper fissionActivityReadMapper;
    @Autowired
    private FissionSaleTaskStatisticsReadMapper fissionSaleTaskStatisticsReadMapper;
    @Autowired
    private FissionSaleReadMapper fissionSaleReadMapper;
    @Autowired
    private FissionSaleWriteMapper fissionSaleWriteMapper;
    @Autowired
    private FissionSalesOrderReadMapper fissionSalesOrderReadMapper;
    @Autowired
    private FissionStatisticsReadMapper fissionStatisticsReadMapper;
    @Autowired
    private FissionDictService fissionDictService;
    @Autowired
    private FissionTradeRecordServiceImpl fissionTradeRecordServiceImpl;
    @Autowired
    private FissionSaleServiceImpl fissionSaleServiceImpl;
    @Reference
    private IDistrictApiService iDistrictApiService;
    @Autowired
    @Qualifier("ClusterRedisService")
    private RedisService redisService;
    @Value("${fission_app_id}")
    private String fission_app_id;
    @Value("${fission_sale_award_template_id}")
    private String fission_sale_award_template_id;
    @Value("${fission_sale_my_wallet_url}")
    private String fission_sale_my_wallet_url;

    /**
     * 获取单个销售
     *
     * @return
     */
    @Override
    public FissionSale getFissionSale(FissionSale fissionSale) {
        return fissionSaleReadMapper.getFissionSale(fissionSale);
    }

    /**
     * 获取裂变活动销售列表
     *
     * @param fissionSale
     * @return com.tuanche.directselling.model.FissionSale
     * @author HuangHao
     * @CreatTime 2020-10-09 17:04
     */
    @Override
    public List<FissionSale> getFissionSaleList(FissionSale fissionSale) {
        return fissionSaleReadMapper.getFissionSaleList(fissionSale);
    }

    @Override
    public Integer getSaleIntegralBySaleId(int saleId) {
        Integer integral = fissionSaleTaskStatisticsReadMapper.getSaleIntegral(saleId);
        return integral == null ? 0 : integral;
    }

    @Override
    public PageResult getSaleIntegralListByBySaleId(int page, int limit, int saleId) {
        PageResult<FissionActivitySaleIntegralVo> pageResult = new PageResult<>();
        PageHelper.startPage(page, limit);
        List<FissionActivitySaleIntegralDto> fissionActivitySaleDtoList = fissionSaleTaskStatisticsReadMapper.selectSaleIntegralListByBySaleId(saleId);
        List<FissionActivitySaleIntegralVo> fissionActivitySaleVos = new ArrayList<>();
        FissionActivitySaleIntegralVo fissionActivitySaleIntegralVo = null;
        for (FissionActivitySaleIntegralDto fissionActivitySaleDto : fissionActivitySaleDtoList) {
            fissionActivitySaleIntegralVo = new FissionActivitySaleIntegralVo();
            BeanUtils.copyProperties(fissionActivitySaleDto, fissionActivitySaleIntegralVo);
            fissionActivitySaleVos.add(fissionActivitySaleIntegralVo);
        }
        PageInfo<FissionActivitySaleIntegralVo> pageInfo = new PageInfo(fissionActivitySaleDtoList);
        pageResult.setCount(pageInfo.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode());
        pageResult.setMsg(StatusCodeEnum.SUCCESS.getText());
        pageResult.setData(fissionActivitySaleVos);
        return pageResult;
    }

    /**
     * 判断销售是否存在于列表活动中
     *
     * @param fissionId
     * @param saleWxOpenId
     * @return -1 参数错误 0：不存在 1：存在
     * @author HuangHao
     * @CreatTime 2020-09-27 10:08
     */
    @Override
    public int saleExistInFission(Integer fissionId, String saleWxOpenId) {
        if (fissionId == null || StringUtils.isEmpty(saleWxOpenId)) {
            return -1;
        }
        Integer exist = fissionSaleReadMapper.saleExistInFission(fissionId, saleWxOpenId);
        return exist == null ? 0 : 1;
    }

    /**
     * 检测用户和分享人是否是销售
     *
     * @param fissionSaleExistDto
     * @return java.lang.Integer
     * @author HuangHao
     * @CreatTime 2020-10-14 11:42
     */
    @Override
    public FissionSaleExistDto userAndShareUserIsSalesman(FissionSaleExistDto fissionSaleExistDto) {
        return fissionSaleReadMapper.userAndShareUserIsSalesman(fissionSaleExistDto);
    }

    /**
     * @param pageResult
     * @param fissionSale
     * @description: 销售打款审核列表
     * @return: com.tuanche.directselling.utils.PageResult
     * @author: dudg
     * @date: 2020/9/27 12:12
     */
    @Override
    public PageResult selectSalePayListByFissionId(PageResult<FissionSalePayDto> pageResult, FissionSale fissionSale) {
        PageHelper.startPage(pageResult.getPage(), pageResult.getLimit(), "id desc");
        List<FissionSalePayDto> fissionSalePayDtos = fissionSaleReadMapper.selectSalePayListByFissionId(fissionSale);

        fissionSalePayDtos.forEach(n -> {
            n.setSalePhone(userBaseService.getUserPhoneById(n.getSaleId()));
            n.setDealerName(companyBaseService.getCsCompanyName(n.getDealerId()));
        });
        PageInfo<FissionSalePayDto> page = new PageInfo<FissionSalePayDto>(fissionSalePayDtos);
        pageResult.setCount(page.getTotal());
        pageResult.setCode("0");
        pageResult.setMsg("");
        pageResult.setData(fissionSalePayDtos);
        return pageResult;
    }

    /**
     * @param fissionSale
     * @description: 销售打款审核列表总数
     * @return: java.lang.Integer
     * @author: dudg
     * @date: 2020/10/9 16:01
     */
    @Override
    public Integer selectSalePayListCountByFissionId(FissionSale fissionSale) {
        return fissionSaleReadMapper.selectSalePayListCountByFissionId(fissionSale);
    }

    @Override
    public BigDecimal selectFissionSaleWithdrawal(Integer saleId, Integer withdrawalState) {
        BigDecimal withdrawal = fissionSaleReadMapper.selectFissionSaleWithdrawal(saleId, withdrawalState);
        if (withdrawal == null) {
            return new BigDecimal(0);
        }
        return withdrawal;
    }

    @Override
    public PageResult selectFissionSaleList(int page, int limit, Integer saleId) {
        PageResult<FissionSaleVo> pageResult = new PageResult<>();
        PageHelper.startPage(page, limit);
        List<FissionSaleDto> fissionActivitySaleDtoList = fissionSaleReadMapper.selectFissionSaleList(saleId);
        List<FissionSaleVo> fissionSaleVos = new ArrayList<>();
        FissionSaleVo fissionSaleVo = null;
        for (FissionSaleDto fissionSaleDto : fissionActivitySaleDtoList) {
            fissionSaleVo = new FissionSaleVo();
            BeanUtils.copyProperties(fissionSaleDto, fissionSaleVo);
            fissionSaleVo.setId(fissionSaleDto.getFissionId());
            fissionSaleVos.add(fissionSaleVo);
        }
        PageInfo<FissionSaleVo> pageInfo = new PageInfo(fissionActivitySaleDtoList);
        pageResult.setCount(pageInfo.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode());
        pageResult.setMsg(StatusCodeEnum.SUCCESS.getText());
        pageResult.setPage(page);
        pageResult.setLimit(limit);
        pageResult.setData(fissionSaleVos);
        return pageResult;
    }

    @Override
    public FissionSaleWalletVo selectFissionSaleWallet(int page, int limit, Integer saleId) {
        FissionSaleWalletVo fissionSaleWalletVo = new FissionSaleWalletVo();
        fissionSaleWalletVo.setWaitingWithdrawal(selectFissionSaleWithdrawal(saleId, WithdrawalState.WAITING_WITHDRAWAL.getStatus()));
        fissionSaleWalletVo.setWithdrawaled(selectFissionSaleWithdrawal(saleId, WithdrawalState.WITHDRAWALED.getStatus()));
        fissionSaleWalletVo.setWithdrawalList(selectFissionSaleList(page, limit, saleId));
        return fissionSaleWalletVo;
    }

    @Override
    public FissionActivityDetailVo getActivitySaleDetail(int dealerId, int fissionId, int saleId) {
        FissionActivityDetailDto fissionActivityDetailDto = fissionActivityReadMapper.selectActivityDetailByFissionId(dealerId, fissionId, saleId);
        if (fissionActivityDetailDto == null) {
            return null;
        }
        boolean isJoin = false;
        FissionActivityDetailVo fissionActivityDetailVo = new FissionActivityDetailVo();
        if (fissionActivityDetailDto.getOrderStatus() != null && SaleOrderStatus.PAID.getType() == fissionActivityDetailDto.getOrderStatus()) {
            isJoin = true;
            fissionActivityDetailVo.setJoin(true);
        }
        Date now = new Date();
        fissionActivityDetailVo.setActivityStatus(FissionActivityStatus.IS_OVER.getStatus());
        if (fissionActivityDetailDto.getStartTime().before(now) && fissionActivityDetailDto.getEndTime().after(now)) {
            fissionActivityDetailVo.setInProgress(true);
            if (FissionOnStatus.OPEN.getStatus() == fissionActivityDetailDto.getOnState()) {
                fissionActivityDetailVo.setActivityStatus(FissionActivityStatus.IN_PROGRESS.getStatus());
            } else {
                fissionActivityDetailVo.setActivityStatus(FissionActivityStatus.NOT_BEGIN.getStatus());
            }
        }
        if (fissionActivityDetailDto.getStartTime().after(now)) {
            fissionActivityDetailVo.setActivityStatus(FissionActivityStatus.NOT_BEGIN.getStatus());
        }
        FissionAwardRule fissionAwardRuleB = null;
        List<FissionAwardRule> fissionAwardRuleList = fissionActivityService.getAwardRuleListByFissionId(fissionId, GlobalConstants.FISSION_AWARD_RULE_TYPE_B);
        Map<String, FissionAwardRule> taskIdRuleMap = new HashMap<>(8);
        for (FissionAwardRule fissionAwardRule : fissionAwardRuleList) {
            fissionAwardRuleB = fissionAwardRule;
            taskIdRuleMap.putIfAbsent(String.valueOf(fissionAwardRule.getTaskCode()), fissionAwardRule);
        }
        BigDecimal zero = BigDecimal.ZERO;
        if (fissionAwardRuleB == null) {
            fissionAwardRuleB = new FissionAwardRule();
            fissionAwardRuleB.setPersonMoney(zero);
            fissionAwardRuleB.setPrizePool(zero);
        }
        fissionActivityDetailVo.setActivityName(fissionActivityDetailDto.getActivityName());
        fissionActivityDetailVo.setHeadPicUrl(fissionActivityDetailDto.getHeadPicUrl());
        fissionActivityDetailVo.setStartTime(fissionActivityDetailDto.getStartTime());
        fissionActivityDetailVo.setEndTime(fissionActivityDetailDto.getEndTime());
        fissionActivityDetailVo.setFissionId(fissionActivityDetailDto.getId());
        fissionActivityDetailVo.setPersonMoney(fissionAwardRuleB.getPersonMoney());
        fissionActivityDetailVo.setPeriodsId(fissionActivityDetailDto.getPeriodsId());
        if (!isJoin) {
            return fissionActivityDetailVo;
        }
        /**
         * 已参与（支付成功）
         */
        FissionActivityInfoVo fissionActivityInfoVo = new FissionActivityInfoVo();
        //个人成绩
        List<FissionActivityPersonalIntegralVo> personalIntegral = getPersonalIntegralVo(taskIdRuleMap, fissionId, saleId);
        //个人目标
        List<FissionActivityPersonalGoalVo> personalGoal = getPersonalGoalVo(fissionActivityInfoVo, personalIntegral);
        //奖金池总金额 团车or经销商承担金额 + 销售支付金额*参与成员数
        BigDecimal joinCount = new BigDecimal(fissionSalesOrderReadMapper.selectSaleJoinCount(fissionId));
        BigDecimal prizePool = fissionAwardRuleB.getPrizePool().add(fissionAwardRuleB.getPersonMoney().multiply(joinCount));
        //该销售的积分数
        Integer fissionSaleIntegral = fissionSaleTaskStatisticsReadMapper.getFissionSaleIntegral(fissionId, saleId);
        //预计收入 & 实际收入
        FissionSale fissionSaleData = fissionSaleReadMapper.selectFissionSaleByFissionAndIdSaleId(fissionId, saleId);
        if (fissionSaleData != null) {
            fissionActivityDetailVo.setSaleWxOpenId(fissionSaleData.getSaleWxOpenId());
            fissionActivityDetailVo.setSaleWxUnionId(fissionSaleData.getSaleWxUnionId());
        }
        //获取排名
        List<FissionSale> dealerRank = fissionSaleReadMapper.selectFissionDealerRank(fissionId);
        List<FissionSale> saleRank = fissionSaleReadMapper.selectFissionSaleRank(fissionId);
        List<FissionSale> dealerSaleRank = fissionSaleReadMapper.selectFissionDealerSaleRank(fissionId, dealerId);
        List<Integer> dealerIds = new ArrayList<>();
        if (!dealerRank.isEmpty()) {
            for (FissionSale fSale : dealerRank) {
                dealerIds.add(fSale.getDealerId());
            }
        }
        Map<Integer, CsCompany> csCompanyMap = companyBaseService.getCompanyMap(dealerIds);
        List<FissionActivityRankVo> dealerRankVo = new ArrayList<>();
        List<FissionActivityRankVo> saleRankVo = new ArrayList<>();
        List<FissionActivityRankVo> dealerSaleRankVo = new ArrayList<>();
        FissionActivityRankVo fissionActivityRankVo = null;
        boolean isRealIncomeComplete = false;
        int financialAuditStatus = 0;
        int rank = 1;
        for (FissionSale fissionSale : saleRank) {
            if (rank == 1) {
                isRealIncomeComplete = fissionSale.getCalculationRealIncome();
                financialAuditStatus = fissionSale.getFinancialAuditStatus();
            }
            if (fissionActivityDetailVo.getActivityStatus() == FissionActivityStatus.IS_OVER.getStatus() && isRealIncomeComplete) {
                //任务结束后判断收入为0 的不展示
                if (fissionSale.getRealIncome().compareTo(zero) == 0) {
                    continue;
                }
            } else {
                //任务结束前判断积分为0 的不展示
                if (fissionSale.getTaskIntegral() == 0) {
                    continue;
                }
            }
            fissionActivityRankVo = new FissionActivityRankVo();
            fissionActivityRankVo.setEstimatedIncome(fissionSale.getEstimatedIncome());
            fissionActivityRankVo.setRealIncome(fissionSale.getRealIncome());
            fissionActivityRankVo.setIntegral(fissionSale.getTaskIntegral());
            fissionActivityRankVo.setName(fissionSale.getSaleName());
            fissionActivityRankVo.setRank(rank);
            saleRankVo.add(fissionActivityRankVo);
            rank++;
        }
        rank = 1;
        for (FissionSale fissionSale : dealerRank) {
            if (fissionActivityDetailVo.getActivityStatus() == FissionActivityStatus.IS_OVER.getStatus() && isRealIncomeComplete) {
                //任务结束后判断收入为0 的不展示
                if (fissionSale.getRealIncome().compareTo(zero) == 0) {
                    continue;
                }
            } else {
                //任务结束前判断积分为0 的不展示
                if (fissionSale.getTaskIntegral() == 0) {
                    continue;
                }
            }
            fissionActivityRankVo = new FissionActivityRankVo();
            fissionActivityRankVo.setEstimatedIncome(fissionSale.getEstimatedIncome());
            fissionActivityRankVo.setRealIncome(fissionSale.getRealIncome());
            fissionActivityRankVo.setIntegral(fissionSale.getTaskIntegral());
            CsCompany csCompany = csCompanyMap.get(fissionSale.getDealerId());
            if (csCompany != null) {
                fissionActivityRankVo.setName(csCompany.getCompanyName());
            }
            fissionActivityRankVo.setRank(rank);
            dealerRankVo.add(fissionActivityRankVo);
            rank++;
        }
        rank = 1;
        for (FissionSale fissionSale : dealerSaleRank) {
            if (fissionActivityDetailVo.getActivityStatus() == FissionActivityStatus.IS_OVER.getStatus() && isRealIncomeComplete) {
                //任务结束后判断收入为0 的不展示
                if (fissionSale.getRealIncome().compareTo(zero) == 0) {
                    continue;
                }
            } else {
                //任务结束前判断积分为0 的不展示
                if (fissionSale.getTaskIntegral() == 0) {
                    continue;
                }
            }
            fissionActivityRankVo = new FissionActivityRankVo();
            fissionActivityRankVo.setEstimatedIncome(fissionSale.getEstimatedIncome());
            fissionActivityRankVo.setRealIncome(fissionSale.getRealIncome());
            fissionActivityRankVo.setIntegral(fissionSale.getTaskIntegral());
            fissionActivityRankVo.setName(fissionSale.getSaleName());
            fissionActivityRankVo.setRank(rank);
            dealerSaleRankVo.add(fissionActivityRankVo);
            rank++;
        }

        if (fissionSaleData == null) {
            fissionActivityInfoVo.setEstimatedIncome(zero);
            fissionActivityInfoVo.setRealIncome(zero);
        } else {
            fissionActivityInfoVo.setEstimatedIncome(fissionSaleData.getEstimatedIncome());
            fissionActivityInfoVo.setRealIncome(fissionSaleData.getRealIncome());
        }
        fissionActivityInfoVo.setRealIncomeComplete(isRealIncomeComplete);
        fissionActivityInfoVo.setFinancialAuditStatus(financialAuditStatus);
        fissionActivityInfoVo.setDealerRank(dealerRankVo);
        fissionActivityInfoVo.setSaleRank(saleRankVo);
        fissionActivityInfoVo.setDealerSaleRank(dealerSaleRankVo);
        fissionActivityInfoVo.setIntegral(fissionSaleIntegral == null ? 0 : fissionSaleIntegral);
        fissionActivityInfoVo.setPrizePool(prizePool);
        fissionActivityInfoVo.setPersonalIntegral(personalIntegral);
        fissionActivityInfoVo.setPersonalGoal(personalGoal);
        fissionActivityDetailVo.setInActivity(fissionActivityInfoVo);
        return fissionActivityDetailVo;
    }

    @Override
    public FissionSaleScoreVo getPersonalIntegralVo(int dealerId, int saleId, Integer fissionId) {
        if (fissionId == null) {
            fissionId = fissionSaleReadMapper.selectNewestFissionIdBySaleId(saleId);
        }
        FissionActivityDetailDto fissionActivityDetailDto = fissionActivityReadMapper.selectActivityDetailByFissionId(dealerId, fissionId, saleId);
        if (fissionActivityDetailDto == null) {
            return null;
        }
        boolean isInProgress = false;
        Date now = new Date();
        if (fissionActivityDetailDto.getStartTime().before(now) && fissionActivityDetailDto.getEndTime().after(now)) {
            isInProgress = true;
        }
        List<FissionAwardRule> fissionAwardRuleList = fissionActivityService.getAwardRuleListByFissionId(fissionId, GlobalConstants.FISSION_AWARD_RULE_TYPE_B);
        Map<String, FissionAwardRule> taskIdRuleMap = new HashMap<>(8);
        for (FissionAwardRule fissionAwardRule : fissionAwardRuleList) {
            taskIdRuleMap.putIfAbsent(String.valueOf(fissionAwardRule.getTaskCode()), fissionAwardRule);
        }

        //该销售的积分数
        Integer fissionSaleIntegral = fissionSaleTaskStatisticsReadMapper.getFissionSaleIntegral(fissionId, saleId);
        FissionSale fissionSaleData = fissionSaleReadMapper.selectFissionSaleByFissionAndIdSaleId(fissionId, saleId);
        FissionSaleScoreVo fissionSaleScoreVo = new FissionSaleScoreVo();
        fissionSaleScoreVo.setInProgress(isInProgress);
        fissionSaleScoreVo.setActivityName(fissionActivityDetailDto.getActivityName());
        fissionSaleScoreVo.setId(fissionId);
        fissionSaleScoreVo.setStartTime(fissionActivityDetailDto.getStartTime());
        fissionSaleScoreVo.setEndTime(fissionActivityDetailDto.getEndTime());
        fissionSaleScoreVo.setIntegral(fissionSaleIntegral == null ? 0 : fissionSaleIntegral);
        fissionSaleScoreVo.setEstimatedIncome(fissionSaleData.getEstimatedIncome());
        fissionSaleScoreVo.setRealIncome(fissionSaleData.getRealIncome());
        fissionSaleScoreVo.setPersonalIntegral(getPersonalIntegralVo(taskIdRuleMap, fissionId, saleId));
        return fissionSaleScoreVo;
    }

    /**
     * 销售的个人成绩
     *
     * @return List<FissionActivityPersonalIntegralVo>
     */
    private List<FissionActivityPersonalIntegralVo> getPersonalIntegralVo(Map<String, FissionAwardRule> taskIdRuleMap, Integer fissionId, Integer saleId) {
        List<FissionActivityPersonalIntegralVo> personalIntegral = new ArrayList<>();
        if (fissionId == null) {
            return personalIntegral;
        }
        List<FissionSaleTaskStatistics> fissionSaleTaskStatisticsList = fissionSaleTaskStatisticsReadMapper.selectTaskFinishInfoByFissionIdSaleId(fissionId, saleId);
        Map<String, FissionSaleTaskStatistics> taskIdMap = new HashMap<>(8);
        for (FissionSaleTaskStatistics stat : fissionSaleTaskStatisticsList) {
            taskIdMap.putIfAbsent(String.valueOf(stat.getTaskId()), stat);
        }
        List<FissionDict> fissionDictList = fissionDictService.getFissionDictByType(FissionDictType.LB.getType());
        int index = 1;
        FissionActivityPersonalIntegralVo fissionActivityPersonalIntegralVo = null;
        for (FissionDict fissionDict : fissionDictList) {
            FissionSaleTaskStatistics stat = taskIdMap.get(fissionDict.getCode());
            fissionActivityPersonalIntegralVo = new FissionActivityPersonalIntegralVo();
            fissionActivityPersonalIntegralVo.setId(index);
            if (stat == null) {
                fissionActivityPersonalIntegralVo.setFinishTaskTotal(0);
                fissionActivityPersonalIntegralVo.setTaskIntegral(0);
                fissionActivityPersonalIntegralVo.setWhetherCompleteTask(CompleteTaskStatus.INCOMPLETE.getStatus());
            } else {
                fissionActivityPersonalIntegralVo.setFinishTaskTotal(stat.getFinishTaskTotal());
                fissionActivityPersonalIntegralVo.setTaskIntegral(stat.getTaskIntegral());
                fissionActivityPersonalIntegralVo.setWhetherCompleteTask(stat.getWhetherCompleteTask() == null ? CompleteTaskStatus.INCOMPLETE.getStatus() : (stat.getWhetherCompleteTask() ? CompleteTaskStatus.COMPLETE.getStatus() : CompleteTaskStatus.INCOMPLETE.getStatus()));
            }
            fissionActivityPersonalIntegralVo.setTaskName(fissionDict.getName());
            FissionAwardRule fissionAwardRule = taskIdRuleMap.get(fissionDict.getCode());
            fissionActivityPersonalIntegralVo.setAwardRule(fissionAwardRule == null ? 0 : fissionAwardRule.getAwardRule().intValue());
            fissionActivityPersonalIntegralVo.setSingleIntegral(fissionAwardRule == null ? 0 : (fissionAwardRule.getAward() == null ? 0 : fissionAwardRule.getAward().intValue()));
            personalIntegral.add(fissionActivityPersonalIntegralVo);
            index++;
        }
        return personalIntegral;
    }

    /**
     * 销售的个人目标
     *
     * @return List<FissionActivityPersonalGoalVo>
     */
    private List<FissionActivityPersonalGoalVo> getPersonalGoalVo(FissionActivityInfoVo fissionActivityDetailVo, List<FissionActivityPersonalIntegralVo> personalIntegral) {
        List<FissionActivityPersonalGoalVo> personalGoal = new ArrayList<>();
        FissionActivityPersonalGoalVo personalGoalVo = null;
        fissionActivityDetailVo.setIsFinishAllTask(CompleteTaskStatus.COMPLETE.getStatus());
        for (FissionActivityPersonalIntegralVo personalIntegralVo : personalIntegral) {
            if (personalIntegralVo.getAwardRule() > 0) {
                personalGoalVo = new FissionActivityPersonalGoalVo();
                BeanUtils.copyProperties(personalIntegralVo, personalGoalVo);
                if (personalGoalVo.getWhetherCompleteTask() == CompleteTaskStatus.INCOMPLETE.getStatus()) {
                    fissionActivityDetailVo.setIsFinishAllTask(CompleteTaskStatus.INCOMPLETE.getStatus());
                }
                personalGoalVo.setGapGoal(personalGoalVo.getAwardRule() - personalGoalVo.getFinishTaskTotal());
                personalGoal.add(personalGoalVo);
                personalGoalVo.getWhetherCompleteTask();
            }
        }
        return personalGoal;
    }

    /**
     * @param fissionSale
     * @description: 更新裂变销售表
     * @return: int
     * @author: dudg
     * @date: 2020/9/27 16:18
     */
    @Override
    public int updateByPrimaryKeySelective(FissionSale fissionSale) {
        int result = fissionSaleWriteMapper.updateByPrimaryKeySelective(fissionSale);
        return result;
    }

    /**
     * @param fissionSalePayDto
     * @description: 裂变活动销售奖励审核/打款
     * @return: int
     * @author: dudg
     * @date: 2020/9/27 18:25
     */
    @Override
    public int financialAuditSalePay(FissionSalePayDto fissionSalePayDto) {
        int res = fissionSaleWriteMapper.financialAuditSalePay(fissionSalePayDto);
        if (res > 0) {
            if (IntegerUtils.isNotNullAndEquals(fissionSalePayDto.getAuditType(), 2)) {
                executorService.execute(() -> {
                    FissionActivity fissionActivity = fissionActivityService.getFissionActivityById(fissionSalePayDto.getFissionId());
                    if (fissionActivity == null) {
                        return;
                    }
                    String timeStr = GlobalConstants.dateToString("yyyy-MM-dd HH:mm:ss", new Date());
                    // 通知销售提现
                    List<FissionSale> paySaleList = fissionSaleWriteMapper.selectPayListByFissionId(fissionSalePayDto);
                    paySaleList.forEach(n -> {
                        try {
                            WxDataDto wxDataDto = new WxDataDto(
                                    "(" + fissionActivity.getActivityName() + ")奖励已到账",
                                    n.getRealIncome().toString(),
                                    "聚合燃爆活动奖励",
                                    null,
                                    timeStr,
                                    null,
                                    "快去个人中心-钱包提现吧");

                            WxTemplateInfoDto templateInfoDto = new WxTemplateInfoDto(
                                    n.getSaleWxOpenId()
                                    , fission_sale_award_template_id
                                    , fission_sale_my_wallet_url,
                                    null,
                                    wxDataDto);
                            wxTemplateMessageBaseService.sendNew(fission_app_id, templateInfoDto);
                        } catch (Exception e) {
                            e.printStackTrace();
                            CommonLogUtil.addError("微信通知销售提现 error", e.getMessage(), e);
                        }
                    });
                });
            }
        }
        return res;
    }

    /**
     * @param fissionSalePayDto
     * @description: 冻结/解冻裂变活动销售
     * @return: int
     * @author: dudg
     * @date: 2020/9/27 18:31
     */
    @Override
    public int freezeOrUnFreezeFissionSale(FissionSalePayDto fissionSalePayDto) {
        int res = fissionSaleWriteMapper.freezeOrUnFreezeFissionSale(fissionSalePayDto);
        if (res > 0) {
            boolean success = false;
            while (!success) {
                TcResponse response = realIncome(Lists.newArrayList(fissionSalePayDto.getFissionId()));
                CommonLogUtil.addInfo("冻结/解冻操作，重新计算销售实际收入：", "返回结果：", response);
                success = response.getResponseHeader().getStatus() == StatusCodeEnum.SUCCESS.getCode();
            }
        }
        return res;
    }


    /**
     * @param fissionSalePayDto
     * @description: 销售提现
     * @return: com.tuanche.arch.web.model.TcResponse
     * @author: dudg
     * @date: 2020/9/28 11:36
     */
    @Override
    public TcResponse saleWithdrawal(FissionSalePayDto fissionSalePayDto) {
        String requestId = UUID.randomUUID().toString();
        CommonLogUtil.addInfo(requestId, "销售提现,请求参数", fissionSalePayDto);
        String lockKey = "SALEWITHDRAWAL:LOCK:" + fissionSalePayDto.getSaleId();
        if (FuncUtil.getDistributeLock(redisService, lockKey, requestId, 5 * 60 * 1000)) {
            if (FuncUtil.isNullOrEquals(fissionSalePayDto.getSaleId(), 0)) {
                return new TcResponse(StatusCodeEnum.USER_NOT_LOGGED_IN.getCode(), StatusCodeEnum.USER_NOT_LOGGED_IN.getText());
            }
            if (StringUtil.isEmpty(fissionSalePayDto.getSaleWxOpenId())) {
                return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "销售微信openId，不能为空");
            }

            try {
                // 取待提现金额
                // BigDecimal waitWithdrawalAmount = selectFissionSaleWithdrawal(fissionSalePayDto.getSaleId(), WithdrawalState.WAITING_WITHDRAWAL.getStatus());
                List<FissionSale> fissionSales = fissionSaleReadMapper.selectWithdrawalFissionListBySaleId(fissionSalePayDto.getSaleId());
                BigDecimal waitWithdrawalAmount = fissionSales.stream().map(FissionSale::getRealIncome).reduce(BigDecimal.ZERO, BigDecimal::add);
                if (waitWithdrawalAmount.compareTo(BigDecimal.ZERO) <= 0) {
                    return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "可提现金额不足");
                }

                // 微信付款
                WeChatPaymentVo weChatPaymentVo = new WeChatPaymentVo();
                weChatPaymentVo.setAppId(fission_app_id);
                weChatPaymentVo.setOpenId(fissionSalePayDto.getSaleWxOpenId());
                weChatPaymentVo.setAmount(Integer.parseInt(waitWithdrawalAmount.multiply(BigDecimal.valueOf(100)).setScale(0, BigDecimal.ROUND_DOWN).toString()));
                weChatPaymentVo.setOriginAmount(waitWithdrawalAmount);
                weChatPaymentVo.setDesc("团车裂变活动销售奖励提现");
                weChatPaymentVo.setTradeType(GlobalConstants.FissionTradeType.SALE_WITHDRAWAL.getCode());
                weChatPaymentVo.setOrder_no(String.valueOf(OrderNoGenerateUtil.nextId()));
                ResultDto resultDto = fissionTradeRecordServiceImpl.wechatPayment(weChatPaymentVo);
                if (resultDto.getCode().intValue() != StatusCodeEnum.SUCCESS.getCode()) {
                    return new TcResponse(resultDto.getCode(), resultDto.getMsg());
                }

                //更新提现状态
                FissionTradeRecord tradeRecord = (FissionTradeRecord) resultDto.getResult();
                fissionSalePayDto.setWithdrawalTime(tradeRecord.getPayTime());
                fissionSalePayDto.setTradeRecordId(tradeRecord.getId());
                fissionSalePayDto.setIdList(fissionSales.stream().map(FissionSale::getId).collect(Collectors.toList()));
                fissionSaleWriteMapper.saleWithDrawal(fissionSalePayDto);

                return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), "提现成功");
            } catch (Exception e) {
                e.printStackTrace();
                CommonLogUtil.addError(requestId, "销售提现 error," + e.getMessage(), e);
                return new TcResponse(StatusCodeEnum.ERROR.getCode(), e.getMessage());
            } finally {
                FuncUtil.releaseDistributeLock(redisService, lockKey, requestId);
            }
        } else {
            return new TcResponse(StatusCodeEnum.SYSTEM_INNER_ERROR.getCode(), StatusCodeEnum.SYSTEM_INNER_ERROR.getText());
        }
    }

    /**
     * 新增裂变销售
     *
     * @param fissionSale
     * @return int
     * @author HuangHao
     * @CreatTime 2020-10-09 10:27
     */
    @Override
    public int insertFissionSale(FissionSale fissionSale) {
        if (fissionSale == null && StringUtils.isEmpty(fissionSale.getSaleWxOpenId())) {
            throw new NullPointerException("销售微信openId不能为空");
        }
        if (StringUtils.isEmpty(fissionSale.getSaleWxUnionId())) {
            throw new NullPointerException("销售微信UnionId不能为空");
        }
        if (fissionSale.getFissionId() == null) {
            throw new NullPointerException("裂变活动ID不能为空");
        }
        if (fissionSale.getDealerId() == null) {
            throw new NullPointerException("经销商ID不能为空");
        }
        if (fissionSale.getSaleId() == null) {
            throw new NullPointerException("销售ID不能为空");
        }
        if (StringUtils.isEmpty(fissionSale.getSaleName())) {
            throw new NullPointerException("销售姓名不能为空");
        }
        if (fissionSale.getPayAmount() == null) {
            throw new NullPointerException("支付金额（对赌金额）不能为空");
        }
        if (StringUtils.isEmpty(fissionSale.getOrderNo())) {
            throw new NullPointerException("订单编号不能为空");
        }
        int num = fissionSaleWriteMapper.insertSelective(fissionSale);
        return num;
    }

    /**
     * 根据FissionId和WxUnionId获取单个销售-走缓存
     *
     * @param fissionId
     * @param wxUnionId
     * @return void
     * @author HuangHao
     * @CreatTime 2021-02-19 16:52
     */
    @Override
    public FissionSale getFissionSaleByFissionIdAndWxUnionId(Integer fissionId, String wxUnionId) {
        String key = MessageFormat.format(BaseCacheKeys.FISSION_SALE_FISSIONID_WXUNIONID_CACHE.getKey(), fissionId, wxUnionId);
        FissionSale fissionSale = null;
        try {
            fissionSale = redisService.get(key, FissionSale.class);
            if (fissionSale == null) {
                FissionSale sale = new FissionSale();
                sale.setFissionId(fissionId);
                sale.setSaleWxUnionId(wxUnionId);
                fissionSale = fissionSaleWriteMapper.getFissionSale(sale);
                if (fissionSale != null) {
                    redisService.set(key, fissionSale, BaseCacheKeys.FISSION_SALE_FISSIONID_WXUNIONID_CACHE.getExpire());
                }
            }
        } catch (RedisException e) {
            e.printStackTrace();
        }
        return fissionSale;
    }

    /**
     * 销售预计收入计算
     *
     * @param fissionIds 裂变活动IDS
     * @return void
     * @author HuangHao
     * @CreatTime 2020-10-10 10:23
     */
    @Override
    public TcResponse estimatedIncome(List<Integer> fissionIds) {
        return incomeStatistics(fissionIds, GlobalConstants.FISSION_SALE_INCOME_TYPE_1);
    }

    /**
     * 销售实际收入计算
     *
     * @param fissionIds 裂变活动IDS
     * @return void
     * @author HuangHao
     * @CreatTime 2020-10-10 10:23
     */
    @Override
    public TcResponse realIncome(List<Integer> fissionIds) {
        return incomeStatistics(fissionIds, GlobalConstants.FISSION_SALE_INCOME_TYPE_2);
    }

    /**
     * 裂变活动销售收入计算
     *
     * @param fissionIds     裂变活动IDS
     * @param saleIncomeType 计算类型：1=预计收入计算 2=实际收入计算
     * @return void
     * @author HuangHao
     * @CreatTime 2020-10-10 10:13
     */
    private TcResponse incomeStatistics(List<Integer> fissionIds, Integer saleIncomeType) {
        TcResponse tcResponse = new TcResponse();
        if (fissionIds == null || fissionIds.size() == 0) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "裂变活动ID不能为空");
        }
        if (saleIncomeType == null) {
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(), "计算类型不能为空");
        }
        //上锁
        TcResponse lockResponse = incomeLock(fissionIds);
        Map<Integer, String> lockMap = null;
        //上锁成功
        if (StatusCodeEnum.SUCCESS.getCode() == lockResponse.getResponseHeader().getStatus()) {
            lockMap = (Map<Integer, String>) lockResponse.getResponse().getResult();
        } else {
            return lockResponse;
        }
        try {
            //裂变活动销售积分
            List<FissionSaleTaskStatistics> saleTotalIntegralList = fissionSaleTaskStatisticsReadMapper.getSaleTotalIntegralByFissionIds(fissionIds);
            if (saleTotalIntegralList.size() > 0) {
                //裂变活动总积分
                Map<Integer, FissionSaleTaskStatistics> totalIntegralMap = new HashMap<>();
                if (GlobalConstants.FISSION_SALE_INCOME_TYPE_2.equals(saleIncomeType)) {
                    //实际收入积分=完成任务的销售总积分
                    totalIntegralMap = fissionSaleTaskStatisticsReadMapper.getCompleteTaskTotalIntegral(fissionIds);
                } else {
                    //预计收入积分=所有销售的总积分
                    totalIntegralMap = fissionSaleTaskStatisticsReadMapper.getTotalIntegralByFissionIds(fissionIds);
                }
                if (totalIntegralMap != null) {
                    List<FissionSale> updateList = new ArrayList<>();
                    //销售奖金池金额
                    final Map<Integer, BigDecimal> salePrizePoolMap = getSalePrizePool(fissionIds);
                    final Map<Integer, FissionSaleTaskStatistics> finalTotalIntegralMap = totalIntegralMap;
                    final BigDecimal zero = new BigDecimal("0");
                    for (FissionSaleTaskStatistics integral : saleTotalIntegralList) {

//                    saleTotalIntegralList.parallelStream().forEach(integral -> {

                        Integer fissionId = integral.getFissionId();
                        FissionSaleTaskStatistics taskStatistics = finalTotalIntegralMap.get(fissionId);
                        //实际收入计算时可能有没有一个销售完成任务的情况，因此只有当该裂变活动有销售完成了任务才计算
                        if (taskStatistics != null) {
                            //是否完成了任务
                            Boolean whetherCompleteTask = integral.getWhetherCompleteTask() == null ? false : integral.getWhetherCompleteTask();
                            //当前资金池总金额
                            BigDecimal salePrizePool = salePrizePoolMap.get(fissionId);
                            BigDecimal income = null;
                            if (taskStatistics.getTaskIntegral() != null && taskStatistics.getTaskIntegral() > 0 &&
                                    integral.getTaskIntegral() != null && integral.getTaskIntegral() > 0) {
                                //裂变活动总积分
                                BigDecimal totalIntegral = new BigDecimal(taskStatistics.getTaskIntegral());
                                //销售积分
                                BigDecimal saleIntegral = new BigDecimal(integral.getTaskIntegral());
                                //销售收入=当前资金池总金额 / 参与的销售在本场裂变活动中的获取的积分总数 * 该销售的积分数
                                income = salePrizePool.divide(totalIntegral, 8, BigDecimal.ROUND_HALF_UP).multiply(saleIntegral);
                            } else {
                                income = zero;
                            }
                            //保留2位小数
                            income = income.setScale(2, BigDecimal.ROUND_HALF_UP);
                            FissionSale fissionSale = new FissionSale();
                            fissionSale.setFissionId(integral.getFissionId());
                            fissionSale.setSaleWxUnionId(integral.getSaleWxUnionId());

                            fissionSale.setWhetherCompleteTask(whetherCompleteTask);
                            fissionSale.setTaskIntegral(integral.getTaskIntegral());
                            //实际收入计算时才保存实际收入字段
                            if (GlobalConstants.FISSION_SALE_INCOME_TYPE_2.equals(saleIncomeType)) {
                                //只有完成所有任务目标才计算实际收入否则是0
                                if (whetherCompleteTask) {
                                    fissionSale.setRealIncome(income);
                                } else {
                                    fissionSale.setRealIncome(zero);
                                }
                                fissionSale.setCalculationRealIncome(true);
                            } else {
                                //定时任务原因，预计收入在活动结束前的5分钟（23:55-00:00点之间的数据）任务未进行计算
                                fissionSale.setEstimatedIncome(income);
                            }
                            updateList.add(fissionSale);
                        }
                    }
                    ;
                    updateFissionSalesIncome(updateList);
                }
            }
            tcResponse = new TcResponse(StatusCodeEnum.SUCCESS.getCode(), "成功");
        } catch (Exception e) {
            e.printStackTrace();
            tcResponse = new TcResponse(StatusCodeEnum.ERROR.getCode(), "裂变活动收入计算发生系统错误");
        } finally {
            //解锁
            incomeUnLock(lockMap);
        }
        return tcResponse;
    }

    /**
     * 更新销售收入
     *
     * @param updateList
     * @return void
     * @author HuangHao
     * @CreatTime 2020-10-10 11:58
     */
    private void updateFissionSalesIncome(List<FissionSale> updateList) {
        int listSize = updateList.size();
        if (listSize > 0) {
            //数量小于等于BATCH_PAGE_SIZE则直接更新，否则分批更新
            if (listSize <= ConstantsUtil.BATCH_PAGE_SIZE) {
                fissionSaleWriteMapper.batchUpdateSaleIncome(updateList);
            } else {
                int pageNum = ConstantsUtil.batchNum(listSize);
                for (int i = 0; i < pageNum; i++) {
                    int toIndex = (i + 1) * ConstantsUtil.BATCH_PAGE_SIZE;
                    if (toIndex > listSize) {
                        toIndex = listSize;
                    }
                    List<FissionSale> batchList = updateList.subList(i * ConstantsUtil.BATCH_PAGE_SIZE, toIndex);
                    fissionSaleWriteMapper.batchUpdateSaleIncome(batchList);
                }
            }
        }
    }

    /**
     * @param fissionId
     * @description: 获取单个裂变活动下B销售总奖金池
     * @return: java.math.BigDecimal
     * @author: dudg
     * @date: 2020/10/10 15:42
     */
    @Override
    public BigDecimal getSalePrizePoolByFissionId(Integer fissionId) {
        Map<Integer, BigDecimal> salePrizePool = getSalePrizePool(Lists.newArrayList(fissionId));
        return salePrizePool.getOrDefault(fissionId, BigDecimal.ZERO);
    }

    @Override
    public List<FissionSaleRewardReconciliationDto> getSaleRewardReconciliationDtoList(SaleRewardReconciliationDto saleRewardReconciliationDto) {
        List<FissionSaleRewardReconciliationDto> saleRewardReconciliationDtos = fissionSaleReadMapper.getSaleRewardReconciliationDtoList(saleRewardReconciliationDto.getFissionId(), saleRewardReconciliationDto.getWithdrawalBeginTime(), saleRewardReconciliationDto.getWithdrawalEndTime());
        if (!saleRewardReconciliationDtos.isEmpty()) {
            fissionSaleServiceImpl.fillFissionSaleRewardReconciliationDto(saleRewardReconciliationDtos, saleRewardReconciliationDto.getFissionId());
        }
        return saleRewardReconciliationDtos;
    }

    @Override
    public PageResult getSaleRewardReconciliationDtoPage(SaleRewardReconciliationDto saleRewardReconciliationDto) {
        PageResult<FissionSaleRewardReconciliationDto> pageResult = new PageResult<>();
        PageHelper.startPage(saleRewardReconciliationDto.getPage(), saleRewardReconciliationDto.getLimit());
        List<FissionSaleRewardReconciliationDto> saleRewardReconciliationDtos = fissionSaleReadMapper.getSaleRewardReconciliationDtoList(saleRewardReconciliationDto.getFissionId(), saleRewardReconciliationDto.getWithdrawalBeginTime(), saleRewardReconciliationDto.getWithdrawalEndTime());
        if (!saleRewardReconciliationDtos.isEmpty()) {
            fissionSaleServiceImpl.fillFissionSaleRewardReconciliationDto(saleRewardReconciliationDtos, saleRewardReconciliationDto.getFissionId());
        }
        PageInfo<FissionSaleRewardReconciliationDto> pageInfo = new PageInfo(saleRewardReconciliationDtos);
        pageResult.setCount(pageInfo.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode());
        pageResult.setMsg(StatusCodeEnum.SUCCESS.getText());
        pageResult.setData(saleRewardReconciliationDtos);
        return pageResult;
    }

    @Override
    public FissionStatisticBigDecimalDto selectPrizeActualByFissionId(Integer fissionId, Date withdrawalBeginTime, Date withdrawalEndTime) {
        return fissionStatisticsReadMapper.selectPrizeActualByFissionId(fissionId, withdrawalBeginTime, withdrawalEndTime);
    }

    private void fillFissionSaleRewardReconciliationDto(List<FissionSaleRewardReconciliationDto> saleRewardReconciliationDtos, Integer fissionId) {
        List<Integer> saleIds = new ArrayList<>();
        List<Integer> dealerIds = new ArrayList<>();
        List<Integer> cityIds = new ArrayList<>();
        for (FissionSaleRewardReconciliationDto dto : saleRewardReconciliationDtos) {
            saleIds.add(dto.getSaleId());
            dealerIds.add(dto.getDealerId());
        }
        List<CsCompany> csCompanyList = companyBaseService.getCompanyListByDealerIds(dealerIds);
        List<FissionSale> salesList = fissionSaleReadMapper.selectFissionRewardGroupSale(fissionId);
//        Map<Integer, FissionSale> salesMap = salesList.stream().collect(Collectors.toMap(FissionSale::getSaleId, Function.identity(), (key1, key2) -> key2));
        Map<Integer, CsCompany> csCompanyMap = new HashMap<>((int) (csCompanyList.size() / 0.75f + 1f));
        for (CsCompany csCompany : csCompanyList) {
            csCompanyMap.put(csCompany.getId(), csCompany);
            cityIds.add(csCompany.getCityId());
        }
        Map<Integer, DistrictOutputBaseDto> districtMap = null;
        if (!cityIds.isEmpty()) {
            DistrictBaseInputDto districtBaseInputDto = new DistrictBaseInputDto();
            districtBaseInputDto.setIds(cityIds);
            List<DistrictOutputBaseDto> districtOutputBaseDtos = iDistrictApiService.getBaseDistrictList(districtBaseInputDto);
            districtMap = districtOutputBaseDtos.stream().collect(Collectors.toMap(DistrictOutputBaseDto::getId, Function.identity(), (key1, key2) -> key2));
        }

        Map<Integer, CsUser> csUserMap = userBaseService.getCsUserByIds(saleIds);
        for (FissionSaleRewardReconciliationDto dto : saleRewardReconciliationDtos) {
//            FissionSale fissionSale = salesMap.get(dto.getSaleId());
            CsUser csUser = csUserMap.get(dto.getSaleId());
            CsCompany csCompany = csCompanyMap.get(dto.getDealerId());
            if (csUser != null) {
                dto.setPhone(csUser.getUphone());
            }
            if (csCompany != null) {
                dto.setDealerName(csCompany.getCompanyName());
                if (districtMap != null) {
                    DistrictOutputBaseDto districtOutputBaseDto = districtMap.get(csCompany.getCityId());
                    if (districtOutputBaseDto != null) {
                        dto.setCityName(districtOutputBaseDto.getName());
                    }
                }

            }
        }
    }

    /**
     * 裂变活动销售总奖金池
     *
     * @param fissionIds
     * @return java.util.Map<java.lang.Integer, java.math.BigDecimal>
     * @author HuangHao
     * @CreatTime 2020-10-09 17:34
     */
    private Map<Integer, BigDecimal> getSalePrizePool(List<Integer> fissionIds) {
        //裂变活动销售支付的对赌总金额
        Map<Integer, FissionSale> salePayAmountMap = fissionSaleReadMapper.getTotalAmountByFissionIds(fissionIds);
        //团车OR经销商的设置的奖金池金额
        Map<Integer, BigDecimal> prizePoolMap = getPrizePool(fissionIds, AwardRuleType.B.getType());
        if (salePayAmountMap.size() < 1) {
            return prizePoolMap;
        }
        //销售裂变活动总奖金池
        Map<Integer, BigDecimal> salePrizePoolMap = new HashMap<>(prizePoolMap.size());
        prizePoolMap.forEach((fissionId, prizePool) -> {
            FissionSale fissionSale = salePayAmountMap.get(fissionId);
            if (fissionSale != null && fissionSale.getPayAmount() != null) {
                //奖金池总额=团车OR经销商的设置的奖金池金额+裂变活动销售支付的对赌总金额
                salePrizePoolMap.put(fissionId, prizePool.add(fissionSale.getPayAmount()));
            } else {
                salePrizePoolMap.put(fissionId, prizePool);
            }
        });
        return salePrizePoolMap;
    }

    /**
     * 获取奖金池金额
     *
     * @param fissionIds
     * @param type       1：团车OR经销商的奖金 2：用户奖金池
     * @return java.util.Map<java.lang.Integer, java.math.BigDecimal>
     * @author HuangHao
     * @CreatTime 2020-10-09 17:21
     */
    private Map<Integer, BigDecimal> getPrizePool(List<Integer> fissionIds, Integer type) {
        Map<Integer, BigDecimal> prizePoolMap = new HashMap<>(fissionIds.size());
        for (Integer fissionId : fissionIds) {
            List<FissionAwardRule> fissionAwardRulesList = fissionActivityService.getAwardRuleListByFissionId(fissionId, type);
            if (fissionAwardRulesList != null && fissionAwardRulesList.size() > 0) {
                FissionAwardRule fissionAwardRule = fissionAwardRulesList.get(0);
                prizePoolMap.put(fissionId, fissionAwardRule.getPrizePool());
            }
        }
        return prizePoolMap;
    }

    /**
     * 是否销售有
     *
     * @return int
     * @author HuangHao
     * @CreatTime 2021-01-20 10:59
     */
    public boolean hasSale(Integer fissionId, String saleWxUnionId) {
        String key = BaseCacheKeys.FISSION_HAS_SALE.getKey() + fissionId + ":" + saleWxUnionId;
        try {
            String cach = redisService.get(key, String.class);
            if (ConstantsUtil.TRUE.equals(cach)) {
                return true;
            }
            FissionSale fissionSale = new FissionSale();
            fissionSale.setFissionId(fissionId);
            fissionSale.setSaleWxUnionId(saleWxUnionId);
            Integer hasSale = fissionSaleWriteMapper.hasSale(fissionSale);
            if (hasSale != null) {
                redisService.set(key, ConstantsUtil.TRUE, BaseCacheKeys.FISSION_HAS_SALE.getExpire());
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonLogUtil.addError(null, "销售是否存在报错了", e);
        }
        return false;
    }

    /**
     * 裂变活动收入计算上锁
     *
     * @return void
     * @author HuangHao
     * @CreatTime 2020-10-10 11:28
     */
    private TcResponse incomeLock(List<Integer> fissionIds) {
        TcResponse tcResponse = null;
        Integer id = null;
        try {
            Map<Integer, String> lockMap = new HashMap<>();
            boolean isLock = false;
            for (Integer fId : fissionIds) {
                String key = BaseCacheKeys.FISSION_INCOME_STATISTICS_LOCK.getKey() + fId;
                String lock = redisService.set(key, "0", "NX", "EX", BaseCacheKeys.FISSION_INCOME_STATISTICS_LOCK.getExpire());
                if (ConstantsUtil.OK.equals(lock)) {
                    lockMap.put(fId, RandomStringUtils.random(15));
                    CommonLogUtil.addInfo(null, FISSION_SALE_INCOME_LOG_KEY_WORD + "上锁ID为：" + fId + "的裂变活动成功");
                } else {
                    CommonLogUtil.addInfo(null, FISSION_SALE_INCOME_LOG_KEY_WORD + "上锁ID为：" + fId + "的裂变活动失败，原因：该裂变活动已经上锁");
                    id = fId;
                    break;
                }
            }
            if (isLock) {
                incomeUnLock(lockMap);
                tcResponse = new TcResponse(StatusCodeEnum.DATA_ALREADY_EXISTED.getCode(), "ID为：" + id + "的裂变活动正在计算销售收入，无法重复计算");
            } else {
                tcResponse = new TcResponse(StatusCodeEnum.SUCCESS.getCode(), "上锁成功", lockMap);
            }
        } catch (RedisException e) {
            e.printStackTrace();
            tcResponse = new TcResponse(StatusCodeEnum.ERROR.getCode(), "上锁失败，原因：redis发生错误，");
        }
        return tcResponse;
    }

    /**
     * 裂变活动收入计算解锁
     *
     * @param lockMap
     * @return void
     * @author HuangHao
     * @CreatTime 2020-10-10 11:28
     */
    private void incomeUnLock(Map<Integer, String> lockMap) {
        if (lockMap != null && lockMap.size() > 0) {
            lockMap.forEach((fissionId, value) -> {
                String key = BaseCacheKeys.FISSION_INCOME_STATISTICS_LOCK.getKey() + fissionId;
                try {
                    String v = redisService.get(key, String.class);
                    if (value.equals(v)) {
                        redisService.del(key);
                        CommonLogUtil.addInfo(null, FISSION_SALE_INCOME_LOG_KEY_WORD + "解锁ID为：" + fissionId + "的裂变活动成功");
                    } else {
                        CommonLogUtil.addInfo(null, FISSION_SALE_INCOME_LOG_KEY_WORD + "解锁ID为：" + fissionId + "的裂变活动失败，原因：解锁的Value=" + value + "，现存锁的Value=" + v);
                    }
                } catch (RedisException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
