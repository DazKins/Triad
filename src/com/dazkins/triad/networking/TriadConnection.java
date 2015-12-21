package com.dazkins.triad.networking;

import com.dazkins.triad.networking.packet.Packet;
import com.dazkins.triad.networking.server.CameraState;
import com.dazkins.triad.networking.server.TriadServer;
import com.esotericsoftware.kryonet.Connection;

public class TriadConnection
{
	private String username;
	private Connection connection;
	private TriadServer server;
	
	private CameraState camState;
	private long camReceiveTime = System.currentTimeMillis() - CAM_OUTDATED_CUTOFF;

	public TriadConnection(TriadServer s, Connection c, String n)
	{
		connection = c;
		server = s;
		username = n;
	}
	
	public void handleCameraUpdate(CameraState cam)
	{
		camState = cam;
		camReceiveTime = System.currentTimeMillis();
	}
	
	private static final int CAM_OUTDATED_CUTOFF = 5000;
	
	public boolean isCamStateOutdated()
	{
		long dt = System.currentTimeMillis() - camReceiveTime;
		return dt >= CAM_OUTDATED_CUTOFF;
	}
	
	public CameraState getCamState()
	{
		return camState;
	}

	public void sendPacket(Packet p, boolean priority)
	{
		server.addPacketToSendQueue(p, connection, priority);
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