/**
 * 
 */
package com.revencoft.connection_pool.connection.factory;

import org.apache.commons.lang3.Validate;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revencoft.connection_pool.Connection;
import com.revencoft.connection_pool.connection.config.ConfigurableConnection;
import com.revencoft.connection_pool.connection.config.ConnectionConfig;

/**
 * @author mengqingyan
 * @version 
 */
public class ConnectionFactory implements PooledObjectFactory<Connection>{

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private Class<?> connClazz;
	private ConnectionConfig connectionConfig;
	
	/**
	 * @param connClazz
	 */
	public ConnectionFactory(Class<?> connClazz, ConnectionConfig connectionConfig) {
		super();
		Validate.isTrue(Connection.class.isAssignableFrom(connClazz),
				"传入的class必须是com.revencoft.connection_pool.Connection的子类");
		this.connClazz = connClazz;
		this.connectionConfig = connectionConfig;
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
		initConnection(connection);
		connection.connect();
		return new DefaultPooledObject<Connection>(connection);
	}

	private void initConnection(Connection connection) {
		if(connection instanceof ConfigurableConnection) {
			ConfigurableConnection cc = (ConfigurableConnection) connection;
			cc.config(connectionConfig);
		}
	}

	public void passivateObject(PooledObject<Connection> pooledConn) throws Exception {
	}

	public boolean validateObject(PooledObject<Connection> pooledConn) {
		final Connection conn = pooledConn.getObject();
		return conn.isConnected() && Connection.PONG.equals(conn.ping());
	}

}
