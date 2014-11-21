package com.dazkins.triad.gfx.model.animation;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.game.entity.mob.EntityPlayer;
import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.game.entity.mob.MovementState;
import com.dazkins.triad.gfx.model.ModelHumanoid;
import com.dazkins.triad.gfx.model.Quad;

public class AnimationHumanoidWalking extends Animation {
	public AnimationHumanoidWalking(Entity e) {
		super(e);
	}
	
	public void updateState(Entity e) {
		if (verifyModel(ModelHumanoid.class)) {
			ModelHumanoid model = (ModelHumanoid) parentModel;
			Mob m = (Mob) e;
			
			int f = e.getFacing();
			
			Quad cHead = model.getHead()[f];
			Quad cRightArm = model.getRightArm()[f];
			Quad cLeftArm = model.getLeftArm()[f];
			Quad cRightLeg = model.getRightLeg()[f];
			Quad cLeftLeg = model.getLeftLeg()[f];
			Quad cBody = model.getBody()[f];
			
			if (f == Facing.LEFT || f == Facing.RIGHT) {
				cRightArm.setRotation((float) Math.cos(m.lifeTicks * (m.getMovementSpeed() / 9.0f)) * 50.0f);
				cLeftArm.setRotation((float) -Math.cos(m.lifeTicks * (m.getMovementSpeed() / 9.0f)) * 50.0f);
				cRightLeg.setRotation((float) Math.sin(m.lifeTicks * (m.getMovementSpeed() / 9.0f)) * 50.0f);
				cLeftLeg.setRotation((float) -Math.sin(m.lifeTicks * (m.getMovementSpeed() / 9.0f)) * 50.0f);
			} else {
				cRightArm.setRotation((float) Math.cos(m.lifeTicks * (m.getMovementSpeed() / 9.0f)) * 15.0f);
				cLeftArm.setRotation((float) -Math.sin(m.lifeTicks * (m.getMovementSpeed() / 9.0f)) * 15.0f);
				cRightLeg.setOffset(0, (float) (Math.sin(m.lifeTicks * (m.getMovementSpeed() / 9.0f)) + 1.0f) * 3.0f);
				cLeftLeg.setOffset(0, (float) (-Math.sin(m.lifeTicks * (m.getMovementSpeed() / 9.0f)) + 1.0f) * 3.0f);
			}
		}
		if (((int) e.lifeTicks - tickStart) > 30) {
			stop();
		}
	}
}