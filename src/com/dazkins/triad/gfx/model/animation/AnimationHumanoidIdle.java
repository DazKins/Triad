package com.dazkins.triad.gfx.model.animation;

import com.dazkins.triad.game.entity.renderer.EntityRenderer;
import com.dazkins.triad.gfx.model.ModelHumanoid;
import com.dazkins.triad.gfx.model.Quad;

public class AnimationHumanoidIdle extends Animation
{
	public AnimationHumanoidIdle(EntityRenderer e, float s)
	{
		super(StorageAnimationID.HUMANOID_IDLE, e, s);
	}

	public void updateState()
	{
		if (verifyModel(ModelHumanoid.class))
		{
			ModelHumanoid model = (ModelHumanoid) parentModel;
			
			int f = eRenderer.getFacing();

			Quad cRightArm = model.getRightArm()[f];
			Quad cLeftArm = model.getLeftArm()[f];
			Quad cRightLeg = model.getRightLeg()[f];
			Quad cLeftLeg = model.getLeftLeg()[f];

			final float fallTime = 2.5f;
			float crar = cRightArm.getRotation();
			if (crar != 0)
			{
				if (crar > 0 && (crar - fallTime) > -fallTime)
					cRightArm.setRotation(crar - fallTime);
				if (crar < 0 && (crar + fallTime) < fallTime)
					cRightArm.setRotation(crar + fallTime);
			}
			float clar = cLeftArm.getRotation();
			if (clar != 0)
			{
				if (clar > 0 && (clar - fallTime) > -fallTime)
					cLeftArm.setRotation(clar - fallTime);
				if (clar < 0 && (clar + fallTime) < fallTime)
					cLeftArm.setRotation(clar + fallTime);
			}
			float crlr = cRightLeg.getRotation();
			if (crlr != 0)
			{
				if (crlr > 0 && (crlr - fallTime) > -fallTime)
					cRightLeg.setRotation(crlr - fallTime);
				if (crlr < 0 && (crlr + fallTime) < fallTime)
					cRightLeg.setRotation(crlr + fallTime);
			}
			float cllr = cLeftLeg.getRotation();
			if (cllr != 0)
			{
				if (cllr > 0 && (cllr - fallTime) > -fallTime)
					cLeftLeg.setRotation(cllr - fallTime);
				if (cllr < 0 && (cllr + fallTime) < fallTime)
					cLeftLeg.setRotation(cllr + fallTime);
			}
			float crlo = cRightLeg.getOffsetY();
			if (crlo != 0)
			{
				if (crlo > 0)
					cRightLeg.setOffset(0, crlo - fallTime / 10.0f);
				if (crlo < 0)
					cRightLeg.setOffset(0, crlo + fallTime / 10.0f);
			}
			float cllo = cLeftLeg.getOffsetY();
			if (cllo != 0)
			{
				if (cllo > 0)
					cLeftLeg.setOffset(0, cllo - fallTime / 10.0f);
				if (cllo < 0)
					cLeftLeg.setOffset(0, cllo + fallTime / 10.0f);
			}
		}
	}
}