package com.dazkins.triad.game.inventory.item.equipable.weapon;

import com.dazkins.triad.game.inventory.item.equipable.ItemEquipable;

public abstract class ItemWeapon extends ItemEquipable {
	public static String itemTypeIdentifier = "Weapon";
	
	protected int damage;
	
	public ItemWeapon(String name, int damage) {
		super(name);
		this.damage = damage;
	}
}