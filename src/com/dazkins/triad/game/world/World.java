package com.dazkins.triad.game.world;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.dazkins.triad.file.DatabaseFile;
import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Camera;

public class World {
	private static DatabaseFile globalWorldDatabase = null;

	public DatabaseFile worldDB;
	public int nChunksX = 25;
	public int nChunksY = 25;
	private Chunk[] chunks;

	static {
		loadWorldDatabase();
	}

	private static void loadWorldDatabase() {
		try {
			globalWorldDatabase = new DatabaseFile("res/data/worlds.db");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public World() {
		chunks = new Chunk[nChunksX * nChunksY];
		for (int x = 0; x < nChunksX; x++) {
			for (int y = 0; y < nChunksY; y++) {
				chunks[x + y * nChunksX] = new Chunk(x * Chunk.chunkW * Tile.tileSize, y * Chunk.chunkH * Tile.tileSize);
			}
		}
//		for (int i = 0; i < entities.length; i++) {
//			entities[i] = new ArrayList<Entity>();
//		}
	}

//	public void addEntity(Entity e) {
//		int x = (int) e.getX() >> 4;
//		int y = (int) e.getY() >> 4;
//
//		entities[x + y * MWIDTH].add(e);
//	}

	public void render(Camera cam) {
		for (int i = 0; i < chunks.length; i++) {
			chunks[i].render();
		}
	}
	
	private boolean isValidTilePos(int x, int y) {
		return (x >= 0 && x < nChunksX * Chunk.chunkW) && (y >= 0 && y < nChunksY * Chunk.chunkH);
	}

	public Tile getTile(int x, int y) {
		if (!isValidTilePos(x, y))
			return null;
		return chunks[(x / Chunk.chunkW) + (y / Chunk.chunkH) * nChunksX].getTile(x % Chunk.chunkW, y % Chunk.chunkH);
	}

	public void tick() {
		
	}
}