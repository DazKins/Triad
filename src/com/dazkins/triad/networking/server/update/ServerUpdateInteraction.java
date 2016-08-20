package com.dazkins.triad.networking.server.update;

import com.dazkins.triad.game.entity.mob.EntityPlayer;

public class ServerUpdateInteraction extends ServerUpdate
{
	private EntityPlayer player;
	private boolean status;
	
	public ServerUpdateInteraction(EntityPlayer p, boolean s)
	{
		this.player = p;
		this.status = s;
	}
	
	public EntityPlayer getPlayer()
	{
		return this.player;
	}
	
	public boolean getStatus()
	{
		return this.status;
	}
}