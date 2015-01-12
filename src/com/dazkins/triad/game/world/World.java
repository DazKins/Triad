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
import com.dazkins.triad.util.Loadable;
import com.dazkins.triad.util.Loader;

public class World {
	private static ArrayList<World> worlds;

	public ChunkManager chunkm;
	public int ambientLightLevel = 120;
	public float iLightFalloff = 20.0f;

	public ArrayList<Particle> particles = new ArrayList<Particle>();
	public ArrayList<Entity>[] entitiesInTiles;
	public ArrayList<Entity> entities;

	private Camera cam;

	private Weather weather;

	protected String pathToLoad;
	
	private int tickCount;

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
		
		worldNoise = new NoiseMap(0.24f, 32, 1/1024.0f);
		
		entities = new ArrayList<Entity>();
	}
	
	public NoiseMap worldNoise;

	public void addEntity(Entity e) {
		if (e.getX() < 0 || e.getY() < 0)
			return;

		if (e instanceof Particle)
			addParticle((Particle) e);
		else {
			int tx = (int) e.getX() >> 5;
			int ty = (int) e.getY() >> 5;
			
			try {
//				entitiesInTiles[tx + ty * nChunksX * Chunk.chunkW].add(e);
			} catch (Exception ex) { }
			
			entities.add(e);
		}
	}

	public void render() {
		GL11.glColor3f(1.0f, 1.0f, 1.0f);

		ArrayList<Chunk> cs = chunkm.getChunksInAABB(cam.getViewportBounds());
		for (Chunk c : cs) {
			if (!c.isVBOGenerated()) {
				c.generateVBO();
			}
			if (!c.isTileMapGenerated()) {
				Loader l = new Loader();
				c.addToLoader(l);
				l.beginLoading();
			} else {
				c.render();
			}
		}
		
//		entities.sort(Entity.ySorter);
//		for (Entity e : entities) {
//			AABB b = e.getBoundsForRendering();
//			if (b != null) {
//				if (b.intersects(cam.getViewportBounds()))
//					e.render();
//			} else {
//				e.render();
//			}
//		}
		
//		for (Particle p : particles) {
//			if (p.getAABB().intersects(cam.getViewportBounds()))
//				p.render();
//		}
	}

	//TODO : Reimplement
//	public void renderGrid() {
//		for (int i = 0; i < chunks.length; i++) {
//			if (chunks[i].getBounds().intersects(cam.getViewportBounds())) {
//				chunks[i].renderGrid();
//			}
//		}
//	}

	public ArrayList<Entity> getEntitiesInAABB(AABB b) {
		ArrayList<Entity> rValue = new ArrayList<Entity>();
		
		int x0 = ((int) b.getX0() >> 5) - 2;
		int y0 = ((int) b.getY0() >> 5) - 2;
		int x1 = ((int) b.getX1() >> 5) + 2;
		int y1 = ((int) b.getY1() >> 5) + 2;
		
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

	public Camera getCam() {
		return cam;
	}

	public ArrayList<Entity> getEntitesInTile(int x, int y) {
		return entities;
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
			cx = x /= Chunk.chunkW;
		else
			cx = (int) Math.floor((float) x / (float) Chunk.chunkW);

		if (y >= 0)
			cy = y /= Chunk.chunkH;
		else
			cy = (int) Math.floor((float) y / (float) Chunk.chunkH);
		
		return chunkm.getChunk(cx, cy);
	}
	
	public int convertToChunkX(int x) {
		if (x < 0)
			return (Chunk.chunkW - (Math.abs(x) % Chunk.chunkW)) & 15;
		else
			return x % Chunk.chunkW;
	}
	
	public int convertToChunkY(int y) {
		if (y < 0)
			return (Chunk.chunkH - (Math.abs(y) % Chunk.chunkH)) & 15;
		else
			return y % Chunk.chunkH;
	}
	
	public Color getTileColor(int x, int y) {
		Chunk c = getChunkFromWorldTileCoords(x, y);
		if (c != null)
			return c.getTileColor(convertToChunkX(x), convertToChunkY(y));
		else
			return new Color(255, 255, 255);
	}
	
	public void blendTileColor(Color c, int x, int y) {
		Chunk ch = chunkm.getChunk(x / Chunk.chunkW, y / Chunk.chunkW);
		if (ch != null)
			chunkm.getChunk(x / Chunk.chunkW, y / Chunk.chunkW).blendTileColor(c, x % Chunk.chunkW, y % Chunk.chunkH);
	}

	public Tile getTile(int x, int y) {
		Chunk c = getChunkFromWorldTileCoords(x, y);
		if (c != null)
			return c.getTile(convertToChunkX(x), convertToChunkY(y));
		else
			return null;
	}

	public void setTile(Tile t, int x, int y) {
		Chunk c = chunkm.getChunk(x / Chunk.chunkW, y / Chunk.chunkW);
		if (c != null)
			chunkm.getChunk(x / Chunk.chunkW, y / Chunk.chunkW).setTile(t, x % Chunk.chunkW, y % Chunk.chunkH);
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
		for (int i = 0; i < cs.size(); i++) {
			cs.get(i).tick();
		}
//		weather.tick();
		
		if (tickCount % 500 == 0)
			System.gc();
		
		tickCount++;
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