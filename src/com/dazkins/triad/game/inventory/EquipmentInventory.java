package com.dazkins.triad.game.inventory;

import java.util.ArrayList;

import com.dazkins.triad.game.inventory.item.Item;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.game.inventory.item.equipable.armour.ItemArmour;
import com.dazkins.triad.game.inventory.item.equipable.armour.body.ItemArmourBody;
import com.dazkins.triad.game.inventory.item.equipable.armour.feet.ItemArmourFeet;
import com.dazkins.triad.game.inventory.item.equipable.armour.head.ItemArmourHead;
import com.dazkins.triad.game.inventory.item.equipable.armour.legs.ItemArmourLegs;
import com.dazkins.triad.game.inventory.item.equipable.weapon.ItemWeapon;

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
	
	public ItemArmourHead getHeadItem() {
		ItemStack is = getItemStack(HEAD);
		if (is != null) {
			return (ItemArmourHead) is.getItemType();
		}
		return null;
	}
	
	public ItemArmourBody getBodyItem() {
		ItemStack is = getItemStack(BODY);
		if (is != null) {
			return (ItemArmourBody) is.getItemType();
		}
		return null;
	}
	
	public ItemArmourLegs getLegsItem() {
		ItemStack is = getItemStack(LEGS);
		if (is != null) {
			return (ItemArmourLegs) is.getItemType();
		}
		return null;
	}
	
	public ItemArmourFeet getFeetItem() {
		ItemStack is = getItemStack(FEET);
		if (is != null) {
			return (ItemArmourFeet) is.getItemType();
		}
		return null;
	}
	
	public ItemWeapon getWeaponItem() {
		ItemStack is = getItemStack(WEAPON);
		if (is != null) {
			return (ItemWeapon) is.getItemType();
		}
		return null;
	}
	
	public ArrayList<ItemArmour> getArmour(){
		ArrayList<ItemArmour> rValue = new ArrayList<ItemArmour>();
		if (getHeadItem() != null)
			rValue.add(getHeadItem());
		if (getBodyItem() != null)
			rValue.add(getBodyItem());
		if (getLegsItem() != null)
			rValue.add(getLegsItem());
		if (getFeetItem() != null)
			rValue.add(getFeetItem());
		
		return rValue;
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