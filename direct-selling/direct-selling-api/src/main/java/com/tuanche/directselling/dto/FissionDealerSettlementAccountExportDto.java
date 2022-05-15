package com.tuanche.directselling.dto;

import java.math.BigDecimal;

public class FissionDealerSettlementAccountExportDto extends FissionDealerSettlementAccountDto {

    private String bankCardNameMan;
    private String bankCardNumberMan;
    private String bankNameMan;
    private BigDecimal realIncome;

    public String getBankCardNameMan() {
        return bankCardNameMan;
    }

    public void setBankCardNameMan(String bankCardNameMan) {
        this.bankCardNameMan = bankCardNameMan;
    }

    public String getBankCardNumberMan() {
        return bankCardNumberMan;
    }

    public void setBankCardNumberMan(String bankCardNumberMan) {
        this.bankCardNumberMan = bankCardNumberMan;
    }

    public String getBankNameMan() {
        return bankNameMan;
    }

    public void setBankNameMan(String bankNameMan) {
        this.bankNameMan = bankNameMan;
    }
}
