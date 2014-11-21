package com.dazkins.triad.gfx.model.animation;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.gfx.model.Model;

public abstract class Animation {
	protected Model parentModel;
	
	protected long tickStart;
	protected int animationTicks;
	
	protected boolean modelOK;
	
	public Animation(Entity e) {
		tickStart = e.lifeTicks;
	}
	
	public void init(Model m) {
		parentModel = m;
	}
	
	public void updateState(Entity e) {
		animationTicks++;
	}
	
	public void stop() {
		parentModel.animationStop(this);
	}
	
	public boolean verifyModel(Class<? extends Model> m) {
		return m.isInstance(parentModel);
	}
}