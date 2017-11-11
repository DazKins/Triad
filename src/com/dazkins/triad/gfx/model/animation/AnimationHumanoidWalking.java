package com.dazkins.triad.gfx.model.animation;

import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.game.entity.renderer.EntityRenderer;
import com.dazkins.triad.gfx.model.ModelHumanoid;
import com.dazkins.triad.gfx.model.Quad;
import com.dazkins.triad.math.MathHelper;

public class AnimationHumanoidWalking extends Animation
{
	public AnimationHumanoidWalking(EntityRenderer e, float s)
	{
		super(StorageAnimationID.HUMANOID_WALKING, e, s);
	}

	private float animSpeed = 1;

	@Override
	public void updateState()
	{
		super.updateState();

		float speedMod = animSpeed / 4.0f;
		if (verifyModel(ModelHumanoid.class))
		{
			ModelHumanoid model = (ModelHumanoid) parentModel;

			int f = eRenderer.getFacing();

			Quad cRightArm = model.getRightArm()[f];
			Quad cLeftArm = model.getLeftArm()[f];
			Quad cRightLeg = model.getRightLeg()[f];
			Quad cLeftLeg = model.getLeftLeg()[f];
			
			if (f == Facing.LEFT || f == Facing.RIGHT)
			{
				cRightArm.setRotation((float) Math.cos(animationTicks * speedMod) * (5.0f * MathHelper.PI / 18.0f));
				cLeftArm.setRotation((float) -Math.cos(animationTicks * speedMod) * (5.0f * MathHelper.PI / 18.0f));
				cRightLeg.setRotation((float) Math.sin(animationTicks * speedMod) * (5.0f * MathHelper.PI / 18.0f));
				cLeftLeg.setRotation((float) -Math.sin(animationTicks * speedMod) * (5.0f * MathHelper.PI / 18.0f));
			} else
			{
				cRightArm.setRotation((float) Math.cos((float) animationTicks * speedMod) * 15.0f);
				cLeftArm.setRotation((float) -Math.cos((float) animationTicks * speedMod) * 15.0f);
				cRightLeg.setOffset(0, (float) (Math.sin((float) animationTicks * speedMod) + 1.0f) * 3.0f);
				cLeftLeg.setOffset(0, (float) (-Math.sin((float) animationTicks * speedMod) + 1.0f) * 3.0f);
			}
		}
		if (animationTicks * speedMod > 2 * Math.PI)
		{
			stop();
		}
	}
}