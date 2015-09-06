package com.dazkins.triad.gfx.model.animation;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.gfx.model.ModelQuadruped;
import com.dazkins.triad.gfx.model.Quad;

public class AnimationQuadrupedWalking extends Animation {
	float animSpeed;

	public AnimationQuadrupedWalking(Entity e) {
		super(e);
	}
	
	public void firstFrameInit(Entity e) {
		animSpeed = ((Mob) e).getMovementSpeed() / 2.0f;
//		animSpeed = 0.125f;
	}
	
	public void updateState(Entity e) {
		super.updateState(e);
		if (verifyModel(ModelQuadruped.class)) {
			int f = e.getFacing();
			ModelQuadruped m = (ModelQuadruped) parentModel;
			
			Quad cFrontLeftLeg = m.getFrontLeftLeg()[f];
			Quad cFrontRightLeg = m.getFrontRightLeg()[f];
			Quad cBackLeftLeg = m.getBackLeftLeg()[f];
			Quad cBackRightLeg = m.getBackRightLeg()[f];
			
			if (f == Facing.LEFT || f == Facing.RIGHT) {
				cFrontLeftLeg.setRotation((float) Math.cos(animationTicks * animSpeed) * 50.0f);
				cFrontRightLeg.setRotation((float) -Math.cos(animationTicks * animSpeed) * 50.0f);
				cBackLeftLeg.setRotation((float) Math.sin(animationTicks * animSpeed) * 50.0f);
				cBackRightLeg.setRotation((float) -Math.sin(animationTicks * animSpeed) * 50.0f);
			} else if (f == Facing.UP) {
				cBackLeftLeg.setOffset(0, (float) (Math.sin((float) animationTicks * animSpeed) + 1.0f) * 1.0f);
				cBackRightLeg.setOffset(0, (float) (-Math.sin((float) animationTicks * animSpeed) + 1.0f) * 1.0f);
			} else if (f == Facing.DOWN) {
				cFrontLeftLeg.setOffset(0, (float) (Math.sin((float) animationTicks * animSpeed) + 1.0f) * 1.0f);
				cFrontRightLeg.setOffset(0, (float) (-Math.sin((float) animationTicks * animSpeed) + 1.0f) * 1.0f);
			}
		}
		if (e.getSpeed() < 0.01f) {
			stop();
		}
	}
}