package com.dazkins.triad.game.entity;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.model.ModelTree;
import com.dazkins.triad.math.AABB;

public class EntityTree extends Entity {
	public EntityTree(World w, float x, float y) {
		super(w, x, y, "tree");
	}
	
	public void initModel() {
		model = new ModelTree();
	}

	public AABB getAABB() {
		return null;
	}
}