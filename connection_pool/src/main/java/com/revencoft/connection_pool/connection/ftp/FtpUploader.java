/**
 * 
 */
package com.revencoft.connection_pool.connection.ftp;

import java.io.File;
import java.io.IOException;

/**
 * @author mengqingyan
 * @version 
 * @param <U>上传状态返回值
 */
public interface FtpUploader<U> {

	
	public U uploadFile(String remoteFile, File localFile,  
		    long remoteSize) throws IOException;
	
	public U upload(String localFilePath, String remoteFilePath) throws IOException;
	
}
