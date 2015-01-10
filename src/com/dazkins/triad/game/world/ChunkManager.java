package com.dazkins.triad.game.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.math.AABB;

public class ChunkManager {
	private World world;
	
	private Map<Integer, Chunk> chunks;
	private ArrayList<Chunk> allChunks;
	
	private int numberChunksGenerated;
	
	public ChunkManager(World w) {
		allChunks = new ArrayList<Chunk>();
		chunks = new HashMap<Integer, Chunk>();
		world = w;
	}
	
	public Chunk getChunk(int x, int y) {
		int i = x + y * 10000;
		
		Chunk c;
		
		if (!chunks.containsKey(i)) {
			if (numberChunksGenerated <= 1) {
				c = new Chunk(world, x, y);
				numberChunksGenerated++;
				long l = System.currentTimeMillis();
//				c.generateTileMap();
//				System.out.println("Regen cunks " + (System.currentTimeMillis() - l));
//				c.generateVBO();
				chunks.put(i, c);
			} else {
				return null;
			}
		} else {
			c = chunks.get(i);
		}
		
		if(!allChunks.contains(c))
			allChunks.add(c);
		
		return c;
	}
	
	public void tick() {
		numberChunksGenerated = 0;
	}
	
	public ArrayList<Chunk> getAllChunks() {
		return allChunks;
	}
	
	public ArrayList<Chunk> getChunksInAABB(AABB b) {
		ArrayList<Chunk> r = new ArrayList<Chunk>();
		
		int x0 = (int) ((b.getX0() / Tile.tileSize) / Chunk.chunkW) - 1;
		int y0 = (int) ((b.getY0() / Tile.tileSize) / Chunk.chunkH) - 1;
		int x1 = (int) ((b.getX1() / Tile.tileSize) / Chunk.chunkW) + 1;
		int y1 = (int) ((b.getY1() / Tile.tileSize) / Chunk.chunkH) + 1;
		
		for (int x = x0; x <= x1; x++) {
			for (int y = y0; y <= y1; y++) {
				Chunk c = getChunk(x, y);
				if (c != null && c.getBounds().intersects(b))
					r.add(c);
			}
		}
		
		return r;
	}
}