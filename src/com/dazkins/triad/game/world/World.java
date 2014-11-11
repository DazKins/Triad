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
import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.game.entity.particle.Particle;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.game.world.weather.Weather;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.math.AABB;

public class World {
	private static ArrayList<World> worlds;

	private Chunk[] chunks;

	public WorldInfo info;
	
	public ArrayList<Particle> particles = new ArrayList<Particle>();
	
	private Camera cam;
	
	private Weather weather;
	
	public static TestWorld testWorld = new TestWorld();

	public WorldInfo getInfo() {
		return info;
	}
	
	public void setWeather(Weather w) {
		w.init(this);
		weather = w;
	}

	public void loadDataFromFile(String p) {
		ListFile l = null;
		SingleLineDatabaseFile metaDatabase = null;
		try {
			l = new ListFile("res/data/worlds/" + p + "_data.lt", " ");
			metaDatabase = new SingleLineDatabaseFile("res/data/worlds/" + p
					+ "_metadata.db");
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.info = new WorldInfo();
		this.info.loadFromDatabase(metaDatabase);

		this.generate();

		int tw = this.info.nChunksX * Chunk.chunkW;

		for (int i = 0; i < l.getSize(); i++) {
			int t = l.getInt(i);
			int x = i / tw;
			int y = i % tw;
			System.out.println(t);
			this.setTile(Tile.tiles[t], x, y);
		}
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

	public void render() {
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		for (int x = info.nChunksX - 1; x >= 0; x--) {
			for (int y = info.nChunksY - 1; y >= 0; y--) {
				if (!chunks[x + y * info.nChunksY].isGenerated()) {
					chunks[x + y * info.nChunksY].generate();
				}
				if (chunks[x + y * info.nChunksY].getBounds().intersects(cam.getViewportBounds())) {
					chunks[x + y * info.nChunksY].render();
				}
			}
		}
		List<Entity> entitiesToRender = new ArrayList<Entity>();
		for (int i = 0; i < chunks.length; i++) {
			entitiesToRender.addAll(chunks[i].getChunkEntities());
		}
		entitiesToRender.sort(Entity.ySorter);
		for (Entity e : entitiesToRender) {
			e.render();
		}
		for (Particle p : particles) {
			if (p.getAABB().intersects(cam.getViewportBounds()))
				p.render();
		}
	}

	public void renderGrid() {
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
	
	public Chunk[] getChunks() {
		return chunks;
	}
	
	public Camera getCam() {
		return cam;
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
	
	public void assignCamera(Camera c) {
		this.cam = c;
	}

	public void sendAttackCommand(int damage, AABB b, Entity e) {
		ArrayList<Entity> ents = getEntitiesInAABB(b);
		for (int i = 0; i < ents.size(); i++) {
			Entity e0 = ents.get(i);
			if (e0 != e && e0 instanceof Mob) {
				Mob m = (Mob) e0;
				m.hurt(damage);
			}
		}
	}
	
	public void addParticle(Particle p) {
		particles.add(p);
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
		getChunkFromWorldTileCoords(x, y).setTile(t, x % Chunk.chunkW, y % Chunk.chunkH);
	}

	public void tick() {
		for (int i = 0; i < chunks.length; i++) {
			chunks[i].tick();
		}
		for (int i = 0; i < particles.size(); i++) {
			if (particles.get(i).needDestruction())
				particles.remove(particles.get(i));
			else
				particles.get(i).tick();
		}
		
		weather.tick();
	}
}