package com.dazkins.triad.game.world;

import java.util.ArrayList;

import com.dazkins.triad.game.entity.Activeatable;
import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.Interactable;
import com.dazkins.triad.game.entity.harvestable.EntityHarvestable;
import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.game.world.weather.Weather;
import com.dazkins.triad.game.world.worldgen.WorldGen;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Color;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.math.MathHelper;
import com.dazkins.triad.util.ChunkLoader;

public class World {
	public ServerChunkManager chunkm;
	
	private Color ambientLightLevel;
	
	public float iLightFalloff = 20.0f;

	private Camera cam;

	private Weather weather;

	protected String pathToLoad;
	
	private ChunkLoader cLoad;
	
	private TimeCycle time;
	
	private ArrayList<Entity> entityLoadQueue;
	private ArrayList<Entity> tickedEntities;
	
	private WorldGen worldGenerator;
	
	public World() {
		ambientLightLevel = new Color(126, 126, 126);
		
		chunkm = new ServerChunkManager(this);
		
		worldGenerator = new WorldGen(this);
		
		cLoad = new ChunkLoader();
		
		entityLoadQueue = new ArrayList<Entity>();
		tickedEntities = new ArrayList<Entity>();
		
		time = new TimeCycle(this);
	}
	
	public WorldGen getWorldGenerator() {
		return worldGenerator;
	}

	public void setWeather(Weather w) {
		w.init(this);
		weather = w;
	}

	public void addEntity(Entity e) {
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
	
	public Color getAmbientLight() {
		return ambientLightLevel;
	}
	
	public void setAmbientLight(Color c) {
		ambientLightLevel = c;
	}
	
	public ArrayList<Entity> getLoadedEntities() {
		ArrayList<Chunk> lc = chunkm.getLoadedChunks();
		ArrayList<Entity> ents = new ArrayList<Entity>();
		for (Chunk c : lc) {
			ents.addAll(c.getEntities());
		}
		return ents;
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
			return c.getEntitiesInTile(MathHelper.convertToChunkTileX(x), MathHelper.convertToChunkTileY(y));
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
	
	public Chunk getChunkWithForceLoad(int x, int y) {
		return chunkm.getChunkWithForceLoad(x, y);
	}
	
	public void forceChunkTileMapLoad(int x, int y) {
		chunkm.getChunkWithForceLoad(x, y).addToLoader(cLoad);
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
	
	public Chunk getChunkFromWorldTileCoords(int x, int y) {
		int cx = MathHelper.getChunkXFromTileX(x);
		int cy = MathHelper.getChunkYFromTileY(y);
		
		return chunkm.getChunk(cx, cy);
	}
	
	public Color getTileColor(int x, int y) {
		Chunk c = getChunkFromWorldTileCoords(x, y);
		if (c != null)
			return c.getTileColor(MathHelper.convertToChunkTileX(x), MathHelper.convertToChunkTileY(y));
		else
			return new Color(255, 255, 255);
	}

	public Tile getTile(int x, int y) {
		Chunk c = getChunkFromWorldTileCoords(x, y);
		if (c != null)
			return c.getTile(MathHelper.convertToChunkTileX(x), MathHelper.convertToChunkTileY(y));
		return null;
	}

	public void setTile(Tile t, int x, int y) {
		Chunk c = getChunkFromWorldTileCoords(x, y);
		if (c != null)
			c.setTile(t, MathHelper.convertToChunkTileX(x), MathHelper.convertToChunkTileY(y));
	}
	
	public void tick() {
		time.tick();
		
		ArrayList<Chunk> cs = chunkm.getLoadedChunks();
		
		for (int i = 0; i < cs.size(); i++) {
			Chunk c = cs.get(i);
			if (c != null)
				c.tick();
		}
		
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
}