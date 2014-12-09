package com.dazkins.triad.game.world;

import java.util.ArrayList;

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
	private float[] lightLevel;
	private boolean generated;

	private BufferObject tilePlane;

	public Chunk(World w, int xp, int yp) {
		this.chunkX = xp;
		this.chunkY = yp;
		this.world = w;

		lightLevel = new float[chunkW * chunkH];
		for (int i = 0; i < lightLevel.length; i++) {
			lightLevel[i] = world.ambientLightLevel;
		}
		tiles = new int[chunkW * chunkH];
	}

	private boolean isValidTilePos(int x, int y) {
		if (x < 0 || y < 0 || x >= chunkW || y >= chunkH)
			return false;
		return true;
	}

	public float getTileBrightness(int x, int y) {
		if (!isValidTilePos(x, y))
			return 0;
		return lightLevel[x + y * chunkW];
	}

	public void setTileBrightness(float b, int x, int y) {
		if (!isValidTilePos(x, y))
			return;
		if (getTileBrightness(x, y) != b) {
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
		if (tilePlane != null)
			tilePlane.deleteBuffer();
		tilePlane = new BufferObject(16 * 16 * 4 * 8 * 2);
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
		for (int i = 0; i < lightLevel.length; i++) {
			lightLevel[i] = world.ambientLightLevel;
		}
	}

	public AABB getBounds() {
		return new AABB(chunkX * Tile.tileSize * chunkW, chunkY * Tile.tileSize * chunkH, chunkX * Tile.tileSize * chunkW + (chunkW * Tile.tileSize), chunkY * Tile.tileSize * chunkH + (chunkH * Tile.tileSize));
	}

	public boolean isGenerated() {
		return generated;
	}

	public void tick() {
		resetLightLevels();
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
	}
}