package com.dazkins.triad.gfx.model.animation;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.gfx.model.ModelTree;
import com.dazkins.triad.gfx.model.Quad;

public class AnimationTreeShake extends Animation {
	private float sX, sY;
	
	public AnimationTreeShake(Entity e) {
		super(e);
	}

	public void firstFrameInit(Entity e) {
		sX = e.getX();
		sY = e.getY();
	}

	public void updateState(Entity e) {
		super.updateState(e);
		
		if (verifyModel(ModelTree.class)) {
			ModelTree m = (ModelTree) parentModel;
			Quad q = m.getQuad();
			
			q.setOffset((float) (Math.random() * 5) - 2.5f, (float) (Math.random() * 5) - 2.5f);
			
			if (animationTicks > 6) {
				q.setOffset(0, 0);
				stop();
			}
		}
	}
}