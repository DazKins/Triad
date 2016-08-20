package com.dazkins.triad.networking.client.update;

import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.item.Item;
import com.dazkins.triad.game.inventory.item.ItemStack;

public class ClientUpdateInventory extends ClientUpdate
{
	private int globalID;

	private Inventory inventory;
	
	public ClientUpdateInventory(int gID, Inventory i)
	{	
		this.globalID = gID;
		this.inventory = i;
	}
	
	public int getGlobalID()
	{
		return this.globalID;
	}
	
	public Inventory getInventory()
	{
		return this.inventory;
	}
}