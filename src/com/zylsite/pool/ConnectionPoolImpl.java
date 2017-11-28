package com.zylsite.pool;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConnectionPoolImpl implements IConnectionPool {

	private final static String URL = "jdbc:mysql://127.0.0.1:3306/wazddz?characterEncoding=UTF-8";
	private final static String USERNAME = "root";
	private final static String PASSWORD = "123456";
	private final static String DRIVER = "com.mysql.jdbc.Driver";

	private int initCount = 10;
	private int stepCount = 5;
	private int maxCount = 50;
	private static List<PooledConnection> connectionList = Collections.synchronizedList(new ArrayList<>());

	public ConnectionPoolImpl() {
		this.init();
	}

	private void init() {
		try {
			Driver driver = (Driver) Class.forName(DRIVER).newInstance();
			DriverManager.registerDriver(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.createConnection(initCount);
	}

	@Override
	public PooledConnection getConnection() {
		if (connectionList.size() <= 0) {
			this.createConnection(initCount);
		}
		PooledConnection pooledConnection = getRealConnection();
		while (null == pooledConnection) {
			this.createConnection(stepCount);
			pooledConnection = getRealConnection();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("当前连接池中的连接数为：" + connectionList.size());
		return pooledConnection;
	}

	@Override
	public void createConnection(int count) {
		if (maxCount > 0 && connectionList.size() + count <= maxCount) {
			for (int i = 0; i < count; i++) {
				try {
					Connection connection = createConnection();
					PooledConnection pooledConnection = new PooledConnection(false, connection);
					connectionList.add(pooledConnection);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.out.println("数据库连接池创建失败!");
		}
	}

	private synchronized PooledConnection getRealConnection() {
		for (PooledConnection pooledConnection : connectionList) {
			if (!pooledConnection.isBusy()) {
				Connection connection;
				try {
					connection = pooledConnection.getConnection();
					if (!connection.isValid(2000)) {
						connection = createConnection();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pooledConnection.setBusy(true);
				return pooledConnection;
			}
		}
		return null;
	}

	private Connection createConnection() throws SQLException {
		return DriverManager.getConnection(URL, USERNAME, PASSWORD);
	}

}
