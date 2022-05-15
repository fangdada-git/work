package com.tuanche.directselling.service.impl.kafka;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.tuanche.arch.kafka.producer.KafkaProducerService;
import com.tuanche.directselling.api.FissionNoticeProducerService;
import com.tuanche.directselling.util.CommonLogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.ExecutorService;

@Service
public class FissionNoticeProducerServiceImpl implements FissionNoticeProducerService {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Value("${kafka.config.live-order-notice.topic-name}")
    private String fissionNoticeTopic;

    /*线程池工具类*/
    ExecutorService executorService = ThreadUtil.newExecutor(2);

    /**
     * @description : 直播间下单成功通知直播间
     * @param : 
     * @return : 
     * @author : fxq
     * @date : 2020/12/17 11:12
     */
    @Override
    public void sendLiveOrderMessage(String jsonData) {
        CommonLogUtil.addInfo("sendLiveOrderMessage","发送kafka通知直播间-直播间下单成功start", jsonData);
        executorService.submit(() -> {
            try{
                kafkaProducerService.sendMessage(fissionNoticeTopic, jsonData);
            }catch (Exception e){
                CommonLogUtil.addError("发送kafka通知直播间-直播间下单成功error",jsonData, e);
            }
        });
        CommonLogUtil.addInfo("sendLiveOrderMessage","发送kafka通知直播间-直播间下单成功end", null);
    }

}
