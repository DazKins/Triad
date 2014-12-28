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
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.math.AABB;

public class Chunk {
	public static int chunkW = 16, chunkH = 16;

	private World world;

	private int chunkX, chunkY;
	private int[] tiles;
	
	private float[] lightR;
	private float[] lightG;
	private float[] lightB;
	
	private float[] pLightR;
	private float[] pLightG;
	private float[] pLightB;
	
	private boolean generated;

	private BufferObject tilePlane;
	
	private static float lFalloff = 0.85f;

	public Chunk(World w, int xp, int yp) {
		this.chunkX = xp;
		this.chunkY = yp;
		this.world = w;

		lightR = new float[chunkW * chunkH];
		lightG = new float[chunkW * chunkH];
		lightB = new float[chunkW * chunkH];
		
		pLightR = new float[chunkW * chunkH];
		pLightG = new float[chunkW * chunkH];
		pLightB = new float[chunkW * chunkH];
		
		resetLightLevels();
		
		tiles = new int[chunkW * chunkH];
	}

	private boolean isValidTilePos(int x, int y) {
		if (x < 0 || y < 0 || x >= chunkW || y >= chunkH)
			return false;
		return true;
	}

	public float getTileR(int x, int y) {
		if (!isValidTilePos(x, y))
			return 0;
		return lightR[x + y * chunkW];
	}
	
	public float getTileG(int x, int y) {
		if (!isValidTilePos(x, y))
			return 0;
		return lightG[x + y * chunkW];
	}
	
	public float getTileB(int x, int y) {
		if (!isValidTilePos(x, y))
			return 0;
		return lightB[x + y * chunkW];
	}
	
	public void setTileR(float b, int x, int y) {
		if (!isValidTilePos(x, y))
			return;
		lightR[x + y * chunkW] = b;
	}
	
	public void setTileG(float b, int x, int y) {
		if (!isValidTilePos(x, y))
			return;
		lightG[x + y * chunkW] = b;
	}
	
	public void setTileB(float b, int x, int y) {
		if (!isValidTilePos(x, y))
			return;
		lightB[x + y * chunkW] = b;
	}
	
	public void applyTileR(float b, int x, int y) {
		if (!isValidTilePos(x, y))
			return;
		if (b > lightR[x + y * chunkW] && b >= world.ambientLightLevel) {
			lightR[x + y * chunkW] = b;
		}
	}
	
	public void applyTileG(float b, int x, int y) {
		if (!isValidTilePos(x, y))
			return;
		if (b > lightG[x + y * chunkW] && b >= world.ambientLightLevel) {
			lightG[x + y * chunkW] = b;
		}
	}
	
	public void applyTileB(float b, int x, int y) {
		if (!isValidTilePos(x, y))
			return;
		if (b > lightB[x + y * chunkW] && b >= world.ambientLightLevel) {
			lightB[x + y * chunkW] = b;
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
		generated = true;
	}
	
	private void resetLightLevels() {
		for (int i = 0; i < lightR.length; i++) {
			lightR[i] = world.ambientLightLevel * lFalloff;
			lightG[i] = world.ambientLightLevel * lFalloff;
			lightB[i] = world.ambientLightLevel * lFalloff;
		}
	}

	public AABB getBounds() {
		return new AABB(chunkX * Tile.tileSize * chunkW, chunkY * Tile.tileSize * chunkH, chunkX * Tile.tileSize * chunkW + (chunkW * Tile.tileSize), chunkY * Tile.tileSize * chunkH + (chunkH * Tile.tileSize));
	}

	public boolean isGenerated() {
		return generated;
	}

	public void tick() {
		for (int i = 0; i < lightR.length; i++) {
			if (lightR[i] * lFalloff > world.ambientLightLevel * lFalloff) {
				lightR[i] *= lFalloff;
			} else if (lightR[i] > world.ambientLightLevel * lFalloff) {
				lightR[i] = world.ambientLightLevel * lFalloff;
			}
			if (lightG[i] * lFalloff > world.ambientLightLevel * lFalloff) {
				lightG[i] *= lFalloff;
			} else if (lightG[i] > world.ambientLightLevel * lFalloff) {
				lightG[i] = world.ambientLightLevel * lFalloff;
			}
			if (lightB[i] * lFalloff > world.ambientLightLevel * lFalloff) {
				lightB[i] *= lFalloff;
			} else if (lightB[i] > world.ambientLightLevel * lFalloff) {
				lightB[i] = world.ambientLightLevel * lFalloff;
			}
		}
		
		if (hasLightChanged())
			generated = false;

		for (int i = 0; i < lightR.length; i++) {
			pLightR[i] = lightR[i];
			pLightG[i] = lightG[i];
			pLightB[i] = lightB[i];
		}
	}
	
	private boolean hasLightChanged() {
		for (int i = 0; i < lightR.length; i++) {
			if (pLightR[i] != lightR[i])
				return true;
			if (pLightG[i] != lightB[i])
				return true;
			if (pLightG[i] != lightB[i])
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
		
//		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
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
//		for (int x = 0; x < chunkW; x++) {
//			for (int y = 0; y < chunkH; y++) {
//				float x0 = x * Tile.tileSize + (chunkX * Tile.tileSize * chunkW);
//				float y0 = y * Tile.tileSize + (chunkY * Tile.tileSize * chunkH);
//				float x1 = x0 + Tile.tileSize;
//				float y1 = y0 + Tile.tileSize;
//				
//				GL11.glDisable(GL11.GL_TEXTURE_2D);
//				GL11.glColor4f(lightR[x + y * chunkW], lightG[x + y * chunkW], lightB[x + y * chunkW], 0.9f);
//				GL11.glBegin(GL11.GL_QUADS);
//					GL11.glVertex3f(x0, y0, 20);
//					GL11.glVertex3f(x1, y0, 20);
//					GL11.glVertex3f(x1, y1, 20);
//					GL11.glVertex3f(x0, y1, 20);
//				GL11.glEnd();
//				GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
//				GL11.glEnable(GL11.GL_TEXTURE_2D);
//			}
//		}
	}
}