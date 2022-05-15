package com.tuanche.directselling.util;

import com.google.common.collect.Lists;
import com.tuanche.arch.redis.service.RedisService;
import com.tuanche.arch.util.exception.RedisException;
import com.tuanche.arch.util.utils.DateUtils;
import com.tuanche.commons.util.StringUtil;
import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @class: FuncUtil
 * @description: 公共方法类
 * @author: dudg
 * @create: 2019-07-05 16:34
 */
public class FuncUtil {

    public static <T> T redisGetOrSetData(RedisService redisService, String key, Class<T> backType, Supplier function, int ttl) throws RedisException {
        T data = StringUtils.isEmpty(key) ? null : redisService.get(key, backType);
        if (data != null)
            return data;
        else {
            data = (T) function.get();
            if(data != null){
                if(ttl>0) {
                    redisService.set(key, data, ttl);
                }
                else{
                    redisService.set(key, data);
                }
            }
            return data;
        }
    }


    public static <T> T redisGetOrSetData(RedisService redisService, String key, Class<T> backType, Supplier function) throws RedisException {
        return redisGetOrSetData(redisService,key,backType,function,0);
    }


    public static boolean isMobile(String mobile){
        if(StringUtil.isEmpty(mobile))return false;
        Pattern pattern = Pattern.compile("^[1][3-9][0-9]{9}$");
        Matcher matcher = pattern.matcher(mobile.trim());
        return matcher.matches();
    }

    public static String post(String uri, String jsonBody, String head) throws Exception {
        String result = null;
        OutputStream output = null;
        ByteArrayOutputStream baos = null;
        InputStream ips = null;
        HttpURLConnection httpconn = null;

        try {
            URL url = new URL(uri);
            httpconn = (HttpURLConnection)url.openConnection();
            httpconn.setRequestMethod("POST");
            httpconn.setDoInput(true);
            httpconn.setDoOutput(true);
            httpconn.setRequestProperty("des", "false");
            httpconn.addRequestProperty("Content-Type", "application/json");
            httpconn.setConnectTimeout(10000);
            httpconn.setReadTimeout(10000);
            if (head != null && !head.equals("")) {
                httpconn.setRequestProperty("traceinfo", head);
            }

            output = httpconn.getOutputStream();
            output.write(jsonBody.getBytes(Charset.forName("UTF-8")));
            ips = httpconn.getInputStream();
            baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];

            int len;
            while((len = ips.read(buf)) > 0) {
                baos.write(buf, 0, len);
            }

            result = new String(baos.toByteArray());
            return result;
        } catch (Exception var14) {
            var14.printStackTrace();
            throw new Exception(var14);
        } finally {
            if (output != null) {
                output.close();
            }

            if (baos != null) {
                baos.close();
            }

            if (ips != null) {
                ips.close();
            }

            if (httpconn != null) {
                httpconn.disconnect();
            }

        }
    }

    /***
     * @description: 校验Integer !=null && !=notValue
     * @param tagert
     * @param notValue
     * @return: java.lang.Boolean
     * @author: dudg
     * @date: 2020/6/10 18:16
     */
    public static Boolean notNullAndEquals(Integer tagert, Integer notValue){
        return tagert != null && !tagert.equals(notValue);
    }

    /**
     * @description: 校验Integer ==null or ==Value
     * @param tagert
     * @param value
     * @return: java.lang.Boolean
     * @author: dudg
     * @date: 2020/9/28 11:44
    */
    public static Boolean isNullOrEquals(Integer tagert, Integer value){
        return tagert == null || tagert.equals(value);
    }

    /**
     *  日期字符串转Date  (yyyy-MM-dd HH:mm:ss)
     * @param dateStr
     * @return
     */
    public static Date StringToDate(String dateStr) {
        return DateUtils.stringToDate(dateStr, "yyyy-MM-dd HH:mm:ss");
    }

    /***
     * @description: 返回两个日期秒差
     * @param before
     * @param after
     * @return: java.lang.Long
     * @author: dudg
     * @date: 2020/10/12 17:19
    */
    public static Long dateSecDiff(Date before, Date after){
        return (after.getTime() - before.getTime())/1000;
    }

    /**
     * @description: 生成订单编号
     * @param: initial 订单起始标识
     * @param: orderId 订单id
     * @param: num 订单长度
     * @author: fxq
     * @date: 2020/9/28 17:17
     */
    public static String createOrderNo(String initial, int orderId, int num) {
        String orderStr = initial + new Date().getTime() + orderId;
        return orderStr.substring(orderStr.length()-num);
    }


    /**
     * @description: 获取分布式锁
     * @param redisService
     * @param lockKey
     * @param requestId
     * @param expTime 超时时间毫秒
     * @return: java.lang.String
     * @author: dudg
     * @date: 2020/9/28 18:21
    */
    public static boolean getDistributeLock(RedisService redisService,String lockKey,String requestId, long expTime) {
        try {
            String result = redisService.set(lockKey, requestId, "NX", "PX", expTime);
            if ("OK".equals(result)) {
                return true;
            }
        } catch (RedisException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @description: 释放分布式锁
     * @param redisService
     * @param lockKey
     * @param requestId
     * @return: boolean
     * @author: dudg
     * @date: 2020/9/28 18:28
    */
    public static boolean releaseDistributeLock(RedisService redisService,String lockKey,String requestId) {
        try {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = redisService.eval(script, Lists.newArrayList(lockKey), Lists.newArrayList(requestId));
            if (Long.valueOf(1L).equals(result)) {
                return true;
            }
        } catch (RedisException e) {
            e.printStackTrace();
        }
        return false;
    }
}
