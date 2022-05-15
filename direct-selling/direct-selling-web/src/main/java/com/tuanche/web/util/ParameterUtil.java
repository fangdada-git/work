package com.tuanche.web.util;

import com.tuanche.commons.exception.BusinessException;
import com.tuanche.framework.base.util.StringKit;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * 项目名称： order_server
 * 类名称： ParameterUtil.java
 * 类描述： TODO
 * 创建人： Administrator
 * 创建时间： 2018年10月18日下午7:59:31
 * 修改人： Administrator
 * 修改时间：  2018年10月18日下午7:59:31
 * 修改备注：  TODO
 * @version
 */
public class ParameterUtil {
	
	/** header 参数的前缀 */
	private final static String HEADER_PRE = "h_";
	/** header 参数的前缀 */
	private final static String DES_LOW = "h_des";
	/** header 参数的前缀 */
	private final static String FILE_LOW = "h_file";
	
	public static String PASSWORD_CRYPT_KEY = "z&-ls0n!";
	public static final String ALGORITHM_DES = "DES";
	private static Key key=null;
	
	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * @author: fxq
	 * @Description:	参数解析
	 * @param: @param request
	 * @param: @return
	 * @param: @throws Exception
	 * @return: Map<String,Object>
	 * @time: 2018年10月19日 上午10:26:37
	 */
	public static Map<String, Object> getBodyHeadMapFromDesForInter(final HttpServletRequest request) throws Exception {

		//获取带前缀的header信息
		final Map<String, Object> headers = _getHeadersInfo(request);

		if (request.getMethod().equalsIgnoreCase("GET")) {
			return headers;
		}

		boolean desFlag = false;
		final Object des = headers.get(DES_LOW);
		if (des != null && Boolean.parseBoolean(des.toString())) {
			desFlag = true;
		}

		boolean fileFlag = false;
		final Object fileStr = headers.get(FILE_LOW);
		if (fileStr != null && Boolean.parseBoolean(fileStr.toString())) {
			fileFlag = true;
		}

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (!fileFlag) {
			try {
				final InputStream ips = request.getInputStream();
//				IOHelper.copyStream(ips, baos);
				copyStream(ips, baos);
			} catch (final IOException e) {
				throw e;
			}
		}

		final String body = baos.toString();
//		System.out.println("body:" + body);
		if (NuNStr(body)) {
//			if (Check.NuNStr(body)) {
			return headers;
		}

		String jsonBody = body;
		if (desFlag) {
			jsonBody = decrypt(body);
//			jsonBody = DesUtils.decrypt(body);
		}

		headers.put("bodyJson", jsonBody);
		Map rstMap = null;
		final List rstList = null;
		if (!fileFlag) {
			try {
				//将参数转化成map
				rstMap = json2Map(jsonBody);
//				rstMap = JsonUtil.json2Map(jsonBody);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}

		if (rstMap != null) {
			headers.putAll(rstMap);
		}
		return headers;
	}

	public static String getBodyHeadMapFromDesForInter2(final HttpServletRequest request) throws Exception {

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			final InputStream ips = request.getInputStream();
			copyStream(ips, baos);
		} catch (final IOException e) {
			throw e;
		}
		final String body = baos.toString();
		
		String jsonBody = body;
		if (body!=null) {
			jsonBody = decrypt(body);
		}else{
			return null;
		}
		
		return jsonBody;
	}

	/**
	 *
	 * @param request
	 * @return 使用com.tuanche.framework.base.util.DesUtils解密
	 * @throws Exception
	 */
	public static String getBodyHeadMapFromDesForInter2SubAccount(final HttpServletRequest request) throws Exception {

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			final InputStream ips = request.getInputStream();
			copyStream(ips, baos);
		} catch (final IOException e) {
			throw e;
		}
		final String body = baos.toString();

		String jsonBody = body;
		if (body!=null) {
			jsonBody = com.tuanche.framework.base.util.DesUtils.decrypt(body);
		}else{
			return null;
		}

		return jsonBody;
	}

	/**
	 * 拷贝流
	 * 
	 * @param ips
	 * @param ops
	 */
	public static void copyStream(final InputStream ips, final OutputStream ops) {
		final byte[] buf = new byte[10];
		int len;
		try {
			while ((len = ips.read(buf)) != -1) {
				ops.write(buf, 0, len);
			}
		} catch (final IOException ignore) {
			ignore.printStackTrace();
		}
	}
	/**
	 * 获取请求头信息par is list note map
	 * @param request
	 * @return
	 */
	private static Map<String, Object> _getHeadersInfo(final HttpServletRequest request) {
		final Map<String, Object> map = new HashMap<String, Object>();
		final Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			final String key = (String) headerNames.nextElement();
			final String value = request.getHeader(key);
			map.put(HEADER_PRE + key, value);
		}
		return map;
	}
	/**
	 * 检测字符串是否为空.
	 * <p>
	 * 1. 未分配内存
	 * </p>
	 * <p>
	 * 2. 字符串剔除掉前后空格后的长度为0
	 * </p>
	 * 
	 * @param str
	 * 待检测的字符串
	 * @return true: 空; false:非空.
	 */
	public static boolean NuNStr(final String str) {
		return (null == str) || (str.trim().length() == 0) || StringUtils.isEmpty(str);
	}
	/**
	 * 根据指定的密钥及算法，将字符串进行解密。
	 * 
	 * @param data
	 *            要进行解密的数据，它是由原来的byte[]数组转化为字符串的结果。
	 * @return 解密后的结果。它由解密后的byte[]重新创建为String对象。如果解密失败，将返回null。
	 */
	public static String decrypt(String data)
	{
		try {
			key = getDESKey(PASSWORD_CRYPT_KEY.getBytes());
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			cipher.init(Cipher.DECRYPT_MODE, key);
			String result = new String(cipher.doFinal(hexStringToBytes(data)), "utf8");
			return result;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 返回可逆算法DES的密钥
	 * 
	 * @param key
	 *            前8字节将被用来生成密钥。
	 * @return 生成的密钥
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static Key getDESKey(byte[] key) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException
	{
		DESKeySpec des = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM_DES);
		return keyFactory.generateSecret(des);
	}
	/**
	 * Convert hex string to byte[]   把为字符串转化为字节数组
	 * @param hexString the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		char[] hexChars = hexString.toCharArray();
		int length = hexString.length();
		byte[] d = new byte[length >>> 1];
		for (int n = 0; n < length; n += 2) {
			String item = new String(hexChars, n, 2);
			// 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
			d[n >>> 1] = (byte) Integer.parseInt(item, 16);
		}
		return d;
	}
	/**
	 * 
	 * 将json数据转换成Map
	 *
	 * @author afi
	 *
	 * @param json	要转换的json数据
	 * @return	Map 转换后的结果
	 * @throws BusinessException	类型转换异常
	 */
	public static Map<?, ?> json2Map(final String json) throws BusinessException {
		if (json == null || json.length() == 0) {
			return null;
		}
//		final ObjectMapper mapper = new ObjectMapper();
		Map<?, ?> result = null;
		try {
			result = mapper.readValue(json, Map.class);
		} catch (final Exception e) {
			e.printStackTrace();
//			throw new BusinessException("Json转换成Map时出现异常。", e);
		}
		return result;
	}
	
	public static String encrypt(String data){
		try {
			
			key = getDESKey(PASSWORD_CRYPT_KEY.getBytes());
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return StringKit.bytesToHexString(cipher.doFinal(data.getBytes("utf8")));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
}
