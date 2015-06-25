package com.dazkins.triad.game.inventory.item.equipable.weapon.harvestTool;

import com.dazkins.triad.game.inventory.item.equipable.weapon.ItemWeapon;

public class ItemAxe extends ItemHarvestTool {
	public ItemAxe() {
		super("axe");
	}
	
	public int getDamage() {
		return 5;
	}
	
	public int getKnockback() {
		return 10;
	}
	
	public int getAttackCooldown() {
		return 50;
	}
	
	public int getAttackRange() {
		return 50;
	}
	
	public int getHarvestDamage() {
		return 5;
	}
}