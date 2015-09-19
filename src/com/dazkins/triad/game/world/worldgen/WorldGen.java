package com.dazkins.triad.game.world.worldgen;

import com.dazkins.triad.game.entity.EntityFlower;
import com.dazkins.triad.game.entity.EntitySandGrass;
import com.dazkins.triad.game.entity.harvestable.EntityTree;
import com.dazkins.triad.game.entity.mob.EntityPig;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.math.PerlinNoiseMap;

public class WorldGen
{
	private World world;

	private PerlinNoiseMap worldNoise;
	private PerlinNoiseMap foliageNoise;

	private float waterLimit;

	public WorldGen(World w)
	{
		worldNoise = new PerlinNoiseMap(0.6f, 8, 1 / 2048.0f);
		worldNoise.checkForConsistencyCorrection();
		foliageNoise = new PerlinNoiseMap(0.6f, 4, 1 / 2048.0f);

		world = w;

		waterLimit = 0.2f;
	}

	public float getWaterLimit()
	{
		return waterLimit;
	}

	public PerlinNoiseMap getWorldNoise()
	{
		return worldNoise;
	}

	public PerlinNoiseMap getFoliageNoise()
	{
		return foliageNoise;
	}

	public void generate(int x, int y)
	{
		float ws = worldNoise.sample(x, y);

		if (ws < waterLimit)
			world.setTile(Tile.water, x, y);
		else if (ws < waterLimit + 0.05f)
		{
			float r = (float) Math.random() * 1000.0f;
			if (r < 2)
				world.addEntity(new EntitySandGrass(world, x * Tile.tileSize, y * Tile.tileSize));
			world.setTile(Tile.sand, x, y);
		} else
		{
			float r = (float) Math.random() * 10000.0f;
			if (r < 10)
				world.addEntity(new EntityTree(world, x * Tile.tileSize, y * Tile.tileSize));
			else if (r < 30)
				world.addEntity(new EntityFlower(world, x * Tile.tileSize, y * Tile.tileSize));
			else if (r > 9998 && ws > 0.4f)
				world.addEntity(new EntityPig(world, x * Tile.tileSize, y * Tile.tileSize, 10));
			world.setTile(Tile.grass, x, y);
		}
	}
}