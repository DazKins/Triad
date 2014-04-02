package com.dazkins.triad.game.inventory;

public class EquipmentInventory extends Inventory {
	public enum Type {
		HEAD, BODY, LEGS, FEET, WEAPON;
	}
	
	public EquipmentInventory() {
		super(5, 5);
	}
}