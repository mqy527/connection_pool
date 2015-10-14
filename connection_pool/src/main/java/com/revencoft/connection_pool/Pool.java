/**
 * 
 */
package com.revencoft.connection_pool;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;

import com.revencoft.connection_pool.connection.config.ConnectionPoolConfig;
import com.revencoft.connection_pool.exception.ConnectionException;

/**
 * @author mengqingyan
 * @version
 * @param <T>
 */
public abstract class Pool<T> {

	protected GenericObjectPool<T> internalPool;

	public Pool() {
	}

	public Pool(final ConnectionPoolConfig poolConfig,
			PooledObjectFactory<T> factory) {
		initPool(poolConfig, factory);
	}

	public void initPool(final ConnectionPoolConfig poolConfig,
			PooledObjectFactory<T> factory) {

		if (this.internalPool != null) {
			try {
				closeInternalPool();
			} catch (Exception e) {
			}
		}

		this.internalPool = new GenericObjectPool<T>(factory, poolConfig);
	}

	public T getResource() {
		try {
			return internalPool.borrowObject();
		} catch (Exception e) {
			throw new ConnectionException(
					"Could not get a resource from the pool", e);
		}
	}

	public void returnResourceObject(final T resource) {
		if (resource == null) {
			return;
		}
		try {
			internalPool.returnObject(resource);
		} catch (Exception e) {
			throw new ConnectionException(
					"Could not return the resource to the pool", e);
		}
	}

	public void returnBrokenResource(final T resource) {
		if (resource != null) {
			returnBrokenResourceObject(resource);
		}
	}

	public void returnResource(final T resource) {
		if (resource != null) {
			returnResourceObject(resource);
		}
	}

	public void destroy() {
		closeInternalPool();
	}

	protected void returnBrokenResourceObject(final T resource) {
		try {
			internalPool.invalidateObject(resource);
		} catch (Exception e) {
			throw new ConnectionException(
					"Could not return the resource to the pool", e);
		}
	}

	protected void closeInternalPool() {
		try {
			internalPool.close();
		} catch (Exception e) {
			throw new ConnectionException("Could not destroy the pool", e);
		}
	}
}
