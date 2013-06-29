package com.dazkins.triad.game.world.tile;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.dazkins.triad.file.DatabaseFile;

public class Tile {
	private byte id;
	private String name;
	private int tx, ty;
	private Tile[] tiles = new Tile[256];

	static {
		loadTileDatabase("res/data/tile.db");
	}

	public Tile(byte i, String s, int tex, int tey) {
		name = s;
		tx = tex;
		ty = tey;
		id = i;
	}

	public void render() {

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
		}
	}
}