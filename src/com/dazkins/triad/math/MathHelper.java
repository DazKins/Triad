package com.dazkins.triad.math;

import com.dazkins.triad.game.world.Chunk;

public class MathHelper
{
	public static final int SIZE_OF_FLOAT = 4;
	
	public static final float PI = (float) Math.PI;
	
	// Treats negatives better
	public static int betterMod(float x, int m)
	{
		float r = 0;

		if (x < 0)
			r = (m - (Math.abs(x) % m));
		else
			r = x % m;

		if (r < 0)
			r = m - 1;
		if (r >= m)
			r = 0;

		return (int) r;
	}

	public static int convertToChunkTileX(int x)
	{
		return MathHelper.betterMod(x, Chunk.CHUNKS);
	}

	public static int convertToChunkTileY(int y)
	{
		return MathHelper.betterMod(y, Chunk.CHUNKS);
	}

	public static int getChunkXFromTileX(int x)
	{
		if (x >= 0)
			return x / Chunk.CHUNKS;
		else
			return (int) Math.floor((float) x / (float) Chunk.CHUNKS);
	}

	public static int getChunkYFromTileY(int y)
	{
		if (y >= 0)
			return y / Chunk.CHUNKS;
		else
			return (int) Math.floor((float) y / (float) Chunk.CHUNKS);
	}
}