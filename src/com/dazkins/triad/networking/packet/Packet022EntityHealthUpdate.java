package com.dazkins.triad.networking.packet;

public class Packet022EntityHealthUpdate extends Packet
{
	private int gID;
	
	private int health;
	private int maxHealth;
	
	public int getgID()
	{
		return gID;
	}
	
	public void setgID(int gID)
	{
		this.gID = gID;
	}
	
	public int getHealth()
	{
		return health;
	}
	
	public void setHealth(int health)
	{
		this.health = health;
	}

	public int getMaxHealth()
	{
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth)
	{
		this.maxHealth = maxHealth;
	}
}