package com.dazkins.triad.game.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.math.AABB;

public class ChunkManager {
	private World world;
	
	private Map<Integer, Chunk> chunks;
	private ArrayList<Integer> loadedChunkI;
	
	private int chunkLoadCount;
	
	private int chunkLoadCuttoff = 10000;
	
	public ChunkManager(World w) {
		loadedChunkI = new ArrayList<Integer>();
		chunks = new HashMap<Integer, Chunk>();
		world = w;
	}
	
	public Chunk getChunkWithForceLoad(int x, int y) {
		Integer i = x + y * 100000;
		
		Chunk c;
		
		if (!chunks.containsKey(i)) {
			c = new Chunk(world, x, y);
			chunks.put(i, c);
		} else {
			c = chunks.get(i);
		}

		if (!loadedChunkI.contains(i)) {
			chunkLoadCount++;
			loadedChunkI.add(i);
			if (chunkLoadCount > chunkLoadCuttoff) {
				Integer ind = loadedChunkI.get(0);
				loadedChunkI.remove(ind);
				chunkLoadCount--;
			}
		}
		
		return c;
	}
	
	public Chunk getChunk(int x, int y) {
		Integer i = x + y * 100000;
		if(chunks.containsKey(i))
			return chunks.get(i);
		return null;
	}
	
	public ArrayList<Chunk> getLoadedChunks() {
		ArrayList<Chunk> r = new ArrayList<Chunk>();
		
		for (int i = 0; i < loadedChunkI.size(); i++)  {
			Integer ind = loadedChunkI.get(i);
			r.add(chunks.get(ind));
		}
		
		return r;
	}
	
	public ArrayList<Chunk> getChunksInAABB(AABB b) {
		ArrayList<Chunk> r = new ArrayList<Chunk>();
		
		int x0 = (int) ((b.getX0() / Tile.tileSize) / Chunk.chunkS) - 1;
		int y0 = (int) ((b.getY0() / Tile.tileSize) / Chunk.chunkS) - 1;
		int x1 = (int) ((b.getX1() / Tile.tileSize) / Chunk.chunkS) + 1;
		int y1 = (int) ((b.getY1() / Tile.tileSize) / Chunk.chunkS) + 1;
		
		for (int x = x0; x <= x1; x++) {
			for (int y = y0; y <= y1; y++) {
				Chunk c = getChunkWithForceLoad(x, y);
				if (c != null && c.getBounds().intersects(b))
					r.add(c);
			}
		}
		
		return r;
	}
}