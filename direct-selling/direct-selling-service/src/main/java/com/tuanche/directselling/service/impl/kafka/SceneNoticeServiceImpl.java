package com.tuanche.directselling.service.impl.kafka;

import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tuanche.arch.kafka.producer.KafkaProducerService;
import com.tuanche.directselling.util.CommonLogUtil;

import cn.hutool.core.thread.ThreadUtil;

/**
 * @class: SceneNoticeServiceImpl
 * @description: 团车直卖-场次活动kafka通知实现类
 * @author: gongbo
 * @create: 2020年3月4日17:45:12
 */
@Service
public class SceneNoticeServiceImpl {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Value("${kafka.config.live-program-topic.topic-name}")
    private String sceneNoticeTopic;

    /*线程池工具类*/
    ExecutorService executorService = ThreadUtil.newExecutor(2);

    public void sendSceneMessage(String msg,String reqMessageId) {
        CommonLogUtil.addInfo(reqMessageId,"发送kafka通知直播系统-删除节目开始", msg);
        executorService.submit(() -> {
            try{
                kafkaProducerService.sendMessage(sceneNoticeTopic, msg);
            }catch (Exception e){
                CommonLogUtil.addError(reqMessageId,msg, e);
            }
        });
        CommonLogUtil.addInfo(reqMessageId,"发送kafka通知直播系统-删除节目结束", null);
    }

    public void sendDirectorMessage(String msg) {
        CommonLogUtil.addInfo("发送kafka通知直播系统-配置导播","开始", msg);
        executorService.submit(() -> {
            try{
                kafkaProducerService.sendMessage(sceneNoticeTopic, msg);
            }catch (Exception e){
                CommonLogUtil.addError("发送kafka通知直播系统-配置导播",msg, e);
            }
        });
        CommonLogUtil.addInfo("发送kafka通知直播系统-配置导播","结束", null);
    }

    public void sendDealerDelMessage(String msg) {
        CommonLogUtil.addInfo("发送kafka通知直播系统-删除经销商品牌配置","开始", msg);
        executorService.submit(() -> {
            try{
                kafkaProducerService.sendMessage(sceneNoticeTopic, msg);
            }catch (Exception e){
                CommonLogUtil.addError("发送kafka通知直播系统-删除经销商品牌配置",msg, e);
            }
        });
        CommonLogUtil.addInfo("发送kafka通知直播系统-删除经销商品牌配置","结束", null);
    }


}