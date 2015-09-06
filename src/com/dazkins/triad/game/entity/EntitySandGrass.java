package com.dazkins.triad.game.entity;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.model.Model;
import com.dazkins.triad.math.AABB;

public class EntitySandGrass extends Entity {

	public EntitySandGrass(World w, float x, float y) {
		super(w, x, y, "sandGrass");
	}

	public AABB getAABB() {
		return null;
	}
	
	public void initModel() {
		model = Model.sandGrass;
	}
	
	public void render(Camera cam) {
		super.renderShadow(cam, x - 8, y - 3, 16, 5);
		super.render(cam);
	}
}