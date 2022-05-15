package com.tuanche.web.util.httpclient;

import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.UnknownHostException;


public class HttpProtocolHandler {

    /*private static String DEFAULT_CHARSET = "GBK";

    /** 连接超时时间，由bean factory设置，缺省为8秒钟 */
    private int    defaultConnectionTimeout = 8000;

    /** 回应超时时间, 由bean factory设置，缺省为30秒钟 */
    private int defaultSoTimeout= 30000;

    /** 闲置连接超时时间, 由bean factory设置，缺省为60秒钟 */
    private int defaultIdleConnTimeout = 60000;

    private int defaultMaxConnPerHost = 30;

    private int defaultMaxTotalConn = 80;

    /** 默认等待HttpConnectionManager返回连接超时（只有在达到最大连接数时起作用）：1秒*/
    private static final long defaultHttpConnectionManagerTimeout = 3 * 1000;

    /**
     * HTTP连接管理器，该连接管理器必须是线程安全的.
     */
    private ClientConnectionManager        connectionManager;

    private static HttpProtocolHandler httpProtocolHandler    = new HttpProtocolHandler();

    /**
     * 工厂方法
     * 
     * @return
     */
    public static HttpProtocolHandler getInstance() {
        return httpProtocolHandler;
    }

    /**
     * 私有的构造方法
     */
    private HttpProtocolHandler() {
    	/*SchemeRegistry schemeRegistry = new SchemeRegistry();  
    	schemeRegistry.register(  
    	         new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));  
    	schemeRegistry.register(  
    	         new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));    
        // 创建一个线程安全的HTTP连接池
        connectionManager = new ThreadSafeClientConnManager(schemeRegistry);  
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(defaultMaxConnPerHost);
        connectionManager.getParams().setMaxTotalConnections(defaultMaxTotalConn);

        IdleConnectionTimeoutThread ict = new IdleConnectionTimeoutThread();
        ict.addConnectionManager(connectionManager);
        ict.setConnectionTimeout(defaultIdleConnTimeout);

        ict.start();*/
    }

    /**
     * 执行Http请求
     * 
     * @param request 请求数据
     * @param strParaFileName 文件类型的参数名
     * @param strFilePath 文件路径
     * @return 
     * @throws HttpException, IOException 
     */
    public HttpResponse execute(final HttpRequest request) throws HttpException, IOException {
    	HttpClient httpclient = new DefaultHttpClient();
    	/*HttpClient httpclient = new HttpClient(connectionManager);
        // 设置连接超时
        int connectionTimeout = defaultConnectionTimeout;
        if (request.getConnectionTimeout() > 0) {
            connectionTimeout = request.getConnectionTimeout();
        }
        httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTimeout);

        // 设置回应超时
        int soTimeout = defaultSoTimeout;
        if (request.getTimeout() > 0) {
            soTimeout = request.getTimeout();
        }
        httpclient.getHttpConnectionManager().getParams().setSoTimeout(soTimeout);

        // 设置等待ConnectionManager释放connection的时间
        httpclient.getParams().setConnectionManagerTimeout(defaultHttpConnectionManagerTimeout);*/

        String charset = request.getCharset();
        HttpPost method = null;
    	//post模式且不带上传文件
    	method = new HttpPost(request.getUrl());  
    	StringEntity entity = new StringEntity(request.getQueryString(),"utf-8");//解决中文乱码问题    
        entity.setContentEncoding("UTF-8");    
        entity.setContentType("application/json");    
        method.setEntity(entity);  
        // 设置Http Header中的User-Agent属性
        method.addHeader("User-Agent", "Mozilla/4.0");
        HttpResponse response = new HttpResponse();
        org.apache.http.HttpResponse res = null;
        try {
        	res =  httpclient.execute(method);
            response.setStringResult(EntityUtils.toString(res.getEntity(), "UTF-8"));
        } catch (UnknownHostException ex) {
        	ex.printStackTrace();
            return null;
        } catch (IOException ex) {
        	ex.printStackTrace();
            return null;
        } catch (Exception ex) {
        	ex.printStackTrace();
            return null;
        } finally {
            method.releaseConnection();
        }
        return response;
    }

    /**
     * 将NameValuePairs数组转变为字符串
     * 
     * @param nameValues
     * @return
     */
    protected String toString(NameValuePair[] nameValues) {
        if (nameValues == null || nameValues.length == 0) {
            return "null";
        }

        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < nameValues.length; i++) {
            NameValuePair nameValue = nameValues[i];

            if (i == 0) {
                buffer.append(nameValue.getName() + "=" + nameValue.getValue());
            } else {
                buffer.append("&" + nameValue.getName() + "=" + nameValue.getValue());
            }
        }

        return buffer.toString();
    }
}
