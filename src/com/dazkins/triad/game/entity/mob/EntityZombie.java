package com.dazkins.triad.game.entity.mob;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Font;
import com.dazkins.triad.gfx.model.ModelHumanoid;
import com.dazkins.triad.math.AABB;

public class EntityZombie extends Mob {
	public EntityZombie(World w, float x, float y) {
		super(w, x, y, "zombie", 1000);
		sxa = this.getMovementSpeed();
	}

	public float getMovementSpeed() {
		return 0.3f;
	}

	public int getMaxHealth() {
		return 1000;
	}
	
	float sxa = getMovementSpeed();
	
	public void tick() {
		super.tick();
		
		if (Math.random() * 100 > 99)
			sxa = -sxa;
		
		move(sxa, 0);
	}

	public void renderToPlayerGui() {
//		Font.drawStringWithShadow("health: " + health, x - (name.length() * 16) / 2, y + 52, 1.0f, 1.0f, 1.0f, 1, 1);
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