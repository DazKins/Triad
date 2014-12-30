package com.dazkins.triad.game.entity;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Color;
import com.dazkins.triad.math.AABB;

public class EntityTorch extends Entity implements LightEmitter {
	public EntityTorch(World w, float x, float y) {
		super(w, x, y, "torch");
	}

	public int getR() {
		return 255;
	}
	
	public int getG() {
		return 255;
	}
	
	public int getB() {
		return 255;
	}

	public void render() {
		
	}
	
	public void tick() {
		super.tick();
		
		y += 1;
		x += 1;
		
		int xx = (int) x >> 5;
		int yy = (int) y >> 5;
		
		int halfScanRange = 25;
		
		for (int ix = xx - halfScanRange; ix < xx + halfScanRange; ix++) {
			float dx = (float) Math.abs(x -  ix * Tile.tileSize);
			for (int iy = yy - halfScanRange; iy < yy + halfScanRange; iy++) {
				float dy = (float) Math.abs(y - iy * Tile.tileSize);
				float dist = (float) Math.sqrt(dx * dx + dy * dy) / Tile.tileSize;

				if (dist <= world.iLightFalloff) {
					int rVal = (int) (getR() + ((world.ambientLightLevel - getR()) * (dist / world.iLightFalloff)));
					int gVal = (int) (getG() + ((world.ambientLightLevel - getG()) * (dist / world.iLightFalloff)));
					int bVal = (int) (getB() + ((world.ambientLightLevel - getB()) * (dist / world.iLightFalloff)));
					
					Color c = new Color(rVal, gVal, bVal);
					c.clip(world.ambientLightLevel);
					
					world.getTileColor(ix, iy).blend(c);
				}
			}
		}
		
//		if (lifeTicks > 60)
//			remove();
	}

	public AABB getAABB() {
		return null;
	}

	public void renderToPlayerGui(Camera c) {
		
	}
}