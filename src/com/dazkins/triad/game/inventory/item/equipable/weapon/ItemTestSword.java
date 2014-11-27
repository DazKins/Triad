package com.dazkins.triad.game.inventory.item.equipable.weapon;

public class ItemTestSword extends ItemWeapon {
	public ItemTestSword() {
		super("testSword");
	}
	
	public int getDamage() {
		return 15;
	}
	
	public int getKnockback() {
		return 10;
	}
	
	public int getAttackCooldown() {
		return 30;
	}
}