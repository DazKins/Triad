package com.dazkins.triad.game.world;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.file.ListFile;
import com.dazkins.triad.file.MultiLineDatabaseFile;
import com.dazkins.triad.file.SingleLineDatabaseFile;
import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.particle.Particle;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.util.TriadProfiler;

public class World {
	private static ArrayList<World> worlds;
	private static Map<String, Integer> mapToIndex;

	private static MultiLineDatabaseFile globalWorldDatabase = null;

	private Chunk[] chunks;

	public WorldInfo info;
	
	public ArrayList<Particle> particles = new ArrayList<Particle>();

	public static void init() {
		try {
			globalWorldDatabase = new MultiLineDatabaseFile(
					"res/data/worlds.db");
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
			metaDatabase = new SingleLineDatabaseFile("res/data/worlds/" + p
					+ "_metadata.db");
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

		if (e instanceof Particle) {
			particles.add((Particle) e);
		} else {
			int cx = (int) ((e.getX() / Tile.tileSize) / Chunk.chunkW);
			int cy = (int) ((e.getY() / Tile.tileSize) / Chunk.chunkH);

			if (cx >= 0 && cy >= 0 && cx < info.nChunksX && cy < info.nChunksY)
				chunks[cx + cy * info.nChunksX].addEntity(e);
		}
	}

	public void render(Camera cam) {
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		for (Particle p : particles) {
			p.render();
		}
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

	public ArrayList<Entity> getEntitiesInAABB(AABB b) {
		ArrayList<Entity> rValue = new ArrayList<Entity>();
		for (int i = 0; i < chunks.length; i++) {
			if (chunks[i].getBounds().intersects(b)) {
				ArrayList<Entity> cChunkEntities = chunks[i].getChunkEntities();
				for (int u = 0; u < cChunkEntities.size(); u++) {
					Entity cEntity = cChunkEntities.get(u);
					AABB eB = cEntity.getAABB();
					if (eB != null) {
						if (eB.intersects(b) && !rValue.contains(cEntity)) {
							rValue.add(cEntity);
						}
					}
				}
			}
		}
		return rValue;
	}

	public Chunk getChunkFromWorldTileCoords(int x, int y) {
		if (!isValidTilePos(x, y))
			return null;
		return chunks[(x / Chunk.chunkW) + (y / Chunk.chunkH) * info.nChunksX];
	}

	public ArrayList<Entity> getEntitiesInChunk(int x, int y) {
		if (x < 0 || y < 0 || x >= info.nChunksX || y >= info.nChunksY)
			return null;
		return chunks[x + y * info.nChunksY].getChunkEntities();
	}

	public ArrayList<Entity> getEntitesInTile(int x, int y) {
		if (x < 0 || y < 0 || x >= info.nChunksX || y >= info.nChunksY)
			return null;
		return getChunkFromWorldTileCoords(x, y).getEntitiesInTile(x % Chunk.chunkW, y % Chunk.chunkH);
	}

	public void sendAttackCommand(int damage, int x, int y) {
		if (!isValidTilePos(x, y))
			return;
		getChunkFromWorldTileCoords(x, y).sendAttackCommand(damage, x & 15, y & 15);
	}

	private boolean isValidTilePos(int x, int y) {
		return (x >= 0 && x < info.nChunksX * Chunk.chunkW)
				&& (y >= 0 && y < info.nChunksY * Chunk.chunkH);
	}

	public byte getTileBrightness(int x, int y) {
		if (!isValidTilePos(x, y))
			return 0;
		return getChunkFromWorldTileCoords(x, y).getTileBrightness(
				x % Chunk.chunkW, y % Chunk.chunkH);
	}

	public void setTileBrightness(byte b, int x, int y) {
		if (!isValidTilePos(x, y))
			return;
		getChunkFromWorldTileCoords(x, y).setTileBrightness(b,
				x % Chunk.chunkW, y % Chunk.chunkH);
	}

	public Tile getTile(int x, int y) {
		if (!isValidTilePos(x, y))
			return null;
		return getChunkFromWorldTileCoords(x, y).getTile(x % Chunk.chunkW,
				y % Chunk.chunkH);
	}

	public void setTile(Tile t, int x, int y) {
		if (!isValidTilePos(x, y))
			return;
		getChunkFromWorldTileCoords(x, y).setTile(t, x % Chunk.chunkW,
				y % Chunk.chunkH);
	}

	public void tick() {
		for (int i = 0; i < chunks.length; i++) {
			chunks[i].tick();
		}
		for (Particle p : particles) {
			p.tick();
		}
	}
}