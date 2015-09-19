package com.dazkins.triad.math;

import com.dazkins.triad.game.world.Chunk;

public class MathHelper
{
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
		return MathHelper.betterMod(x, Chunk.chunkS);
	}

	public static int convertToChunkTileY(int y)
	{
		return MathHelper.betterMod(y, Chunk.chunkS);
	}

	public static int getChunkXFromTileX(int x)
	{
		if (x >= 0)
			return x / Chunk.chunkS;
		else
			return (int) Math.floor((float) x / (float) Chunk.chunkS);
	}

	public static int getChunkYFromTileY(int y)
	{
		if (y >= 0)
			return y / Chunk.chunkS;
		else
			return (int) Math.floor((float) y / (float) Chunk.chunkS);
	}
}