package com.dazkins.triad.game.inventory.item;

public class ItemStack {
	private Item itemType;
	private int size;
	
	public ItemStack(Item i, int s) {
		itemType = i;
		size = s;
	}
	
	public int getSize() {
		return size;
	}
	
	public void addToQuantity(int a) {
		size += a;
	}
	
	public void setSize(int s) {
		size = s;
	}
	
	public Item getItemType() {
		return itemType;
	}
 }