package com.dazkins.triad.game.inventory.item;

public class ItemWeapon extends Item {
	public static String itemTypeIdentifier = "Weapon";
	
	private int damage;
	
	public ItemWeapon(int tx, int ty, String name, int damage) {
		super(tx, ty, name);
		this.damage = damage;
	}
}