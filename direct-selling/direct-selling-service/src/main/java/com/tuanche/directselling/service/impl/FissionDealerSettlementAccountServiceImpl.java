package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.directselling.api.FissionActivityService;
import com.tuanche.directselling.api.FissionDealerSettlementAccountService;
import com.tuanche.directselling.api.FissionGoodsOrderRecordservice;
import com.tuanche.directselling.dto.FissionDealerOrderAmountDto;
import com.tuanche.directselling.dto.FissionDealerSettlementAccountDto;
import com.tuanche.directselling.dto.FissionGoodsOrderRecordDto;
import com.tuanche.directselling.enums.BrankAccountType;
import com.tuanche.directselling.enums.FissionGoodsOrderStatus;
import com.tuanche.directselling.mapper.read.directselling.FissionDealerSettlementAccountReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FissionGoodsOrderRecordReadMapper;
import com.tuanche.directselling.mapper.read.directselling.FissionSaleReadMapper;
import com.tuanche.directselling.mapper.write.directselling.FissionDealerSettlementAccountWriteMapper;
import com.tuanche.directselling.model.FissionActivity;
import com.tuanche.directselling.model.FissionDealerSettlementAccount;
import com.tuanche.directselling.model.FissionGoodsOrderRecord;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.eap.api.bean.manufacturer.CsDealerFinancial;
import com.tuanche.eap.api.enums.DealerFinancialAccountTypeEnum;
import com.tuanche.eap.api.service.manufacturer.CsDealerFinancialService;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.merchant.business.api.order.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author：HuangHao
 * @CreatTime 2021-03-09 11:32
 */
@Service
public class FissionDealerSettlementAccountServiceImpl implements FissionDealerSettlementAccountService {

    @Autowired
    FissionDealerSettlementAccountWriteMapper fissionDealerSettlementAccountWriteMapper;
    @Autowired
    FissionDealerSettlementAccountReadMapper fissionDealerSettlementAccountReadMapper;
    @Autowired
    FissionGoodsOrderRecordReadMapper fissionGoodsOrderRecordReadMapper;
    @Autowired
    FissionSaleReadMapper fissionSaleReadMapper;
    @Autowired
    FissionActivityService fissionActivityService;
    @Reference
    private IOrderService iOrderService;
    @Reference
    private CsDealerFinancialService csDealerFinancialService;
    @Reference
    private CompanyBaseService companyBaseService;
    @Autowired
    private FissionGoodsOrderRecordservice fissionGoodsOrderRecordservice;
    /**
     * 通过主键获取单个结算账户
     * @author HuangHao
     * @CreatTime 2021-03-09 11:29
     * @param null
     * @return
     */
    public FissionDealerSettlementAccount getDealerSettlementAccountById(Integer id){
        if(id == null){
            return null;
        }
        return fissionDealerSettlementAccountReadMapper.getDealerSettlementAccountById(id);
    }
    /**
     * 经销商获取结算账号列表-分页
     * @author HuangHao
     * @CreatTime 2021-03-09 11:41
     * @param settlementAccount
     * @return com.tuanche.directselling.utils.PageResult<com.tuanche.directselling.dto.FissionDealerSettlementAccountDto>
     */
    @Override
    public PageResult<FissionDealerSettlementAccountDto> getDealerListByPage(FissionDealerSettlementAccountDto settlementAccount){
        PageResult<FissionDealerSettlementAccountDto> pageResult = new PageResult<>();
        if(settlementAccount == null || settlementAccount.getDealerId() == null){
            return pageResult;
        }
        return getFissionDealerSettlementAccountList(settlementAccount);
    }

    @Override
    public List<FissionDealerSettlementAccountDto> getSettlementAccountList(FissionDealerSettlementAccountDto settlementAccount) {
        return fissionDealerSettlementAccountReadMapper.getSettlementAccountList(settlementAccount);
    }

    @Override
    public PageResult getFissionDealerSettlementAccountList(FissionDealerSettlementAccountDto settlementAccount){
        PageResult<FissionDealerSettlementAccountDto> pageResult = new PageResult<>();
        if(settlementAccount == null) {
            return pageResult;
        }
        PageHelper.startPage(settlementAccount.getPage(), settlementAccount.getLimit());
        List<FissionDealerSettlementAccountDto> list = fissionDealerSettlementAccountReadMapper.getSettlementAccountList(settlementAccount);
        PageInfo<FissionDealerSettlementAccountDto> page = new PageInfo<>(list);
        if(!CollectionUtils.isEmpty(list)){
            List<FissionGoodsOrderRecord> recordList = new ArrayList<>();
            for (FissionDealerSettlementAccountDto settlementAccountDto : list) {
                FissionGoodsOrderRecord record = new FissionGoodsOrderRecord();
                record.setFissionId(settlementAccountDto.getFissionId());
                record.setDealerId(settlementAccountDto.getDealerId());
                recordList.add(record);
            }
            Map<String, FissionGoodsOrderRecordDto> writeOffDataMap = fissionGoodsOrderRecordservice.getDealerWriteOffData(recordList);
            for (FissionDealerSettlementAccountDto settlementAccountDto : list) {
                String key = settlementAccountDto.getFissionId()+":"+settlementAccountDto.getDealerId();
                FissionGoodsOrderRecordDto orderRecordDto = writeOffDataMap.get(key);
                if (orderRecordDto!=null) {
                    //核销订单数
                    settlementAccountDto.setOrderCheckoutSum(orderRecordDto.getOrderCheckoutSum());
                    //核销订单金额
                    settlementAccountDto.setOrderAmount(orderRecordDto.getOrderAmount());
                }
            }
        }
        pageResult.setCount(page.getTotal());
        pageResult.setCode(StatusCodeEnum.SUCCESS.getCode() + "");
        pageResult.setMsg("");
        pageResult.setData(list);
        return pageResult;
    }
    /**
     * 获取经销商的结算账户
     * @author HuangHao
     * @CreatTime 2021-03-09 11:41
     * @param settlementAccount
     * @return com.tuanche.directselling.utils.PageResult<com.tuanche.directselling.dto.FissionDealerSettlementAccountDto>
     */
    public FissionDealerSettlementAccount getDealerSettlementAccount(FissionDealerSettlementAccount settlementAccount){
        if(settlementAccount == null || settlementAccount.getFissionId() == null || settlementAccount.getDealerId() == null){
            return null;
        }
        FissionDealerSettlementAccount account = fissionDealerSettlementAccountReadMapper.getDealerSettlementAccount(settlementAccount);
        //如果为空则获取最后一次配置的账户
        if(account == null){
            account = fissionDealerSettlementAccountReadMapper.getDealerLastSettlementAccount(settlementAccount.getDealerId());
        }
        return account;
    }

    /**
     * 获取经销商最后一个结算账户
     * @author HuangHao
     * @CreatTime 2021-03-12 10:53
     * @param dealerId
     * @return com.tuanche.directselling.model.FissionDealerSettlementAccount
     */
    public FissionDealerSettlementAccount getDealerLastSettlementAccount(Integer dealerId){
        if(dealerId == null){
            return null;
        }
        return fissionDealerSettlementAccountReadMapper.getDealerLastSettlementAccount(dealerId);
    }

    /**
     *
     * @author HuangHao
     * @CreatTime 2021-03-12 11:42
     * @param fissionId
     * @param dealerId
     * @return java.lang.Integer
     */
    public Integer getDealerSettlementAccountId(Integer fissionId,Integer dealerId){
        if(fissionId == null || dealerId == null){
            return null;
        }
        FissionDealerSettlementAccount fissionDealerSettlementAccount = new FissionDealerSettlementAccount();
        fissionDealerSettlementAccount.setFissionId(fissionId);
        fissionDealerSettlementAccount.setDealerId(dealerId);
        return fissionDealerSettlementAccountReadMapper.getDealerSettlementAccountId(fissionDealerSettlementAccount);
    }

    /**
     * 保存结算账户
     * @author HuangHao
     * @CreatTime 2021-03-09 15:25
     * @param settlementAccount
     * @return com.tuanche.arch.web.model.TcResponse
     */
    public TcResponse save(FissionDealerSettlementAccount settlementAccount){
        if(settlementAccount == null || settlementAccount.getFissionId() == null){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"列表活动ID不能为空");
        }
        if(settlementAccount.getDealerId() == null){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"经销商ID不能为空");
        }
        if(settlementAccount.getAccountType() == null){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"账户类型不能为空");
        //对公账户
        }else if(settlementAccount.getAccountType().equals(BrankAccountType.ACCOUNT_TYPE_1.getCode()) ){
            if(StringUtils.isEmpty(settlementAccount.getBankCardNumber())){
                return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"门店对公账号不能为空");
            }
            if(StringUtils.isEmpty(settlementAccount.getBankName())){
                return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"开户行全称不能为空");
            }
            /*if(StringUtils.isEmpty(settlementAccount.getBankAddress())){
                return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"开户行地址不能为空");
            }
            if(StringUtils.isEmpty(settlementAccount.getBankCardName())){
                return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"账户填写人姓名不能为空");
            }
            if(StringUtils.isEmpty(settlementAccount.getBankCardPhone())){
                return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"账户填写人手机号不能为空");
            }*/
        //个人账户
        }else if(settlementAccount.getAccountType().equals(BrankAccountType.ACCOUNT_TYPE_2.getCode()) ){
            if(StringUtils.isEmpty(settlementAccount.getBankCardName())){
                return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"收款人姓名不能为空");
            }
            if(StringUtils.isEmpty(settlementAccount.getBankCardNumber())){
                return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"银行卡号不能为空");
            }
            if(StringUtils.isEmpty(settlementAccount.getBankName())){
                return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"开户行名称不能为空");
            }
        }else{
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"账户类型不正确");
        }
        return addOrUpdate(settlementAccount);
    }

    /**
     * 新增或更新结算账户
     * @author HuangHao
     * @CreatTime 2021-04-22 14:05
     * @param settlementAccount
     * @return com.tuanche.arch.web.model.TcResponse
     */
    @Override
    public TcResponse addOrUpdate(FissionDealerSettlementAccount settlementAccount){
        FissionActivity fissionActivity = fissionActivityService.getFissionActivityById(settlementAccount.getFissionId());
        if(fissionActivity == null){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"活动不存在");
        }
        //新增
        if(settlementAccount.getId() == null){
            FissionDealerSettlementAccount account = fissionDealerSettlementAccountReadMapper.getDealerSettlementAccount(settlementAccount);
            if(account != null){
                return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"操作失败，原因：该场活动已经添加了结算账户");
            }
            fissionDealerSettlementAccountWriteMapper.insert(settlementAccount);
            //更新
        }else{
            fissionDealerSettlementAccountWriteMapper.updateById(settlementAccount);
        }
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(),"成功");
    }

    @Override
    public TcResponse addDealerSettlementAccount(FissionDealerSettlementAccount settlementAccount) {
        FissionActivity fissionActivity = fissionActivityService.getFissionActivityById(settlementAccount.getFissionId());
        if(fissionActivity == null){
            return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"活动不存在");
        }
        //新增
        if(settlementAccount.getId() == null){
            FissionDealerSettlementAccount account = fissionDealerSettlementAccountReadMapper.getDealerSettlementAccount(settlementAccount);
            if(account != null){
                return new TcResponse(StatusCodeEnum.PARAM_IS_INVALID.getCode(),"操作失败，原因：该场活动已经添加了结算账户");
            }
            fissionDealerSettlementAccountWriteMapper.insert(settlementAccount);
        }
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(),"成功");
    }

    @Override
    public void remit (Integer id) {
        FissionDealerSettlementAccount account = new FissionDealerSettlementAccountDto();
        account.setId(id);
        account.setPaymentStatus(true);
        fissionDealerSettlementAccountWriteMapper.updateById(account);
    }

    /**
     * 通过裂变ID获取结算账户信息，返回Map<dealerId,id>
     * @author HuangHao
     * @CreatTime 2021-04-07 15:21
     * @param fissionId
     * @return java.util.Map<java.lang.Integer,java.lang.Integer>
     */
    @Override
    public Map<Integer,Integer> getMapByFissionId(Integer fissionId){
        return fissionDealerSettlementAccountReadMapper.getMapByFissionId(fissionId);
    }

    /**
     * 批量插入结算账户
     * @author HuangHao
     * @CreatTime 2021-04-07 15:56
     * @param list
     * @return int
     */
    @Override
    public int batchInsert(List<FissionDealerSettlementAccount> list) {
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        List<CsDealerFinancial> dealerFinancials = new ArrayList<>();
        CsDealerFinancial csDealerFinancial = null;
        for (FissionDealerSettlementAccount dealerSettlementAccount : list) {
            csDealerFinancial = new CsDealerFinancial();
            csDealerFinancial.setDealerId(dealerSettlementAccount.getDealerId());
            csDealerFinancial.setAccountType(0);
            csDealerFinancial.setCompany(dealerSettlementAccount.getDealerName());
            csDealerFinancial.setCardNumber(dealerSettlementAccount.getBankCardNumber());
            csDealerFinancial.setCardBank(dealerSettlementAccount.getBankName());
            csDealerFinancial.setCardBankAddress(dealerSettlementAccount.getBankAddress());
            csDealerFinancial.setCreateDt(new Date());
            dealerFinancials.add(csDealerFinancial);
        }
        csDealerFinancialService.batchInsertIgnoreExist(dealerFinancials);
        return fissionDealerSettlementAccountWriteMapper.batchInsert(list);
    }

    @Override
    public void remitBatch(List<Integer> ids) {
        fissionDealerSettlementAccountWriteMapper.updatePaymentStatusByIds(ids);
    }

    @Override
    public Integer deleteDealerSettlement(Integer fissionId, Integer dealerId) {
        return fissionDealerSettlementAccountWriteMapper.deleteDealerLastSettlementAccountByFissionIdAndDealerId(fissionId, dealerId);
    }

    @Override
    public TcResponse syncDealerSettlement(FissionDealerSettlementAccount settlementAccount) {
        FissionDealerSettlementAccount dealerSettlementAccount = fissionDealerSettlementAccountReadMapper.getDealerSettlementAccount(settlementAccount);
        CsDealerFinancial csDealerFinancial = csDealerFinancialService.getCsDealerFinancial(settlementAccount.getDealerId(),
                settlementAccount.getAccountType().equals(BrankAccountType.ACCOUNT_TYPE_1.getCode()) ? DealerFinancialAccountTypeEnum.AccountType0.getType() : DealerFinancialAccountTypeEnum.AccountType1.getType());
        if (dealerSettlementAccount == null) {
            if (settlementAccount.getAccountType().equals(BrankAccountType.ACCOUNT_TYPE_1.getCode())) {
                settlementAccount.setBankAddress(csDealerFinancial.getCardBankAddress());
            } else {
                settlementAccount.setBankCardName(csDealerFinancial.getCompany());
            }
            settlementAccount.setBankName(csDealerFinancial.getCardBank());
            settlementAccount.setBankCardNumber(csDealerFinancial.getCardNumber());
            settlementAccount.setAccountType(settlementAccount.getAccountType());
            settlementAccount.setDutyParagraph(csDealerFinancial.getDutyParagraph());
            settlementAccount.setCreateDt(new Date());
            fissionDealerSettlementAccountWriteMapper.insert(settlementAccount);
        } else {
            if (settlementAccount.getAccountType().equals(BrankAccountType.ACCOUNT_TYPE_1.getCode())) {
                settlementAccount.setBankName(csDealerFinancial.getCardBank()== null ? "" : csDealerFinancial.getCardBank());
                settlementAccount.setBankCardNumber(csDealerFinancial.getCardNumber()== null ? "" : csDealerFinancial.getCardNumber());
                settlementAccount.setBankCardName("");
                settlementAccount.setBankAddress(csDealerFinancial.getCardBankAddress()== null ? "" : csDealerFinancial.getCardBankAddress());
                settlementAccount.setDutyParagraph(csDealerFinancial.getDutyParagraph()== null ? "" : csDealerFinancial.getDutyParagraph());
            } else {
                settlementAccount.setBankName(csDealerFinancial.getCardBank() == null ? "" : csDealerFinancial.getCardBank());
                settlementAccount.setBankCardNumber(csDealerFinancial.getCardNumber() == null ? "" : csDealerFinancial.getCardNumber());
                settlementAccount.setBankCardName(csDealerFinancial.getCompany() == null ? "" : csDealerFinancial.getCompany());
                settlementAccount.setBankAddress("");
                settlementAccount.setDutyParagraph("");
            }
            settlementAccount.setId(dealerSettlementAccount.getId());
            settlementAccount.setUpdateDt(new Date());
            fissionDealerSettlementAccountWriteMapper.updateById(settlementAccount);
        }
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), "成功");
    }
}
