package com.dazkins.triad.game.world;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.LightEmitter;
import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.game.entity.particle.Particle;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Color;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.math.NoiseMap;
import com.dazkins.triad.util.Loadable;
import com.dazkins.triad.util.Loader;

public class Chunk implements Loadable {
	public static int chunkW = 16, chunkH = 16;
	
	private static float lFadeOut = 0.85f;

	private World world;

	public int chunkX, chunkY;
	private int[] tiles;
	
	private Color[] tileColors;
	
	//Store light value from the previous frame
	private Color[] pTileColors;
	
	private boolean vboGenerated;
	private boolean tilesGenerated;
	
	private boolean isBeingLoaded;

	private BufferObject tilePlane;

	public Chunk(World w, int xp, int yp) {
		this.chunkX = xp;
		this.chunkY = yp;
		this.world = w;

		tileColors = new Color[chunkW * chunkH];
		pTileColors = new Color[chunkW * chunkH];
		
		resetLightLevels();
		
		tiles = new int[chunkW * chunkH];
	}
	
	public void addToLoader(Loader l) {
		if (!isBeingLoaded) {
			l.addLoad(this);
			isBeingLoaded = true;
		}
	}
	
	public void generateTileMap() {
		for (int x = 0; x < chunkW; x++) {
			for (int y = 0; y < chunkH; y++) {
				float s = world.worldNoise.sample(x + chunkX * chunkW, y + chunkY * chunkH);
				if (s < 0) setTile(Tile.water, x, y);
				else if (s < 0.01f) setTile(Tile.sand, x, y);
				else setTile(Tile.grass, x, y);
			}
		}
		vboGenerated = false;
		
		Chunk c0 = world.chunkm.getChunk(chunkX - 1, chunkY);
		Chunk c1 = world.chunkm.getChunk(chunkX + 1, chunkY);
		Chunk c2 = world.chunkm.getChunk(chunkX, chunkY + 1);
		Chunk c3 = world.chunkm.getChunk(chunkX, chunkY - 1);
		Chunk c4 = world.chunkm.getChunk(chunkX - 1, chunkY -1);
		Chunk c5 = world.chunkm.getChunk(chunkX + 1, chunkY + 1);
		Chunk c6 = world.chunkm.getChunk(chunkX - 1, chunkY + 1);
		Chunk c7 = world.chunkm.getChunk(chunkX + 1, chunkY - 1);
		
		if (c0 != null)
			c0.vboGenerated = false;
		if (c1 != null)
			c1.vboGenerated = false;
		if (c2 != null)
			c2.vboGenerated = false;
		if (c3 != null)
			c3.vboGenerated = false;
		if (c4 != null)
			c4.vboGenerated = false;
		if (c5 != null)
			c5.vboGenerated = false;
		if (c6 != null)
			c6.vboGenerated = false;
		if (c7 != null)
			c7.vboGenerated = false;
		
		tilesGenerated = true;
	}

	private boolean isValidTilePos(int x, int y) {
		if (x < 0 || y < 0 || x >= chunkW || y >= chunkH)
			return false;
		return true;
	}

	public Color getTileColor(int x, int y) {
		if (!isValidTilePos(x, y))
			return new Color(0);
		return tileColors[x + y * chunkW];
	}
	
	public void blendTileColor(Color c, int x, int y) {
		if (!isValidTilePos(x, y))
			return;
		tileColors[x + y * chunkW].blend(c);
	}

	public void setTile(Tile t, int x, int y) {
		if (getTile(x, y) != t) {
			if (!isValidTilePos(x, y))
				return;
			tiles[x + y * chunkW] = t.getID();
			vboGenerated = false;
		}
	}

	public Tile getTile(int x, int y) {
		if (!isValidTilePos(x, y))
			return null;
		return Tile.tiles[tiles[x + y * chunkW]];
	}

	public void generateVBO() {
		if (tilePlane != null)
			tilePlane.deleteBuffer();
		tilePlane = new BufferObject(16 * 16 * 4 * 8 * 2 * 2);
		tilePlane.start();
		tilePlane.bindImage(Image.getImageFromName("spriteSheet"));
		for (int x = 0; x < chunkW; x++) {
			for (int y = 0; y < chunkH; y++) {
				int tileIndex = tiles[x + y * chunkW];
				if (tileIndex != 0) {
					Tile.tiles[tileIndex].render(tilePlane, world, x * Tile.tileSize + (chunkX * Tile.tileSize * chunkW), y * Tile.tileSize + (chunkY * Tile.tileSize * chunkH));
				}
			}
		}
		tilePlane.stop();
		vboGenerated = true;
	}
	
	private void resetLightLevels() {
		for (int i = 0; i < tileColors.length; i++) {
			int c = (int) (world.ambientLightLevel * lFadeOut);
			tileColors[i] = new Color(c, c, c);
		}
	}

	public AABB getBounds() {
		return new AABB(chunkX * Tile.tileSize * chunkW, chunkY * Tile.tileSize * chunkH, chunkX * Tile.tileSize * chunkW + (chunkW * Tile.tileSize), chunkY * Tile.tileSize * chunkH + (chunkH * Tile.tileSize));
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
		for (int i = 0; i < tileColors.length; i++) {
			Color c = tileColors[i];
			if (c.getR() * lFadeOut > world.ambientLightLevel * lFadeOut) {
				c.setR((int) (c.getR() * lFadeOut));
			} else if (c.getR() > world.ambientLightLevel * lFadeOut) {
				c.setR((int) (world.ambientLightLevel * lFadeOut));
			}
			if (c.getG() * lFadeOut > world.ambientLightLevel * lFadeOut) {
				c.setG((int) (c.getG() * lFadeOut));
			} else if (c.getG() > world.ambientLightLevel * lFadeOut) {
				c.setG((int) (world.ambientLightLevel * lFadeOut));
			}
			if (c.getB() * lFadeOut > world.ambientLightLevel * lFadeOut) {
				c.setB((int) (c.getB() * lFadeOut));
			} else if (c.getB() > world.ambientLightLevel * lFadeOut) {
				c.setB((int) (world.ambientLightLevel * lFadeOut));
			}
		}
		
		if (hasLightChanged())
			vboGenerated = false;

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
		int xx0 = chunkX * Tile.tileSize * chunkW;
		int yy0 = chunkY * Tile.tileSize * chunkH;
		int ww0 = chunkW * Tile.tileSize;
		int hh0 = chunkH * Tile.tileSize;
		
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
		
//		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public void render() {
		tilePlane.render();
	}

	public void load() {
		if (!isTileMapGenerated()) {
			this.generateTileMap();
		}
	}
}