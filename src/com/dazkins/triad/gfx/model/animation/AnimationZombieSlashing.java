package com.dazkins.triad.gfx.model.animation;

import com.dazkins.triad.game.entity.renderer.EntityRenderer;
import com.dazkins.triad.gfx.model.ModelHumanoid;

public class AnimationZombieSlashing extends AnimationHumanoidSlashing
{
	public AnimationZombieSlashing(EntityRenderer e, int t)
	{
		super(StorageAnimationID.ZOMBIE_SLASHING, e, t);
	}

	public void updateState()
	{
		ModelHumanoid m = (ModelHumanoid) parentModel;
		int f = eRenderer.getFacing();
		m.getLeftArm()[f].setRotation(m.getRightArm()[f].getRotation());
	}
}