package com.dazkins.triad.game.inventory.item;

public class ItemStack
{
	private Item itemType;
	private int stackSize;

	public ItemStack(Item i, int s)
	{
		itemType = i;
		stackSize = s;
	}

	public int getStackSize()
	{
		return stackSize;
	}

	public void addToQuantity(int a)
	{
		stackSize += a;
	}

	public void setSize(int s)
	{
		stackSize = s;
	}

	public Item getItemType()
	{
		return itemType;
	}
}