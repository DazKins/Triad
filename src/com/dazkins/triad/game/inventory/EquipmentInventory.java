package com.dazkins.triad.game.inventory;

import com.dazkins.triad.game.inventory.item.Item;
import com.dazkins.triad.game.inventory.item.ItemArmour.ItemArmourBody;
import com.dazkins.triad.game.inventory.item.ItemArmour.ItemArmourFeet;
import com.dazkins.triad.game.inventory.item.ItemArmour.ItemArmourHead;
import com.dazkins.triad.game.inventory.item.ItemArmour.ItemArmourLegs;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.game.inventory.item.ItemWeapon;

public class EquipmentInventory extends Inventory {
	public static final int HEAD = 0, BODY = 1, LEGS = 2, FEET = 3, WEAPON = 4;
	
	public boolean addItemStack(ItemStack is) {
		Item it = is.getItemType();
		if (it instanceof ItemArmourHead) {
			return addItemStack(is, HEAD);
		}
		else if (it instanceof ItemArmourBody) {
			return addItemStack(is, BODY);
		}
		else if (it instanceof ItemArmourLegs) {
			return addItemStack(is, LEGS);
		}
		else if (it instanceof ItemArmourFeet) {
			return addItemStack(is, FEET);
		}
		else if (it instanceof ItemWeapon) {
			return addItemStack(is, WEAPON);
		}
		return false;
	}
	
	public boolean addItemStack(ItemStack is, int i) {
		if (getItemStack(i) == null) {
			if (is.getItemType() != null) {
				items[i] = is;
				return true;
			}
			else
				System.err.println("Item type added was null!");
		}
		return false;
	}
	
	public EquipmentInventory() {
		super(5, 5);
	}
}