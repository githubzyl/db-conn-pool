package com.zylsite.pool;

import java.sql.Connection;

public class PooledConnection {

	private boolean isBusy;
	private Connection connection;

	public PooledConnection(boolean isBusy, Connection connection) {
		super();
		this.isBusy = isBusy;
		this.connection = connection;
	}
	
	public void releaseConnection(){
		this.isBusy = false;
	}

	public boolean isBusy() {
		return isBusy;
	}

	public void setBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}
