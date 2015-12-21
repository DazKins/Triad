package com.dazkins.triad.networking.packet;

public class Packet010PlayerNameSet extends Packet
{
	private String name;
	private int gID;
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String s)
	{
		this.name = s;
	}
	
	public int getgID()
	{
		return gID;
	}
	
	public void setgID(int gID)
	{
		this.gID = gID;
	}
}