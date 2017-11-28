package com.zylsite.pool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestPool {

	public static void main(String[] args) {
		for (int i = 5000; i > 0; i--) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					query();
				}
			}, "Thread-"+i).start();
		}
	}

	private static synchronized void query() {
		ConnectionPoolImpl pool = PoolManager.getInstance();
		PooledConnection pooledConnection = pool.getConnection();
		Connection connection = pooledConnection.getConnection();
		ResultSet rs = null;
		try {
			Statement statement = connection.createStatement();
			// 要执行的SQL语句
			String sql = "select * from t_girl";
			// 3.ResultSet类，用来存放获取的结果集！！
			rs = statement.executeQuery(sql);
			System.out.println("---------" + Thread.currentThread().getName() + "--------");
			System.out.println("ID" + "\t\t" + "年龄" + "\t\t" + "大小");

			int id = 0;
			int age = 0;
			String cup_size = null;
			while (rs.next()) {
				id = rs.getInt("id");
				age = rs.getInt("age");
				cup_size = rs.getString("cup_size");
				// 输出结果
				System.out.println(id + "\t\t" + age + "\t\t" + cup_size);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != rs) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (null != pooledConnection) {
				pooledConnection.releaseConnection();
			}
		}
	}

}
