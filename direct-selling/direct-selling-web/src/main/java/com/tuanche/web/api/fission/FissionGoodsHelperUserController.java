package com.tuanche.web.api.fission;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.api.FissionGoodsHelperUserService;
import com.tuanche.directselling.api.FissionUserService;
import com.tuanche.directselling.model.FissionGoodsHelperUser;
import com.tuanche.directselling.model.FissionUser;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.manubasecenter.dto.TcResponse;
import com.tuanche.web.util.DirectCommonUtil;
import com.tuanche.web.util.Globals;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/api/fission/user/goodsHelper")
public class FissionGoodsHelperUserController {
    
    @Reference
    private FissionGoodsHelperUserService fissionGoodsHelperUserService;
    @Reference
    private FissionUserService fissionUserService;
    
    /**
     * @description : 获取助力人列表
     * @param : 
     * @return : 
     * @author : fxq
     * @date : 2020/11/18 14:52
     */
    @RequestMapping("/getFissionGoodsHelperUserList")
    @ResponseBody
    public TcResponse getFissionGoodsHelperUserList (HttpServletRequest request, FissionGoodsHelperUser helperUser) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionGoodsHelperUserController","getFissionGoodsHelperUserList",  "获取助力人列表 start " +st, JSON.toJSONString(helperUser));
        List<FissionGoodsHelperUser> list = null;
        if (helperUser==null || helperUser.getFissionId()==null || helperUser.getGoodsId()==null || helperUser.getUserWxUnionId()==null) {
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "参数错误", st, 0);
        }
        try {
            helperUser.setBuyGoods(Globals.FISSION_GOODS.have_not_purchased);
            list = fissionGoodsHelperUserService.getFissionGoodsHelperUserList(helperUser);
            if (CollectionUtils.isEmpty(list)) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "获取助力人列表 空", st,
                        "FissionGoodsHelperUserController","getFissionGoodsHelperUserList", helperUser);
            }
        } catch (Exception e) {
            return DirectCommonUtil.addErrorLog("FissionGoodsHelperUserController", "获取助力人列表 error", e, st, "");
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionGoodsHelperUserController","getFissionGoodsHelperUserList",  "获取助力人列表 end " +st, JSON.toJSONString(list));
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, list);
    }
    /**
     * @description : 获取助力人数
     * @param : 
     * @return : 
     * @author : fxq
     * @date : 2020/11/18 14:52
     */
    @RequestMapping("/getFissionGoodsHelperNum")
    @ResponseBody
    public TcResponse getFissionGoodsHelperNum (HttpServletRequest request, FissionGoodsHelperUser helperUser) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionGoodsHelperUserController","getFissionGoodsHelperNum",  "获取助力人数 start " +st, JSON.toJSONString(helperUser));
        int num = 0;
        if (helperUser==null || helperUser.getFissionId()==null || helperUser.getGoodsId()==null || helperUser.getUserWxUnionId()==null) {
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "参数错误", st, 0);
        }
        try {
            helperUser.setBuyGoods(Globals.FISSION_GOODS.have_not_purchased);
            num = fissionGoodsHelperUserService.getFissionGoodsHelperUserCount(helperUser);
        } catch (Exception e) {
            return DirectCommonUtil.addErrorLog("FissionGoodsHelperUserController", "获取助力人数 error", e, st, "");
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionGoodsHelperUserController","getFissionGoodsHelperNum",  "获取助力人数 end " +st, num);
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, num);
    }
    
    /**
     * @description : 获取用户是否助力过该商品（活动维度）
     * @param : 
     * @return : 0未主力
     * @return : 1已助力此商品
     * @return : 2已助力其他商品
     * @author : fxq
     * @date : 2020/11/18 14:52
     */
    @RequestMapping("/getHelperGoodsId")
    @ResponseBody
    public TcResponse getHelperGoodsId (HttpServletRequest request, FissionGoodsHelperUser helperUser) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionGoodsHelperUserController","getHelperGoodsId",  "获取用户是否助力过该商品 start " +st, JSON.toJSONString(helperUser));
        Integer falg = 0;
        if (helperUser==null || helperUser.getFissionId()==null || helperUser.getHelperWxUnionId()==null || StringUtil.isEmpty(helperUser.getUserWxUnionId())) {
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "参数错误", st, 0);
        }
        try {
            FissionGoodsHelperUser goodsHelperUser = new FissionGoodsHelperUser();
            goodsHelperUser.setFissionId(helperUser.getFissionId());
            goodsHelperUser.setHelperWxUnionId(helperUser.getHelperWxUnionId());
            List<FissionGoodsHelperUser> fissionGoodsHelperUserList = fissionGoodsHelperUserService.getFissionGoodsHelperUserList(goodsHelperUser);
            if (CollectionUtils.isNotEmpty(fissionGoodsHelperUserList)) {
                if (fissionGoodsHelperUserList.size()>1) {
                    return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "获取助力信息异常", st,
                            "FissionGoodsHelperUserController","getFissionGoodsHelperUserList", helperUser);
                }
                if (helperUser.getUserWxUnionId().equals(fissionGoodsHelperUserList.get(0).getUserWxUnionId())) {
                    falg = 1;
                } else {
                    falg = 2;
                }
            }
        } catch (Exception e) {
            return DirectCommonUtil.addErrorLog("FissionGoodsHelperUserController", "获取用户是否助力过该商品（活动维度） error", e, st, "");
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionGoodsHelperUserController","getHelperGoodsId",  "获取用户是否助力过该商品 end " +st, falg+"");
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, falg);
    }

    /**
     * @description : 添加助力人
     * @param : 
     * @return : 
     * @author : fxq
     * @date : 2020/11/18 14:52
     */
    @RequestMapping("/addFissionGoodsHelperUser")
    @ResponseBody
    public TcResponse addFissionGoodsHelperUser (HttpServletRequest request, FissionGoodsHelperUser helperUser) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionGoodsHelperUserController","addFissionGoodsHelperUser",  "添加助力人 start " +st, JSON.toJSONString(helperUser));
        int num = 0;
        if (helperUser==null || helperUser.getFissionId()==null || helperUser.getGoodsId()==null 
                || helperUser.getUserWxUnionId()==null || helperUser.getHelperWxUnionId()==null
                || helperUser.getUserWxUnionId().equals(helperUser.getHelperWxUnionId())
                || helperUser.getUserWxUnionId().length()!=28 || helperUser.getHelperWxUnionId().length()!=28) {
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "参数错误", st, helperUser==null ? "":JSON.toJSONString(helperUser));
        }
        try {
            FissionGoodsHelperUser user = new FissionGoodsHelperUser();
            user.setFissionId(helperUser.getFissionId());
            user.setHelperWxUnionId(helperUser.getHelperWxUnionId());
            int count = fissionGoodsHelperUserService.getFissionGoodsHelperUserCount(user);
            if (count >0) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "该场活动您已助力", st,
                        "FissionGoodsHelperUserController","addFissionGoodsHelperUserList", helperUser);
            }
            FissionGoodsHelperUser helper = new FissionGoodsHelperUser();
            helper.setFissionId(helperUser.getFissionId());
            helper.setUserWxUnionId(helperUser.getUserWxUnionId());
            num = fissionGoodsHelperUserService.getFissionGoodsHelperUserCount(helper);
            helperUser.setBuyGoods(Globals.FISSION_GOODS.have_not_purchased);
            int id = fissionGoodsHelperUserService.insertSelective(helperUser);
            if (id==0) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "助力失败", st,
                        "FissionGoodsHelperUserController","addFissionGoodsHelperUserList", helperUser);
            }
        } catch (Exception e) {
            return DirectCommonUtil.addErrorLog("FissionGoodsHelperUserController", "获取助力人列表 error", e, st, "");
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionGoodsHelperUserController","addFissionGoodsHelperUser",  "添加助力人 end " +st, num+1+"");
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, num+1);
    }

    /**
     * @description : 获取助力人信息
     * @param : 
     * @return : 
     * @author : fxq
     * @date : 2020/11/18 14:52
     */
    @RequestMapping("/getUserInfoByUnionid")
    @ResponseBody
    public TcResponse getUserInfoByUnionid (HttpServletRequest request, FissionGoodsHelperUser helperUser) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionGoodsHelperUserController","getUserInfoByUnionid",  "获取助力人信息 start " +st, JSON.toJSONString(helperUser));
        FissionUser user = null;
        if (helperUser==null || helperUser.getUserWxUnionId()==null || helperUser.getFissionId()==null) {
            return DirectCommonUtil.setTcResponse(StatusCodeEnum.PARAM_NOT_COMPLETE.getCode(), "参数错误", st, 0);
        }
        try {
            FissionUser fissionUser = new FissionUser();
            fissionUser.setFissionId(helperUser.getFissionId());
            fissionUser.setUserWxUnionId(helperUser.getUserWxUnionId());
            List<FissionUser> userWxInfo = fissionUserService.getUserWxInfo(fissionUser);
            if (CollectionUtils.isEmpty(userWxInfo)) {
                return DirectCommonUtil.setTcResponseAndLog(StatusCodeEnum.DATA_IS_WRONG.getCode(), "获取助力人信息 空", st,
                        "FissionGoodsHelperUserController","getUserInfoByUnionid", helperUser);
            }
            user = userWxInfo.get(0);
        } catch (Exception e) {
            return DirectCommonUtil.addErrorLog("FissionGoodsHelperUserController", "获取助力人信息 error", e, st, "");
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionGoodsHelperUserController","getUserInfoByUnionid",  "获取助力人信息 end " +st, JSON.toJSONString(user));
        return DirectCommonUtil.setTcResponse(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getText(), st, user);
    }

}
