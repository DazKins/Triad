package com.dazkins.triad.game.entity.mob;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.math.AABB;

public abstract class Mob extends Entity {
	protected int health;
	
	public Mob(World w, float x, float y, String s, int h) {
		super(w, x, y, s);
		health = h;
	}
	
	public abstract int getMaxHealth();
	
	public int getHealth() {
		return health;
	}
	
	public void move(float xa, float ya) {
		AABB aabb = this.getAABB();
		
		int x0 = ((int) x / Tile.tileSize) - 3;
		int y0 = ((int) y / Tile.tileSize) - 3;
		int x1 = ((int) x / Tile.tileSize) + 3;
		int y1 = ((int) y / Tile.tileSize) + 3;
		
		if (x0 < 0)
			x0 = 0;
		if (y0 < 0)
			y0 = 0;
//		if (x1 > world.MWIDTH)
//			x1 = world.MWIDTH;
//		if (y1 > world.MHEIGHT)
//			y1 = world.MHEIGHT;
		
		for (int x = x0; x < x1; x++) {
			for (int y = y0; y < y1; y++) {
//				if (world.getTile(x, y).isCollidable()) {
//					AABB taabb = world.getTile(x, y).getAABB(world, x, y);
//					if (aabb.shifted(xa, 0).intersects(taabb)) {
//						xa = 0;
//					}
//					if (aabb.shifted(0, ya).intersects(taabb)) {
//						ya = 0;
//					}
//				}
			}
		}
		
		super.move(xa, ya);
	}
}