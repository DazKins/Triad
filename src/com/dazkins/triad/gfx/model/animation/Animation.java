package com.dazkins.triad.gfx.model.animation;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.gfx.model.Model;

public abstract class Animation {
	protected Model parentModel;
	
	protected int tickStart;
	protected int animationTicks;
	
	protected boolean modelOK;
	
	public void init(Model m) {
		parentModel = m;
	}
	
	public abstract void updateState(Entity e);
	
	public void onStop() {
		parentModel.animationStop();
	}
	
	public void verifyModel(Class<? extends Model> m) {
		if (m.isInstance(parentModel))
			modelOK = true;
		else
			modelOK = false;
	}
}