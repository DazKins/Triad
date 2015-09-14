package com.dazkins.triad.game.entity;

import com.dazkins.triad.game.entity.mob.EntityPlayerClient;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.model.ModelChest;
import com.dazkins.triad.math.AABB;

public class EntityChest extends Entity implements Interactable {
	private Inventory inv;
	
	public EntityChest(World w, float x, float y) {
		super(w, EntityIDStorage.CHEST, x, y, "chest");
		inv = new Inventory(9, 5);
	}

	public AABB getAABB() {
		return new AABB(x - 32, y - 3, x + 32, y + 7);
	}
	
	public Inventory getInventory() {
		return inv;
	}

	public void onInteract(Entity e) {
		if (e instanceof EntityPlayerClient) {
			((EntityPlayerClient) e).setInteractingObject(this);
		}
	}
}