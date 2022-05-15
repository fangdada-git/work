package com.tuanche.directselling.config;


import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import com.deepoove.swagger.dubbo.annotations.EnableDubboSwagger;
import org.springframework.context.annotation.Configuration;

/**
 * @class: SwaggerConfig
 * @description: swagger配置类
 * @author: dudg
 * @create: 2019-07-09 22:48
 */
@Configuration
@DubboComponentScan(basePackages = { "com.tuanche.directselling.service" })  //dubbo实现类的路径
@EnableDubboSwagger //生成api-docs及调用的REST接口
public class SwaggerConfig{

}

