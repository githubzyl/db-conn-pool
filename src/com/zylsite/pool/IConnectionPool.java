package com.zylsite.pool;

public interface IConnectionPool {

	public PooledConnection getConnection();
	
	public void createConnection(int count);
	
}
