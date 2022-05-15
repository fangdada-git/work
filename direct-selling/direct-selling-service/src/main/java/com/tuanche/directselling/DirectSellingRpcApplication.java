package com.tuanche.directselling;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author ants·ht
 * @description
 * @date 2018/8/7 16:22
 */
@DubboComponentScan(value = {"com.tuanche.directselling.service.impl"})
@ComponentScan(basePackages = {"com.tuanche.directselling", "com.tuanche.arch.redis.cluster",
        "com.tuanche.arch.zipkin", "com.tuanche.arch.job.config"})
@SpringBootApplication
@EnableApolloConfig
@EnableTransactionManagement
public class DirectSellingRpcApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DirectSellingRpcApplication.class);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(DirectSellingRpcApplication.class);
    }
}
