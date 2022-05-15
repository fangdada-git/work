package com.tuanche.directselling.api;

import com.tuanche.directselling.model.AfterSaleAmountAlarm;

public interface AfterSaleAmountAlarmService {

    AfterSaleAmountAlarm getAfterSaleAmountAlarm ();

    int addAfterSaleAmountAlarm (AfterSaleAmountAlarm afterSaleAmountAlarm);

    int updateAfterSaleAmountAlarm (AfterSaleAmountAlarm afterSaleAmountAlarm);

}
