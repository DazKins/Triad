package com.dazkins.triad.game.inventory.item;

public class ItemArmour extends Item {
	private int armour;
	
	public ItemArmour(int tx, int ty, String name, int a) {
		super(tx, ty, name);
		armour = a;
	}
	
	public static class ItemArmourHead extends ItemArmour{
		public static String itemTypeIdentifier = "Armour_Head";
		
		public ItemArmourHead(int tx, int ty, String name, int a) {
			super(tx, ty, name, a);
		}
	}
}