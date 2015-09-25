package com.dazkins.triad.game.inventory;

import com.dazkins.triad.game.inventory.item.Item;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.util.TriadLogger;

public class Inventory
{
	public int width, height;

	protected ItemStack[] items;

	public Inventory(int width, int height)
	{
		this.height = height;
		this.width = width;
		items = new ItemStack[height * width];
	}

	public ItemStack getItemStack(int x, int y)
	{
		return items[x + y * width];
	}

	public ItemStack getItemStack(int i)
	{
		if (i >= 0)
			return items[i];
		return null;
	}

	public void removeItemStack(int i)
	{
		items[i] = null;
	}

	public boolean addItem(Item i)
	{
		ItemStack is = new ItemStack(i, 1);
		return addItemStack(is);
	}

	public boolean addItems(Item i, int n)
	{
		ItemStack is = new ItemStack(i, n);
		return addItemStack(is);
	}

	public boolean addItemStack(ItemStack is)
	{
		if (is != null)
		{
			if (is.getItemType() != null)
			{
				for (int i = 0; i < items.length; i++)
				{
					ItemStack itemS = items[i];
					if (itemS != null)
					{
						if (itemS.getItemType() == is.getItemType())
						{
							if (is.getItemType().isStackable())
							{
								itemS.addToQuantity(is.getSize());
								return true;
							}
						}
					}
				}
				for (int i = 0; i < items.length; i++)
				{
					if (items[i] == null)
					{
						items[i] = is;
						return true;
					}
				}
				return false;
			} else
			{
				TriadLogger.log("Item type added was null!", true);
			}
		}
		return false;
	}

	public void addItemStack(ItemStack is, int x, int y)
	{
		if (is.getItemType() != null)
			items[x + y * width] = is;
		else
			TriadLogger.log("Item type added was null!", true);
	}

	public boolean addItemStack(ItemStack is, int i)
	{
		if (is.getItemType() != null)
		{
			items[i] = is;
			return true;
		} else
		{
			TriadLogger.log("Item type added was null!", true);
			return false;
		}
	}

	public void removeItemStack(int x, int y)
	{
		items[x + y * width] = null;
	}
}