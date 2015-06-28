package com.dazkins.triad.game.world.tile;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

import javax.swing.ImageIcon;

import org.lwjgl.system.linux.SysIOctl;

import com.dazkins.triad.Triad;
import com.dazkins.triad.file.MultiLineDatabaseFile;
import com.dazkins.triad.game.world.Chunk;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Color;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.math.MathHelper;

public class Tile {
	public static final int tileSize = 32;
	public static Tile[] tiles = new Tile[256];
	
	public static TileGrass grass = new TileGrass(1);
	public static TileStone stone = new TileStone(2);
	public static TileSand sand = new TileSand(3);
	public static TileWhite white = new TileWhite(4);
	public static TileWater water = new TileWater(5);
	
	private int id;
	private String name;
	private int ty;
	private boolean col;
	private int renderOrderPos;

	public static float yPosToDepth(float y) {
//		return -((MathHelper.betterMod(y, Chunk.chunkS * Tile.tileSize) / tileSize) % (Triad.zMax - 10)) - 3;
		return -((y / (float) tileSize)) - 3;
	}
	
	public static float yPosToDepthRelativeToCamera(Camera c, float y) {
		return yPosToDepth(y - c.getY());
	}

	public Tile(int i, String s, int tey, boolean c, int rop) {
		name = s;
		ty = tey;
		id = i;
		col = c;
		renderOrderPos = rop;
		
		tiles[id] = this;
	}

	public AABB getAABB(World w, int x, int y) {
		//TODO Sort this out
//		int x0 = dbs.getInt("AABB.x0", id - 1) + x * Tile.tileSize;
//		int y0 = dbs.getInt("AABB.y0", id - 1) + y * Tile.tileSize;
//		int x1 = dbs.getInt("AABB.x1", id - 1) + x * Tile.tileSize;
//		int y1 = dbs.getInt("AABB.y1", id - 1) + y * Tile.tileSize;
		return null;
	}

	public boolean isCollidable() {
		return col;
	}
	
	private int getRenderOrderPos() {
		return renderOrderPos;
	}

	public void render(BufferObject bo, World wo, int x, int y) {
		byte x0 = 15;
		
		int xt = x / tileSize;
		int yt = y / tileSize;
		
		Tile no = wo.getTile(xt, yt + 1);
		Tile ea = wo.getTile(xt + 1, yt);
		Tile so = wo.getTile(xt, yt - 1);
		Tile we = wo.getTile(xt - 1, yt);
		
		if (no == null || no.getRenderOrderPos() < this.getRenderOrderPos())
			x0 -= 1;
		if (ea == null || ea.getRenderOrderPos() < this.getRenderOrderPos())
			x0 -= 2;
		if (so == null || so.getRenderOrderPos() < this.getRenderOrderPos())
			x0 -= 4;
		if (we == null || we.getRenderOrderPos() < this.getRenderOrderPos())
			x0 -= 8;
		
		float tOffset = 0.001f;
		
		Color c = wo.getTileColor(xt, yt);
		Color c0 = Color.averageColors(new Color[] {wo.getTileColor(xt + 1, yt + 1), wo.getTileColor(xt, yt + 1), wo.getTileColor(xt + 1, yt), c});
		Color c1 = Color.averageColors(new Color[] {wo.getTileColor(xt - 1, yt + 1), wo.getTileColor(xt, yt + 1), wo.getTileColor(xt - 1, yt), c});
		Color c2 = Color.averageColors(new Color[] {wo.getTileColor(xt + 1, yt - 1), wo.getTileColor(xt, yt - 1), wo.getTileColor(xt + 1, yt), c});
		Color c3 = Color.averageColors(new Color[] {wo.getTileColor(xt - 1, yt - 1), wo.getTileColor(xt, yt - 1), wo.getTileColor(xt - 1, yt), c});
		
		Image i = Image.getImageFromName("spriteSheet");
		
		float txmin = (x0 * 16.0f) / i.getWidth() + tOffset;
		float tymin = (ty * 16.0f) / i.getHeight() + tOffset;
		float txmax = (x0 * 16.0f + 16) / i.getWidth() - tOffset;
		float tymax = (ty * 16.0f + 16) / i.getHeight() - tOffset;
		
		bo.bindImage(i);
		bo.setDepth(Tile.yPosToDepth(MathHelper.betterMod(y, Chunk.chunkS * Tile.tileSize)) - 1.0f);
		bo.setColor(c0);
		bo.setUV(txmax, tymin);
		bo.addVertex(x + tileSize, y + tileSize);
		bo.setColor(c1);
		bo.setUV(txmin, tymin);
		bo.addVertex(x, y + tileSize);
		bo.setColor(c3);
		bo.setUV(txmin, tymax);
		bo.addVertex(x, y);
		bo.setColor(c2);
		bo.setUV(txmax, tymax);
		bo.addVertex(x + tileSize, y);
	}

	public int getID() {
		return this.id;
	}

	public String toString() {
		return name;
	}
}