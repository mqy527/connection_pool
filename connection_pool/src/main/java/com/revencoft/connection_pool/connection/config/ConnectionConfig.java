package com.revencoft.connection_pool.connection.config;

/**
 * @author mengqingyan 2019/2/28
 */
public interface ConnectionConfig {
    String getUsername();

    String getPassword();

    String getHost();

    int getPort();
}
