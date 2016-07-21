package com.revencoft.connection_pool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 
 * @author mengqingyan
 *
 */
public class ConnectionInvocationHandler implements InvocationHandler {

	private final Pool<Connection> connectionPool;
	
	
	public ConnectionInvocationHandler(Pool<Connection> connectionPool) {
		super();
		this.connectionPool = connectionPool;
	}


	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		Object result = null;
		Connection delegate = null;
		try {
			delegate = getConnection();
			result = method.invoke(delegate, args);
		} catch (Exception e) {
			this.connectionPool.returnBrokenResource(delegate);
			throw e;
		} finally {
			this.connectionPool.returnResource(delegate);
		}
		return result;
	}
	
	private Connection getConnection() {
		return this.connectionPool.getResource();
	}

}
