/**
 * 
 */
package com.revencoft.connection_pool.connection.factory;

import org.apache.commons.lang3.Validate;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.log4j.Logger;

import com.revencoft.connection_pool.Connection;

/**
 * @author mengqingyan
 * @version 
 */
public class ConnectionFactory implements PooledObjectFactory<Connection>{

	private static final Logger log = Logger.getLogger(ConnectionFactory.class);
	
	private Class<?> connClazz;
	
	
	/**
	 * @param connClazz
	 */
	public ConnectionFactory(Class<?> connClazz) {
		super();
		Validate.isTrue(Connection.class.isAssignableFrom(connClazz),
				"传入的class必须是com.revencoft.connection_pool.Connection的子类");
		this.connClazz = connClazz;
	}


	public void activateObject(PooledObject<Connection> pooledConn) throws Exception {
		final Connection conn = pooledConn.getObject();
		if(!conn.isConnected()) {
			conn.connect();
		} else if(!Connection.PONG.equals(conn.ping())) {
			conn.disConnect();
			conn.connect();
		}
	}

	
	public void destroyObject(PooledObject<Connection> pooledConn) throws Exception {
		final Connection conn = pooledConn.getObject();
		if(conn.isConnected()) {
			conn.disConnect();
		}
		
	}

	public PooledObject<Connection> makeObject() throws Exception {
		Connection connection = (Connection) connClazz.newInstance();
		connection.connect();
		return new DefaultPooledObject<Connection>(connection);
	}

	public void passivateObject(PooledObject<Connection> pooledConn) throws Exception {
	}

	public boolean validateObject(PooledObject<Connection> pooledConn) {
		final Connection conn = pooledConn.getObject();
		return conn.isConnected() && Connection.PONG.equals(conn.ping());
	}

}
