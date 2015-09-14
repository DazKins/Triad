package com.dazkins.triad.networking;

import com.esotericsoftware.kryonet.Connection;

public class TriadConnection {
	private String username;
	private Connection connection;
	
	public TriadConnection(Connection c, String s) {
		connection = c;
		username = s;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getIP() {
		return connection.getRemoteAddressTCP().getHostName();
	}
}