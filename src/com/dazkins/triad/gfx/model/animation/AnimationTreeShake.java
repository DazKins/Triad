package com.dazkins.triad.gfx.model.animation;

import com.dazkins.triad.game.entity.renderer.EntityRenderer;
import com.dazkins.triad.gfx.model.ModelTree;
import com.dazkins.triad.gfx.model.Quad;

public class AnimationTreeShake extends Animation
{
	public AnimationTreeShake(EntityRenderer e)
	{
		super(StorageAnimationID.TREE_SHAKE, e);
	}

	public void updateState()
	{
		if (verifyModel(ModelTree.class))
		{
			ModelTree m = (ModelTree) parentModel;
			Quad q = m.getQuad();

			q.setOffset((float) (Math.random() * 5) - 2.5f, (float) (Math.random() * 5) - 2.5f);

			if (animationTicks > 6)
			{
				q.setOffset(0, 0);
				stop();
			}
		}
	}
}