package com.dazkins.triad.networking.client.update;

public class ClientUpdateCooldown extends ClientUpdate
{
	private int gID;
	private int abilityNo;
	private int cooldown;
	
	public ClientUpdateCooldown(int id, int an, int cd)
	{
		this.gID = id;
		this.abilityNo = an;
		this.cooldown = cd;
	}

	public int getgID()
	{
		return gID;
	}

	public int getAbilityNo()
	{
		return abilityNo;
	}

	public int getCooldown()
	{
		return cooldown;
	}
}