package com.sogou.bizdev.compass.delayfree.cache.memcache;

import java.util.Arrays;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.sogou.bizdev.compass.delayfree.cache.MyCache;
import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

/**
 * 
 * @author wl
 * 
 */
public class MyMemCache implements MyCache, InitializingBean {
	private static final Logger logger = LoggerFactory
			.getLogger(MyMemCache.class);

	private String poolName = "poolName";
	private String[] hostAndPort;// ip和端口
	private Integer[] hostWeight;// 每个server的权重
	private Integer initConn; // 初始化连接数
	private Integer minConn;// 最少保持连接数
	private Integer maxConn;// 最大连接数
	private Integer maxIdleTime;// 连接最大空闲时间
	private Integer maintSleepTime;// 连接池维护线程睡眠时间,每maintSleepTime醒来一次做一次连接池维护
	private Integer socketReadTimeOut;// socket读超时时间
	private Integer socketConnectTimeOut;// socket连接超时时间
	private boolean socketNagle;// 是否使用SOCKET的nagle算法(小包拼揍成大包发送)
	private boolean failover;// 是否自动跳过故障节点
	private boolean failback;// 故障节点恢复是否重新使用
	private boolean primitiveAsString;// 原始类型数据当string处理


	private MemCachedClient memCachedClient;

	private void init() {
		SockIOPool pool = SockIOPool.getInstance(poolName);
		pool.setServers(hostAndPort);
		pool.setWeights(hostWeight);
		pool.setInitConn(initConn);
		pool.setMinConn(minConn);
		pool.setMaxConn(maxConn);
		pool.setMaxIdle(maxIdleTime);
		pool.setMaintSleep(maintSleepTime);
		pool.setNagle(socketNagle);
		pool.setSocketTO(socketReadTimeOut);
		pool.setSocketConnectTO(socketConnectTimeOut);
		
		pool.setHashingAlg(SockIOPool.NATIVE_HASH);
		pool.setFailover(failover);
		pool.setFailback(failback);

		pool.initialize();
		memCachedClient = new MemCachedClient(poolName);
		memCachedClient.setPrimitiveAsString(primitiveAsString);
	}

	@Override
	public void put(String key, Object o) {
		memCachedClient.set(key, o);
	}

	@Override
	public void put(String key, Object o, Integer timeToLiveSeconds) {

		if (null == timeToLiveSeconds) {
			throw new IllegalArgumentException("invalid timeToLiveSeconds");
		}
		memCachedClient.set(key, o, new Date(1000L * timeToLiveSeconds));
	}

	@Override
	public Object get(String key) {
		return memCachedClient.get(key);
	}

	@Override
	public boolean delete(String key) {
		return memCachedClient.delete(key);
	}
	
	
	@Override
	public void afterPropertiesSet() throws Exception {

		 if (getHostAndPort() == null || getHostAndPort().length < 1) {
			 throw new IllegalArgumentException("memcache.host_port required.");
		 }
		 logger.info("load memcache.host_port=" + Arrays.asList(getHostAndPort()));
		
		 if(getHostWeight() == null || getHostWeight().length != getHostAndPort().length) {
			 throw new IllegalArgumentException("memcache.host_weight must match host_port.");
		 }
		 logger.info("load memcache.host_weight=" + Arrays.asList(getHostWeight()));
		 
		 if(getInitConn() == null || getInitConn() < 1) {
			 throw new IllegalArgumentException("memcache.init_Conn not set or invalid.");
		 }
		 logger.info("load memcache.init_connection=" + getInitConn());
		 
		 
		 if(getMinConn() == null || getMinConn() < 1) {
			 throw new IllegalArgumentException("memcache.min_connection not set or invalid.");
		 }
		 logger.info("load memcache.min_connection=" + getMinConn());
		 
		 
		 if(getMaxConn() == null || getMaxConn() < 1) {
			 throw new IllegalArgumentException("memcache.max_connection not set or invalid.");
		 }
		 logger.info("load memcache.max_connection=" + getMaxConn());
		 
		 if(getMaxIdleTime() == null || getMaxIdleTime() < 1) {
			 throw new IllegalArgumentException("memcache.max_idle_time not set or invalid.");
		 }
		 logger.info("load memcache.max_idle_time=" + getMaxIdleTime());
		 
		 if(getMaintSleepTime() == null || getMaintSleepTime() < 1) {
			 throw new IllegalArgumentException("memcache.maint_sleep_time not set or invalid.");
		 }
		 logger.info("load memcache.maint_sleep_time=" + getMaintSleepTime());
		 
		 if(getSocketReadTimeOut() == null || getSocketReadTimeOut() < 1) {
			 throw new IllegalArgumentException("memcache.socket_read_timeout not set or invalid.");
		 }
		 logger.info("load memcache.socket_read_timeout=" + getSocketReadTimeOut());

		 if(getSocketConnectTimeOut() == null || getSocketConnectTimeOut() < 1) {
			 throw new IllegalArgumentException("memcache.socket_connect_timeout not set or invalid.");
		 }
		 logger.info("load memcache.socket_connect_timeout=" + getSocketConnectTimeOut());
		 logger.info("load memcache.socket_nagle=" + isSocketNagle());
		 logger.info("load memcache.failover=" + isFailover());
		 logger.info("load memcache.failback=" + isFailback());
		 logger.info("load memcache.primitive_as_string=" + isPrimitiveAsString());
		
		 init();
		
	}
	
	
	public String getPoolName() {
		return poolName;
	}

	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}

	public String[] getHostAndPort() {
		return hostAndPort;
	}

	public void setHostAndPort(String[] hostAndPort) {
		this.hostAndPort = hostAndPort;
	}

	public Integer[] getHostWeight() {
		return hostWeight;
	}

	public void setHostWeight(Integer[] hostWeight) {
		this.hostWeight = hostWeight;
	}

	public Integer getInitConn() {
		return initConn;
	}

	public void setInitConn(Integer initConn) {
		this.initConn = initConn;
	}

	public Integer getMinConn() {
		return minConn;
	}

	public void setMinConn(Integer minConn) {
		this.minConn = minConn;
	}

	public Integer getMaxConn() {
		return maxConn;
	}

	public void setMaxConn(Integer maxConn) {
		this.maxConn = maxConn;
	}

	public Integer getMaxIdleTime() {
		return maxIdleTime;
	}

	public void setMaxIdleTime(Integer maxIdleTime) {
		this.maxIdleTime = maxIdleTime;
	}

	public Integer getMaintSleepTime() {
		return maintSleepTime;
	}

	public void setMaintSleepTime(Integer maintSleepTime) {
		this.maintSleepTime = maintSleepTime;
	}

	public Integer getSocketReadTimeOut() {
		return socketReadTimeOut;
	}

	public void setSocketReadTimeOut(Integer socketReadTimeOut) {
		this.socketReadTimeOut = socketReadTimeOut;
	}

	public Integer getSocketConnectTimeOut() {
		return socketConnectTimeOut;
	}

	public void setSocketConnectTimeOut(Integer socketConnectTimeOut) {
		this.socketConnectTimeOut = socketConnectTimeOut;
	}

	public boolean isSocketNagle() {
		return socketNagle;
	}

	public void setSocketNagle(boolean socketNagle) {
		this.socketNagle = socketNagle;
	}

	public boolean isFailover() {
		return failover;
	}

	public void setFailover(boolean failover) {
		this.failover = failover;
	}

	public boolean isFailback() {
		return failback;
	}

	public void setFailback(boolean failback) {
		this.failback = failback;
	}

	public boolean isPrimitiveAsString() {
		return primitiveAsString;
	}

	public void setPrimitiveAsString(boolean primitiveAsString) {
		this.primitiveAsString = primitiveAsString;
	}

	public MemCachedClient getMemCachedClient() {
		return memCachedClient;
	}

	public void setMemCachedClient(MemCachedClient memCachedClient) {
		this.memCachedClient = memCachedClient;
	}


}
