package com.dazkins.triad.networking.client;

import com.dazkins.triad.game.inventory.item.Item;
import com.dazkins.triad.game.inventory.item.ItemStack;

public class InventoryUpdate
{
	private int entityID;
	
	private int width;
	private int height;
	
	private ItemStack[] items;
	
	public InventoryUpdate(int eID, int w, int h)
	{
		this.entityID = eID;
		this.width = w;
		this.height = h;
		items = new ItemStack[width * height];
	}
	
	public void setItemData(int itemID, int stackCount, int ind)
	{
		items[ind] = new ItemStack(Item.items[itemID], stackCount);
	}
	
	public ItemStack[] getItems()
	{
		return items;
	}
	
	public int getEntityID()
	{
		return entityID;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}
}