package com.tuanche.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.directselling.api.AfterSaleAgentService;
import com.tuanche.directselling.model.AfterSaleAgent;
import com.tuanche.directselling.utils.BeanCopyUtil;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.web.vo.AfterSaleAgentEditVo;
import com.tuanche.web.vo.AfterSaleAgentVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/afterSale/agent")
public class AfterSaleAgentManagerController extends BaseController {

    @Reference
    private AfterSaleAgentService afterSaleAgentService;
    @Value("${direct.selling.domain}")
    private String domain;
    @RequestMapping("/toList")
    public String toFissionGoodsList(ModelMap modelMap, Integer dealerId,
                                     Integer activityId,  Integer type) {
        modelMap.addAttribute("type", type);
        modelMap.addAttribute("dealer", dealerId);
        modelMap.addAttribute("activityId", activityId);
        modelMap.addAttribute("domain", domain);
        return "after_sale/agent/list";
    }

    @GetMapping("/list")
    @ResponseBody
    public PageResult list(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("type") Integer type,
                           @RequestParam("activityId") Integer activityId,
                           @RequestParam(value = "nameOrPhone", required = false) String nameOrPhone) {
        PageResult pageResult = afterSaleAgentService.getAgentList(page, limit, activityId, type, nameOrPhone);
        pageResult.setCode("0");
        if (pageResult.getData() != null) {
            pageResult.setData(BeanCopyUtil.copyListProperties(pageResult.getData(), AfterSaleAgentVo::new));
        }
        return pageResult;
    }

    @GetMapping("/toEdit")
    public String edit(ModelMap modelMap, @RequestParam("id") Integer id) {
        AfterSaleAgent afterSaleAgent = afterSaleAgentService.selectByPrimaryKey(id);
        modelMap.addAttribute("agent", afterSaleAgent);
        return "after_sale/agent/edit";
    }

    @PostMapping("/edit")
    @ResponseBody
    public ResultDto edit(@RequestBody @Valid AfterSaleAgentEditVo afterSaleAgentEditVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return dynamicResult(StatusCodeEnum.PARAM_IS_INVALID.getCode(), bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        AfterSaleAgent afterSaleAgent = new AfterSaleAgent();
        afterSaleAgent.setId(afterSaleAgentEditVo.getId());
        afterSaleAgent.setName(afterSaleAgentEditVo.getName());
        afterSaleAgent.setPhone(afterSaleAgentEditVo.getPhone());
        afterSaleAgent.setPosition(afterSaleAgentEditVo.getPosition());
        afterSaleAgentService.updateByPrimaryKeySelective(afterSaleAgent);
        return success();
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResultDto delete(@RequestBody AfterSaleAgentEditVo afterSaleAgentEditVo) {
        AfterSaleAgent afterSaleAgent = new AfterSaleAgent();
        afterSaleAgent.setId(afterSaleAgentEditVo.getId());
        afterSaleAgent.setDeleteFlag(true);
        afterSaleAgent.setUpdateDt(new Date());
        afterSaleAgentService.updateByPrimaryKeySelective(afterSaleAgent);
        return success();
    }

    @PostMapping("/upRole")
    @ResponseBody
    public ResultDto upRole(@RequestBody AfterSaleAgentEditVo afterSaleAgentEditVo) {
        AfterSaleAgent afterSaleAgent = new AfterSaleAgent();
        afterSaleAgent.setId(afterSaleAgentEditVo.getId());
        afterSaleAgent.setIsAdmin(true);
        afterSaleAgent.setUpdateDt(new Date());
        afterSaleAgentService.updateByPrimaryKeySelective(afterSaleAgent);
        return success();
    }

    @PostMapping("/downRole")
    @ResponseBody
    public ResultDto downRole(@RequestBody AfterSaleAgentEditVo afterSaleAgentEditVo) {
        AfterSaleAgent afterSaleAgent = new AfterSaleAgent();
        afterSaleAgent.setId(afterSaleAgentEditVo.getId());
        afterSaleAgent.setIsAdmin(false);
        afterSaleAgent.setUpdateDt(new Date());
        afterSaleAgentService.updateByPrimaryKeySelective(afterSaleAgent);
        return success();
    }
    /**
     * 获取代理人列表
     * @author HuangHao
     * @CreatTime 2021-10-18 14:05
     * @param request
     * @param activityId
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleAgent>
     */
    @RequestMapping("/getAgentList")
    @ResponseBody
    public List<AfterSaleAgent> getAfterSaleAgentList(HttpServletRequest request, Integer activityId) {
        if(activityId == null || activityId < 1){
            return null;
        }
        AfterSaleAgent agent = new AfterSaleAgent();
        agent.setActivityId(activityId);
        return afterSaleAgentService.getAfterSaleAgentList(agent);
    }
}
