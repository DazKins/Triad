package com.dazkins.triad.gfx.model.animation;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.gfx.model.ModelHumanoid;
import com.dazkins.triad.gfx.model.Quad;

public class AnimationZombieWalking extends AnimationHumanoidWalking {
	public void updateState(Entity e) {
		super.updateState(e);
		
		Facing f = e.getFacing();
		int ordinal = f.ordinal();
		
		ModelHumanoid model = (ModelHumanoid) parentModel;
		
		Quad cRightArm = model.getRightArm()[ordinal];
		Quad cLeftArm = model.getLeftArm()[ordinal];
		
		if (f == Facing.LEFT) {
			cRightArm.setRotation(-90);
			cLeftArm.setRotation(-90);
		} else if (f == Facing.RIGHT) {
			cRightArm.setRotation(90);
			cLeftArm.setRotation(90);
		}
	}
}