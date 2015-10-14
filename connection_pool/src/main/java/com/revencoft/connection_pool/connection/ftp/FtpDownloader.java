/**
 * 
 */
package com.revencoft.connection_pool.connection.ftp;

import java.io.IOException;


/**
 * @author mengqingyan
 * @version
 * @param <D> 下载状态返回值 
 */
public interface FtpDownloader<D> {
	
	public D download(String remoteFilePath, String localFilePath)  
			  throws IOException;
	
}
