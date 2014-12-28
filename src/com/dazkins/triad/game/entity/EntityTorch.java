package com.dazkins.triad.game.entity;

import java.util.Random;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.math.AABB;

public class EntityTorch extends Entity implements LightEmitter {
	private float r;
	private float g;
	private float b;
	
	public EntityTorch(World w, float x, float y) {
		super(w, x, y, "torch");
		r = 1.0f;
		g = 0.5f;
		b = 0.5f;
	}

	public float getLightStrength() {
		return 1.0f;
	}

	public void render() {
		
	}
	
	public void tick() {
		super.tick();
		
//		y += 1;
//		x += 1;
		
		int xx = (int) x >> 5;
		int yy = (int) y >> 5;
		
		int halfScanRange = 25;
		
		float falloff = 14.0f;
		
		for (int ix = xx - halfScanRange; ix < xx + halfScanRange; ix++) {
			float dx = (float) Math.abs(x -  ix * Tile.tileSize);
			for (int iy = yy - halfScanRange; iy < yy + halfScanRange; iy++) {
				float dy = (float) Math.abs(y - iy * Tile.tileSize);
				float dist = (float) Math.sqrt(dx * dx + dy * dy) / Tile.tileSize;

				float rVal = r + ((world.ambientLightLevel - r) * (dist / falloff));
				float gVal = g + ((world.ambientLightLevel - g) * (dist / falloff));
				float bVal = b + ((world.ambientLightLevel - b) * (dist / falloff));
				
				if (rVal <= r) {
					world.applyTileR(rVal, ix, iy);
				}
			
				if (gVal <= g) {
					world.applyTileG(gVal, ix, iy);
				}
			
				if (bVal <= b) {
					world.applyTileB(bVal, ix, iy);
				}
			}
		}
		
		if (lifeTicks > 60)
			remove();
	}

	public AABB getAABB() {
		return null;
	}

	public void renderToPlayerGui(Camera c) {
		
	}
}