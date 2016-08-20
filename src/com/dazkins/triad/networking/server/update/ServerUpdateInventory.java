package com.dazkins.triad.networking.server.update;

import com.dazkins.triad.game.entity.mob.EntityPlayer;
import com.dazkins.triad.game.inventory.Inventory;

public class ServerUpdateInventory extends ServerUpdate
{
	private Inventory inv;
	private EntityPlayer player;
	
	public ServerUpdateInventory(Inventory i, EntityPlayer p)
	{
		this.inv = i;
		this.player = p;
	}
	
	public Inventory getInventory()
	{
		return this.inv;
	}
	
	public EntityPlayer getPlayer()
	{
		return this.player;
	}
}