/**
 * 
 */
package com.revencoft.connection_pool.connection.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author mengqingyan
 * @version 
 */
public class ConnectionPoolConfig extends GenericObjectPoolConfig {

	public ConnectionPoolConfig() {
		setTestWhileIdle(true);
//		setMinEvictableIdleTimeMillis(60L * 1000L *60L);	//1小时，默认半小时
		setTimeBetweenEvictionRunsMillis(10L* 60L * 1000L); //10min
		setNumTestsPerEvictionRun(-1);
	}
}
