package com.dazkins.triad.game.entity.harvestable;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.math.AABB;

public class EntityHarvestable extends Entity {
	int harvestMax;
	
	public EntityHarvestable(World w, int id, float x, float y, String s, int max) {
		super(w, id, x, y, s);
		harvestMax = max;
	}
	
	public void harvest(int d) {
		harvestMax -= d;
		if (harvestMax <= 0)
			onDestroy();
	}
	
	public void onDestroy() {
		dropLoot();
		remove();
	}
	
	public void dropLoot() { }
	
	public AABB getAABB() {
		return null;
	}
}