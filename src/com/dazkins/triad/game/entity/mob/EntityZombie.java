package com.dazkins.triad.game.entity.mob;

import com.dazkins.triad.game.inventory.item.Item;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.model.animation.AnimationZombieWalking;
import com.dazkins.triad.math.AABB;

public class EntityZombie extends Mob {
	public EntityZombie(World w, float x, float y) {
		super(w, x, y, "zombie", 100);
		sxa = this.getMovementSpeed();
	}

	public float getMovementSpeed() {
		return 0.3f;
	}

	public int getMaxHealth() {
		return 100;
	}
	
	float sxa = getMovementSpeed();
	
	public void tick() {
		super.tick();
		
		if (Math.random() * 100 > 99)
			sxa = -sxa;
		
		move(sxa, 0);
		
		if (!this.getModel().hasAnimation())
			this.getModel().setCurrentAnimation(new AnimationZombieWalking());
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