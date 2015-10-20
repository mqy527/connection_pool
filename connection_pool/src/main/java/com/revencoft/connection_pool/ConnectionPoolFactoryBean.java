/**
 * 
 */
package com.revencoft.connection_pool;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.revencoft.connection_pool.connection.config.ConnectionPoolConfig;
import com.revencoft.connection_pool.connection.factory.ConnectionFactory;

/**
 * @author mengqingyan
 * @version
 */
public class ConnectionPoolFactoryBean extends Pool<Connection> implements
		FactoryBean<Pool<Connection>>, InitializingBean, DisposableBean {

	private ConnectionPoolConfig poolConfig;
	private String connClazz;
	
	

	@Override
	public void returnResource(Connection resource) {
		if(resource != null) {
			resource.resetState();
			super.returnResource(resource);
		}
	}

	public Pool<Connection> getObject() throws Exception {
		return this;
	}

	public Class<?> getObjectType() {
		return Pool.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void afterPropertiesSet() throws Exception {
		
		Class<?> clazz = Class.forName(connClazz);
		super.initPool(poolConfig, new ConnectionFactory(clazz));
		
	}

	public void setPoolConfig(ConnectionPoolConfig poolConfig) {
		this.poolConfig = poolConfig;
	}

	public void setConnClazz(String connClazz) {
		this.connClazz = connClazz;
	}

}
