package com.dazkins.triad.networking.client.update;

public class ClientUpdatePlayerName
{
	private String name;
	private int gID;
	
	public ClientUpdatePlayerName(String n, int g)
	{
		name = n;
		gID = g;
	}
	
	public int getGID()
	{
		return gID;
	}
	
	public String getName()
	{
		return name;
	}
}