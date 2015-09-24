package com.dazkins.triad.gfx.model.animation;

import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.game.entity.renderer.EntityRenderer;
import com.dazkins.triad.gfx.model.ModelHumanoid;
import com.dazkins.triad.gfx.model.Quad;

public class AnimationZombieWalking extends AnimationHumanoidWalking
{
	public AnimationZombieWalking(EntityRenderer e)
	{
		super(e);
	}

	public void updateState()
	{
		int f = eRenderer.getFacing();

		ModelHumanoid model = (ModelHumanoid) parentModel;

		Quad cRightArm = model.getRightArm()[f];
		Quad cLeftArm = model.getLeftArm()[f];

		if (f == Facing.LEFT)
		{
			cRightArm.setRotation(-90);
			cLeftArm.setRotation(-90);
		} else if (f == Facing.RIGHT)
		{
			cRightArm.setRotation(90);
			cLeftArm.setRotation(90);
		} else
		{
			cRightArm.setRotation(0);
			cLeftArm.setRotation(0);
		}
	}
}