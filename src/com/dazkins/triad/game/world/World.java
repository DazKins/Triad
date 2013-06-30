package com.dazkins.triad.game.world;

import java.util.ArrayList;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.bitmap.Bitmap;
import com.dazkins.triad.gfx.Camera;

public class World {
	public final int MWIDTH = 40;
	public final int MHEIGHT = 40;
	private byte[] tiles;
	private ArrayList<Entity>[] entities = new ArrayList[MWIDTH * MHEIGHT];
	
	public World() {
		tiles = new byte[MWIDTH * MHEIGHT];
		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = 1;
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
		for (int y = 0; y < MHEIGHT; y++) {
			for (int x = 0; x < MWIDTH; x++) {
				Tile.tiles[tiles[x + y * MWIDTH]].render(b, (int) (x * Tile.tileSize - cam.getX()), (int) (y * Tile.tileSize - cam.getY()));
			}
			for (int x = 0; x < MWIDTH; x++) {
				ArrayList<Entity> list = entities[x + y * MWIDTH];
				for (int i = 0; i < list.size(); i++) {
					list.get(i).render(b, cam);
				}
 			}
		}
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