package com.dazkins.triad.game.entity.mob;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.EntityIDStorage;
import com.dazkins.triad.game.entity.Interactable;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.gfx.model.ModelHumanoid;
import com.dazkins.triad.gfx.model.animation.AnimationHumanoidIdle;
import com.dazkins.triad.math.AABB;

public class EntityPlayerServer extends Mob {
	private Interactable interactingObject;
	
	public EntityPlayerServer(World w, float x, float y) {
		super(w, EntityIDStorage.PLAYER, x, y, "player", 1000);
		this.inv = new Inventory(9, 5);
	}
	
	public int getMaxHealth() {
		return 1000;
	}
	
	public Interactable getInteractingObject() {
		return interactingObject;
	}
	
	public void setInteractingObject(Interactable i) {
		interactingObject = i;
	}

	public void tick() {
		super.tick();
		
		move();
		
		xa *= 0.75;
		ya *= 0.75;
	}
	
	public boolean mayPass(Entity e) {
		return true;
	}
	
	protected int getBaseDamage() {
		return 5;
	}
	
	protected int getBaseKnockback() {
		return 5;
	}
	
	protected int getBaseAttackCooldown() {
		return 40;
	}
	
	protected int getBaseAttackRange() {
		return 50;
	}

	public AABB getAABB() {
		return new AABB(x - 8, y, x + 8, y + 10);
	}

	public float getMovementSpeed() {
		return 1f;
	}
}