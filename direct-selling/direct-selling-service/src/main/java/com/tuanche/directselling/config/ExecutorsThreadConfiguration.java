package com.tuanche.directselling.config;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
  * @description 线程池 bean 的管理，配置文件走apollo配置中心，并且给定默认值
  * @author ants·ht
  * @date 2018/8/13 14:02
*/
@Configuration
public class ExecutorsThreadConfiguration {

	/**
	 * 线程池中核心线程的数量 -- 根据服务器内核可做相应调整
	 */
	private final int THREAD_POOL_CORE_SIZE = 8;

	/**
	 * 线程池中允许的最大线程数 -- 一般可最大取值到核心线程池的数量
	 */
	private final int THREAD_POOL_MAXIMUM_SIZE = 8;

	/**
	 * 线程最大等待时间
	 * 当线程数大于核心线程数时，在等待新任务的最大等待时间 -- 默认是0
	 */
	private final Long THREAD_POOL_KEEPALIVE_TIME = 0L;

	@ApolloConfig("application-common.properties")
	Config config;

	@Bean("executorService")
	public ExecutorService registerExecutors() {
		return new ThreadPoolExecutor( null == config ? THREAD_POOL_CORE_SIZE : config.getIntProperty("thread.pool.core.size",THREAD_POOL_CORE_SIZE),
				null == config ? THREAD_POOL_MAXIMUM_SIZE : config.getIntProperty("thread.pool.maximum.size",THREAD_POOL_MAXIMUM_SIZE),
				null == config ? THREAD_POOL_KEEPALIVE_TIME : config.getLongProperty("thread.pool.keepalive.time",THREAD_POOL_KEEPALIVE_TIME),
				TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<>(null == config ? Integer.MAX_VALUE : config.getIntProperty("thread.pool.queue.size",Integer.MAX_VALUE)));
	}
}
