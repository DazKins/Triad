package com.dazkins.triad.game.world;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.dazkins.triad.file.DatabaseFile;
import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.GLRenderer;

public class World {
	private static DatabaseFile globalWorldDatabase = null;

	public DatabaseFile worldDB;
	public int MWIDTH = 400;
	public int MHEIGHT = 400;
	private byte[] tiles;
	private ArrayList<Entity>[] entities = new ArrayList[MWIDTH * MHEIGHT];

	static {
		loadWorldDatabase();
	}

	private static void loadWorldDatabase() {
		try {
			globalWorldDatabase = new DatabaseFile("res/data/worlds.db");
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < globalWorldDatabase.tags.size(); i++) {
			loadWorldFromFile("res/data/worlds/" + (String) globalWorldDatabase.tags.get(i).get("PATH"), (Integer) globalWorldDatabase.tags.get(i).get("INDEX"));
		}
	}

	public static World loadWorldFromFile(String p, int i) {
		World w = new World();
		try {
			w.worldDB = new DatabaseFile(p + "_metadata.db");
		} catch (IOException e) {
			e.printStackTrace();
		}
		w.MHEIGHT = (int) w.worldDB.tags.get(0).get("HEIGHT");
		w.MWIDTH = (int) w.worldDB.tags.get(0).get("WIDTH");
		return w;
	}

	public World() {
		tiles = new byte[MWIDTH * MHEIGHT];
		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = (byte) ((i + 3) % 17 == 0 ? 2 : 1);
		}
		for (int i = 0; i < entities.length; i++) {
			entities[i] = new ArrayList<Entity>();
		}
	}

	public void addEntity(Entity e) {
		int x = (int) e.getX() >> 4;
		int y = (int) e.getY() >> 4;

		entities[x + y * MWIDTH].add(e);
	}

	public void render(GLRenderer t, Camera cam) {
		int camx0 = (int) cam.getX() / Tile.tileSize;
		int camy0 = (int) cam.getY() / Tile.tileSize;
		int camx1 = camx0 + ((int) cam.getW() / Tile.tileSize);
		int camy1 = camy0 + ((int) cam.getH() / Tile.tileSize);

		for (int y = camy0; y < camy1 + 2; y++) {
			int y0 = y;
			if (y0 < 0)
				y0 = 0;
			if (y0 > MHEIGHT - 1)
				y0 = MHEIGHT - 1;
			int x0;
			for (int x = camx0; x < camx1 + 2; x++) {
				x0 = x;
				if (x0 < 0)
					x0 = 0;
				if (x0 > MWIDTH - 1)
					x0 = MWIDTH - 1;
				Tile tile = Tile.tiles[tiles[x0 + y0 * MWIDTH]];
				tile.render(t, (int) (x * Tile.tileSize - cam.getX()), (int) (y * Tile.tileSize - cam.getY()));
			}
			for (int x = camx0; x < camx1; x++) {
				x0 = x;
				if (x0 < 0)
					x0 = 0;
				if (x0 > MWIDTH - 1)
					x0 = MWIDTH - 1;
				ArrayList<Entity> list = entities[x0 + y0 * MWIDTH];
				Collections.sort(list, new Comparator<Entity>() {
					public int compare(Entity e0, Entity e1) {
						if (e1.getY() < e0.getY())
							return 1;
						if (e1.getY() > e0.getY())
							return -1;
						return 0;
					}

				});
				for (int i = 0; i < list.size(); i++) {
					list.get(i).render(t, cam);
				}
			}
		}
	}

	public Tile getTile(int x, int y) {
		if (x < 0 || y < 0 || x > MWIDTH || y > MHEIGHT)
			return null;
		return Tile.tiles[tiles[x + y * MWIDTH]];
	}

	public void tick() {
		for (int i = 0; i < entities.length; i++) {
			ArrayList<Entity> list = entities[i];
			for (int u = 0; u < list.size(); u++) {
				Entity e = list.get(u);
				int x0 = (int) e.getX() / Tile.tileSize;
				int y0 = (int) e.getY() / Tile.tileSize;

				e.tick();

				int x1 = (int) e.getX() / Tile.tileSize;
				int y1 = (int) e.getY() / Tile.tileSize;

				if (x1 < 0)
					x1 = 0;
				if (x1 > MWIDTH)
					x1 = MWIDTH;
				if (y1 < 0)
					y1 = 0;
				if (y1 > MHEIGHT)
					y1 = MHEIGHT;

				if (x0 != x1 || y0 != y1) {
					list.remove(e);
					entities[x1 + y1 * MWIDTH].add(e);
				}
			}
		}
	}
}