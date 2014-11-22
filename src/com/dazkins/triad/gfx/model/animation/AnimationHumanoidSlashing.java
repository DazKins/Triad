package com.dazkins.triad.gfx.model.animation;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.gfx.model.ModelHumanoid;
import com.dazkins.triad.gfx.model.Quad;

public class AnimationHumanoidSlashing extends Animation {
	public AnimationHumanoidSlashing(Entity e) {
		super(e);
	}

	public void updateState(Entity e) {
		super.updateState(e);
		if (verifyModel(ModelHumanoid.class)) {
			ModelHumanoid model = (ModelHumanoid) parentModel;
			int f = e.getFacing();
			
			Quad rArm = model.getRightArm()[f];
			
			if (f == Facing.LEFT) {
				rArm.setRotation(((float) Math.sin((animationTicks - 8) / 8.0f) * 90.0f) - 90.0f);
			}
			if (f == Facing.RIGHT) {
				rArm.setRotation(((float) Math.sin((animationTicks - 8) / 8.0f) * -90.0f) + 90.0f);
			}
			if (f == Facing.UP) {
				rArm.setRotation(((float) Math.sin((animationTicks) / 4.2f) * 30f) + 30.0f);
			}
			if (f == Facing.DOWN) {
				rArm.setRotation(((float) Math.sin((animationTicks) / 4.2f) * -30f) - 30.0f);
			}
		}
		if (((int) e.lifeTicks - tickStart) > 20) {
			stop();
		}
	}
}

