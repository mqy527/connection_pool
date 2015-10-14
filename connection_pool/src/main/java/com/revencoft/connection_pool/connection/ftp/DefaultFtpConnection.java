/**
 * 
 */
package com.revencoft.connection_pool.connection.ftp;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * @author mengqingyan
 * @version 
 */
public class DefaultFtpConnection implements FtpConnection<String, String> {

	private static final Logger log = Logger.getLogger(DefaultFtpConnection.class);
	
	private boolean connected = false;
	
	public boolean connect() {
		log.info("connect");
		connected = true;
		return true;
	}

	public void disConnect() {
		log.info("disConnect");
		connected = false;
	}

	public boolean isConnected() {
		log.info("isConnected");
		return connected;
	}

	public String download(String remoteFilePath, String localFilePath)
			throws IOException {
		log.info("download");
		return "SUCCESS";
	}

	public String uploadFile(String remoteFile, File localFile, long remoteSize)
			throws IOException {
		log.info("uploadFile");
		return "SUCCESS";
	}

	public String upload(String localFilePath, String remoteFilePath)
			throws IOException {
		log.info("upload");
		return "SUCCESS";
	}

	public String ping() {
		return PONG;
	}

	public void resetState() {
		log.info("resetState");
	}
}
