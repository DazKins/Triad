package com.dazkins.triad.game.entity.mob;

import com.dazkins.triad.game.entity.Entity;

public abstract class Mob extends Entity {
	protected int health;
	
	public Mob(float x, float y, int dfr, int h) {
		super(x, y, dfr);
		health = h;
	}
	
	public abstract int getMaxHealth();
	
	public int getHealth() {
		return health;
	}
}