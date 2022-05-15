package com.tuanche.web.filter;

import com.tuanche.arch.web.interceptor.log.BaseInterceptor;
import com.tuanche.arch.web.interceptor.log.LogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @class: DoInterceptorConfigurer
 * @description:
 * @author: dudg
 * @create: 2019-09-27 15:15
 */
@Configuration
public class DoInterceptorConfigurer extends WebMvcConfigurerAdapter {

    /**
     * @Author GongBo
     * @Description 多个拦截器组成一个拦截器链
     * @Date 16:50 2019/3/20
     * @Param [registry]
     * @return void
     **/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 注册拦截器
        InterceptorRegistration ir1 = registry.addInterceptor(new BaseInterceptor());
        // 配置拦截的路径
        ir1.addPathPatterns("/**");
        // 配置不拦截的路径
        ir1.excludePathPatterns("/**.html");
        // 静态资源
        ir1.excludePathPatterns("/static/**");

        // 登录拦截器
        InterceptorRegistration ir2 = registry.addInterceptor(new LogInterceptor());
        // 配置拦截的路径
        ir2.addPathPatterns("/**");
    }
}
