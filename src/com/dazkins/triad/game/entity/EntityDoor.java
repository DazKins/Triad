package com.dazkins.triad.game.entity;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.model.ModelDoor;
import com.dazkins.triad.math.AABB;

public class EntityDoor extends Entity implements Activeatable {
	private boolean open;
	
	private int closedOrientation;

	public EntityDoor(World w, float x, float y, int o) {
		super(w, EntityIDStorage.DOOR, x, y, "door");
		closedOrientation = o;
	}
	
	public int getDirectionToRender() {
		if (closedOrientation == Facing.UP) {
			if (open)
				return Facing.RIGHT;
			return Facing.UP;
		} else if (closedOrientation == Facing.DOWN) {
			if (open)
				return Facing.LEFT;
			return Facing.DOWN;
		} else if (closedOrientation == Facing.LEFT) {
			if (open) 
				return Facing.DOWN;
			return Facing.LEFT;
		} else {
			if (open)
				return Facing.UP;
			return Facing.RIGHT;
		}
	}
	
	public boolean mayPass(Entity e) {
		return false;
	}

	public void onActivate(Entity e) {
		open = !open;
	}

	public AABB getAABB() {
		int d = getDirectionToRender();
		if (d == Facing.DOWN || d == Facing.UP) {
			return new AABB(x, y, x + 32, y + 5);
		} else {
			return new AABB(x, y - 16, x + 5, y + 1);
		}
	}
}