package com.dazkins.triad.game.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.math.AABB;

public class ServerChunkManager
{
	private World world;

	// TODO Use Chunks Coordinate system
	private Map<ChunkCoordinate, Chunk> chunks;
	private ArrayList<ChunkCoordinate> loadedChunks;

	private int chunkLoadCount;

	private int chunkLoadCuttoff = 100;

	public ServerChunkManager(World w)
	{
		loadedChunks = new ArrayList<ChunkCoordinate>();
		chunks = new HashMap<ChunkCoordinate, Chunk>();
		world = w;
	}

	public Chunk getChunkWithForceLoad(int x, int y)
	{
		ChunkCoordinate c = new ChunkCoordinate(x, y);

		Chunk ch;

		if (!chunks.containsKey(c))
		{
			ch = new Chunk(world, x, y);
			chunks.put(c, ch);
		} else
		{
			ch = chunks.get(c);
		}

		if (!loadedChunks.contains(c))
		{
			chunkLoadCount++;
			loadedChunks.add(c);
			if (chunkLoadCount > chunkLoadCuttoff)
			{
				loadedChunks.remove(0);
				chunkLoadCount--;
			}
		}

		return ch;
	}

	public Chunk getChunk(int x, int y)
	{
		ChunkCoordinate c = new ChunkCoordinate(x, y);
		if (chunks.containsKey(c))
			return chunks.get(c);
		return null;
	}

	public ArrayList<Chunk> getLoadedChunks()
	{
		ArrayList<Chunk> r = new ArrayList<Chunk>();

		for (int i = 0; i < loadedChunks.size(); i++)
		{
			ChunkCoordinate c = loadedChunks.get(i);
			r.add(chunks.get(c));
		}

		return r;
	}

	public ArrayList<Chunk> getChunksInAABB(AABB b)
	{
		ArrayList<Chunk> r = new ArrayList<Chunk>();

		int x0 = (int) ((b.getX0() / Tile.tileSize) / Chunk.chunkS) - 1;
		int y0 = (int) ((b.getY0() / Tile.tileSize) / Chunk.chunkS) - 1;
		int x1 = (int) ((b.getX1() / Tile.tileSize) / Chunk.chunkS) + 1;
		int y1 = (int) ((b.getY1() / Tile.tileSize) / Chunk.chunkS) + 1;

		for (int x = x0; x <= x1; x++)
		{
			for (int y = y0; y <= y1; y++)
			{
				Chunk c = getChunkWithForceLoad(x, y);
				if (c != null && c.getBounds().intersects(b))
					r.add(c);
			}
		}

		return r;
	}
}