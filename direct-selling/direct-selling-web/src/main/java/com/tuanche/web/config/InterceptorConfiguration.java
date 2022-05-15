package com.tuanche.web.config;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.arch.interceptor.LoginedInterceptor;
import com.tuanche.arch.service.LoginRpcService;
import com.tuanche.arch.web.exception.handler.ExceptionHandler;
import com.tuanche.arch.web.interceptor.log.BaseInterceptor;
import com.tuanche.arch.web.interceptor.sign.SignInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @description API 拦截器的相关配置
 * @author ants·ht
 * @date 2018/8/13 15:34
 */
@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    @Reference
    LoginRpcService loginRpcService;

    @Bean
    FissionLoginInterceptor loginInterceptor() {
        return new FissionLoginInterceptor();
    }

    @Bean
    public LoginRpcService getLoginRpcService(){
        return loginRpcService;
    }

    @Bean
    public LoginedInterceptor getUserInfoInterceptor(){
        return new LoginedInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BaseInterceptor()).addPathPatterns("/**");
//		registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**");
        //http接口验签
        InterceptorRegistration signIr = registry.addInterceptor(new SignInterceptor());
////        signIr.addPathPatterns("/dealerAnchor/**");
////        signIr.excludePathPatterns("/dealerAnchor/synctblogincookie");
////        裂变接口验签
        signIr.addPathPatterns("/api/fission/**").
                excludePathPatterns(new String[]{"/api/fission/manu/user/order/userOrderNotice"
                                        ,"/api/fission/manu/sales/order/salesOrderNotice"
                                        ,"/api/fission/activityH5OrMiniProgram"
                                        ,"/api/v2/afterSale/subAccountNotice"
                                        ,"/api/fission/manu/sales/getLoginPicVerifyCode"});
        //裂变B端用户登陆拦截
        registry.addInterceptor(loginInterceptor()).addPathPatterns(new String[]{"/api/fission/manu/sales/order/**"
                                        ,"/api/fission/manu/sales/**"
                                        ,"/api/fission/manu/leader/**"
                                        ,"/api/fission/manu/list"
                                        ,"/api/fission/manu/detail"
                                        ,"/api/fission/manu/info"
                                        ,"/api/fission/manu/integral/list"
                                        ,"/api/fission/manu/wallet"
                                        ,"/api/fission/manu/order"
                                        ,"/api/fission/manu/taskRecord/list"
                                        ,"/api/fission/manu/activity"
                                        ,"/api/fission/manu/score"
                                        ,"/api/fission/manu/saleWithDrawal"})
                .excludePathPatterns(new String[]{"/api/fission/manu/sales/sendVerifyCode"
                                        ,"/api/fission/manu/sales/userLogin"
                                        ,"/api/fission/manu/sales/getLoginPicVerifyCode"
                                        ,"/api/fission/manu/sales/checkPicVerifyCode"
                                        ,"/api/fission/manu/sales/order/salesOrderNotice"
                                        ,"/api/v2/afterSale/subAccountNotice"
                                        ,"/api/afterSale/agent/**"
                                        ,"/api/after/sale/coupon/scanGetCoupon"
                });
        //裂变c端用户登陆拦截
        registry.addInterceptor(getUserInfoInterceptor()).addPathPatterns(new String[]{"/api/fission/manu/user/order/**"
                                        ,"/api/fashioncar/order/**"
                                        ,"/api/fashioncar/winning/getCodeList"
                                        ,"/api/fashioncar/winning/getHalfPriceWinNum"
                                        ,"/api/fashioncar/helper/getHelperUserList"
                                        })
                .excludePathPatterns(new String[]{"/api/fission/manu/user/order/userOrderNotice"
                                        ,"/api/fission/activityH5OrMiniProgram"
                });
    }

    /**
     * @description 统一异常处理的拦截器配置
     * @author ants·ht
     * @date 2018/8/13 15:46
     */
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(new ExceptionHandler(""));
    }

}

