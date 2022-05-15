package com.tuanche.directselling.config;

import com.tuanche.arch.util.utils.CommonUtils;
import org.apache.commons.configuration.CompositeConfiguration;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2021/9/3 14:57
 **/
@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient() {
        CompositeConfiguration configuration = CommonUtils.getConfiguration(new String[]{"./config/", "./"}, "application.redis.cluster.properties", (CompositeConfiguration) null);
        List<String> redisUrl = new ArrayList();
        for (String url : configuration.getString("redis.cluster.nodes").split(",")) {
            redisUrl.add("redis://" + url);
        }
        Config config = new Config();
        config.useClusterServers()
                .setScanInterval(2000)
                .setTimeout(30000)
                .setNodeAddresses(redisUrl);
        config.setCodec(new JsonJacksonCodec());
        return Redisson.create(config);
    }
}
