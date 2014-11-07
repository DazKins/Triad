package com.dazkins.triad.game.entity;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.math.AABB;

public class EntityButton extends Entity {
	public EntityButton(World w, float x, float y, String s) {
		super(w, x, y, "button");
	}

	public void renderToPlayerGui(Camera c) {
		
	}

	public void render() {
		
	}

	public AABB getAABB() {
		return null;
	}
}