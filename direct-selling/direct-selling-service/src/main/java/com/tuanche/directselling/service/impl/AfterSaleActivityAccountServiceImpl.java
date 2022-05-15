package com.tuanche.directselling.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.tuanche.arch.commonmodel.resp.TcRpcResponse;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.directselling.api.AfterSaleActivityAccountService;
import com.tuanche.directselling.dto.AfterSaleActivityAccountDto;
import com.tuanche.directselling.mapper.read.directselling.AfterSaleActivityAccountReadMapper;
import com.tuanche.directselling.mapper.write.directselling.AfterSaleActivityAccountWriteMapper;
import com.tuanche.directselling.model.AfterSaleActivity;
import com.tuanche.directselling.model.AfterSaleActivityAccount;
import com.tuanche.directselling.util.CommonLogUtil;
import com.tuanche.directselling.utils.AfterSaleConstants;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.pay.out.enumeration.ProfitSharingDetailResultEnum;
import com.tuanche.pay.out.service.PayProfitsharingService;
import com.tuanche.pay.out.vo.TcPayProfitsharingDetailVo;
import com.tuanche.pay.out.vo.TcPayProfitsharingReceiverVo;
import com.tuanche.pay.out.vo.TcPayProfitsharingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * @program: direct-selling
 * @description: 分账信息服务实现类
 * @author: lvsen
 * @create: 2021-12-15 13:44
 **/
@Service
public class AfterSaleActivityAccountServiceImpl implements AfterSaleActivityAccountService {
    @Autowired
    AfterSaleActivityAccountWriteMapper accountWriteMapper;

    @Autowired
    AfterSaleActivityAccountReadMapper accountReadMapper;
    @Autowired
    AfterSaleOrderProfitSharingServiceImpl profitSharingService;
    @Reference(timeout = 1200000)
    PayProfitsharingService payProfitsharingService;


    @Override
    public ResultDto saveAndVerifyAfterSaleActivityAccount(AfterSaleActivityAccount afterSaleActivityAccount, Integer optUserId) {
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(StatusCodeEnum.ERROR.getCode());
        if (Objects.isNull(afterSaleActivityAccount) || Objects.isNull(afterSaleActivityAccount.getActivityId()) || Objects.isNull(afterSaleActivityAccount.getDealerId())) {
            resultDto.setMsg("数据缺失");
            return resultDto;
        }
        afterSaleActivityAccount.setApproveMaterials(afterSaleActivityAccount.getApproveMaterials1() + "##" + afterSaleActivityAccount.getApproveMaterials2() + "##" +
                afterSaleActivityAccount.getApproveMaterials3() + "##" + afterSaleActivityAccount.getApproveMaterials4() + "##" + afterSaleActivityAccount.getApproveMaterials5() + "##L");
        TcPayProfitsharingReceiverVo addReceiverVo = new TcPayProfitsharingReceiverVo();
        addReceiverVo.setType("MERCHANT_ID");
        addReceiverVo.setAccount(afterSaleActivityAccount.getMchId());
        addReceiverVo.setName(afterSaleActivityAccount.getMchName());
        addReceiverVo.setRelationType("PARTNER");
        if (Objects.isNull(afterSaleActivityAccount.getId())) {
            List<TcPayProfitsharingReceiverVo> receiverVoList = new ArrayList<>();
            receiverVoList.add(addReceiverVo);
            TcRpcResponse tcRpcResponse = payProfitsharingService.addReceivers("104", receiverVoList);
            CommonLogUtil.addInfo("创建商户分账账户", "创建商户分账账户接口返回结果：" + JSON.toJSONString(tcRpcResponse));
            //商户号开通后才能保存
            if (tcRpcResponse.isSuccess()) {
                afterSaleActivityAccount.setCtreateBy(optUserId);
                afterSaleActivityAccount.setCtreateDt(new Date());
                accountWriteMapper.insertSelective(afterSaleActivityAccount);
                resultDto.setCode(StatusCodeEnum.SUCCESS.getCode());
                resultDto.setMsg(afterSaleActivityAccount.getId().toString());
                return resultDto;
            } else {
                resultDto.setMsg(tcRpcResponse.getMsg());
                return resultDto;
            }
        } else {
            AfterSaleActivityAccount oldAccount = accountReadMapper.selectByPrimaryKey(afterSaleActivityAccount.getId());
            if (oldAccount.getMchId().equals(afterSaleActivityAccount.getMchId()) && oldAccount.getMchName().equals(afterSaleActivityAccount.getMchName())) {
                afterSaleActivityAccount.setUpdateBy(optUserId);
                afterSaleActivityAccount.setUpdateDt(new Date());
                accountWriteMapper.updateByPrimaryKeySelective(afterSaleActivityAccount);
                resultDto.setCode(StatusCodeEnum.SUCCESS.getCode());
                resultDto.setMsg(afterSaleActivityAccount.getId().toString());
                return resultDto;
            } else {
                //商户号 主体有变动，先删除，再添加
                List<TcPayProfitsharingReceiverVo> receiverVoList = new ArrayList<>();
                TcPayProfitsharingReceiverVo delReceiverVo = new TcPayProfitsharingReceiverVo();
                delReceiverVo.setType("MERCHANT_ID");
                delReceiverVo.setAccount(oldAccount.getMchId());
                receiverVoList.add(delReceiverVo);
                TcRpcResponse delTcRpcResponse = payProfitsharingService.deleteReceivers("104", receiverVoList);
                CommonLogUtil.addInfo("删除商户分账账户", "创建商户分账账户接口返回结果：" + JSON.toJSONString(delTcRpcResponse));
                receiverVoList.clear();
                receiverVoList.add(addReceiverVo);
                TcRpcResponse tcRpcResponse = payProfitsharingService.addReceivers("104", receiverVoList);
                CommonLogUtil.addInfo("创建商户分账账户", "创建商户分账账户接口返回结果：" + JSON.toJSONString(tcRpcResponse));
                if (tcRpcResponse.isSuccess()) {
                    afterSaleActivityAccount.setUpdateBy(optUserId);
                    afterSaleActivityAccount.setUpdateDt(new Date());
                    accountWriteMapper.updateByPrimaryKeySelective(afterSaleActivityAccount);
                    resultDto.setCode(StatusCodeEnum.SUCCESS.getCode());
                    resultDto.setMsg(afterSaleActivityAccount.getId().toString());
                    return resultDto;
                } else {
                    resultDto.setMsg(tcRpcResponse.getMsg());
                    return resultDto;
                }
            }
        }
    }

    @Override
    public AfterSaleActivityAccount saveAfterSaleActivityAccount(AfterSaleActivityAccount afterSaleActivityAccount, Integer optUserId) {
        if (Objects.isNull(afterSaleActivityAccount) || Objects.isNull(afterSaleActivityAccount.getActivityId()) || Objects.isNull(afterSaleActivityAccount.getDealerId())) {
            return null;
        }
        afterSaleActivityAccount.setApproveMaterials(afterSaleActivityAccount.getApproveMaterials1() + "##" + afterSaleActivityAccount.getApproveMaterials2() + "##" +
                afterSaleActivityAccount.getApproveMaterials3() + "##" + afterSaleActivityAccount.getApproveMaterials4() + "##" + afterSaleActivityAccount.getApproveMaterials5() + "##L");
        if (Objects.isNull(afterSaleActivityAccount.getId())) {
            afterSaleActivityAccount.setCtreateBy(optUserId);
            afterSaleActivityAccount.setCtreateDt(new Date());
            accountWriteMapper.insertSelective(afterSaleActivityAccount);
            return afterSaleActivityAccount;
        } else {
            afterSaleActivityAccount.setUpdateBy(optUserId);
            afterSaleActivityAccount.setUpdateDt(new Date());
            accountWriteMapper.updateByPrimaryKeySelective(afterSaleActivityAccount);
            return afterSaleActivityAccount;
        }
    }

    /**
     * 查询分账账号
     *
     * @param activityAccount
     * @return
     */
    @Override
    public List<AfterSaleActivityAccount> getAfterSaleActivityAccount(AfterSaleActivityAccount activityAccount) {
        if (Objects.isNull(activityAccount)) {
            return new ArrayList<>();
        }
        List<AfterSaleActivityAccount> afterSaleActivityAccounts = accountReadMapper.selectListByAccount(activityAccount);
        for (AfterSaleActivityAccount afterSaleActivityAccount : afterSaleActivityAccounts) {
            transform(afterSaleActivityAccount);
        }
        return afterSaleActivityAccounts;
    }

    @Override
    public AfterSaleActivityAccount getAfterSaleActivityAccountById(Integer id) {
        AfterSaleActivityAccount account = accountReadMapper.selectByPrimaryKey(id);
        transform(account);
        return account;
    }

    @Override
    public AfterSaleActivityAccount getAfterSaleActivityAccountByActivityId(Integer activityId) {
        AfterSaleActivityAccount account = accountReadMapper.selectByActivityId(activityId, null);
        transform(account);
        return account;
    }

    private void transform(AfterSaleActivityAccount account) {
        if (!Objects.isNull(account) && !StringUtils.isEmpty(account.getApproveMaterials())) {
            String[] pics = account.getApproveMaterials().split("##");
            account.setApproveMaterials1(pics[0]);
            account.setApproveMaterials2(pics[1]);
            account.setApproveMaterials3(pics[2]);
            account.setApproveMaterials4(pics[3]);
            account.setApproveMaterials5(pics[4]);
        }
    }

    @Override
    public AfterSaleActivityAccount updateAfterSaleActivityAccount(AfterSaleActivityAccount afterSaleActivityAccount, Integer optUserId) {
        afterSaleActivityAccount.setUpdateBy(optUserId);
        afterSaleActivityAccount.setUpdateDt(new Date());
        accountWriteMapper.updateByPrimaryKeySelective(afterSaleActivityAccount);
        return afterSaleActivityAccount;
    }

    /**
     * 验证分账
     * @param account
     * @param activity
     * @param optUser
     * @return
     */
    @Override
    public ResultDto verfiyProfitsharing(AfterSaleActivityAccount account, AfterSaleActivity activity, Integer optUser) {
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(StatusCodeEnum.SYSTEM_INNER_ERROR.getCode());
        resultDto.setMsg(StatusCodeEnum.SYSTEM_INNER_ERROR.getText());
        BigDecimal oneHundred = new BigDecimal("100");
        String keyWord = "验证分账" + account.getMchId();
        //订单金额
        BigDecimal orderMoney = account.getOrderMoney();
        //经销商分账比例=（100-团车分账比例）/100
        BigDecimal dealerSubAccountRatio = (oneHundred.subtract(account.getSubAccountRatio())).divide(oneHundred);
        //分账金额
        BigDecimal distributionAmount = orderMoney.multiply(dealerSubAccountRatio).setScale(2, BigDecimal.ROUND_HALF_UP);
        TcRpcResponse<TcPayProfitsharingVo> profitsharingResponse = profitSharingService.profitsharing(account.getOrderNo(), account.getSubAccountTransactionDetailId(), account.getMchId(), distributionAmount, "验证分账" + account.getMchId());
        CommonLogUtil.addInfo(null, keyWord + "-分账接口返回结果：" + JSON.toJSONString(profitsharingResponse));
        if (!profitsharingResponse.getCode().equals(StatusCodeEnum.SUCCESS.getCode())) {
            CommonLogUtil.addInfo(null, keyWord + "-分账失败,原因：" + profitsharingResponse.getMsg());
            account.setVerifyState(AfterSaleConstants.SubAccountVerifyState.STATE_120.getCode());
            account.setFailReason(profitsharingResponse.getMsg());
            updateAfterSaleActivityAccount(account, optUser);
        } else {
            TcPayProfitsharingVo payProfitsharingVo = profitsharingResponse.getData();
            if (payProfitsharingVo != null || CollectionUtils.isEmpty(payProfitsharingVo.getPayProfitsharingDetailVoList())) {
                List<TcPayProfitsharingDetailVo> payProfitsharingDetailVoList = payProfitsharingVo.getPayProfitsharingDetailVoList();
                TcPayProfitsharingDetailVo profitsharingDetailVo = payProfitsharingDetailVoList.get(0);
                if (ProfitSharingDetailResultEnum.PENDING.getValue().equals(profitsharingDetailVo.getResult())) {
                    account.setVerifyState(AfterSaleConstants.SubAccountVerifyState.STATE_120.getCode());
                    account.setFailReason("分账处理中，稍后请再点一次分账");
                    updateAfterSaleActivityAccount(account, optUser);
                    CommonLogUtil.addInfo(null, keyWord + "-分账处理中");
                } else if (ProfitSharingDetailResultEnum.SUCCESS.getValue().equals(profitsharingDetailVo.getResult())) {
                    account.setVerifyState(AfterSaleConstants.SubAccountVerifyState.STATE_110.getCode());
                    account.setFailReason("");
                    updateAfterSaleActivityAccount(account, optUser);
                    resultDto.setCode(StatusCodeEnum.SUCCESS.getCode());
                    resultDto.setMsg(StatusCodeEnum.SUCCESS.getText());
                    CommonLogUtil.addInfo(null, keyWord + "-分账成功");
                } else {
                    CommonLogUtil.addInfo(null, keyWord + "-分账失败,原因：" + profitsharingResponse.getMsg());
                    account.setVerifyState(AfterSaleConstants.SubAccountVerifyState.STATE_120.getCode());
                    account.setFailReason(profitsharingResponse.getMsg());
                    updateAfterSaleActivityAccount(account, optUser);
                }
            } else {
                CommonLogUtil.addInfo(null, keyWord + "-分账失败,原因：" + profitsharingResponse.getMsg());
                account.setVerifyState(AfterSaleConstants.SubAccountVerifyState.STATE_120.getCode());
                account.setFailReason(profitsharingResponse.getMsg());
                updateAfterSaleActivityAccount(account, optUser);
            }
        }
        return resultDto;
    }

    /**
     * 获取账号列表
     * @author HuangHao
     * @CreatTime 2021-12-21 10:26
     * @param accountDto
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivityAccount>
     */
    @Override
    public List<AfterSaleActivityAccount> getActivityAccountList(AfterSaleActivityAccountDto accountDto){
        if(accountDto == null){
            return null;
        }
        return accountReadMapper.getActivityAccountList(accountDto);
    }

    /**
     * 根据活动ID获取获取账号信息
     * @author HuangHao
     * @CreatTime 2021-12-21 10:51
     * @param accountDto
     * @return java.util.Map<java.lang.Integer,com.tuanche.directselling.model.AfterSaleActivityAccount>
     */
    @Override
    public Map<Integer,AfterSaleActivityAccount> getActivityAccountMapByActivityIds(Set<Integer> activityIds){
        if(activityIds==null || activityIds.size()<1){
            return null;
        }
        AfterSaleActivityAccountDto accountDto = new AfterSaleActivityAccountDto();
        accountDto.setActivityIds(activityIds);
        List<AfterSaleActivityAccount> list = getActivityAccountList(accountDto);
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        Map<Integer,AfterSaleActivityAccount> map = new HashMap<>((int)(list.size()*1.5));
        for (AfterSaleActivityAccount account : list) {
            map.put(account.getActivityId(), account);
        }
        return map;
    }

}
