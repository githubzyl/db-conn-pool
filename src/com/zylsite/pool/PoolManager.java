package com.zylsite.pool;

public class PoolManager {

	private static class pool {
		private static ConnectionPoolImpl instance = new ConnectionPoolImpl();
	}

	public static ConnectionPoolImpl getInstance() {
		return pool.instance;
	}

}
