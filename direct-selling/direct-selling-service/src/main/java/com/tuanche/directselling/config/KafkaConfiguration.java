package com.tuanche.directselling.config;

import com.tuanche.arch.kafka.producer.KafkaProducerFactoryBean;
import com.tuanche.arch.kafka.producer.KafkaProducerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: materials
 * @description: kafka配置类
 * @author: gongbo
 * @create: 2020年3月4日17:26:18
 **/
@Configuration
public class KafkaConfiguration {

    @Value("${kafka.config.servers}")
    private String servers;
    @Value("${kafka.config.batch-size}")
    private int batchSize;
    @Value("${kafka.config.buffer-memory}")
    private int bufferMemory;

    @Bean
    KafkaProducerService kafkaProducerService(){
        KafkaProducerFactoryBean kafkaProducerFactoryBean = new KafkaProducerFactoryBean();
        kafkaProducerFactoryBean.setBatchSize(batchSize);
        kafkaProducerFactoryBean.setBufferMemory(bufferMemory);
        kafkaProducerFactoryBean.setServers(servers);
        return kafkaProducerFactoryBean.getObject();
    }


}
