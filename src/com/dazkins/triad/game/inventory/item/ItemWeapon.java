package com.dazkins.triad.game.inventory.item;

public class ItemWeapon extends ItemEquipable {
	public static String itemTypeIdentifier = "Weapon";
	
	private int damage;
	
	public ItemWeapon(String name, int damage) {
		super(name);
		this.damage = damage;
	}
}