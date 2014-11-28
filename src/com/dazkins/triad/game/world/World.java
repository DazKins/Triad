package com.dazkins.triad.game.world;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.file.ListFile;
import com.dazkins.triad.file.SingleLineDatabaseFile;
import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.LightEmitter;
import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.game.entity.particle.Particle;
import com.dazkins.triad.game.inventory.item.equipable.weapon.ItemWeapon;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.game.world.weather.Weather;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.util.Loadable;

public abstract class World implements Loadable {
	private static ArrayList<World> worlds;

	private Chunk[] chunks;

	public int nChunksX;
	public int nChunksY;
	public byte ambientLightLevel;

	public ArrayList<Particle> particles = new ArrayList<Particle>();
	public ArrayList<Entity>[] entitiesInTiles;
	public ArrayList<Entity> entities;

	private Camera cam;

	private Weather weather;

	protected String pathToLoad;

	public static WorldTest testWorld = new WorldTest();

	public void setWeather(Weather w) {
		w.init(this);
		weather = w;
	}

	public void load() {
		loadDataFromFile(pathToLoad);
	}

	public void loadDataFromFile(String p) {
		ListFile l = null;
		SingleLineDatabaseFile metaDatabase = null;
		try {
			l = new ListFile("res/data/worlds/" + p + "_data.lt", " ");
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.nChunksX = l.getInt(0);
		this.nChunksY = l.getInt(1);
		
		entitiesInTiles = new ArrayList[nChunksX * Chunk.chunkW * nChunksY * Chunk.chunkH];
		for (int i = 0; i < entitiesInTiles.length; i++) {
			entitiesInTiles[i] = new ArrayList<Entity>();
		}
		
		entities = new ArrayList<Entity>();
		
		this.generate();
		
		int tw = this.nChunksX * Chunk.chunkW;

		for (int i = 2; i < l.getSize(); i++) {
			int t = l.getInt(i);
			int x = (i - 2) / tw;
			int y = (i - 2) % tw;
			this.setTile(Tile.tiles[t], x, y);
		}
	}

	private void generate() {
		chunks = new Chunk[nChunksX * nChunksY];
		for (int x = 0; x < nChunksX; x++) {
			for (int y = 0; y < nChunksY; y++) {
				chunks[x + y * nChunksX] = new Chunk(this, x, y);
			}
		}
	}

	public void addEntity(Entity e) {
		if (e.getX() < 0 || e.getY() < 0)
			return;

		if (e instanceof Particle)
			addParticle((Particle) e);
		
		int tx = (int) e.getX() >> 5;
		int ty = (int) e.getY() >> 5;
		
		entitiesInTiles[tx + ty * nChunksX * Chunk.chunkW].add(e);
		entities.add(e);
		
		if (e instanceof LightEmitter) {
			int cx = (int) e.getX() >> 9;
			int cy = (int) e.getY() >> 9;
			for (int x = cx - 1; x < cx + 2; x++) {
				for (int y = cy - 1; y < cy + 2; y++) {
					if (x >= 0 && y >= 0 && x < nChunksX && y < nChunksY) {
						Chunk c = chunks[x + y * nChunksX];
						c.recalculateLighting();
					}
				}
			}
		}
	}

	public void render() {
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		for (int x = nChunksX - 1; x >= 0; x--) {
			for (int y = nChunksY - 1; y >= 0; y--) {
				if (!chunks[x + y * nChunksY].isGenerated()) {
					chunks[x + y * nChunksY].generate();
				}
				if (chunks[x + y * nChunksY].getBounds().intersects(
						cam.getViewportBounds())) {
					chunks[x + y * nChunksY].render();
				}
			}
		}
		
		entities.sort(Entity.ySorter);
		for (Entity e : entities) {
			AABB b = e.getAABB();
			if (b != null) {
				if (e.getAABB().intersects(cam.getViewportBounds()))
					e.render();
			}
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
		
		int x0 = ((int) b.getX0() >> 5) - 1;
		int y0 = ((int) b.getY0() >> 5) - 1;
		int x1 = ((int) b.getX1() >> 5) + 1;
		int y1 = ((int) b.getY1() >> 5) + 1;
		
		for (int x = x0; x < x1; x++) {
			for (int y = y0; y < y1; y++) {
				ArrayList<Entity> tmp = getEntitesInTile(x, y);
				for (Entity e : tmp) {
					if (e.getAABB() != null) {
						if (e.getAABB().intersects(b)) {
							rValue.add(e);
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
		return chunks[(x / Chunk.chunkW) + (y / Chunk.chunkH) * nChunksX];
	}

	public Chunk[] getChunks() {
		return chunks;
	}
	
	public float getW() {
		return nChunksX * Chunk.chunkW * Tile.tileSize; 
	}
	
	public float getH() {
		return nChunksY * Chunk.chunkH * Tile.tileSize;
	}

	public Camera getCam() {
		return cam;
	}

	public ArrayList<Entity> getEntitesInTile(int x, int y) {
		if (x < 0 || y < 0 || x >= nChunksX * Chunk.chunkW || y >= nChunksY * Chunk.chunkH)
			return new ArrayList<Entity>();
		
		return entitiesInTiles[x + y * nChunksX * Chunk.chunkW];
	}

	public void assignCamera(Camera c) {
		this.cam = c;
	}

	public void sendAttackCommand(AABB b, Mob m, int d, int k) {
		ArrayList<Entity> ents = getEntitiesInAABB(b);
		for (int i = 0; i < ents.size(); i++) {
			Entity e = ents.get(i);
			if (e != m && e instanceof Mob) {
				Mob tm = (Mob) e;
				tm.hurt(m, d, k);
			}
		}
	}

	public void addParticle(Particle p) {
		p.initWorld(this);
		particles.add(p);
	}

	public boolean isValidTilePos(int x, int y) {
		return (x >= 0 && x < nChunksX * Chunk.chunkW) && (y >= 0 && y < nChunksY * Chunk.chunkH);
	}

	public byte getTileBrightness(int x, int y) {
		if (!isValidTilePos(x, y))
			return 0;
		return getChunkFromWorldTileCoords(x, y).getTileBrightness(x % Chunk.chunkW, y % Chunk.chunkH);
	}

	public void setTileBrightness(byte b, int x, int y) {
		if (!isValidTilePos(x, y))
			return;
		getChunkFromWorldTileCoords(x, y).setTileBrightness(b, x % Chunk.chunkW, y % Chunk.chunkH);
	}

	public Tile getTile(int x, int y) {
		if (!isValidTilePos(x, y))
			return null;
		return getChunkFromWorldTileCoords(x, y).getTile(x % Chunk.chunkW, y % Chunk.chunkH);
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
				particles.remove(i);
			else
				particles.get(i).tick();
		}
		for (int i = 0; i < entitiesInTiles.length; i++) {
			ArrayList<Entity> tmp = entitiesInTiles[i];
			for (int u = 0; u < tmp.size(); u++) {
				Entity e = tmp.get(u);
				if (e.needsToBeRemoved()) {
					entitiesInTiles[i].remove(e);
					entities.remove(e);
				}
				else {
					int tx0 = (int) e.getX() >> 4;
					int ty0 = (int) e.getY() >> 4;
					
					e.tick();
					
					int tx1 = (int) e.getX() >> 4;
					int ty1 = (int) e.getY() >> 4;
					
					if (tx0 != tx1 || ty0 != ty1) {
						entities.remove(e);
						entitiesInTiles[i].remove(e);
						
						addEntity(e);
					}
				}
			}
		}

		weather.tick();
	}

	public ArrayList<Entity> getEntities() {
		ArrayList<Entity> rValue = new ArrayList<Entity>();
		for (int i = 0; i < entitiesInTiles.length; i++) {
			ArrayList<Entity> tmp = entitiesInTiles[i];
			for (Entity e : tmp) {
				rValue.add(e);
			}
		}
		return rValue;
	}
}