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
		return 2f;
	}

	public int getMaxHealth() {
		return 100;
	}
	
	public void tick() {
		super.tick();
		
		Model m = getModel();
		
		m.addAnimation(new AnimationHumanoidIdle(this), 0);
		
		if (Math.random() * 1000 > 999)
			xv = ((float) Math.random() - 0.5f) * getMovementSpeed();
		if (Math.random() * 1000 > 999)
			yv = ((float) Math.random() - 0.5f) * getMovementSpeed();
		
		xv *= 0.85;
		yv *= 0.85;
		
		if (xv > 0.6f || yv > 0.6f)
			m.addAnimation(new AnimationZombieWalking(this), 1);
		
		move(xv, yv);
		
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