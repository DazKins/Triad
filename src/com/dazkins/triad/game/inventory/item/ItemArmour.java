package com.dazkins.triad.game.inventory.item;

public class ItemArmour extends ItemEquipable {
	private int armour;
	
	public ItemArmour(String name, int a) {
		super(name);
		armour = a;
	}
	
	public static class ItemArmourHead extends ItemArmour {
		public static String itemTypeIdentifier = "Armour_Head";
		
		public ItemArmourHead(String name, int a) {
			super(name, a);
		}
	}
	
	public static class ItemArmourBody extends ItemArmour {
		public static String itemTypeIdentifier = "Armour_Body";
		
		public ItemArmourBody(String name, int a) {
			super(name, a);
		}
		
	}
	
	public static class ItemArmourLegs extends ItemArmour {
		public static String itemTypeIdentifier = "Armour_Legs";
		
		public ItemArmourLegs(String name, int a) {
			super(name, a);
		}
		
	}
	
	public static class ItemArmourFeet extends ItemArmour {
		public static String itemTypeIdentifier = "Armour_Feet";
		
		public ItemArmourFeet(String name, int a) {
			super(name, a);
		}
		
	}
}