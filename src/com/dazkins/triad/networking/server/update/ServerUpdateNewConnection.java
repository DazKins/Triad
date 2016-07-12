package com.dazkins.triad.networking.server.update;

import com.dazkins.triad.networking.TriadConnection;
import com.esotericsoftware.kryonet.Connection;

public class ServerUpdateNewConnection extends ServerUpdate
{
	private String username;
	private Connection connection;
	
	public ServerUpdateNewConnection(String u, Connection tc)
	{
		this.username = u;
		this.connection = tc;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public Connection getConnection()
	{
		return connection;
	}
}