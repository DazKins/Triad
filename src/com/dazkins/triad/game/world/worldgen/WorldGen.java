package com.dazkins.triad.game.world.worldgen;

import java.util.ArrayList;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.math.PerlinNoiseMap;

public class WorldGen {
	private World world;
	
	private PerlinNoiseMap worldNoise;
	private PerlinNoiseMap foliageNoise;
	
	private float waterLimit;
	
	public WorldGen(World w) {
		worldNoise = new PerlinNoiseMap(0.6f, 8, 1 / 2048.0f);
		worldNoise.checkForConsistencyCorrection();
		foliageNoise = new PerlinNoiseMap(0.6f, 4, 1 / 2048.0f);
		
		world = w;
		
		waterLimit = 0.2f;
	}
	
	public float getWaterLimit() {
		return waterLimit;
	}
	
	public PerlinNoiseMap getWorldNoise() {
		return worldNoise;
	}
	
	public PerlinNoiseMap getFoliageNoise() {
		return foliageNoise;
	}
	
	public void generate(int x, int y) {
		float ws = worldNoise.sample(x, y);
		
		if (ws < waterLimit)
			world.setTile(Tile.water, x, y);
		else if (ws < waterLimit + 0.05f)
			world.setTile(Tile.sand, x, y);
		else
			world.setTile(Tile.grass, x, y);
	}
}