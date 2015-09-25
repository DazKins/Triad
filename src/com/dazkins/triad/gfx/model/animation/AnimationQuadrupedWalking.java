package com.dazkins.triad.gfx.model.animation;

import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.game.entity.renderer.EntityRenderer;
import com.dazkins.triad.gfx.model.ModelQuadruped;
import com.dazkins.triad.gfx.model.Quad;
import com.dazkins.triad.math.MathHelper;

public class AnimationQuadrupedWalking extends Animation
{
	public AnimationQuadrupedWalking(EntityRenderer e, float s)
	{
		super(StorageAnimationID.QUADRUPED_WALKING, e, s);
	}

	public void updateState()
	{
		super.updateState();
		if (verifyModel(ModelQuadruped.class))
		{
			int f = eRenderer.getFacing();
			ModelQuadruped mod = (ModelQuadruped) parentModel;

			Quad cFrontLeftLeg = mod.getFrontLeftLeg()[f];
			Quad cFrontRightLeg = mod.getFrontRightLeg()[f];
			Quad cBackLeftLeg = mod.getBackLeftLeg()[f];
			Quad cBackRightLeg = mod.getBackRightLeg()[f];
			
			int tickTime = 60;

			float a = ((float) animationTicks / (float) tickTime) * 2.0f * MathHelper.PI * animSpeed;
			if (f == Facing.LEFT || f == Facing.RIGHT)
			{
				float maxSwing = 50.0f;
				cFrontLeftLeg.setRotation((float) Math.cos(a) * maxSwing);
				cFrontRightLeg.setRotation((float) -Math.cos(a) * maxSwing);
				cBackLeftLeg.setRotation((float) Math.sin(a) * maxSwing);
				cBackRightLeg.setRotation((float) -Math.sin(a) * maxSwing);
			} else if (f == Facing.UP)
			{
				cBackLeftLeg.setOffset(0, (float) (Math.sin(a) + 1.0f));
				cBackRightLeg.setOffset(0, (float) (-Math.sin(a) + 1.0f));
			} else if (f == Facing.DOWN)
			{
				cFrontLeftLeg.setOffset(0, (float) (Math.sin(a) + 1.0f));
				cFrontRightLeg.setOffset(0, (float) (-Math.sin(a) + 1.0f));
			}
			if (a >= Math.PI * 2)
			{
				super.stop();
			}
		}
	}
}