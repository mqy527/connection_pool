/**
 * 
 */
package com.revencoft.connection_pool;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.revencoft.connection_pool.connection.config.ConnectionPoolConfig;
import com.revencoft.connection_pool.connection.factory.ConnectionFactory;

/**
 * @author mengqingyan
 * @version
 */
public class ConnectionFactoryBean extends Pool<Connection> implements
		FactoryBean<Connection>, InitializingBean, DisposableBean {

	private ConnectionPoolConfig poolConfig;
	private Class<?> connClazz;
	
	

	@Override
	public void returnResource(Connection resource) {
		if(resource != null) {
			resource.resetState();
			super.returnResource(resource);
		}
	}

	public Connection getObject() throws Exception {
		return (Connection) Proxy.newProxyInstance(this.getClass().getClassLoader(), connClazz.getInterfaces(),
				new ConnectionInvocationHandler(this));
	}
	

	public Class<?> getObjectType() {
		return connClazz;
	}

	public boolean isSingleton() {
		return true;
	}

	public void afterPropertiesSet() throws Exception {
		super.initPool(poolConfig, new ConnectionFactory(this.connClazz));
		
	}

	public void setPoolConfig(ConnectionPoolConfig poolConfig) {
		this.poolConfig = poolConfig;
	}

	public void setConnClazz(String connClazz) throws ClassNotFoundException {
		this.connClazz = Class.forName(connClazz);
		Assert.isAssignable(Connection.class, this.connClazz);
	}

}
