package com.dazkins.triad.game.entity;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.model.Model;
import com.dazkins.triad.gfx.model.ModelFlower;
import com.dazkins.triad.math.AABB;

public class EntityFlower extends Entity {
	public EntityFlower(World w, float x, float y) {
		super(w, x, y, "flower");
	}
	
	public void initModel() {
		model = Model.flower;
	}

	public void render(Camera cam) {
		renderShadow(cam, x - 6, y - 2, 10, 4);
		super.render(cam);
	}

	public AABB getAABB() {
		return null;
	}
}