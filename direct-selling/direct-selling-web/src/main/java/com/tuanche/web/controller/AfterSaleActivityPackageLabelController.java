package com.tuanche.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.exception.RedisException;
import com.tuanche.arch.web.model.TcResponse;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.directselling.api.*;
import com.tuanche.directselling.dto.AfterSaleActivityPackagePrimaryLabelDto;
import com.tuanche.directselling.model.AfterSaleActivityPackageLabel;
import com.tuanche.directselling.model.AfterSaleActivityPackagePrimaryLabel;
import com.tuanche.directselling.model.AfterSaleActivityPackageSecondaryLabel;
import com.tuanche.directselling.model.AfterSaleOrderRecord;
import com.tuanche.directselling.utils.AfterSaleConstants;
import com.tuanche.directselling.utils.ApiBaseCacheKeys;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.inner.sso.core.conf.Conf;
import com.tuanche.inner.sso.core.user.XxlUser;
import com.tuanche.web.api.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 给测试用来测试售后特卖奖励的controller
 *
 * @param
 * @author HuangHao
 * @CreatTime 2021-08-02 16:40
 * @return
 */
@Controller
@RequestMapping("/afterSale/packageLabel")
public class AfterSaleActivityPackageLabelController extends BaseController {

    @Reference
    AfterSaleActivityPackagePrimaryLabelService afterSaleActivityPackagePrimaryLabelService;
    @Reference
    AfterSaleActivityPackageLabelService afterSaleActivityPackageLabelService;

    /**
     * 跳转到套餐设置标签页面
     * @author HuangHao
     * @CreatTime 2021-11-25 15:34
     * @param modelMap
     * @param activityId
     * @return java.lang.String
     */
    @RequestMapping("/package/config")
    public String packageIndex(ModelMap modelMap, Integer packageId,Integer activityId) {
        modelMap.addAttribute("packageId", packageId);
        modelMap.addAttribute("activityId", activityId);
        return "after_sale/label/package_label";
    }

    /**
     * 根据活动ID获取标签列表
     * @author HuangHao
     * @CreatTime 2021-11-24 11:25
     * @param
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivityPackagePrimaryLabel>
     */
    @RequestMapping("/package/getPackageLabels")
    @ResponseBody
    public List<AfterSaleActivityPackageLabel> getPackageLabels(Integer packageId){
        return afterSaleActivityPackageLabelService.getPackageLabels(packageId);
    }
    /**
     * 保存套餐标签
     * @author HuangHao
     * @CreatTime 2021-11-25 15:34
     * @param request
     * @param packageLabels
     * @return com.tuanche.arch.web.model.TcResponse
     */
    @RequestMapping("/package/savePackageLabel/{packageId}")
    @ResponseBody
    public TcResponse savePackageLabel(HttpServletRequest request, @PathVariable("packageId")Integer packageId, @RequestBody List<AfterSaleActivityPackageLabel> packageLabels) {
        XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
        String name = xxlUser.getEmpName();
        List<Integer> ids = afterSaleActivityPackageLabelService.getLabelIds(packageId);
        if(!CollectionUtils.isEmpty(packageLabels)){
            int size=packageLabels.size()*2;
            List<AfterSaleActivityPackageLabel> addList = new ArrayList<>(size);
            List<AfterSaleActivityPackageLabel> updateList = new ArrayList<>(size);
            for (AfterSaleActivityPackageLabel label : packageLabels) {
                if(label.getId()==null || label.getId() < 1){
                    label.setCreateName(name);
                    addList.add(label);
                }else{
                    label.setUpdateName(name);
                    updateList.add(label);
                    if(ids!=null && ids.size()>0){
                        ids.remove(label.getId());
                    }
                }
            }
            if(addList.size()>0){
                afterSaleActivityPackageLabelService.batchInsert(addList);
            }
            if(updateList.size()>0){
                afterSaleActivityPackageLabelService.batchUpdate(updateList);
            }
        }
        if(ids.size()>0){
            afterSaleActivityPackageLabelService.delByIds(ids);
        }
        return new TcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText());
    }
    /**
     * 标签配置页面
     * @author HuangHao
     * @CreatTime 2021-11-25 15:35
     * @param modelMap
     * @param activityId
     * @return java.lang.String
     */
    @RequestMapping("/config/index")
    public String index(ModelMap modelMap, Integer activityId) {
        modelMap.addAttribute("activityId", activityId);
        return "after_sale/label/index";
    }

    /**
     * 根据活动ID获取标签列表
     * @author HuangHao
     * @CreatTime 2021-11-24 11:25
     * @param
     * @return java.util.List<com.tuanche.directselling.model.AfterSaleActivityPackagePrimaryLabel>
     */
    @RequestMapping("/config/getLabel")
    @ResponseBody
    public List<AfterSaleActivityPackagePrimaryLabel> getLabelsByActivityId(Integer activityId){
        return afterSaleActivityPackagePrimaryLabelService.getLabelsByActivityId(activityId);
    }


    /**
     * 保存标签
     * @author HuangHao
     * @CreatTime 2021-11-24 11:00
     * @param packageLabels
     * @return com.tuanche.arch.web.model.TcResponse
     */
    @RequestMapping("/config/saveLabel")
    @ResponseBody
    public TcResponse saveLabel(HttpServletRequest request,@RequestBody List<AfterSaleActivityPackagePrimaryLabelDto> packageLabels){
        XxlUser xxlUser = (XxlUser) request.getAttribute(Conf.SSO_USER);
        if(packageLabels.size()>0 && xxlUser != null){
            String name = xxlUser.getEmpName();
            for (AfterSaleActivityPackagePrimaryLabelDto label : packageLabels) {
                if(label.getId()!=null && label.getId()>0){
                    label.setUpdateName(name);
                }else{
                    label.setCreateName(name);
                }
                if(label.getSecondaryLabels() != null){
                    List<AfterSaleActivityPackageSecondaryLabel> secondaryLabels = label.getSecondaryLabels();

                    for (AfterSaleActivityPackageSecondaryLabel secondaryLabel : secondaryLabels) {
                        if(secondaryLabel.getId()!=null && secondaryLabel.getId()>0){
                            secondaryLabel.setUpdateName(name);
                        }else{
                            secondaryLabel.setCreateName(name);
                        }
                    }
                }
            }
        }
        return afterSaleActivityPackagePrimaryLabelService.save(packageLabels);
    }

}
