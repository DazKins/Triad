package com.dazkins.triad.game.world;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.LightEmitter;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.util.TriadProfiler;

public class Chunk {
	public static int chunkW = 16, chunkH = 16;
	
	private World world;
	
	private int chunkX, chunkY;
	private byte[] tiles;
	private byte[] lightLevel;
	private boolean generated;
	
	private BufferObject tilePlane;
	
	public ArrayList<Entity> entities;
	private ArrayList<Entity> entitiesMarkedForRemoval;
	
	public Chunk(World w, int xp, int yp) {
		this.chunkX = xp;
		this.chunkY = yp;
		this.world = w;
		
		entities = new ArrayList<Entity>();
		entitiesMarkedForRemoval = new ArrayList<Entity>();
		
		lightLevel = new byte[chunkW * chunkH];
		tiles = new byte[chunkW * chunkH];
	}
	
	public void addEntity(Entity e) {
		entities.add(e);
		if (e instanceof LightEmitter) {
			recalculateLighting();
		}
	}
	
	private boolean isValidTilePos(int x, int y) {
		if (x < 0 || y < 0 || x >= chunkW || y >= chunkH) 
			return false;
		return true;
	}
	
	public byte getTileBrightness(int x, int y) {
		if (!isValidTilePos(x, y))
			return 0;
		return lightLevel[x + y * chunkW];
	}
	
	public void setTileBrightness(byte b, int x, int y) {
		if (getTileBrightness(x, y) != b) {
			if (!isValidTilePos(x, y))
				return;
			lightLevel[x + y * chunkW] = b;
			generated = false;
		}
	}
	
	public void setTile(Tile t, int x, int y) {
		if (getTile(x, y) != t) {
			if (!isValidTilePos(x, y))
				return;
			tiles[x + y * chunkW] = t.getID();
			generated = false;
		}
	}
	
	public Tile getTile(int x, int y) {
		if (!isValidTilePos(x, y)) 
			return null;
		return Tile.tiles[tiles[x + y * chunkW]];
	}
	
	public void generate() {
		recalculateLighting();
		tilePlane = new BufferObject(16 * 16 * 4 * 8);
		tilePlane.start();
		tilePlane.bindImage(Image.spriteSheet);
		for (int x = 0; x < chunkW; x++) {
			for (int y = 0; y < chunkH; y++) {
				int tileIndex = tiles[x + y * chunkW];
				if (tileIndex != 0) {
					tilePlane.setBrightness((float) lightLevel[x + y * chunkW] / 14.0f);
					Tile.tiles[tileIndex].render(tilePlane, x * Tile.tileSize + (chunkX * Tile.tileSize * chunkW), y * Tile.tileSize + (chunkY * Tile.tileSize * chunkH));
				}
			}
		}
		tilePlane.stop();
		generated = true;
	}
	
	public void recalculateLighting() {
		for (int i = 0; i < lightLevel.length; i++) {
			lightLevel[i] = world.info.ambientLightLevel;
		}
		ArrayList<Entity> tmpEntities = new ArrayList<Entity>();
		for (int x = chunkX - 1; x <= chunkX + 1; x++) {
			for (int y = chunkY - 1; y <= chunkY + 1; y++) {
				ArrayList<Entity> tmp = world.getChunkEntites(x, y);
				if (tmp != null) {
					for (Object o : tmp)
						tmpEntities.add((Entity) o);
				}
			}
		}
		for (Object o : tmpEntities) {
			if (o instanceof Entity) {
				Entity e = (Entity) o;
				if (e instanceof LightEmitter) {
					LightEmitter le = (LightEmitter) e;
					
					int xx = (int) (e.getX() / Tile.tileSize);
					int yy = (int) (e.getY() / Tile.tileSize);
					
					int cx = xx - (chunkX * chunkW);
					int cy = yy - (chunkY * chunkH);

					byte l = le.getLightStrength();
					
					for (int x = cx - 13; x < cx + 14; x++) {
						for (int y = cy - 13; y < cy + 14; y++) {
							int dx = Math.abs(x - cx);
							int dy = Math.abs(y - cy);
							int dist = (int) Math.sqrt(dx * dx + dy * dy);
							
							int ccx = x + (chunkX * chunkW);
							int ccy = y + (chunkY * chunkH);
							
							byte lightVal = (byte) (l - dist);
							byte cLightVal = world.getTileBrightness(ccx, ccy);
							
							if (cLightVal < lightVal)
								world.setTileBrightness((byte) (lightVal), ccx, ccy);
						}
					}
				}
			}
		}
	}
	
	public AABB getBounds() {
		return new AABB(chunkX * Tile.tileSize * chunkW, chunkY * Tile.tileSize * chunkH, chunkX * Tile.tileSize * chunkW + (chunkW * Tile.tileSize), chunkY * Tile.tileSize * chunkH + (chunkH * Tile.tileSize));
	}
	
	public boolean isGenerated() {
		return generated;
	}
	
	public void tick() {
		for (Object o : entities) {
			if (o instanceof Entity) {
				Entity e = (Entity) o;
				
				e.tick();
				
				int xx = (int) (e.getX() / Tile.tileSize);
				int yy = (int) (e.getY() / Tile.tileSize);
				
				if (xx < chunkX * chunkW || yy < chunkY * chunkH || xx > chunkX * chunkW + chunkW || yy > chunkY * chunkH + chunkH) {
					entitiesMarkedForRemoval.add(e);
					world.addEntity(e);
				}
			}
		}
		for (Object o : entitiesMarkedForRemoval) {
			if (o instanceof Entity) {
				Entity e = (Entity) o;
				entities.remove(e);
			}
		}
		if (entitiesMarkedForRemoval.size() > 0)
			entitiesMarkedForRemoval.clear();
	}
	
	public void renderGrid() {
		int xx0 = chunkX * Tile.tileSize * chunkW;
		int yy0 = chunkY * Tile.tileSize * chunkH;
		int ww0 = chunkW * Tile.tileSize;
		int hh0 = chunkH * Tile.tileSize;
		
		GL11.glLineWidth(4);
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glColor4f(0.8f, 0.8f, 0.8f, 1.0f);
		
		GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2f(xx0, yy0);
			GL11.glVertex2f(xx0 + ww0, yy0);

			GL11.glVertex2f(xx0 + ww0, yy0);
			GL11.glVertex2f(xx0 + ww0, yy0 + hh0);

			GL11.glVertex2f(xx0 + ww0, yy0 + hh0);
			GL11.glVertex2f(xx0, yy0 + hh0);

			GL11.glVertex2f(xx0, yy0 + hh0);
			GL11.glVertex2f(xx0, yy0);
		GL11.glEnd();
		
		GL11.glLineWidth(0.002f);
		
		for (int x = 0; x < chunkW; x++) {
			for (int y = 0; y < chunkH; y++) {
				int xx1 = xx0 + x * Tile.tileSize;
				int yy1 = yy0 + y * Tile.tileSize;
				
				GL11.glBegin(GL11.GL_LINES);
					GL11.glVertex2f(xx1, yy1);
					GL11.glVertex2f(xx1 + Tile.tileSize, yy1);

					GL11.glVertex2f(xx1, yy1);
					GL11.glVertex2f(xx1, yy1 + Tile.tileSize);
				GL11.glEnd();
			}
		}
		
		GL11.glColor4f(1, 1, 1, 1);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public void render() {
		tilePlane.render();
		for (Object e : entities) {
			((Entity)e).render();
		}
	}
}