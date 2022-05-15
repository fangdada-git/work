package com.tuanche.directselling.util;

import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author: czy
 * @Date: 2020/5/25 11:15
 * @Version 1.0
 */
public class ConstantsUtil {
    /** 海报类型  6 海报预热 */
    public static final Integer POSTER_TYPE_6 = 6;
    /** 海报类型 7 海报正式*/
    public static final Integer POSTER_TYPE_7 = 7;

    /** 节目类型  1 预热 */
    public static final Integer PROGRAM_TYPE_B_1 = 1;
    /** 节目类型 2 正式*/
    public static final Integer PROGRAM_TYPE_B_2 = 2;

    /** 商品类型  1 车款 */
    public static final Integer GOODS_TYPE_B_1 = 1;
    /** 商品类型 2 到店礼*/
    public static final Integer GOODS_TYPE_B_2 = 2;

    /** 商品状态  0 草稿*/
    public static final Integer GOODS_STATUS_B_0 = 0;
    /** 商品状态 1 待审核*/
    public static final Integer GOODS_STATUS_B_1 = 1;
    /** 商品状态 2 审核通过*/
    public static final Integer GOODS_STATUS_B_2 = 2;
    /** redis锁返回结果 */
    public static final String OK = "OK";

    public static final String YES = "YES";
    public static final String TRUE = "true";
    //批量入库条数
    public static int BATCH_PAGE_SIZE = 1000;

    /**
     * 批量入库的次数
     * @author HuangHao
     * @CreatTime 2020-09-29 17:08
     * @param listSize
     * @return int
     */
    public static int batchNum(int listSize){
        return listSize%ConstantsUtil.BATCH_PAGE_SIZE==0?listSize/ConstantsUtil.BATCH_PAGE_SIZE:listSize/ConstantsUtil.BATCH_PAGE_SIZE+1;
    }

    public static class FISSION_GOODS_ORDER {
        //商品邮寄
        public final static int goods_post = 1;
        //商品核销
        public final static int goods_checkout = 0;
        //订单来源 0：自然流量  1：直播间下单  2：裂变活动页下单
        public final static int source_nature = 0;
        //订单来源 0：自然流量  1：直播间下单  2：裂变活动页下单
        public final static int source_live = 1;
        //订单来源 0：自然流量  1：直播间下单  2：裂变活动页下单
        public final static int source_fission = 2;



    }
    /**
     * 是否是微信unionid
     * @author HuangHao
     * @CreatTime 2021-01-28 14:26
     * @param wxUnionId
     * @return boolean
     */
    public static boolean isWxUnionId(String wxUnionId){
        return StringUtils.isEmpty(wxUnionId)||wxUnionId.length() != 28?false:true;
    }

    /**
     * 或者指定分钟（可为负数）的时间
     * @author HuangHao
     * @CreatTime 2021-08-19 15:39
     * @param i
     * @param dfs   格式
     * @return java.lang.String
     */
    public static String getMinuteDateStr(int i, String dfs)  {
        SimpleDateFormat dft = new SimpleDateFormat(dfs);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(calendar.MINUTE, i);
        return dft.format(calendar.getTime());
    }
}
