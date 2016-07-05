package com.dazkins.triad.game.inventory;

import com.dazkins.triad.game.inventory.item.Item;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.networking.client.update.ClientUpdateInventory;
import com.dazkins.triad.networking.packet.Packet012Inventory;
import com.dazkins.triad.networking.packet.Packet014InteractionUpdate;
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
		changed = true;
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
								itemS.addToQuantity(is.getStackSize());
								return true;
							}
						}
					}
				}
				int count = 0;
				for (int i = 0; i < items.length; i++)
				{
					if (count == is.getStackSize())
						break;
					if (items[i] == null)
					{
						items[i] = new ItemStack(is.getItemType(), 1);
						count++;
						changed = true;
					}
				}
				if (count == is.getStackSize())
					return true;
				else
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
		{
			items[x + y * width] = is;
			changed = true;
		}
		else
			TriadLogger.log("Item type added was null!", true);
	}

	public boolean addItemStack(ItemStack is, int i)
	{
		if (is != null)
		{
			if (is.getItemType() != null)
			{
				items[i] = is;
				changed = true;
				return true;
			} else
			{
				TriadLogger.log("Item type added was null!", true);
				return false;
			}
		}
		return false;
	}

	public void removeItemStack(int x, int y)
	{
		items[x + y * width] = null;
		changed = true;
	}
	
	//For server client interaction
	boolean changed = true;
	
	public boolean hasChanged()
	{
		return changed;
	}
	
	public static Packet012Inventory compressToPacket(Inventory inv, int eID)
	{
		Packet012Inventory p0 = new Packet012Inventory();
		p0.setEntityGID(eID);
		p0.setWidth(inv.width);
		p0.setHeight(inv.height);
		int items[] = new int[inv.width * inv.height];
		int stackSizes[] = new int[inv.width * inv.height];
		for (int i = 0; i < inv.width * inv.height; i++)
		{
			ItemStack is = inv.getItemStack(i);
			if (is != null)
			{
				int size = is.getStackSize();
				int id = is.getItemType().getID();
				items[i] = id;
				stackSizes[i] = size;
			}
		}
		p0.setItems(items);
		p0.setStackCounts(stackSizes);
		
		return p0;
	}
	
	public static Inventory createInventoryObject(Packet012Inventory p)
	{
		int width = p.getWidth();
		int height = p.getHeight();
		
		Inventory inv = new Inventory(width, height); 
				
		int items[] = p.getItems();
		int stackCounts[] = p.getStackCounts();
		
		for (int i = 0; i < width * height; i++)
		{
			int itemID = items[i];
			int stackSize = stackCounts[i];
			if (stackSize > 0 && itemID >= 0)
			{
				Item it = Item.items[itemID];
				ItemStack is = new ItemStack(it, stackSize);
				inv.addItemStack(is, i);
			}
		}
		
		return inv;
	}
	
	public static Inventory createInventoryObject(ClientUpdateInventory p)
	{
		int width = p.getWidth();
		int height = p.getHeight();
		
		Inventory inv = new Inventory(width, height); 
				
		ItemStack items[] = p.getItems();
		
		for (int i = 0; i < width * height; i++)
		{
			ItemStack ci = items[i];
			if (ci != null)
			{
				inv.addItemStack(ci, i);
			}
		}
		
		return inv;
	}
	
	public void resetHasChangedFlag()
	{
		changed = false;
	}
}