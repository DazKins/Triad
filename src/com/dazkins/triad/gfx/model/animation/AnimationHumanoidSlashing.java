package com.dazkins.triad.gfx.model.animation;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.gfx.model.ModelHumanoid;
import com.dazkins.triad.gfx.model.Quad;

public class AnimationHumanoidSlashing extends Animation
{
	private int time;

	public AnimationHumanoidSlashing(Entity e, int t)
	{
		super(e);
		time = t;
	}

	public void updateState(Entity e)
	{
		super.updateState(e);
		if (verifyModel(ModelHumanoid.class))
		{
			ModelHumanoid model = (ModelHumanoid) parentModel;
			int f = e.getFacing();

			Quad rArm = model.getRightArm()[f];

			float multiplier;

			if (time != 0)
				multiplier = 50 / time;
			else
				multiplier = 50;

			if (f == Facing.LEFT)
			{
				rArm.setRotation(((float) Math.sin(((animationTicks * multiplier) - 8) / 8.0f) * 90.0f) - 90.0f);
			}
			if (f == Facing.RIGHT)
			{
				rArm.setRotation(((float) Math.sin(((animationTicks * multiplier) - 8) / 8.0f) * -90.0f) + 90.0f);
			}
			if (f == Facing.UP)
			{
				rArm.setRotation(((float) Math.sin((animationTicks * multiplier) / 4.2f) * 30f) + 30.0f);
			}
			if (f == Facing.DOWN)
			{
				rArm.setRotation(((float) Math.sin((animationTicks * multiplier) / 4.2f) * -30f) - 30.0f);
			}

			if (Math.abs(rArm.getRotation()) < 10)
			{
				stop();
			}
		}
	}
}
