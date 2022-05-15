package com.tuanche.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuanche.directselling.dto.AfterSaleKeepOnRecordBrandCarSeriesDto;

import java.util.Date;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/10/12 16:01
 **/
public class AfterSaleKeepOnRecordBrandCarSeriesVo extends AfterSaleKeepOnRecordBrandCarSeriesDto {
    /**
     * 匹配时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date matchingTime;
}
