package com.tuanche.web.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.directselling.api.FissionUserTaskRecordService;
import com.tuanche.directselling.vo.FissionUserTaskRecordVo;
import com.tuanche.framework.util.IPUtil;
import com.tuanche.web.util.CommonLogUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author：HuangHao
 * @CreatTime 2020-09-23 17:32
 */
@Controller
@RequestMapping("/api/fission/usertask")
public class FissionUserTaskController {

    @Reference
    FissionUserTaskRecordService fissionUserTaskRecordService;

    private static String KEW_WORD = "裂变活动用户任务";
    /**
     * 用户完成任务
     * @author HuangHao
     * @CreatTime 2020-09-23 17:23
     * @param task
     * @return com.tuanche.arch.web.model.TcResponse
     */
    //@AccessLimit(seconds = 30, maxCount = 40 )//30秒内超过40次请求则把该IP加入黑名单
    @RequestMapping("/completetask")
    @ResponseBody
    TcResponse completeTask(HttpServletRequest request,FissionUserTaskRecordVo task){
        String ip  = IPUtil.getRequestIp(request);
        task.setIp(ip);
        TcResponse response = fissionUserTaskRecordService.completeTask(task);
        CommonLogUtil.addInfo(null, KEW_WORD+"返回结果", response);
        return response;
    }
}
