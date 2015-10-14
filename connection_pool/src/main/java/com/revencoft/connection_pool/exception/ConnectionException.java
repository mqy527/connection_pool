/**
 * 
 */
package com.revencoft.connection_pool.exception;

/**
 * @author mengqingyan
 * @version 
 */
public class ConnectionException extends RuntimeException {

	/**
	 * @param string
	 * @param e
	 */
	public ConnectionException(String msg, Exception e) {
		super(msg, e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8566456581878814053L;

}
