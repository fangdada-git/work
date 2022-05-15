package com.tuanche.directselling.api;

import com.tuanche.directselling.dto.ResultMapDto;

import java.util.List;
import java.util.Map;

/**
 * @author：HuangHao
 * @CreatTime 2021-09-29 17:31
 */
public interface AfterSaleUserBrowseService {

    /**
     * 活动的浏览数量统计
     * @author HuangHao
     * @CreatTime 2021-09-29 17:24
     * @param activityIds
     * @return java.util.Map<java.lang.String,com.tuanche.directselling.dto.ResultMapDto>
     */
    Map<String, ResultMapDto> activityBrowseTotalMap(List<Integer> activityIds);
}
