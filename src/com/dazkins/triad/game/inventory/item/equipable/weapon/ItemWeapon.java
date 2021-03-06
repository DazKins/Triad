package com.dazkins.triad.game.inventory.item.equipable.weapon;

import com.dazkins.triad.game.inventory.item.equipable.ItemEquipable;

public abstract class ItemWeapon extends ItemEquipable
{
	public ItemWeapon(int id, String name)
	{
		super(id, name);
	}

	public int getDamage()
	{
		return 0;
	}

	public int getKnockback()
	{
		return 0;
	}

	public int getAttackCooldown()
	{
		return 0;
	}

	public int getAttackRange()
	{
		return 0;
	}
}