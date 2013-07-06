package com.dazkins.triad.game.world.tile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.dazkins.triad.file.DatabaseFile;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Art;
import com.dazkins.triad.gfx.bitmap.Bitmap;
import com.dazkins.triad.math.AABB;

public class Tile {
	private static DatabaseFile dbs = null;
	
	public static final int tileSize = 16;
	private byte id;
	private String name;
	private int tx, ty;
	private boolean col;
	public static Tile[] tiles = new Tile[256];

	static {
		loadTileDatabase("res/data/tile.db");
	}

	public Tile(byte i, String s, int tex, int tey, boolean c) {
		name = s;
		tx = tex;
		ty = tey;
		id = i;
		col = c;
	}
	
	public AABB getAABB(World w, int x, int y) {
		int x0 = ((Integer) dbs.tags.get(id - 1).get("AABB.x0")) + (x << 4);
		int y0 = ((Integer) dbs.tags.get(id - 1).get("AABB.y0")) + (y << 4);
		int x1 = ((Integer) dbs.tags.get(id - 1).get("AABB.x1")) + (x << 4);
		int y1 = ((Integer) dbs.tags.get(id - 1).get("AABB.y1")) + (y << 4);
		return new AABB(x0, y0, x1, y1);
	}
	
	public boolean isCollidable() {
		return col;
	}

	public void render(Bitmap b, int x, int y) {
		Art.spriteSheet.renderSprite(tx, ty, b, x, y);
	}

	private static void loadTileDatabase(String path) {
		dbs = null;
		
		try {
			dbs = new DatabaseFile(path);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		for (int i = 0; i < dbs.tags.size(); i++) {
			byte id = ((Integer) dbs.tags.get(i).get("ID")).byteValue();
			String s = (String) dbs.tags.get(i).get("NAME");
			int tex = (Integer) dbs.tags.get(i).get("TX");
			int tey = (Integer) dbs.tags.get(i).get("TY");
			boolean col = (Boolean) dbs.tags.get(i).get("COL");
			Tile t = new Tile(id, s, tex, tey, col);
			tiles[id] = t;
		}
	}
	
	public String toString() {
		return name;
	}
}