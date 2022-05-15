package com.tuanche.web.config;

import com.tuanche.arch.web.interceptor.sign.SignPropertiesParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: direct-selling
 * @description: ${description}
 * @author: fxq
 * @create: 2020-03-31 10:33
 **/
@Configuration
public class SignKeyConfiguration {
    @Value("${sign.sso.service.url}")
    private String ssoUrl;
    @Bean(name = "signPropertiesParam")
    public SignPropertiesParam signPropertiesParam()  {
        SignPropertiesParam signPropertiesParam = new SignPropertiesParam();
        signPropertiesParam.setSignSsoUrl(ssoUrl);
        return signPropertiesParam;
    }
}
