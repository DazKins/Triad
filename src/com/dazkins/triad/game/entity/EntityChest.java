package com.dazkins.triad.game.entity;

import com.dazkins.triad.game.entity.mob.EntityPlayer;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.model.ModelChest;
import com.dazkins.triad.math.AABB;

public class EntityChest extends Entity implements Interactable {
	private Inventory inv;
	
	public EntityChest(World w, float x, float y) {
		super(w, x, y, "chest");
		inv = new Inventory(9, 5);
	}
	
	public void initModel() {
		model = new ModelChest();
	}

	public AABB getAABB() {
		return new AABB(x - 32, y - 3, x + 32, y + 7);
	}
	
	public boolean mayPass(Entity e) {
		return false;
	}
	
	public Inventory getInv() {
		return inv;
	}
	
	public void render(Camera cam) {
		renderShadow(cam, x - 32, y - 2, 64, 6);
		super.render(cam);
	}

	public void onInteract(Entity e) {
		if (e instanceof EntityPlayer) {
			((EntityPlayer) e).setInteractingObject(this);
		}
	}
}