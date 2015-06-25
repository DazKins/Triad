package com.dazkins.triad.game.world;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.EntityFlower;
import com.dazkins.triad.game.entity.harvestable.EntityTree;
import com.dazkins.triad.game.entity.mob.EntityZombie;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Color;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.util.ChunkLoader;
import com.dazkins.triad.util.Loadable;

public class Chunk implements Loadable {
	public static int chunkS = 16;
	public static int chunkSS = chunkS * chunkS;
	
	private static float lFadeOut = 0.85f;

	private World world;

	public int chunkX, chunkY;
	public int rChunkX, rChunkY;
	private int[] tiles;
	
	private Color[] tileColors;
	
	//Store light value from the previous frame
	private Color[] pTileColors;
	
	private boolean vboGenerated;
	private boolean tilesGenerated;
	
	private boolean isBeingLoaded;

	private BufferObject tilePlane;
	
	private ArrayList<Entity>[] entitiesInTiles;
	private int entityCount;

	public Chunk(World w, int xp, int yp) {
		this.chunkX = xp;
		this.chunkY = yp;
		this.world = w;
		
		this.rChunkX = chunkX * chunkS;
		this.rChunkY = chunkY * chunkS;

		tileColors = new Color[chunkSS];
		pTileColors = new Color[chunkSS];
		
		resetLightLevels();
		
		tiles = new int[chunkSS];
		entitiesInTiles = new ArrayList[chunkSS]; 
		for (int i = 0; i < chunkSS; i++) {
			entitiesInTiles[i] = new ArrayList<Entity>();
		}
	}
	
	public void addToLoader(ChunkLoader l) {
		if (!isBeingLoaded) {
			l.addChunk(this);
			isBeingLoaded = true;
		}
	}
	
	public void addEntity(Entity e) {
		int xx = (int) (e.getX() / Tile.tileSize);
		int yy = (int) (e.getY() / Tile.tileSize);
		
		if (xx < rChunkX || yy < rChunkY || xx >= rChunkX + chunkS || yy >= rChunkY + chunkS) {
			System.err.println("OHNOES!!");
			return;
		}
		
		entityCount++;
		
		entitiesInTiles[world.convertToChunkX(xx) + world.convertToChunkY(yy) * chunkS].add(e);
	}
	
	public ArrayList<Entity> getEntitiesInTile(int tx, int ty) {
		return entitiesInTiles[tx + ty * chunkS];
	}
	
	public void generateTileMap() {
		for (int x = 0; x < chunkS; x++) {
			for (int y = 0; y < chunkS; y++) {
				float s = world.worldNoise.sample(x + chunkX * chunkS, y + chunkY * chunkS);
				float st = world.treeNoise.sample(x + chunkX * chunkS, y + chunkY * chunkS);
				if (s < 0) setTile(Tile.water, x, y);
				else if (s < 0.05f) setTile(Tile.sand, x, y);
				else {
					setTile(Tile.grass, x, y);
					if (st < 0.14f) {
						double r = Math.random();
						if (r < 0.01f)
							world.addEntity(new EntityTree(world, (x + rChunkX) * Tile.tileSize, ((y + rChunkY) * Tile.tileSize) + (float) (Math.random() * 0.02f - 0.01f)));
						else if (r < 0.02f)
							world.addEntity(new EntityFlower(world, (x + rChunkX) * Tile.tileSize, ((y + rChunkY) * Tile.tileSize) + (float) (Math.random() * 0.02f - 0.01f)));
					}
				}
			}
		}
		vboGenerated = false;
		
		for (int x = chunkX - 1; x < chunkX + 2; x++) {
			for (int y = chunkY - 1; y < chunkY + 2; y++) {
				world.chunkm.getChunkWithForceLoad(x, y).vboGenerated = false;
			}
		}
		
		tilesGenerated = true;
	}

	private boolean isValidTilePos(int x, int y) {
		if (x < 0 || y < 0 || x >= chunkS || y >= chunkS)
			return false;
		return true;
	}

	public Color getTileColor(int x, int y) {
		if (!isValidTilePos(x, y)) {
			return new Color(0);
		}
		return tileColors[x + y * chunkS];
	}
	
	public void blendTileColor(Color c, int x, int y) {
		if (!isValidTilePos(x, y))
			return;
		tileColors[x + y * chunkS].blend(c);
	}

	public void setTile(Tile t, int x, int y) {
		if (getTile(x, y) != t) {
			if (!isValidTilePos(x, y))
				return;
			tiles[x + y * chunkS] = t.getID();
			vboGenerated = false;
		}
	}

	public Tile getTile(int x, int y) {
		if (!isValidTilePos(x, y))
			return null;
		return Tile.tiles[tiles[x + y * chunkS]];
	}

	public void generateVBO() {
		if (tilePlane != null)
			tilePlane.deleteBuffer();
		tilePlane = new BufferObject(16 * 16 * 4 * 8 * 2 * 2);
		tilePlane.start();
		tilePlane.bindImage(Image.getImageFromName("spriteSheet"));
		for (int x = 0; x < chunkS; x++) {
			for (int y = 0; y < chunkS; y++) {
				int tileIndex = tiles[x + y * chunkS];
				if (tileIndex != 0) {
					Tile.tiles[tileIndex].render(tilePlane, world, x * Tile.tileSize + (chunkX * Tile.tileSize * chunkS), y * Tile.tileSize + (chunkY * Tile.tileSize * chunkS));
				}
			}
		}
		tilePlane.stop();
		vboGenerated = true;
	}
	
	private void resetLightLevels() {
		Color c = world.getAmbientLight();
		int r = (int) (c.getR() * lFadeOut);
		int g = (int) (c.getG() * lFadeOut);
		int b = (int) (c.getB() * lFadeOut);
		for (int i = 0; i < tileColors.length; i++) {
			tileColors[i] = new Color(r, g, b);
		}
	}

	public AABB getBounds() {
		return new AABB(chunkX * Tile.tileSize * chunkS, chunkY * Tile.tileSize * chunkS, chunkX * Tile.tileSize * chunkS + (chunkS * Tile.tileSize), chunkY * Tile.tileSize * chunkS + (chunkS * Tile.tileSize));
	}

	public boolean isVBOGenerated() {
		return vboGenerated;
	}
	
	public synchronized boolean isTileMapGenerated() {
		return tilesGenerated;
	}
	
	public void markForTileGeneration() {
		tilesGenerated = false;
	}

	public void tick() {
		for (int x = 0; x < chunkS; x++) {
			for (int y = 0; y < chunkS; y++) {
				if (getTileColor(x, y).getBrightness() < 125 && Math.random() * 20000000.0f < 1) {
					if (entityCount < 1) {
						world.addEntity(new EntityZombie(world, (0 + rChunkX) * Tile.tileSize, ((0 + rChunkY) * Tile.tileSize)));
					}
				}
			}
		}
		for (int i = 0; i < chunkSS; i++) {
			ArrayList<Entity> es = entitiesInTiles[i];
			for (int u = 0; u < es.size(); u++) {
				Entity e = es.get(u);
				
				if (e.needsToBeRemoved()) {
					es.remove(e);
					entityCount--;
					continue;
				}
				
				int x0 = (int) (e.getX() / Tile.tileSize);
				int y0 = (int) (e.getY() / Tile.tileSize);
				
				if (!world.entityHasBeenTicked(e)) {
					e.tick();
				}
				
				int x1 = (int) (e.getX() / Tile.tileSize);
				int y1 = (int) (e.getY() / Tile.tileSize);
				
				if (x0 != x1 || y0 != y1) {
					es.remove(e);
					entityCount--;
					world.addEntity(e);
					u--;
				}
			}
		}
	}
	
	public void postTick() {
		Color wc = world.getAmbientLight();
		int r = (int) (wc.getR() * lFadeOut);
		int g = (int) (wc.getG() * lFadeOut);
		int b = (int) (wc.getB() * lFadeOut);
		for (int i = 0; i < tileColors.length; i++) {
			Color c = tileColors[i];
			if (c.getR() * lFadeOut > r) {
				c.setR((int) (c.getR() * lFadeOut));
			} else if (c.getR() > r) {
				c.setR(r);
			}
			if (c.getG() * lFadeOut > g) {
				c.setG((int) (c.getG() * lFadeOut));
			} else if (c.getG() > g) {
				c.setG(g);
			}
			if (c.getB() * lFadeOut > b) {
				c.setB((int) (c.getB() * lFadeOut));
			} else if (c.getB() > b) {
				c.setB(b);
			}
			
			if (c.getR() < r) {
				c.setR(r);
			}
			if (c.getG() < g) {
				c.setG(g);
			}
			if (c.getB() < b) {
				c.setB(b);
			}
		}
		
		if (hasLightChanged()) {
			vboGenerated = false;
		}

		for (int i = 0; i < tileColors.length; i++) {
			pTileColors[i] = tileColors[i].copyOf();
		}
	}
	
	private boolean hasLightChanged() {
		for (int i = 0; i < tileColors.length; i++) {
			Color c = tileColors[i];
			Color pc = pTileColors[i];
			if (!c.equals(pc))
				return true;
		}
		return false;
	}
	
	public void renderGrid() {
		int xx0 = chunkX * Tile.tileSize * chunkS;
		int yy0 = chunkY * Tile.tileSize * chunkS;
		int ww0 = chunkS * Tile.tileSize;
		int hh0 = chunkS * Tile.tileSize;
		
		GL11.glLineWidth(4);
		
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
		
		for (int x = 0; x < chunkS; x++) {
			for (int y = 0; y < chunkS; y++) {
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
	}

	public void render() {
		GL11.glPushMatrix();
		GL11.glTranslatef(0, 0, Tile.yPosToDepthRelativeToCamera(world.getCam(), rChunkY * Tile.tileSize));
		tilePlane.render();
		GL11.glPopMatrix();
		
		for (int i = 0; i < chunkSS; i++) {
			ArrayList<Entity> es = entitiesInTiles[i];
			for (Entity e : es) {
				world.addToEntityToRenderQueue(e);
			}
		}
	}

	public void load() {
		if (!isTileMapGenerated()) {
			this.generateTileMap();
		}
	}
}