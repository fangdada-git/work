package com.tuanche.web.api.fission;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.manubasecenter.api.UserBaseService;
import com.tuanche.manubasecenter.constant.ManuBaseConstants;
import com.tuanche.manubasecenter.dto.TcResponse;
import com.tuanche.manubasecenter.dto.TcResponseCode;
import com.tuanche.manubasecenter.model.CsUser;
import com.tuanche.manubasecenter.util.ManeBaseConsoleConstants;
import com.tuanche.web.service.FissionLoginServiceImpl;
import com.tuanche.web.util.DirectCommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @program: manu_service_api
 * @description: 裂变-经销商活动主管相关操作
 * @author: fxq
 * @create: 2020-09-23 11:53
 **/
@Controller
@RequestMapping("/api/fission/manu/leader")
public class CsUserLeaderController {

    @Reference
    private UserBaseService userBaseService;
    @Autowired
    private FissionLoginServiceImpl fissionLoginServiceImpl;

    /**
     * @description: 经销商管理员登陆 (废弃)
     * @param:
     * @return:
     * @author: fxq
     * @date: 2020/9/23 13:52
     */
    @RequestMapping("/userLogin")
    @ResponseBody
    public TcResponse userLogin (HttpServletRequest request, String ulogin, String upwd) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "CsUserLeaderController", "userLogin",  "经销商管理员登陆 start " +st, ulogin+"-"+upwd);
        int code = TcResponseCode.OK.getIndex();
        String msg = "登陆成功";
        Integer userId = 0;
        try {
            if (StringUtils.isEmpty(ulogin) || StringUtils.isEmpty(upwd)) {
                return DirectCommonUtil.setTcResponse(TcResponseCode.PARAM_INVALID.getIndex(), TcResponseCode.PARAM_INVALID.getName(), st, 0);
            }
            CsUser user = new CsUser();
            user.setUlogin(ulogin.trim());
            user.setUlevel(ManuBaseConstants.MANUFACTURER_DEALER_PC);
            List<CsUser> userList = userBaseService.getCsUserList(user);
            if (CollectionUtils.isEmpty(userList)) {
                user.setUlogin(null);
                user.setEmail(ulogin.trim());
                userList = userBaseService.getCsUserList(user);
            }
            if (CollectionUtils.isEmpty(userList)) {
                return DirectCommonUtil.setTcResponse(TcResponseCode.PARAM_INVALID.getIndex(), "无此账号", st, userId);
            } else if (userList.size()==1) {
                if (StringUtils.isEmpty(user.getUphone())) {
                    return DirectCommonUtil.setTcResponse(TcResponseCode.RESPONSE.getIndex(), "该帐号未绑定手机，为了您的帐号安全请前往电脑端绑定手机号码", st, userId);
                } else {
                    //检查是否是初始密码
                    if (checkPassword(user.getUpwd())) {
                        return DirectCommonUtil.setTcResponse(TcResponseCode.RESPONSE.getIndex(), "您的密码仍是初始密码，安全级别较低，请前往电脑端修改", st, userId);
                    } else {
                        userId = userList.get(0).getId();
//                        fissionLoginServiceImpl.addLoginUserToken(user);
                    }
                }
            } else {
                return DirectCommonUtil.setTcResponse(TcResponseCode.PARAM_INVALID.getIndex(), "账号异常", st, userId);
            }
        }catch (Exception e) {
            return DirectCommonUtil.addErrorLog("CsUserController", "经销商管理员登陆 error", e, st,ulogin+ "-" +upwd);
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "CsUserLeaderController", "userLogin",  "经销商管理员登陆 end " +st, userId+"");
        return DirectCommonUtil.setTcResponse(code, msg, st, userId);
    }

    private boolean checkPassword(String password) {
        String initPassword = userBaseService.getInitPwd();// apiUserService.getDealerInitPassword();
        if (initPassword != null && initPassword.equals(password)) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * @description: 添加销售
     * @param:
     * @return:
     * @author: fxq
     * @date: 2020/9/23 16:00
     */
    @RequestMapping("addSale")
    @ResponseBody
    public TcResponse addSale (HttpServletRequest request,String userName, String userPhone) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "CsUserLeaderController", "addSale",  "添加销售 start " +st, userName+"-"+userPhone);
        int code = TcResponseCode.OK.getIndex();
        String msg = "销售添加成功";
        try {
            if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(userPhone)) {
                return DirectCommonUtil.setTcResponse(TcResponseCode.PARAM_INVALID.getIndex(), TcResponseCode.PARAM_INVALID.getName(), st, 0);
            }
            if (!ManeBaseConsoleConstants.isMobile(userPhone.trim())) {
                return DirectCommonUtil.setTcResponseAndLog(TcResponseCode.PARAM_INVALID.getIndex(), "手机号不合法", st,
                        "CsUserLeaderController", "addSale", userName+"-"+userPhone);
            }
            CsUser csUser = fissionLoginServiceImpl.getLoginUser(request);
            
            CsUser user = new CsUser();
            user.setUphone(userPhone.trim());
            user.setUlevelList(Arrays.asList(new Integer[]{
                    ManuBaseConstants.MANUFACTURER_USER_TYPE5, 
                    ManuBaseConstants.MANUFACTURER_USER_TYPE4, 
                    ManuBaseConstants.MANUFACTURER_USER_TYPE3,
                    ManuBaseConstants.MANUFACTURER_USER_TYPE2,
                    ManuBaseConstants.MANUFACTURER_USER_TYPE1,
                    ManuBaseConstants.MANUFACTURER_USER_TYPE0}));
//            int num = userBaseService.insertCheck(user);
            List<CsUser> csUserList = userBaseService.getCsUserList(user);
            if (!CollectionUtils.isEmpty(csUserList)) {
                if (csUserList.size()>1 || !csUserList.get(0).getUlevel().equals(ManuBaseConstants.MANUFACTURER_USER_TYPE5)) {
                    return DirectCommonUtil.setTcResponseAndLog(TcResponseCode.RESPONSE.getIndex(), "该销售已存在", st,
                            "CsUserLeaderController", "addSale", userName+"-"+userPhone);
                } else {
                    if (!csUserList.get(0).getDealerId().equals(csUser.getDealerId())) {
                        return DirectCommonUtil.setTcResponseAndLog(TcResponseCode.RESPONSE.getIndex(), "同一人不能任职于两家经销商", st,
                                "CsUserLeaderController", "addSale", userName+"-"+userPhone);
                    }
                }
            }
            Date date = new Date();
            user.setUlevel(ManuBaseConstants.MANUFACTURER_USER_TYPE4);
            user.setDealerId(csUser.getDealerId());
            user.setAddUser(csUser.getId());
            user.setCityId(csUser.getCityId());
            user.setAddTime(date);
            user.setUpdateUser(csUser.getId());
            user.setUpdateTime(date);
            user.setAddUser(csUser.getId());
            user.setUname(userName.trim());
            user.setStat(ManuBaseConstants.CSUSER_STAT_USABLE);
            int n = userBaseService.insert(user);
            if (n==0) {
                return DirectCommonUtil.setTcResponseAndLog(TcResponseCode.RESPONSE.getIndex(), "销售添加失败", st,
                        "CsUserLeaderController", "addSale", userName+"-"+userPhone);
            }
        }catch (Exception e) {
            return DirectCommonUtil.addErrorLog("CsUserController", "添加销售 error", e, st,userName + "-" +userPhone);
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "CsUserLeaderController", "addSale",  "添加销售 end " +st, msg);
        return DirectCommonUtil.setTcResponse(code, msg, st, 0);
    }

    /**
     * @description: 修改经销商员工
     * @param:
     * @return:
     * @author: fxq
     * @date: 2020/9/23 16:00
     */
    @RequestMapping("updateSale")
    @ResponseBody
    public TcResponse updateSale (HttpServletRequest request,Integer id, String userName, String userPhone) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "CsUserLeaderController", "updateSale",  "修改经销商员工 start " +st, id+"-"+userName+"-"+userPhone);
        int code = TcResponseCode.OK.getIndex();
        String msg = "销售修改成功";
        try {
            if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(userPhone) || id==null) {
                return DirectCommonUtil.setTcResponse(TcResponseCode.PARAM_INVALID.getIndex(), TcResponseCode.PARAM_INVALID.getName(), st, 0);
            }
            if (!ManeBaseConsoleConstants.isMobile(userPhone.trim())) {
                return DirectCommonUtil.setTcResponseAndLog(TcResponseCode.PARAM_INVALID.getIndex(), "手机号不合法", st,
                        "CsUserLeaderController", "updateSale", id+"-"+userName+"-"+userPhone);
            }
            CsUser csUser = fissionLoginServiceImpl.getLoginUser(request);
            CsUser user = new CsUser();
            user.setUphone(userPhone.trim());
            Date date = new Date();
            user.setId(id);
            user.setUpdateUser(csUser.getId());
            user.setUpdateTime(date);
            user.setUname(userName.trim());
            int n = userBaseService.update(user);
            if (n==0) {
                return DirectCommonUtil.setTcResponseAndLog(TcResponseCode.RESPONSE.getIndex(), "销售修改失败", st,
                        "CsUserLeaderController", "updateSale", id+"-"+userName+"-"+userPhone);
            }
        }catch (Exception e) {
            return DirectCommonUtil.addErrorLog("CsUserController", "修改经销商员工 error", e, st,userName + "-" +userPhone);
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "CsUserLeaderController", "updateSale",  "修改经销商员工 end " +st, msg);
        return DirectCommonUtil.setTcResponse(code, msg, st, 0);
    }

    /**
     * @description: 经经销商管理员获取经销商员工名单
     * @param:
     * @return:
     * @author: fxq
     * @date: 2020/9/23 16:18
     */
    @RequestMapping("getUserList")
    @ResponseBody
    public TcResponse getUserList (HttpServletRequest request) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "CsUserLeaderController", "getUserList",  "经经销商管理员获取经销商员工名单 start " +st, "");
        int code = TcResponseCode.OK.getIndex();
        String msg = TcResponseCode.OK.getName();
        Object result = 0;
        try {
            CsUser csUser = fissionLoginServiceImpl.getLoginUser(request);
            CsUser user = new CsUser();
            user.setDealerId(csUser.getDealerId());//经销商ID
            user.setUlevels(ManuBaseConstants.MANUFACTURER_USER_TYPE4.toString() + "," + ManuBaseConstants.MANUFACTURER_USER_TYPE3.toString());
            List<CsUser> res = userBaseService.getCsUserList(user);
            if (CollectionUtils.isEmpty(res)) {
                return DirectCommonUtil.setTcResponseAndLog(TcResponseCode.RESPONSE.getIndex(), "经销商管理员获取经销商员工名单错误", st,
                        "CsUserLeaderController", "getUserList", csUser.getDealerId());
            } else {
                result = res;
            }
        }catch (Exception e) {
            return DirectCommonUtil.addErrorLog("CsUserController", "经销商管理员获取经销商员工名单 error", e, st,"");
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "CsUserLeaderController", "getUserList",  "经经销商管理员获取经销商员工名单 end " +st, "");
        return DirectCommonUtil.setTcResponse(code, msg, st, result);
    }

    /**
     * @description: 查询B端用户
     * @param:
     * @return:
     * @author: fxq
     * @date: 2020/9/24 18:37
     */
    @RequestMapping("getUserById")
    @ResponseBody
    public TcResponse getUserById (Integer userId) {
        long st = System.currentTimeMillis();
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "CsUserLeaderController", "getUserById",  "查询B端用户 start " +st, userId+"");
        int code = TcResponseCode.OK.getIndex();
        String msg = TcResponseCode.OK.getName();
        Object result = 0;
        try {
            if (userId==null || !(userId>0)) {
                return DirectCommonUtil.setTcResponse(TcResponseCode.PARAM_INVALID.getIndex(), TcResponseCode.PARAM_INVALID.getName(), st, userId);
            }
            CsUser user = userBaseService.getCsUserById(userId);
            if (user==null || user.getId()==null) {
                return DirectCommonUtil.setTcResponseAndLog(TcResponseCode.RESPONSE.getIndex(), TcResponseCode.RESPONSE.getName(), st,
                        "CsUserLeaderController", "getUserById", userId);
            }
            result = user;
        }catch (Exception e) {
            return DirectCommonUtil.addErrorLog("CsUserController", "查询B端用户 error", e, st,"");
        }
        StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "CsUserLeaderController", "getUserById",  "查询B端用户 end " +st, JSON.toJSONString(result));
        return DirectCommonUtil.setTcResponse(code, msg, st, result);
    }


}
