package com.dazkins.triad.game.entity;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.model.Model;
import com.dazkins.triad.gfx.model.ModelTree;
import com.dazkins.triad.math.AABB;

public class EntityTree extends Entity {
	public EntityTree(World w, float x, float y) {
		super(w, x, y, "tree");
	}
	
	public void render(Camera cam) {
		renderShadow(cam, x - 15, y - 5, 30, 10);
		super.render(cam);
	}
	
	public void initModel() {
		model = Model.tree;
	}

	public AABB getAABB() {
		return null;
	}
}