package com.dazkins.triad.game.entity.mob;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Font;
import com.dazkins.triad.gfx.model.ModelHumanoid;
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
		
		if (Math.random() * 1000 > 999)
			sxa = -sxa;
		
		move(sxa, 0);
		
		if (!this.getModel().hasAnimation())
			this.getModel().setCurrentAnimation(new AnimationZombieWalking());
	}

	public void render() {
		super.render(false);
		this.getModel().updateAnimationState(this);
		this.getModel().render(this);
	}

	public AABB getAABB() {
		return new AABB(x - 8, y, x + 8, y + 48);
	}
}