package com.dazkins.triad.game.entity.mob;

import java.util.ArrayList;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.Facing;
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
	
	protected Mob target;

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
	
	protected int getBaseDamage() {
		return 0;
	}
	
	protected int getDamage() {
		ItemWeapon wep = eInv.getWeaponItem();
		if (wep != null) {
			return wep.getDamage();
		} else {
			return getBaseDamage();
		}
	}
	
	protected int getBaseKnockback() {
		return 0;
	}
	
	protected int getKnockbackValue() {
		ItemWeapon wep = eInv.getWeaponItem();
		if (wep != null) {
			return wep.getKnockback();
		} else {
			return getBaseKnockback();
		}
	}
	
	protected int getBaseAttackCooldown() {
		return 0;
	}
	
	protected int getAttackCooldown() {
		ItemWeapon wep = eInv.getWeaponItem();
		if (wep != null) {
			return wep.getAttackCooldown();
		} else {
			return getBaseAttackCooldown();
		}
	}
	
	protected int getBaseAttackRange() {
		return 0;
	}
	
	protected int getAttackRange() {
		ItemWeapon wep = eInv.getWeaponItem();
		if (wep != null) {
			return wep.getAttackRange();
		} else {
			return getBaseAttackRange();
		}
	}
	
	protected boolean attemptAttack(AABB a) {
		if (attackCooldownCounter == 0) {
			attackArea(a);
			attackCooldownCounter = getAttackCooldown();
			return true;
		} else {
			return false;
		}
	}
	
	private void attackArea(AABB a) {
		world.sendAttackCommand(a, this, getDamage(), getKnockbackValue());
	}
	
	public Class<? extends Mob>[] getHostileMobs() {
		return null;
	}
	
	public AABB getEnemyScanArea() {
		return null;
	}

	public void hurt(Mob m, int d, int k) {
		float x0 = this.getX() - m.getX();
		float y0 = this.getY() - m.getY();
		float mag = (float) Math.sqrt(x0 * x0 + y0 * y0);
		x0 /= mag;
		y0 /= mag;
		push(x0 * k, y0 * k);
		health -= d;
	}
	
	public void tick() {
		super.tick();
		
		
		ArrayList<Entity> ents = new ArrayList<Entity>();
		
		AABB eb = this.getEnemyScanArea();
		
		if (eb != null) {
			ArrayList<Mob> hostileMobs = new ArrayList<Mob>();
			ents = world.getEntitiesInAABB(eb);
			for (Entity e : ents) {
				if (e instanceof Mob) {
					if (this.isHostileTo(e))
						hostileMobs.add((Mob) e);
				}
			}
			
			if (hostileMobs.size() > 1) {
				int r = (int) (Math.random() * (hostileMobs.size() - 1));
				this.target = hostileMobs.get(r);
			} else if (hostileMobs.size() == 1) {
				this.target = hostileMobs.get(0);
			} else {
				target = null;
			}
		}
		
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
		if (stacks != null) {
			for (int i = 0; i < stacks.length; i++) {
				Item.dropItemStack(world, x, y, stacks[i]);
			}
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
	
	public boolean mayPass(Entity e) {
		if (e instanceof Mob) {
			return false;
		}
		return true;
	}
	
	public boolean isHostileTo(Entity e) {
		Class<? extends Entity>[] c = getHostileMobs();
		for (int i = 0; i < c.length; i++) {
			if (e.getClass() == c[i])
				return true;
		}
		return false;
	}
	
	public void renderToPlayerGui(Camera c) {
		float trans = 0.5f;
		healthBar.updateStatus((float) getHealth() / (float) getMaxHealth());
		healthBar.render(x - (64 / c.getZoom()), y + 50, Tile.yPosToDepth(y) + 500, 1 / c.getZoom(), trans);
		Font.drawString(name, x - ((8 / 1.5f) * 1/ c.getZoom()) * name.length(), y + 50 + (20 / (c.getZoom())), 1.0f, 1.0f, 1.0f, trans, Tile.yPosToDepth(y) + 500, (1 / c.getZoom()) / 1.5f);
	}
	
	public Inventory getInventory() {
		return inv;
	}

	protected AABB[] getAttackAreas() {
		AABB[] r = new AABB[4];
		int ar = getAttackRange();
		r[Facing.DOWN] = new AABB(x - 20, y - ar + 5, x + 20, y + 5);
		r[Facing.UP] = new AABB(x - 20, y + 5, x + 20, y + 5 + ar);
		r[Facing.LEFT] = new AABB(x - ar, y - 10, x, y + 20);
		r[Facing.RIGHT] = new AABB(x, y - 10, x + ar, y + 20);
		return r;
	}
	
	protected AABB getFacingAttackArea(int i) {
		return getAttackAreas()[i];
	}
	
	public EquipmentInventory getEquipmentInventory() {
		return eInv;
	}
}