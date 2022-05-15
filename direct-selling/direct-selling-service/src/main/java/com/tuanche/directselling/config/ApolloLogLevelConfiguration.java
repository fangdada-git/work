package com.tuanche.directselling.config;

import com.tuanche.arch.apollo.config.ApolloLogbackLinsenerInit;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
  * @description Apollo 日志级别热切换
  * @author ants·ht
  * @date 2018/7/24 19:55
*/
@Configuration
public class ApolloLogLevelConfiguration {

    @PostConstruct
    public void apolloLogbackListener() {
        new ApolloLogbackLinsenerInit().apolloLogbackLisener();
    }
}
