package com.dazkins.triad.networking.server.update;

import com.dazkins.triad.game.entity.mob.EntityPlayer;

public class ServerUpdateAbilityUse extends ServerUpdate
{
	private int abilityNo;
	private EntityPlayer player;
	
	public ServerUpdateAbilityUse(int a, EntityPlayer p)
	{
		abilityNo = a;
		player = p;
	}
	
	public EntityPlayer getPlayer()
	{
		return player;
	}
	
	public int getAbiityNo()
	{
		return abilityNo;
	}
}