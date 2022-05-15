package com.tuanche.directselling.util;

import com.tuanche.directselling.dto.AfterSaleAgentStatDto;
import com.tuanche.directselling.enums.AfterSaleAgentSortType;

import java.util.Comparator;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/7/28 15:14
 **/
public class AfterSaleAgentStatSort implements Comparator<AfterSaleAgentStatDto> {

    private AfterSaleAgentSortType afterSaleAgentSortType;

    public AfterSaleAgentStatSort(AfterSaleAgentSortType afterSaleAgentSortType) {
        this.afterSaleAgentSortType = afterSaleAgentSortType;
    }

    @Override
    public int compare(AfterSaleAgentStatDto o1, AfterSaleAgentStatDto o2) {
        if (AfterSaleAgentSortType.SHARE == afterSaleAgentSortType) {
            return o2.getShareCount().compareTo(o1.getShareCount());
        }
        if (AfterSaleAgentSortType.UNIQUE_VISITOR == afterSaleAgentSortType) {
            return o2.getUniqueVisitorCount().compareTo(o1.getUniqueVisitorCount());
        }
        if (AfterSaleAgentSortType.PAGE_VIEW == afterSaleAgentSortType) {
            return o2.getPageViewCount().compareTo(o1.getPageViewCount());
        }
        if (AfterSaleAgentSortType.PACKAGE_CARD_COUNT == afterSaleAgentSortType) {
            return o2.getPackageCardCount().compareTo(o1.getPackageCardCount());
        }
        return 0;
    }
}
