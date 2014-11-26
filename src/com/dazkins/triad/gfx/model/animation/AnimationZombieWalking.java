package com.dazkins.triad.gfx.model.animation;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.gfx.model.ModelHumanoid;
import com.dazkins.triad.gfx.model.Quad;

public class AnimationZombieWalking extends AnimationHumanoidWalking {
	public AnimationZombieWalking(Entity e, float a) {
		super(e, a);
	}

	public void updateState(Entity e) {
		super.updateState(e);
		
		int f = e.getFacing();
		
		ModelHumanoid model = (ModelHumanoid) parentModel;
		
		Quad cRightArm = model.getRightArm()[f];
		Quad cLeftArm = model.getLeftArm()[f];
		
		if (f == Facing.LEFT) {
			cRightArm.setRotation(-90);
			cLeftArm.setRotation(-90);
		} else if (f == Facing.RIGHT) {
			cRightArm.setRotation(90);
			cLeftArm.setRotation(90);
		} else {
			cRightArm.setRotation(0);
			cLeftArm.setRotation(0);
		}
	}
}