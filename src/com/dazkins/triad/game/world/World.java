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
		mapToIndex.put(w.info.getName(), i);
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
		
		int tw = rValue.info.getnChunksX() * Chunk.chunkW;
		
		for (int i = 0; i < l.getSize(); i++) {
			int t = l.getInt(i);
			int x = i % tw;
			int y = i / tw;
			rValue.setTile(Tile.tiles[t], x, y);
		}
		
		return rValue;
	}
	
	public void saveWorldToFile(String path) throws FileNotFoundException {
		File f = new File(path);
		PrintWriter pr = new PrintWriter(f);
		String output = "";
		for (int x = 0; x < info.getnChunksX() * Chunk.chunkW; x++) {
			for (int y = 0; y < info.getnChunksY() * Chunk.chunkH; y++) {
				output += getTile(x, y).getID() + " ";
			}
		}
		pr.write(output);
		pr.close();
		
		pr = null;
		output = null;
		f = null;
		System.gc();
	}
	
	private void generate() {
		chunks = new Chunk[info.getnChunksX() * info.getnChunksY()];
		for (int x = 0; x < info.getnChunksX(); x++) {
			for (int y = 0; y < info.getnChunksY(); y++) {
				chunks[x + y * info.getnChunksX()] = new Chunk(this, x, y);
			}
		}
	}

	public void addEntity(Entity e) {
		if (e.getX() < 0 || e.getY() < 0)
			return;
		
		int cx = (int) ((e.getX() / (float)Tile.tileSize) / (float)Chunk.chunkW);
		int cy = (int) ((e.getY() / (float)Tile.tileSize) / (float)Chunk.chunkH);
		
		if (cx >= 0 && cy >= 0 && cx < info.getnChunksX() && cy < info.getnChunksY())
			chunks[cx + cy * info.getnChunksX()].addEntity(e);
	}

	public void render(Camera cam) {
		System.out.println(info.getnChunksX() * Chunk.chunkW * info.getnChunksY() * Chunk.chunkH);
		for (int i = 0; i < chunks.length; i++) {
			if (chunks[i].getBounds().intersects(cam.getViewportBounds())) {
				if (!chunks[i].isGenerated()) {
					chunks[i].generate();
				} 
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
		if (cx < 0 || cy < 0 || cx >= info.getnChunksX() || cy >= info.getnChunksY())
			return null;
		return chunks[cx + cy * info.getnChunksX()].entities;
	}
	
	private boolean isValidTilePos(int x, int y) {
		return (x >= 0 && x < info.getnChunksX() * Chunk.chunkW) && (y >= 0 && y < info.getnChunksY() * Chunk.chunkH);
	}
	
	public byte getTileBrightness(int x, int y) {
		if (!isValidTilePos(x, y))
			return 0;
		return chunks[(x / Chunk.chunkW) + (y / Chunk.chunkH) * info.getnChunksX()].getTileBrightness(x % Chunk.chunkW, y % Chunk.chunkH);
	}
	
	public void setTileBrightness(byte b, int x, int y) {
		if (!isValidTilePos(x, y))
			return;
		chunks[(x / Chunk.chunkW) + (y / Chunk.chunkH) * info.getnChunksX()].setTileBrightness(b, x % Chunk.chunkW, y % Chunk.chunkH);
	}

	public Tile getTile(int x, int y) {
		if (!isValidTilePos(x, y))
			return null;
		return chunks[(x / Chunk.chunkW) + (y / Chunk.chunkH) * info.getnChunksX()].getTile(x % Chunk.chunkW, y % Chunk.chunkH);
	}
	
	public void setTile(Tile t, int x, int y) {
		if (!isValidTilePos(x, y))
			return;
		chunks[(x / Chunk.chunkW) + (y / Chunk.chunkH) * info.getnChunksX()].setTile(t, x % Chunk.chunkW, y % Chunk.chunkH);
	}

	public void tick() {
		for (int i = 0; i < chunks.length; i++) {
			chunks[i].tick();
		}
	}
}