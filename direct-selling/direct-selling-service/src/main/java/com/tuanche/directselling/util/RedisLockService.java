package com.tuanche.directselling.util;

import com.alibaba.fastjson.JSON;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.exception.RedisException;
import com.tuanche.directselling.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * @author：HuangHao
 * @CreatTime 2020-11-13 10:40
 */
@Service
public class RedisLockService {

    @Autowired
    @Qualifier("ClusterRedisService")
    private RedisService redisService;
    //获取不到锁暂停时间-毫秒
    private static int sleepTime = 50;

    public RedisLockDto lock(String key,long milliseconds,long waitingTime){
        return lock(key, randomUUID(), milliseconds, waitingTime);
    }
    public RedisLockDto lock(String key,String value,long milliseconds,long waitingTime){
        return lock(key, value, "NX", "PX", milliseconds, waitingTime);
    }
    /**
     * 获取redis锁
     * @author HuangHao
     * @CreatTime 2020-11-13 11:38
     * @param key   键
     * @param value 值
     * @param nxxx
     * @param expx
     * @param time
     * @param waitingTime 等待时间，单位：毫秒，0不等待，如果等待时间到了还没获取到锁就返回
     * @return com.tuanche.directselling.util.RedisLockDto
     */
    public RedisLockDto lock(String key,String value,String nxxx, String expx, long time,long waitingTime){
        RedisLockDto lockResponse = new RedisLockDto(false,value);
        if(StringUtils.isEmpty(key)){
            return lockResponse;
        }
        value=StringUtils.isEmpty(value)?randomUUID():value;
        try {
            boolean whi = true;

            long wtime = 1;
            while (whi){
                RedisLockDto dto = new RedisLockDto(true,value);
                //此处直接放value的话在通过redisService.get(key,String.class)时会有序列化问题，因此换成对象存储
                String lock = redisService.set(key, JSON.toJSONString(dto), nxxx, expx, time);
                boolean isOk = Constants.OK.equals(lock);
                if(isOk || wtime>waitingTime){
                    whi = false;
                    lockResponse = new RedisLockDto(isOk,value);
                }else {
                    try {
                        Thread.sleep(sleepTime);
                        wtime+= sleepTime;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (RedisException e) {
            e.printStackTrace();
        }
        return lockResponse;
    }

    /**
     * 解锁
     * @author HuangHao
     * @CreatTime 2020-11-13 14:01
     * @param key
     * @param redisLockDto
     * @return void
     */
    public void unLock(String key,RedisLockDto redisLockDto){
        try {
            RedisLockDto dto = redisService.get(key, RedisLockDto.class);
            if(dto != null && redisLockDto.getValue().equals(dto.getValue())){
                redisService.del(key);
            }
        } catch (RedisException e) {
            e.printStackTrace();
        }
    }

    public static String randomUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

}
