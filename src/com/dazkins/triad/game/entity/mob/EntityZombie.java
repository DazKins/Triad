package com.dazkins.triad.game.entity.mob;

import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.game.inventory.item.Item;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.gfx.model.Model;
import com.dazkins.triad.gfx.model.ModelZombie;
import com.dazkins.triad.gfx.model.animation.AnimationHumanoidIdle;
import com.dazkins.triad.gfx.model.animation.AnimationZombieSlashing;
import com.dazkins.triad.gfx.model.animation.AnimationZombieWalking;
import com.dazkins.triad.math.AABB;

public class EntityZombie extends Mob {
	public EntityZombie(World w, float x, float y) {
		super(w, x, y, "zombie", 100);
	}

	public float getMovementSpeed() {
		return 0.25f;
	}

	public int getMaxHealth() {
		return 100;
	}
	
	public void tick() {
		super.tick();
		
		model.addAnimation(new AnimationHumanoidIdle(this), 0, false);
		
		if (target != null) {
			float tx = target.getX() - this.getX();
			float ty = target.getY() - this.getY();
			if (Math.abs(ty) < 0.01f) ty = 0;
			float mag = (float) Math.sqrt(tx * tx + ty * ty);
			float xv = (tx / mag) * getMovementSpeed();
			float yv = (ty / mag) * getMovementSpeed();
			
			if (getFacingAttackArea(getFacing()).intersects(target.getAABB())) {
				if (attemptAttack(getFacingAttackArea(getFacing()))) {
					model.addAnimation(new AnimationZombieSlashing(this, getAttackCooldown()), 5, true);
				}
			}
			
			setFacingBasedOnVelocities(xv, yv);
			
			push(xv, yv);
		}
		
		xa *= 0.75;
		ya *= 0.75;
		
		if (getSpeed() > 0.0001f)
			model.addAnimation(new AnimationZombieWalking(this, getSpeed()), 1, false);
		
		super.move();
		
		model.updateAnimationState(this);
	}
	
	protected void initModel() {
		model = new ModelZombie(Image.getImageFromName("zombie"));
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
		return 30;
	}

	public AABB getAABB() {
		return new AABB(x - 8, y, x + 8, y + 10);
	}
	
	public ItemStack[] getItemsToDrop() {
		ItemStack[] stacks = new ItemStack[10];
		for (int i = 0; i < 10; i++)
			stacks[i] = new ItemStack(Item.testHelmet, 1);
		return stacks;
	}
	
	public Class<? extends Mob>[] getHostileMobs() {
		Class<? extends Mob>[] r = new Class[1];
		r[0] = EntityPlayer.class;
		return r;
	}
	
	public AABB getEnemyScanArea() {
		return new AABB(x - 256, y - 256, x + 256, y + 256);
	}
}