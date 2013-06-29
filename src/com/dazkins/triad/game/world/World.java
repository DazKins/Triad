package com.dazkins.triad.game.world;

import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Bitmap;

public class World {
	private final int MWIDTH = 40;
	private final int MHEIGHT = 40;
	private byte[] tiles;
	
	public World() {
		tiles = new byte[MWIDTH * MHEIGHT];
		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = 1;
		}
	}
	
	public void render(Bitmap b) {
		for (int x = 0; x < MWIDTH; x++) {
			for (int y = 0; y < MHEIGHT; y++) {
				Tile.tiles[tiles[x + y * MWIDTH]].render(b, x * Tile.tileSize, y * Tile.tileSize);
			}
		}
	}
	
	public void tick() {
		
	}
}