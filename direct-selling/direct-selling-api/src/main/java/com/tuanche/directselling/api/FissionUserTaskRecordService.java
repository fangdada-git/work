package com.tuanche.directselling.api;

import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.directselling.dto.FissionSubscribeOrLiveCountDto;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.vo.FissionUserTaskRecordVo;

import java.util.List;

/**
 * @author：HuangHao
 * @CreatTime 2020-09-23 15:37
 */
public interface FissionUserTaskRecordService {

    /**
     * 用户完成任务方法
     *
     * @param task
     * @return com.tuanche.arch.web.model.TcResponse
     * @author HuangHao
     * @CreatTime 2020-09-23 17:23
     */
    TcResponse completeTask(FissionUserTaskRecordVo task);


    /**
     * 分享结果的数据明细 for 前端
     *
     * @param fissionId
     * @param taskId
     * @return
     */
    PageResult selectFissionUserTaskRecordDetail(int page, int limit, Integer fissionId, Integer taskId, Integer saleId);


    /**
     * 分享结果的数据明细 for 管理后台
     *
     * @param fissionId
     * @param taskId
     * @return
     */
    PageResult selectFissionUserTaskRecordDetailVoWithSaleName(int page, int limit, Integer fissionId, Integer taskId, String saleName, String dealerName, Integer isEffective);


    /**
     * 预约直播人数\预约直播并观看直播人数\观看直播人数
     *
     * @param fissionId
     * @return
     */
    FissionSubscribeOrLiveCountDto selectFissionSubscribeOrLiveCount(Integer fissionId);
}
