package com.dazkins.triad.game.entity.mob;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.model.ModelHumanoid;
import com.dazkins.triad.math.AABB;

public class EntityZombie extends Mob {
	public EntityZombie(World w, float x, float y) {
		super(w, x, y, "zombie", 1000);
	}

	public float getMovementSpeed() {
		return 0;
	}

	public int getMaxHealth() {
		return 1000;
	}

	public void renderToPlayerGui() {
		
	}

	public void render() {
		((ModelHumanoid)this.getModel()).render(this);
	}

	public AABB getAABB() {
		return null;
	}
	
}