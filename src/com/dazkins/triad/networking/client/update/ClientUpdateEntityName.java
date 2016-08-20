package com.dazkins.triad.networking.client.update;

public class ClientUpdateEntityName extends ClientUpdate
{
	private String name;
	private int gID;
	
	public ClientUpdateEntityName(String n, int g)
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