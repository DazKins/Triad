package com.dazkins.triad.gfx.model.animation;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.gfx.model.ModelHumanoid;

public class AnimationZombieSlashing extends AnimationHumanoidSlashing
{
	public AnimationZombieSlashing(Entity e, int t)
	{
		super(e, t);
	}

	public void updateState(Entity e)
	{
		super.updateState(e);
		ModelHumanoid m = (ModelHumanoid) parentModel;
		int f = e.getFacing();
		m.getLeftArm()[f].setRotation(m.getRightArm()[f].getRotation());
	}
}