package com.dazkins.triad.game.entity;

import com.dazkins.triad.game.world.World;

public abstract class ActiveateableEntity extends Entity {
	public ActiveateableEntity(World w, float x, float y, String s) {
		super(w, x, y, s);
	}
	
	public abstract void onActivate();
}