package com.dazkins.triad.game.entity.mob;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.EntityItemStack;
import com.dazkins.triad.game.gui.GuiStatusBar;
import com.dazkins.triad.game.inventory.EquipmentInventory;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.item.Item;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.game.inventory.item.equipable.weapon.ItemWeapon;
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
	
	private int attackCooldownCounter;

	public Mob(World w, float x, float y, String s, int h) {
		super(w, x, y, s);
		health = h;
		eInv = new EquipmentInventory();
		
		healthBar = new GuiStatusBar(0, 0, 0xFF0000, 128);
	}

	public int getMovementState() {
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
	
	public int getBaseDamage() {
		return 0;
	}
	
	public int getBaseKnockback() {
		return 0;
	}
	
	public int getBaseAttackCooldown() {
		return 0;
	}
	
	protected boolean attemptAttack(AABB a) {
		if (attackCooldownCounter == 0) {
			attackArea(a);
			ItemWeapon wep = eInv.getWeaponItem();
			if (wep != null)
				attackCooldownCounter = wep.getAttackCooldown();
			else
				attackCooldownCounter = getBaseAttackCooldown();
			return true;
		} else {
			return false;
		}
	}
	
	private void attackArea(AABB a) {
		world.sendAttackCommand(a, this, eInv.getWeaponItem());
	}

	public void hurt(Mob m, ItemWeapon it) {
		float x0 = this.getX() - m.getX();
		float y0 = this.getY() - m.getY();
		float mag = (float) Math.sqrt(x0 * x0 + y0 * y0);
		x0 /= mag;
		y0 /= mag;
		if (it != null) {
			push(x0 * it.getKnockback(), y0 * it.getKnockback());
			health -= it.getDamage();
		} else {
			push(x0 * m.getBaseKnockback(), y0 * m.getBaseKnockback());
			health -= m.getBaseDamage();
		}
	}
	
	public void push(float xa, float ya) {
		addXAMod(xa);
		addYAMod(ya);
	}
	
	public void tick() {
		super.tick();
		
		if(attackCooldownCounter > 0) {
			attackCooldownCounter--;
		}
		
		if (health <= 0) {
			kill();
		}
	}
	
	public ItemStack[] getItemsToDrop() {
		return null;
	}
	
	public void onDeath() {
		ItemStack[] stacks = getItemsToDrop();
		for (int i = 0; i < stacks.length; i++) {
			Item.dropItemStack(world, x, y, stacks[i]);
		}
	}
	
	public void kill() {
		onDeath();
		remove();
	}
	
	public void move() {
		for (Float i : xvm) {
			this.xa += i;
		}
		for (Float i : yvm) {
			this.ya += i;
		}
		
		AABB aabb = this.getAABB();

		int x0 = ((int) x / Tile.tileSize) - 3;
		int y0 = ((int) y / Tile.tileSize) - 3;
		int x1 = ((int) x / Tile.tileSize) + 3;
		int y1 = ((int) y / Tile.tileSize) + 3;

		if (x0 < 0)
			x0 = 0;
		if (y0 < 0)
			y0 = 0;
		if (x1 > world.nChunksX * Chunk.chunkW)
			x1 = world.nChunksX * Chunk.chunkW;
		if (y1 > world.nChunksY * Chunk.chunkH)
			y1 = world.nChunksY * Chunk.chunkH;

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

		super.move(true);
	}
	
	public void renderToPlayerGui(Camera c) {
		float trans = 0.5f;
		healthBar.updateStatus((float) getHealth() / (float) getMaxHealth());
		healthBar.render(x - (64 / c.getZoom()), y + 50, Tile.yPosToDepth(y) + 500, 1 / c.getZoom(), trans);
		Font.drawString(name, x - ((8 / 1.5f) * 1/ c.getZoom()) * name.length(), y + 50 + (20 / (c.getZoom())), 1.0f, 1.0f, 1.0f, trans, Tile.yPosToDepth(y) + 500, (1 / c.getZoom()) / 1.5f);
	}
	
	public void render(boolean debug) {
		super.render();
		
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