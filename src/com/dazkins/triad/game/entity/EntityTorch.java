package com.dazkins.triad.game.entity;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Color;
import com.dazkins.triad.gfx.model.ModelTorch;
import com.dazkins.triad.math.AABB;

public class EntityTorch extends Entity implements LightEmitter
{
	public EntityTorch(World w, float x, float y)
	{
		super(w, EntityIDStorage.TORCH, x, y, "torch");
	}

	public int getR()
	{
		return 255;
	}

	public int getG()
	{
		return 150;
	}

	public int getB()
	{
		return 150;
	}

	public void tick()
	{
		super.tick();

		int xx = (int) x >> 5;
		int yy = (int) y >> 5;

		int halfScanRange = 25;

		for (int ix = xx - halfScanRange; ix < xx + halfScanRange; ix++)
		{
			float dx = (float) Math.abs(x - ix * Tile.tileSize);
			for (int iy = yy - halfScanRange; iy < yy + halfScanRange; iy++)
			{
				float dy = (float) Math.abs(y - iy * Tile.tileSize);
				float dist = (float) Math.sqrt(dx * dx + dy * dy) / Tile.tileSize;

				if (dist <= world.iLightFalloff)
				{
					Color worldC = world.getAmbientLight();
					int rVal = (int) (getR() + ((worldC.getR() - getR()) * (dist / world.iLightFalloff)));
					int gVal = (int) (getG() + ((worldC.getG() - getG()) * (dist / world.iLightFalloff)));
					int bVal = (int) (getB() + ((worldC.getB() - getB()) * (dist / world.iLightFalloff)));

					Color c = new Color(rVal, gVal, bVal);
					c.blend(worldC);

					world.getTileColor(ix, iy).blend(c);
				}
			}
		}

		// if (lifeTicks > 60)
		// remove();

		super.move();

		xa *= 0.85;
		ya *= 0.85;
	}

	public boolean mayPass(Entity e)
	{
		return true;
	}

	public AABB getAABB()
	{
		return new AABB(x - 4, y, x + 4, y + 5);
	}

	public void renderToPlayerGui(Camera c)
	{

	}
}