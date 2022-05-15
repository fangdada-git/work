package com.tuanche.directselling.api;

public interface FissionNoticeProducerService {

    /**
     * @description : 直播间下单成功通知直播间
     * @param : 
     * @return : 
     * @author : fxq
     * @date : 2020/12/17 11:12
     */
    void sendLiveOrderMessage(String msg);
    
}
