package com.dazkins.triad.game.entity;

import com.dazkins.triad.game.inventory.Inventory;

public interface IEntityWithInventory
{
	public Inventory getInventory();
	public void setInventory(Inventory i);
}