package com.tuanche.web.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.exception.RedisException;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.commons.util.StringUtil;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.eap.api.utils.ResultDto;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.api.UserBaseService;
import com.tuanche.manubasecenter.constant.ManuBaseConstants;
import com.tuanche.manubasecenter.model.CsUser;
import com.tuanche.web.util.DirectCommonUtil;
import com.tuanche.web.util.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Service
public class FissionLoginServiceImpl {

    @Autowired
    @Qualifier("ClusterRedisService")
    private RedisService redisService;
    //裂变店员登陆有效时间  秒
    @Value("${fission_sale_login_time}")
    private String fission_sale_login_time;
    @Reference
    private CompanyBaseService companyBaseService;
    @Reference
    private UserBaseService userBaseService;

    /**
     * @description : 经销商店员登陆
     * @param : 
     * @return : 
     * @author : fxq
     * @date : 2020/12/2 17:31
     */
    public ResultDto userLogin (String userPhone) {
        if (StringUtil.isEmpty(userPhone)) return DirectCommonUtil.addParamNull();
        ResultDto dto = new ResultDto();
        dto.setCode(StatusCodeEnum.SUCCESS.getCode());
        CsUser result = null;
        CsUser user = new CsUser();
        user.setUphone(userPhone);
        user.setStat(ManuBaseConstants.CSUSER_STAT_USABLE);
        List<CsUser> userList = null;
        try {
            userList = userBaseService.getCsUserList(user);
            if (CollectionUtils.isEmpty(userList) || userList.size()>2) {
                return DirectCommonUtil.addResultInfo(StatusCodeEnum.DATA_IS_WRONG.getCode(), "手机号异常");
            }
            result = userList.get(0);
            if (userList.size()==2) {
                if (userList.get(1).equals(ManuBaseConstants.MANUFACTURER_USER_TYPE5)) result = userList.get(1);
            }
            if (result.getUlevel().equals(ManuBaseConstants.MANUFACTURER_USER_TYPE5) && StringUtil.isEmpty(result.getUname())) result.setUname(result.getUphone());
        }catch (Exception e) {
            return DirectCommonUtil.addResultInfo(StatusCodeEnum.ERROR.getCode(), "经销商店员登陆error");
        }
        dto.setResult(result);
        return dto;
    }

    public String addLoginUserTokenToRedis (CsUser user) {
        if (user==null || user.getId()==null) return "";
        String uuid = UUID.randomUUID().toString().replace("-","");
        try {
            redisService.set(DirectCommonUtil.FISSION_SALE_LOGIN_TOKEN_REDIS+uuid, user.getId(), Long.valueOf(fission_sale_login_time));
        } catch (RedisException e) {
            StaticLogUtils.error(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "FissionLoginServiceImpl","addLoginUserToken", "店员登陆成功，写登陆token error", e);
        }
        return uuid;
    }
    public void addLoginUserToRedis (CsUser user) {
        if (user==null || user.getId()==null) return;
        try {
            CsUser csUser = new CsUser();
            csUser.setId(user.getId());
            csUser.setUname(user.getUname());
            csUser.setUphone(user.getUphone());
            csUser.setDealerId(user.getDealerId());
            csUser.setDealerName(companyBaseService.getCsCompanyName(user.getDealerId()));
            csUser.setCityId(user.getCityId());
            csUser.setUlevel(user.getUlevel());
            redisService.set(DirectCommonUtil.FISSION_SESSION_SALE_REDIS+user.getId(), csUser, 30*60*1000);
        } catch (RedisException e) {
            StaticLogUtils.error(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "FissionLoginServiceImpl","addLoginUserToRedis", "店员登陆成功，写登陆状态error", e);
        }
    }

    public CsUser getLoginUser (HttpServletRequest request) {
        CsUser user = null;
        try {
            String token = request.getHeader(DirectCommonUtil.FISSION_SALE_LOGIN_TOKEN);
            if (!StringUtils.isEmpty(token)) {
                Integer userId = redisService.get(DirectCommonUtil.FISSION_SALE_LOGIN_TOKEN_REDIS+token, Integer.class);
                if (userId!=null && userId>0) {
                    user = redisService.get(DirectCommonUtil.FISSION_SESSION_SALE_REDIS+userId, CsUser.class);
                }
            }
        } catch (RedisException e) {
            StaticLogUtils.error(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "FissionLoginServiceImpl","getSessionUser", "获取店员登陆状态error", e);
        }
        return user;
    }

    public void delLoginUser (HttpServletRequest request) {
        try {
            String token = request.getHeader(DirectCommonUtil.FISSION_SALE_LOGIN_TOKEN);
            if (!StringUtils.isEmpty(token)) {
                Integer userId = redisService.get(DirectCommonUtil.FISSION_SALE_LOGIN_TOKEN_REDIS+token, Integer.class);
                redisService.del(DirectCommonUtil.FISSION_SESSION_SALE_REDIS+userId);
                redisService.del(DirectCommonUtil.FISSION_SALE_LOGIN_TOKEN_REDIS+token);
            }
        }catch (Exception e) {
            StaticLogUtils.error(SystemLogType.LOG_SYS_B, Globals.SYSTEM_NAME, "FissionLoginServiceImpl","delLoginUser", "店员登出error", e);
        }
    }
    
    
    
}
