package com.dazkins.triad.game.world;

import java.util.ArrayList;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.bitmap.Bitmap;
import com.dazkins.triad.gfx.Camera;

public class World {
	public final int MWIDTH = 400;
	public final int MHEIGHT = 400;
	private byte[] tiles;
	private ArrayList<Entity>[] entities = new ArrayList[MWIDTH * MHEIGHT];
	
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
	
	public void render(Bitmap b, Camera cam) {
		int camx0 = (int) cam.getX() >> 4;
		int camy0 = (int) cam.getY() >> 4;
		int camx1 = camx0 + ((int) cam.getW() >> 4);
		int camy1 = camy0 + ((int) cam.getH() >> 4);
		
		for (int y = camy0; y < camy1 + 2; y++) {
			for (int x = camx0; x < camx1 + 2; x++) {
				Tile.tiles[tiles[x + y * MWIDTH]].render(b, (int) (x * Tile.tileSize - cam.getX()), (int) (y * Tile.tileSize - cam.getY()));
			}
			for (int x = camx0; x < camx1; x++) {
				ArrayList<Entity> list = entities[x + y * MWIDTH];
				for (int i = 0; i < list.size(); i++) {
					list.get(i).render(b, cam);
				}
 			}
		}
	}
	
	public Tile getTile(int x, int y) {
		if (x < 0 || y < 0 || x > MWIDTH || y > MHEIGHT) return null;
		return Tile.tiles[tiles[x + y * MWIDTH]];
	}
	
	public void tick() {
		for (int i = 0; i < entities.length; i++) {
			ArrayList<Entity> list = entities[i];
			for (int u = 0; u < list.size(); u++) {
				Entity e = list.get(u);
				int x0 = (int) e.getX() >> 4;
				int y0 = (int) e.getY() >> 4;
				
				e.tick();
				
				int x1 = (int) e.getX() >> 4;
				int y1 = (int) e.getY() >> 4;
				
				if(x0 != x1 || y0 != y1) {
					if (x1 > 0 && y1 > 0 && x1 < MWIDTH && y1 < MHEIGHT) {
						list.remove(e);
						entities[x1 + y1 * MWIDTH].add(e);
					}
				}
			}
		}
	}
}