package com.dazkins.triad.game.inventory.item.equipable.weapon.harvestTool;

import com.dazkins.triad.game.inventory.item.equipable.weapon.ItemWeapon;

public class ItemAxe extends ItemHarvestTool
{
	public ItemAxe(int id)
	{
		super(id, "axe");
	}

	public int getDamage()
	{
		return 2;
	}

	public int getKnockback()
	{
		return 7;
	}

	public int getAttackCooldown()
	{
		return 23;
	}

	public int getAttackRange()
	{
		return 50;
	}

	public int getHarvestDamage()
	{
		return 5;
	}
}