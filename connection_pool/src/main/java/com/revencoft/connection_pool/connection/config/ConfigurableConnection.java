package com.revencoft.connection_pool.connection.config;

import com.revencoft.connection_pool.Connection;

/**
 * @author mengqingyan 2019/2/28
 */
public interface ConfigurableConnection extends Connection {
    void config(ConnectionConfig connectionConfig);
}
