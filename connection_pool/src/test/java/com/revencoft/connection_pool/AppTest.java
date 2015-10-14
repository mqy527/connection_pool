package com.revencoft.connection_pool;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;

import com.revencoft.connection_pool.connection.config.ConnectionPoolConfig;
import com.revencoft.connection_pool.connection.factory.ConnectionFactory;
import com.revencoft.connection_pool.connection.ftp.DefaultFtpConnection;


/**
 * Unit test for simple App.
 */
public class AppTest {
	
	@Test
	public void testFtpConn() throws Exception {
		ConnectionFactory factory = new ConnectionFactory(DefaultFtpConnection.class);
		PooledObject<Connection> pooledObject = factory.makeObject();
		Connection conn = pooledObject.getObject();
		System.out.println(conn);
	}
	
	@Test
	public void testPool() {
		ConnectionFactory factory = new ConnectionFactory(DefaultFtpConnection.class);
		ConnectionPoolConfig config = new ConnectionPoolConfig();
		config.setLifo(false);  
		config.setMaxTotal(5); 
		config.setMaxIdle(5);
		config.setMinIdle(1);  
		config.setMaxWaitMillis(15 * 1000);  

		ObjectPool<Connection> pool = new GenericObjectPool<Connection>(factory, config);  
		for (int i = 0; i < 10; i++) {  
			Thread thread = new Thread(new MyTask(pool));  
			thread.start();  
		}
//		try {
//			System.in.read();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		try {
			pool.borrowObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
