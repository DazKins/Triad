package com.dazkins.triad.networking.client.update;

public class ClientUpdateEntityHealthUpdate extends ClientUpdate
{
	private int gID;
	private int health;
	private int maxHealth;
	
	public ClientUpdateEntityHealthUpdate(int id, int h, int mh)
	{
		this.gID = id;
		this.health = h;
		this.maxHealth = mh;
	}
	
	public int getGID()
	{
		return gID;
	}
	
	public int getHealth()
	{
		return health;
	}
	
	public int getMaxHealth()
	{
		return maxHealth;
	}
}