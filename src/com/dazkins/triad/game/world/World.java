package com.dazkins.triad.game.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dazkins.triad.file.ListFile;
import com.dazkins.triad.file.MultiLineDatabaseFile;
import com.dazkins.triad.file.SingleLineDatabaseFile;
import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Camera;

public class World {
	private static ArrayList<World> worlds;
	private static Map<String, Integer> mapToIndex;
	
	private static MultiLineDatabaseFile globalWorldDatabase = null;
	
	private Chunk[] chunks;
	
	public WorldInfo info;

	public static void init() {
		try {
			globalWorldDatabase = new MultiLineDatabaseFile("res/data/worlds.db");
		} catch (IOException e) {
			e.printStackTrace();
		}
		worlds = new ArrayList<World>();
		mapToIndex = new HashMap<String, Integer>();
		for (int i = 0; i < globalWorldDatabase.getLineCount(); i++) {
			String path = globalWorldDatabase.getString("PATH", i);
			World w = loadWorldFromFile(path);
			worlds.add(w);
			assignWorldToDatabase(i, w);
		}
	}
	
	public static World getWorldFromName(String n) {
		return (World) (worlds.get((Integer) (mapToIndex.get(n))));
	}
	
	private static void assignWorldToDatabase(int i, World w) {
		mapToIndex.put(w.info.name, i);
	}
	
	public WorldInfo getInfo() {
		return info;
	}
	
	public static World loadWorldFromFile(String p) {
		World rValue = new World();
		ListFile l = null;
		SingleLineDatabaseFile metaDatabase = null;
		try {
			l = new ListFile("res/data/worlds/" + p + "_data.lt", " ");
			metaDatabase = new SingleLineDatabaseFile("res/data/worlds/" + p + "_metadata.db");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		rValue.info = new WorldInfo();
		rValue.info.loadFromDatabase(metaDatabase);
		
		rValue.generate();
		
		int tw = rValue.info.nChunksX * Chunk.chunkW;
		
		for (int i = 0; i < l.getSize(); i++) {
			int t = l.getInt(i);
			int x = i / tw;
			int y = i % tw;
			rValue.setTile(Tile.tiles[t], x, y);
		}
		
		return rValue;
	}
	
	private void generate() {
		chunks = new Chunk[info.nChunksX * info.nChunksY];
		for (int x = 0; x < info.nChunksX; x++) {
			for (int y = 0; y < info.nChunksY; y++) {
				chunks[x + y * info.nChunksX] = new Chunk(this, x, y);
			}
		}
	}

	public void addEntity(Entity e) {
		if (e.getX() < 0 || e.getY() < 0)
			return;
		
		int cx = (int) ((e.getX() / (float)Tile.tileSize) / (float)Chunk.chunkW);
		int cy = (int) ((e.getY() / (float)Tile.tileSize) / (float)Chunk.chunkH);
		
		if (cx >= 0 && cy >= 0 && cx < info.nChunksX && cy < info.nChunksY)
			chunks[cx + cy * info.nChunksX].addEntity(e);
	}

	public void render(Camera cam) {
		for (int i = 0; i < chunks.length; i++) {
			if (!chunks[i].isGenerated()) {
				chunks[i].generate();
			}
			if (chunks[i].getBounds().intersects(cam.getViewportBounds())) {
				chunks[i].render();
			}
		}
	}
	
	public void renderGrid(Camera cam) {
		for (int i = 0; i < chunks.length; i++) {
			if (chunks[i].getBounds().intersects(cam.getViewportBounds())) {
				chunks[i].renderGrid();
			}
		}
	}
	
	public ArrayList<Entity> getChunkEntites(int cx, int cy) {
		if (cx < 0 || cy < 0 || cx >= info.nChunksX || cy >= info.nChunksY)
			return null;
		return chunks[cx + cy * info.nChunksX].entities;
	}
	
	private boolean isValidTilePos(int x, int y) {
		return (x >= 0 && x < info.nChunksX * Chunk.chunkW) && (y >= 0 && y < info.nChunksY * Chunk.chunkH);
	}
	
	public byte getTileBrightness(int x, int y) {
		if (!isValidTilePos(x, y))
			return 0;
		return chunks[(x / Chunk.chunkW) + (y / Chunk.chunkH) * info.nChunksX].getTileBrightness(x % Chunk.chunkW, y % Chunk.chunkH);
	}
	
	public void setTileBrightness(byte b, int x, int y) {
		if (!isValidTilePos(x, y))
			return;
		chunks[(x / Chunk.chunkW) + (y / Chunk.chunkH) * info.nChunksX].setTileBrightness(b, x % Chunk.chunkW, y % Chunk.chunkH);
	}

	public Tile getTile(int x, int y) {
		if (!isValidTilePos(x, y))
			return null;
		return chunks[(x / Chunk.chunkW) + (y / Chunk.chunkH) * info.nChunksX].getTile(x % Chunk.chunkW, y % Chunk.chunkH);
	}
	
	public void setTile(Tile t, int x, int y) {
		if (!isValidTilePos(x, y))
			return;
		chunks[(x / Chunk.chunkW) + (y / Chunk.chunkH) * info.nChunksX].setTile(t, x % Chunk.chunkW, y % Chunk.chunkH);
	}

	public void tick() {
		for (int i = 0; i < chunks.length; i++) {
			chunks[i].tick();
		}
	}
}