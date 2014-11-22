package com.dazkins.triad.game.inventory.item.equipable.weapon;

import com.dazkins.triad.game.inventory.item.equipable.ItemEquipable;

public abstract class ItemWeapon extends ItemEquipable {
	public ItemWeapon(String name) {
		super(name);
	}
	
	public int getDamage() {
		return 0;
	}
}