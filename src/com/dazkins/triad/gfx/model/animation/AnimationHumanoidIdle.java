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

			final float fallSpeed = 2.5f;
			float crar = cRightArm.getRotation();
			if (crar != 0)
			{
				if (crar > 0)
				{
					if ((crar - fallSpeed) > fallSpeed)
						cRightArm.setRotation(crar - fallSpeed);
					else
						cRightArm.setRotation(0.0f);
				}
				
				if (crar < 0)
				{
					if ((crar + fallSpeed) < -fallSpeed)
						cRightArm.setRotation(crar + fallSpeed);
					else
						cRightArm.setRotation(0.0f);
				}
			}
			float clar = cLeftArm.getRotation();
			if (clar != 0)
			{
				if (clar > 0)
				{
					if ((clar - fallSpeed) > fallSpeed)
						cLeftArm.setRotation(clar - fallSpeed);
					else
						cLeftArm.setRotation(0.0f);
				}
				
				if (clar < 0)
				{
					if ((clar + fallSpeed) < -fallSpeed)
						cLeftArm.setRotation(clar + fallSpeed);
					else
						cLeftArm.setRotation(0.0f);
				}
			}
			float crlr = cRightLeg.getRotation();
			if (crlr != 0)
			{
				if (crlr > 0)
				{
					if ((crlr - fallSpeed) > fallSpeed)
						cRightLeg.setRotation(crlr - fallSpeed);
					else
						cRightLeg.setRotation(0.0f);
				}
				
				if (crlr < 0)
				{
					if ((crlr + fallSpeed) < -fallSpeed)
						cRightLeg.setRotation(crlr + fallSpeed);
					else
						cRightLeg.setRotation(0.0f);
				}
			}
			float cllr = cLeftLeg.getRotation();
			if (cllr != 0)
			{
				if (cllr > 0)
				{
					if ((cllr - fallSpeed) > fallSpeed)
						cLeftLeg.setRotation(cllr - fallSpeed);
					else	
						cLeftLeg.setRotation(0.0f);
				}
				
				if (cllr < 0)
				{
					if (cllr < 0 && (cllr + fallSpeed) < -fallSpeed)
						cLeftLeg.setRotation(cllr + fallSpeed);
					else	
						cLeftLeg.setRotation(0.0f);
				}
			}
			float crlo = cRightLeg.getOffsetY();
			float legFallSpeed = fallSpeed / 10.0f;
			if (crlo != 0)
			{
				if (crlo > 0)
				{
					if ((crlo - legFallSpeed) > legFallSpeed)
						cRightLeg.setOffset(0, crlo - legFallSpeed);
					else
						cRightLeg.setOffset(0.0f, 0.0f);
				}
				if (crlo < 0)
				{
					if ((crlo + legFallSpeed) < -legFallSpeed)
						cRightLeg.setOffset(0, crlo + legFallSpeed);
					else
						cRightLeg.setOffset(0.0f, 0.0f);
				}
			}
			float cllo = cLeftLeg.getOffsetY();
			if (cllo != 0)
			{
				if (cllo > 0)
				{
					if ((cllo - legFallSpeed) > legFallSpeed)
						cLeftLeg.setOffset(0.0f, cllo - legFallSpeed);
					else
						cLeftLeg.setOffset(0.0f, 0.0f);
				}
				if (cllo < 0)
				{
					if ((cllo + legFallSpeed) < -legFallSpeed)
						cLeftLeg.setOffset(0.0f, cllo + legFallSpeed);
					else
						cLeftLeg.setOffset(0.0f, 0.0f);
				}
			}
		}
	}
}