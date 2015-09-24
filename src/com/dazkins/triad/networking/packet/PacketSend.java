package com.dazkins.triad.networking.packet;

import com.esotericsoftware.kryonet.Connection;

public class PacketSend
{
	private Packet packet;
	private Connection con;
	
	public PacketSend(Packet p, Connection c)
	{
		packet = p;
		con = c;
	}

	public Packet getPacket()
	{
		return packet;
	}

	public void setPacket(Packet packet)
	{
		this.packet = packet;
	}

	public Connection getCon()
	{
		return con;
	}

	public void setCon(Connection con)
	{
		this.con = con;
	}
}