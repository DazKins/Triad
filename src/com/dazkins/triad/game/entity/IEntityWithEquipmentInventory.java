package com.dazkins.triad.game.entity;

import com.dazkins.triad.game.inventory.EquipmentInventory;

public interface IEntityWithEquipmentInventory
{
	public EquipmentInventory getEquipmentInventory();
	public void setEquipmentInventory(EquipmentInventory i);
}