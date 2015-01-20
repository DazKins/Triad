package com.dazkins.triad.game.world;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.file.ListFile;
import com.dazkins.triad.game.entity.Activeatable;
import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.game.entity.particle.Particle;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.game.world.weather.Weather;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Color;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.math.NoiseMap;
import com.dazkins.triad.math.PerlinNoise;
import com.dazkins.triad.util.ChunkLoader;
import com.dazkins.triad.util.Loadable;
import com.dazkins.triad.util.Loader;

public class World {
	private static ArrayList<World> worlds;

	public ChunkManager chunkm;
	public int ambientLightLevel = 40;
	public float iLightFalloff = 20.0f;

	public ArrayList<Particle> particles = new ArrayList<Particle>();
	public ArrayList<Entity>[] entitiesInTiles;

	private Camera cam;

	private Weather weather;

	protected String pathToLoad;
	
	private int tickCount;
	
	private ChunkLoader cLoad;
	
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
				System.err.println("Failed to add " + e);
				if (!entityLoadQueue.contains(e)) {
					entityLoadQueue.add(e);
				}
			}
		}
	}
	
	public void addToEntityRenderQueue(Entity e) {
		entityRenderQueue.add(e);
	}
	
	public void render() {
		GL11.glColor3f(1.0f, 1.0f, 1.0f);

		AABB b = cam.getViewportBounds().shiftX0(-Chunk.chunkS).shiftX1(Chunk.chunkS).shiftY0(-Chunk.chunkS).shiftY1(Chunk.chunkS);
		ArrayList<Chunk> cs = chunkm.getChunksInAABB(b);
		
//		System.out.println(cs.size() + " renders");
		for (Chunk c : cs) {
			if (!c.isVBOGenerated()) {
				c.generateVBO();
			}
			if (!c.isTileMapGenerated()) {
				cLoad.addChunk(c);
			} else if (c.getBounds().intersects(cam.getViewportBounds())) {
				c.render();
			}
		}
		
		entityRenderQueue.sort(Entity.ySorter);
		for (Entity e : entityRenderQueue) {
			AABB b0 = e.getBoundsForRendering();
			if (b0 != null) {
				if (b0.intersects(cam.getViewportBounds())) {
					e.render(cam);
				}
			} else {
				e.render(cam);
			}
		}
		entityRenderQueue.clear();
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
		if (x < 0)
			return (Chunk.chunkS - (Math.abs(x) % Chunk.chunkS)) & (Chunk.chunkS - 1);
		else
			return x % Chunk.chunkS;
	}
	
	public int convertToChunkY(int y) {
		if (y < 0)
			return (Chunk.chunkS - (Math.abs(y) % Chunk.chunkS)) & (Chunk.chunkS - 1);
		else
			return y % Chunk.chunkS;
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

	private ArrayList<Entity> ticked = new ArrayList<Entity>();
	
	public void tick() {
//		for (int i = 0; i < particles.size(); i++) {
//			if (particles.get(i).needDestruction())
//				particles.remove(i);
//			else
//				particles.get(i).tick();
//		}
		
//		ticked = new ArrayList<Entity>();
//		for (int i = 0; i < entitiesInTiles.length; i++) {
//			ArrayList<Entity> tmp = entitiesInTiles[i];
//			for (int u = 0; u < tmp.size(); u++) {
//				Entity e = tmp.get(u);
//				if (e.needsToBeRemoved()) {
//					entitiesInTiles[i].remove(e);
//					entities.remove(e);
//				} else {
//					int tx0 = (int) e.getX() >> 5;
//					int ty0 = (int) e.getY() >> 5;
//					
//					if (!ticked.contains(e)) {
//						e.tick();
//					}
//					
//					ticked.add(e);
//					
//					int tx1 = (int) e.getX() >> 5;
//					int ty1 = (int) e.getY() >> 5;
//		
//					if (tx0 != tx1 || ty0 != ty1) {
//						entities.remove(e);
//						entitiesInTiles[i].remove(e);
//						
//						addEntity(e);
//						u--;
//					}
//				}
//			}
//		}
		ArrayList<Chunk> cs = chunkm.getLoadedChunks();
		
//		System.out.println(cs.size() + " ticks");
		
		for (int i = 0; i < cs.size(); i++) {
			Chunk c = cs.get(i);
			if (c != null)
				c.tick();
		}
//		weather.tick();
		
		tickCount++;
		
		for (int i = 0; i < cs.size(); i++) {
			Chunk c = cs.get(i);
			if (c != null)
				c.postTick();
		}
		
		for (int i = 0; i < entityLoadQueue.size(); i++) {
			addEntity(entityLoadQueue.get(i));
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