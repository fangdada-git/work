package com.tuanche.web.api.aftersale.v2;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.directselling.api.AfterSaleAgentService;
import com.tuanche.directselling.api.AfterSaleStatService;
import com.tuanche.directselling.model.AfterSaleAgent;
import com.tuanche.directselling.utils.AfterSaleConstants;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.web.api.BaseController;
import com.tuanche.web.util.EmojiFilter;
import com.tuanche.web.vo.AfterSaleAgentRegister;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;


@RequestMapping("/api/v2/aftersale")
@RestController
public class AfterSaleAgent2Controller extends BaseController {

    @Reference
    AfterSaleAgentService afterSaleAgentService;
    @Reference
    AfterSaleStatService afterSaleStatService;

    /**
     * 代理人注册
     *
     * @param agentRegister
     * @return
     */
    @PostMapping("/register")
    public TcResponse activityInfoForApi(@RequestBody @Valid AfterSaleAgentRegister agentRegister, BindingResult bindingResult) {
        long st = System.currentTimeMillis();
        if (bindingResult.hasErrors()) {
            return response(StatusCodeEnum.PARAM_IS_INVALID, bindingResult.getAllErrors().get(0).getDefaultMessage(), st);
        }
        //销售
        if(AfterSaleConstants.AgentType.TYPE_0.getCode().equals(agentRegister.getType())){
            AfterSaleAgent afterSaleAgent1 = afterSaleAgentService.getAfterSaleAgent(agentRegister.getActivityId(), agentRegister.getAgentWxUnionId());
            if (afterSaleAgent1 != null) {
                return response(StatusCodeEnum.UNION_ID_EXIST, StatusCodeEnum.UNION_ID_EXIST.getText(), st);
            }
        //团车电销
        }else if(AfterSaleConstants.AgentType.TYPE_1.getCode().equals(agentRegister.getType())){
            Integer zero = 0;
            //团车电销活动ID为0
            agentRegister.setActivityId(zero);
            AfterSaleAgent afterSaleAgent1 = afterSaleAgentService.getAfterSaleAgent(zero, agentRegister.getAgentWxUnionId());
            if (afterSaleAgent1 != null) {
                return response(StatusCodeEnum.UNION_ID_EXIST, StatusCodeEnum.UNION_ID_EXIST.getText(), st);
            }
        }

        AfterSaleAgent afterSaleAgent = new AfterSaleAgent();
        BeanUtils.copyProperties(agentRegister, afterSaleAgent);
        afterSaleAgent.setCreateDt(new Date());
        afterSaleAgent.setNickName(agentRegister.getNickName());
        afterSaleAgentService.insertSelective(afterSaleAgent);
        return success(0, st);
    }

}
