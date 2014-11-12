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
	public ArrayList<Entity> entities = new ArrayList<Entity>();

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

		this.generate();
		
		int tw = this.nChunksX * Chunk.chunkW;

		for (int i = 2; i < l.getSize(); i++) {
			int t = l.getInt(i);
			int x = (i - 2) / tw;
			int y = (i - 2) % tw;
			this.setTile(Tile.tiles[t], x, y);
		}
	}
	
	public ArrayList<Entity> getEntities() {
		return entities;
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
		
		entities.add(e);
		
		int i = 0;
		if (e instanceof LightEmitter) {
			int xx = (int) e.getX() >> 9;
			int yy = (int) e.getY() >> 9;
			for (int x = xx - 1; x < xx + 2; x++) {
				for (int y = yy - 1; y < yy + 2; y++) {
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
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			AABB a = e.getAABB();
			if (a != null) {
				if (e.getAABB().intersects(b))
					rValue.add(e);
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

	public Camera getCam() {
		return cam;
	}

	public ArrayList<Entity> getEntitesInTile(int x, int y) {
		ArrayList<Entity> rValue = new ArrayList<Entity>();
		if (x < 0 || y < 0 || x >= nChunksX || y >= nChunksY)
			return null;
		for (int i = 0; i < entities.size(); i++) {
			if (new AABB((int) x << 5, (int) y << 5, 32, 32).intersects(entities.get(i).getAABB()))
				rValue.add(entities.get(i));
				
		}
		return rValue;
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
		p.initWorld(this);
		particles.add(p);
	}

	public boolean isValidTilePos(int x, int y) {
		return (x >= 0 && x < nChunksX * Chunk.chunkW)
				&& (y >= 0 && y < nChunksY * Chunk.chunkH);
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
		for (int i = 0; i < particles.size(); i++) {
			if (particles.get(i).needDestruction())
				particles.remove(i);
			else
				particles.get(i).tick();
		}
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i).needsToBeRemoved())
				entities.remove(i);
			else
				entities.get(i).tick();
		}

		weather.tick();
	}
}