package com.dazkins.triad.game.entity.mob;

import com.dazkins.triad.game.inventory.item.Item;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.gfx.model.Model;
import com.dazkins.triad.gfx.model.ModelZombie;
import com.dazkins.triad.gfx.model.animation.AnimationHumanoidIdle;
import com.dazkins.triad.gfx.model.animation.AnimationZombieWalking;
import com.dazkins.triad.math.AABB;

public class EntityZombie extends Mob {
	public EntityZombie(World w, float x, float y) {
		super(w, x, y, "zombie", 100);
	}

	public float getMovementSpeed() {
		return 0.05f;
	}

	public int getMaxHealth() {
		return 100;
	}
	
	private float dirX, dirY;
	
	public void tick() {
		super.tick();
		
		model.addAnimation(new AnimationHumanoidIdle(this), 0, false);
		
		if (Math.random() * 100 > 99) {
			int r = (int) (Math.random() * 3);
			if (r == 0) {
				dirX = getMovementSpeed();
			} else if (r == 1) {
				dirX = -getMovementSpeed();
			} else {
				dirX = 0;
			}
		}
		if (Math.random() * 100 > 99) {
			int r = (int) (Math.random() * 3);
			if (r == 0) {
				dirY = getMovementSpeed();
			} else if (r == 1) {
				dirY = -getMovementSpeed();
			} else {
				dirY = 0;
			}
		}
		
		addXAMod(dirX);
		addYAMod(dirY);
		
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
	
	public ItemStack[] getItemsToDrop() {
		ItemStack[] stacks = new ItemStack[10];
		for (int i = 0; i < 10; i++)
			stacks[i] = new ItemStack(Item.testHelmet, 1);
		return stacks;
	}

	public AABB getAABB() {
		return new AABB(x - 8, y, x + 8, y + 48);
	}
}