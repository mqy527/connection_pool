/**
 * 
 */
package com.revencoft.connection_pool.connection.ftp;

import com.revencoft.connection_pool.Connection;



/**
 * ftp连接、上传、下载
 * @author mengqingyan
 * @version 
 * @param <D>
 * @param <U>
 */
public interface FtpConnection<D, U> extends Connection, FtpDownloader<D>, FtpUploader<U> {

	 
}
