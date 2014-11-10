package com.dazkins.triad.game.inventory.item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dazkins.triad.file.MultiLineDatabaseFile;
import com.dazkins.triad.game.inventory.item.ItemArmour.ItemArmourBody;
import com.dazkins.triad.game.inventory.item.ItemArmour.ItemArmourFeet;
import com.dazkins.triad.game.inventory.item.ItemArmour.ItemArmourHead;
import com.dazkins.triad.game.inventory.item.ItemArmour.ItemArmourLegs;

public class ItemRegisterer {
	private static MultiLineDatabaseFile mainDBS;
	public static ArrayList<Item> items = new ArrayList<Item>();
	
	public static Map<String, Item> nameToItem = new HashMap<String, Item>();
	
	public static void register() {
		try {
			mainDBS = new MultiLineDatabaseFile("res/data/items/item_database.db");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		for (int i = 0; i < mainDBS.getLineCount(); i++) {
			String type = mainDBS.getString("ITEM_TYPE", i);
			String name = mainDBS.getString("NAME", i);
			boolean stackable = mainDBS.getBoolean("STACKS", i);
			Item itemToAdd = null;
			
			if (type == "null")
				itemToAdd = new Item(name, true);
			else if (type.equals(ItemWeapon.itemTypeIdentifier)) {
				int damage = mainDBS.getInt("DAMAGE", i);
				itemToAdd = new ItemWeapon(name, damage);
			} else if (type.equals(ItemArmourHead.itemTypeIdentifier)) {
				int armour = mainDBS.getInt("ARMOUR", i);
				itemToAdd = new ItemArmourHead(name, armour);
			} else if (type.equals(ItemArmourBody.itemTypeIdentifier)) {
				int armour = mainDBS.getInt("ARMOUR", i);
				itemToAdd = new ItemArmourBody(name, armour);
			} else if (type.equals(ItemArmourLegs.itemTypeIdentifier)) {
				int armour = mainDBS.getInt("ARMOUR", i);
				itemToAdd = new ItemArmourLegs(name, armour);
			} else if (type.equals(ItemArmourFeet.itemTypeIdentifier)) {
				int armour = mainDBS.getInt("ARMOUR", i);
				itemToAdd = new ItemArmourFeet(name, armour);
			} else {
				System.out.println(type + " unknown item type!");
				continue;
			}
			
			items.add(itemToAdd);
			nameToItem.put(name.toLowerCase(), itemToAdd);
		}
	}
	
	public static Item getItemByName(String n) {
		return nameToItem.get(n.toLowerCase());
	}
	
	public static Item getItemByIndex(int i) {
		return items.get(i);
	}
	
	public static int getItemID(Item i) {
		return items.indexOf(i);
	}
}