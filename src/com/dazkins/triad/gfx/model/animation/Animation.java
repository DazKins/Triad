package com.dazkins.triad.gfx.model.animation;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.renderer.EntityRenderer;
import com.dazkins.triad.gfx.model.Model;

public abstract class Animation
{
	private int id;
	
	protected Model parentModel;

	protected int animationTicks;

	protected boolean modelOK;
	
	protected EntityRenderer eRenderer;

	public Animation(int i, EntityRenderer e)
	{
		eRenderer = e;
		id = i;
	}
	
	public void init(Model m)
	{
		parentModel = m;
	}
	
	public int getID()
	{
		return id;
	}

	//TODO fix this
	public void firstFrameInit(Entity e)
	{
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