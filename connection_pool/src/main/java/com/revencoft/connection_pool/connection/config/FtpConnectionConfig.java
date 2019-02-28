package com.revencoft.connection_pool.connection.config;

/**
 * @author mengqingyan 2019/2/28
 */
public class FtpConnectionConfig implements ConnectionConfig {

    /** 登录用户名 */
    private String username;

    /** 登录密码 */
    private String password;

    /** 私钥 */
    private String privateKey;

    /** 服务器地址IP地址 */
    private String host;

    /** 端口 */
    private int    port;

    private String rootDir = "/";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }
}
