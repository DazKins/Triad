package com.dazkins.triad.game.inventory;

import org.lwjgl.Sys;

import com.dazkins.triad.game.inventory.item.ItemStack;

public class Inventory {
	public int width, height;
	
	protected ItemStack[] items;
	
	public Inventory(int width, int height) {
		this.height = height;
		this.width = width;
		items = new ItemStack[height * width];
	}
	
	public ItemStack getItemStack(int x, int y) {
		return items[x + y * width];
	}
	
	public ItemStack getItemStack(int i) {
		return items[i];
	}
	
	public void removeItemStack(int i) {
		items[i] = null;
	}
	
	public boolean addItemStack(ItemStack is) {
		if (is != null) {
			if (is.getItemType() != null) {
				for (int i = 0; i < items.length; i++) {
					if (items[i] == null) {
						items[i] = is;
						return true;
					}
				}
			} else {
				System.err.println("Item type added was null!");
			}
		}
		return false;
	}
	
	public void addItemStack(ItemStack is, int x, int y) {
		if (is.getItemType() != null)
			items[x + y * width] = is;
		else
			System.err.println("Item type added was null!");
	}
	
	public boolean addItemStack(ItemStack is, int i) {
		if (is.getItemType() != null) {
			items[i] = is;
			return true;
		}
		else {
			System.err.println("Item type added was null!");
			return false;
		}
	}
	
	public void removeItemStack(int x, int y) {
		items[x + y * width] = null;
	}
}