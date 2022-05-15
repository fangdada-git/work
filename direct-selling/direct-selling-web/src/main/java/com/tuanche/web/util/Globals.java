package com.tuanche.web.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/** 
 * 项目名称： order_server
 * 类名称： Globals.java
 * 类描述： TODO
 * 创建人： Administrator
 * 创建时间： 2018-7-11下午4:33:42
 * 修改人： Administrator
 * 修改时间：  2018-7-11下午4:33:42
 * 修改备注：  TODO
 * @version
 */
public class Globals {
	
	/**
	 * 日志参数	系统类型
	 */
	public static final String SYSTEM_TYPE = "SYS_B";
	/**
	 * 日志参数	系统名称
	 */
	public static final String SYSTEM_NAME = "direct-selling-web";
	
	private static final Map<String, ThreadLocal<SimpleDateFormat>> dateFormatPool = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

	private static final Object dateFormatLock = new Object();
	
	private static String timeFormatPattern = "HHmmss";
	// \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔),    
		// 字符串在编译时会被转码一次,所以是 "\\b"    
		// \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)    
		static String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i"    
		       +"|windows (phone|ce)|blackberry"    
		       +"|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp"    
		       +"|laystation portable)|nokia|fennec|htc[-_]"    
		       +"|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";    
		static String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser"    
		       +"|[1-4][0-9]{2}x[1-4][0-9]{2})\\b"; 
	//移动设备正则匹配：手机端、平板  
	static Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);    
	static Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);  
	
	public static final int COUPON_CODE_TOTAL_LENGTH = 12;
	public static final int COUPON_CODE_PASS_LENGTH = 4;
	

    /** redis全局缓存key-根据团车节ID、品牌ID得到所有经销商列表-JSON */
    public static final String CACHE_KEY_PERIODS_BRAND_DEALER_LIST_JSON = "order_service:cache:periods:brand:dealer:list:json:";
    /** redis全局缓存key-团车节下配置的所有经销商的品牌列表-JSON */
    public static final String CACHE_KEY_PERIODS_BRAND_LIST_JSON = "order_service:cache:periods:brand:list:json:";
    
	/**
	 * 
	 * @author: fxq
	 * @Description:生成订单编号  先保存订单  在更新订单编号
	 * @param: @param orderId
	 * @param: @return
	 * @return: String
	 * @time: 2018年10月17日 下午2:03:51
	 */
    public static String createCheMallOrderNo(final Long orderId) {
        final Random random = new Random(new Date().getTime());
        final int ranval = random.nextInt(100);
        final String orderNo = "22" + orderId + String.format("%02d", ranval);
        return orderNo;
    }
	/**
	 * @author: fxq
	 * @Description:生成orderTradeId 	 this.orderTradeByTime("mall_", updateOrderInfo.getOrderNo())
	 * @param: @param prefix
	 * @param: @param orderNo
	 * @param: @return
	 * @return: String
	 * @time: 2018年10月17日 下午2:04:02
	 */
	public static String orderTradeByTime(String prefix, String orderNo) {
		String dateTime = getTimeFormat().format(new Date());
		//System.out.println(dateTime);
		return prefix + dateTime + orderNo;
	}
	
	private static SimpleDateFormat getTimeFormat() {
    	ThreadLocal<SimpleDateFormat> tl = dateFormatPool.get(timeFormatPattern);
		if (null == tl) {
			synchronized (dateFormatLock) {
				tl = dateFormatPool.get(timeFormatPattern);
				if (null == tl) {
					tl = new ThreadLocal<SimpleDateFormat>() {
						@Override
						protected synchronized SimpleDateFormat initialValue() {
							return new SimpleDateFormat(timeFormatPattern);
						}
					};
					dateFormatPool.put(timeFormatPattern, tl);
				}
			}
		}
		return tl.get();
    }
	
	/** 
	* 检测是否是移动设备访问 
	*  
	* @Title: check 
	* @Date : 2014-7-7 下午01:29:07 
	* @param userAgent 浏览器标识 
	* @return true:移动设备接入，false:pc端接入 
	*/  
	public static Integer check(HttpServletRequest request){
		String userAgent = request.getHeader("user-agent").toLowerCase();
		if(null == userAgent){    
			userAgent = "";    
		}    
		//匹配    
		Matcher matcherPhone = phonePat.matcher(userAgent);    
		Matcher matcherTable = tablePat.matcher(userAgent);    
		if(matcherPhone.find() || matcherTable.find()){    
			return 1;    
		} else {    
			return 0;    
		}    
	}
	
	
	public static String getCoupleCode(Integer coupleCodeId){
		String couponCode = StringUtils.leftPad(String.valueOf(coupleCodeId), COUPON_CODE_TOTAL_LENGTH - COUPON_CODE_PASS_LENGTH, '0')
                + StringUtils.leftPad(String.valueOf(RandomUtils.nextInt(0, (int)Math.pow(10, COUPON_CODE_PASS_LENGTH))), COUPON_CODE_PASS_LENGTH, '0');
		return couponCode;
	}
	
	public static class FISSION_GOODS {
		//是否秒杀 1：是  0：否
		public final static int seckill = 1;
		public final static int seckill_no = 0;
		//是否删除 1：是  0：否
		public final static int delete = 1;
		public final static int delete_no = 0;
		//是否上架
		public final static int upShelf = 1;
		public final static int upShelf_no = 0;
		public final static int downShelf = 1;
		public final static int downShef_no = 0;
		//是否助力
		public final static int isHelper = 1;
		public final static int isHelper_no = 0;
		//商品助力是否购买过
		public final static int have_not_purchased = 0;
		public final static int have_purchased = 1;
		//商品是否有未付款订单  1:是|有     0：否|无
		public final static int goods_non_pay = 1;
		public final static int goods_non_pay_no = 0;

	}
	public static class FISSION_GOODS_ORDER {
		//商品邮寄
		public final static int goods_post = 1;
		//商品核销
		public final static int goods_checkout = 0;
		//订单来源 0：自然流量  1：直播间下单  2：裂变活动页下单  3:app下单
		public final static int source_nature = 0;
		//订单来源 0：自然流量  1：直播间下单  2：裂变活动页下单  3:app下单
		public final static int source_live = 1;
		//订单来源 0：自然流量  1：直播间下单  2：裂变活动页下单  3:app下单
		public final static int source_fission = 2;
		//订单来源 0：自然流量  1：直播间下单  2：裂变活动页下单  3:app下单
		public final static int source_app = 3;

		//支付来源   0:pc 1:wap 2:app  3:微信小程序
		public final static int pay_source_pc = 0;
		public final static int pay_source_wap = 1;
		public final static int pay_source_app = 2;
		public final static int pay_source_wx_mini_program = 3;

		//订单是否已过期   1:是  0否
		public final static int is_expired = 1;
		public final static int is_expired_non = 0;

		//终端设置类型   http://wiki.tuanche.cn/pages/viewpage.action?pageId=122095391#id-数据字典服务-2、终端设置类型（sid）定义及枚举值
		//1:PC站  -1:M站  -38:微信  -6:CC/400客服  -3:第三方接口  -1111:小程序  -1112:团车活动小程序  22:IOS  24:安卓  -1113:支付宝小程序
		public final static int SID_PC = 1;
		public final static int SID_MOBILE = -1;
		public final static int SID_WECHAT = -38;
		public final static int SID_CALL_CENTER = -6;
		public final static int SID_THIRD_API = -3;
		public final static int SID_MINI_PROGRAM = -1111;
		public final static int SID_ACTIVITY = -1112;
		public final static int SID_ANDROID = 24;
		public final static int SID_IOS = 22;
		public final static int SID_ALIPAY_PROGRAM = -1113;


	}
	
}
