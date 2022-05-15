package com.tuanche.web.config;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.commons.util.StringUtils;
import com.tuanche.directselling.utils.StatusCodeEnum;
import com.tuanche.eap.api.utils.ResultDto;
import com.tuanche.manubasecenter.api.UserBaseService;
import com.tuanche.manubasecenter.constant.ManuBaseConstants;
import com.tuanche.manubasecenter.dto.TcResponse;
import com.tuanche.manubasecenter.model.CsUser;
import com.tuanche.web.service.FissionLoginServiceImpl;
import com.tuanche.web.util.DirectCommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Configuration
public class FissionLoginInterceptor extends HandlerInterceptorAdapter {

	@Reference
	private UserBaseService userBaseService;
	@Autowired
	private FissionLoginServiceImpl fissionLoginServiceImpl;
	@Autowired
	@Qualifier("ClusterRedisService")
	private RedisService redisService;
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		String token = request.getHeader(DirectCommonUtil.FISSION_SALE_LOGIN_TOKEN);
		try {
			if (StringUtils.isNotEmpty(token)) {
				Integer userId = redisService.get(DirectCommonUtil.FISSION_SALE_LOGIN_TOKEN_REDIS+token, Integer.class);
				if (userId !=null && userId>0) {
					CsUser csuser = redisService.get(DirectCommonUtil.FISSION_SESSION_SALE_REDIS+userId, CsUser.class);
					if (csuser!=null && csuser.getId()!=null && csuser.getUlevel()!=null
							&& (csuser.getUlevel().equals(ManuBaseConstants.MANUFACTURER_USER_TYPE3)
							|| csuser.getUlevel().equals(ManuBaseConstants.MANUFACTURER_USER_TYPE4)
							|| csuser.getUlevel().equals(ManuBaseConstants.MANUFACTURER_USER_TYPE5))) {
						if (!csuser.getUlevel().equals(ManuBaseConstants.MANUFACTURER_USER_TYPE5)) {
							String url = request.getServletPath();
							if (url.contains("/api/fission/manu/leader/")) {
								TcResponse tcResponse = DirectCommonUtil.setTcResponse(ManuBaseConstants.RESULT_CODE_10003, "无权限", System.currentTimeMillis(), 0);
								DirectCommonUtil.sendJsonMessage(response, tcResponse);
								return false;
							}
						}
						return true;
					} else if (csuser==null) {
						if (userId!=null && userId>0) {
							CsUser queryUser = new CsUser();
							queryUser.setId(userId);
							queryUser.setStat(ManuBaseConstants.CSUSER_STAT_USABLE);
							List<CsUser> userList = userBaseService.getCsUserList(queryUser);
							if (CollectionUtils.isNotEmpty(userList)) {
								ResultDto resultDto = fissionLoginServiceImpl.userLogin(userList.get(0).getUphone());
								if (resultDto.getCode().equals(StatusCodeEnum.SUCCESS.getCode())) {
									CsUser user = (CsUser) resultDto.getResult();
									fissionLoginServiceImpl.addLoginUserToRedis(user);
									return true;
								}
							} else {
								StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "裂变店员登陆拦截器 token对应的userid无效", token ,userId+"");
								redisService.del(DirectCommonUtil.FISSION_SALE_LOGIN_TOKEN_REDIS+token);
							}
						}
					}
					StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "裂变店员登陆拦截器 啥也没有", token ,""+userId);
				}
				StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "裂变店员登陆拦截器userId空", token ,""+userId);
			}
			StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "裂变店员登陆拦截器token空", token ,"裂变店员登陆拦截器token空");
		} catch (Exception e) {
			StaticLogUtils.error(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "FissionLoginInterceptor", "preHandle" ,"裂变店员登录拦截器error", e);
		}
		StaticLogUtils.info(SystemLogType.LOG_SYS_B, "DIRECT-SELLING-WEB", "裂变店员登陆拦截器", token ,"登陆失败");
		TcResponse tcResponse = DirectCommonUtil.setTcResponse(StatusCodeEnum.USER_NOT_LOGGED_IN.getCode(), "登陆失败", System.currentTimeMillis(), 0);
		DirectCommonUtil.sendJsonMessage(response, tcResponse);
		return false;
	}



}
