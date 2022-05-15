package com.tuanche.directselling.api;

import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2020-09-29 18:01
 */
public interface FissionSaleTaskStatisticsService {

    /**
     * 统计销售积分
     * @author HuangHao
     * @CreatTime 2020-09-29 17:54
     * @param
     * @return void
     */
    void salesTaskStatistics(List<Integer> fissionIds);
}
