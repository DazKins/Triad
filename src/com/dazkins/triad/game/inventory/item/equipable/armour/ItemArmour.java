package com.dazkins.triad.game.inventory.item.equipable.armour;

import com.dazkins.triad.game.inventory.item.equipable.*;

public class ItemArmour extends ItemEquipable
{
	protected int armour;

	public ItemArmour(int id, String name, int a)
	{
		super(id, name);
		armour = a;
	}
}