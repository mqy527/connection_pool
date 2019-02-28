package com.revencoft.connection_pool.connection.sftp;

import java.util.List;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpATTRS;
import com.revencoft.connection_pool.connection.config.ConfigurableConnection;

/**
 * @author mengqingyan 2019/2/28
 */
public interface SftpConnection extends ConfigurableConnection {

    /**
     * 下载
     * @param relativePath
     * @param fileName
     * @return
     * @throws Exception
     */
    byte[] getDownFileStream(String relativePath, String fileName) throws Exception;

    SftpATTRS getFileInfo(String relativePath, String fileName) throws Exception;

    List<ChannelSftp.LsEntry> getFileDirInfo(String relativePath) throws Exception;

    /**
     * 上传
     * @param relativePath
     * @param fileName
     * @param content
     * @return
     * @throws Exception
     */
    boolean uploadFileStream(String relativePath, String fileName, byte[] content) throws Exception;

    boolean isFileExists(String relativePath, String fileName);
}
