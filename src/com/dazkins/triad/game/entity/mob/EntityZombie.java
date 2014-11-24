package com.dazkins.triad.game.entity.mob;

import com.dazkins.triad.game.inventory.item.Item;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.model.Model;
import com.dazkins.triad.gfx.model.animation.AnimationHumanoidIdle;
import com.dazkins.triad.gfx.model.animation.AnimationZombieWalking;
import com.dazkins.triad.math.AABB;

public class EntityZombie extends Mob {
	public EntityZombie(World w, float x, float y) {
		super(w, x, y, "zombie", 100);
	}

	public float getMovementSpeed() {
		return 0.2f;
	}

	public int getMaxHealth() {
		return 100;
	}
	
	private float dirX = -1, dirY = -1;
	
	public void tick() {
		super.tick();
		
		Model m = getModel();
		
		m.addAnimation(new AnimationHumanoidIdle(this), 0);
		
		if (Math.random() * 1000 > 999) {
			int r = (int) (Math.random() * 3);
		}
		if (Math.random() * 1000 > 999)
			dirY *= -1;
		
		addXAMod(dirX * getMovementSpeed());
		addYAMod(dirY * getMovementSpeed());
		
		xa *= 0.75;
		ya *= 0.75;
		
		if (xa > 0.4f || ya > 0.4f)
			m.addAnimation(new AnimationZombieWalking(this), 1);
		
		super.move();
		
		m.updateAnimationState(this);
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