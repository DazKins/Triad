package com.dazkins.triad.networking.client.update;

public class ClientUpdateInteraction
{
	private int eID;
	private int iID;
	
	private boolean start;
	
	public ClientUpdateInteraction(int e, int i, boolean s)
	{
		eID = e;
		iID = i;
		start = s;
	}
	
	public int getEntityID()
	{
		return eID;
	}
	
	public int getInteractingID()
	{
		return iID;
	}
	
	public boolean isStart()
	{
		return start;
	}
}