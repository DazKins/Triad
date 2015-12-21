package com.dazkins.triad.networking.packet;

public class Packet009EntityRemoved extends Packet
{
	private int gID;
	
	public void setGID(int v)
	{
		gID = v;
	}
	
	public int getGID()
	{
		return gID;
	}
}