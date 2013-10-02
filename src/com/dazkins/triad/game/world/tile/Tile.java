package com.dazkins.triad.game.world.tile;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

import javax.swing.ImageIcon;

import com.dazkins.triad.file.MultiLineDatabaseFile;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.math.AABB;

public class Tile {
	private static MultiLineDatabaseFile dbs = null;
	public static final int tileSize = 32;
	
	private byte id;
	private String name;
	private int tx, ty;
	private boolean col;
	public static Tile[] tiles = new Tile[256];
	
	public static void initDatabase() {
		loadTileDatabase("res/data/tile.db");
	}
	
	public static float yPosToDepth(float y) {
		return -((y / tileSize) * 0.01f);
	}

	public Tile(byte i, String s, int tex, int tey, boolean c) {
		name = s;
		tx = tex;
		ty = tey;
		id = i;
		col = c;
	}
	
	public AABB getAABB(World w, int x, int y) {
		int x0 = dbs.getInt("AABB.x0", id - 1) + x * Tile.tileSize;
		int y0 = dbs.getInt("AABB.y0", id - 1) + y * Tile.tileSize;
		int x1 = dbs.getInt("AABB.x1", id - 1) + x * Tile.tileSize;
		int y1 = dbs.getInt("AABB.y1", id - 1) + y * Tile.tileSize;
		return new AABB(x0, y0, x1, y1);
	}
	
	public boolean isCollidable() {
		return col;
	}

	public void render(BufferObject b, World w, int x, int y) {
//		int t = w.getTileBrightness((int) (x / 32.0f), (int) (y / 32.0f));
//		if (t < 8 || t > 14)
//			System.out.println(t);
		Image.spriteSheet.renderSprite(b, x, y, tileSize, tileSize, tx * 16, ty * 16, 16, 16, -0.5f, w.getTileBrightness((int) (x / 32.0f), (int) (y / 32.0f)) / 14.0f);
	}

	private static void loadTileDatabase(String path) {
		try {
			dbs = new MultiLineDatabaseFile(path);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		for (int i = 0; i < dbs.getLineCount(); i++) {
			byte id = dbs.getByte("ID", i);
			String s = dbs.getString("NAME", i);
			int tex = dbs.getInt("TX", i);
			int tey = dbs.getInt("TY", i);
			boolean col = dbs.getBoolean("COL", i);
			Tile t = new Tile(id, s, tex, tey, col);
			tiles[id] = t;
		}
	}
	
	public static ImageIcon getTileImageIcon(int i, int scale) {
		Tile t = tiles[i];
		if (t != null) {
			BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
			int pixels[] = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
			int oldPixels[] = Image.spriteSheet.getRawImage().getRGB(t.tx * 16, t.ty * 16, 16, 16, null, 0, 16);
			for (int x = 0; x < 16; x++) {
				for (int y = 0; y < 16; y++) {
					pixels[x + y * 16] = oldPixels[x + y * 16];
				}
			}
			java.awt.Image finalImg = img.getScaledInstance(scale, scale, 0);
			return new ImageIcon(finalImg);
		}
		return null;
	}
	
	public byte getID() {
		return this.id;
	}
	
	public String toString() {
		return name;
	}
}