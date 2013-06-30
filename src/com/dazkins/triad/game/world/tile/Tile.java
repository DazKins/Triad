package com.dazkins.triad.game.world.tile;

import java.io.IOException;

import com.dazkins.triad.file.DatabaseFile;
import com.dazkins.triad.gfx.Art;
import com.dazkins.triad.gfx.bitmap.Bitmap;

public class Tile {
	public static final int tileSize = 16;
	private byte id;
	private String name;
	private int tx, ty;
	public static Tile[] tiles = new Tile[256];

	static {
		loadTileDatabase("res/data/tile.db");
	}

	public Tile(byte i, String s, int tex, int tey) {
		name = s;
		tx = tex;
		ty = tey;
		id = i;
	}

	public void render(Bitmap b, int x, int y) {
		Art.spriteSheet.renderSprite(tx, ty, b, x, y);
	}

	private static void loadTileDatabase(String path) {
		DatabaseFile dbs = null;
		try {
			dbs = new DatabaseFile(path);
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < dbs.tags.size(); i++) {
			byte id = ((Integer) dbs.tags.get(i).get("ID")).byteValue();
			String s = (String) dbs.tags.get(i).get("NAME");
			int tex = (Integer) dbs.tags.get(i).get("TX");
			int tey = (Integer) dbs.tags.get(i).get("TY");
			Tile t = new Tile(id, s, tex, tey);
			tiles[id] = t;
		}
	}
}