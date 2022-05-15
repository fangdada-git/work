package com.tuanche.web.config;

import com.tuanche.arch.apollo.config.ApolloLogbackLinsenerInit;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @class: ApolloLogLevelConfiguration
 * @description:
 * @author: dudg
 * @create: 2019-09-27 14:31
 */
@Configuration
public class ApolloLogLevelConfiguration {

    @PostConstruct
    public void apolloLogbackListener() {
        new ApolloLogbackLinsenerInit().apolloLogbackLisener();
    }
}