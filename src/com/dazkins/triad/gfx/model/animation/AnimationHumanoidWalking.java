package com.dazkins.triad.gfx.model.animation;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.game.entity.mob.EntityPlayer;
import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.game.entity.mob.MovementState;
import com.dazkins.triad.gfx.model.ModelHumanoid;
import com.dazkins.triad.gfx.model.Quad;

public class AnimationHumanoidWalking extends Animation {
	public void updateState(Entity e) {
		verifyModel(ModelHumanoid.class);
		if (modelOK) {
			ModelHumanoid model = (ModelHumanoid) parentModel;
			Mob m = (Mob) e;
			
			int f = e.getFacing();
			
			Quad cHead = model.getHead()[f];
			Quad cRightArm = model.getRightArm()[f];
			Quad cLeftArm = model.getLeftArm()[f];
			Quad cRightLeg = model.getRightLeg()[f];
			Quad cLeftLeg = model.getLeftLeg()[f];
			Quad cBody = model.getBody()[f];
			
			if (m.getMovementState() == MovementState.MOVING) {
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
			} else {
				final float fallTime = 2.5f;
				float crar = cRightArm.getRotation();
				if (crar != 0) {
					if (crar > 0)
						cRightArm.setRotation(crar - fallTime);
					if (crar < 0)
						cRightArm.setRotation(crar + fallTime);
				}
				float clar = cLeftArm.getRotation();
				if (clar != 0) {
					if (clar > 0)
						cLeftArm.setRotation(clar - fallTime);
					if (clar < 0)
						cLeftArm.setRotation(clar + fallTime);
				}
				float crlr = cRightLeg.getRotation();
				if (crlr != 0) {
					if (crlr > 0)
						cRightLeg.setRotation(crlr - fallTime);
					if (crlr < 0)
						cRightLeg.setRotation(crlr + fallTime);
				}
				float cllr = cLeftLeg.getRotation();
				if (cllr != 0) {
					if (cllr > 0)
						cLeftLeg.setRotation(cllr - fallTime);
					if (cllr < 0)
						cLeftLeg.setRotation(cllr + fallTime);
				}
				float crlo = cRightLeg.getOffsetY();
				if (crlo != 0) {
					if (crlo > 0)
						cRightLeg.setOffset(0, crlo - fallTime / 10.0f);
					if (crlo < 0)
						cRightLeg.setOffset(0, crlo + fallTime / 10.0f);
				}
				float cllo = cLeftLeg.getOffsetY();
				if (cllo != 0) {
					if (cllo > 0)
						cLeftLeg.setOffset(0, cllo - fallTime / 10.0f);
					if (cllo < 0)
						cLeftLeg.setOffset(0, cllo + fallTime / 10.0f);
				}
			}
		}
	}
}