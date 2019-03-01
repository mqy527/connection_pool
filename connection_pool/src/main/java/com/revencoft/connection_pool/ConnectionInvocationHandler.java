package com.revencoft.connection_pool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author mengqingyan
 *
 */
public class ConnectionInvocationHandler implements InvocationHandler {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final Pool<Connection> connectionPool;
	
	
	public ConnectionInvocationHandler(Pool<Connection> connectionPool) {
		super();
		this.connectionPool = connectionPool;
	}


	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		Object result = null;
		Connection delegate = null;
		boolean ex = false;
		try {
			delegate = getConnection();
			logger.debug("using delegate connection: {}", delegate);
			result = method.invoke(delegate, args);
		} catch (Exception e) {
			ex = true;
			this.connectionPool.returnBrokenResource(delegate);
			throw e;
		} finally {
			if(!ex) {
				this.connectionPool.returnResource(delegate);
			}
		}
		return result;
	}
	
	private Connection getConnection() {
		return this.connectionPool.getResource();
	}

}
