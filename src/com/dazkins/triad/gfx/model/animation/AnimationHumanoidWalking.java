package com.dazkins.triad.gfx.model.animation;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.gfx.model.ModelHumanoid;
import com.dazkins.triad.gfx.model.Quad;

public class AnimationHumanoidWalking extends Animation
{
	private float animSpeed;

	public AnimationHumanoidWalking(Entity e)
	{
		super(e);
	}

	public void firstFrameInit(Entity e)
	{
		animSpeed = ((Mob) e).getMovementSpeed() / 4.0f;
	}

	public void updateState(Entity e)
	{
		super.updateState(e);
		if (verifyModel(ModelHumanoid.class))
		{
			ModelHumanoid model = (ModelHumanoid) parentModel;

			int f = e.getFacing();

			Quad cRightArm = model.getRightArm()[f];
			Quad cLeftArm = model.getLeftArm()[f];
			Quad cRightLeg = model.getRightLeg()[f];
			Quad cLeftLeg = model.getLeftLeg()[f];

			if (f == Facing.LEFT || f == Facing.RIGHT)
			{
				cRightArm.setRotation((float) Math.cos(animationTicks * animSpeed) * 50.0f);
				cLeftArm.setRotation((float) -Math.cos(animationTicks * animSpeed) * 50.0f);
				cRightLeg.setRotation((float) Math.sin(animationTicks * animSpeed) * 50.0f);
				cLeftLeg.setRotation((float) -Math.sin(animationTicks * animSpeed) * 50.0f);
			} else
			{
				cRightArm.setRotation((float) Math.cos((float) animationTicks * animSpeed) * 15.0f);
				cLeftArm.setRotation((float) -Math.cos((float) animationTicks * animSpeed) * 15.0f);
				cRightLeg.setOffset(0, (float) (Math.sin((float) animationTicks * animSpeed) + 1.0f) * 3.0f);
				cLeftLeg.setOffset(0, (float) (-Math.sin((float) animationTicks * animSpeed) + 1.0f) * 3.0f);
			}
		}
		if (e.getSpeed() < 0.01f)
		{
			stop();
		}
	}
}