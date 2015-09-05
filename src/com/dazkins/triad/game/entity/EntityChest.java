package com.dazkins.triad.game.entity;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.math.AABB;

public class EntityChest extends Entity {

	public EntityChest(World w, float x, float y) {
		super(w, x, y, "chest");
		// TODO Auto-generated constructor stub
	}

	@Override
	public AABB getAABB() {
		// TODO Auto-generated method stub
		return null;
	}
	
}