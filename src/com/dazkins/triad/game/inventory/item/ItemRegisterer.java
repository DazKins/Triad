package com.dazkins.triad.game.inventory.item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dazkins.triad.file.MultiLineDatabaseFile;

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
			int tx = mainDBS.getInt("TX", i);
			int ty = mainDBS.getInt("TY", i);
			String type = mainDBS.getString("ITEM_TYPE", i);
			String name = mainDBS.getString("NAME", i);
			Item itemToAdd = null;
			
			if (type == "null")
				itemToAdd = new Item(tx, ty, name);
			else if (type.equals(ItemWeapon.itemTypeIdentifier)) {
				int damage = mainDBS.getInt("DAMAGE", i);
				itemToAdd = new ItemWeapon(tx, ty, name, damage);
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