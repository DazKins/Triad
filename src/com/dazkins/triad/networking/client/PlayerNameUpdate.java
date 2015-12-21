package com.dazkins.triad.networking.client;

public class PlayerNameUpdate
{
	private String name;
	private int gID;
	
	public PlayerNameUpdate(String n, int g)
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