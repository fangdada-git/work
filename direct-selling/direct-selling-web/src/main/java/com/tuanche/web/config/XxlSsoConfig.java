package com.tuanche.web.config;

import com.tuanche.inner.sso.core.filter.XxlSsoFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @class: XxlSsoConfig
 * @description: sso配置
 * @author: dudg
 * @create: 2019-10-29 15:23
 */
@Configuration
public class XxlSsoConfig {

    @Bean
    public FilterRegistrationBean xxlSsoFilterRegistration() {


        // filter
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setName("XxlSsoFilter");
        registration.setOrder(1);
        registration.addUrlPatterns("/*");
        //直接跳转使用XxlSsoFilter
        registration.setFilter(new XxlSsoFilter());
        //过滤器正则匹配，^/css/ 代表以/css/开头
        registration.addInitParameter("ignoreRegex","^/css/|^/images/|^/js/|^/findpwd/|^/materials/web/|^/dealerAnchor/|^/api/");
        //后缀排除规则，可用于静态文件排除，“|”隔开
        registration.addInitParameter("ignoreSuffix",".icon|.jpg|.png|.css|.js");
        return registration;

    }
}
