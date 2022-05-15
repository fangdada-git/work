package com.tuanche.directselling.service.impl.kafka;

import com.alibaba.fastjson.JSONObject;
import com.tuanche.arch.kafka.consumer.KafkaBaseConsumer;
import com.tuanche.directselling.service.impl.FissionActivityServiceImpl;
import com.tuanche.directselling.util.CommonLogUtil;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @class: FissionLiveStartNoticeConsumer
 * @description: 裂变直播间开播消息通知
 * @author: dudg
 * @create: 2020-11-06 16:51
 */
@Service
public class FissionLiveStartNoticeConsumer extends KafkaBaseConsumer<String, String> implements CommandLineRunner {

    @Value("${kafka.config.live-Start-notice.group-name}")
    private String groupName;
    @Value("${kafka.config.zookeeper-connect}")
    private String zkConnect;
    @Value("${kafka.config.live-Start-notice.topic-name}")
    private String topic;
    @Value("${kafka.config.live-Start-notice.topic-count}")
    private int topicCount;

    @Autowired
    FissionActivityServiceImpl fissionActivityServiceImpl;

    @Bean
    public ConsumerConnector videoCallBackConnector(){
        com.tuanche.arch.kafka.consumer.KafkaConsumerFactoryBean consumerFactoryBean = new com.tuanche.arch.kafka.consumer.KafkaConsumerFactoryBean();
        consumerFactoryBean.setGroupName(groupName);
        consumerFactoryBean.setZookeeperConnect(zkConnect);

        return consumerFactoryBean.getObject();
    }

    /**
     * kafka 消费消息
     * @param message
     */
    @Override
    public void consumeMsg(String message) {

        try {
            // 开播通知
            CommonLogUtil.addInfo("裂变活动直播间开播通知", "接受到消息=======", message);

            if (StringUtils.isNotEmpty(message)) {
                JSONObject jsonObject = JSONObject.parseObject(message);
                String fissionId = jsonObject.getString("fissionId");
                String liveId = jsonObject.getString("liveId");
                if(StringUtils.isNotEmpty(liveId)){
                    fissionActivityServiceImpl.liveStartRemind(Integer.valueOf(liveId));
                }
            }
            CommonLogUtil.addInfo("裂变活动直播间开播通知", "消费消息完成=======");
        } catch (Exception e) {
            e.printStackTrace();
            CommonLogUtil.addError("裂变活动直播间开播通知", "发生异常-"+e.getMessage(),e);
        }
    }


    @Override
    public void run(String... args) throws Exception {
        Map<String,Integer> topicCountmap = new HashMap<>();
        topicCountmap.put(topic,Integer.valueOf(topicCount));
        StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
        StringDecoder valDecoder = new StringDecoder(new VerifiableProperties());
        Map<String, List<KafkaStream<String,String>>> consumeMap = videoCallBackConnector().createMessageStreams(topicCountmap,keyDecoder,valDecoder);
        this.subscribe(topic,topicCount,consumeMap);

    }

    @PreDestroy
    public void shutdown(){
        if(videoCallBackConnector() !=null ) videoCallBackConnector().shutdown();
        super.shutdown();
    }

}

