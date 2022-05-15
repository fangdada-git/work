package com.tuanche.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuanche.directselling.dto.AfterSaleKeepOnRecordDto;

import java.util.Date;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/9/29 16:48
 **/
public class AfterSaleKeepOnRecordVo extends AfterSaleKeepOnRecordDto {

    /**
     * 线下活动结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date offlineConvertEndTime;

    /**
     * 购车时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date buyCarTime;

    /**
     * 最后进店时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date latestEnterTime;

    /**
     * 导入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDt;


    /**
     * 同步时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date syncTime;

}
