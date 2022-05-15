package com.tuanche.web;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @class: DirectSellingWebApplication
 * @description: web-web启动类
 * @author: gongbo
 * @create: 2020年3月4日18:41:06
 */
@ComponentScan(basePackages = {"com.tuanche.web", "com.tuanche.arch.zipkin","com.tuanche.arch.redis.cluster"})
@SpringBootApplication(exclude = MongoAutoConfiguration.class)
@Configuration
@EnableApolloConfig
public class DirectSellingWebApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(DirectSellingWebApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(DirectSellingWebApplication.class);
    }
}