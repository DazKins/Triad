package com.dazkins.triad.gfx.model.animation;

import com.dazkins.triad.game.entity.renderer.EntityRenderer;
import com.dazkins.triad.gfx.model.Model;

public abstract class Animation
{
	private int id;
	
	protected Model parentModel;

	protected int animationTicks;

	protected boolean modelOK;
	
	protected EntityRenderer eRenderer;
	
	protected float animSpeed;

	public Animation(int i, EntityRenderer e, float s)
	{
		eRenderer = e;
		id = i;
		animSpeed = s;
	}
	
	public void init(Model m)
	{
		parentModel = m;
	}
	
	public int getID()
	{
		return id;
	}

	public void stop()
	{
		parentModel.animationStop(this);
	}

	public boolean verifyModel(Class<? extends Model> m)
	{
		return m.isInstance(parentModel);
	}

	public void updateState()
	{
		animationTicks++;
	}
}