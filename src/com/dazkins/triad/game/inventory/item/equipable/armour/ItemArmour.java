package com.dazkins.triad.game.inventory.item.equipable.armour;

import com.dazkins.triad.game.inventory.item.equipable.*;

public class ItemArmour extends ItemEquipable {
	protected int armour;
	
	public ItemArmour(String name, int a) {
		super(name);
		armour = a;
	}
}