package com.dazkins.triad.game.world;

import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.math.AABB;

public class Chunk {
	public static int chunkW = 16, chunkH = 16;
	
	private int chunkX, chunkY;
	private byte[] tiles;
	private boolean generated;
	
	private BufferObject model;
	
	public Chunk(int xp, int yp) {
		this.chunkX = xp;
		this.chunkY = yp;
		
		tiles = new byte[chunkW * chunkH];
		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = (byte) (i % 7 != 0 ? 1 : 2);
		}
	}
	
	public void setTile(Tile t, int x, int y) {
		if (x < chunkX || y < chunkY || x >= chunkX + chunkW || y >= chunkY + chunkH) 
			return;
		tiles[x + y * chunkW] = t.getID();
	}
	
	public Tile getTile(int x, int y) {
		if (x < chunkX || y < chunkY || x >= chunkX + chunkW || y >= chunkY + chunkH) 
			return null;
		return Tile.tiles[tiles[x + y * chunkW]];
	}
	
	public void generate() {
		model = new BufferObject(16 * 16 * 4 * 5);
		model.start();
		for (int x = 0; x < chunkW; x++) {
			for (int y = 0; y < chunkH; y++) {
				Tile.tiles[tiles[x + y * chunkW]].render(model, x * Tile.tileSize + chunkX, y * Tile.tileSize + chunkY);
			}
		}
		model.stop();
		generated = true;
	}
	
	public AABB getBounds() {
		return new AABB(chunkX, chunkY, chunkX + (chunkW * Tile.tileSize), chunkY + (chunkH * Tile.tileSize));
	}
	
	public boolean isGenerated() {
		return generated;
	}
	
	public void tick() {
		
	}
	
	public void render() {
		model.render();
	}
}