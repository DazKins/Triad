package com.dazkins.triad.game.inventory.item.equipable.weapon.harvestTool;

import com.dazkins.triad.game.inventory.item.equipable.weapon.ItemWeapon;

public class ItemHarvestTool extends ItemWeapon
{
	public ItemHarvestTool(int id, String name)
	{
		super(id, name);
	}

	public int getHarvestDamage()
	{
		return 0;
	}
}