package com.tuanche.directselling.utils;

import com.tuanche.directselling.enums.BrankAccountType;
import com.tuanche.eap.api.enums.DealerFinancialAccountTypeEnum;

/**
 * @author：HuangHao
 * @CreatTime 2021-04-25 15:00
 */
public class SettlementAccountTypeUtil {

    /**
     * 经销商结算账户类型转成经销商账户类型
     * @author HuangHao
     * @CreatTime 2021-04-25 15:02
     * @param
     * @return java.lang.Integer
     */
    public static Integer toFinancealAccountType(Integer accountType){
        if(accountType == null){
            return null;
        }else if(BrankAccountType.ACCOUNT_TYPE_1.getCode().equals(accountType)){
            return DealerFinancialAccountTypeEnum.AccountType0.getType();
        }else if(BrankAccountType.ACCOUNT_TYPE_2.getCode().equals(accountType)){
            return DealerFinancialAccountTypeEnum.AccountType1.getType();
        }
        return null;
    }
}
