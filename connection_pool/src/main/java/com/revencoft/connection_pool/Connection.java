/**
 * 
 */
package com.revencoft.connection_pool;

import java.io.IOException;

/**
 * @author mengqingyan
 * @version 
 */
public interface Connection {
	
	String PONG = "PONG";

	
	public boolean connect() throws IOException;
	
	public void disConnect() throws IOException;
	
	public boolean isConnected();
	
	public String ping();
	
	public void resetState();
}
