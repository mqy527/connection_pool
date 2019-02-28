package com.revencoft.connection_pool.connection.sftp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.jcraft.jsch.*;
import com.revencoft.connection_pool.Connection;
import com.revencoft.connection_pool.connection.config.ConnectionConfig;
import com.revencoft.connection_pool.connection.config.FtpConnectionConfig;

/**
 * @author mengqingyan 2019/2/28
 */
public class JschSftpConnection implements SftpConnection {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private ChannelSftp sftp;

    private Session     session;

    /** SFTP 登录用户名 */
    private String      username;

    /** SFTP 登录密码 */
    private String      password;

    /** 私钥 */
    private String      privateKey;

    /** SFTP 服务器地址IP地址 */
    private String      host;

    /** SFTP 端口 */
    private int         port;

    private String sftpRootDir = "/";

    public void config(ConnectionConfig connectionConfig) {
        FtpConnectionConfig ftpConnectionConfig = (FtpConnectionConfig) connectionConfig;
        this.username = ftpConnectionConfig.getUsername();
        this.password = ftpConnectionConfig.getPassword();
        this.privateKey = ftpConnectionConfig.getPrivateKey();
        this.host = ftpConnectionConfig.getHost();
        this.port = ftpConnectionConfig.getPort();
        this.sftpRootDir =ftpConnectionConfig.getRootDir();
    }


    public byte[] getDownFileStream(String relativePath, String fileName) throws Exception {
        sftp.cd(getFullPath(relativePath));
        InputStream in = sftp.get(fileName);
        ByteArrayOutputStream output;
        try {
            output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int n = 0;
            while (-1 != (n = in.read(buffer))) {
                output.write(buffer, 0, n);
            }
        } finally {
            in.close();
        }

        return output.toByteArray();
    }

    public SftpATTRS getFileInfo(String relativePath, String fileName) throws Exception {
        sftp.cd(getFullPath(relativePath));
        SftpATTRS lstat = sftp.lstat(fileName);
        return lstat;
    }

    public List<ChannelSftp.LsEntry> getFileDirInfo(String relativePath) throws Exception {
        String remoteFtpDir = getFullPath(relativePath);
        sftp.cd(remoteFtpDir);
        Vector<ChannelSftp.LsEntry> fileDirInfo = sftp.ls(remoteFtpDir);
        if(CollectionUtils.isEmpty(fileDirInfo)) {
            return fileDirInfo;
        }

        List<ChannelSftp.LsEntry> lsEntries = new ArrayList<ChannelSftp.LsEntry>(fileDirInfo.size());
        for (ChannelSftp.LsEntry lsEntry : fileDirInfo) {
            String filename = lsEntry.getFilename();
            if(StringUtils.equals(filename, ".") || StringUtils.equals(filename, "..")) {
                continue;
            }
            lsEntries.add(lsEntry);
        }

        return lsEntries;
    }

    public boolean uploadFileStream(String relativePath, String fileName, byte[] content) throws Exception {
        cd(getFullPath(relativePath), sftp);
        InputStream in = new ByteArrayInputStream(content);
        try {
            sftp.put(in, fileName);
        } finally {
            in.close();
        }
        return true;
    }

    public boolean isFileExists(String relativePath, String fileName) {
        try {
            SftpATTRS sftpATTRS = getFileInfo(getFullPath(relativePath), fileName);
            if(sftpATTRS != null) {
                return true;
            }
            return false;
        } catch (Exception e) {
            String message = e.getMessage();
            if(StringUtils.equals(message, "No such file")) {
                return false;
            }
            logger.error("SFTP_GET_FILE_INFO_ERROR", e);
            throw new RuntimeException(e);
        }
    }

    public boolean connect() throws IOException {
        try {
            JSch jsch = new JSch();
            if (privateKey != null) {
                jsch.addIdentity(privateKey);// 设置私钥
            }

            session = jsch.getSession(username, host, port);

            if (password != null) {
                session.setPassword(password);
            }
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();

            sftp = (ChannelSftp) channel;
        } catch (JSchException e) {
            logger.error("connect error:", e);
            return false;
        }

        return true;
    }

    public void disConnect() throws IOException {
        if (sftp != null && sftp.isConnected()) {
            sftp.disconnect();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }

    public boolean isConnected() {
        return session.isConnected() && sftp.isConnected();
    }

    public String ping() {
        try {
            this.sftp.stat(this.sftpRootDir);
        } catch (SftpException e) {
            logger.error("ping error: ", e);
            return "ERROR";
        }
        return Connection.PONG;
    }

    public void resetState() {
    }
    private String getFullPath(String relativePath) {
        if(StringUtils.startsWith(relativePath, "/")) {
            return relativePath;
        }

        StringBuffer fullPath = new StringBuffer(this.sftpRootDir);
        String pathArry[] = relativePath.split("/");
        for (String childPath : pathArry) {
            if (childPath.equals("")) {
                continue;
            }
            fullPath.append(childPath + "/");
        }
        return fullPath.toString();
    }
    /**
     * 切换至目标目录 若目录不存在，则先创建目标目录
     * @throws SftpException
     */
    private void cd(String path, ChannelSftp sftp) throws SftpException {
        if (!isDirExist(path, sftp)) {
            String pathArry[] = path.split("/");
            StringBuffer filePath = new StringBuffer("/");
            for (String childPath : pathArry) {
                if (childPath.equals("")) {
                    continue;
                }
                filePath.append(childPath + "/");
                if (isDirExist(filePath.toString(), sftp)) {
                    sftp.cd(filePath.toString());
                } else {
                    // 建立目录
                    sftp.mkdir(filePath.toString());
                    // 进入并设置为当前目录
                    sftp.cd(filePath.toString());
                }
            }
        } else {
            sftp.cd(path);
        }
    }

    /**
     * 判断目录是否存在
     */
    private boolean isDirExist(String directory, ChannelSftp sftp) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(directory);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such directory")) {
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }

}


