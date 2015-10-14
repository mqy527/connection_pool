/**
 * 
 */
package com.revencoft.connection_pool;

import org.apache.commons.pool2.ObjectPool;

import com.revencoft.connection_pool.connection.ftp.FtpConnection;

/**
 * @author mengqingyan
 * @version
 */
class MyTask implements Runnable {

	private ObjectPool<Connection> pool;

	public MyTask(ObjectPool<Connection> pool) {
		this.pool = pool;
	}

	@SuppressWarnings("unchecked")
	public void run() {
		FtpConnection<String, String> myConn = null;
		try {
			myConn = (FtpConnection<String, String>) pool.borrowObject();
			myConn.download(null, null);
			Thread.sleep(10L * 1000L);
		} catch (Exception e) {
			System.out.println("Cannot borrow connection from pool." + e);
		} finally {
			if (myConn != null) {
				try {
					pool.returnObject(myConn);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		}
	}

}
