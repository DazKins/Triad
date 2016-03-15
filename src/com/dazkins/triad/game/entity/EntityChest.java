package com.dazkins.triad.game.entity;

import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.item.Item;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.math.AABB;

public class EntityChest extends Entity implements Interactable, IEntityWithInventory
{
	private Inventory inv;

	public EntityChest(World w, float x, float y)
	{
		super(w, StorageEntityID.CHEST, x, y, "chest");
		inv = new Inventory(9, 5);
		
		inv.addItem(Item.testSword);
	}

	public AABB getAABB()
	{
		return new AABB(x - 32, y - 3, x + 32, y + 7);
	}

	public Inventory getInventory()
	{
		return inv;
	}

	public void setInventory(Inventory i)
	{
		inv = i;
	}

	public void onInteract(Entity e)
	{
	}
}