package com.dazkins.triad.networking;

import com.dazkins.triad.networking.packet.Packet;
import com.dazkins.triad.networking.server.TriadServer;
import com.esotericsoftware.kryonet.Connection;

public class TriadConnection
{
	private String username;
	private Connection connection;
	private TriadServer server;

	public TriadConnection(TriadServer s, Connection c, String n)
	{
		connection = c;
		server = s;
		username = n;
	}

	public void sendPacket(Packet p)
	{
		server.addPacketToSendQueue(p, connection);
	}

	public String getUsername()
	{
		return username;
	}

	public String getIP()
	{
		return connection.getRemoteAddressTCP().getHostName();
	}
	
	public String toString()
	{
		return getIP();
	}
	
	public boolean isConnection(Connection c)
	{
		return c == connection;
	}
}