package com.dazkins.triad.game.entity.mob;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.gui.GuiStatusBar;
import com.dazkins.triad.game.inventory.EquipmentInventory;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.world.Chunk;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Font;
import com.dazkins.triad.math.AABB;

public abstract class Mob extends Entity {
	protected int health;
	protected Inventory inv;
	protected EquipmentInventory eInv;
	
	//I don't really want graphical data stored in Entity classes
	//This is just a temporary measure
	protected GuiStatusBar healthBar;

	public Mob(World w, float x, float y, String s, int h) {
		super(w, x, y, s);
		health = h;
		eInv = new EquipmentInventory();
		
		healthBar = new GuiStatusBar(0, 0, 0xFF0000, 128);
	}

	public MovementState getMovementState() {
		float speedLen = 0.2f;
		if (Math.abs(xa) > speedLen || Math.abs(ya) > speedLen)
			return MovementState.MOVING;
		else
			return MovementState.STATIONARY;
	}

	public abstract float getMovementSpeed();

	public abstract int getMaxHealth();

	public int getHealth() {
		return health;
	}

	public void hurt(int damage) {
		health -= damage;
	}

	public void move(float xa, float ya) {
		AABB aabb = this.getAABB();

		int x0 = ((int) x / Tile.tileSize) - 3;
		int y0 = ((int) y / Tile.tileSize) - 3;
		int x1 = ((int) x / Tile.tileSize) + 3;
		int y1 = ((int) y / Tile.tileSize) + 3;

		if (x0 < 0)
			x0 = 0;
		if (y0 < 0)
			y0 = 0;
		if (x1 > world.info.nChunksX * Chunk.chunkW)
			x1 = world.info.nChunksX * Chunk.chunkW;
		if (y1 > world.info.nChunksY * Chunk.chunkH)
			y1 = world.info.nChunksY * Chunk.chunkH;

		for (int x = x0; x < x1; x++) {
			for (int y = y0; y < y1; y++) {
				if (world.getTile(x, y).isCollidable()) {
					AABB taabb = world.getTile(x, y).getAABB(world, x, y);
					if (aabb.shifted(xa, 0).intersects(taabb)) {
						xa = 0;
					}
					if (aabb.shifted(0, ya).intersects(taabb)) {
						ya = 0;
					}
				}
			}
		}

		this.xa = xa;
		this.ya = ya;

		super.move(xa, ya);
	}
	
	public void renderToPlayerGui(Camera c) {
		healthBar.updateStatus((float) getHealth() / (float) getMaxHealth());
		healthBar.render(x - (64 / c.getZoom()), y + 50, Tile.yPosToDepth(y) + 500, 1 / c.getZoom());
		Font.drawString(name, x - ((8 / 1.5f) * 1/ c.getZoom()) * name.length(), y + 50 + (20 / (c.getZoom())), 1.0f, 1.0f, 1.0f, Tile.yPosToDepth(y) + 500, (1 / c.getZoom()) / 1.5f);
	}
	
	public void render(boolean debug) {
		if (debug)
			getAABB().renderBounds(1);
	}
	
	public Inventory getInventory() {
		return inv;
	}
	
	public EquipmentInventory getEquipmentInventory() {
		return eInv;
	}
}