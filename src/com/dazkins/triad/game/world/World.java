package com.dazkins.triad.game.world;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.entity.Activeatable;
import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.Interactable;
import com.dazkins.triad.game.entity.harvestable.EntityHarvestable;
import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.game.entity.particle.Particle;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.game.world.weather.Weather;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Color;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.math.MathHelper;
import com.dazkins.triad.math.NoiseMap;
import com.dazkins.triad.util.ChunkLoader;
import com.dazkins.triad.util.debugmonitor.DebugMonitor;

public class World {
	public ChunkManager chunkm;
	
	private Color ambientLightLevel;
	
	public float iLightFalloff = 20.0f;

	public ArrayList<Particle> particles = new ArrayList<Particle>();
	public ArrayList<Entity>[] entitiesInTiles;

	private Camera cam;

	private Weather weather;

	protected String pathToLoad;
	
	private ChunkLoader cLoad;
	
	private TimeCycle time;
	
	private ArrayList<Entity> entityRenderQueue;
	private ArrayList<Entity> entityLoadQueue;
	private ArrayList<Entity> tickedEntities;
	
	public NoiseMap worldNoise;
	public NoiseMap treeNoise;

	public void setWeather(Weather w) {
		w.init(this);
		weather = w;
	}
	
	public World() {
		ambientLightLevel = new Color(0);
		
		chunkm = new ChunkManager(this);
		
		entitiesInTiles = new ArrayList[256 * 256];
		for (int i = 0; i < entitiesInTiles.length; i++) {
			entitiesInTiles[i] = new ArrayList<Entity>();
		}
		
		worldNoise = new NoiseMap(0.7f, 8, 1/2048.0f);
		treeNoise = new NoiseMap(0.7f, 8, 1/2048.0f);
		
		cLoad = new ChunkLoader();
		
		entityRenderQueue = new ArrayList<Entity>();
		entityLoadQueue = new ArrayList<Entity>();
		tickedEntities = new ArrayList<Entity>();
		
		time = new TimeCycle(this);
	}

	public void addEntity(Entity e) {
		if (e instanceof Particle)
			addParticle((Particle) e);
		else {
			int tx = (int) (e.getX() / Tile.tileSize);
			int ty = (int) (e.getY() / Tile.tileSize);
			
			try {
				getChunkFromWorldTileCoords(tx, ty).addEntity(e);
				if (entityLoadQueue.contains(e)) {
					entityLoadQueue.remove(e);
				}
			} catch (Exception ex) {
				if (!entityLoadQueue.contains(e)) {
					entityLoadQueue.add(e);
				}
			}
		}
	}
	
	public void addToEntityToRenderQueue(Entity e) {
		entityRenderQueue.add(e);
	}
	
	public Color getAmbientLight() {
		return ambientLightLevel;
	}
	
	public void setAmbientLight(Color c) {
		ambientLightLevel = c;
	}
	
	public void render() {
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		
		ArrayList<Chunk> cs = chunkm.getChunksInAABB(cam.getViewportBounds().shiftX0(-Chunk.chunkS * Tile.tileSize).shiftX1(Chunk.chunkS * Tile.tileSize).shiftY0(-Chunk.chunkS * Tile.tileSize).shiftY1(Chunk.chunkS * Tile.tileSize));
		int regCount = 0;
		
		for (Chunk c : cs) {
			if (!c.isTileMapGenerated()) {
				cLoad.addChunk(c);
			} else {
				if (!c.isVBOGenerated()) {
					c.generateVBO();
					regCount++;
				}
				if (c.getBounds().intersects(cam.getViewportBounds()))
					c.render();
			}
		}
		
		if (regCount != 0)
			DebugMonitor.setVariableValue("Chunk Loads", regCount);
		
		entityRenderQueue.sort(Entity.ySorter);
		for (Entity e : entityRenderQueue) {
			AABB b0 = e.getBoundsForRendering();
			if (b0 != null) {
				if (b0.intersects(cam.getViewportBounds())) {
					e.render(getCam());
				}
			} else {
				e.render(getCam());
			}
		}
		entityRenderQueue.clear();
		
		for (Particle p : particles) {
			p.render();
		}
	}
	
	public void registerEntityTick(Entity e) {
		tickedEntities.add(e);
	}
	
	public boolean entityHasBeenTicked(Entity e) {
		return tickedEntities.contains(e);
	}

	public ArrayList<Entity> getEntitiesInAABB(AABB b) {
		ArrayList<Entity> rValue = new ArrayList<Entity>();
		
		int x0 = ((int) b.getX0() / Tile.tileSize) - 2;
		int y0 = ((int) b.getY0() / Tile.tileSize) - 2;
		int x1 = ((int) b.getX1() / Tile.tileSize) + 2;
		int y1 = ((int) b.getY1() / Tile.tileSize) + 2;
		
		for (int x = x0; x < x1; x++) {
			for (int y = y0; y < y1; y++) {
				ArrayList<Entity> tmp = getEntitesInTile(x, y);
				if (tmp != null) {
					for (Entity e : tmp) {
						if (e.getAABB() != null) {
							if (e.getAABB().intersects(b)) {
								rValue.add(e);
							}
						}
					}
				}
			}
		}
		
		return rValue;
	}

	public Camera getCam() {
		return cam;
	}

	public ArrayList<Entity> getEntitesInTile(int x, int y) {
		Chunk c = getChunkFromWorldTileCoords(x, y);
		if (c != null)
			return c.getEntitiesInTile(convertToChunkX(x), convertToChunkY(y));
		return null;
	}

	public void assignCamera(Camera c) {
		this.cam = c;
	}
	
	public ArrayList<Interactable> getInteractablesInAABB(AABB b) {
		ArrayList<Interactable> rValue = new ArrayList<Interactable>();
		ArrayList<Entity> ents = getEntitiesInAABB(b);
		for (Entity e : ents) {
			if (e instanceof Interactable)
				rValue.add((Interactable) e);
		}
		return rValue;
	}
	
	public ArrayList<Activeatable> getActivatablesInAABB(AABB b) {
		ArrayList<Activeatable> rValue = new ArrayList<Activeatable>();
		ArrayList<Entity> ents = getEntitiesInAABB(b);
		for (Entity e : ents) {
			if (e instanceof Activeatable)
				rValue.add((Activeatable) e);
		}
		return rValue;
	}

	public void sendAttackCommand(AABB b, Mob m, int d, int k) {
		ArrayList<Entity> ents = getEntitiesInAABB(b);
		for (Entity e : ents) {
			if (e != m && e instanceof Mob) {
				Mob tm = (Mob) e;
				tm.hurt(m, d, k);
			}
		}
	}
	
	public void sendHarvestCommand(AABB b, Mob m, int d) {
		ArrayList<Entity> ents = getEntitiesInAABB(b);
		for (Entity e : ents) {
			if (e != m && e instanceof EntityHarvestable) {
				EntityHarvestable h = (EntityHarvestable) e;
				h.harvest(d);
			}
		}
	}

	public void addParticle(Particle p) {
		p.initWorld(this);
		particles.add(p);
	}
	
	public Chunk getChunkFromWorldTileCoords(int x, int y) {
		int cx = 0;
		int cy = 0;
		
		if (x >= 0)
			cx = x /= Chunk.chunkS;
		else
			cx = (int) Math.floor((float) x / (float) Chunk.chunkS);

		if (y >= 0)
			cy = y /= Chunk.chunkS;
		else
			cy = (int) Math.floor((float) y / (float) Chunk.chunkS);
		
		return chunkm.getChunk(cx, cy);
	}
	
	public int convertToChunkX(int x) {
		return MathHelper.betterMod(x, Chunk.chunkS);
	}
	
	public int convertToChunkY(int y) {
		return MathHelper.betterMod(y, Chunk.chunkS);
	}
	
	public Color getTileColor(int x, int y) {
		Chunk c = getChunkFromWorldTileCoords(x, y);
		if (c != null)
			return c.getTileColor(convertToChunkX(x), convertToChunkY(y));
		else
			return new Color(255, 255, 255);
	}

	public Tile getTile(int x, int y) {
		Chunk c = getChunkFromWorldTileCoords(x, y);
		if (c != null)
			return c.getTile(convertToChunkX(x), convertToChunkY(y));
		else
			return null;
	}

	public void setTile(Tile t, int x, int y) {
		Chunk c = chunkm.getChunkWithForceLoad(x / Chunk.chunkS, y / Chunk.chunkS);
		if (c != null)
			chunkm.getChunkWithForceLoad(x / Chunk.chunkS, y / Chunk.chunkS).setTile(t, x % Chunk.chunkS, y % Chunk.chunkS);
	}
	
	public void tick() {
		time.tick();
		
		AABB b = cam.getViewportBounds().shiftX0(4 * -Chunk.chunkS * Tile.tileSize).shiftX1(4 * Chunk.chunkS * Tile.tileSize).shiftY0(4 * -Chunk.chunkS * Tile.tileSize).shiftY1(4 * Chunk.chunkS * Tile.tileSize);
		ArrayList<Chunk> cs = chunkm.getChunksInAABB(b);
		
		for (int i = 0; i < cs.size(); i++) {
			Chunk c = cs.get(i);
			if (c != null)
				c.tick();
		}
		weather.tick();
		
		for (int i = 0; i < cs.size(); i++) {
			Chunk c = cs.get(i);
			if (c != null)
				c.postTick();
		}
		
		for (int i = 0; i < entityLoadQueue.size(); i++) {
			addEntity(entityLoadQueue.get(i));
		}
		
		for (int i = 0; i < particles.size(); i++) {
			Particle p = particles.get(i);
			if (p.needsToBeRemoved()) {
				particles.remove(p);
				continue;
			}
			p.tick();
		}
		
		tickedEntities.clear();
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